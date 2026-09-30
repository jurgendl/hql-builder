# 07: Security

## 1. Context

- Everything runs **locally**. Images are built and used on the user's machine and never leave it
  (D8). There is no registry and no server deployment.
- Each environment runs in its own container, created from the **generic image**. The environment
  folder (model jars and property files, including the DB and UI credentials) is **bind-mounted
  read-only** at `/env`. Optionally `~/conf` is mounted read-only as well. In the optional **baked**
  mode the folder is copied into a local environment image instead ([11](11-docker-packaging.md)).
- The container publishes **one port** (Tomcat 8080 → host `127.0.0.1:<port>`). It serves the React
  SPA and the REST API. `HqlService` and HttpInvoker are never exposed.
- **Between the web frontend and the Java backend** sits a login with the single username and
  password from the environment's `security.properties`.

A web server removes the operating-system boundary the Swing client relied on, even on a single
machine. Any web page open in the same browser, and any other process or user on the machine, can
send it HTTP requests. The login plus the mitigations below close that gap.

## 2. Dangerous capabilities

| # | Capability | Where | Impact | Control |
|---|---|---|---|---|
| C1 | Arbitrary HQL including `update` / `delete` / `insert` | `RunQuery` (`executeUpdate`) | data modification | `capabilities.dml` / `read-only` |
| C2 | Plain SQL mode | `RunSqlQuery` | any SQL the DB user may run, DDL included | `capabilities.plain-sql` / `read-only`; a DB user with limited rights |
| C3 | Raw SQL statements | `HqlService.sql(String[])` | same as C2 | **not exposed** |
| C4 | **Groovy evaluation** (parameters, column scripts) | `GroovyCompiler.eval` | **code execution inside the container JVM**. It can read everything the container sees: `/env` (including credentials), `~/conf` if mounted, `/data`, and the network | `capabilities.groovy` |
| C5 | Save and delete entities | PATCH / DELETE | data modification | `capabilities.editing` / `read-only` |
| C6 | Information disclosure | connection info, system properties, schema DDL, `/system/environment`, exception details | reconnaissance | login; capabilities; secrets masked |
| C7 | File writes in favorites | `QueryFavoriteUtils` with an unsanitized name | path traversal inside the container | name validation; `/env` and `~/conf` are read-only mounts |
| C8 | Schema changes at startup | `hibernate.hbm2ddl.auto` from the user's (or imported) properties | `update`/`create` alters or wipes the schema | only `none`/`validate`, unless explicitly allowed (ConfigCheck + StartupValidator) |
| C9 | Legacy HttpInvoker | `hql-builder-webservice` | deserialization RCE | never part of the WAR |
| C10 | Arbitrary Spring beans | `hqlbuilder.extra-context` XML | code execution at startup | it is the user's own file in the user's own folder; same trust level as the model jar |

## 3. Threats and mitigations

| Threat | Example | Mitigation |
|---|---|---|
| Unauthenticated use by another local process or user | a script calls `http://127.0.0.1:8081/api/v1/queries` | **login required** for every `/api/**` call except login, `me` and health |
| LAN exposure | publishing with `-p 8081:8080` binds `0.0.0.0` (on Ubuntu this even bypasses ufw) | the scripts always use `-p 127.0.0.1:<port>:8080`; documented; the login protects anyway |
| CSRF from another website in the same browser | a form POST to `http://127.0.0.1:8081/api/v1/queries` | `SameSite=Strict` cookie, CSRF token header, JSON-only content type, no CORS |
| DNS rebinding | `evil.example` resolves to `127.0.0.1` after the page loads | `Host` allow-list (`localhost`, `127.0.0.1`, `[::1]`); cookies are bound to the real host name |
| Password guessing | brute force against the login | bcrypt; lockout after 5 failures for 5 minutes; audit log |
| Credentials in a baked image | an old environment image stays around | images stay local (D8); mounted mode keeps credentials out of images entirely; delete unused baked images and prune the build cache |
| Secrets in the image metadata (baked mode) | a password passed as `--build-arg` shows in `docker history` | secrets travel only as files through the named build context |
| Other users reading the environment folder (Ubuntu) | `db.properties` is world-readable | keep the folder user-only (`chmod 700`); `run-env.sh` runs the container with your uid so it can still read the files |
| Wrong environment | a DML statement runs against the production copy | name and colour in the header and tab title; `read-only` and capability flags; DML confirmation dialog |
| Compromised dependency / container escape | a vulnerable library, a Tomcat CVE | non-root user (or your own uid), no Docker socket, no extra capabilities, read-only mounts, default Tomcat apps removed, regular rebuilds of the generic image (`--pull`) |
| XSS turning into API calls | a malicious value stored in the DB is rendered in the grid | React escaping, no `dangerouslySetInnerHTML`, strict CSP (`default-src 'self'`) |

## 4. Required mitigations

### P1: with the first usable version

1. **Login**: one user from the environment's properties, `DelegatingPasswordEncoder`
   (`{bcrypt}`, or `{noop}` with a warning), JSON login/logout/me, session id change on login.
2. **Session cookie**: `HQLB_SESSION_<env>`, `HttpOnly`, `SameSite=Strict`, idle timeout (default 8 h).
3. **CSRF**: cookie-to-header token on POST/PUT/PATCH/DELETE (except login); `application/json` only.
4. **No CORS configuration** at all.
5. **Host header allow-list** (`hqlbuilder.security.allowed-hosts`, port ignored).
6. **Login throttling**: lockout after `max-failed-logins` for 5 minutes.
7. **Capabilities enforced server-side**. DML is detected from the translated statement type.
   `read-only` gives read-only sessions and connections.
8. **`hbm2ddl.auto` restricted** to `none`/`validate`, including values that arrive through imports.
9. **Favorites name validation**, and the resolved path must stay inside `/data/favorites/<project>`.
10. **Security headers**: CSP (`default-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; connect-src 'self'; frame-ancestors 'none'`),
    `X-Content-Type-Options: nosniff`, `Referrer-Policy: no-referrer`. The inline styles are for
    Mantine; verify in phase 1.
11. **Container hardening**:
    - non-root, and an arbitrary uid in group 0 works;
    - `/env` and `~/conf` are mounted **read-only**;
    - the default webapps are removed, the shutdown port is disabled, AJP is off, the server header
      is hidden, `maxPostSize` is limited.
12. **Loopback port publishing** in all scripts and docs.
13. **Secret hygiene**:
    - mounted mode is the default, so images hold no credentials;
    - baked mode uses files, never build args or `ENV`;
    - environment folders stay outside git;
    - `ConfigCheck` warns about `{noop}` passwords;
    - `*password*` / `*secret*` values are masked in logs, the banner and `/system/*`.
14. **Error hygiene**: problem+json carries the Hibernate or DB message, never a stack trace. The
    details go to the log.

### P3: before the editing features ship

15. **Audit log** (`hqlbuilder.audit` → stdout + `/data/logs/audit.log`): login, failed login,
    execute, export, patch, delete, with the statement (truncated), row count, duration and outcome.
16. **Confirmations** in the UI for DML statements, deletes and edits (UX, not a security boundary).

### Not needed (local only)

Multi-user management, roles, LDAP/OIDC, TLS, per-user favorites. If a user deliberately publishes
the port beyond loopback, the login still applies, but plain HTTP then exposes the password on the
network. The docs advise against it (09, Q24).

## 5. Where the credentials live

| Mode | DB credentials | UI password | In the image? |
|---|---|---|---|
| Mounted (default) | host files: the environment folder, or an imported `~/conf/*.properties` (as today for Swing) | environment folder (`{bcrypt}` recommended) | **no** |
| Baked (optional) | copied into the local environment image | same | yes. The image stays on the machine; delete it when you no longer need it |

In both modes, choose a DB account with only the rights you need. Use a read-only account for
production copies, and set `hqlbuilder.read-only=true` for those environments.

## 6. About Groovy

- Sandboxing Groovy (`SecureASTCustomizer`, blacklists) is **not** a security boundary.
- Anyone logged in to an environment with Groovy on can run code in that container, with access to
  what the container can read (C4). With a single, local, authenticated user this matches the
  Swing tool, where Groovy ran in the user's own JVM.
- Recommendation: keep it on for dev and test. Turn it off (`hqlbuilder.capabilities.groovy=false`,
  or `read-only`) for environments that point at sensitive databases. The typed parameter mode
  covers the common cases.

## 7. Dependencies and lifecycle

- Spring 5.3, Spring Security 5.8, Boot 2.7 and Tomcat 9 need CVE tracking: Dependabot, the OWASP
  dependency-check plugin (sketched in the root pom), and `npm audit`.
- **Rebuild the generic image regularly** (`build-image -SkipMaven` pulls the patched `tomcat:9.0-jre17`
  base), then re-create the containers. Mounted environments need nothing else.
- Remove the legacy `hql-builder-web` module (HttpInvoker) in phase 5, and plan the Jakarta / Boot 3 /
  Tomcat 10 track.

## 8. Checklist per phase

| Item | Phase |
|---|---|
| Login (single user, bcrypt/noop), JSON login/logout/me | 1 |
| Session cookie per environment, idle timeout | 1 |
| CSRF + JSON content type, no CORS | 1 |
| Host header allow-list, login throttling | 1 |
| Capability enforcement, read-only mode | 1 |
| `hbm2ddl.auto` restriction (ConfigCheck + StartupValidator, imports included) | 1 |
| Security headers / CSP | 1 |
| Container hardening (non-root / arbitrary uid, read-only mounts), loopback publishing | 1 |
| Secret hygiene (mounted default, masking, `{noop}` warning, no build args) | 1 |
| Security integration tests + container smoke test | 1 |
| Favorites name validation | 1 / 2 |
| Audit log, confirmations for DML/edit/delete | 3 |
| Dependency scanning in CI, image rebuild guidance | 1 |

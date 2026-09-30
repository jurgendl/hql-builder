# 09: Decisions and open questions

The decisions already made are at the top, in the **decision log**. Open questions follow, each
with its context, the options, a recommendation, and whether it blocks a phase. Once the owner
decides, add a row to the log and update the affected documents.

## Decision log

| ID | Date | Decision | Decided by | Affects |
|---|---|---|---|---|
| D1 | 2026-09-23 | Everything runs **locally, never on a (shared) server**. No multi-user mode, roles or SSO | owner | 03, 07, 08 |
| D2 | 2026-09-23 | Docker is the packaging and runtime: a WAR runs in a container, and the environment configuration comes from the user. D9 refines where the WAR is built; Q25 decides mounted versus baked configuration | owner | 03, 05, 11 |
| D3 | 2026-09-23 | Per environment the user supplies the **model jar(s)**, the **DB connection properties** and the **security properties**. Another set gives another environment | owner | 11 |
| D4 | 2026-09-23 | **Reuse the backend** as much as possible. **Only the frontend** moves to the web | owner | 03, 05 |
| D5 | 2026-09-23 | The frontend uses a web framework like **React** | owner | 03, 06 |
| D6 | 2026-09-23 | **Only the web port** is published from the container | owner | 03, 11 |
| D7 | 2026-09-23 | **Security between the web frontend and the Java backend**, with a username and password provided by the user | owner | 05 §9, 07 |
| D8 | 2026-09-23 | **Images never leave the user's machine.** The user builds them, rebuilds them when needed, and starts containers. No registry, no pushing | owner | 07, 11 |
| D9 | 2026-09-23 | Users have **JDK 17/21, Maven and Node 24** (mostly Windows, some Ubuntu). The WAR is built on the host with Maven, and Docker only packages and runs it. There is no Maven or Node build inside Docker | owner | 05 §12, 11 §3 |
| D10 | 2026-09-23 | The old `hql-builder-demo` (removed in `feb021df`, unmaintained) is **out of scope**: no demo module, no demo environment. Automated tests use a test-scope fixture only | owner | 05 §10, 06 §16, 08 |
| D11 | 2026-09-23 | Settings that host projects keep in Spring XML (packages to scan, extra SQL functions, project name, placeholder file locations) **move into the user's configuration** (the GISMO example in 11 §5) | owner | 05 §3, 11 §4-5 |

## Blocking phase 0 or 1

### Q25. Default configuration mode: mounted or baked?

- **Context:** the owner asked whether Docker can read the model jar and properties from the host,
  so a frequently changing model jar does not force an image rebuild.
- **Options:**
  (a) **mounted**: one generic image, the environment folder bind-mounted read-only at `/env`, and
  `docker restart` after a change;
  (b) **baked**: an environment image with a copy of the folder, rebuilt after every change;
  (c) both.
- **Recommendation:** (c), with **mounted as the default**. Mounted is faster to iterate with, keeps
  credentials out of images, and can reuse `~/conf`. Baked stays available for frozen snapshots
  (phase 2, P2-13).
- **Blocks:** P1-01 (spike), P1-12/13.

### Q26. What did "hqlbuilder.properties" in the remarks mean?

- **Interpretation in this plan:** `hqlbuilder.properties` is the **main, required** file of an
  environment. It holds the model and application settings (project, packages to scan, SQL
  functions, config imports, environment name and colour, capabilities). `db.properties` and
  `security.properties` are optional convention files. Because all keys are namespaced, **everything
  may also live in `hqlbuilder.properties` alone**.
- **Background:** the obsolete bootstrap described in `readme.apt` also used a file named
  `hqlbuilder.properties`, with `hibernate.configuration`, `hibernate.persistence.context`, `help`
  and `resource.location[n]` (a list of property files in priority order). The new
  `hqlbuilder.config.imports` plays the role of `resource.location[n]`, and the name fits that tradition.
- **Please confirm**, or say whether you meant: one single file only, a different name, the old
  `readme.apt` key names, or no such file.
- **Blocks:** P1-03, P1-15.

### Q2. Which backend framework runs inside the WAR?

- **Options:** (a) Spring Boot 2.7.18 as WAR (`SpringBootServletInitializer`) in Tomcat 9;
  (b) plain Spring MVC 5.3 WAR; (c) wait for the Jakarta / Boot 3 track.
- **Recommendation:** (a). Details in 03 §7.
- **Blocks:** P1-02.

### Q3. Where does the new code live?

- **Proposal:** Maven modules `hql-builder/hql-builder-webapp` (WAR) and `hql-builder/hql-builder-webui`
  (SPA jar), plus a `docker/` folder at the git root (Dockerfiles, scripts, example environment).
  `web-migration/` stays documentation-only.
- **Blocks:** P1-02, P1-12, P1-17.

### Q19. One container, or frontend and backend containers?

- **Recommendation:** one Tomcat container serving the SPA and the API on one port. The browser has
  to reach the API in any case, so an nginx frontend container would only proxy it and hide nothing
  (03 §3).
- **Please confirm** that this matches "only the frontend port exported".

### Q12. Real model jars and databases (partly answered)

- **Answered by the GISMO host config** (11 §5):
  - entities are found by package scan (`be.ugent.gismo.model`, `be.ugent.komodo.model`);
  - properties come from `gismo.services.default.properties` / `gismo.services.local.properties`
    on the classpath and `~/conf/gismo.services.properties` / `~/conf/gismo.db.properties`;
  - there are 19 extra SQL functions;
  - `project=GISMO`.
- **Still open:**
  - Which jars provide those packages and the `gismo.services.*.properties` files (a model jar, a
    services jar, both)?
  - Which runtime dependencies do they pull in?
  - Which database and driver is it (the functions such as `string_agg` and the bitmap functions
    suggest PostgreSQL)? Is the DB reachable from Docker (host, remote, VPN)?
  - Does `gismo.db.properties` use `localhost`?
  - Does any imported file set `hibernate.hbm2ddl.auto`?
- **Blocks:** the phase 1 exit criteria. The spike (P1-01) can already use the GISMO setup.

## Needed during phase 1 or 2

### Q4. Groovy parameter evaluation

- **Recommendation:** both Groovy and typed mode, with Groovy on by default and switchable per
  environment (07 §6).

### Q5. Favorites storage

- **Recommendation:** server-side in `/data` (a volume per environment) in the Swing XML format, with
  optional sharing with Swing (Q18).

### Q6. Legacy `hql-builder-web`

- **Recommendation:** leave it behind its profile and delete it in phase 5 (earlier if nobody uses
  the HttpInvoker server).

### Q7. UI component library: Mantine (recommended), MUI, or shadcn/ui + Radix.

### Q8. Editor: CodeMirror 6 (recommended) or Monaco.

### Q11. Languages: English and Dutch (recommended).

### Q13. Package manager: npm, bundled with the installed Node 24 (recommended).

### Q14. Joda-Time or `java.time` in the parameter helpers?

- **Recommendation:** Joda heuristics in Groovy mode; the expected type in typed mode.

### Q16. Base image and Java version

- **Recommendation:** the official `tomcat:9.0-jre17-temurin`. JRE 21 (`-JavaVersion 21`) is possible
  after testing Hibernate 5.6's ByteBuddy on Java 21. It must be Tomcat **9**, because Tomcat 10+
  cannot run a `javax` WAR.

### Q17. UI password format

- **Recommendation:** accept `{bcrypt}` and `{noop}`, with a warning on `{noop}`. `hash-password`
  helps create bcrypt values.

### Q18. Default for favorites persistence

- **Recommendation:** a named volume per environment by default; `-ShareSwingFavorites` bind-mounts
  `~/hqlbuilder/favorites` to share them with Swing.

### Q21. Runtime overrides

- **Resolved by mounted mode** (Q25): configuration changes need only a restart. OS environment
  variables still do not override environment-folder keys (11 §4.1).

### Q22. One user per environment, or several?

- **Recommendation:** one (D7). A list can be added later without API changes.

### Q23. Where do environment folders live?

- **Recommendation:** outside the repository, e.g. `%USERPROFILE%\hqlbuilder-envs\<name>`
  (`~/hqlbuilder-envs/<name>` on Ubuntu).

### Q24. HTTPS inside the container?

- **Recommendation:** no. `http://localhost` is a secure context and the port is bound to loopback.

### Q27. Automatic reload when a model jar changes?

- **Options:** (a) manual `docker restart` (or a `restart-env` script); (b) Tomcat `reloadable`
  context that watches the jars.
- **Recommendation:** (a). Reloading web apps with Hibernate, Groovy and Lucene tends to leak class
  loaders, and a restart takes only seconds.

## Needed later (phase 3 or later)

### Q9. Column scripts (F12)

- **Recommendation:** client-side JavaScript over JSON values, unless users need entity properties
  (then server-side Groovy behind the capability).

### Q10. When is Swing retired?

- **Recommendation:** deprecate it 2 releases after M5, and remove it with the Jakarta upgrade.

### Q15. Is multi-tab editing or query history in scope?

- **Recommendation:** phase 4, optional.

## Resolved or withdrawn

| Former question | Outcome |
|---|---|
| Q1 local versus shared server | D1 (local only) |
| Q20 build inside Docker versus a prebuilt WAR | D9 (built on the host with Maven) |

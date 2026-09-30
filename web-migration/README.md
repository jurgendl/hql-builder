# HQL Builder: Swing to Web migration plan

This folder holds the plan for replacing the Swing desktop client (`hql-builder-client`) with a
React web frontend on top of the existing Java backend. The backend runs locally in Docker, one
container per environment. It is a planning folder: no production code lives here. The code goes
into Maven modules under `hql-builder/` and a `docker/` folder at the git root (see
[03-target-architecture.md](03-target-architecture.md)).

| | |
|---|---|
| **Status** | Planning. No implementation started. |
| **Created** | 2026-09-23. Revised twice the same day after the owner's remarks: first on local Docker packaging, then on local images, the host toolchain, mounted environments, no demo, and the GISMO host setup |
| **Based on commit** | `278fddca` (branch `master`, version `6.0.6-SNAPSHOT`) |
| **Owner** | jurgendl |

## Requirements from the owner

These are decisions D1-D11 in [09](09-open-questions.md).

1. Everything runs **locally**, never on a server. **Images never leave the user's machine.** The
   user builds them, rebuilds them when needed, and starts containers.
2. Per environment the user provides the **model jar(s)**, the **database properties** and the
   **security properties** (username and password). Other inputs give another environment.
3. **Reuse the backend.** **Only the frontend** moves to the web (React). Only the web port is
   published, and a login protects the backend.
4. Users already have **JDK 17/21, Maven and Node 24** (mostly Windows, some Ubuntu). The WAR is
   built on the host.
5. Preferably Docker **reads the model jar and properties from the host**, so a frequently changing
   model jar needs no image rebuild.
6. Settings from the host projects' Spring XML (packages to scan, SQL functions, ...) move into the
   user's configuration.
7. The old demo module is unmaintained and **out of scope**.

## Reading order

| # | Document | What it answers |
|---|---|---|
| 01 | [Current architecture](01-current-architecture.md) | How the code works today, including a real host project (GISMO) |
| 02 | [Feature inventory](02-feature-inventory.md) | Every Swing feature and what happens to it. **This is the parity checklist.** |
| 03 | [Target architecture](03-target-architecture.md) | Generic image plus mounted environment, what is reused, how the WAR boots, technology choices |
| 11 | [Docker packaging and environments](11-docker-packaging.md) | **The user's workflow:** build the image, environment folder and keys, the GISMO mapping, `collect-libs`, `check-env`, `run-env`, updates, baked mode, troubleshooting |
| 04 | [REST API](04-rest-api.md) | Endpoints, DTOs, auth, the error model, the `HqlService` mapping |
| 05 | [Backend plan](05-backend-plan.md) | The WAR module, `/env` config loading, SQL functions, Tomcat integration, ConfigCheck, result mapping, security, tests, build |
| 06 | [Frontend plan](06-frontend-plan.md) | React app: login, editor, grid, panels, i18n, shortcuts, tests |
| 07 | [Security](07-security.md) | Threat model and mitigations: login, CSRF, capabilities, where the credentials live |
| 08 | [Roadmap](08-roadmap.md) | Phases (starting with a spike), task checklists, exit criteria, risks |
| 09 | [Decisions and open questions](09-open-questions.md) | Decision log D1-D11 and the open questions (**Q25, Q26 and Q19 need an answer**) |
| 10 | [Existing issues](10-existing-issues.md) | Bugs found in the current code, including the SQL functions that are never registered |

(Document 11 was added later, but it belongs right after 03.)

## Summary

**Today.** A Swing app (`hql-builder-client`, `HqlBuilderFrame` alone is 3.4k lines) is started by
a host project. The host project's `main` calls `HqlBuilderFrame.start(args, new HqlServiceClientLoaderImpl())`.
Its `applicationContext.xml` sets up placeholders from property files, `packagesToScan`, extra SQL
functions and the import of `spring-service4-hibernate5-context.xml`. Its Maven dependencies put the
model and driver on the classpath.

**Target.**

```
once per hql-builder version                    per environment (host folder, no image rebuild)
┌─────────────────────────────────────┐         ┌──────────────────────────────────────────────┐
│ mvn package (JDK 17/21, Node 24)    │         │ C:\hqlbuilder-envs\gismo-dev\                │
│   → generic hql-builder-webapp.war  │         │   lib\  model/services jars, JDBC driver     │  ← collect-libs (Maven)
│ docker build → hql-builder:latest   │         │   hqlbuilder.properties  project, packages,  │
│   (tomcat:9.0-jre17 + WAR)          │         │        SQL functions, imports of ~/conf/*.   │
└──────────────────┬──────────────────┘         │   db.properties / security.properties        │
                   │                            └──────────────────────┬───────────────────────┘
                   └──────────────► docker run (run-env) ◄─────────────┘  bind mount → /env (read-only)
                                      container hql-builder-gismo-dev, 127.0.0.1:8081 → 8080, volume /data
                                      browser → http://localhost:8081 → login
                                      new model jar → copy into lib\ → docker restart (no rebuild)
```

- **Backend:** the generic WAR `hql-builder-webapp` (Spring Boot 2.7 as `SpringBootServletInitializer`,
  in Tomcat 9 because the stack is `javax`). It imports the **unchanged**
  `spring-service4-hibernate5-context.xml` and uses `HqlServiceImpl` and the Hibernate adapters as they
  are. It adds a JSON REST API, DTO mapping, security, and configuration loading from `/env`. Tomcat
  loads `/env/lib/*.jar` into the web-app class loader.
- **Configuration:** `hqlbuilder.properties` (plus the optional `db.properties` and
  `security.properties`) replaces the host project's XML. `hqlbuilder.config.imports` reuses existing
  files such as `~/conf/gismo.db.properties` unchanged. The GISMO mapping is worked out in
  [11 §5](11-docker-packaging.md#5-worked-example-the-gismo-host-project).
- **Frontend:** `hql-builder-webui`, a React + TypeScript + Vite SPA built with the installed Node 24
  (CodeMirror 6, TanStack Table, Mantine), served by the same WAR on the same port.
- **Environments:** one folder and one container each. They run side by side on different ports,
  each labelled with its name and colour, each with its own capabilities (read-only for a production copy).
- **Security:** a single-user login per environment (bcrypt recommended), session cookie, CSRF, host
  check, lockout, server-side capabilities. The port is published on `127.0.0.1`. **In mounted mode
  the image contains no credentials.**
- **Optional baked mode:** an environment image that contains a copy of the folder, for a frozen snapshot.

**The hard part is the wire format.** `ExecutionResult` carries live Hibernate entities. The web
backend maps them to typed JSON cells and entity references **while the session is open**
([04](04-rest-api.md), [05](05-backend-plan.md)).

**Found along the way:** the host-configured SQL functions (`AdditionalSqlFunctionsPostProcessor`) are
**never registered**, because the post-processor calls in `LocalSessionFactoryBeanPlus5` are commented
out. Phase 0 fixes it ([10](10-existing-issues.md) #29).

**Coexistence.** The Swing client keeps working. The two UIs can share favorites and the `~/conf` files.

**Phases** (details in [08](08-roadmap.md)):

0. Groundwork: shared logic moved to common, session and cancel fixes, SQL functions fixed, a test fixture.
1. A spike on Tomcat class loading and mounts, then the WAR, the `/env` config, security, the generic image
   and scripts, and the read-only MVP.
2. Productivity: autocomplete, favorites, wizard, named queries, search, settings, i18n, CSV, optional baked mode.
3. Editing and exploration.
4. Parity and polish.
5. Optional: retire Swing and the legacy module, then start the Jakarta track.

## Proposed key decisions

Rows marked **D** were decided by the owner. The others are proposals until they are confirmed in
[09](09-open-questions.md).

| Topic | Decision / proposal |
|---|---|
| Deployment (D1, D8) | local only; images are built and used locally, never pushed |
| Build (D9) | the WAR is built on the host (Maven, JDK 17/21, Node 24); Docker only packages and runs it |
| Environment inputs (D3, D11) | `lib/` + `hqlbuilder.properties` (+ `db.properties`, `security.properties`); imports of existing property files |
| Configuration mode (Q25) | **mounted by default** (generic image, read-only `/env` bind mount, restart after changes); baked optional |
| Frontend (D5) | React 19 + TypeScript + Vite, Mantine, TanStack Query, Zustand, CodeMirror 6, TanStack Table |
| Backend (D4) | reuse `HqlServiceImpl`, the adapters and the service context XML; WAR module with Spring Boot 2.7 (Q2) |
| Runtime | `tomcat:9.0-jre17-temurin`, non-root / any uid, one published port on 127.0.0.1 (D6) |
| Auth (D7) | single user per environment; JSON login, session cookie, CSRF |
| Demo (D10) | out of scope; a test-scope fixture only |
| Favorites | `/data` volume per environment, Swing XML format; optionally shared with Swing |

## Maintaining these docs

- Record decisions in the decision log in [09](09-open-questions.md), and update the affected documents.
- Tick checkboxes in [08-roadmap.md](08-roadmap.md) and the status column of
  [02-feature-inventory.md](02-feature-inventory.md) as work lands.
- File and line references are valid as of commit `278fddca`; prefer method names when they drift.
- Paths are relative to the git root unless they are Markdown links.

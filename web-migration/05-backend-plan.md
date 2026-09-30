# 05: Backend plan (Java)

This document covers the Java work: refactors in the existing modules (phase 0) and the new
`hql-builder-webapp` WAR module (phase 1 and later). It assumes the recommendations in
[03](03-target-architecture.md):

- Spring Boot 2.7.18 packaged as WAR, in Tomcat 9 (`javax`), Java 17 (21 optional);
- one generic image, with each environment read from `/env` (bind mount or baked, [11](11-docker-packaging.md)).

## 1. Guiding rules

1. **Reuse, don't rewrite.** `HqlServiceImpl`, the Hibernate adapters and
   `spring-service4-hibernate5-context.xml` are used as they are.
2. **Do not break the Swing client.** Changes in `common`, `service` and `hibernate/*` keep the
   Swing behavior, or fix it where it is broken (S12).
3. **Do not change the `HqlService` interface** (the HttpInvoker contract).
4. **Keep Hibernate-version-specific code in the adapters.**
5. **Never serialize entities.** Only DTOs built by mappers leave the backend.
6. **The generic WAR contains no environment.** Model classes, the driver and all configuration
   come from `${hqlbuilder.env.dir}` (default `/env`) at startup.

## 2. Phase 0: refactors in existing modules

### 2.1 Move UI-independent logic out of `hql-builder-client`

There is no `module-info.java`, so classes can move to another jar and keep their FQN.

| Class / logic | From | To | Notes |
|---|---|---|---|
| `cleanupSql`, `makeMultiline`, `removeBlanks`, `keywordGroups`, `hqlReplacers` | `HqlServiceClientImpl` | new `org.tools.hqlbuilder.common.SqlFormatter`; `HqlServiceClientImpl` delegates | replace `org.swingeasy.ObjectWrapper` with `org.jhaws.common.lang.ObjectWrapper` |
| `QueryFavorite`, `QueryFavoriteUtils` | client | common (same package) | add `jhaws-io` to common; a configurable base directory; fix the delete bug and sanitize names (see 10) |
| `HqlBuilderFrame.convertParameterString` | client | `org.tools.hqlbuilder.common.ParameterImport` | |
| `PathResolver` | client | stays | ported to TypeScript (06 §9) |
| `HqlServiceClient(Impl)` | client | stays | Swing only (the GISMO host config defines it; the web version does not need it) |

Tests to add while moving:

- `SqlFormatterTest`: characterization tests. `CleanupSqlTest` has no assertions and no `@Test`.
- `QueryFavoriteUtilsTest`: v1, v2 and v3 favorite files.
- `ParameterImportTest`: JSON object, Groovy map, list.

### 2.2 `HqlServiceImpl`, `LocalSessionFactoryBeanPlus5` and friends

| # | Change | Why | Swing impact |
|---|---|---|---|
| S1 | `latches` → `ConcurrentHashMap`; add `Map<String, Session> runningSessions` | concurrent requests | none |
| S2 | `stopQuery(uuid)`: `session.cancelQuery()` (JDBC `Statement.cancel()`), then release the latch | today the DB query and worker thread keep running | improvement |
| S3 | New overload `<R> R execute(QueryParameters qp, ResultHandler<R> handler)`, where `ResultHandler<R> { R handle(ExecutionResult r, Session s); }` runs **while the session is open**, and the session is **always closed** afterwards | web mapping needs the open session; no leak | none (new method) |
| S4 | `execute(QueryParameters)` keeps its behavior (session left open), documented; optional flag `closeSessionAfterExecute` (default `false`) | Swing in-process mode relies on attached entities | none |
| S5 | Optional query timeout (`Query#setTimeout`) in `RunQuery` | protects the DB | default off |
| S6 | DML detection (`update`/`delete`/`insert`) in every `RunQuery` copy | `delete from X` goes through `list()` today | bug fix |
| S7 | `sql(String[])`: try-with-resources | connection leak | bug fix |
| S8 | `save()`/`delete()`: close the session; `flush()` before `commit()` | leak, wrong order | small |
| S9 | `GroovyCompilerInstance`: `System.out.println` → `logger.debug` | parameter values in `docker logs` | none |
| S10 | Update `commonconfig.properties` help URLs per adapter | links point to 4.3 docs | improvement |
| S11 | `LocalSessionFactoryBeanPlus5.setHibernateConfigLocation`: ignore **blank** values | the webapp defaults `hibernate.cfg.xml` to empty | none |
| S12 | `LocalSessionFactoryBeanPlus5.buildSessionFactory(sfb)`: call `postProcessMappings(sfb)` and `postProcessConfiguration(sfb)` before `super.buildSessionFactory(sfb)`. `LocalSessionFactoryBuilder` *is* a `Configuration`, so the commented-out code only needs to pass `sfb` | **`ConfigurationPostProcessor` beans such as `AdditionalSqlFunctionsPostProcessor` are never invoked today**, so host-configured SQL functions (GISMO: 19 functions) are silently ignored (10 #29) | **bug fix**: Swing hosts get their SQL functions registered as intended. Verify with the GISMO config that nothing relied on them being absent |
| S13 | `AdditionalSqlFunctionsPostProcessor`: use the defaulted `type` (`object`) for the lookup, not the raw value; fail with a clear message on an unknown type name | the default is computed but not used, and an unknown type gives a `null` type silently (10 #30) | bug fix |

`$$` expansion with a `ResultHandler` (S3): run the sub-queries in **one** session, merge the
results, and call the handler once.

### 2.3 Entity introspection per Hibernate version

```java
public interface EntityIntrospector {
    String entityName(Session s, Object entityOrProxy);          // unwraps proxies without initializing
    Serializable identifier(Session s, Object entityOrProxy);
    Class<?> identifierType(SessionFactory sf, String entityName);
    List<PropertyMeta> properties(SessionFactory sf, String entityName); // id, version, mapped props: kind/type/nullable
    boolean isInitialized(Object value);
    Object load(Session s, String entityName, Serializable id);
}
```

One implementation per adapter (the 5.x adapters share one), loaded through `ServiceLoader`.

## 3. The new module `hql-builder-webapp` (packaging `war`)

### 3.1 `pom.xml` (sketch)

```xml
<artifactId>hql-builder-webapp</artifactId>
<packaging>war</packaging>
<build><finalName>hql-builder-webapp</finalName></build>   <!-- target/hql-builder-webapp/ (exploded) is what the image copies -->
<dependencyManagement>
  <!-- the jhaws BOMs come FIRST (root pom); import the Boot BOM after them so Spring stays 5.3.39 -->
  <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-dependencies</artifactId>
    <version>2.7.18</version><type>pom</type><scope>import</scope></dependency>
</dependencyManagement>
<dependencies>
  hql-builder-service, hql-builder-common
  hibernate-${version.hibernate}                           (the adapter; -Dversion.hibernate, default 5.6)
  hql-builder-webui                                        (runtime: the SPA assets)
  spring-boot-starter-web (exclude spring-boot-starter-tomcat) + spring-boot-starter-tomcat (provided)
  spring-boot-starter-security, spring-boot-starter-validation, spring-boot-starter-actuator (health only)
  jackson-datatype-jsr310, jackson-datatype-joda, jackson-datatype-jdk8
  org.springdoc:springdoc-openapi-ui:1.8.0
  test: spring-boot-starter-test, spring-security-test, h2, hql-builder-service:test-jar (fixture entities, §10)
</dependencies>
```

- The WAR contains **no JDBC driver and no model**.
- The build writes `WEB-INF/classes/hqlbuilder/provided-artifacts.txt` (groupId:artifactId of every
  jar in `WEB-INF/lib`, for example from `maven-dependency-plugin:list` plus a small transformation).
  `collect-libs` and `ConfigCheck` use it to spot duplicates in `/env/lib`.
- Check with `mvn dependency:tree` that the Boot BOM does not downgrade Hibernate, Jackson or Groovy.

### 3.2 Package layout

```
org.tools.hqlbuilder.webapp
├── HqlBuilderWebApplication      extends SpringBootServletInitializer; @SpringBootApplication(exclude = {
│                                 DataSourceAutoConfiguration, HibernateJpaAutoConfiguration, DataSourceTransactionManager...,
│                                 UserDetailsServiceAutoConfiguration}); @ImportResource(webapp-context.xml);
│                                 onStartup(): session cookie name/timeout; main() for IDE runs
├── env/
│   ├── EnvironmentLocator          resolves hqlbuilder.env.dir; lists files and /env/lib jars (for the banner and /system/environment)
│   ├── EnvironmentPropertySources  ApplicationContextInitializer: defaults < imports < db < hqlbuilder < security (§3.5)
│   ├── SqlFunctionsConfig          @Configuration: AdditionalSqlFunctionsPostProcessor from raw hqlbuilder.sql-functions.* keys
│   ├── ExtraContextInitializer     loads hqlbuilder.extra-context XML into the context before refresh
│   └── StartupValidator            required keys, safe hbm2ddl, entity discovery; the banner
├── config/
│   ├── HqlBuilderProperties        @ConfigurationProperties("hqlbuilder"): environment, capabilities, limits, security, data dir
│   ├── SecurityConfig              filter chain (§9)
│   ├── HostHeaderFilter, LoginAttemptService
│   ├── WebConfig                   SPA fallback (non-/api GET → index.html), cache headers
│   └── OpenApiConfig
├── api/                            thin @RestControllers per area in 04: Auth, Health, Info, Meta, Hql, Parameter,
│   │                               Query, Entity, Favorite, System
│   └── ProblemAdvice               exception → problem+json
├── dto/                            Java 17 records mirroring 04
├── service/                        CapabilityService, QueryExecutionService, QueryRegistry, ResultMapper/CellMapper,
│                                   CsvExportService, EntityViewService, EntityEditService, ParameterCompiler,
│                                   FavoritesService, GraphMapper, MetadataCache, AuditLogger, LatestVersionService
└── tools/
    └── ConfigCheck                 main(): run by `hqlb-entrypoint check` (check-env, run-env, baked image build)
```

### 3.3 Spring context (`org/tools/hqlbuilder/webapp/webapp-context.xml`)

```xml
<beans ...>
  <!-- reused unchanged: dataSource, sessionFactory, txManager, configurationBean, hqlService (+MethodLogger), hqlServiceImpl -->
  <import resource="classpath:org/tools/hqlbuilder/service/spring-service4-hibernate5-context.xml"/>

  <!-- the host projects' <util:list id="packagesToScan"> now comes from hibernate.packagesToScan -->
  <bean id="packagesToScan" class="org.springframework.util.StringUtils"
        factory-method="commaDelimitedListToStringArray">
    <constructor-arg value="${hibernate.packagesToScan:}"/>
  </bean>
</beans>
```

- `SqlFunctionsConfig` adds the `AdditionalSqlFunctionsPostProcessor` bean. `LocalSessionFactoryBeanPlus5`
  finds it by type (with S12 in place).
- `ExtraContextInitializer` adds the user's optional XML.
- The webapp looks up `HqlServiceImpl` by type, the `SessionFactory` through
  `hqlServiceImpl.getSessionFactory()`, and `hqlService` (the `MethodLogger` proxy) by name.

### 3.4 Built-in defaults (`hqlbuilder/defaults.properties`)

```properties
# placeholders of spring-service4-hibernate5-context.xml
hibernate.cfg.xml=
hibernate.packagesToScan=
hibernate.hbm2ddl.auto=none
hibernate.jdbc.batch_size=0
hibernate.query.substitutions=
hibernate.validator.autoregister_listeners=false
hibernate.search.default.directory_provider=local-heap
hibernate.search.default.indexBase=/tmp/hqlbuilder-index
hibernate.search.autoregister_listeners=false
project=${hqlbuilder.environment.name}

# web application
hqlbuilder.environment.color=#1971c2
hqlbuilder.data-dir=/data
hqlbuilder.read-only=false
hqlbuilder.capabilities.dml=true
hqlbuilder.capabilities.plain-sql=true
hqlbuilder.capabilities.groovy=true
hqlbuilder.capabilities.editing=true
hqlbuilder.capabilities.schema-script=false
hqlbuilder.capabilities.system-properties=true
hqlbuilder.limits.max-results=5000
hqlbuilder.limits.query-timeout-seconds=0
hqlbuilder.limits.csv-max-rows=1000000
hqlbuilder.limits.max-concurrent-queries=4
hqlbuilder.security.session-timeout-minutes=480
hqlbuilder.security.max-failed-logins=5
hqlbuilder.security.allowed-hosts=localhost,127.0.0.1,[::1]
hqlbuilder.allow-schema-changes=false
management.endpoints.web.exposure.include=health
```

The user-facing keys are documented in [11 §4](11-docker-packaging.md#4-the-environment-folder).

### 3.5 Loading the environment configuration

`EnvironmentPropertySources` is an `ApplicationContextInitializer`, registered in
`SpringBootServletInitializer.configure(builder.initializers(...))`. It runs before any bean is created:

1. `envDir = System.getProperty("hqlbuilder.env.dir", "/env")`. The image sets it in `setenv.sh`;
   an IDE run sets it to a host folder.
2. It reads `hqlbuilder.properties` (required), plus `db.properties` and `security.properties` when
   they exist.
3. It resolves `hqlbuilder.config.imports`, a comma-separated list:
   - placeholders are resolved first (`${user.home}` against the system properties);
   - `classpath:` resolves through the web-app class loader, which includes `/env/lib`;
   - `file:` resolves inside the container;
   - missing resources are skipped and logged.
4. Priority, highest first: `security` > `hqlbuilder` > `db` > imports (the last one wins) > defaults.
   All of them are added **above** `systemEnvironment` and `systemProperties`, so a stray OS
   variable cannot change the DB target. The exception is `hqlbuilder.env.dir`, which is read
   directly.
5. It records the origin of every required key, for the banner, `ConfigCheck` and `/system/environment`.
   Keys matching `*password*|*secret*` are masked.

SQL functions: `SqlFunctionsConfig` reads the **raw** keys that start with `hqlbuilder.sql-functions.`
by walking the environment's `EnumerablePropertySource`s. It does not use Boot's relaxed `Binder`,
which lowercases and normalizes map keys (`TO_STRING_FIRST_UPPER` must keep its case, because
`StandardSQLFunction` renders that name).

### 3.6 Tomcat integration (`src/main/webapp/META-INF/context.xml`)

```xml
<Context>
  <Resources>
    <!-- the environment's jars appear as WEB-INF/lib entries of the web-app class loader -->
    <PostResources className="org.apache.catalina.webresources.DirResourceSet"
                   base="/env/lib" webAppMount="/WEB-INF/lib"/>
  </Resources>
  <CookieProcessor className="org.apache.tomcat.util.http.Rfc6265CookieProcessor" sameSiteCookies="strict"/>
  <JarScanner>
    <JarScanFilter defaultTldScan="false" defaultPluggabilityScan="false" pluggabilityScan="spring-web*.jar"/>
  </JarScanner>
</Context>
```

- `/env/lib` always exists in the image (it is empty until something is mounted or baked in). The
  run and check scripts refuse an environment folder without `lib/`.
- **Phase 1 starts with a spike** that verifies, on Windows (Docker Desktop) and Ubuntu:
  - `PostResources` class loading (entities found, annotations visible);
  - that the SCI discovery of Spring still works with the JarScanFilter;
  - bind-mount reads;
  - an arbitrary uid (`--user <uid>:0`).
- Boot's embedded Tomcat (IDE, `java -jar`) ignores `META-INF/context.xml`. For IDE runs the
  environment's jars go on the run configuration classpath (11 §12).
- The session cookie name `HQLB_SESSION_<env>`, `HttpOnly` and the timeout are set in
  `HqlBuilderWebApplication.onStartup` (`servletContext.getSessionCookieConfig()`,
  `setSessionTimeout`). Boot's `server.servlet.session.*` properties only apply to the embedded server.

### 3.7 Startup and failure handling

- `StartupValidator` runs before the SessionFactory is built (as a `BeanFactoryPostProcessor`). It
  checks:
  - the required keys (wherever they came from);
  - `hbm2ddl.auto` is `none` or `validate` unless `allow-schema-changes`;
  - that `packagesToScan` or `cfg.xml` is set;
  - the password format;
  - the SQL-function types.
- It logs a banner:
  - environment, project and Hibernate adapter;
  - key origins (masked) and the JDBC URL without credentials;
  - the jars in `/env/lib` with their sizes and timestamps (so a new model jar is visibly picked up);
  - the number of mapped entities and the capabilities.
- An unreachable DB makes the context fail. Tomcat then does not deploy the app, and `docker logs`
  shows why. A degraded mode (the Hibernate part in a lazily created child context, so the UI
  shows "database unreachable, retry") is a phase 4 option.

### 3.8 `ConfigCheck`

`hqlb-entrypoint check` runs
`java -cp "webapps/ROOT/WEB-INF/classes:webapps/ROOT/WEB-INF/lib/*:/env/lib/*" org.tools.hqlbuilder.webapp.tools.ConfigCheck`.
It is used by `check-env`, before `run-env`, and in the baked image build (11 §6.3, §8). It reuses
`EnvironmentPropertySources` and `StartupValidator`, and adds classpath checks:

- the driver and dialect classes can be loaded;
- `@Entity` classes are found in `packagesToScan` (with Spring's
  `ClassPathScanningCandidateComponentProvider`), or `cfg.xml` exists;
- every mapped class loads with its dependencies. A `NoClassDefFoundError` becomes the hint "add the
  jar with X to lib/";
- no `jakarta.persistence` in the model;
- duplicates against `provided-artifacts.txt`.

It exits non-zero on errors and **never connects to the database**.

## 4. Result mapping (`ResultMapper`, `CellMapper`)

All of this runs inside `ResultHandler.handle(result, session)`, while the session is open.

**Rows.** For each element of `result.getResults().getValue()`:

- `Object[]` → its elements.
- `Collection` → its elements (as in Swing).
- `Map` → one column per key (`select new map(...)`).
- Anything else → a single cell.

**Columns.** `javaType` = `queryReturnTypeNames[i]`. The alias follows the precedence of
`HqlBuilderFrame.afterQuery` (lines 1117-1125): `queryReturnAliases[i]`, then
`scalarColumnNames[i][0]`, then `sqlAliases[i]`, then `i`. `label` = `simpleType + " " + alias`.

**Cell classification**, in this order:

| Condition | Cell |
|---|---|
| `null` | `{t:"null"}` |
| `CharSequence`, `Character` | `string` |
| `Boolean` | `boolean` |
| `Byte/Short/Integer/Float/Double`, or `Long` within ±2^53 | `number` |
| other `Long`, `BigInteger`, `BigDecimal` | `decimal` (string) |
| `java.util.Date`/`Calendar`/`java.sql.*`/`java.time.*`/`org.joda.time.*` | `temporal` (ISO-8601) |
| `Enum` | `enum` |
| `byte[]`, `Blob`, `Clob` | `binary` (size only) |
| an entity (the `EntityIntrospector` lookup succeeds; proxies included) | `entity`: entityName, id, idType, guarded `toString()` |
| `Collection`/`Map` inside a cell | `collection` (size only if initialized) |
| anything else | `object`, guarded `toString()` |

**Guarded `toString()`.** Catch any `RuntimeException`, including `LazyInitializationException`.
The fallback is `SimpleName#id` or `*LAZY*` (a proxy is never initialized just to render it).
Truncate to 500 characters.

**Paging.** Fetch `max + 1` rows and set `hasMore`. The server caps `max` at
`hqlbuilder.limits.max-results`.

## 5. Parameters (`ParameterCompiler`)

`HqlServiceImpl.innerExecute` only compiles `valueText` when `value == null` (the value is
`transient`, which is irrelevant for in-process calls).

- **Groovy mode** (capability `groovy`): leave `value == null`, so `GroovyCompiler` evaluates it as
  today. `/parameters/compile` calls `GroovyCompiler.eval` directly.
- **Typed mode**: convert with a `DefaultFormattingConversionService` plus converters (Joda types,
  `enum:<FQN>`, `entity:<FQN>` → `session.load`, `list:<type>`). Set `value`.
- When the capability is off, `mode=groovy` is rejected with 403. The expected type from
  `findParameters` pre-selects the typed-mode type.

## 6. Favorites (`FavoritesService`)

- `QueryFavoriteUtils` (moved to common) with base directory
  `${hqlbuilder.data-dir}/favorites/<project>/favorite.<name>.xml`. `/data` is the volume; with
  `-ShareSwingFavorites` it is the Swing folder.
- Names are validated (`^[\p{L}\p{N} _.-]{1,100}$`, no `.`/`..`), and the resolved path must stay
  inside the project folder.
- The list is rebuilt from disk on every `GET`. Delete removes the correct file.

## 7. Entity views and editing

`EntityViewService.get(entityName, idString)`:

1. Parse the id with the identifier type.
2. Open a session (`setDefaultReadOnly(true)`, `FlushMode.MANUAL`) and load the entity.
3. For each property:
   - to-one → an `entity` cell, with the id taken from the proxy without initializing it;
   - collection → `{initialized:false}`;
   - basic, enum, temporal → a typed cell.
4. Category, as in the Swing `PropertyPanel`:
   - `editable` when the editing capability is on, the property is mapped, it is not the id or the
     version, and its kind is basic/enum/temporal/Boolean;
   - `no editor` for other writable types;
   - `read-only` otherwise.
5. Close the session.

`EntityEditService.patch(entityName, id, version, changes)`:

1. Check the capability.
2. Open a session, begin a transaction and load the entity. Return 409 if the version differs.
3. Convert each value and set it through Hibernate's property access.
4. `flush()`, then `commit()`. Map `ConstraintViolationException` with `JavaxValidationConverter` to 422.
5. Reload and return the view.

`delete` works the same way. Both are audited.

## 8. Concurrency and limits

- `QueryRegistry` and `latches` are concurrent maps. `GroovyCompiler` is thread-local.
- A global semaphore (`max-concurrent-queries`, default 4) returns 429 beyond the limit.
- Request body limit about 1 MB (Tomcat `maxPostSize` plus Spring).
- CSV export: `ScrollableResults`, `FORWARD_ONLY`, fetch size about 500, flushing every N rows,
  honoring cancel and `csv-max-rows`.

## 9. Security implementation

Requirements and threat model are in [07](07-security.md).

- **User store:** an `InMemoryUserDetailsManager` with one user from `hqlbuilder.security.username`
  / `.password`. `PasswordEncoderFactories.createDelegatingPasswordEncoder()` accepts `{bcrypt}`
  (also `$2y$` from `htpasswd`) and `{noop}`.
- **Login API:**
  - `POST /api/v1/auth/login {username, password}` authenticates through the `AuthenticationManager`,
    stores the `SecurityContext` in the HTTP session, and changes the session id.
  - `POST /api/v1/auth/logout`.
  - `GET /api/v1/auth/me`: 200 `{username, environment}`, or 401 with `environment {name, color}`.
- **Session cookie** `HQLB_SESSION_<env>`: `HttpOnly`, `SameSite=Strict` (Tomcat `CookieProcessor`),
  idle timeout from the properties. It is configured in `onStartup` (§3.6), and `Secure` is not set
  (plain HTTP on localhost).
- **CSRF:** `CookieCsrfTokenRepository.withHttpOnlyFalse()`. The SPA echoes `X-XSRF-TOKEN`. JSON
  endpoints accept only `application/json`.
- **Authorization rules:**
  - `permitAll`: `/`, `/index.html`, `/assets/**`, `/favicon.*`, `POST /api/v1/auth/login`,
    `GET /api/v1/auth/me`, `GET /api/v1/health`.
  - `authenticated`: everything else under `/api/**`.
  - Unauthenticated calls get a 401 problem+json, never a redirect.
- **`LoginAttemptService`:** a lockout of 5 minutes after `max-failed-logins` failures (429).
- **`HostHeaderFilter`:** runs first and rejects `Host` names outside `allowed-hosts` (the port is ignored).
- **Headers:** CSP `default-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; connect-src 'self'; frame-ancestors 'none'`,
  plus `X-Content-Type-Options: nosniff` and `Referrer-Policy: no-referrer`.
- **Capabilities:** checked in every service method that needs one:
  - DML is detected from the translated statement type;
  - `read-only` also sets `session.setDefaultReadOnly(true)` and `connection.setReadOnly(true)`.

## 10. Test fixture (test scope only; no demo)

The old `hql-builder-demo` module was removed in `feb021df` and stays out of scope. Automated
tests still need a real `SessionFactory`, so there is a **test-scope fixture**:

- About 10 entities in `hql-builder-service/src/test`:
  - to-one (lazy), one-to-many, many-to-many;
  - enum, `java.time` and Joda dates, `BigDecimal`;
  - an embeddable;
  - an inheritance pair (for `$$` / `SUB(...)`);
  - a composite id and `@Version`;
  - one named query and one custom SQL function.
- H2 in-memory with an `import.sql`, published as a `test-jar` so the webapp tests reuse it.
- It is never deployed, not documented as a product, and not a demo environment.
- CI (§12) builds a throwaway environment folder from it (the test-jar plus H2 in `lib/` plus
  properties) for the container smoke test.

## 11. Tests

| Level | What | Tooling |
|---|---|---|
| Unit | `SqlFormatter`, `ParameterImport`, favorites, `CellMapper`, `GraphMapper`, id parsing, name validation, **property source priority and imports**, **raw SQL-function key reading** | JUnit 5 |
| Characterization | exception regex mapping in `HqlServiceImpl.execute` (fixture) | JUnit 5 + H2 |
| Regression | S12/S13: a configured SQL function is usable in a select clause (fixture) | JUnit 5 + H2 |
| Integration | controllers against the fixture context: execute (select, DML, `$$`, paging, errors), cancel, entities, favorites; **security**: login, logout, lockout, CSRF, host header, capabilities, read-only | Spring Boot Test + MockMvc + spring-security-test |
| Config check | `ConfigCheck` against good and broken environment folders (missing driver or dependency, jakarta model, conflicting jar, unsafe hbm2ddl, bad function type, missing import) | JUnit 5 |
| Container smoke | generic image + throwaway fixture environment: `check`, run, health, login, one query, security headers, non-root, `--user <uid>:0` | shell script in CI |
| Adapter | integration suite with `-Dversion.hibernate=5.4/5.5/5.6` | CI matrix |

## 12. Build and CI

- The users build on the host with JDK 17/21, Maven and **Node 24** (D9). Nothing is built inside
  Docker.
- Reactor order: `... common, service, client, webui, webapp`. The new modules live in a profile
  until phase 1 is stable, then join the default modules.
- `hql-builder-webui`:
  - By default, `exec-maven-plugin` runs the **installed** `npm` (`npm ci`, `npm run build`,
    `npm test -- --run`). `package.json` declares `"engines": {"node": ">=24"}` and the build
    enforces it (`engine-strict`).
  - The `dist/**` output is packaged into the jar under `META-INF/resources/`.
  - Profile `-Pbundled-node` switches to frontend-maven-plugin, which downloads Node 24 for machines
    or CI runners without Node.
  - `-Dskip.webui` reuses the last installed webui jar.
- Windows and Ubuntu builds both work, because nothing in the Maven build is OS-specific. The
  scripts exist in `.ps1` and `.sh` flavors, and `.gitattributes` keeps `*.sh` at LF.
- GitHub Actions:
  - `build.yml` runs `mvn verify` with `actions/setup-java` (17) and `actions/setup-node` (24),
    caching `~/.m2` and the npm cache.
  - A new `docker.yml` builds the generic image and runs the container smoke test with the
    throwaway fixture environment. **No images are pushed anywhere** (D8).
- Release: `hql-builder-webui` and `hql-builder-webapp` are deployed to `mvn-repo` like the other
  modules. Images are always built locally by the users with `docker/build-image`.

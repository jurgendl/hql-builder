# 04: REST API design

- Base path: `/api/v1`. JSON (UTF-8) only, except the CSV export and the schema script.
- The OpenAPI document is served at `/api/v1/openapi.json` (springdoc). TypeScript types are
  generated from it (`openapi-typescript`).
- Every request needs a logged-in session, except `POST /auth/login` and `GET /health` (§1).
  Every state-changing request (POST/PUT/PATCH/DELETE) except the login also needs the CSRF header
  `X-XSRF-TOKEN` (see [07](07-security.md)).
- The browser reaches the API through the container's single published port, at the same origin
  as the SPA.
- DTOs below use TypeScript notation. Java DTOs mirror them in `org.tools.hqlbuilder.webapp.dto`.
- Errors use `application/problem+json` (RFC 7807), see §9.

## 1. Authentication, health and info

The credentials are the single user from the environment's `security.properties`
([11 §4.4](11-docker-packaging.md#44-securityproperties)). Every `/api/v1/**` endpoint needs an
authenticated session, except `POST /auth/login` and `GET /health`. Unauthenticated calls get a
**401 problem+json**, never a redirect.

### `POST /auth/login`

```ts
// request
{ username: string; password: string }
// 204, Set-Cookie: HQLB_SESSION_<env>=...; HttpOnly; SameSite=Strict; Path=/   (session id changed on login)
//      Set-Cookie: XSRF-TOKEN=...; Path=/                                        (readable by the SPA, echoed as X-XSRF-TOKEN)
// 401 /problems/unauthenticated   bad credentials
// 429 /problems/locked            too many failed attempts (lockout, default 5 attempts / 5 minutes)
```

The login request needs no CSRF header. The session does not exist yet, and `SameSite=Strict`
plus the JSON-only content type block cross-site login CSRF.

### `POST /auth/logout`

Invalidates the session. Response 204.

### `GET /auth/me`

```ts
// 200
{ username: string; environment: { name: string; color: string } }
// 401 when not logged in: problem+json /problems/unauthenticated with the extension
//     environment: { name, color }   (only these two values, so the login page can show which environment it is)
// The SPA calls this at startup to decide between the login page and the app; the call also issues the XSRF-TOKEN cookie.
```

### `GET /health` (no authentication)

```ts
{ status: "UP" | "DOWN"; db: "UP" | "DOWN" }   // nothing else: no versions, no names
```

It can back a Docker `HEALTHCHECK`.

### `GET /info`

Loaded once at startup. It drives the title bar, the About dialog and feature visibility.

```ts
interface InfoDto {
  name: "HQL Builder";
  version: string;               // hql-builder version (getVersion())
  latestVersion?: string;        // from GitHub maven-metadata.xml, cached, may be absent offline
  project: string;               // getProject() (db.properties "project", default = environment name)
  connectionInfo: string;        // getConnectionInfo(): user@jdbc-url@modelVersion (never the password)
  environment: {                 // from hqlbuilder.properties
    name: string;                // e.g. "gismo-dev": header badge and document.title
    color: string;               // header colour, e.g. "#c92a2a" for a production copy
    configMode: "mounted" | "baked";
    hibernateAdapter: string;    // "5.6"
    javaVersion: string;
    readOnly: boolean;
  };
  user: { name: string };
  capabilities: {                // effective values (hqlbuilder.properties, with read-only applied)
    luceneSearch: boolean;       // Information available
    plainSql: boolean;           // SQL mode
    dml: boolean;                // update/delete/insert HQL
    groovy: boolean;             // Groovy parameter evaluation and column scripts
    editing: boolean;            // PATCH/DELETE entities
    schemaScript: boolean;       // createScript()
    systemProperties: boolean;   // container JVM system properties
  };
  limits: { maxResults: number; queryTimeoutSeconds: number; maxSearchResults: number };
  hibernate: Record<string, string>;   // getHibernateInfo()
  helpUrls: { hibernate?: string; hql?: string; lucene?: string; wiki: string };
}
```

## 2. Metadata

| Method and path | Response | Maps to |
|---|---|---|
| `GET /meta/keywords` | `string[]` | `getReservedKeywords()` |
| `GET /meta/classes` | `ClassSummaryDto[]` | `getHibernateWebResolver().getClasses()` |
| `GET /meta/graph` | `GraphDto` | `getHibernateWebResolver()` (flattened) |
| `GET /meta/classes/{entityName}/properties` | `string[]` (excludes id/version) | `getProperties(classname)` |
| `GET /meta/property-names?entity={name}&path={a.b}` | `string[]` | `getPropertyNames(entity, ["alias", "a", "b"])` |
| `GET /meta/named-queries` | `Record<string, string>` | `getNamedQueries()` |
| `GET /meta/search?q=&type=class\|field\|all&max=` | `string[]`; 501 when Lucene is unavailable | `search(text, type, max)` |
| `GET /meta/schema-script` | `text/plain`; capability `schemaScript` | `createScript()` |

```ts
interface ClassSummaryDto { name: string; simpleName: string; packageName: string }

interface GraphDto {
  classes: Array<{
    name: string;                // FQN, the node id
    simpleName: string;
    edges: Array<{
      name: string;              // property name, or "SUB(<fqn>)" for subclass edges
      target: string;            // FQN of the target node
      kind: "toOne" | "collection" | "subclass";
    }>;
  }>;
}
```

The graph replaces the cyclic `HibernateWebResolver` object. It is small (one entry per mapped
class) and is cached by the client for the whole session.

## 3. HQL tooling

| Method and path | Request | Response | Maps to |
|---|---|---|---|
| `POST /hql/analyze` | `{ hql: string }` | `AnalyzeDto`, or a syntax problem | new: `QueryTranslator` without executing (+ `getSqlForHql`) |
| `POST /hql/parameters` | `{ hql: string }` | `ParameterDto[]` | `findParameters(hql)` |
| `POST /hql/format` | `{ hql: string }` | `{ hql: string }` | `SqlFormatter.makeMultiline(removeBlanks(...))` |
| `POST /sql/format` | `SqlFormatRequest` | `{ sql: string }` | `SqlFormatter.cleanupSql(...)` |

```ts
interface AnalyzeDto {
  sql: string;
  fromAliases: Record<string, string>;   // alias → entity FQN (for autocomplete)
  returnTypes: string[];                 // FQNs
  returnAliases: (string | null)[];
  parameters: ParameterDto[];
  statementType: "select" | "dml";
}

interface SqlFormatRequest {
  sql: string;
  returnAliases?: (string | null)[];
  scalarColumnNames?: string[][];
  options: SqlFormatOptions;
}
interface SqlFormatOptions { enabled: boolean; removeJoins: boolean; formatLines: boolean; replaceProperties: boolean }
```

`/hql/analyze` is new. It lets autocomplete resolve `alias.` without running the query first. The
Swing client used the aliases of the *last successful execution* (`HqlBuilderFrame.aliases`).
The client calls it debounced (about 500 ms after typing stops) and only when the editor text changed.

## 4. Parameters

```ts
interface ParameterDto {
  name?: string;          // named parameter  (exactly one of name / index)
  index?: number;         // positional, 1-based
  valueText: string;      // what the user typed (Groovy expression or typed text)
  mode?: "groovy" | "typed";          // default: groovy when capability on, else typed
  type?: string;          // typed mode: target type (see below); response: resulting Java type
  display?: string;       // response: toString() of the compiled value
  expectedType?: string;  // from findParameters: expected Java simple type, "?" if unknown
}
```

| Method and path | Request | Response |
|---|---|---|
| `POST /parameters/compile` | `{ valueText, mode, type? }` | `{ ok: boolean, type?: string, display?: string, error?: string }` |
| `POST /parameters/import` | `{ text: string }` (JSON object, Groovy map `{a=1}`, or list `[1,2]`) | `ParameterDto[]` |

Typed mode supports these `type` values: `String`, `Long`, `Integer`, `Short`, `BigDecimal`,
`BigInteger`, `Double`, `Boolean`, `java.util.Date`, `java.time.LocalDate`, `java.time.LocalDateTime`,
`org.joda.time.LocalDate`, `org.joda.time.LocalDateTime`, `enum:<FQN>`, `entity:<FQN>` (loads a
reference by id), and `list:<type>` (comma-separated values).

## 5. Query execution

### `POST /queries`

```ts
interface ExecuteRequest {
  uuid: string;                 // client-generated (crypto.randomUUID()); used for cancel
  hql: string;                  // already reduced to the statement under the caret, comments blanked
  plainSql: boolean;            // SQL mode (capability plainSql)
  first: number;                // 0-based start index
  max: number;                  // page size; the server caps it at limits.maxResults
  parameters: ParameterDto[];
  sqlFormat: SqlFormatOptions;  // so the response carries formattedSql
}

interface QueryResultDto {
  uuid: string;
  statementType: "select" | "dml";
  sql: string;                  // raw generated SQL
  formattedSql: string;         // after cleanupSql (or raw when sqlFormat.enabled=false)
  fromAliases: Record<string, string>;
  columns: ColumnDto[];
  rows: CellDto[][];            // rows[r][c]
  size: number;                 // rows returned (select), or affected rows (dml)
  first: number;
  max: number;
  hasMore: boolean;             // true when size == max (optionally verified by fetching max+1)
  durationMs: number;           // ExecutionResult.duration
  overheadMs: number;           // ExecutionResult.overhead
}

interface ColumnDto {
  index: number;
  javaType?: string;            // queryReturnTypeNames[i]
  simpleType?: string;
  alias?: string;               // same precedence as Swing: returnAliases > scalarColumnNames[i][0] > sqlAliases > index
  label: string;                // "<simpleType> <alias>", as in the Swing header
}
```

### Cell encoding

Every cell is a tagged union. It is always renderable through `text`, and it carries typed data
where that is useful.

```ts
type CellDto =
  | { t: "null" }
  | { t: "string";   v: string }
  | { t: "number";   v: number; javaType: string }              // only when exactly representable in a JS double
  | { t: "decimal";  v: string; javaType: string }              // Long > 2^53, BigInteger, BigDecimal
  | { t: "boolean";  v: boolean }
  | { t: "temporal"; v: string; javaType: string }              // ISO-8601 (Date, Timestamp, java.time, Joda)
  | { t: "enum";     v: string; javaType: string }              // name()
  | { t: "entity";   entity: string; id: string; idType: string; text: string; truncated?: boolean }
  | { t: "object";   javaType: string; text: string; truncated?: boolean }   // embeddables, other values
  | { t: "collection"; javaType: string; size?: number; text: string }
  | { t: "binary";   size: number };
```

Rules:

- `text` for entity and object cells is `toString()`, computed inside the session, wrapped in
  try/catch (the fallback is `<SimpleName>#<id>` or `*LAZY*`), and truncated to 500 characters
  (`truncated: true`).
- Entity detection uses the session and entity metamodel. Proxies are resolved with
  `HibernateProxy.getHibernateLazyInitializer()`, and uninitialized proxies are **not** initialized.
- The CSV export uses the same cell text.

### `POST /queries/{uuid}/cancel`

Response: `{ cancelled: boolean }`. It is idempotent, and returns `false` when the query is
unknown or already finished.

### `POST /queries/export.csv`

The body is the same `ExecuteRequest`. `first` and `max` are ignored, and a server hard cap
applies. The response is `text/csv; charset=utf-8` with
`Content-Disposition: attachment; filename="<project>-<timestamp>.csv"`. It is streamed with
`ScrollableResults`, uses RFC 4180 quoting, and starts with a header row of the column labels.

## 6. Entities (property panel, object tree, editing)

`{entityName}` is the Hibernate entity name, usually the FQN. `{id}` is the string id, URL-encoded.
Composite ids are JSON encoded and then URL-encoded.

### `GET /entities/{entityName}/{id}`

```ts
interface EntityViewDto {
  entity: string;
  id: string;
  version?: string;                 // for optimistic locking on PATCH
  text: string;                     // toString (guarded)
  properties: PropertyDto[];        // sorted by name
}

interface PropertyDto {
  name: string;
  javaType: string;
  kind: "id" | "version" | "basic" | "enum" | "temporal" | "toOne" | "collection" | "component";
  value: CellDto;                   // toOne → entity cell (id taken from the proxy, no initialization)
  category: "read-only" | "editable" | "no editor";   // same grouping as the Swing PropertyPanel
  editable: boolean;                // editing capability && category === "editable"
  enumValues?: string[];            // kind === "enum"
  nullable?: boolean;
  collection?: { elementEntity?: string; initialized: false };   // kind === "collection"
}
```

Property enumeration uses the Hibernate `ClassMetadata`: identifier, version and mapped
properties. `?beanProperties=true` adds unmapped JavaBean getters as read-only, which is how
the Swing `PropertyPanel` does it (`PropertyDescriptorsBean`).

### `GET /entities/{entityName}/{id}/collections/{property}?first=0&max=50`

```ts
interface CollectionPageDto { size: number; first: number; items: CellDto[] }
```

It is implemented with an HQL of the form
`select c from <Entity> e join e.<property> c where e.<idProp> = :id` plus a count query. The
object tree calls it when a collection node is expanded.

### `PATCH /entities/{entityName}/{id}`, capability `editing`

```ts
// request
{ version?: string; changes: Record<string, JsonValue | null> }
// JsonValue follows the property kind: string | number | boolean | ISO date string | enum name
// | { entity: string, id: string } for toOne (optional, phase 3+)
// response 200: EntityViewDto (reloaded)
// 409 optimistic-lock conflict, 422 validation problem
```

This replaces the Swing flow "mutate the local object, then `save(bean)`, then `get(class, id)`".
The server loads the entity in a new session, converts each value with a `ConversionService`,
sets it through Hibernate's property access, flushes, and commits. Constraint violations go
through the existing `JavaxValidationConverter` and come back as a validation problem.

### `DELETE /entities/{entityName}/{id}`, capability `editing`

Response 204. It replaces `delete(bean)`.

## 7. Favorites

Favorites are stored in the container's data volume (`/data/favorites/<project>/favorite.<name>.xml`)
in the Swing XML format. Bind-mounting `~/hqlbuilder/favorites` shares them with the Swing client
(05 §6, 11 §5.4).

| Method and path | Request / response |
|---|---|
| `GET /favorites` | `FavoriteDto[]` (excludes `LAST`) |
| `GET /favorites/{name}` | `FavoriteDto`; 404 when missing. `LAST` is allowed |
| `PUT /favorites/{name}` | `{ hql: string; parameters: ParameterDto[] }` → 204. Name validated: `^[\p{L}\p{N} _.-]{1,100}$` |
| `DELETE /favorites/{name}` | 204 |

```ts
interface FavoriteDto { name: string; hql: string; parameters: ParameterDto[]; hqlPreview: string; parametersPreview: string }
```

## 8. System

| Method and path | Capability | Response |
|---|---|---|
| `GET /system/properties` | `systemProperties` | `Record<string,string>` of the container JVM, sorted. Keys that contain `password`, `secret` or `token` are masked |
| `GET /system/environment` | (logged in) | `EnvironmentDetailsDto` (below): which files, imports and jars the environment uses |

```ts
interface EnvironmentDetailsDto {
  envDir: string;                                  // "/env"
  configMode: "mounted" | "baked";
  files: { name: string; found: boolean }[];       // hqlbuilder.properties, db.properties, security.properties, extra-context
  imports: { location: string; found: boolean }[]; // hqlbuilder.config.imports, in order
  keyOrigins: Record<string, string>;              // required key → source (file or import); values never included
  libs: { file: string; size: number; modified: string }[];   // /env/lib/*.jar, so a swapped model jar is visible
  sqlFunctions: Record<string, string>;            // name → return type, as registered
  entityCount: number;
}
```

There is no shutdown endpoint. The container is stopped with `docker stop` (or the run script).

## 9. Error model

All errors are `application/problem+json`:

```ts
interface Problem {
  type: string;          // URI, see table below
  title: string;
  status: number;
  detail?: string;       // Hibernate/DB message; never a stack trace
  // extensions per type:
  syntaxType?: "unexpected_token" | "invalid_path" | "could_not_resolve_property" | "not_mapped"
             | "unable_to_resolve_path" | "illegal_attempt_to_dereference_collection";
  wrong?: string; line?: number; col?: number;           // SyntaxException
  sql?: string; sqlState?: string; sqlException?: string; // SqlException / partial result
  invalidValues?: { propertyName: string; propertyPath: string; message: string; value?: string }[];
}
```

| Java exception | `type` | HTTP |
|---|---|---|
| `SyntaxException` | `/problems/syntax` | 400 |
| `SqlException` | `/problems/sql` | 422 |
| `ServiceException` (any other; may carry `partialResult.sql`) | `/problems/query` | 422 |
| `ValidationException` / `ConstraintViolationException` | `/problems/validation` | 422 |
| `OptimisticLockException` / `StaleObjectStateException` | `/problems/conflict` | 409 |
| cancelled by the user (`cancelQuery`) | `/problems/cancelled` | 409 |
| query timeout | `/problems/timeout` | 504 |
| `UnsupportedOperationException` (Lucene) | `/problems/unsupported` | 501 |
| not logged in, session expired, bad credentials | `/problems/unauthenticated` | 401 |
| too many failed logins | `/problems/locked` | 429 |
| too many concurrent queries | `/problems/busy` | 429 |
| capability disabled for this environment (for example `read-only`) | `/problems/forbidden` | 403 |
| CSRF token missing or invalid | `/problems/csrf` | 403 |
| unknown entity or id | `/problems/not-found` | 404 |
| anything else | `/problems/internal` | 500 (generic message, details only in the log) |

## 10. `HqlService` coverage

| `HqlService` method | Endpoint |
|---|---|
| `getVersion`, `getProject`, `getConnectionInfo`, `getHibernateInfo`, `get*HelpURL` | `GET /info` |
| `getReservedKeywords` | `GET /meta/keywords` |
| `getClasses`, `getHibernateWebResolver` | `GET /meta/classes`, `GET /meta/graph` |
| `getProperties` | `GET /meta/classes/{e}/properties` |
| `getPropertyNames` | `GET /meta/property-names` |
| `getNamedQueries` | `GET /meta/named-queries` |
| `search` | `GET /meta/search` |
| `createScript` | `GET /meta/schema-script` |
| `getSqlForHql` | `POST /hql/analyze` |
| `findParameters` | `POST /hql/parameters` |
| `execute` | `POST /queries`, `POST /queries/export.csv` |
| `stopQuery` | `POST /queries/{uuid}/cancel` |
| `get` | `GET /entities/{e}/{id}` |
| `save` | `PATCH /entities/{e}/{id}` |
| `delete` | `DELETE /entities/{e}/{id}` |
| `sql(String[])` | not exposed |
| `log()` | not exposed |
| client `cleanupSql`, `makeMultiline`, `removeBlanks` | `POST /sql/format`, `POST /hql/format`, inline in `QueryResultDto.formattedSql` |

## 11. Example

```http
POST /api/v1/queries
Content-Type: application/json
X-XSRF-TOKEN: 3f1c...

{ "uuid": "5a0b...", "hql": "select p, p.name from Person p where p.status = :status",
  "plainSql": false, "first": 0, "max": 100,
  "parameters": [{ "name": "status", "valueText": "Status.ACTIVE", "mode": "groovy" }],
  "sqlFormat": { "enabled": true, "removeJoins": true, "formatLines": true, "replaceProperties": true } }
```

```json
{
  "uuid": "5a0b...", "statementType": "select",
  "sql": "select person0_.id as id1_0_0_, ...", "formattedSql": "select\n    person.id ...",
  "fromAliases": { "p": "com.acme.model.Person" },
  "columns": [
    { "index": 0, "javaType": "com.acme.model.Person", "simpleType": "Person", "alias": "0", "label": "Person 0" },
    { "index": 1, "javaType": "java.lang.String", "simpleType": "String", "alias": "1", "label": "String 1" }
  ],
  "rows": [
    [ { "t": "entity", "entity": "com.acme.model.Person", "id": "42", "idType": "java.lang.Long", "text": "Person[42, Jane]" },
      { "t": "string", "v": "Jane" } ]
  ],
  "size": 1, "first": 0, "max": 100, "hasMore": false, "durationMs": 7, "overheadMs": 3
}
```

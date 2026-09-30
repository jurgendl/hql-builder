# 02: Feature inventory (parity checklist)

This document lists every user-visible feature of the Swing client. It was built from a full read
of `HqlBuilderFrame.java` and the other client classes. Use it as the acceptance checklist for
the web version.

**Plan codes**

- **Keep**: same behavior, web-native UI.
- **Adapt**: same goal, different mechanism.
- **Improve**: better than Swing, or fixes a bug.
- **Drop**: desktop-only or obsolete.

**Phase** refers to [08-roadmap.md](08-roadmap.md). **Status**: `-` not started, `wip`, `done`.

Unless noted otherwise, line numbers refer to
`hql-builder/hql-builder-client/src/main/java/org/tools/hqlbuilder/client/HqlBuilderFrame.java`.

## A. Application shell

| ID | Feature | Swing location | Web plan | Phase | Status |
|---|---|---|---|---|---|
| A1 | Four panels: HQL/SQL/Lucene tabs, Parameters, Properties, Results | `start()` 2728-2890 | **Keep**: `react-resizable-panels` | 1 | - |
| A2 | Two layouts: (a) editor, params and properties on top with results below; (b) editor and params on top, results and properties below | `switch_layout` 3233, `layout` 2402 | **Keep**: two presets, persisted, split sizes remembered | 1 | - |
| A3 | Title shows name, version, connection info, project, service URL | 3196 | **Keep**: header bar with the **environment badge in the environment colour** (N2), plus `document.title` = `<env> · HQL Builder` | 1 | - |
| A4 | Splash screen with progress while the context boots | `SplashHelper`, `start` 307-368 | **Adapt**: the container boots before the user opens the browser. Show the login page (N1), then a loading screen while `/info` loads, with a "did you know" tip | 1 | - |
| A5 | Menus: Actions, Settings, HQL, Results, Help | 3027-3194 | **Adapt**: compact menu bar plus a **command palette** listing every action | 1 / 4 | - |
| A6 | Vertical toolbars (HQL tools, results tools), can be hidden | 2997-3025, `show_toolbars` 2472 | **Keep**: icon toolbars with tooltips, toggle in settings | 1 | - |
| A7 | Context menus on the editor and the results table | `createHqlPopupMenu` 1409, `createResultsPopupMenu` 1428 | **Keep** | 1 / 2 | - |
| A8 | Progress glass pane "querying..."; clicking it stops the query | `progressbarStart` 2565, glass pane | **Adapt**: non-blocking progress bar over the results with a Stop button. The editor stays usable | 1 | - |
| A9 | System tray icon, minimize to tray | `start` 371-405, `switchVisibility` 3287 | **Drop** | - | - |
| A10 | Always on top | `always_on_top` 1208 | **Drop** | - | - |
| A11 | Exit / force exit with confirmation | `exit` 1603, `force_exit` 1748 | **Drop**: the container is stopped with `docker stop` or the run script. "Log out" (N1) takes its place in the menu | - | - |
| A12 | Uncaught exception dialog, fatal startup error dialog | `start` 406-411 | **Adapt**: error boundary plus toast. A failed startup (for example an unreachable DB) is reported in `docker logs` by the startup validator's banner | 1 | - |

## B. HQL editor

| ID | Feature | Swing location | Service | Web plan | Phase | Status |
|---|---|---|---|---|---|---|
| B1 | Multi-line editor with line numbers and a configurable font (default DejaVu Sans Mono, 16) | `start` 2759-2760, `fontAction` 735 | - | **Keep**: CodeMirror 6; font family and size in settings, applied without a restart | 1 | - |
| B2 | Run (Ctrl+Enter): statement under the caret (split on `;`) or the selection | `getHqlText` 1842, `executeQuery` 1541 | `execute` | **Keep**: port the logic exactly (see 06 §5) | 1 | - |
| B3 | `--` comments blanked before execution, columns preserved | `getHqlText` 1863-1876 | - | **Improve**: also strip `//` comments, which are documented and toggled but not stripped today | 1 | - |
| B4 | Highlight the executed statement (setting "select HQL being executed") | `getHqlText` 1857-1861 | - | **Keep**: decoration or selection | 1 | - |
| B5 | Retry: if running a selection fails, run the full statement | `executeQuery` 1577-1596 | `execute` | **Keep** | 1 | - |
| B6 | Stop query (button, progress click) | `stop_query` 3210 | `stopQuery` | **Improve**: real JDBC cancel on the server (see 05) plus aborting the fetch | 1 | - |
| B7 | Keyword highlighting from server keywords, "continuous" or only on run; colour configurable | `hilightSyntax` 2177, `continuous_syntax_highlighting` 3359 | `getReservedKeywords` | **Adapt**: CodeMirror language mode, always live (cheap in CM6). Keep the colour setting | 1 | - |
| B8 | Bracket matching highlight, colour configurable | `hilightBraces` 2145 | - | **Keep**: `bracketMatching()` | 1 | - |
| B9 | Auto-insert the closing `)` | key listener 2932-2939 | - | **Keep**: `closeBrackets()`, toggle in settings | 1 | - |
| B10 | Toggle remark over the selected lines, Ctrl+Shift+/ (`--`; removes `--` and `//`) | `remark_toggle` 2586, `remarkToggle` 2596 | - | **Keep**: port the semantics; also bind Ctrl+/ | 1 | - |
| B11 | Syntax error markers (red border at the offending token) plus an error tooltip on hover | `hilightSyntaxException` 2217, tooltip 763-799 | `execute` → `SyntaxException` | **Keep**: CodeMirror lint diagnostics from problem+json `{syntaxType, wrong, line, col}` | 1 | - |
| B12 | Hover tooltip on `:name` / `?` shows the bound parameter value | tooltip 800-836 | - | **Keep**: `hoverTooltip` | 2 | - |
| B13 | Insert helper (Ctrl+Shift+Space / Ctrl+Shift++): class list after `from `, property list after `alias.` | `helpInsert` 2023, `helpInsertClass` 2037, `helpInsertProperty` 2044 | `getHibernateWebResolver`, `getPropertyNames` | **Improve**: real autocompletion. Aliases come from `/hql/analyze` instead of the *last successful run* | 2 | - |
| B14 | Format (Alt+F9): collapse whitespace, then one clause per line | `format` 1756/1765 | client `removeBlanks`, `makeMultiline` | **Keep**: server `SqlFormatter` or a TypeScript port | 1 | - |
| B15 | Clear / reset query (Alt+F2): editor, parameters, results, SQL, column scripts | `clear` 1233 | - | **Keep** | 1 | - |
| B16 | Delete inverted selection (Ctrl+Delete): keep only the selected text | `delete_inverted_selection` 1442 | - | **Keep**, but remap the shortcut (see 06 §14) | 2 | - |
| B17 | Export: copy HQL as Java string concatenation to the clipboard (Alt+F11) | `export__copy_hql_as_java_to_clipboard` 1611 | - | **Keep**: Clipboard API, with a fallback dialog on insecure origins | 2 | - |
| B18 | Import: paste Java string concatenation from the clipboard as HQL (Alt+F12) | `import__paste_hql_as_java_from_clipboard` 2317 | - | **Keep**: paste dialog (reading the clipboard needs a permission) | 2 | - |
| B19 | Copy / paste / undo / redo / find (ETextArea popup) plus search highlight colour | `ETextArea`, `search_color` 2698 | - | **Keep**: CodeMirror history and search panel; colour setting | 1 | - |
| B20 | Load named query (Alt+F10): filterable list, replaces the editor content | `load_named_query` 2430 | `getNamedQueries` | **Keep** | 2 | - |
| B21 | Wizard (Alt+F1): pick source and target class, BFS path search with accept / continue / stop, rows with inner-join/select toggles, generates `select ... from ... join ...` | `wizard` 3354, `HqlWizard.java`, `PathResolver.java` | `getHibernateWebResolver` | **Keep**: port `PathResolver` and `fini()` to TypeScript on top of `/meta/graph` | 2 | - |
| B22 | SQL mode (plain SQL instead of HQL); disables HQL-only actions | `sql_mode` 3369, `oldPlainSqlAction` 579 | `execute(isPlainOldSql)` | **Keep**: per-editor toggle, gated by a server capability | 2 | - |
| B23 | `$$` placeholder runs the query across all subclasses of the class in parameter `$$` | `HqlServiceImpl.execute` 310 | `execute` | **Keep** (server-side, no UI) | 1 | - |
| B24 | Drag a property name from the Properties panel into the editor | `PropertyPanel` TransferHandler 251-294, DidYouKnow #1 | - | **Keep**: HTML5 drag and drop, plus an "insert name" action | 3 | - |

## C. SQL tab

| ID | Feature | Swing location | Service | Web plan | Phase | Status |
|---|---|---|---|---|---|---|
| C1 | Shows the generated SQL of the last run | `afterQuery` 1065 | `execute().sql` | **Keep**: read-only CodeMirror with SQL highlighting | 1 | - |
| C2 | SQL post-processing: "format sql" plus options "remove joins", "format lines", "replace properties" | `formatSql` 1773-1796 | client `cleanupSql` | **Keep**: server-side `SqlFormatter`; options sent with the execute request | 1 | - |
| C3 | On error: message, SQL state, exception, and the partial SQL | `afterQuery(Throwable)` 1160-1206 | exceptions | **Keep**: problem+json fields rendered in the SQL tab | 1 | - |
| C4 | Tab title colour: green on success, red on error, grey when stale | 1136-1137, 1161-1162, 2554 | - | **Keep**: status dot or colour on the tabs | 1 | - |

## D. Lucene search tab

| ID | Feature | Swing location | Service | Web plan | Phase | Status |
|---|---|---|---|---|---|---|
| D1 | Search box, "class" and "field" checkboxes (both or neither means all), result list | `start` 2766-2833 | `search(text, type, max)` | **Keep** | 2 | - |
| D2 | Maximum number of search results (default 2000) | `maximumNumberOfSearchResultsAction` 588 | - | **Keep** (setting) | 2 | - |
| D3 | "Lucene unavailable" message | 2796-2798 | `UnsupportedOperationException` | **Keep**: hide the tab when `/info.capabilities.luceneSearch=false` | 2 | - |
| D4 | Click a result to insert or copy it | not implemented in Swing | - | **Improve** (optional): insert the class or property name into the editor | 2 | - |

## E. Parameters panel

| ID | Feature | Swing location | Service | Web plan | Phase | Status |
|---|---|---|---|---|---|---|
| E1 | Parameter list (`index:` or `name=`, type, value) | `parametersUnsafe`, layout 2857-2880 | - | **Keep** | 1 | - |
| E2 | Editor fields: Name, Value (the Groovy text), Compiled (read-only result and type) | same | - | **Keep** | 1 | - |
| E3 | Live compile of the value on every keystroke | `compile` 1367, listener 2893-2918 | Groovy runs client-side | **Adapt**: debounced `POST /parameters/compile` | 1 | - |
| E4 | Heuristics: ISO date → `new LocalDate(y,m,d)` (Joda), integer → `123l` | listener 2899-2915 | - | **Keep** in Groovy mode. **Improve**: use the expected type from `findParameters` when it is known | 1 | - |
| E5 | Save (Enter), Add new, Remove (selected, or all when nothing is selected), Up, Down | `save` 2665, `add_parameter` 1005, `remove` 2648, `up`/`down` | - | **Keep** | 1 | - |
| E6 | Set null, To text (wrap in `'...'`) | `set_null` 2705, `to_text` 3331 | - | **Keep** | 1 | - |
| E7 | Find parameters (Alt+F3): replaces the list with the parameters detected in the HQL | `find_parameters` 1732 | `findParameters` | **Improve**: merge with existing values instead of wiping them (optional) | 1 | - |
| E8 | Import parameters from text: JSON object, Groovy map `{a=1}`, or list `[1,2]` for positional parameters | `convertParameterString` 204, `import_parameters` 2334 | - | **Keep**: `POST /parameters/import` (the map/list forms need Groovy) | 2 | - |
| E9 | Parameters are stored with favorites and with LAST | `addLast` 1030 | - | **Keep** | 1 / 2 | - |
| E10 | Typed parameter entry without Groovy | not in Swing | - | **Improve**: type selector (String, Long, Integer, BigDecimal, Boolean, date types, enum, entity id, list), required when Groovy is disabled | 2 | - |

## F. Results panel

| ID | Feature | Swing location | Service | Web plan | Phase | Status |
|---|---|---|---|---|---|---|
| F1 | Grid with headers `<SimpleType> <alias>`, row numbers, cell selection | `afterQuery` 1064-1135, 2740-2751 | `execute` | **Keep**: virtualized grid | 1 | - |
| F2 | `Object[]` rows become columns; `Collection` rows become columns; single objects become one column | `afterQuery` 1078-1086 | - | **Keep**. **Improve**: `select new map(...)` rows become one column per key | 1 | - |
| F3 | Auto-size columns, "resize columns" setting | 1139-1145 | - | **Keep** | 1 | - |
| F4 | Status: "N results in X s" / "No results"; duration and overhead logged | 1146-1156 | - | **Keep** | 1 | - |
| F5 | Max results indicator `[ max=N ]`, red when N > 500 or N ≤ 0; double-click to change (default 100) | `setMaxResults` 2720, `maximum_number_of_results` 2462 | - | **Keep** | 1 | - |
| F6 | Paging: `|<` back to 0, start index field, `>` next page (start += max) | 741-755, 2882-2888 | `execute(first,max)` | **Keep**. **Improve**: `hasMore` flag, previous page | 1 | - |
| F7 | Selecting a cell shows that object in the Properties panel | `addTableSelectionListener` 2954-2979 | - (local object) | **Adapt**: an entity cell triggers `GET /entities/{name}/{id}` | 1 | - |
| F8 | Copy selected cell (Shift+Alt+F11) | `copy_selected_cell` 1391 | - | **Keep** | 1 | - |
| F9 | "Hibernate properties" (Shift+Alt+F8): copy the entity's property names, comma-separated, to the clipboard | `hibernate_properties` 2097 | `getProperties` | **Keep** | 2 | - |
| F10 | Object tree window (Shift+Alt+F9) | `object_tree` 2488, `ObjectTree.java` | - (local reflection) | **Adapt**: dialog with a lazy tree via entity and collection endpoints | 3 | - |
| F11 | Delete object (Shift+Alt+F10), only when "editable results" is on; confirmation | `delete_object` 1458 | `delete(bean)` | **Adapt**: `DELETE /entities/{name}/{id}` | 3 | - |
| F12 | Groovy script on a column (Shift+Alt+F12): transform each cell with variable `x`; header marked `*` | `execute_script_on_column` 1508 | Groovy client-side | **Decide** (see 09, Q9): server-side Groovy on re-fetched objects, client-side JS on JSON values, or drop | 4 | - |
| F13 | Export table to clipboard (TSV) | `export_table_data_to_clipboard` 1690 (**not wired to any action**) | - | **Improve**: "Copy as TSV" in the context menu | 2 | - |
| F14 | Export the whole dataset to CSV (all rows) | `export_dataset_to_csv_file` 1631 (**not wired**, naive quoting) | `execute` with unlimited max | **Improve**: `POST /queries/export.csv` streaming download with RFC 4180 quoting | 2 | - |
| F15 | DML (`update ...`) shows the affected row count | `RunQuery` `isUpdateStatement` | - | **Improve**: also `delete` / `insert`; show "N rows affected" | 1 | - |

## G. Properties panel

| ID | Feature | Swing location | Web plan | Phase | Status |
|---|---|---|---|---|---|
| G1 | Property sheet of the selected object, sorted, grouped by category ("read-only", "editable", "no editor found") | `PropertyPanel.java` 171-249 | **Keep** | 1 (read-only) | - |
| G2 | Lazy, uninitialized values shown as `*LAZY*` | `PropertyPanel` 93-99, 195-203 | **Improve**: to-one references show `{entity,id}` without loading; collections get a "load" action (fresh session) | 1 / 3 | - |
| G3 | Editing (when "editable results" is on): String, numbers, BigInteger, BigDecimal, `Date`, `Timestamp`, Joda `LocalDate`/`LocalDateTime`, enums (combo) | `PropertyPanel` 155-230 | **Keep**, plus `java.time` types and Boolean | 3 | - |
| G4 | On change: set the value on the object, `save(bean)`, reload with `get(class,id)`, show a success dialog; show validation errors as a list | `PropertyListener` 623-667 | **Adapt**: `PATCH` with the changed field; toast on success; inline validation errors | 3 | - |
| G5 | Drag the property name out of the panel | 251-294 | see B24 | 3 | - |

## H. Favorites and history

| ID | Feature | Swing location | Service | Web plan | Phase | Status |
|---|---|---|---|---|---|---|
| H1 | Add to favorites (Alt+F5): asks for a name; warns when the last run did not succeed | `add_to_favorites` 1014 | files | **Keep**: `PUT /favorites/{name}` | 2 | - |
| H2 | Favorites dialog (Alt+F4): table with name, parameter preview, HQL preview; restore (double-click), delete, cancel | `favorites` 1714, `FavoritesDialog.java` | files | **Keep**, with a filter box. **Improve**: delete works (it is broken in Swing, see 10) | 2 | - |
| H3 | Auto-save `LAST` before each run; restore at startup | `preQuery` 2557, `loadFavorites` 2447 | files | **Keep**: `PUT /favorites/LAST`, `GET /favorites/LAST` | 1 | - |
| H4 | Favorites are scoped per project (`getProject()`) | `QueryFavoriteUtils` | `getProject` | **Keep**: `/data/favorites/<project>` in the environment's Docker volume; optionally bind-mounted to the Swing folder | 1 | - |
| H5 | Query history (undo through the history with Ctrl+Z/Y, described in `info.apt`) | not in the current code | - | **Improve** (optional): keep a list of the last N executed statements | 4 | - |
| H6 | Multiple editor tabs | not in Swing | - | **Improve** (optional): several HQL tabs, each with its own parameters | 4 | - |

## I. Settings

Swing stores all settings in Java Preferences. In the web version they move to browser
`localStorage` (see 06 §12). Server-enforced limits come from `/info`.

| ID | Setting | Swing default | Web plan |
|---|---|---|---|
| I1 | SQL mode | off | per-tab toggle (B22) |
| I2 | Format SQL | on | keep |
| I3 | Remove joins / format lines / replace properties | on / on / on | keep (sub-options of I2) |
| I4 | Language | system language, en fallback; restart required | keep (en, nl); switches live |
| I5 | Highlight syntax / colour (19,128,19) / continuous | on / green / on | keep highlight and colour; drop "continuous" (always live) |
| I6 | Highlight braces / colour (164,164,226) | on / lavender | keep |
| I7 | Resize columns | on | keep |
| I8 | Maximum number of results | 100 | keep; the server may cap it |
| I9 | Maximum number of search results | 2000 | keep |
| I10 | Font | DejaVu Sans Mono 16; restart required | keep (family and size), live |
| I11 | Always on top | off | drop |
| I12 | Editable results | off | keep; disabled when the server capability is off |
| I13 | Search colour (245,225,145) | yellow | keep |
| I14 | System tray | on | drop |
| I15 | Add end brace | on | keep |
| I16 | Show error tooltip | on | keep |
| I17 | Select HQL being executed | on | keep |
| I18 | Show toolbars | on | keep |
| I19 | Switch layout | on (layout a) | keep (A2) |
| I20 | Reset settings (restores the defaults above) | `start` 3120-3152 | keep |
| I21 | Theme (light/dark) | n/a | **new** |

## J. Help menu and information

| ID | Feature | Swing location | Service | Web plan | Phase | Status |
|---|---|---|---|---|---|---|
| J1 | Hibernate documentation, HQL documentation, Lucene query syntax (open in the browser) | `hibernate_documentation` 2068, `hql_documentation` 2309, `Lucene_query_syntax` 2454 | `get*HelpURL` | **Keep**: links. **Improve**: update the URLs (they point to Hibernate 4.3) | 2 | - |
| J2 | Help (F1) → GitHub wiki | `help` 2006, `ClientUtils.getHelpUrl` | - | **Keep** | 1 | - |
| J3 | Java system properties table | `java_properties` 2076 | - (client JVM) | **Adapt**: container JVM properties, capability `system-properties`, secrets masked | 4 | - |
| J4 | Versions dialog (Hibernate, annotations, validator, JPA) | `versions` 3343 | `getHibernateInfo` | **Keep** | 2 | - |
| J5 | About: version, latest release (from GitHub `maven-metadata.xml`), download link | `about` 963, `fetchLatestVersion` 1993 | - | **Keep**: `/info.latestVersion`, fetched server-side with a timeout and cached | 2 | - |

## K. Server features not exposed in the Swing UI

| ID | Feature | Service | Web plan |
|---|---|---|---|
| K1 | Translate HQL to SQL without executing | `getSqlForHql` | **Use** in `/hql/analyze` (autocomplete aliases, "show SQL" without running) |
| K2 | Schema DDL script | `createScript` | Optional admin viewer (phase 4) |
| K3 | Execute raw SQL statements | `sql(String[])` | **Do not expose** (plain SQL mode covers the use case; it also leaks connections, see 10) |
| K4 | Log Hibernate properties | `log()` | Skip |

## N. New in the web version (not in Swing)

These features follow from the per-environment Docker setup ([11](11-docker-packaging.md)) and the
login requirement.

| ID | Feature | Web plan | Phase | Status |
|---|---|---|---|---|
| N1 | Login with the environment's username and password; logout; on session expiry (401) go back to the login page and restore the editor state afterwards | login page, `/auth/login`, `/auth/logout`, `/auth/me` | 1 | - |
| N2 | Environment awareness: name and colour in the header and tab title; a "read-only" badge when the environment is read-only | from `/info.environment` | 1 | - |
| N3 | Capability-driven UI: hide or disable DML confirmation paths, SQL mode, Groovy mode, editing, schema script, system properties when the environment disables them | from `/info.capabilities` (the server enforces them anyway) | 1-3 | - |
| N4 | About / Versions show the environment info (name, config mode mounted/baked, Hibernate adapter, hql-builder version, Java version) | from `/info.environment` | 2 | - |
| N5 | Environment details dialog: the jars loaded from `/env/lib` (name, size, modified time), which config files and imports were found, and the origin of each required key (secrets masked). It makes a swapped model jar visible after `docker restart` | `GET /system/environment` | 2 | - |

## L. Keyboard shortcuts (Swing)

These are defined in the `HqlBuilderAction` constructors (lines 524-960). The proposed web mapping
is in [06-frontend-plan.md §14](06-frontend-plan.md#14-keyboard-shortcuts).

| Shortcut | Action |
|---|---|
| F1 | Help |
| Alt+X | Exit |
| Ctrl+Enter | Start query |
| Alt+F1 | Wizard |
| Alt+F2 | Clear |
| Alt+F3 | Find parameters |
| Alt+F4 | Favorites (**conflicts with closing the window on Windows**) |
| Alt+F5 | Add to favorites |
| Alt+F6 / F7 / F8 | free (comment in code) |
| Alt+F9 | Format |
| Alt+F10 | Load named query |
| Alt+F11 | Export HQL as Java |
| Alt+F12 | Import HQL from Java |
| Ctrl+Shift+Plus, Ctrl+Shift+Space | Insert helper |
| Ctrl+Shift+/ | Remark toggle |
| Ctrl+Delete | Delete inverted selection |
| Shift+Alt+F8 | Hibernate properties |
| Shift+Alt+F9 | Object tree |
| Shift+Alt+F10 | Delete object |
| Shift+Alt+F11 | Copy selected cell |
| Shift+Alt+F12 | Script on column |

## M. Summary counts

Counted over sections A-H and J. Settings (I), server-only features (K) and the web-only features
(N) are not included, and G5 is counted under B24.

| Keep | Adapt | Improve / new | Drop | Decide | Total |
|---|---|---|---|---|---|
| 57 | 11 | 12 | 3 | 1 | 84 |

The three dropped features are desktop-only: system tray, always on top, and exit (the container
is managed with Docker). Three settings are also dropped: always on top, system tray, and the
"continuous highlighting" toggle. The only open design question is F12 (column scripts).

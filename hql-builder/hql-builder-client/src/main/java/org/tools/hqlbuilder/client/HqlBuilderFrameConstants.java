package org.tools.hqlbuilder.client;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;

/**
 * @author Jurgen
 */
public interface HqlBuilderFrameConstants {
    public static final File USER_HOME_DIR = new File(System.getProperty("user.home"));

    public static final File PROGRAM_DIR = new File(USER_HOME_DIR, "hqlbuilder");

    public static final File FAVORITES_DIR = new File(PROGRAM_DIR, "favorites");

    public static final String PROJECT_META = "https://raw.githubusercontent.com/jurgendl/mvn-repo/master/releases/org/tools/hql-builder/hql-builder-client/maven-metadata.xml";

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public static final String PERSISTENT_LOCALE = "locale";

    public static final String FAVORITE_PREFIX = "favorite.";

    public static final String downloadLatestURI = "https://github.com/jurgendl/hql-builder/releases";

    public static final String ABOUT = "about";

    public static final String SET_NULL = "set null";

    public static final String TO_TEXT = "to text";

    public static final String IMPORT_PARAMETERS = "import parameters";

    public static final String FONT = "font";

    public static final String LOAD_NAMED_QUERY = "load named query";

    public static final String HIBERNATE_PROPERTIES = "hibernate properties";

    public static final String LUCENE_QUERY_SYNTAX = "Lucene query syntax";

    public static final String SEARCH_COLOR = "search color";

    public static final String HIGHLIGHT_SYNTAX_CONTINUOUS = "continuous syntax highlighting";

    public static final String HIGHLIGHT_SYNTAX_COLOR = "highlight syntax color";

    public static final String HIGHLIGHT_BRACES_COLOR = "highlight braces color";

    public static final String HIGHLIGHT_SYNTAX = "highlight syntax";

    public static final String HIGHLIGHT_BRACES = "highlight braces";

    public static final String RESIZE_COLUMNS = "resize columns";

    public static final String CSV = ".csv";

    public static final String FAVORITES_EXT = ".xml";

    public static final String LAST = "LAST";

    public static final String OBJECT_TREE = "object tree";

    public static final String HELP_INSERT = "help-insert";

    public static final String REMARK_TOGGLE = "remark-toggle";

    public static final String ADD_TO_FAVORITES = "add to favorites";

    public static final String FAVORITES = "favorites";

    public static final String FIND_PARAMETERS = "find parameters";

    public static final String MAXIMUM_NUMBER_OF_RESULTS = "maximum number of results";

    public static final String MAXIMUM_NUMBER_OF_SEARCH_RESULTS = "maximum number of search results";

    public static final String EDITABLE_RESULTS = "editable results";

    public static final String HQL_DOCUMENTATION = "hql documentation";

    public static final String HIBERNATE_DOCUMENTATION = "hibernate documentation";

    public static final String PERSISTENT_ID = "HQL Builder";

    public static final String NAME = "HQL Builder";

    public static final String SYSTEM_TRAY = "system tray";

    public static final String ALWAYS_ON_TOP = "always on top";

    public static final String EXECUTE_SCRIPT_ON_COLUMN = "execute script on column";

    public static final String DELETE_OBJECT = "delete object";

    public static final String COPY_SELECTED_CELL = "copy selected cell";

    public static final String HELP = "help";

    public static final String WIZARD = "wizard";

    public static final String UP = "up";

    public static final String SAVE = "save";

    public static final String REMOVE = "remove";

    public static final String DOWN = "down";

    public static final String IMMEDIATE_QUERY = "immediateQuery";

    public static final String FORMAT_SQL = "format sql";

    public static final String SHOW_TOOLBARS = "show toolbars";

    public static final String REPLACE_PROPERTIES = "replace properties";

    public static final String FORMAT_LINES = "format lines";

    public static final String REMOVE_JOINS = "remove joins";

    public static final String SWITCH_LAYOUT = "switch layout";

    public static final String FORCE_EXIT = "force exit";

    public static final String CLEAR = "clear";

    public static final String EXIT = "exit";

    public static final String IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD = "import: paste hql as java from clipboard";

    public static final String EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD = "export: copy hql as java to clipboard";

    public static final String FORMAT = "format";

    public static final String START_QUERY = "start query";

    public static final String STOP_QUERY = "stop query";

    public static final String COL = "col";

    public static final String ROW = "row";

    public static final String DELETE_INVERTED_SELECTION = "delete inverted selection";

    public static final String ADD_PARAMETER = "add parameter";

    public static final String CAN_EXECUTE_SELECTION = "can execute selection";

    public static final String VERSIONS = "versions";

    public static final String ADD_END_BRACE = "add end brace";

    public static final String SHOW_ERROR_TOOLTIP = "show error tooltip";

    public static final String SELECT_HQL_BEING_EXECUTED = "select hql being executed";

    public static final String LINESEPERATOR = "\\r?\\n";

    public static final String REMARKTAG = "--";

    public static final Serializable SERIALIZABLE = new Serializable() {
        private static final long serialVersionUID = 1L;
    };
}

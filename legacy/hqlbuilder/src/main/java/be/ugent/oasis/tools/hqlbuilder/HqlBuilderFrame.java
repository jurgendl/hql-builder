package be.ugent.oasis.tools.hqlbuilder;

import static org.swingeasy.system.SystemSettings.getTmpdir;
import groovy.util.Eval;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.DocumentBuilderFactory;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.NamedQueryDefinition;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.hibernate.impl.QueryImpl;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.OneToOneType;
import org.hibernate.type.Type;
import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;
import org.swingeasy.CustomizableOptionPane;
import org.swingeasy.DocumentKeyListener;
import org.swingeasy.EButton;
import org.swingeasy.ECheckBox;
import org.swingeasy.EComponentPopupMenu;
import org.swingeasy.ELabel;
import org.swingeasy.ELabeledTextFieldButtonComponent;
import org.swingeasy.EList;
import org.swingeasy.EListConfig;
import org.swingeasy.EListRecord;
import org.swingeasy.EProgressBar;
import org.swingeasy.ETable;
import org.swingeasy.ETableConfig;
import org.swingeasy.ETableHeaders;
import org.swingeasy.ETableRecord;
import org.swingeasy.ETableRecordCollection;
import org.swingeasy.ETextArea;
import org.swingeasy.ETextAreaBorderHighlightPainter;
import org.swingeasy.ETextAreaConfig;
import org.swingeasy.ETextAreaFillHighlightPainter;
import org.swingeasy.ETextField;
import org.swingeasy.ETextFieldConfig;
import org.swingeasy.EToggleToolBarButton;
import org.swingeasy.EToolBarButtonCustomizer;
import org.swingeasy.EventHelper;
import org.swingeasy.EventModifier;
import org.swingeasy.ListOptionPaneCustomizer;
import org.swingeasy.MessageType;
import org.swingeasy.ObjectWrapper;
import org.swingeasy.OptionType;
import org.swingeasy.ProgressGlassPane;
import org.swingeasy.ResultType;
import org.swingeasy.UIUtils;

import be.ugent.oasis.tools.hqlbuilder.HqlWizard.HqlWizardListener;

/**
 * HqlBuilder
 * 
 * @author jdlandsh
 */
public class HqlBuilderFrame {
    private static final String LOAD_NAMED_QUERY = "load named query";

    private static final String HIBERNATE_PROPERTIES = "hibernate properties";

    private static final String LUCENE_QUERY_SYNTAX = "Lucene query syntax";

    private static final String SEARCH_COLOR = "search color";

    private static final String HIGHLIGHT_COLOR = "highlight color";

    ca.odell.glazedlists.swing.EventSelectionModel<?> wtf;

    private static final String HIGHLIGHT_SYNTAX = "highlight syntax";

    private static final String RESIZE_COLUMNS = "resize columns";

    private static final String CSV = ".csv";

    private static final String FAVORITES_DIR = ".settings/.favorites";

    private static final String FAVORITES_EXT = ".xml";

    private static final String LAST = "LAST";

    private static final String OBJECT_TREE = "object tree";

    private static final String NEWLINE = "\n";

    private static final String HELP_INSERT = "help-insert";

    private static final String REMARK_TOGGLE = "remark-toggle";

    private static final String ADD_TO_FAVORITES = "add to favorites";

    private static final String FAVORITES = "favorites";

    private static final String FIND_PARAMETERS = "find parameters";

    private static final String MAXIMUM_NUMBER_OF_RESULTS = "maximum number of results";

    private static final String EDITABLE_RESULTS = "editable results";

    private static final String SERVICE_NAME = "be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper";

    private static final String NEW_PROGRESSION_MARKER = "new progression marker";

    private static final String OFFLINE_DOCUMENTATION = "offline documentation";

    private static final String HQL_DOCUMENTATION = "hql documentation";

    private static final String HIBERNATE_DOCUMENTATION = "hibernate documentation";

    private static final String PERSISTENT_ID = "HqlBuilder2";

    private static final String NAME = "HqlBuilder v2.0";

    private static final String SYSTEM_TRAY = "system tray";

    private static final String ALWAYS_ON_TOP = "always on top";

    private static final String EXECUTE_SCRIPT_ON_COLUMN = "execute script on column";

    private static final String DELETE_OBJECT = "delete object";

    private static final String COPY_SELECTED_CELL = "copy selected cell";

    private static final String HELP = "help";

    private static final String WIZARD = "wizard";

    private static final String UP = "up";

    private static final String SAVE = "save";

    private static final String REMOVE = "remove";

    private static final String DOWN = "down";

    private static final String IMMEDIATE_QUERY = "immediateQuery";

    private static final String FORMAT_SQL = "format sql";

    private static final String REPLACE_PROPERTIES = "replace properties";

    private static final String FORMAT_LINES = "format lines";

    private static final String REMOVE_ADO_JOINS = "remove ADO joins";

    private static final String SWITCH_LAYOUT = "switch layout";

    private static final String FORCE_EXIT = "force exit";

    private static final String CLEAR = "clear";

    private static final String EXIT = "exit";

    private static final String IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD = "import: paste hql as java from clipboard";

    private static final String EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD = "export: copy hql as java to clipboard";

    private static final String FORMAT = "format";

    private static final String STOP_QUERY = "stop query";

    private static final String START_QUERY = "start query";

    private static final String PAUSE_QUERY = "pause query";

    private static IHqlBuilderHelper hqlBuilderHelper;

    private static final String JAVASCRIPT = "javascript";

    private static final String JAVA = "java";

    private static final String GROOVY = "groovy";

    private static final String FROM_ALIASES = "from_aliases";

    private static final String SELECTED = "selected";

    private static final String VALUE = "value";

    private static final String SCRIPT = "script";

    private static final String COL = "col";

    private static final String ROW = "row";

    @SuppressWarnings("unused")
    private org.swingeasy.UIExceptionHandler uiExceptionHandler = org.swingeasy.UIExceptionHandler.getInstance();

    private final JFrame frame = new JFrame();

    private final JTabbedPane hql_sql_tabs = new JTabbedPane();

    private final ELabel resultsInfo = font(new ELabel(""), null);

    private final JPanel propertypanel = new JPanel(new BorderLayout());

    private final ETextField parameterBuilder = font(new ETextField(new ETextFieldConfig()), 12);

    private final ETextField parameterName = font(new ETextField(new ETextFieldConfig()), 12);

    private final ETextField parameterValue = font(new ETextField(new ETextFieldConfig(false)), 12);

    private final ETextArea hql = font(new ETextArea(new ETextAreaConfig()), 12);

    private final ETextArea sql = font(new ETextArea(new ETextAreaConfig(false)), 12);

    // not EDT safe
    private final EList<QueryParameter> parametersUnsafe = font(new EList<QueryParameter>(new EListConfig()), null);

    // EDT safe
    private final EList<QueryParameter> parametersEDT = parametersUnsafe.stsi();

    // not EDT safe
    private final ETable<List<Object>> resultsUnsafe = font(new ETable<List<Object>>(new ETableConfig(true)), null);

    // EDT safe
    private final ETable<List<Object>> resultsEDT = resultsUnsafe.stsi();

    // results

    private final HqlBuilderAction hibernatePropertiesAction = new HqlBuilderAction(resultsUnsafe, this, HIBERNATE_PROPERTIES, true,
            HIBERNATE_PROPERTIES, "page_white_stack.png", HIBERNATE_PROPERTIES, HIBERNATE_PROPERTIES, true, null, "shift alt F8");

    private final HqlBuilderAction objectTreeAction = new HqlBuilderAction(resultsUnsafe, this, OBJECT_TREE, true, OBJECT_TREE,
            "application_side_tree.png", OBJECT_TREE, OBJECT_TREE, true, null, "shift alt F9");

    private final HqlBuilderAction deleteObjectAction = new HqlBuilderAction(resultsUnsafe, this, DELETE_OBJECT, true, DELETE_OBJECT,
            "bin_empty.png", DELETE_OBJECT, DELETE_OBJECT, true, null, "shift alt F10");

    private final HqlBuilderAction copyCellAction = new HqlBuilderAction(resultsUnsafe, this, COPY_SELECTED_CELL, true, COPY_SELECTED_CELL,
            "cell_layout.png", COPY_SELECTED_CELL, COPY_SELECTED_CELL, true, null, "shift alt F11");

    private final HqlBuilderAction executeScriptOnColumnAction = new HqlBuilderAction(resultsUnsafe, this, EXECUTE_SCRIPT_ON_COLUMN, true,
            EXECUTE_SCRIPT_ON_COLUMN, "groovy.png", EXECUTE_SCRIPT_ON_COLUMN, EXECUTE_SCRIPT_ON_COLUMN, true, null, "shift alt F12");

    // parameters

    private final HqlBuilderAction downAction = new HqlBuilderAction(parametersUnsafe, this, DOWN, true, DOWN, "bullet_arrow_down.png", DOWN, DOWN,
            false, null, null);

    private final HqlBuilderAction removeAction = new HqlBuilderAction(parametersUnsafe, this, REMOVE, true, REMOVE, "bin_empty.png", REMOVE, REMOVE,
            false, null, null);

    private final HqlBuilderAction saveAction = new HqlBuilderAction(parametersUnsafe, this, SAVE, true, SAVE, "disk.png", SAVE, SAVE, false, null,
            null);

    private final HqlBuilderAction upAction = new HqlBuilderAction(parametersUnsafe, this, UP, true, UP, "bullet_arrow_up.png", UP, UP, false, null,
            null);

    // hql

    private final HqlBuilderAction wizardAction = new HqlBuilderAction(hql, this, WIZARD, true, WIZARD, "wizard.png", WIZARD, WIZARD, true, null,
            "alt F1");

    private final HqlBuilderAction clearAction = new HqlBuilderAction(hql, this, CLEAR, true, CLEAR, "bin_empty.png", CLEAR, CLEAR, true, 'r',
            "alt F2");

    private final HqlBuilderAction findParametersAction = new HqlBuilderAction(hql, this, FIND_PARAMETERS, true, FIND_PARAMETERS, "book_next.png",
            FIND_PARAMETERS, FIND_PARAMETERS, true, null, "alt F3");

    private final HqlBuilderAction favoritesAction = new HqlBuilderAction(hql, this, FAVORITES, true, FAVORITES, "favb16.png", FAVORITES, FAVORITES,
            true, 'h', "alt F4");

    private final HqlBuilderAction addToFavoritesAction = new HqlBuilderAction(hql, this, ADD_TO_FAVORITES, true, ADD_TO_FAVORITES,
            "database_save.png", ADD_TO_FAVORITES, ADD_TO_FAVORITES, true, null, "alt F5");

    private final HqlBuilderAction startQueryAction = new HqlBuilderAction(hql, this, START_QUERY, true, START_QUERY, "control_play_blue.png",
            START_QUERY, START_QUERY, true, 'q', "alt F6");

    private final HqlBuilderAction pauseQueryAction = new HqlBuilderAction(hql, this, PAUSE_QUERY, true, PAUSE_QUERY, "control_pause_blue.png",
            PAUSE_QUERY, PAUSE_QUERY, false, 'u', "alt F7");

    private final HqlBuilderAction stopQueryAction = new HqlBuilderAction(hql, this, STOP_QUERY, true, STOP_QUERY, "control_stop_blue.png",
            STOP_QUERY, STOP_QUERY, true, 's', "alt F8");

    private final HqlBuilderAction formatAction = new HqlBuilderAction(hql, this, FORMAT, true, FORMAT, "text_align_justify.png", FORMAT, FORMAT,
            true, 'o', "alt F9");

    private final HqlBuilderAction namedQueryAction = new HqlBuilderAction(hql, this, LOAD_NAMED_QUERY, true, LOAD_NAMED_QUERY, "cog.png",
            LOAD_NAMED_QUERY, LOAD_NAMED_QUERY, true, null, "alt F10");

    private final HqlBuilderAction clipboardExportAction = new HqlBuilderAction(hql, this, EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, true,
            EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, "sc_arrowshapes.striped-right-arrow.png", EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD,
            EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, true, 'e', "alt F11");

    private final HqlBuilderAction clipboardImportAction = new HqlBuilderAction(hql, this, IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, true,
            IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, "sc_arrowshapes.striped-left-arrow.png", IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD,
            IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, true, 'i', "alt F12");

    private final HqlBuilderAction helpInsertAction = new HqlBuilderAction(hql, this, HELP_INSERT, true, HELP_INSERT, null, HELP_INSERT, HELP_INSERT,
            true, 'i', "ctrl shift Space");

    private final HqlBuilderAction remarkToggleAction = new HqlBuilderAction(hql, this, REMARK_TOGGLE, true, REMARK_TOGGLE, null, REMARK_TOGGLE,
            REMARK_TOGGLE, true, 'r', "ctrl shift Colon");

    // existing

    private final HqlBuilderAction helpAction = new HqlBuilderAction(null, this, HELP, true, HELP, "help16.png", HELP, HELP, true, 'h', "F1");

    private final HqlBuilderAction exitAction = new HqlBuilderAction(null, this, EXIT, true, EXIT, "sc_quit.png", EXIT, EXIT, true, 'x', "alt X");

    // not accelerated

    private final HqlBuilderAction helpHibernateAction = new HqlBuilderAction(null, this, HIBERNATE_DOCUMENTATION, true, HIBERNATE_DOCUMENTATION,
            "help16.png", HIBERNATE_DOCUMENTATION, HIBERNATE_DOCUMENTATION, true, null, null);

    private final HqlBuilderAction helpHqlAction = new HqlBuilderAction(null, this, HQL_DOCUMENTATION, true, HQL_DOCUMENTATION, "help16.png",
            HQL_DOCUMENTATION, HQL_DOCUMENTATION, true, null, null);

    private final HqlBuilderAction helpOfflineAction = new HqlBuilderAction(null, this, OFFLINE_DOCUMENTATION, true, OFFLINE_DOCUMENTATION,
            "help16.png", OFFLINE_DOCUMENTATION, OFFLINE_DOCUMENTATION, true, null, null);

    private final HqlBuilderAction luceneQuerySyntaxAction = new HqlBuilderAction(null, this, LUCENE_QUERY_SYNTAX, true, LUCENE_QUERY_SYNTAX,
            "help16.png", LUCENE_QUERY_SYNTAX, LUCENE_QUERY_SYNTAX, true, null, null);

    private final HqlBuilderAction forceExitAction = new HqlBuilderAction(null, this, FORCE_EXIT, true, FORCE_EXIT, "exit.png", FORCE_EXIT,
            FORCE_EXIT, true, 't', null);

    private final HqlBuilderAction removeADOJoinsAction = new HqlBuilderAction(null, this, IMMEDIATE_QUERY, true, REMOVE_ADO_JOINS, null,
            REMOVE_ADO_JOINS, REMOVE_ADO_JOINS, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction formatLinesAction = new HqlBuilderAction(null, this, IMMEDIATE_QUERY, true, FORMAT_LINES, null, FORMAT_LINES,
            FORMAT_LINES, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction replacePropertiesAction = new HqlBuilderAction(null, this, IMMEDIATE_QUERY, true, REPLACE_PROPERTIES, null,
            REPLACE_PROPERTIES, REPLACE_PROPERTIES, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction resizeColumnsAction = new HqlBuilderAction(null, this, RESIZE_COLUMNS, true, RESIZE_COLUMNS, null, RESIZE_COLUMNS,
            RESIZE_COLUMNS, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction formatSqlAction = new HqlBuilderAction(null, this, FORMAT_SQL, true, FORMAT_SQL, null, FORMAT_SQL, FORMAT_SQL,
            true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction maximumNumberOfResultsAction = new HqlBuilderAction(null, this, MAXIMUM_NUMBER_OF_RESULTS, true,
            MAXIMUM_NUMBER_OF_RESULTS, null, MAXIMUM_NUMBER_OF_RESULTS, MAXIMUM_NUMBER_OF_RESULTS, true, null, null, PERSISTENT_ID, Integer.class,
            100);

    private final HqlBuilderAction groovyAction = new HqlBuilderAction(null, this, null, true, GROOVY, null, GROOVY, GROOVY, true, null, null,
            PERSISTENT_ID);

    private final HqlBuilderAction javaAction = new HqlBuilderAction(null, this, null, true, JAVA, null, JAVA, JAVA, false, null, null, PERSISTENT_ID);

    private final HqlBuilderAction javascriptAction = new HqlBuilderAction(null, this, null, true, JAVASCRIPT, null, JAVASCRIPT, JAVASCRIPT, false,
            null, null, PERSISTENT_ID);

    private final HqlBuilderAction systrayAction = new HqlBuilderAction(null, this, null, true, SYSTEM_TRAY, null, SYSTEM_TRAY, SYSTEM_TRAY, true,
            null, null, PERSISTENT_ID);

    private final HqlBuilderAction hiliAction = new HqlBuilderAction(null, this, null, true, HIGHLIGHT_SYNTAX, null, HIGHLIGHT_SYNTAX,
            HIGHLIGHT_SYNTAX, false, null, null, PERSISTENT_ID);

    private final HqlBuilderAction hiliColorAction = new HqlBuilderAction(null, this, HIGHLIGHT_COLOR, true, HIGHLIGHT_COLOR, null, HIGHLIGHT_COLOR,
            HIGHLIGHT_COLOR, false, null, null, PERSISTENT_ID, Color.class, null);

    private final HqlBuilderAction searchColorAction = new HqlBuilderAction(null, this, SEARCH_COLOR, true, SEARCH_COLOR, null, SEARCH_COLOR,
            SEARCH_COLOR, false, null, null, PERSISTENT_ID, Color.class, null);

    private final HqlBuilderAction alwaysOnTopAction = new HqlBuilderAction(null, this, ALWAYS_ON_TOP, true, ALWAYS_ON_TOP, null, ALWAYS_ON_TOP,
            ALWAYS_ON_TOP, false, null, null, PERSISTENT_ID);

    private final HqlBuilderAction editableResultsAction = new HqlBuilderAction(null, this, null, true, EDITABLE_RESULTS, null, EDITABLE_RESULTS,
            EDITABLE_RESULTS, false, null, null, PERSISTENT_ID);

    private final HqlBuilderAction newProgressAction = new HqlBuilderAction(null, this, null, true, NEW_PROGRESSION_MARKER, null,
            NEW_PROGRESSION_MARKER, NEW_PROGRESSION_MARKER, false, null, null, PERSISTENT_ID);

    private final HqlBuilderAction switchLayoutAction = new HqlBuilderAction(null, this, SWITCH_LAYOUT, true, SWITCH_LAYOUT, "layout_content.png",
            SWITCH_LAYOUT, SWITCH_LAYOUT, false, 'w', null, PERSISTENT_ID);

    private final EProgressBar progressbar = new EProgressBar() {
        private static final long serialVersionUID = -1377265196757589394L;

        private boolean init = false;

        {
            init = true;
        }

        @Override
        public void setIndeterminate(boolean newValue) {
            super.setIndeterminate(newValue);
            if (init) {
                setGlassVisible(newValue);
            }
        };
    };

    private final ELabel maxResults = font(new ELabel(" / " + maximumNumberOfResultsAction.getValue()), null);

    private final LinkedList<QueryFavorite> favorites = new LinkedList<QueryFavorite>();

    private final JComponent values = hqlBuilderHelper.getPropertyFrame(null, new Object(), false);

    private final Timer timer = new Timer(true);

    private final ValueHolders valueHolders = new ValueHolders();

    private final JPanel resultPanel = new JPanel(new BorderLayout());

    private final JPanel parameterspanel = new JPanel(new MigLayout("wrap 3", "[]rel[grow]rel[]", "[][][][shrink][shrink][shrink][grow]"));

    private final JPanel normalContentPane = new JPanel(new BorderLayout());

    private boolean test = false;

    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private final JMenu formatSqlOptionsMenu = new JMenu(HqlResourceBundle.getMessage("format sql options"));

    private HibernateWebResolver meta;

    private EList<String> insertPropertyHelper;

    private JPopupMenu insertClassHelper;

    private ScheduledExecutorService scheduler;

    private EList<String> insertHelperList;

    private JPopupMenu insertHelper;

    private ScheduledFuture<Object> scheduledFuture;

    private SessionFactory sessionFactory;

    private TimerTask task;

    private JSplitPane split0;

    private JSplitPane split1;

    private JSplitPane split2;

    private ProgressGlassPane glass = null;

    private ETextAreaBorderHighlightPainter hili = new ETextAreaBorderHighlightPainter(getHiliColor());

    private ETextAreaBorderHighlightPainter hiko = new ETextAreaBorderHighlightPainter(getHiliColor());

    private TrayIcon trayIcon;

    private Information information;

    private Map<String, String> namedQueries = new HashMap<String, String>();

    private HqlBuilderFrame() {
        super();
    }

    protected void down() {
        parametersEDT.moveSelectedDown();
    }

    protected void up() {
        parametersEDT.moveSelectedUp();
    }

    protected void clear() {
        scheduleCancel();

        resultsInfo.setText("");
        parametersEDT.removeAllRecords();
        parameterBuilder.setText("");
        parameterName.setText("");
        parameterValue.setText("");
        hql.setText("");
        sql.setText("");
        valueHolders.clear();
        clearResults();
        propertypanel.add(hqlBuilderHelper.getPropertyFrame(null, new Object(), false), BorderLayout.CENTER);
        propertypanel.revalidate();
        hql_sql_tabs.setForegroundAt(1, Color.gray);
    }

    private void clearResults() {
        resultsEDT.setHeaders(new ETableHeaders<List<Object>>());
        resultsEDT.removeAllRecords();
    }

    protected void favorites() {
        try {
            FavoritesDialog dialog = new FavoritesDialog(frame, favorites);
            dialog.setTitle(HqlResourceBundle.getMessage("favorites.long"));
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            QueryFavorite selection = dialog.getSelection();

            if (selection != null) {
                importFromFavorites(selection);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    private void log(Object message) {
        hqlBuilderHelper.log(message);
    }

    private void importFromFavorites(QueryFavorite selection) {
        clear();
        importFromFavoritesNoQ(selection);
        scheduleQuery(0l, false);
    }

    private void importFromFavoritesNoQ(QueryFavorite selection) {
        hql.setText(selection.getFull());

        if (selection.getParameters() != null) {
            for (QueryParameter p : selection.getParameters()) {
                parametersEDT.addRecord(new EListRecord<QueryParameter>(p));
            }
        }
    }

    private void compile(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressbar.setIndeterminate(true);
                log("progress: compiling");
                progressbar.setString("compiling");
            }
        });

        log("compiling");

        valueHolders.put(VALUE, null);
        parameterValue.setText("");

        if (task != null) {
            task.cancel();
        }

        task = new TimerTask() {
            @Override
            public void run() {
                String type = GROOVY;

                try {
                    Object returns = null;

                    try {
                        log("java:" + javaAction.isSelected());
                        log("javascript:" + javascriptAction.isSelected());
                        log("groovy:" + groovyAction.isSelected());
                        if (javaAction.isSelected()) {
                            returns = RuntimeCompiler.eval(text);
                            type = JAVA;
                        } else if (javascriptAction.isSelected()) {
                            returns = JavaScriptCompiler.eval(text);
                            type = JAVASCRIPT;
                        } else if (groovyAction.isSelected()) {
                            returns = GroovyCompiler.eval(text);
                        } else {
                            throw new RuntimeException("not implemented");
                        }
                    } catch (Exception ex) {
                        log(ex);
                        returns = text;
                    }

                    valueHolders.put(VALUE, returns);
                } catch (Exception ex2) {
                    log(ex2);
                    valueHolders.put(VALUE, null);
                }

                parameterValue.setText(new QueryParameter(null, null, valueHolders.get(VALUE), type).toString());

                log("compiled: " + valueHolders.get(VALUE));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressbar.setIndeterminate(false);
                        progressbar.setString("");
                        log("progress: null");
                    }
                });
            }
        };

        log("java:" + javaAction.isSelected());
        log("javascript:" + javascriptAction.isSelected());
        log("groovy:" + groovyAction.isSelected());

        if (javaAction.isSelected()) {
            timer.schedule(task, 1500);
        } else if (javascriptAction.isSelected()) {
            timer.schedule(task, 750);
        } else if (groovyAction.isSelected()) {
            timer.schedule(task, 750);
        } else {
            throw new RuntimeException("not implemented");
        }
    }

    private void scheduleQuery(Long delay, final boolean equalsCheck) {
        if (hql.getText().trim().length() < 5) {
            return;
        }

        log("scheduleQuery");

        syntaxHiRemove();
        syntaxHi();

        if (delay == null) {
            delay = 1500l;
        }

        schedule(delay, new Runnable() {
            @Override
            public void run() {
                query(equalsCheck);
            }
        });
    }

    private JPopupMenu getInsertHelperProperties() {
        if (insertClassHelper == null) {
            insertClassHelper = new JPopupMenu();

            insertPropertyHelper = new EList<String>(new EListConfig());

            JScrollPane scroll = new JScrollPane(insertPropertyHelper);
            insertClassHelper.add(scroll);

            insertClassHelper.setPreferredSize(new Dimension(300, 150));
            insertClassHelper.setSize(new Dimension(300, 150));
            insertClassHelper.setMinimumSize(new Dimension(300, 150));

            insertPropertyHelper.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if (EventHelper.isMouse1x1(e)) {
                            insertPropertyHelp();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                    }
                }
            });
        }

        return insertClassHelper;
    }

    private void insertPropertyHelp() throws Exception {
        String prop = insertPropertyHelper.getSelectedRecord().get();
        hql.getDocument().insertString(hql.getCaretPosition(), prop, null);
        insertClassHelper.setVisible(false);
    }

    private JPopupMenu getInsertHelperClasses() {
        if (insertHelper == null) {
            insertHelper = new JPopupMenu();

            EList<String> list = new EList<String>(new EListConfig());
            insertHelperList = font(list, null);

            JScrollPane scroll = new JScrollPane(insertHelperList);
            insertHelper.add(scroll);

            TreeSet<String> options = new TreeSet<String>();

            for (be.ugent.oasis.tools.hqlbuilder.HibernateWebResolver.ClassNode node : meta.getClasses()) {
                String clazz = node.getId();
                String option = clazz.substring(clazz.lastIndexOf('.') + 1) + " (" + clazz.substring(0, clazz.lastIndexOf('.')) + ")";
                options.add(option);
            }

            for (String option : options) {
                list.addRecord(new EListRecord<String>(option));
            }

            insertHelper.setPreferredSize(new Dimension(500, 200));
            insertHelper.setSize(new Dimension(500, 200));
            insertHelper.setMinimumSize(new Dimension(500, 200));

            insertHelperList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if (EventHelper.isMouse1x1(e)) {
                            insertHelp();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                    }
                }
            });
        }

        return insertHelper;
    }

    private void insertHelp() throws Exception {
        String clazz = insertHelperList.getSelectedRecord().get();
        clazz = clazz.substring(0, clazz.indexOf(" ")).trim();
        hql.getDocument().insertString(hql.getCaretPosition(), clazz, null);
        insertHelper.setVisible(false);
    }

    private void helpInsertClass() {
        getInsertHelperClasses().show(hql, (int) hql.getCaret().getMagicCaretPosition().getX(), (int) hql.getCaret().getMagicCaretPosition().getY());
        insertHelperList.grabFocus();
    }

    private void helpInsertProperty(String before) {
        int pos = Math.max(0, Math.max(before.lastIndexOf(getNewline()), before.lastIndexOf(' ')));
        before = before.substring(pos + 1, before.length() - 1);

        String[] parts = before.split("\\Q.\\E");

        Map<?, ?> allClassMetadata = sessionFactory.getAllClassMetadata();
        AbstractEntityPersister persister = (AbstractEntityPersister) allClassMetadata.get(Map.class.cast(valueHolders.get(FROM_ALIASES)).get(
                parts[0]));

        for (int i = 1; i < parts.length; i++) {
            int index = -1;
            String[] propertyNames = persister.getClassMetadata().getPropertyNames();

            for (int j = 0; (j < propertyNames.length) && (index == -1); j++) {
                if (propertyNames[j].equals(parts[i])) {
                    index = j;
                }
            }

            Type type = persister.getClassMetadata().getPropertyTypes()[index];

            if (type instanceof ManyToOneType) {
                String nextClass = ManyToOneType.class.cast(type).getAssociatedEntityName();
                persister = (AbstractEntityPersister) allClassMetadata.get(nextClass);
            } else if (type instanceof OneToOneType) {
                String nextClass = OneToOneType.class.cast(type).getAssociatedEntityName();
                persister = (AbstractEntityPersister) allClassMetadata.get(nextClass);
            } else {
                throw new RuntimeException(type.toString());
            }
        }

        JPopupMenu insertHelper2local = getInsertHelperProperties();

        insertPropertyHelper.removeAllRecords();

        for (String prop : new TreeSet<String>(Arrays.asList(persister.getPropertyNames()))) {
            if (!prop.startsWith("_")) {
                insertPropertyHelper.addRecord(new EListRecord<String>(prop));
            }
        }

        insertHelper2local.show(hql, (int) hql.getCaret().getMagicCaretPosition().getX(), (int) hql.getCaret().getMagicCaretPosition().getY());
        insertPropertyHelper.grabFocus();
    }

    protected static String getNewline() {
        return NEWLINE;
    }

    private void helpInsert() {
        String before;
        try {
            before = getHqlText().substring(0, hql.getCaret().getDot());
        } catch (StringIndexOutOfBoundsException ex) {
            before = getHqlText().substring(0, hql.getCaret().getDot() - 1) + " ";
        }
        if (before.toLowerCase().endsWith("from ")) {
            helpInsertClass();
        } else if (before.endsWith(".")) {
            helpInsertProperty(before);
        }
    }

    private String remarkToggle(String hqltext, int selectionStart, int selectionEnd) {
        if (selectionStart != 0) {
            int lastEnter = hqltext.lastIndexOf(getNewline(), selectionStart);

            if (lastEnter == -1) {
                selectionStart = 0;
            } else {
                selectionStart = lastEnter;
            }
        }

        if (selectionEnd != hqltext.length()) {
            int nextEnter = hqltext.indexOf(getNewline(), selectionEnd);

            if (nextEnter == -1) {
                selectionEnd = hqltext.length();
            } else {
                selectionEnd = nextEnter;
            }
        }

        String voor = hqltext.substring(0, selectionStart);
        String geselecteerd = hqltext.substring(selectionStart, selectionEnd);
        String na = hqltext.substring(selectionEnd);

        StringBuilder sb = new StringBuilder();

        if (geselecteerd.contains("--") || geselecteerd.contains("//")) {
            sb.append(voor);
            sb.append(geselecteerd.replaceAll("--", "").replaceAll("//", ""));
            sb.append(na);
        } else {
            if (selectionStart == 0) {
                sb.append("--");
            } else {
                sb.append(voor);
            }

            String[] lijnen = geselecteerd.split(getNewline());

            for (int i = 0; i < (lijnen.length - 1); i++) {
                sb.append(lijnen[i]).append(getNewline()).append("--");
            }

            sb.append(lijnen[lijnen.length - 1]);

            sb.append(na);
        }

        return sb.toString();
    }

    /**
     * start app
     * 
     * @param args
     */
    public static void start(String[] args) {
        try {
            File oldfavs = new File("favorites");
            if (oldfavs.exists()) {
                oldfavs.renameTo(new File(FAVORITES_DIR));
            }

            // zet look en feel gelijk aan default voor OS
            UIUtils.niceLookAndFeel();

            {
                // inladen configuratie class (class die interface met deze naam implementeert)
                InputStream helperin = HqlBuilderFrame.class.getClassLoader().getResourceAsStream(SERVICE_NAME);

                if (helperin == null) {
                    // wanneer niet teruggevonden: vraag om model jar (moet ook config bevatten) of maven assembly (moet ZIP zijn)
                    loadModelAtRuntime();
                    helperin = HqlBuilderFrame.class.getClassLoader().getResourceAsStream(SERVICE_NAME);
                }

                // faalt: exit
                if (helperin == null) {
                    System.exit(-1);
                    return;
                }

                // nu kunnen we eindelijk de configuratie inlezen
                byte[] tmp = new byte[helperin.available()];
                helperin.read(tmp);
                helperin.close();
                hqlBuilderHelper = (IHqlBuilderHelper) Class.forName(new String(tmp)).newInstance();
            }

            {
                // dtd validatie uitschakelen
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                // factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

                // unhandled exceptions toch opvangen
                UIUtils.registerDefaultUncaughtExceptionHandler();

                // tooltip cfg: sneller en langer tonen
                UIUtils.setLongerTooltips();
            }

            // lees properties in
            Properties properties = new Properties();

            // configuratieclass geeft locaties terug
            for (String rl : hqlBuilderHelper.getResourceLocations()) {
                try {
                    hqlBuilderHelper.log("try loading classpath resource " + rl);
                    // probeer als classpath resource in te lezen
                    InputStream in = HqlBuilderFrame.class.getClassLoader().getResourceAsStream(rl);
                    if (in == null) {
                        // wanneer niet gevonden, probeer als file (relatief/absolutt)
                        File file = new File(rl);
                        hqlBuilderHelper.log("try loading file " + file.getCanonicalPath());

                        if (file.exists()) {
                            in = new FileInputStream(file);
                            hqlBuilderHelper.log("file found " + file.getCanonicalPath() + " wordt ingelezen");
                        }
                    } else {
                        hqlBuilderHelper.log("classpath resource found " + rl + " wordt ingelezen");
                    }
                    if (in != null) {
                        // alles gelukt: laadt in
                        properties.load(in);
                        in.close();
                    } else {
                        hqlBuilderHelper.log("resource/file not found");
                    }
                } catch (Exception ex) {
                    hqlBuilderHelper.log(ex);
                }
            }

            // wanneer argumenten mee gegeven ("key=value") zijn worden die ook in de properties gestoken
            for (String arg : args) {
                String[] parts = arg.split("=");
                properties.put(parts[0], parts[1]);
            }

            // custom configuratie
            hqlBuilderHelper.onInit(properties);

            // log connectie
            hqlBuilderHelper.log(properties.get("hibernate.connection.username") + "@" + properties.getProperty("hibernate.connection.url"));

            // maak frame en start start
            final HqlBuilderFrame hqlBuilder = new HqlBuilderFrame();
            hqlBuilder.startPre(properties);
            hqlBuilder.start(properties);
            hqlBuilder.hql.grabFocus();

            // wanneer system tray wordt ondersteund, gebruikt deze dan (configureerbaar)
            if (SystemTray.isSupported()) {
                ActionListener listener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hqlBuilder.switchVisibility();
                    }
                };
                hqlBuilder.trayIcon = new TrayIcon(hqlBuilder.frame.getIconImage(), hqlBuilder.frame.getTitle());
                PopupMenu popupmenu = new PopupMenu(hqlBuilder.frame.getTitle());
                {
                    MenuItem switchvisibility = new MenuItem(HqlResourceBundle.getMessage("show"));
                    switchvisibility.addActionListener(listener);
                    popupmenu.add(switchvisibility);
                }
                {

                    MenuItem trayswitch = new MenuItem(HqlResourceBundle.getMessage("disable systemtray"));
                    trayswitch.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean newvalue = !hqlBuilder.systrayAction.isSelected();
                            hqlBuilder.systrayAction.setSelected(newvalue);
                            if (newvalue && !hqlBuilder.frame.isVisible()) {
                                hqlBuilder.switchVisibility();
                            }
                        }
                    });
                    popupmenu.add(trayswitch);
                }
                {
                    MenuItem exit = new MenuItem(hqlBuilder.exitAction.getName());
                    exit.addActionListener(hqlBuilder.exitAction);
                    popupmenu.add(exit);
                }
                hqlBuilder.trayIcon.setPopupMenu(popupmenu);
                hqlBuilder.trayIcon.addActionListener(listener);
                hqlBuilder.frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowIconified(WindowEvent e) {
                        hqlBuilder.switchVisibility();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    private static void loadModelAtRuntime() {
        JFrame dummy = new JFrame();
        dummy.setIconImage(new ImageIcon(HqlBuilderFrame.class.getClassLoader().getResource("bricks-icon.png")).getImage());
        JFileChooser fc = new JFileChooser(new File("."));
        fc.setDialogTitle(HqlResourceBundle.getMessage("open model & hibernate configuration"));
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".zip") || f.getName().endsWith(".jar");
            }

            @Override
            public String getDescription() {
                return HqlResourceBundle.getMessage("single model & hibernate config jar / maven assembly zip");
            }
        });
        int returnVal = fc.showOpenDialog(dummy);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fc.getSelectedFile();

                URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                Class<?> sysclass = URLClassLoader.class;
                Method method = sysclass.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);

                if (file.getName().endsWith("zip")) {
                    File target = new File(getTmpdir(), file.getName());
                    unzip(target, file);
                    String[] list = target.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith("jar");
                        }
                    });
                    if (list != null) {
                        for (String element : list) {
                            method.invoke(sysloader, new Object[] { new File(target, element).toURI().toURL() });
                        }
                    }
                } else {
                    method.invoke(sysloader, new Object[] { file.toURI().toURL() });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
                return;
            }
        } else {
            System.exit(-1);
            return;
        }
    }

    private static void unzip(File dir, File zipFile) {
        FileInputStream fis;
        ZipInputStream zis = null;
        try {
            fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            BufferedOutputStream dest = null;

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte[] data = new byte[1024 * 8];
                String extractFileLocation = dir + "/" + entry.getName();

                // maak alle tussenliggende directories aan
                if (extractFileLocation.lastIndexOf('/') != -1) {
                    File file = new File(extractFileLocation.substring(0, extractFileLocation.lastIndexOf('/')));

                    if (!file.exists()) {
                        file.mkdirs();
                    }
                }

                // write the files to the disk
                FileOutputStream fos = new FileOutputStream(dir + "/" + entry.getName());
                dest = new BufferedOutputStream(fos, 1024 * 8);

                try {
                    while ((count = zis.read(data, 0, 1024 * 8)) != -1) {
                        dest.write(data, 0, count);
                    }
                } finally {
                    dest.close();
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void switchVisibility() {
        if (!SystemTray.isSupported()) {
            return;
        }
        if (!Boolean.TRUE.equals(systrayAction.getValue())) {
            return;
        }
        boolean v = !frame.isVisible();
        frame.setVisible(v);
        if (v) {
            frame.toFront();
            frame.setState(Frame.NORMAL);
        }
        if (!v) {
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        } else {
            SystemTray.getSystemTray().remove(trayIcon);
        }
    }

    public static interface RowProcessor {
        void process(List<Object> lijn);
    }

    private synchronized void query(boolean equalsCheck) {
        if (pauseQueryAction.isSelected()) {
            return;
        }

        if (equalsCheck && !checkHqlChanged()) {
            log("query did not change");
            return;
        }

        progressbarStart("quering");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sql.setText("");
                resultsInfo.setText("");
                clearResults();
            }
        });

        _query(null);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    resultsUnsafe.setAutoResizeMode(ETable.AUTO_RESIZE_OFF);
                    for (int i = 0; i < resultsEDT.getColumnCount(); i++) {
                        resultsUnsafe.packColumn(i, 2);
                    }
                    if (resizeColumnsAction.isSelected()) {
                        resultsUnsafe.setAutoResizeMode(ETable.AUTO_RESIZE_ALL_COLUMNS);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        progressbarStop();
    }

    private void progressbarStop() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressbar.setIndeterminate(false);
                progressbar.setString("");
            }
        });
    }

    private void progressbarStart(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressbar.setIndeterminate(true);
                progressbar.setString(HqlResourceBundle.getMessage(text));
                log("progress: " + text);
            }
        });
    }

    private void query(RowProcessor rowProcessor) {
        progressbarStart("quering");
        _query(rowProcessor);
        progressbarStop();
        JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("done"));
    }

    @SuppressWarnings("unchecked")
    private synchronized void _query(RowProcessor rowProcessor) {
        Session session = sessionFactory.openSession();
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    hql_sql_tabs.setForegroundAt(1, Color.gray);
                }
            });

            addLast(LAST);

            syntaxHiRemove();

            // resultsEDT.clear();

            String hqlGetText = getHqlText();
            QueryTranslatorImpl createQueryTranslator = new QueryTranslatorImpl("queryIdentifier", hqlGetText, new HashMap<Object, Object>(),
                    (SessionFactoryImplementor) sessionFactory);
            createQueryTranslator.compile(new HashMap<Object, Object>(), false);
            log(createQueryTranslator.getSQLString());
            if (formatSqlAction.isSelected()) {
                sql.setText(cleanupSql(createQueryTranslator.getSQLString(), null, null));
            } else {
                sql.setText(createQueryTranslator.getSQLString());
            }
            log(sql.getText());

            // int parameterCount =
            // createQueryTranslator.getParameterTranslations().getOrdinalParameterCount();

            QueryImpl createQuery = (QueryImpl) session.createQuery(hqlGetText);
            int index = 0;

            for (QueryParameter value : EList.convertRecords(parametersEDT.getRecords())) {
                try {
                    if (value.getName() != null) {
                        if (value.getValue() instanceof Collection) {
                            @SuppressWarnings("rawtypes")
                            Object[] l = new ArrayList((Collection) value.getValue()).toArray();
                            createQuery.setParameterList(value.getName(), l);
                        } else {
                            createQuery.setParameter(value.getName(), value.getValue());
                        }
                    } else {
                        createQuery.setParameter(index++, value.getValue());
                    }
                } catch (org.hibernate.QueryParameterException ex) {
                    // org.hibernate.QueryParameterException: could not locate named parameter [nummer]
                    System.out.println(ex); // => whatever
                } catch (java.lang.IndexOutOfBoundsException ex) {
                    // java.lang.IndexOutOfBoundsException: Remember that ordinal parameters are 1-based!
                    System.out.println(ex); // => whatever
                }
            }

            Type[] queryReturnTypes = get(createQueryTranslator, "queryLoader.queryReturnTypes", Type[].class);
            String[] queryReturnAliases = get(createQueryTranslator, "queryLoader.queryReturnAliases", String[].class);
            String[][] scalarColumnNames = get(createQueryTranslator, "queryLoader.scalarColumnNames", String[][].class);
            String[] entityAliases = get(createQueryTranslator, "queryLoader.entityAliases", String[].class);
            Queryable[] entityPersisters = get(createQueryTranslator, "queryLoader.entityPersisters", Queryable[].class);
            String[] sqlAliases = get(createQueryTranslator, "queryLoader.sqlAliases", String[].class);
            @SuppressWarnings("unused")
            Map<String, String> sqlAliasByEntityAlias = get(createQueryTranslator, "queryLoader.sqlAliasByEntityAlias", Map.class);

            if (formatSqlAction.isSelected()) {
                try {
                    sql.setText(removeBlanks(makeMultiline(cleanupSql(createQueryTranslator.getSQLString(), queryReturnAliases, scalarColumnNames))));
                    log(sql.getText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log(createQueryTranslator.getSQLString());
                }
            }

            HashMap<String, String> from_aliases = new HashMap<String, String>();

            for (int i = 0; i < entityAliases.length; i++) {
                String alias = entityAliases[i];

                if (alias != null) {
                    from_aliases.put(alias, entityPersisters[i].getClassMetadata().getEntityName());
                }
            }

            valueHolders.put(FROM_ALIASES, from_aliases);

            try {
                if (rowProcessor == null) {
                    createQuery.setMaxResults((Integer) maximumNumberOfResultsAction.getValue());
                }

                log(createQuery);

                long start = System.currentTimeMillis();

                List<?> list = createQuery.list();

                long duration = (System.currentTimeMillis() - start);

                ETableHeaders<List<Object>> headers = null;

                if (rowProcessor == null) {
                    if (list.size() == 0) {
                        resultsInfo.setText(HqlResourceBundle.getMessage("No results"));
                    } else {
                        resultsInfo.setText(HqlResourceBundle.getMessage("results in milliseconds", String.valueOf(list.size()),
                                String.valueOf(duration)));
                    }
                }

                List<ETableRecord<List<Object>>> records = new ArrayList<ETableRecord<List<Object>>>();
                for (Object o : list) {
                    ETableRecordCollection<Object> record = null;
                    List<Object> recordItems;

                    if (o instanceof Object[]) {
                        recordItems = new ArrayList<Object>(Arrays.asList((Object[]) o));
                    } else if (o instanceof Collection<?>) {
                        recordItems = new ArrayList<Object>((Collection<Object>) o);
                    } else {
                        recordItems = new ArrayList<Object>(Collections.singleton(o));
                    }

                    record = new ETableRecordCollection<Object>(recordItems);

                    if (rowProcessor != null) {
                        rowProcessor.process(record.getBean());
                        continue;
                    }

                    if (headers == null) {
                        headers = new ETableHeaders<List<Object>>();

                        for (int i = 0; i < record.size(); i++) {
                            boolean script = getScript(i) != null;
                            if ((queryReturnAliases == null) || String.valueOf(i).equals(queryReturnAliases[i])) {
                                try {
                                    headers.add("<html>" + (script ? "*" : "") + queryReturnTypes[i].getReturnedClass().getSimpleName() + "<br>"
                                            + scalarColumnNames[i][0] + (script ? "*" : "") + "<html>");
                                } catch (Exception ex) {
                                    log(ex);

                                    try {
                                        headers.add("<html>" + (script ? "*" : "") + queryReturnTypes[i].getReturnedClass().getSimpleName() + "<br>"
                                                + sqlAliases[i] + (script ? "*" : "") + "<html>");
                                    } catch (Exception ex2) {
                                        log(ex2);
                                        headers.add("<html>" + (script ? "*" : "") + queryReturnTypes[i].getReturnedClass().getSimpleName() + "<br>"
                                                + i + (script ? "*" : "") + "<html>");
                                    }
                                }
                            } else {
                                headers.add("<html>" + queryReturnTypes[i].getReturnedClass().getSimpleName() + "<br>" + queryReturnAliases[i]
                                        + "<html>");
                            }
                        }

                        resultsEDT.setHeaders(headers);
                    }

                    records.add(record);
                }
                resultsEDT.addRecords(records);

                syntaxHi();
            } catch (Exception ex) {
                throw ex;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    hql_sql_tabs.setForegroundAt(1, Color.GREEN);
                }
            });
        } catch (Exception ex) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    hql_sql_tabs.setForegroundAt(1, Color.RED);
                }
            });
            ex.printStackTrace();
            // uiExceptionHandler.show(null, ex, HqlResourceBundle.getMessage("hql error"));
            sql.setText(ex.toString() + getNewline() + getNewline() + "-----------------------------" + getNewline() + getNewline() + sql.getText());
            valueHolders.put(VALUE, ex);
            clearResults();
        } finally {
            session.close();
        }
    }

    private String getScript(int i) {
        @SuppressWarnings("unchecked")
        Map<Integer, String> scripts = (Map<Integer, String>) valueHolders.get(SCRIPT);
        if (scripts == null) {
            return null;
        }
        return scripts.get(i);
    }

    private String removeBlanks(String string) {
        string = string.trim();
        while (string.indexOf("  ") != -1) {
            string = string.replaceAll("  ", " ");
        }
        while (string.indexOf(getNewline() + getNewline()) != -1) {
            string = string.replaceAll("\\Q" + getNewline() + getNewline() + "\\E", getNewline());
        }
        return string.trim();
    }

    private String cleanupSql(String sqlString, String[] queryReturnAliases, String[][] scalarColumnNames) {
        // kolom alias (kan enkel maar wanneer de query al is omgezet, dus de tweede maal dat deze methode wordt opgeroepen-
        if (queryReturnAliases != null) {
            for (int i = 0; i < queryReturnAliases.length; i++) {
                String queryReturnAlias = queryReturnAliases[i];
                try {
                    String scalarColumnName = scalarColumnNames[i][0];
                    try {
                        // nummers worden vervangen door 'kolom${nummer}' want nummer alleen wordt niet aanvaard
                        Long.parseLong(queryReturnAlias);
                        String newAlias = queryReturnAlias.replace('.', ' ').replace('(', ' ').replace(')', ' ').trim().replace(' ', '_');
                        log(": " + scalarColumnName + " >> " + queryReturnAlias + " >> " + newAlias);
                        sqlString = sqlString.replace(scalarColumnName, newAlias);
                    } catch (NumberFormatException ex) {
                        log(": " + scalarColumnName + " >> " + queryReturnAlias);
                        sqlString = sqlString.replace(scalarColumnName, queryReturnAlias);
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    //
                }
            }
        }

        // maakt replacers aan
        HashMap<String, String> replacers = new HashMap<String, String>();

        // vervang tabel_?_?_ door tabelnamen in "from ..." en "... join ..."
        // vervang AcademischDomeinObject door ADOx
        {
            String prefix = "((from)|(join)|(,))";
            String joinfromgroup = "( ([a-zA-Z0-9_]+) ([a-zA-Z0-9_]+))";
            Matcher matcher = Pattern.compile(prefix + joinfromgroup, Pattern.CASE_INSENSITIVE).matcher(sqlString);
            int startpos = 0;

            while (matcher.find(startpos)) {
                String replacing = matcher.group(7);

                if ("when".equals(replacing)) {
                    startpos++;
                    continue;
                }

                String replaceBy = matcher.group(6);

                if (replaceBy.contains("AcademischDomeinObject")) {
                    log("-> " + replaceBy + " >> " + replaceBy.replace("AcademischDomeinObject", "ADOx"));
                    replaceBy = replaceBy.replace("AcademischDomeinObject", "ADOx");
                }

                int existing = CollectionUtils.cardinality(replaceBy, replacers.values());

                if (existing > 0) {
                    log("-> " + replaceBy + " >> " + replaceBy + (existing + 1));
                    replaceBy = replaceBy + (existing + 1);
                }

                log("- " + replacing + " >> " + replaceBy);
                replacers.put(replacing, replaceBy);

                startpos = matcher.end();
            }
        }

        // vervang (1) door (2) om geen dubbels te hebben
        // (1) tabel_?_?_=tabelnaamY EN tabel_?_=tabelnaamX
        // (2) tabel_?_?_=tabelnaamX_tabelnaamY EN tabel_?_=tabelnaamX
        List<String> ADOx = new ArrayList<String>();

        for (Map.Entry<String, String> replacer : replacers.entrySet()) {
            for (Map.Entry<String, String> replacerOther : replacers.entrySet()) {
                if (!replacer.getKey().equals(replacerOther.getKey()) && replacer.getKey().startsWith(replacerOther.getKey())) {
                    String newvalue = replacerOther.getValue() + "_" + replacer.getValue();

                    // oracle heeft 30 len limiet
                    if (newvalue.length() > 30) {
                        newvalue = newvalue.substring(0, 30);
                    }

                    log("* " + replacer + " EN " + replacerOther + " >> " + replacer.getValue() + "=" + newvalue);
                    replacer.setValue(newvalue);
                    ADOx.add(newvalue);
                }
            }
        }

        // sorteer replacers op langste eerst
        List<String> keys = new ArrayList<String>(replacers.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() < o2.length()) {
                    return 1;
                } else if (o1.length() > o2.length()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        // vervang nu replacers
        for (String key : keys) {
            String value = replacers.get(key);
            log("+ " + key + " > " + value);
            sqlString = sqlString.replaceAll(key, value);
        }

        // vervang kolomnamen
        if (replacePropertiesAction.isSelected()) {
            Matcher matcher = Pattern.compile("(( )([^ ]+)( as )([a-zA-Z0-9_]+))", Pattern.CASE_INSENSITIVE).matcher(sqlString);

            while (matcher.find()) {
                String newvalue = matcher.group(3).replace('.', ' ').replace('(', ' ').replace(')', ' ').trim().replace(' ', '_');

                // oracle heeft 30 len limiet
                if (newvalue.length() > 30) {
                    newvalue = newvalue.substring(0, 30);
                }

                newvalue = " " + matcher.group(3) + " as " + newvalue;
                String group = matcher.group();

                try {
                    log("/ " + group + " > " + newvalue);
                    sqlString = sqlString.replaceAll("\\Q" + group + "\\E", newvalue);
                } catch (Exception ex) {
                    ex.printStackTrace(System.out);
                }
            }
        }

        log(sqlString);

        if (formatLinesAction.isSelected()) {
            sqlString = makeMultiline(sqlString);
        }

        sqlString = removeBlanks(sqlString);

        @SuppressWarnings("unused")
        String[] sqlStringParts = sqlString.split(getNewline());

        String[] lines = sqlString.split(getNewline());

        if (removeADOJoinsAction.isSelected()) {
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];

                if (line.contains("ADOx")) {
                    boolean keep = keep(ADOx, lines, i, line);

                    // zal verwijderd worden
                    if (!keep) {
                        lines[i] = null;
                    }
                }
            }
        }

        StringBuilder anew = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null) {
                anew.append(lines[i].replaceAll("ADOx", "ADO")).append(getNewline());
            }
        }

        sqlString = anew.toString();

        log(sqlString);

        return sqlString;
    }

    private boolean keep(List<String> ADOx, String[] lines, int i, String line) {
        if (!line.contains("ADOx")) {
            return true;
        }
        for (String ADOxEl : ADOx) {
            if (line.contains(ADOxEl)) {
                for (int j = 0; j < lines.length; j++) {
                    if (i != j) {
                        if (lines[j] != null && lines[j].contains(ADOxEl)) {
                            // deze mag niet vervangen worden
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String getHqlTextEq() {
        String text = getHqlText();
        text = text.replaceAll(",", " ");

        while (text.indexOf("  ") != -1) {
            text = text.replaceAll("  ", " ");
        }

        text = text.trim();

        return text;
    }

    private String getHqlText() {
        String text = hql.getText();
        text = text.replaceAll("\r", getNewline());
        text = text.replaceAll("\t", " ");

        StringBuilder sb = new StringBuilder();

        for (String lijn : StringUtils.split(text, getNewline())) {
            String trim = lijn.trim();

            if (!trim.startsWith("--") && !trim.startsWith("//")) {
                sb.append(trim).append(" ");
            }
        }

        text = sb.toString();

        while (text.indexOf("  ") != -1) {
            text = text.replaceAll("  ", " ");
        }

        text = text.trim();

        return text;
    }

    /**
     * start
     * 
     * @param properties
     * 
     * @throws IOException
     */
    public void start(Properties properties) throws IOException {
        reloadColor();

        hili.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0F, new float[] { 1.0F }, 0.F));

        hql.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                hilightBraces(hql.getText());
            }
        });

        sql.setEditable(false);

        resultsUnsafe.setAutoscrolls(true);
        resultsUnsafe.setFillsViewportHeight(true);
        resultsUnsafe.setColumnSelectionAllowed(true);

        propertypanel.add(values, BorderLayout.CENTER);

        Container framepanel = frame.getContentPane();

        JScrollPane jspResults = new JScrollPane(resultsUnsafe, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        resultPanel.add(jspResults, BorderLayout.CENTER);
        resultsUnsafe.addRowHeader(jspResults, 3);

        JPanel resultBottomPanel = new JPanel(new BorderLayout());
        resultPanel.add(resultBottomPanel, BorderLayout.SOUTH);
        resultPanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("results")));

        parameterspanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("parameters")));

        propertypanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("properties")));

        progressbar.setMinimumSize(new Dimension(400, 20));
        progressbar.setPreferredSize(new Dimension(400, 20));
        progressbar.setStringPainted(true);
        progressbar.setString("");

        hql_sql_tabs.addTab("HQL", new JScrollPane(hql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        hql_sql_tabs.addTab("SQL", new JScrollPane(sql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));

        {
            EList<String> searchresults = new EList<String>(new EListConfig().setSortable(false));
            final EList<String> searchresultsEDTSafe = searchresults.stsi();
            final ECheckBox searchClass = new ECheckBox("class", true);
            final ECheckBox searchField = new ECheckBox("field", true);
            ELabeledTextFieldButtonComponent searchinput = new ELabeledTextFieldButtonComponent() {
                private static final long serialVersionUID = 6519657911421417572L;

                @Override
                public JComponent getParentComponent() {
                    return null;
                }

                @Override
                public void copy(ActionEvent e) {
                    //
                }

                @Override
                protected Icon getIcon() {
                    return new ImageIcon(HqlBuilderFrame.class.getClassLoader().getResource("search.png"));
                }

                @Override
                protected String getAction() {
                    return "search-index";
                }

                @Override
                protected void doAction() {
                    if (information != null && information.isReady()) {
                        searchresultsEDTSafe.removeAllRecords();
                        try {
                            List<String> results;
                            if (searchClass.isSelected() != searchField.isSelected()) {
                                if (searchClass.isSelected()) {
                                    results = information.search(getInput().getText(), Information.CLASS);
                                } else {
                                    results = information.search(getInput().getText(), Information.FIELD);
                                }
                            } else {
                                results = information.search(getInput().getText(), null);
                            }
                            searchresultsEDTSafe.addRecords(EList.convert(results));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };
            JPanel searchActions = new JPanel(new BorderLayout());
            searchActions.add(searchinput, BorderLayout.CENTER);

            JPanel searchTypes = new JPanel(new GridLayout(1, 2));
            searchTypes.add(searchClass);
            searchTypes.add(searchField);
            searchActions.add(searchTypes, BorderLayout.EAST);

            JPanel infopanel = new JPanel(new BorderLayout());
            infopanel.add(searchActions, BorderLayout.NORTH);
            infopanel.add(new JScrollPane(searchresults, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),
                    BorderLayout.CENTER);
            hql_sql_tabs.addTab(HqlResourceBundle.getMessage("Lucene search"), infopanel);
        }

        hql_sql_tabs.setForegroundAt(1, Color.gray);

        Dimension bd = new Dimension(24, 24);
        EButton saveButton = new EButton(new EToolBarButtonCustomizer(bd), saveAction);
        saveButton.setText("");
        EButton removeButton = new EButton(new EToolBarButtonCustomizer(bd), removeAction);
        removeButton.setText("");
        EButton upButton = new EButton(new EToolBarButtonCustomizer(bd), upAction);
        upButton.setText("");
        EButton downButton = new EButton(new EToolBarButtonCustomizer(bd), downAction);
        downButton.setText("");

        parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("name") + ": "));
        parameterspanel.add(parameterName, "grow");
        parameterspanel.add(saveButton, "spany 2");

        parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("value") + ": "));
        parameterspanel.add(parameterBuilder, "grow");

        parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("compiled") + ": "));
        parameterspanel.add(parameterValue, "grow");
        parameterspanel.add(new ELabel());

        parameterspanel.add(new JScrollPane(parametersUnsafe), "spanx 2, spany 4, growx, growy");
        parameterspanel.add(upButton, "bottom, shrinky");
        parameterspanel.add(removeButton, "shrinky");
        parameterspanel.add(downButton, "top, shrinky");
        parameterspanel.add(new ELabel(), "growy");

        maxResults.setMinimumSize(new Dimension(80, 22));
        maxResults.setPreferredSize(new Dimension(80, 22));
        resultsInfo.setMinimumSize(new Dimension(300, 22));
        resultsInfo.setPreferredSize(new Dimension(300, 22));

        JPanel ab2 = new JPanel(new BorderLayout());
        ab2.add(resultsInfo, BorderLayout.CENTER);
        ab2.add(maxResults, BorderLayout.EAST);
        resultBottomPanel.add(ab2, BorderLayout.WEST);
        resultBottomPanel.add(progressbar, BorderLayout.CENTER);

        if (test) {
            // TO DO
        } else {
            switch_layout();
            framepanel.add(normalContentPane, BorderLayout.CENTER);
        }

        parameterBuilder.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // extra help enkel in geval van groovy
                String text = parameterBuilder.getText();

                if (groovyAction.isSelected()) {
                    try {
                        // vervangt datums als string automatisch door localdate (parameters zijn dikwijls localdate)
                        LocalDate localDate = ISODateTimeFormat.date().parseDateTime(text).toLocalDate();
                        text = "new LocalDate(" + localDate.getYear() + "," + localDate.getMonthOfYear() + "," + localDate.getDayOfMonth() + ")";
                    } catch (Exception ex2) {
                        //
                    }

                    try {
                        // zet automatisch l achter nummers (parameters zijn dikwijls id's en die zijn van het type long)
                        Long.parseLong(text);
                        text = text + "l";
                    } catch (Exception ex2) {
                        //
                    }
                }

                compile(text);
            }
        });
        hql.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                try {
                    scheduleQuery(null, true);
                } catch (Exception ex) {
                    log(ex);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (EventHelper.keyEvent(e, EventModifier.CTR_SHIFT_DOWN, ' ')) {
                    help_insert();
                    return;
                }

                if (EventHelper.keyEvent(e, EventModifier.CTR_SHIFT_DOWN, '/')) {
                    remark_toggle();
                    return;
                }
            }
        });

        createHqlPopupMenu();

        createSqlPopupMenu();

        createResultsPopupMenu();

        addTableSelectionListener(resultsUnsafe, new TableSelectionListener() {
            @Override
            public void rowChanged(int row) {
                //
            }

            @Override
            public void columnChanged(int column) {
                //
            }

            @Override
            public void cellChanged(int row, int column) {
                propertypanel.removeAll();
                Object data = ((row == -1) || (column == -1)) ? null : resultsEDT.getValueAt(row, column);
                if (data == null) {
                    propertypanel.add(hqlBuilderHelper.getPropertyFrame(null, new Object(), editableResultsAction.isSelected()), BorderLayout.CENTER);
                } else {
                    propertypanel.add(font(hqlBuilderHelper.getPropertyFrame(sessionFactory, data, editableResultsAction.isSelected()), null),
                            BorderLayout.CENTER);
                }
                propertypanel.revalidate();
            }
        });
        parametersEDT.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                parameterSelected();
            }
        });
        // FIXME
        // DefaultListModel.class.cast(parameters.getModel()).addListDataListener(new ListDataListener() {
        // @Override
        // public void intervalRemoved(ListDataEvent e) {
        // try {
        // query();
        // } catch (Exception ex) {
        // ex.printStackTrace(System.out);
        // }
        // }
        //
        // @Override
        // public void intervalAdded(ListDataEvent e) {
        // try {
        // query();
        // } catch (Exception ex) {
        // ex.printStackTrace(System.out);
        // }
        // }
        //
        // @Override
        // public void contentsChanged(ListDataEvent e) {
        // try {
        // query();
        // } catch (Exception ex) {
        // ex.printStackTrace(System.out);
        // }
        // }
        // });

        // KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(keyboardKeyEventProcessor);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        JPanel toolbars = new JPanel(new FlowLayout(FlowLayout.LEFT));
        framepanel.add(toolbars, BorderLayout.NORTH);

        EToolBarButtonCustomizer etbc = new EToolBarButtonCustomizer();
        {
            JToolBar hqltools = new JToolBar();
            hqltools.add(new EButton(etbc, wizardAction));
            hqltools.add(new EButton(etbc, clearAction));
            hqltools.add(new EButton(etbc, findParametersAction));
            hqltools.add(new EButton(etbc, favoritesAction));
            hqltools.add(new EButton(etbc, addToFavoritesAction));
            hqltools.add(new EButton(etbc, startQueryAction));
            hqltools.add(new EToggleToolBarButton(etbc, pauseQueryAction));
            hqltools.add(new EButton(etbc, stopQueryAction));
            hqltools.add(new EButton(etbc, formatAction));
            hqltools.add(new EButton(etbc, namedQueryAction));
            hqltools.add(new EButton(etbc, clipboardExportAction));
            hqltools.add(new EButton(etbc, clipboardImportAction));
            toolbars.add(hqltools);
        }
        {
            JToolBar resultstools = new JToolBar();
            resultstools.add(new EButton(etbc, hibernatePropertiesAction));
            resultstools.add(new EButton(etbc, objectTreeAction));
            resultstools.add(new EButton(etbc, deleteObjectAction));
            resultstools.add(new EButton(etbc, copyCellAction));
            resultstools.add(new EButton(etbc, executeScriptOnColumnAction));
            toolbars.add(resultstools);
        }

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        {
            JMenu actionsMenu = new JMenu(HqlResourceBundle.getMessage("actions"));
            actionsMenu.add(new JCheckBoxMenuItem(switchLayoutAction));
            actionsMenu.add(new JMenuItem(exitAction));
            actionsMenu.add(new JMenuItem(forceExitAction));
            menuBar.add(actionsMenu);
        }
        {
            JMenu settingsMenu = new JMenu(HqlResourceBundle.getMessage("settings"));
            {
                settingsMenu.add(new JCheckBoxMenuItem(formatSqlAction));
                settingsMenu.add(formatSqlOptionsMenu);
                formatSqlOptionsMenu.add(new JCheckBoxMenuItem(removeADOJoinsAction));
                formatSqlOptionsMenu.add(new JCheckBoxMenuItem(formatLinesAction));
                formatSqlOptionsMenu.add(new JCheckBoxMenuItem(replacePropertiesAction));
            }
            {
                JMenu compmi = new JMenu(HqlResourceBundle.getMessage("compiler"));
                ButtonGroup compbg = new ButtonGroup();
                compmi.add(addBG(compbg, new JRadioButtonMenuItem(groovyAction)));
                compmi.add(addBG(compbg, new JRadioButtonMenuItem(javaAction)));
                compmi.add(addBG(compbg, new JRadioButtonMenuItem(javascriptAction)));
                settingsMenu.add(compmi);
            }
            {
                JMenu addmi = new JMenu(HqlResourceBundle.getMessage("additional settings"));
                addmi.add(new JCheckBoxMenuItem(hiliAction));
                addmi.add(new JMenuItem(hiliColorAction));
                addmi.add(new JCheckBoxMenuItem(resizeColumnsAction));
                addmi.add(maximumNumberOfResultsAction);
                addmi.add(new JCheckBoxMenuItem(alwaysOnTopAction));
                addmi.add(new JCheckBoxMenuItem(newProgressAction));
                addmi.add(new JCheckBoxMenuItem(editableResultsAction));
                addmi.add(new JMenuItem(searchColorAction));
                if (SystemTray.isSupported()) {
                    addmi.add(new JCheckBoxMenuItem(systrayAction));
                }
                settingsMenu.add(addmi);
            }
            menuBar.add(settingsMenu);
        }
        {
            JMenu hqlmenu = new JMenu("HQL");
            hqlmenu.add(new JMenuItem(wizardAction));
            hqlmenu.add(new JMenuItem(clearAction));
            hqlmenu.add(new JMenuItem(findParametersAction));
            hqlmenu.add(new JMenuItem(favoritesAction));
            hqlmenu.add(new JMenuItem(addToFavoritesAction));
            hqlmenu.add(new JMenuItem(startQueryAction));
            hqlmenu.add(new JCheckBoxMenuItem(pauseQueryAction));
            hqlmenu.add(new JMenuItem(stopQueryAction));
            hqlmenu.add(new JMenuItem(formatAction));
            hqlmenu.add(new JMenuItem(namedQueryAction));
            hqlmenu.add(new JMenuItem(clipboardExportAction));
            hqlmenu.add(new JMenuItem(clipboardImportAction));
            menuBar.add(hqlmenu);
        }
        {
            JMenu resultsmenu = new JMenu(HqlResourceBundle.getMessage("results"));
            resultsmenu.add(new JMenuItem(hibernatePropertiesAction));
            resultsmenu.add(new JMenuItem(objectTreeAction));
            resultsmenu.add(new JMenuItem(deleteObjectAction));
            resultsmenu.add(new JMenuItem(copyCellAction));
            resultsmenu.add(new JMenuItem(executeScriptOnColumnAction));
            menuBar.add(resultsmenu);
        }
        {
            JMenu helpmenu = new JMenu(HqlResourceBundle.getMessage("help"));
            helpmenu.add(new JMenuItem(helpHibernateAction));
            helpmenu.add(new JMenuItem(helpHqlAction));
            helpmenu.add(new JMenuItem(helpOfflineAction));
            helpmenu.add(new JMenuItem(luceneQuerySyntaxAction));
            helpmenu.add(new JMenuItem(helpAction));
            menuBar.add(helpmenu);
        }

        String version = HqlResourceBundle.getMessage("latest");
        try {
            String url = HqlBuilderFrame.class.getClassLoader().getResource("be/ugent/oasis/model/Parameter.class").toString();
            version = url.substring(url.indexOf("model-") + "model-".length(), url.indexOf(".jar"));
        } catch (Exception ex) {
            //
        }

        frame.setTitle(NAME + " - " + properties.getProperty("hibernate.connection.username") + "@"
                + properties.getProperty("hibernate.connection.url").replaceAll("jdbc:oracle:thin:@", "") + "@" + version);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(new Dimension(1024, 768));
        frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        frame.setIconImage(new ImageIcon(HqlBuilderFrame.class.getClassLoader().getResource("bricks-icon.png")).getImage());
        frame.setGlassPane(getGlass(frame));
        frame.setAlwaysOnTop(Boolean.TRUE.equals(alwaysOnTopAction.getValue()));
    }

    protected void startPre(Properties properties) {
        scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                Thread thread = Executors.defaultThreadFactory().newThread(r);
                thread.setDaemon(true);

                return thread;
            }
        });

        valueHolders.addPropertyChangeListener(FROM_ALIASES, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                log(">>> " + evt.getNewValue());
            }
        });

        AnnotationConfiguration configuration = new AnnotationConfiguration();

        for (Class<?> persistentClass : hqlBuilderHelper.getEntityDiscovery().getEntityClasses(hqlBuilderHelper.getHibernateConfiguration())) {
            log(persistentClass.getName());
            configuration.addAnnotatedClass(persistentClass);
        }

        try {
            InputStream in = HqlBuilderFrame.class.getClassLoader().getResourceAsStream(hqlBuilderHelper.getPersistenceContext());
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            String xmlstring = new String(buffer);
            int fromIndex = xmlstring.indexOf("standardSQLFunctions");
            int toIndex = xmlstring.indexOf("</map>", fromIndex);
            xmlstring = xmlstring.substring(fromIndex, toIndex);
            Matcher matcher = Pattern.compile("((key=\")([^\"]+)(\"[ ]+value=\")([^\"]+)(\"))").matcher(xmlstring);
            while (matcher.find()) {
                String name = matcher.group(3);
                String type = matcher.group(5);
                Type basic = DefaultTypeFactory.basic(type);
                log("function " + basic.getReturnedClass().getName() + "(" + type + ") " + name);
                configuration.addSqlFunction(name.toLowerCase(), new StandardSQLFunction(name, basic));
            }
        } catch (Exception ex) {
            log("standardSQLFunctions: " + ex);
        }

        configuration.setProperties(properties);

        configuration.setProperty("hibernate.format_sql", "true");
        configuration.setProperty("hibernate.use_sql_comments", "true");
        configuration.setProperty("hibernate.query.substitutions", "true 1, false 0");
        configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        configuration.setProperty("hibernate.cache.use_query_cache", "true");
        configuration.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");

        sessionFactory = configuration.buildSessionFactory();
        hqlBuilderHelper.onFirstSessionReady(sessionFactory);
        meta = new HibernateWebResolver(hqlBuilderHelper, sessionFactory);

        @SuppressWarnings({ "hiding", "unchecked" })
        Map<String, NamedQueryDefinition> namedQueries = get(sessionFactory, "namedQueries", Map.class);
        for (Map.Entry<String, NamedQueryDefinition> entry : namedQueries.entrySet()) {
            this.namedQueries.put(entry.getKey(), entry.getValue().getQueryString());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    information = new Information(sessionFactory);
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        loadFavorites();
    }

    private JRadioButtonMenuItem addBG(ButtonGroup bg, JRadioButtonMenuItem mi) {
        bg.add(mi);
        return mi;
    }

    @SuppressWarnings("cast")
    private void layout(Dimension size) {
        if (test) {
            return;
        }

        if (frame.getSize().height == 0) {
            size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            Insets insets = java.awt.Toolkit.getDefaultToolkit().getScreenInsets(
                    java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
            size = new Dimension((int) (size.getWidth() - insets.left - insets.right), (int) (size.getHeight() - insets.top - insets.bottom));
        } else {
            size = normalContentPane.getSize();
        }

        int w = (int) size.getWidth();
        int h = (int) size.getHeight();
        // if (menu_largeLayout.getState()) {
        if (switchLayoutAction.isSelected()) {
            // hql_sql_tabs .... parameterspanel .... propertypanel
            // resultPanel
            split0.setDividerLocation((int) ((double) h * 1.0 / 3.0));
            split1.setDividerLocation((int) ((double) w * 1.0 / 3.0));
            split2.setDividerLocation((int) ((double) w * 1.0 / 3.0));
        } else {
            // hql_sql_tabs .... parameterspanel
            // resultPanel ..... propertypanel
            split0.setDividerLocation((int) ((double) h * 1.0 / 3.0));
            split1.setDividerLocation((int) ((double) w * 2.0 / 3.0));
            split2.setDividerLocation((int) ((double) w * 2.0 / 3.0));
        }
    }

    protected void switch_layout() {
        if (test) {
            // TO DO
            return;
        }

        log("change layout");

        normalContentPane.setVisible(false);
        normalContentPane.removeAll();

        // if (menu_largeLayout.getState()) {
        if (switchLayoutAction.isSelected()) {
            // hql_sql_tabs .... parameterspanel .... propertypanel
            // resultPanel
            split0 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

            EComponentPopupMenu.removeAllRegisteredKeystroke(split0);
            EComponentPopupMenu.removeAllRegisteredKeystroke(split1);
            EComponentPopupMenu.removeAllRegisteredKeystroke(split2);

            split0.setLeftComponent(split1);
            split1.setRightComponent(split2);

            split1.setLeftComponent(hql_sql_tabs);
            split2.setLeftComponent(parameterspanel);
            split2.setRightComponent(propertypanel);
            split0.setRightComponent(resultPanel);

            normalContentPane.add(split0, BorderLayout.CENTER);
        } else {
            // hql_sql_tabs .... parameterspanel
            // resultPanel ..... propertypanel
            split0 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

            EComponentPopupMenu.removeAllRegisteredKeystroke(split0);
            EComponentPopupMenu.removeAllRegisteredKeystroke(split1);
            EComponentPopupMenu.removeAllRegisteredKeystroke(split2);

            split0.setLeftComponent(split1);
            split0.setRightComponent(split2);

            split1.setLeftComponent(hql_sql_tabs);
            split1.setRightComponent(parameterspanel);
            split2.setLeftComponent(resultPanel);
            split2.setRightComponent(propertypanel);

            normalContentPane.add(split0, BorderLayout.CENTER);
        }

        layout(null);

        normalContentPane.setVisible(true);
    }

    protected void immediateQuery() {
        scheduleQuery(null, false);
    }

    private void createSqlPopupMenu() {
        // EComponentPopupMenu.installPopupMenu((ReadableComponent) new TextComponentWritableComponent(sql));
    }

    private void createHqlPopupMenu() {
        JPopupMenu hqlpopupmenu = hql.getComponentPopupMenu();
        hqlpopupmenu.addSeparator();
        hqlpopupmenu.add(new JMenuItem(wizardAction));
        hqlpopupmenu.add(new JMenuItem(clearAction));
        hqlpopupmenu.add(new JMenuItem(findParametersAction));
        hqlpopupmenu.add(new JMenuItem(favoritesAction));
        hqlpopupmenu.add(new JMenuItem(addToFavoritesAction));
        hqlpopupmenu.add(new JMenuItem(startQueryAction));
        hqlpopupmenu.add(new JCheckBoxMenuItem(pauseQueryAction));
        hqlpopupmenu.add(new JMenuItem(stopQueryAction));
        hqlpopupmenu.add(new JMenuItem(formatAction));
        hqlpopupmenu.add(new JMenuItem(namedQueryAction));
        hqlpopupmenu.add(new JMenuItem(clipboardExportAction));
        hqlpopupmenu.add(new JMenuItem(clipboardImportAction));
        hqlpopupmenu.addSeparator();
        hqlpopupmenu.add(new JMenuItem(helpInsertAction));
        hqlpopupmenu.add(new JMenuItem(remarkToggleAction));
    }

    private void createResultsPopupMenu() {
        JPopupMenu resultspopupmenu = resultsUnsafe.getComponentPopupMenu();
        resultspopupmenu.addSeparator();
        resultspopupmenu.add(new JMenuItem(hibernatePropertiesAction));
        resultspopupmenu.add(new JMenuItem(objectTreeAction));
        resultspopupmenu.add(new JMenuItem(deleteObjectAction));
        resultspopupmenu.add(new JMenuItem(copyCellAction));
        resultspopupmenu.add(new JMenuItem(executeScriptOnColumnAction));
    }

    protected void exit() {
        log("exit(0)");

        // KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(keyboardKeyEventProcessor);

        for (Runnable runnable : scheduler.shutdownNow()) {
            log(runnable);
        }

        log("scheduler.shutdown=" + scheduler.isShutdown());
        log("scheduler.terminated=" + scheduler.isTerminated());

        System.exit(0);
    }

    protected void force_exit() {
        log("exit(-1)");
        System.exit(-1);
    }

    @SuppressWarnings("null")
    protected void export_dataset_to_csv_file() {
        OutputStream fout = null;
        try {
            final JFileChooser fc = new JFileChooser();
            if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(frame)) {
                File dir_shortname = fc.getSelectedFile();
                String absolutePath = dir_shortname.getAbsolutePath();
                if (!absolutePath.endsWith(CSV)) {
                    absolutePath += CSV;
                }
                fout = new FileOutputStream(new File(absolutePath));
                final BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fout));
                valueHolders.put("#", 0);
                query(new RowProcessor() {
                    @Override
                    public void process(List<Object> lijn) {
                        try {
                            for (Object object : lijn) {
                                if (object instanceof Object[]) {
                                    Object[] array = (Object[]) object;
                                    for (int i = 0; i < array.length; i++) {
                                        br.write("\"");
                                        br.write(array[i].toString().replaceAll(",", ";").replaceAll("\"", "'"));
                                        br.write("\"");

                                        if (i < array.length - 1) {
                                            br.write(",");
                                        }
                                    }
                                } else {
                                    br.write("\"");
                                    br.write(object.toString().replaceAll(",", ";").replaceAll("\"", "'"));
                                    br.write("\"");
                                }
                            }
                            br.write(getNewline());
                            Integer c = (Integer) valueHolders.get("#");
                            c++;
                            if (c != 0 && c % 100 == 0) {
                                log(c + " records");
                            }
                            valueHolders.put("#", c);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                log(valueHolders.get("#") + " records");
                br.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fout.close();
            } catch (Exception ex2) {
                //
            }
        }
    }

    protected void export_table_data_to_clipboard() {
        try {
            StringBuilder sb = new StringBuilder();

            for (String name : resultsEDT.getHeadernames()) {
                sb.append(name).append("\t");
            }

            sb.append(getNewline());

            for (ETableRecord<List<Object>> record : resultsEDT.getRecords()) {
                for (int i = 0; i < record.size(); i++) {
                    sb.append(record.get(i)).append("\t");
                }

                sb.append(getNewline());
            }

            clipboard.setContents(new StringSelection(sb.toString()), getClipboardOwner());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    // private boolean keyboardKeyEvent(KeyEvent e) throws Exception {
    // if ((insertHelperList == e.getSource()) && (e.getKeyCode() == KeyEvent.VK_ENTER) && (KeyEvent.KEY_RELEASED == e.getID())) {
    // String clazz = String.class.cast(insertHelperList.getSelectedValue());
    // hql.getDocument().insertString(hql.getCaretPosition(), Class.forName(clazz).getSimpleName(), null);
    // insertHelper.setVisible(false);
    //
    // return true;
    // } else if ((insertPropertyHelper == e.getSource()) && (e.getKeyCode() == KeyEvent.VK_ENTER) && (KeyEvent.KEY_RELEASED == e.getID())) {
    // String prop = String.class.cast(insertPropertyHelper.getSelectedValue());
    // hql.getDocument().insertString(hql.getCaretPosition(), prop, null);
    // insertClassHelper.setVisible(false);
    //
    // return true;
    // }
    //
    // return false;
    // }

    protected void format() {
        try {
            hql.setText(format(hql.getText()));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    protected void copy() {
        try {
            int start = hql.getCaret().getDot();
            int end = hql.getCaret().getMark();
            String string = hql.getDocument().getText(Math.min(start, end), Math.max(start, end) - Math.min(start, end));
            clipboard.setContents(new StringSelection(string), getClipboardOwner());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    protected void paste() {
        try {
            Transferable contents = clipboard.getContents(this);
            Object transferData = contents.getTransferData(DataFlavor.stringFlavor);
            String string = (String) transferData;
            int start = hql.getCaret().getDot();
            int end = hql.getCaret().getMark();
            string = hql.getDocument().getText(0, Math.min(start, end)) + string
                    + hql.getDocument().getText(Math.max(start, end), hql.getDocument().getLength() - Math.max(start, end));
            hql.setText(string);
            scheduleQuery(null, false);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    protected void import__paste_hql_as_java_from_clipboard() {
        try {
            Transferable contents = clipboard.getContents(this);
            Object transferData = contents.getTransferData(DataFlavor.stringFlavor);
            String string = (String) transferData;
            String replaceAll = string.replaceAll("//", "").replaceAll("\";", "").replaceAll("hql[ ]{0,}\\+[ ]{0,}=[ ]{0,}\"", "")
                    .replaceAll("\\Q\r\n\\E", getNewline());
            StringBuilder sb = new StringBuilder();
            for (String line : replaceAll.split(getNewline())) {
                sb.append(line.trim()).append(getNewline());
            }
            hql.setText(sb.toString());
            scheduleQuery(0l, false);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    private String format(String text) {
        return makeMultiline(removeBlanks(text.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ')));
    }

    private String makeMultiline(String string) {
        string = lineformat1replace(string, "right outer join");
        string = lineformat1replace(string, "left outer join");
        string = lineformat1replace(string, "inner join");
        string = lineformat1replace(string, "from");
        string = lineformat1replace(string, "where");
        string = lineformat1replace(string, "having");
        string = lineformat1replace(string, "and");
        string = lineformat1replace(string, "or");
        string = lineformat1replace(string, "group by");
        string = lineformat1replace(string, "order by");

        if (string.startsWith("select ")) {
            string = "select " + getNewline() + string.substring("select ".length());
        }

        String split = "from ";

        String[] parts = string.split(split);

        StringBuilder sb = new StringBuilder(parts[0].replaceAll(", ", "," + getNewline())).append(split);

        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i]);

            if (i < parts.length - 1) {
                sb.append(split);
            }
        }

        String result = sb.toString();

        if (result.trim().equals("from")) {
            return string;
        }

        return sb.toString();
    }

    private String lineformat1replace(String string, String splitter) {
        Matcher matcher = Pattern.compile(" " + splitter + " ", Pattern.CASE_INSENSITIVE).matcher(string);
        String CTE = "AAAAAAAAAAA"; // wordt zeker niet gebruikt, wordt later vervangen
        String replaceAll = matcher.replaceAll(" " + getNewline() + CTE + " ").replaceAll(CTE, splitter);

        return replaceAll;
    }

    protected void stop_query() {
        scheduleCancel();
    }

    private void parameterSelected() {
        EListRecord<QueryParameter> selected = parametersEDT.getSelectedRecord();

        if (selected == null) {
            parameterValue.setText("");
            parameterBuilder.setText("");
            parameterName.setText("");
            valueHolders.put(SELECTED, null);
            valueHolders.put(VALUE, null);

            return;
        }

        QueryParameter param = parametersEDT.getSelectedRecord().get();

        parameterValue.setText(param.toString());
        parameterName.setText((param.getName() == null) ? "" : param.getName());
        parameterBuilder.setText(param.getText());
        valueHolders.put(SELECTED, param);
        valueHolders.put(VALUE, param.getValue());
    }

    protected void remove() {
        parametersEDT.removeSelectedRecords();
        parameterValue.setText("");
        parameterBuilder.setText("");
        parameterName.setText("");
        valueHolders.put(SELECTED, null);
        valueHolders.put(VALUE, null);
        valueHolders.put(SCRIPT, null);
    }

    protected void save() {
        String text = parameterBuilder.getText();
        String name = (parameterName.getText().length() > 0) ? parameterName.getText() : null;
        Object value = valueHolders.get(VALUE);

        EListRecord<QueryParameter> selectedRecord = parametersEDT.getSelectedRecord();
        QueryParameter selected;

        if (selectedRecord == null) {
            selected = new QueryParameter();
            parametersEDT.addRecord(new EListRecord<QueryParameter>(selected));
        } else {
            selected = selectedRecord.get();
        }

        String type;

        log("java:" + javaAction.isSelected());
        log("javascript:" + javascriptAction.isSelected());
        log("groovy:" + groovyAction.isSelected());

        if (javaAction.isSelected()) {
            type = JAVA;
        } else if (javascriptAction.isSelected()) {
            type = JAVASCRIPT;
        } else if (groovyAction.isSelected()) {
            type = GROOVY;
        } else {
            throw new RuntimeException("not implemented");
        }

        selected.setText(text);
        selected.setName(name);
        selected.setValue(value);
        selected.setType(type);

        parameterValue.setText("");
        parameterBuilder.setText("");
        parameterName.setText("");
        valueHolders.put(SELECTED, null);
        valueHolders.put(VALUE, null);

        parametersEDT.clearSelection();
    }

    private void addTableSelectionListener(final ETable<?> table, final TableSelectionListener listener) {
        final HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put(ROW, -1);
        map.put(COL, -1);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                outputSelection(ROW, map, table, listener);
            }
        });
        table.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                outputSelection(COL, map, table, listener);
            }
        });
    }

    private static void outputSelection(@SuppressWarnings("unused") String type, HashMap<String, Integer> map, ETable<?> table,
            TableSelectionListener listener) {
        int row = map.get(ROW);
        int col = map.get(COL);

        int row_ = ((table.getSelectedRows() == null) || (table.getSelectedRows().length == 0)) ? (-1) : table.getSelectedRows()[0];
        int col_ = ((table.getSelectedColumns() == null) || (table.getSelectedColumns().length == 0)) ? (-1) : table.getSelectedColumns()[0];

        if (table.getColumnSelectionAllowed() && table.getRowSelectionAllowed() && ((row_ != row) || (col_ != col))) {
            listener.cellChanged(row_, col_);
        } else if (table.getColumnSelectionAllowed() && (col_ != col)) {
            listener.columnChanged(col_);
        } else if (table.getRowSelectionAllowed() && (row_ != row)) {
            listener.rowChanged(row_);
        } else {
            //
        }

        map.put(ROW, row_);
        map.put(COL, col_);
    }

    /**
     * schedule a job
     * 
     * @param delay delay in milliseconds
     * @param job0 {@link Runnable}
     */
    @SuppressWarnings("unchecked")
    private void schedule(long delay, Runnable job0) {
        scheduleCancel();
        scheduledFuture = (ScheduledFuture<Object>) scheduler.schedule(job0, delay, TimeUnit.MILLISECONDS);
    }

    private void scheduleCancel() {
        if (scheduledFuture != null) {
            log("force stop: " + scheduledFuture.cancel(true));
            progressbarStop();
        }
    }

    private <T extends JComponent> T font(T comp, Integer size) {
        return hqlBuilderHelper.font(comp, size);
    }

    protected void start_query() {
        if (pauseQueryAction.isSelected()) {
            pauseQueryAction.setSelected(false);
        }
        scheduleQuery(0l, false);
    }

    protected void pause_query() {
        if (!pauseQueryAction.isSelected()) {
            scheduleQuery(0l, false);
        }
    }

    protected void export__copy_hql_as_java_to_clipboard() {
        try {
            StringBuilder sb = new StringBuilder("String hql = \"\";" + getNewline());

            for (String line : hql.getText().replaceAll("\r\n", getNewline()).split(getNewline())) {
                if (!line.startsWith("--") && !line.startsWith("//")) {
                    sb.append("hql +=\" ").append(line).append("\";");
                } else {
                    sb.append("// ").append(line);
                }

                sb.append(getNewline());
            }

            clipboard.setContents(new StringSelection(sb.toString()), getClipboardOwner());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    /**
     * TableSelectionListener
     */
    private interface TableSelectionListener {
        /**
         * row selection veranderd
         * 
         * @param row
         */
        public void rowChanged(int row);

        /**
         * column selection veranderd
         * 
         * @param column
         */
        public void columnChanged(int column);

        /**
         * cell selection veranderd
         * 
         * @param row
         * @param column
         */
        public void cellChanged(int row, int column);
    }

    /**
     * intern object
     * 
     * @author jdlandsh
     */
    protected static class ValueHolders extends HashMap<String, Object> {
        /** serialVersionUID */
        private static final long serialVersionUID = -7668322252952289124L;

        /** propertyChangeSupport */
        private final PropertyChangeSupport propertyChangeSupport;

        /**
         * Creates a new ValueHolders object.
         */
        public ValueHolders() {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }

        /**
         * 
         * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
         */
        @Override
        public Object put(String key, Object newValue) {
            Object oldValue = super.get(key);
            propertyChangeSupport.firePropertyChange(key, oldValue, newValue);

            return super.put(key, newValue);
        }

        /**
         * 
         * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
         */
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            this.propertyChangeSupport.addPropertyChangeListener(listener);
        }

        /**
         * 
         * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
         */
        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
        }

        /**
         * 
         * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
         */
        public PropertyChangeListener[] getPropertyChangeListeners() {
            return this.propertyChangeSupport.getPropertyChangeListeners();
        }

        /**
         * 
         * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
         */
        public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
            return this.propertyChangeSupport.getPropertyChangeListeners(propertyName);
        }

        /**
         * 
         * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
         */
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            this.propertyChangeSupport.removePropertyChangeListener(listener);
        }

        /**
         * 
         * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
         */
        public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
        }
    }

    protected void wizard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HqlWizard(hqlBuilderHelper, new HqlWizardListener() {
                    @Override
                    public void query(String query) {
                        hql.setText(query);
                    }
                }, frame, meta);
            }
        }).start();
    }

    protected void help() {
        try {
            Desktop.getDesktop().browse(URI.create(hqlBuilderHelper.getHelpUrl()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void hibernate_documentation() {
        try {
            Desktop.getDesktop().browse(URI.create("http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html/"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void hql_documentation() {
        try {
            Desktop.getDesktop().browse(URI.create("http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html/queryhql.html"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void offline_documentation() {
        try {
            File hibernate_doc_offline_dir = new File(File.createTempFile("prefix", "suffix").getParentFile(), "hibernate_doc_offline");
            if (!hibernate_doc_offline_dir.exists()) {
                hibernate_doc_offline_dir.mkdir();
            }
            File hibernate_doc_offline = new File(hibernate_doc_offline_dir, "hibernate_reference.pdf");
            if (!hibernate_doc_offline.exists()) {
                InputStream in = HqlBuilderFrame.class.getClassLoader().getResource("hibernate_reference.pdf").openStream();
                OutputStream fout = new FileOutputStream(hibernate_doc_offline);
                byte[] buffer = new byte[1024 * 8];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    fout.write(buffer, 0, read);
                }
                in.close();
                fout.close();
            }
            Desktop.getDesktop().open(hibernate_doc_offline);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void copy_selected_cell() {
        int selectedRow = resultsEDT.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no row selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int selectedColumn = resultsEDT.getSelectedColumn();
        if (selectedColumn == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no col selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object valueAt = resultsEDT.getValueAt(selectedRow, selectedColumn);
        if (valueAt == null) {
            return;
        }
        clipboard.setContents(new StringSelection(valueAt.toString()), getClipboardOwner());
    }

    private ClipboardOwner getClipboardOwner() {
        return new ClipboardOwner() {
            @Override
            public void lostOwnership(Clipboard cb, Transferable contents) {
                //
            }
        };
    }

    protected void execute_script_on_column() {
        int selectedCol = resultsEDT.getSelectedColumn();
        if (selectedCol == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no column selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        @SuppressWarnings("unchecked")
        Map<Integer, String> scripts = (Map<Integer, String>) valueHolders.get(SCRIPT);
        if (scripts == null) {
            scripts = new HashMap<Integer, String>();
            valueHolders.put(SCRIPT, scripts);
        }
        String script = scripts.get(selectedCol);
        script = JOptionPane.showInputDialog(frame, HqlResourceBundle.getMessage("groovy_cell"), script != null ? script : "");
        if (script == null) {
            scripts.remove(selectedCol);
            return;
        }
        boolean error = false;
        scripts.put(selectedCol, script);
        for (int row = 0; row < resultsEDT.getRowCount(); row++) {
            Object x = resultsEDT.getValueAt(row, selectedCol);
            if (x == null) {
                return;
            }
            try {
                x = Eval.x(x, script);
                resultsEDT.setValueAt(x, row, selectedCol);
            } catch (Exception ex) {
                ex.printStackTrace();
                error = true;
            }
        }
        if (error) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("script_exception"));
        }
        resultsEDT.repaint();
    }

    private ProgressGlassPane getGlass(JFrame parent) {
        if (glass == null) {
            glass = new ProgressGlassPane(parent);

            try {
                glass.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
            } catch (Exception ex) {
                //
            }
        }
        return glass;
    }

    private void setGlassVisible(boolean v) {
        if (glass != null && newProgressAction.isSelected()) {
            glass.setVisible(v);
        }
    }

    protected void maximum_number_of_results() {
        Object newValue = JOptionPane.showInputDialog(frame, HqlResourceBundle.getMessage(MAXIMUM_NUMBER_OF_RESULTS),
                String.valueOf(maximumNumberOfResultsAction.getValue()));
        if (newValue != null) {
            maximumNumberOfResultsAction.setValue(Integer.parseInt(String.valueOf(newValue)));
            maxResults.setText(" / " + newValue);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T get(Object o, String path, @SuppressWarnings("unused") Class<T> t) {
        return (T) new ObjectWrapper(o).get(path);
    }

    protected void format_sql() {
        formatSqlOptionsMenu.setEnabled(formatSqlAction.isSelected());
        immediateQuery();
    }

    protected void find_parameters() {
        Session session = sessionFactory.openSession();
        try {
            String hqlGetText = getHqlText();
            QueryImpl createQuery = (QueryImpl) session.createQuery(hqlGetText);
            org.hibernate.engine.query.ParameterMetadata pminof = get(createQuery, "parameterMetadata",
                    org.hibernate.engine.query.ParameterMetadata.class);
            parametersEDT.removeAllRecords();
            for (String param : createQuery.getNamedParameters()) {
                String simpleName = "?";
                try {
                    simpleName = pminof.getNamedParameterExpectedType(param).getReturnedClass().getSimpleName();
                } catch (Exception ex) {
                    //
                }
                QueryParameter p = new QueryParameter(null, param, simpleName, GROOVY);
                parametersEDT.addRecord(new EListRecord<QueryParameter>(p));
            }
            for (int i = 1; i <= pminof.getOrdinalParameterCount(); i++) {
                String simpleName = "?";
                try {
                    simpleName = pminof.getOrdinalParameterExpectedType(i).getReturnedClass().getSimpleName();
                } catch (Exception ex) {
                    //
                }
                QueryParameter p = new QueryParameter(null, null, i + ":" + simpleName, GROOVY);
                parametersEDT.addRecord(new EListRecord<QueryParameter>(p));
            }
        } finally {
            session.close();
        }
    }

    protected void add_to_favorites() {
        try {
            if (!hql_sql_tabs.getForegroundAt(1).equals(Color.GREEN)) {
                if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(frame, HqlResourceBundle.getMessage("no query"),
                        HqlResourceBundle.getMessage("add to favorites"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)) {
                    return;
                }
            }
            String name = JOptionPane.showInputDialog(frame, HqlResourceBundle.getMessage("add to favorites"),
                    HqlResourceBundle.getMessage("favorite name"));
            addLast(name);
        } catch (Exception ex) {
            log(ex);
        }
    }

    private void addLast(String name) throws IOException {
        List<QueryParameter> qps = new ArrayList<QueryParameter>();
        for (EListRecord<QueryParameter> qp : parametersEDT.getRecords()) {
            qps.add(qp.get());
        }
        String hql_text = hql.getText();
        QueryFavorite favorite = new QueryFavorite(name, hql_text, qps.toArray(new QueryParameter[qps.size()]));
        if (favorites.contains(favorite)) {
            favorites.remove(favorites.indexOf(favorite));
        }
        favorites.add(favorite);
        File xmlhistory = new File(FAVORITES_DIR, name + FAVORITES_EXT);
        if (!xmlhistory.getParentFile().exists()) {
            xmlhistory.getParentFile().mkdirs();
        }
        FileOutputStream os = new FileOutputStream(xmlhistory);
        XMLEncoder enc = new XMLEncoder(os);
        enc.writeObject(favorite);
        enc.close();
    }

    private void loadFavorites() {
        File[] xmls = new File(FAVORITES_DIR).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(FAVORITES_EXT);
            }
        });
        if (xmls == null) {
            return;
        }
        for (File xml : xmls) {
            FileInputStream is;
            try {
                is = new FileInputStream(xml);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            XMLDecoder dec = new XMLDecoder(is);
            QueryFavorite favorite = (QueryFavorite) dec.readObject();
            if (favorites.contains(favorite)) {
                favorites.remove(favorites.indexOf(favorite));
            }
            favorites.add(favorite);
            dec.close();
            if (xml.getName().equals(LAST + FAVORITES_EXT)) {
                importFromFavoritesNoQ(favorite);
            }
        }
    }

    protected void always_on_top() {
        boolean alwaysOnTop = Boolean.TRUE.equals(alwaysOnTopAction.getValue());
        frame.setAlwaysOnTop(alwaysOnTop);
    }

    protected void delete_object() {
        if (!editableResultsAction.isSelected()) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("results not editable"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int col = resultsEDT.getSelectedColumn();
        if (col == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no column selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int row = resultsEDT.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no row selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object bean = resultsEDT.getModel().getValueAt(row, col);
        if (hqlBuilderHelper.accept(bean.getClass())) {
            org.hibernate.classic.Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.delete(bean);
            resultsEDT.getModel().setValueAt(null, row, col);
            tx.commit();
            session.flush();
            propertypanel.removeAll();
            propertypanel.revalidate();
            propertypanel.repaint();
        } else {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("not a persistent object"),
                    HqlResourceBundle.getMessage("not a persistent object"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    protected void help_insert() {
        try {
            log("help insert");

            helpInsert();

            scheduleQuery(null, false);
        } catch (Exception ex) {
            log(ex);
        }
    }

    protected void remark_toggle() {
        log("remark toggle");

        int selectionStart = hql.getSelectionStart();
        int selectionEnd = hql.getSelectionEnd();
        String hqltext = hql.getText();

        hqltext = remarkToggle(hqltext, selectionStart, selectionEnd);
        hql.setText(hqltext);

        scheduleQuery(null, false);
    }

    private String hqlChanged = null;

    private boolean checkHqlChanged() {
        String newtext = getHqlTextEq();
        boolean changed = !new EqualsBuilder().append(hqlChanged, newtext).isEquals();
        hqlChanged = newtext;
        return changed;
    }

    protected void object_tree() {
        final int col = resultsEDT.getSelectedColumn();
        if (col == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no column selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final int row = resultsEDT.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no row selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        progressbarStart("fetching data");
        Object bean = resultsEDT.getModel().getValueAt(row, col);
        new ObjectTree(hqlBuilderHelper, sessionFactory, bean, editableResultsAction.isSelected()).setIconImage(frame.getIconImage());
        progressbarStop();
    }

    protected void resize_columns() {
        //
    }

    private void syntaxHi() {
        if (!hiliAction.isSelected()) {
            return;
        }

        LinkedList<int[]> pos = new LinkedList<int[]>();

        for (String kw : hqlBuilderHelper.getReservedKeywords()) {
            syntaxHi(pos, kw);
        }

        Collections.sort(pos, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return new CompareToBuilder().append(o1[0], o2[0]).toComparison();
            }
        });

        String hqltext = this.hql.getText().toLowerCase();

        Iterator<int[]> iterator = new ArrayList<int[]>(pos).iterator();

        if (iterator.hasNext()) {
            int[] pair1 = iterator.next();
            while (iterator.hasNext()) {
                int[] pair2 = iterator.next();
                // System.out.println(hqltext.substring(pair1[0], pair1[1]) + "*");
                // System.out.println(hqltext.substring(pair2[0], pair2[1]) + "*");
                // System.out.println(hqltext.charAt(pair1[1]) + "*");
                // System.out.println(pair1[0] + "," + pair1[1]);
                // System.out.println(pair2[0] + "," + pair2[1]);
                // System.out.println();
                if (pair1[1] + 1 == pair2[0] && (hqltext.charAt(pair1[1]) == ' ' || hqltext.charAt(pair1[1]) == '\t')) {
                    pair1[1] = pair2[1];
                    pos.remove(pair2);
                } else {
                    pair1 = pair2;
                }
            }
            for (int[] p : pos) {
                try {
                    this.hql.addHighlight(p[0], p[1], hili);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    protected void syntaxHi(List<int[]> pos, String str) {
        str = str.toLowerCase();
        String hqltext = this.hql.getText().toLowerCase();
        int fromIndex = 0;
        int start = hqltext.indexOf(str, fromIndex);
        while (start != -1) {
            int end = start + str.length();
            boolean before = start == 0 || isSeperator(hqltext.charAt(start - 1));
            boolean after = (end >= (hqltext.length() - 1)) || isSeperator(hqltext.charAt(end));
            if (before && after) {
                try {
                    pos.add(new int[] { start, end });
                } catch (Exception ex) {
                    //
                }
            }
            start = hqltext.indexOf(str, end);
        }
    }

    private void syntaxHiRemove() {
        this.hql.removeHighlights(hili);
    }

    private boolean isSeperator(char c) {
        return Character.isWhitespace(c) || c == '\'' || c == '(' || c == ')' || c == ',' || c == '-' || c == '/';
    }

    protected void highlight_color() {
        Color color = getHiliColor();
        color = JColorChooser.showDialog(null, HqlResourceBundle.getMessage("Choose HQL highlight color"), color);
        hiliColorAction.setValue(color);
        syntaxHiRemove();
        hili.setColor(color);
        hiko.setColor(color);
    }

    private Color getHiliColor() {
        return hiliColorAction.getValue() == null ? new Color(0, 0, 255) : (Color) hiliColorAction.getValue();
    }

    private Color chooseColor() {
        Color color = getSearchColor();
        color = JColorChooser.showDialog(null, HqlResourceBundle.getMessage("Choose search highlight color"), color);
        return color;
    }

    protected void search_color() {
        searchColorAction.setValue(applyColor(chooseColor()));
    }

    private Color applyColor(Color color) {
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        Color gradientColor = calcGradient(color);
        ETextAreaFillHighlightPainter painter = ETextAreaFillHighlightPainter.class.cast(hql.getHighlightPainter());
        painter.setVerticalGradient(true);
        painter.setColor(color);
        painter.setGradientColor(gradientColor);
        return color;
    }

    private Color calcGradient(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
    }

    private Color getSearchColor() {
        return searchColorAction.getValue() == null ? new Color(245, 225, 145) : (Color) searchColorAction.getValue();
    }

    private void reloadColor() {
        applyColor(getSearchColor());
    }

    protected void Lucene_query_syntax() {
        try {
            Desktop.getDesktop().browse(URI.create("http://lucene.apache.org/core/old_versioned_docs/versions/3_5_0/queryparsersyntax.html"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void hibernate_properties() {
        int selectedRow = resultsEDT.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no row selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int selectedColumn = resultsEDT.getSelectedColumn();
        if (selectedColumn == -1) {
            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("no col selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object value = resultsEDT.getValueAt(selectedRow, selectedColumn);
        if (value == null) {
            return;
        }
        Class<?> clas = value.getClass();
        String classname = clas.getName();
        @SuppressWarnings("unchecked")
        Map<String, AbstractEntityPersister> allClassMetadata = sessionFactory.getAllClassMetadata();
        AbstractEntityPersister classMeta = allClassMetadata.get(classname);
        if (classMeta == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        List<String> propertyNames = new ArrayList<String>(Arrays.asList(classMeta.getPropertyNames()));
        propertyNames.remove("id");
        propertyNames.remove("version");
        for (int i = 0; i < propertyNames.size() - 1; i++) {
            sb.append(propertyNames.get(i)).append(",").append(getNewline());
        }
        if (propertyNames.size() > 0) {
            sb.append(propertyNames.get(propertyNames.size() - 1));
        }
        clipboard.setContents(new StringSelection(sb.toString()), getClipboardOwner());
    }

    public static int find(String t, int pos) {
        try {
            if (t.charAt(pos) == '(') {
                return zoekHaakje(t, pos, +1);
            } else if (t.charAt(pos) == ')') {
                return zoekHaakje(t, pos, -1);
            } else {
                return -1;
            }
        } catch (StringIndexOutOfBoundsException ex) {
            return -1;
        }
    }

    public static int zoekHaakje(String t, int startpos, int direction) {
        int position = startpos + direction;
        int count = direction;
        while (true) {
            char c = t.charAt(position);
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
            }
            if (count == 0) {
                break;
            }
            position = position + direction;
        }
        return position;
    }

    public void hilightBraces(String hqltext) {
        this.hql.removeHighlights(hiko);

        if (!hiliAction.isSelected()) {
            return;
        }

        try {
            int p1 = hql.getCaret().getDot();
            int p2 = hql.getCaret().getMark();
            int min = Math.min(p1, p2);
            int max = Math.min(p1, p2);
            int len = max - min;
            if (len > 1) {
                return;
            }

            int caret = min;
            int match = find(hqltext, caret);
            if (match != -1) {
                try {
                    this.hql.addHighlight(caret, caret + 1, hiko);
                    this.hql.addHighlight(match, match + 1, hiko);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            //
        }
    }

    protected void load_named_query() {
        final EList<String> list = new EList<String>(new EListConfig());
        list.addRecords(EList.convert(new TreeSet<String>(namedQueries.keySet())));
        JPanel container = new JPanel(new BorderLayout());
        container.add(new JScrollPane(list), BorderLayout.CENTER);
        container.add(list.getSearchComponent(), BorderLayout.NORTH);
        if (ResultType.OK == CustomizableOptionPane.showCustomDialog(hql, container, HqlResourceBundle.getMessage(LOAD_NAMED_QUERY),
                MessageType.QUESTION, OptionType.OK_CANCEL, null, new ListOptionPaneCustomizer<String>(list))) {
            if (list.getSelectedRecord() != null) {
                clear();
                hql.setText(namedQueries.get(list.getSelectedRecord().get()));
            }
        }
    }
}
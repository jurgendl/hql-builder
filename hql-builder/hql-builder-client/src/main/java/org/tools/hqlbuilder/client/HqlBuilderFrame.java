package org.tools.hqlbuilder.client;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
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
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Utilities;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.text.WordUtils;
import org.jhaws.common.lang.IntegerValue;
import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.LoggerFactory;
import org.swingeasy.CustomizableOptionPane;
import org.swingeasy.DocumentKeyListener;
import org.swingeasy.EButton;
import org.swingeasy.EButtonConfig;
import org.swingeasy.ECheckBox;
import org.swingeasy.ECheckBoxConfig;
import org.swingeasy.EComponentGradientRenderer;
import org.swingeasy.EComponentPopupMenu;
import org.swingeasy.EFontChooser;
import org.swingeasy.EFormattedTextField;
import org.swingeasy.EFormattedTextFieldConfig;
import org.swingeasy.ELabel;
import org.swingeasy.ELabeledTextFieldButtonComponent;
import org.swingeasy.EList;
import org.swingeasy.EListConfig;
import org.swingeasy.EListRecord;
import org.swingeasy.ETable;
import org.swingeasy.ETableConfig;
import org.swingeasy.ETableHeaders;
import org.swingeasy.ETableRecord;
import org.swingeasy.ETableRecordArray;
import org.swingeasy.ETableRecordCollection;
import org.swingeasy.ETextArea;
import org.swingeasy.ETextAreaConfig;
import org.swingeasy.ETextComponentBorderHighlightPainter;
import org.swingeasy.ETextComponentFillHighlightPainter;
import org.swingeasy.ETextField;
import org.swingeasy.ETextFieldConfig;
import org.swingeasy.EToolBarButton;
import org.swingeasy.EToolBarButtonConfig;
import org.swingeasy.EToolBarButtonCustomizer;
import org.swingeasy.EURILabel;
import org.swingeasy.EventHelper;
import org.swingeasy.EventModifier;
import org.swingeasy.ListOptionPaneCustomizer;
import org.swingeasy.MessageType;
import org.swingeasy.MouseDoubleClickAction;
import org.swingeasy.ObjectWrapper;
import org.swingeasy.OptionType;
import org.swingeasy.ProgressGlassPane;
import org.swingeasy.ResultType;
import org.swingeasy.UIUtils;
import org.swingeasy.formatters.NumberFormatBuilder;
import org.swingeasy.system.SystemSettings;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.ExecutionResult;
import org.tools.hqlbuilder.common.GroovyCompiler;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.HqlBuilderImages;
import org.tools.hqlbuilder.common.QueryParameter;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.exceptions.ServiceException;
import org.tools.hqlbuilder.common.exceptions.SqlException;
import org.tools.hqlbuilder.common.exceptions.SyntaxException;
import org.tools.hqlbuilder.common.exceptions.SyntaxException.SyntaxExceptionType;
import org.tools.hqlbuilder.common.icons.ClientIcons;
import org.tools.hqlbuilder.common.icons.CommonIcons;

import net.miginfocom.swing.MigLayout;

/**
 * @author Jurgen
 */
public class HqlBuilderFrame implements HqlBuilderFrameConstants {
    private static final Color TAB_OK_COLOR = new Color(0, 200, 0);

    private static final float DEFAULT_FONT_SIZE = 16f;

    private static final Color HIGHLIGHT_ERROR_DEFAULT_COLOR = Color.RED;

    private static final Color HIGHLIGHT_SYNTAXT_DEFAULT_COLOR = new Color(19, 128, 19);

    private static final Color HIGHLIGHT_SEARCH_DEFAULT_COLOR = new Color(245, 225, 145);

    private static final Color HIGHLIGHT_BRACES_DEFAULT_COLOR = new Color(164, 164, 226);

    private static List<QueryParameter> convertParameterString(String parameters) {
        List<QueryParameter> qps = new ArrayList<>();
        if (parameters.startsWith("{") && parameters.endsWith("}")) {
            String replaced = parameters.replace("{", "[").replace("}", "]").replace("=", ":").replace("%", "PERCENT");
            Object eval = GroovyCompiler.eval(replaced);
            Map<?, ?> map = (Map<?, ?>) eval;
            map.entrySet().stream().map(e -> convertToParameter(e)).forEach(qps::add);
        } else if (parameters.startsWith("[") && parameters.endsWith("]")) {
            List<?> list = (List<?>) GroovyCompiler.eval(parameters);
            IntegerValue index = new IntegerValue();
            list.stream().map(v -> convertToParameter(index, v)).forEach(qps::add);
        }
        return qps;
    }

    private static QueryParameter convertToParameter(Entry<?, ?> e) {
        Object value = e.getValue();
        String vs = String.valueOf(value);
        return new QueryParameter(e.getKey().toString(), vs, value.getClass().getName(), e.getValue());
    }

    private static QueryParameter convertToParameter(IntegerValue index, Object value) {
        String vs = String.valueOf(value);
        return new QueryParameter(index.add().get(), vs, value.getClass().getName(), value);
    }

    public static int find(String t, int pos) {
        try {
            if (t.charAt(pos) == '(') {
                return HqlBuilderFrame.zoekHaakje(t, pos, +1);
            } else if (t.charAt(pos) == ')') {
                return HqlBuilderFrame.zoekHaakje(t, pos, -1);
            } else {
                return -1;
            }
        } catch (StringIndexOutOfBoundsException ex) {
            return -1;
        }
    }

    public static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        return response.toString();
    }

    private static void outputSelection(String type, HashMap<String, Integer> map, ETable<?> table, TableSelectionListener listener) {
        int row = map.get(HqlBuilderFrameConstants.ROW);
        int col = map.get(HqlBuilderFrameConstants.COL);

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

        map.put(HqlBuilderFrameConstants.ROW, row_);
        map.put(HqlBuilderFrameConstants.COL, col_);
    }

    /**
     * start app
     */
    public static void start(String[] args, HqlServiceClientLoader serviceLoader) {
        try {
            // System.out.println("arguments: " + Arrays.asList(args));

            // zet look en feel gelijk aan default voor OS
            UIUtils.systemLookAndFeel();

            Preferences preferences = Preferences.userRoot().node(HqlBuilderFrameConstants.PERSISTENT_ID);
            String lang = preferences.get(HqlBuilderFrameConstants.PERSISTENT_LOCALE, SystemSettings.getCurrentLocale().getLanguage());
            SystemSettings.setCurrentLocale(new Locale(lang));

            String version = fetchVersion();
            String latestVersion = fetchLatestVersion();
            if ("?".equals(version)) version = latestVersion;

            SplashHelper.setup(version);
            SplashHelper.step();

            CommonUtils.run(200l, SplashHelper::progress, Exception::printStackTrace);

            // problem in "all in one" setup
            // sending 'wrong' locale to Oracle gives exception
            // Oracle will now give English exceptions
            SystemSettings.setCurrentLocale(Locale.UK);
            HqlServiceClient service = serviceLoader.getHqlServiceClient();
            // now load users locale
            SystemSettings.setCurrentLocale(new Locale(lang));

            SplashHelper.update(service.getConnectionInfo());
            SplashHelper.step();

            if (!HqlBuilderFrameConstants.PROGRAM_DIR.exists()) {
                HqlBuilderFrameConstants.PROGRAM_DIR.mkdirs();
            }
            if (!HqlBuilderFrameConstants.FAVORITES_DIR.exists()) {
                HqlBuilderFrameConstants.FAVORITES_DIR.mkdirs();
            }

            SplashHelper.step();

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

            GroovyCompiler.eval("new Long(0l)"); // warm up Groovy

            SplashHelper.step();

            // maak frame en start start
            final HqlBuilderFrame hqlBuilder = new HqlBuilderFrame();
            hqlBuilder.preferences = preferences;
            hqlBuilder.hqlService = service;

            SplashHelper.step();

            hqlBuilder.startPre();

            SplashHelper.step();

            hqlBuilder.start(latestVersion, version);

            hqlBuilder.hql.grabFocus();

            // log connectie
            HqlBuilderFrame.logger.info(hqlBuilder.hqlService.getConnectionInfo());

            SplashHelper.step();
            SplashHelper.end();

            // wanneer system tray wordt ondersteund, gebruikt deze dan (configureerbaar)
            if (SystemTray.isSupported()) {
                ActionListener listener = e -> hqlBuilder.switchVisibility();
                hqlBuilder.trayIcon = new TrayIcon(hqlBuilder.frame.getIconImage(), hqlBuilder.frame.getTitle());
                PopupMenu popupmenu = new PopupMenu(hqlBuilder.frame.getTitle());
                {
                    MenuItem switchvisibility = new MenuItem(HqlResourceBundle.getMessage("show"));
                    switchvisibility.addActionListener(listener);
                    popupmenu.add(switchvisibility);
                }
                {

                    MenuItem trayswitch = new MenuItem(HqlResourceBundle.getMessage("disable systemtray"));
                    trayswitch.addActionListener(e -> {
                        boolean newvalue = !hqlBuilder.systrayAction.isSelected();
                        hqlBuilder.systrayAction.setSelected(newvalue);
                        if (newvalue && !hqlBuilder.frame.isVisible()) {
                            hqlBuilder.switchVisibility();
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
            HqlBuilderFrame.logger.error("{}", ex);
            SplashHelper.end();
            ex.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, WordUtils.wrap("" + ex, 100), "Fatal Exception", JOptionPane.ERROR_MESSAGE);
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

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlBuilderFrame.class);

    private String latestVersion;

    private String version;

    private Preferences preferences;

    private final JFrame frame = new JFrame();

    private final JPanel hql_sql_tabs_panel = new JPanel(new BorderLayout());

    private final JTabbedPane hql_sql_tabs = new JTabbedPane();

    private final ELabel resultsInfo;

    private final JPanel propertypanel = new JPanel(new BorderLayout());

    private final ETextField parameterBuilder;

    private final ETextField parameterName;

    private final ETextField parameterValue;

    private final ETextArea hql;

    private JScrollPane hqlsp;

    private final ETextArea sql;

    private final EList<QueryParameter> parametersUnsafe;

    private final EList<QueryParameter> parametersEDT;

    private final ETable<List<Object>> resultsUnsafe;

    private final ETable<List<Object>> resultsEDT;

    private final HqlBuilderAction hibernatePropertiesAction;

    private final HqlBuilderAction objectTreeAction;

    private final HqlBuilderAction deleteObjectAction;

    private final HqlBuilderAction copyCellAction;

    private final HqlBuilderAction executeScriptOnColumnAction;

    private final HqlBuilderAction downAction;

    private final HqlBuilderAction removeAction;

    private final HqlBuilderAction saveAction;

    private final HqlBuilderAction setNullAction;

    private final HqlBuilderAction toTextAction;

    private final HqlBuilderAction upAction;

    private final HqlBuilderAction addParameterAction;

    private final HqlBuilderAction importParametersAction;

    private final HqlBuilderAction wizardAction;

    private final HqlBuilderAction clearAction;

    private final HqlBuilderAction findParametersAction;

    private final HqlBuilderAction favoritesAction;

    private final HqlBuilderAction addToFavoritesAction;

    private final HqlBuilderAction startQueryAction;

    private final HqlBuilderAction stopQueryAction;

    private final HqlBuilderAction formatAction;

    private final HqlBuilderAction namedQueryAction;

    private final HqlBuilderAction clipboardExportAction;

    private final HqlBuilderAction clipboardImportAction;

    private final HqlBuilderAction helpInsertAction;

    private final HqlBuilderAction remarkToggleAction;

    private final HqlBuilderAction deleteInvertedSelectionAction;

    private final HqlBuilderAction helpAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.HELP, true, HqlBuilderFrameConstants.HELP,
            CommonIcons.getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP), HqlBuilderFrameConstants.HELP, HqlBuilderFrameConstants.HELP,
            true, 'h', "F1");

    private final HqlBuilderAction exitAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.EXIT, true, HqlBuilderFrameConstants.EXIT,
            CommonIcons.getIcon(ClientIcons.DOOR), HqlBuilderFrameConstants.EXIT, HqlBuilderFrameConstants.EXIT, true, 'x', "alt X");

    private final HqlBuilderAction aboutAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.ABOUT, true,
            HqlBuilderFrameConstants.ABOUT, HqlBuilderImages.getIcon(), HqlBuilderFrameConstants.ABOUT, HqlBuilderFrameConstants.ABOUT, true, null,
            null);

    private final HqlBuilderAction versionsAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.VERSIONS, true,
            HqlBuilderFrameConstants.VERSIONS, HqlBuilderImages.getIcon(), HqlBuilderFrameConstants.VERSIONS, HqlBuilderFrameConstants.VERSIONS, true,
            null, null);

    private final HqlBuilderAction helpHibernateAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.HIBERNATE_DOCUMENTATION, true,
            HqlBuilderFrameConstants.HIBERNATE_DOCUMENTATION, CommonIcons.getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP),
            HqlBuilderFrameConstants.HIBERNATE_DOCUMENTATION, HqlBuilderFrameConstants.HIBERNATE_DOCUMENTATION, true, null, null);

    private final HqlBuilderAction helpHqlAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.HQL_DOCUMENTATION, true,
            HqlBuilderFrameConstants.HQL_DOCUMENTATION, CommonIcons.getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP),
            HqlBuilderFrameConstants.HQL_DOCUMENTATION, HqlBuilderFrameConstants.HQL_DOCUMENTATION, true, null, null);

    private final HqlBuilderAction helpJavaPropertiesAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.JAVA_PROPERTIES, true,
            HqlBuilderFrameConstants.JAVA_PROPERTIES, CommonIcons.getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP),
            HqlBuilderFrameConstants.JAVA_PROPERTIES, HqlBuilderFrameConstants.JAVA_PROPERTIES, true, null, null);

    private final HqlBuilderAction luceneQuerySyntaxAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.LUCENE_QUERY_SYNTAX, true,
            HqlBuilderFrameConstants.LUCENE_QUERY_SYNTAX, CommonIcons.getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP),
            HqlBuilderFrameConstants.LUCENE_QUERY_SYNTAX, HqlBuilderFrameConstants.LUCENE_QUERY_SYNTAX, true, null, null);

    private final HqlBuilderAction forceExitAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.FORCE_EXIT, true,
            HqlBuilderFrameConstants.FORCE_EXIT, CommonIcons.getIcon(ClientIcons.CROSS), HqlBuilderFrameConstants.FORCE_EXIT,
            HqlBuilderFrameConstants.FORCE_EXIT, true, 't', null);

    private final HqlBuilderAction removeJoinsAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.IMMEDIATE_QUERY, true,
            HqlBuilderFrameConstants.REMOVE_JOINS, null, HqlBuilderFrameConstants.REMOVE_JOINS, HqlBuilderFrameConstants.REMOVE_JOINS, true, null,
            null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction formatLinesAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.IMMEDIATE_QUERY, true,
            HqlBuilderFrameConstants.FORMAT_LINES, null, HqlBuilderFrameConstants.FORMAT_LINES, HqlBuilderFrameConstants.FORMAT_LINES, true, null,
            null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction replacePropertiesAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.IMMEDIATE_QUERY, true,
            HqlBuilderFrameConstants.REPLACE_PROPERTIES, null, HqlBuilderFrameConstants.REPLACE_PROPERTIES,
            HqlBuilderFrameConstants.REPLACE_PROPERTIES, true, null, null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction resizeColumnsAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.RESIZE_COLUMNS, true,
            HqlBuilderFrameConstants.RESIZE_COLUMNS, null, HqlBuilderFrameConstants.RESIZE_COLUMNS, HqlBuilderFrameConstants.RESIZE_COLUMNS, true,
            null, null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction formatSqlAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.FORMAT_SQL, true,
            HqlBuilderFrameConstants.FORMAT_SQL, null, HqlBuilderFrameConstants.FORMAT_SQL, HqlBuilderFrameConstants.FORMAT_SQL, true, null, null,
            HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction maximumNumberOfResultsAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_RESULTS,
            true, HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_RESULTS, "org/tools/hqlbuilder/client/images/scr.png",
            HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_RESULTS, HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_RESULTS, true, null, null,
            HqlBuilderFrameConstants.PERSISTENT_ID, Integer.class, 100);

    private final HqlBuilderAction maximumNumberOfSearchResultsAction = new HqlBuilderAction(null, this,
            HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_SEARCH_RESULTS, true, HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_SEARCH_RESULTS,
            "org/tools/hqlbuilder/client/images/scr.png", HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_SEARCH_RESULTS,
            HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_SEARCH_RESULTS, true, null, null, HqlBuilderFrameConstants.PERSISTENT_ID, Integer.class, 2000);

    private final HqlBuilderAction showToolbarsAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.SHOW_TOOLBARS, true,
            HqlBuilderFrameConstants.SHOW_TOOLBARS, null, HqlBuilderFrameConstants.SHOW_TOOLBARS, HqlBuilderFrameConstants.SHOW_TOOLBARS, true, null,
            null, HqlBuilderFrameConstants.PERSISTENT_ID);


    private HqlBuilderAction fontAction;

    private final HqlBuilderAction systrayAction = new HqlBuilderAction(null, this, null, true, HqlBuilderFrameConstants.SYSTEM_TRAY, null,
            HqlBuilderFrameConstants.SYSTEM_TRAY, HqlBuilderFrameConstants.SYSTEM_TRAY, true, null, null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction highlightSyntaxAction = new HqlBuilderAction(null, this, null, true, HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX,
            null, HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX, HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX, true, null, null,
            HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction continuousSyntaxHighlightingAction;

    private final HqlBuilderAction highlightBracesAction = new HqlBuilderAction(null, this, null, true, HqlBuilderFrameConstants.HIGHLIGHT_BRACES,
            null, HqlBuilderFrameConstants.HIGHLIGHT_BRACES, HqlBuilderFrameConstants.HIGHLIGHT_BRACES, true, null, null,
            HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction highlightSyntaxColorAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX_COLOR,
            true, HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX_COLOR, null, HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX_COLOR,
            HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX_COLOR, false, null, null, HqlBuilderFrameConstants.PERSISTENT_ID, Color.class,
            HIGHLIGHT_SYNTAXT_DEFAULT_COLOR);

    private final HqlBuilderAction highlightBracesColorAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.HIGHLIGHT_BRACES_COLOR,
            true, HqlBuilderFrameConstants.HIGHLIGHT_BRACES_COLOR, null, HqlBuilderFrameConstants.HIGHLIGHT_BRACES_COLOR,
            HqlBuilderFrameConstants.HIGHLIGHT_BRACES_COLOR, false, null, null, HqlBuilderFrameConstants.PERSISTENT_ID, Color.class,
            HIGHLIGHT_BRACES_DEFAULT_COLOR);

    private final HqlBuilderAction searchColorAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.SEARCH_COLOR, true,
            HqlBuilderFrameConstants.SEARCH_COLOR, null, HqlBuilderFrameConstants.SEARCH_COLOR, HqlBuilderFrameConstants.SEARCH_COLOR, false, null,
            null, HqlBuilderFrameConstants.PERSISTENT_ID, Color.class, HIGHLIGHT_SEARCH_DEFAULT_COLOR);

    private final HqlBuilderAction alwaysOnTopAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.ALWAYS_ON_TOP, true,
            HqlBuilderFrameConstants.ALWAYS_ON_TOP, null, HqlBuilderFrameConstants.ALWAYS_ON_TOP, HqlBuilderFrameConstants.ALWAYS_ON_TOP, false, null,
            null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction editableResultsAction;

    private final HqlBuilderAction switchLayoutAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.SWITCH_LAYOUT, true,
            HqlBuilderFrameConstants.SWITCH_LAYOUT, CommonIcons.getIcon(ClientIcons.LAYOUT_CONTENT), HqlBuilderFrameConstants.SWITCH_LAYOUT,
            HqlBuilderFrameConstants.SWITCH_LAYOUT, false, 'w', null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction addEndBraceAction = new HqlBuilderAction(null, this, null, true, HqlBuilderFrameConstants.ADD_END_BRACE, null,
            HqlBuilderFrameConstants.ADD_END_BRACE, HqlBuilderFrameConstants.ADD_END_BRACE, true, null, null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction addShowErrorTooltip = new HqlBuilderAction(null, this, null, true, HqlBuilderFrameConstants.SHOW_ERROR_TOOLTIP,
            null, HqlBuilderFrameConstants.SHOW_ERROR_TOOLTIP, HqlBuilderFrameConstants.SHOW_ERROR_TOOLTIP, true, null, null,
            HqlBuilderFrameConstants.PERSISTENT_ID);

    private final HqlBuilderAction addSelectExecutedHql = new HqlBuilderAction(null, this, null, true,
            HqlBuilderFrameConstants.SELECT_HQL_BEING_EXECUTED, null, HqlBuilderFrameConstants.SELECT_HQL_BEING_EXECUTED,
            HqlBuilderFrameConstants.SELECT_HQL_BEING_EXECUTED, true, null, null, HqlBuilderFrameConstants.PERSISTENT_ID);

    private final ELabel maxResults;

    private final EFormattedTextField<Integer> startResults = new EFormattedTextField<>(
            new EFormattedTextFieldConfig(new NumberFormatBuilder(NumberFormatBuilder.Type.Integer)).setColumns(12), 0);

    private final LinkedList<QueryFavorite> favorites = new LinkedList<>();

    private final JComponent values = ClientUtils.getPropertyFrame(new Serializable() {
        private static final long serialVersionUID = 1L;
    }, false);

    /** aliases query */
    private Map<String, String> aliases = new HashMap<>();

    /** selected query parameter */
    private QueryParameter selectedQueryParameter = new QueryParameter();

    /** scripts being executed on column */
    private Map<Integer, String> scripts = new HashMap<>();

    /** record count query */
    private int recordCount = 0;

    private final JPanel resultPanel = new JPanel(new BorderLayout());

    private final JPanel parameterspanel = new JPanel(new MigLayout("wrap 3", "[]rel[grow]rel[]", "[][][][shrink][shrink][shrink][shrink][grow]"));

    private final ETextField importParametersFromTextF = new ETextField(new ETextFieldConfig());

    private final JPanel normalContentPane = new JPanel(new BorderLayout());

    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private final JMenu formatSqlOptionsMenu = new JMenu(HqlResourceBundle.getMessage("format sql options"));

    private HibernateWebResolver hibernateWebResolver;

    private EList<String> insertPropertyHelper;

    private JPopupMenu insertClassHelper;

    private EList<String> insertHelperList;

    private JPopupMenu insertHelper;

    private JSplitPane split0;

    private JSplitPane split1;

    private JSplitPane split2;

    private ProgressGlassPane glass = null;

    private ETextComponentBorderHighlightPainter syntaxHighlight = new ETextComponentBorderHighlightPainter(this.getSyntaxHighlightColor());

    private ETextComponentFillHighlightPainter bracesHighlight = new ETextComponentFillHighlightPainter(this.getBracesHighlightColor());

    private ETextComponentBorderHighlightPainter syntaxErrorsHighlight = new ETextComponentBorderHighlightPainter(HIGHLIGHT_ERROR_DEFAULT_COLOR);

    private TrayIcon trayIcon;

    private HqlServiceClient hqlService;

    private boolean ingoreParameterListSelectionListener = false;

    private EComponentGradientRenderer backgroundRenderer = new EComponentGradientRenderer(EComponentGradientRenderer.GradientOrientation.VERTICAL,
            Color.white, new Color(212, 212, 212));

    private final List<Highlighter.Highlight> errorLocs = new ArrayList<>();

    private String errorString;

    private Set<String> reservedKeywords;

    private JToolBar hqltools;

    private JToolBar resultstools;

    private final EButton nextResultsButton;

    private final EButton backToStartResultsButton;

    private SwingWorker<ExecutionResult, Void> queryExecution = null;

    private QueryParameters queryParameters = null;

    private HqlBuilderFrame() {
        // needs to be first to init font
        this.fontAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.FONT, true, HqlBuilderFrameConstants.FONT,
                CommonIcons.getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.FONT), HqlBuilderFrameConstants.FONT, HqlBuilderFrameConstants.FONT,
                true, null, null, HqlBuilderFrameConstants.PERSISTENT_ID, Font.class, ClientUtils.getDefaultFont().deriveFont(16f));
        this.fontAction.setWarnRestart(true);

        nextResultsButton = new EButton(new EButtonConfig(new AbstractAction(" > ") {
            private static final long serialVersionUID = 2525393811237450637L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Number maximumNumberOfResults = (Number) maximumNumberOfResultsAction.getValue();
                Number startResult = (Number) startResults.getValue();
                startResults.setValue(startResult.intValue() + maximumNumberOfResults.intValue());
                start_query(false);
            }
        }));
        backToStartResultsButton = new EButton(new EButtonConfig(new AbstractAction(" |< ") {
            private static final long serialVersionUID = 2525393811237450637L;

            @Override
            public void actionPerformed(ActionEvent e) {
                start_query(true);
            }
        }));

        UIManager.put("ToolTip.font", new FontUIResource(this.getFont()));
        this.resultsInfo = this.font(new ELabel(""), null, Font.BOLD);
        this.parameterBuilder = this.font(new ETextField(new ETextFieldConfig()), null);
        this.parameterName = this.font(new ETextField(new ETextFieldConfig()), null);
        this.parameterValue = this.font(new ETextField(new ETextFieldConfig(false)), null);
        ETextArea hqlTextArea = new ETextArea(new ETextAreaConfig().setTooltips(true)) {
            private static final long serialVersionUID = 5778951450821178464L;

            @Override
            public String getToolTipText(MouseEvent event) {
                if (HqlBuilderFrame.this.addShowErrorTooltip.isSelected()) {
                    if (StringUtils.isNotBlank(HqlBuilderFrame.this.errorString)) {
                        int offs = this.viewToModel(event.getPoint());
                        try {
                            Rectangle modelToView = this.modelToView(offs);
                            if (Math.abs(event.getPoint().x - modelToView.x) > 17) {
                                return null;
                            }
                            if (Math.abs(event.getPoint().y - modelToView.y) > 17) {
                                return null;
                            }
                        } catch (BadLocationException ex) {
                            //
                        }
                        boolean showTT = true;
                        if (HqlBuilderFrame.this.errorLocs.size() > 0) {
                            showTT = false;
                            for (Highlight hl : HqlBuilderFrame.this.errorLocs) {
                                if ((hl.getStartOffset() <= offs) && (offs <= hl.getEndOffset())) {
                                    showTT = true;
                                }
                            }
                        }
                        if (!showTT) {
                            return null;
                        }
                        if (HqlBuilderFrame.this.errorString.length() > 100) {
                            return "<html><p width=800>" + HqlBuilderFrame.this.errorString + "</p><html>";
                        }
                        return "<html><p>" + HqlBuilderFrame.this.errorString + "</p><html>";
                    }
                }
                try {
                    int offs = this.viewToModel(event.getPoint());
                    int startIndex = Utilities.getWordStart(this, offs);
                    int endIndex = Utilities.getWordEnd(this, offs);
                    String substring = this.getText().substring(Math.max(0, startIndex - 1), endIndex);
                    if (substring.startsWith(":")) {
                        String tt = substring.substring(1);
                        for (EListRecord<QueryParameter> record : HqlBuilderFrame.this.parametersEDT.getRecords()) {
                            if (tt.equals(record.get().getName())) {
                                return record.get().toString();
                            }
                        }
                    }
                    if ((substring.length() == 2) && (substring.charAt(1) == '?')) {
                        String textUpTo = this.getText().substring(0, Math.max(0, startIndex - 1));
                        String lines[] = textUpTo.split(HqlBuilderFrameConstants.LINESEPERATOR);
                        int count = 0;
                        boolean remarked = false;
                        for (String line : lines) {
                            int dash = line.indexOf(HqlBuilderFrameConstants.REMARKTAG);
                            remarked = dash != -1;
                            if (dash != -1) {
                                line = line.substring(0, dash);
                            }
                            count += StringUtils.countMatches(line, "?");
                        }
                        if (!remarked) {
                            try {
                                return HqlBuilderFrame.this.parametersEDT.getRecords().get(count).get().toString();
                            } catch (IndexOutOfBoundsException ex) {
                                //
                            }
                        }
                    }
                } catch (BadLocationException ex) {
                    //
                }
                return null;
            }
        };
        continuousSyntaxHighlightingAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX_CONTINUOUS, true,
                HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX_CONTINUOUS, null, HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX_CONTINUOUS,
                HqlBuilderFrameConstants.HIGHLIGHT_SYNTAX_CONTINUOUS, true, null, null, HqlBuilderFrameConstants.PERSISTENT_ID, Boolean.class,
                Boolean.TRUE);
        hqlTextArea.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                if (Boolean.TRUE.equals(continuousSyntaxHighlightingAction.getValue())) {
                    hilightSyntax();
                }
            }
        });
        this.hql = this.font(hqlTextArea, null);
        ToolTipManager.sharedInstance().registerComponent(this.hql);
        this.sql = this.font(new ETextArea(new ETextAreaConfig(false)), null);
        this.maxResults = this.font(new ELabel(""), null, Font.BOLD);
        this.setMaxResults(Integer.parseInt("" + this.maximumNumberOfResultsAction.getValue()));
        new MouseDoubleClickAction(this.maximumNumberOfResultsAction).inject(this.maxResults);
        this.parametersUnsafe = this.font(new EList<QueryParameter>(new EListConfig().setBackgroundRenderer(this.backgroundRenderer)), null);
        this.parametersUnsafe.setFixedCellHeight(20);
        this.parametersEDT = this.parametersUnsafe.stsi();
        this.resultsUnsafe = this.font(new ETable<List<Object>>(new ETableConfig(true)), null);
        this.resultsEDT = this.resultsUnsafe.stsi();
        this.hibernatePropertiesAction = new HqlBuilderAction(this.resultsUnsafe, this, HqlBuilderFrameConstants.HIBERNATE_PROPERTIES, true,
                HqlBuilderFrameConstants.HIBERNATE_PROPERTIES, CommonIcons.getIcon(ClientIcons.PAGE_WHITE_STACK),
                HqlBuilderFrameConstants.HIBERNATE_PROPERTIES, HqlBuilderFrameConstants.HIBERNATE_PROPERTIES, true, null, "shift alt F8");
        this.objectTreeAction = new HqlBuilderAction(this.resultsUnsafe, this, HqlBuilderFrameConstants.OBJECT_TREE, true,
                HqlBuilderFrameConstants.OBJECT_TREE, CommonIcons.getIcon(ClientIcons.APPLICATION_SIDE_TREE), HqlBuilderFrameConstants.OBJECT_TREE,
                HqlBuilderFrameConstants.OBJECT_TREE, true, null, "shift alt F9");
        this.deleteObjectAction = new HqlBuilderAction(this.resultsUnsafe, this, HqlBuilderFrameConstants.DELETE_OBJECT, true,
                HqlBuilderFrameConstants.DELETE_OBJECT, CommonIcons.getIcon(ClientIcons.BIN_EMPTY), HqlBuilderFrameConstants.DELETE_OBJECT,
                HqlBuilderFrameConstants.DELETE_OBJECT, true, null, "shift alt F10");
        this.copyCellAction = new HqlBuilderAction(this.resultsUnsafe, this, HqlBuilderFrameConstants.COPY_SELECTED_CELL, true,
                HqlBuilderFrameConstants.COPY_SELECTED_CELL, CommonIcons.getIcon(ClientIcons.TABLE_LIGHTNING),
                HqlBuilderFrameConstants.COPY_SELECTED_CELL, HqlBuilderFrameConstants.COPY_SELECTED_CELL, true, null, "shift alt F11");
        this.executeScriptOnColumnAction = new HqlBuilderAction(this.resultsUnsafe, this, HqlBuilderFrameConstants.EXECUTE_SCRIPT_ON_COLUMN, true,
                HqlBuilderFrameConstants.EXECUTE_SCRIPT_ON_COLUMN, "org/tools/hqlbuilder/client/images/groovy.png",
                HqlBuilderFrameConstants.EXECUTE_SCRIPT_ON_COLUMN, HqlBuilderFrameConstants.EXECUTE_SCRIPT_ON_COLUMN, true, null, "shift alt F12");
        this.downAction = new HqlBuilderAction(this.parametersUnsafe, this, HqlBuilderFrameConstants.DOWN, true, HqlBuilderFrameConstants.DOWN,
                CommonIcons.getIcon(ClientIcons.BULLET_ARROW_DOWN), HqlBuilderFrameConstants.DOWN, HqlBuilderFrameConstants.DOWN, false, null, null);
        this.removeAction = new HqlBuilderAction(this.parametersUnsafe, this, HqlBuilderFrameConstants.REMOVE, true, HqlBuilderFrameConstants.REMOVE,
                CommonIcons.getIcon(ClientIcons.BIN_EMPTY), HqlBuilderFrameConstants.REMOVE, HqlBuilderFrameConstants.REMOVE, false, null, null);
        this.saveAction = new HqlBuilderAction(this.parametersUnsafe, this, HqlBuilderFrameConstants.SAVE, true, HqlBuilderFrameConstants.SAVE,
                CommonIcons.getIcon(ClientIcons.DISK), HqlBuilderFrameConstants.SAVE, HqlBuilderFrameConstants.SAVE, false, null, null);
        this.setNullAction = new HqlBuilderAction(this.parametersUnsafe, this, HqlBuilderFrameConstants.SET_NULL, true,
                HqlBuilderFrameConstants.SET_NULL, CommonIcons.getIcon(ClientIcons.PICTURE_EMPTY), HqlBuilderFrameConstants.SET_NULL,
                HqlBuilderFrameConstants.SET_NULL, false, null, null);
        this.toTextAction = new HqlBuilderAction(this.parametersUnsafe, this, HqlBuilderFrameConstants.TO_TEXT, true,
                HqlBuilderFrameConstants.TO_TEXT, CommonIcons.getIcon(ClientIcons.TEXTFIELD), HqlBuilderFrameConstants.TO_TEXT,
                HqlBuilderFrameConstants.TO_TEXT, false, null, null);
        this.upAction = new HqlBuilderAction(this.parametersUnsafe, this, HqlBuilderFrameConstants.UP, true, HqlBuilderFrameConstants.UP,
                CommonIcons.getIcon(ClientIcons.BULLET_ARROW_UP), HqlBuilderFrameConstants.UP, HqlBuilderFrameConstants.UP, false, null, null);
        this.addParameterAction = new HqlBuilderAction(this.parametersUnsafe, this, HqlBuilderFrameConstants.ADD_PARAMETER, true,
                HqlBuilderFrameConstants.ADD_PARAMETER, CommonIcons.getIcon(ClientIcons.ADD), HqlBuilderFrameConstants.ADD_PARAMETER,
                HqlBuilderFrameConstants.ADD_PARAMETER, false, null, null);
        ActionListener commitParam = e -> {
            if (HqlBuilderFrame.this.parametersEDT.getSelectedRecord() != null) {
                HqlBuilderFrame.this.saveAction.actionPerformed(e);
            } else {
                HqlBuilderFrame.this.addParameterAction.actionPerformed(e);
            }
        };
        this.parameterName.addActionListener(commitParam);
        this.parameterBuilder.addActionListener(commitParam);
        this.importParametersAction = new HqlBuilderAction(this.parametersUnsafe, this, HqlBuilderFrameConstants.IMPORT_PARAMETERS, true,
                HqlBuilderFrameConstants.IMPORT_PARAMETERS, CommonIcons.getIcon(ClientIcons.COG), HqlBuilderFrameConstants.IMPORT_PARAMETERS,
                HqlBuilderFrameConstants.IMPORT_PARAMETERS, false, null, null);
        this.wizardAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.WIZARD, true, HqlBuilderFrameConstants.WIZARD,
                CommonIcons.getIcon(ClientIcons.WAND), HqlBuilderFrameConstants.WIZARD, HqlBuilderFrameConstants.WIZARD, true, null, "alt F1");
        this.clearAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.CLEAR, true, HqlBuilderFrameConstants.CLEAR,
                CommonIcons.getIcon(ClientIcons.BIN_EMPTY), HqlBuilderFrameConstants.CLEAR, HqlBuilderFrameConstants.CLEAR, true, null, "alt F2");
        this.findParametersAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.FIND_PARAMETERS, true,
                HqlBuilderFrameConstants.FIND_PARAMETERS, CommonIcons.getIcon(ClientIcons.BOOK_NEXT), HqlBuilderFrameConstants.FIND_PARAMETERS,
                HqlBuilderFrameConstants.FIND_PARAMETERS, true, null, "alt F3");
        this.favoritesAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.FAVORITES, true, HqlBuilderFrameConstants.FAVORITES,
                CommonIcons.getIcon(ClientIcons.AWARD_STAR_GOLD_3), HqlBuilderFrameConstants.FAVORITES, HqlBuilderFrameConstants.FAVORITES, true,
                null, "alt F4");
        this.addToFavoritesAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.ADD_TO_FAVORITES, true,
                HqlBuilderFrameConstants.ADD_TO_FAVORITES, CommonIcons.getIcon(ClientIcons.AWARD_STAR_ADD), HqlBuilderFrameConstants.ADD_TO_FAVORITES,
                HqlBuilderFrameConstants.ADD_TO_FAVORITES, true, null, "alt F5");
        // alt F6 not taken
        // alt F7 not taken
        // alt F8 not taken
        this.formatAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.FORMAT, true, HqlBuilderFrameConstants.FORMAT,
                CommonIcons.getIcon(ClientIcons.TEXT_ALIGN_LEFT), HqlBuilderFrameConstants.FORMAT, HqlBuilderFrameConstants.FORMAT, true, null,
                "alt F9");
        this.namedQueryAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.LOAD_NAMED_QUERY, true,
                HqlBuilderFrameConstants.LOAD_NAMED_QUERY, CommonIcons.getIcon(ClientIcons.PACKAGE_GO), HqlBuilderFrameConstants.LOAD_NAMED_QUERY,
                HqlBuilderFrameConstants.LOAD_NAMED_QUERY, true, null, "alt F10");
        this.clipboardExportAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, true,
                HqlBuilderFrameConstants.EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, CommonIcons.getIcon(ClientIcons.CONTROL_FASTFORWARD_BLUE_PNG),
                HqlBuilderFrameConstants.EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, HqlBuilderFrameConstants.EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, true,
                null, "alt F11");
        this.clipboardImportAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, true,
                HqlBuilderFrameConstants.IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, CommonIcons.getIcon(ClientIcons.CONTROL_REWIND_BLUE_PNG),
                HqlBuilderFrameConstants.IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, HqlBuilderFrameConstants.IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD,
                true, null, "alt F12");
        this.helpInsertAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.HELP_INSERT, true, HqlBuilderFrameConstants.HELP_INSERT,
                CommonIcons.getIcon(ClientIcons.ATTACH), HqlBuilderFrameConstants.HELP_INSERT, HqlBuilderFrameConstants.HELP_INSERT, true, null,
                "ctrl shift +");
        this.remarkToggleAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.REMARK_TOGGLE, true,
                HqlBuilderFrameConstants.REMARK_TOGGLE, CommonIcons.getIcon(ClientIcons.TEXT_INDENT), HqlBuilderFrameConstants.REMARK_TOGGLE,
                HqlBuilderFrameConstants.REMARK_TOGGLE, true, null, "ctrl shift SLASH");
        this.startQueryAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.START_QUERY, true, HqlBuilderFrameConstants.START_QUERY,
                CommonIcons.getIcon(ClientIcons.CONTROL_PLAY_BLUE), HqlBuilderFrameConstants.START_QUERY, HqlBuilderFrameConstants.START_QUERY, true,
                null, "ctrl ENTER");
        this.stopQueryAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.STOP_QUERY, true, HqlBuilderFrameConstants.STOP_QUERY,
                CommonIcons.getIcon(ClientIcons.CONTROL_STOP_BLUE), HqlBuilderFrameConstants.STOP_QUERY, HqlBuilderFrameConstants.STOP_QUERY, true,
                null, null);
        stopQueryAction.setEnabled(false);
        this.deleteInvertedSelectionAction = new HqlBuilderAction(this.hql, this, HqlBuilderFrameConstants.DELETE_INVERTED_SELECTION, true,
                HqlBuilderFrameConstants.DELETE_INVERTED_SELECTION, CommonIcons.getIcon(ClientIcons.TABLE_ROW_DELETE),
                HqlBuilderFrameConstants.DELETE_INVERTED_SELECTION, HqlBuilderFrameConstants.DELETE_INVERTED_SELECTION, true, null, "ctrl DELETE");

        // other
        this.editableResultsAction = new HqlBuilderAction(null, this, HqlBuilderFrameConstants.EDITABLE_RESULTS, true,
                HqlBuilderFrameConstants.EDITABLE_RESULTS, null, HqlBuilderFrameConstants.EDITABLE_RESULTS, HqlBuilderFrameConstants.EDITABLE_RESULTS,
                false, null, null, HqlBuilderFrameConstants.PERSISTENT_ID);
        this.editable_results();

        font(nextResultsButton, null);
        font(startResults, null);
        font(backToStartResultsButton, null);
    }

    protected void about() {
        try {
            final JDialog d = new JDialog(this.frame, HqlResourceBundle.getMessage("about"), true);
            d.setUndecorated(true);
            JPanel cp = new JPanel(new MigLayout("insets 0 0 0 0", "10[]", "5[]5[]5[]5"));
            Container contentPane = d.getContentPane();
            contentPane.setLayout(new BorderLayout(0, 0));
            contentPane.add(cp);

            cp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED),
                    BorderFactory.createMatteBorder(1, 1, 0, 0, new Color(150, 150, 150))));

            boolean upToDate = "?".equals(latestVersion) || (this.version.compareTo(latestVersion) >= 0);

            JLabel cp0 = new JLabel(new ImageIcon(HqlBuilderImages.getLogo()));
            cp0.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            cp.add(cp0, "dock north");
            ELabel cp1 = this.font(new ELabel(HqlResourceBundle.getMessage("versioning", this.version, latestVersion,
                    HqlResourceBundle.getMessage(String.valueOf(upToDate), false))), 14);
            cp.add(cp1, "wrap");
            EURILabel cp2 = this
                    .font(new EURILabel(URI.create(HqlBuilderFrameConstants.downloadLatestURI), HqlResourceBundle.getMessage("download latest")), 14);
            cp.add(cp2, "wrap");

            AbstractAction ok = new AbstractAction("Ok") {
                private static final long serialVersionUID = -8251984652275658858L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    d.dispose();
                }
            };

            cp.add(new EButton(new EButtonConfig(ok)), "align center");

            d.setResizable(false);
            d.pack();
            d.setLocationRelativeTo(this.frame);
            d.setVisible(true);
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void add_parameter() {
        this.ingoreParameterListSelectionListener = true;

        this.parametersEDT.clearSelection();
        this.clearParameter();

        this.ingoreParameterListSelectionListener = false;
    }

    protected void add_to_favorites() {
        try {
            if (!this.hql_sql_tabs.getForegroundAt(1).equals(TAB_OK_COLOR)) {
                if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this.frame, HqlResourceBundle.getMessage("no query"),
                        HqlResourceBundle.getMessage("add to favorites"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)) {
                    return;
                }
            }
            String name = JOptionPane.showInputDialog(this.frame.getContentPane(), HqlResourceBundle.getMessage("add to favorites"),
                    HqlResourceBundle.getMessage("favorite name"));
            this.addLast(name);
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    private void addLast(String name) throws IOException {
        List<QueryParameter> qps = new ArrayList<>();
        for (EListRecord<QueryParameter> qp : this.parametersEDT.getRecords()) {
            qps.add(qp.get());
        }
        String hql_text = this.hql.getText();
        QueryFavorite favorite = new QueryFavorite(name, hql_text, qps.toArray(new QueryParameter[qps.size()]));
        if (this.favorites.contains(favorite)) {
            this.favorites.remove(this.favorites.indexOf(favorite));
        }
        this.favorites.add(favorite);
        QueryFavoriteUtils.save(this.hqlService.getProject(), name, favorite);
    }

    private void addTableSelectionListener(final ETable<?> table, final TableSelectionListener listener) {
        final HashMap<String, Integer> map = new HashMap<>();
        map.put(HqlBuilderFrameConstants.ROW, -1);
        map.put(HqlBuilderFrameConstants.COL, -1);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }

            HqlBuilderFrame.outputSelection(HqlBuilderFrameConstants.ROW, map, table, listener);
        });
        table.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }

            HqlBuilderFrame.outputSelection(HqlBuilderFrameConstants.COL, map, table, listener);
        });
    }

    private void afterQuery(long start, ExecutionResult rv, RowProcessor rowProcessor) throws Exception {
        this.sql.setText(this.formatSql(rv));

        this.aliases = rv.getFromAliases();

        List<?> list = rv.getResults() == null ? null : rv.getResults().getValue();
        ETableHeaders<List<Object>> headers = null;

        List<ETableRecord<List<Object>>> records = new ArrayList<>();
        if (list != null) {
            for (Object o : list) {
                ETableRecordCollection<Object> record = null;
                List<Object> recordItems;

                if (o instanceof Object[]) {
                    recordItems = new ArrayList<>(Arrays.asList((Object[]) o));
                } else if (o instanceof Collection<?>) {
                    @SuppressWarnings("unchecked")
                    Collection<Object> o2 = (Collection<Object>) o;
                    recordItems = new ArrayList<>(o2);
                } else {
                    recordItems = new ArrayList<>(Collections.singleton(o));
                }

                record = new ETableRecordCollection<>(recordItems);

                if (rowProcessor != null) {
                    rowProcessor.process(record.getBean());
                    continue;
                }

                if (headers == null) {
                    headers = new ETableHeaders<>();

                    for (int i = 0; i < record.size(); i++) {
                        boolean script = this.scripts.get(i) != null;
                        Class<?> type;
                        String name;
                        try {
                            name = rv.getQueryReturnTypeNames()[i];
                            type = Class.forName(rv.getQueryReturnTypeNames()[i]);
                            name = type.getSimpleName();
                        } catch (Exception ex) {
                            type = Object.class;
                            name = "";
                        }
                        // String br = "<br>";
                        // String html = "<html>";
                        // String _html = "</html>";
                        String br = " ";
                        String html = "";
                        String _html = "";
                        if ((rv.getQueryReturnAliases() == null) || rv.getQueryReturnAliases()[i] == null
                                || String.valueOf(i).equals(rv.getQueryReturnAliases()[i])) {
                            try {
                                headers.add(html + (script ? "*" : "") + name + br + rv.getScalarColumnNames()[i][0] + (script ? "*" : "") + _html,
                                        type);
                            } catch (Exception ex) {
                                HqlBuilderFrame.logger.error("{}", ex);

                                try {
                                    headers.add(html + (script ? "*" : "") + name + br + rv.getSqlAliases()[i] + (script ? "*" : "") + _html, type);
                                } catch (Exception ex2) {
                                    HqlBuilderFrame.logger.error("{}", ex2);
                                    headers.add(html + (script ? "*" : "") + name + br + i + (script ? "*" : "") + _html, type);
                                }
                            }
                        } else {
                            headers.add(html + name + br + rv.getQueryReturnAliases()[i] + _html, type);
                        }
                    }

                    this.resultsEDT.setHeaders(headers);
                }

                records.add(record);
            }
        }
        this.resultsEDT.addRecords(records);
        this.hql_sql_tabs.setForegroundAt(0, TAB_OK_COLOR);
        this.hql_sql_tabs.setForegroundAt(1, TAB_OK_COLOR);

        this.resultsUnsafe.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < this.resultsEDT.getColumnCount(); i++) {
            this.resultsUnsafe.packColumn(i, 2);
        }
        if (this.resizeColumnsAction.isSelected()) {
            this.resultsUnsafe.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
        if (rv.getSize() == 0) {
            this.resultsInfo.setText(HqlResourceBundle.getMessage("No results"));
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String d = df.format(rv.getDuration() / 1000.0);
            this.resultsInfo.setText(HqlResourceBundle.getMessage("results in seconds", String.valueOf(rv.getSize()), d));
        }
        HqlBuilderFrame.logger.info("duration: {} ms", rv.getDuration());
        HqlBuilderFrame.logger.info("overhead-server: {} ms", rv.getOverhead());
        HqlBuilderFrame.logger.info("overhead-client: {} ms", ((System.currentTimeMillis() - start - rv.getDuration())));
        HqlBuilderFrame.logger.debug("end query");
    }

    private void afterQuery(Throwable ex) {
        this.hql_sql_tabs.setForegroundAt(0, HIGHLIGHT_ERROR_DEFAULT_COLOR);
        this.hql_sql_tabs.setForegroundAt(1, HIGHLIGHT_ERROR_DEFAULT_COLOR);

        String sqlString = this.sql.getText();
        if (ex instanceof java.util.concurrent.ExecutionException) {
            ex = ex.getCause();
        }
        String exceptionString = "";
        if (ex instanceof ServiceException) {
            exceptionString = ex.getMessage();
            this.errorString = ex.getMessage();
            ExecutionResult partialResult = ServiceException.class.cast(ex).getPartialResult();
            if ((partialResult != null) && (partialResult.getSql() != null)) {
                sqlString = partialResult.getSql();
            }
        } else if (ex instanceof SqlException) {
            this.errorString = ex.getMessage();
            SqlException sqlException = SqlException.class.cast(ex);
            if (sqlException.getSql() != null) {
                sqlString = sqlException.getSql();
            }
            exceptionString += this.getNewline() + sqlException.getState() + " - " + sqlException.getException();
        } else {
            this.errorString = ex.getMessage();
            exceptionString = ex.toString();
        }
        // hql.setToolTipText(exceptionString.trim());
        sqlString = this.formatSql(sqlString);
        if (StringUtils.isNotBlank(sqlString)) {
            exceptionString = exceptionString + this.getNewline() + "-----------------------------" + this.getNewline() + this.getNewline()
                    + sqlString + this.getNewline() + this.getNewline();
        }
        this.sql.setText(exceptionString.trim());
        this.sql.setCaret(0);
        this.clearResults();
        if (ex instanceof SyntaxException) {
            SyntaxException se = SyntaxException.class.cast(ex);
            this.errorString = se.getMessage();
            int p = this.errorString.indexOf("[");
            if (p != -1) {
                this.errorString = this.errorString.substring(0, p - 1);
            }
            this.hilightSyntaxException(se.getType(), se.getWrong(), se.getLine(), se.getCol());
        }
        HqlBuilderFrame.logger.debug("end query");
    }

    protected void always_on_top() {
        boolean alwaysOnTop = Boolean.TRUE.equals(this.alwaysOnTopAction.getValue());
        this.frame.setAlwaysOnTop(alwaysOnTop);
    }

    private Color applyColor(Color color) {
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        Color gradientColor = this.calcGradient(color);
        ETextComponentFillHighlightPainter painter = ETextComponentFillHighlightPainter.class.cast(this.hql.getHighlightPainter());
        painter.setVerticalGradient(true);
        painter.setColor(color);
        painter.setGradientColor(gradientColor);
        return color;
    }

    private Color calcGradient(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
    }

    private Color chooseColor() {
        Color color = this.getSearchColor();
        color = JColorChooser.showDialog(this.frame, HqlResourceBundle.getMessage("Choose search highlight color"), color);
        return color;
    }

    protected void clear() {
        this.aliases.clear();
        this.resultsInfo.setText("");
        this.parametersEDT.removeAllRecords();
        this.clearParameter();
        this.hql.setText("");
        this.sql.setText("");
        this.scripts.clear();
        this.clearResults();
        this.propertypanel.add(ClientUtils.getPropertyFrame(HqlBuilderFrameConstants.SERIALIZABLE, false), BorderLayout.CENTER);
        this.propertypanel.revalidate();
        this.hql_sql_tabs.setForegroundAt(0, Color.gray);
        this.hql_sql_tabs.setForegroundAt(1, Color.gray);
    }

    // private static void loadModelAtRuntime() {
    // JFrame dummy = new JFrame();
    // dummy.setIconImage(HqlBuilderImages.getIcon()).getImage());
    // JFileChooser fc = new JFileChooser(new File("."));
    // fc.setDialogTitle(HqlResourceBundle.getMessage("open model & hibernate configuration"));
    // fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    // fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
    // @Override
    // public boolean accept(File f) {
    // return f.isDirectory() || f.getName().endsWith(".zip") || f.getName().endsWith(".jar");
    // }
    //
    // @Override
    // public String getDescription() {
    // return HqlResourceBundle.getMessage("single model & hibernate config jar / maven assembly zip");
    // }
    // });
    // int returnVal = fc.showOpenDialog(dummy);
    // if (returnVal == JFileChooser.APPROVE_OPTION) {
    // try {
    // File file = fc.getSelectedFile();
    //
    // URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    // Class<?> sysclass = URLClassLoader.class;
    // Method method = sysclass.getDeclaredMethod("addURL", URL.class);
    // method.setAccessible(true);
    //
    // if (file.getName().endsWith("zip")) {
    // File target = new File(getTmpdir(), file.getName());
    // unzip(target, file);
    // String[] list = target.list(new FilenameFilter() {
    // @Override
    // public boolean accept(File dir, String name) {
    // return name.endsWith("jar");
    // }
    // });
    // if (list != null) {
    // for (String element : list) {
    // method.invoke(sysloader, new Object[] { new File(target, element).toURI().toURL() });
    // }
    // }
    // } else {
    // method.invoke(sysloader, new Object[] { file.toURI().toURL() });
    // }
    // } catch (Exception ex) {
    // logger.error("loadModelAtRuntime()", ex);
    // System.exit(0);
    // return;
    // }
    // } else {
    // System.exit(-1);
    // return;
    // }
    // }
    //
    // private static void unzip(File dir, File zipFile) {
    // FileInputStream fis;
    // ZipInputStream zis = null;
    // try {
    // fis = new FileInputStream(zipFile);
    // zis = new ZipInputStream(new BufferedInputStream(fis));
    // BufferedOutputStream dest = null;
    //
    // ZipEntry entry;
    //
    // while ((entry = zis.getNextEntry()) != null) {
    // int count;
    // byte[] data = new byte[1024 * 8];
    // String extractFileLocation = dir + "/" + entry.getName();
    //
    // // maak alle tussenliggende directories aan
    // if (extractFileLocation.lastIndexOf('/') != -1) {
    // File file = new File(extractFileLocation.substring(0, extractFileLocation.lastIndexOf('/')));
    //
    // if (!file.exists()) {
    // file.mkdirs();
    // }
    // }
    //
    // // write the files to the disk
    // FileOutputStream fos = new FileOutputStream(dir + "/" + entry.getName());
    // dest = new BufferedOutputStream(fos, 1024 * 8);
    //
    // try {
    // while ((count = zis.read(data, 0, 1024 * 8)) != -1) {
    // dest.write(data, 0, count);
    // }
    // } finally {
    // dest.close();
    // }
    // }
    //
    // } catch (Exception ex) {
    // throw new RuntimeException(ex);
    // } finally {
    // if (zis != null) {
    // try {
    // zis.close();
    // } catch (IOException e) {
    // throw new RuntimeException(e);
    // }
    // }
    // }
    // }

    private void clearParameter() {
        this.selectedQueryParameter = new QueryParameter();
        this.parameterBuilder.setText("");
        this.parameterName.setText("");
        this.parameterValue.setText("");
    }

    private void clearResults() {
        this.recordCount = 0;
        this.resultsEDT.setHeaders(new ETableHeaders<List<Object>>());
        this.resultsEDT.removeAllRecords();
    }

    private void compile(final String text) {
        HqlBuilderFrame.logger.info("compiling");
        this.selectedQueryParameter.setValue(null);
        try {
            Object val = GroovyCompiler.eval(text);
            this.selectedQueryParameter.setValueTypeText(val).setValueText(text);
        } catch (Exception ex2) {
            HqlBuilderFrame.logger.error("{}", ex2);
        }
        HqlBuilderFrame.logger.info("compiled: " + this.selectedQueryParameter);
        this.parameterValue.setText(this.selectedQueryParameter.toString());
    }

    protected void copy() {
        try {
            int start = this.hql.getCaret().getDot();
            int end = this.hql.getCaret().getMark();
            String string = this.hql.getDocument().getText(Math.min(start, end), Math.max(start, end) - Math.min(start, end));
            this.clipboard.setContents(new StringSelection(string), this.getClipboardOwner());
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void copy_selected_cell() {
        int selectedRow = this.resultsEDT.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no row selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int selectedColumn = this.resultsEDT.getSelectedColumn();
        if (selectedColumn == -1) {
            JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no col selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object valueAt = this.resultsEDT.getValueAt(selectedRow, selectedColumn);
        if (valueAt == null) {
            return;
        }
        this.clipboard.setContents(new StringSelection(valueAt.toString()), this.getClipboardOwner());
    }

    private void createHqlPopupMenu() {
        JPopupMenu hqlpopupmenu = this.hql.getComponentPopupMenu();
        hqlpopupmenu.addSeparator();
        hqlpopupmenu.add(new JMenuItem(this.startQueryAction));
        hqlpopupmenu.add(new JMenuItem(this.stopQueryAction));
        hqlpopupmenu.add(new JMenuItem(this.wizardAction));
        hqlpopupmenu.add(new JMenuItem(this.clearAction));
        hqlpopupmenu.add(new JMenuItem(this.findParametersAction));
        hqlpopupmenu.add(new JMenuItem(this.favoritesAction));
        hqlpopupmenu.add(new JMenuItem(this.addToFavoritesAction));
        hqlpopupmenu.add(new JMenuItem(this.formatAction));
        hqlpopupmenu.add(new JMenuItem(this.namedQueryAction));
        hqlpopupmenu.add(new JMenuItem(this.clipboardExportAction));
        hqlpopupmenu.add(new JMenuItem(this.clipboardImportAction));
        hqlpopupmenu.add(new JMenuItem(this.helpInsertAction));
        hqlpopupmenu.add(new JMenuItem(this.remarkToggleAction));
        hqlpopupmenu.add(new JMenuItem(this.deleteInvertedSelectionAction));
    }

    private void createResultsPopupMenu() {
        JPopupMenu resultspopupmenu = this.resultsUnsafe.getComponentPopupMenu();
        resultspopupmenu.addSeparator();
        resultspopupmenu.add(new JMenuItem(this.hibernatePropertiesAction));
        resultspopupmenu.add(new JMenuItem(this.objectTreeAction));
        resultspopupmenu.add(new JMenuItem(this.deleteObjectAction));
        resultspopupmenu.add(new JMenuItem(this.copyCellAction));
        resultspopupmenu.add(new JMenuItem(this.executeScriptOnColumnAction));
    }

    private void createSqlPopupMenu() {
        // EComponentPopupMenu.installPopupMenu((ReadableComponent) new TextComponentWritableComponent(sql));
    }

    protected void delete_inverted_selection() {
        Caret caret = this.hql.getCaret();
        int p1 = caret.getDot();
        int p2 = caret.getMark();
        if (p1 == p2) {
            return;
        }
        if (p1 > p2) {
            int tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        this.hql.setText(this.hql.getText().substring(p1, p2));
        this.hql.setCaret(0);
    }

    protected void delete_object() {
        try {
            if (!this.editableResultsAction.isSelected()) {
                JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("results not editable"), "", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int col = this.resultsEDT.getSelectedColumn();
            if (col == -1) {
                JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no column selected"), "", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int row = this.resultsEDT.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no row selected"), "", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Serializable bean = (Serializable) this.resultsEDT.getModel().getValueAt(row, col);
            if (bean == null) {
                JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no object selected"), "", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this.frame, HqlResourceBundle.getMessage("delete_confirmation"),
                    HqlResourceBundle.getMessage("delete confirmation"), JOptionPane.YES_NO_OPTION)) {
                return;
            }
            this.hqlService.delete(bean);
            this.resultsEDT.getModel().setValueAt(null, row, col);
            this.propertypanel.removeAll();
            this.propertypanel.revalidate();
            this.propertypanel.repaint();
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
            JOptionPane.showMessageDialog(this.frame, "" + ex, HqlResourceBundle.getMessage("delete exception"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private ExecutionResult doQuery(String hqlGetText, int start, int maxresults) {
        queryParameters = new QueryParameters(hqlGetText, start, maxresults,
                this.parametersEDT.getRecords().stream().map(EListRecord::get).collect(Collectors.toList()));
        return this.hqlService.execute(queryParameters);
    }

    protected void down() {
        this.parametersEDT.moveSelectedDown();
    }

    protected void editable_results() {
        this.deleteObjectAction.setEnabled(this.editableResultsAction.isSelected());
    }

    protected void execute_script_on_column() {
        int selectedCol = this.resultsEDT.getSelectedColumn();
        if (selectedCol == -1) {
            JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no column selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String script = this.scripts.get(selectedCol);
        script = JOptionPane.showInputDialog(this.frame, HqlResourceBundle.getMessage("groovy_cell"), script != null ? script : "");
        if (script == null) {
            this.scripts.remove(selectedCol);
            return;
        }
        boolean error = false;
        this.scripts.put(selectedCol, script);
        for (int row = 0; row < this.resultsEDT.getRowCount(); row++) {
            Object x = this.resultsEDT.getValueAt(row, selectedCol);
            if (x == null) {
                return;
            }
            try {
                x = GroovyCompiler.eval(script, x);
                this.resultsEDT.setValueAt(x, row, selectedCol);
            } catch (Exception ex) {
                HqlBuilderFrame.logger.error("{}", ex);
                error = true;
            }
        }
        if (error) {
            JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("script_exception"));
        }
        this.resultsEDT.repaint();
    }

    private synchronized void executeQuery(final RowProcessor rowProcessor, final boolean failFast, final boolean resetPointer) {
        HqlBuilderFrame.logger.debug("start query");

        if (resetPointer) {
            startResults.setValue(0);
        }

        final long startTime = System.currentTimeMillis();

        if (!this.startQueryAction.isEnabled()) {
            return;
        }

        final String hqlGetText = this.getHqlText();

        if (StringUtils.isBlank(hqlGetText)) {
            return;
        }

        this.preQuery();

        final int maxresults = rowProcessor == null ? (Integer) this.maximumNumberOfResultsAction.getValue() : Integer.MAX_VALUE;
        final int startNr = startResults.getObjectValue();

        queryExecution = new SwingWorker<ExecutionResult, Void>() {
            @Override
            protected ExecutionResult doInBackground() throws Exception {
                return HqlBuilderFrame.this.doQuery(hqlGetText, startNr, maxresults);
            }

            @Override
            protected void done() {
                boolean retry = false;
                try {
                    ExecutionResult rv = this.get();
                    HqlBuilderFrame.this.afterQuery(startTime, rv, rowProcessor);
                } catch (ExecutionException ex) {
                    Caret caret = HqlBuilderFrame.this.hql.getCaret();
                    if (!failFast && (caret.getDot() != caret.getMark())) {
                        retry = true;
                    } else {
                        Throwable cause = ex.getCause();
                        HqlBuilderFrame.this.afterQuery(cause);
                    }
                } catch (java.util.concurrent.CancellationException ex) {
                    HqlBuilderFrame.this.postQuery();
                } catch (Exception ex) {
                    HqlBuilderFrame.this.afterQuery(ex);
                } finally {
                    HqlBuilderFrame.this.postQuery();
                }
                if (retry) {
                    Caret caret = HqlBuilderFrame.this.hql.getCaret();
                    caret.moveDot(caret.getMark());
                    HqlBuilderFrame.this.executeQuery(rowProcessor, true, true);
                }
            }
        };

        queryExecution.execute();
    }

    protected void exit() {
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this.frame, HqlResourceBundle.getMessage("exit_confirmation"), "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
            HqlBuilderFrame.logger.info("exiting with 0");
            System.exit(0);
        }
    }

    protected void export__copy_hql_as_java_to_clipboard() {
        try {
            StringBuilder sb = new StringBuilder("String hql = \"\";" + this.getNewline());

            for (String line : this.hql.getText().replace("\r\n", this.getNewline()).split(this.getNewline())) {
                if (!line.startsWith(HqlBuilderFrameConstants.REMARKTAG) && !line.startsWith("//")) {
                    sb.append("hql +=\" ").append(line).append("\";");
                } else {
                    sb.append("// ").append(line);
                }

                sb.append(this.getNewline());
            }

            this.clipboard.setContents(new StringSelection(sb.toString()), this.getClipboardOwner());
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void export_dataset_to_csv_file() {
        OutputStream fout = null;
        try {
            final JFileChooser fc = new JFileChooser();
            if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this.frame)) {
                File dir_shortname = fc.getSelectedFile();
                String absolutePath = dir_shortname.getAbsolutePath();
                if (!absolutePath.endsWith(HqlBuilderFrameConstants.CSV)) {
                    absolutePath += HqlBuilderFrameConstants.CSV;
                }
                fout = new FileOutputStream(new File(absolutePath));
                final BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fout));
                this.recordCount = 0;
                this.query(lijn -> {
                    try {
                        for (Object object : lijn) {
                            if (object instanceof Object[]) {
                                Object[] array = (Object[]) object;
                                for (int i = 0; i < array.length; i++) {
                                    br.write("\"");
                                    br.write(array[i].toString().replace(",", ";").replace("\"", "'"));
                                    br.write("\"");

                                    if (i < (array.length - 1)) {
                                        br.write(",");
                                    }
                                }
                            } else {
                                br.write("\"");
                                br.write(object.toString().replace(",", ";").replace("\"", "'"));
                                br.write("\"");
                            }
                        }
                        br.write(HqlBuilderFrame.this.getNewline());
                        HqlBuilderFrame.this.recordCount++;
                        if ((HqlBuilderFrame.this.recordCount != 0) && ((HqlBuilderFrame.this.recordCount % 100) == 0)) {
                            HqlBuilderFrame.logger.info(HqlBuilderFrame.this.recordCount + " records");
                        }
                    } catch (Exception ex) {
                        HqlBuilderFrame.logger.error("{}", ex);
                    }
                });
                HqlBuilderFrame.logger.info("{} records", this.recordCount);
                br.flush();
                br.close();
            }
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception ex2) {
                    //
                }
            }
        }
    }

    protected void export_table_data_to_clipboard() {
        try {
            StringBuilder sb = new StringBuilder();

            for (String name : this.resultsEDT.getHeadernames()) {
                sb.append(name).append("\t");
            }

            sb.append(this.getNewline());

            for (ETableRecord<List<Object>> record : this.resultsEDT.getRecords()) {
                for (int i = 0; i < record.size(); i++) {
                    sb.append(record.get(i)).append("\t");
                }

                sb.append(this.getNewline());
            }

            this.clipboard.setContents(new StringSelection(sb.toString()), this.getClipboardOwner());
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void favorites() {
        try {
            FavoritesDialog dialog = new FavoritesDialog(this.frame, this.favorites);
            dialog.setTitle(HqlResourceBundle.getMessage("favorites.long"));
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            QueryFavorite selection = dialog.getSelection();

            if (selection != null) {
                this.importFromFavorites(selection);
            }
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    public void find_parameters() {
        this.parametersEDT.removeAllRecords();
        for (QueryParameter p : this.hqlService.findParameters(this.getHqlText())) {
            this.parametersEDT.addRecord(new EListRecord<>(p));
        }
    }

    protected void font() {
        Font font = EFontChooser.showDialog((JComponent) this.frame.getContentPane(), this.getFont().getFamily());
        if (font == null) {
            return;
        }
        this.fontAction.setValue(font);
    }

    public <T extends JComponent> T font(T comp, Integer size) {
        return this.font(comp, size, null);
    }

    public <T extends JComponent> T font(T comp, Integer size, Integer style) {
        Font f = this.getFont();
        if ((size != null) && (f.getSize() != size)) {
            f = f.deriveFont(f.getSize() + (float) size);
        }
        if (style != null) {
            f = f.deriveFont(style);
        }
        comp.setFont(f);
        return comp;
    }

    protected void force_exit() {
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this.frame, HqlResourceBundle.getMessage("force_exit_confirmation"), "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
            HqlBuilderFrame.logger.info("exiting with -1");
            System.exit(-1);
        }
    }

    protected void format() {
        try {
            this.hql.setText(this.format(this.hql.getText()));
            this.hql.setCaret(0);
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    private String format(String text) {
        return this.hqlService.makeMultiline(this.hqlService.removeBlanks(text.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ')));
    }

    protected void format_sql() {
        this.formatSqlOptionsMenu.setEnabled(this.formatSqlAction.isSelected());
    }

    private String formatSql(ExecutionResult rv) {
        String sqlString = rv.getSql();
        String[] queryReturnAliases = rv.getQueryReturnAliases();
        String[][] scalarColumnNames = rv.getScalarColumnNames();
        boolean doFormat = this.formatLinesAction.isSelected();
        boolean doRemoveJoins = this.removeJoinsAction.isSelected();
        boolean doReplaceProperties = this.replacePropertiesAction.isSelected();
        return this.formatSql(sqlString, queryReturnAliases, scalarColumnNames, doFormat, doRemoveJoins, doReplaceProperties);
    }

    private String formatSql(String sqlString) {
        return this.formatSql(sqlString, null, null, false, false, false);
    }

    private String formatSql(String sqlString, String[] queryReturnAliases, String[][] scalarColumnNames, boolean doFormat, boolean doRemoveJoins,
            boolean doReplaceProperties) {
        if (StringUtils.isBlank(sqlString)) {
            return sqlString;
        }
        if (this.formatSqlAction.isSelected()) {
            sqlString = this.hqlService.cleanupSql(sqlString, queryReturnAliases, scalarColumnNames, doReplaceProperties, doFormat, doRemoveJoins);
        }
        return sqlString;
    }

    protected <T> T get(Object o, String path, Class<T> t) {
        return t.cast(new ObjectWrapper(o).get(path));
    }

    private ClipboardOwner getClipboardOwner() {
        return (cb, contents) -> {
            //
        };
    }

    public Font getFont() {
        return (Font) this.fontAction.getValue();
    }

    private ProgressGlassPane getGlass(JFrame parent) {
        if (this.glass == null) {
            this.glass = new ProgressGlassPane(parent);

            try {
                this.glass.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
            } catch (Exception ex) {
                //
            }
        }
        return this.glass;
    }

    private HibernateWebResolver getHibernateWebResolver() {
        if (this.hibernateWebResolver == null) {
            this.hibernateWebResolver = this.hqlService.getHibernateWebResolver();
        }
        return this.hibernateWebResolver;
    }

    private Color getBracesHighlightColor() {
        return this.highlightBracesColorAction.getValue() == null ? HIGHLIGHT_BRACES_DEFAULT_COLOR
                : (Color) this.highlightBracesColorAction.getValue();
    }

    private Color getSyntaxHighlightColor() {
        return this.highlightSyntaxColorAction.getValue() == null ? HIGHLIGHT_SYNTAXT_DEFAULT_COLOR
                : (Color) this.highlightSyntaxColorAction.getValue();
    }

    private String getHqlText() {
        String hqlstring = this.hql.getText();
        Caret caret = this.hql.getCaret();
        int begin = Math.min(caret.getDot(), caret.getMark());
        int end = Math.max(caret.getDot(), caret.getMark());
        if (begin == end) {
            begin = hqlstring.substring(0, begin).lastIndexOf(";") + 1;
            end = hqlstring.indexOf(";", end);
            if (end == -1) {
                end = hqlstring.length();
            }
        }
        hqlstring = hqlstring.substring(begin, end).replace(';', ' ');

        final Point viewPosition = this.hqlsp.getViewport().getViewPosition();
        if (this.addSelectExecutedHql.isSelected()) {
            this.hql.setSelectionStart(begin);
            this.hql.setSelectionEnd(end);
        }
        SwingUtilities.invokeLater(() -> HqlBuilderFrame.this.hqlsp.getViewport().setViewPosition(viewPosition));

        String lines[] = hqlstring.split(HqlBuilderFrameConstants.LINESEPERATOR);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            int dash = line.indexOf(HqlBuilderFrameConstants.REMARKTAG);
            if (dash != -1) {
                sb.append(line.substring(0, dash));
                for (int i = dash; i < line.length(); i++) {
                    sb.append(" ");
                }
            } else {
                sb.append(line);
            }
            sb.append("\n");
        }
        hqlstring = sb.toString();
        return hqlstring;
    }

    private JPopupMenu getInsertHelperClasses() {
        if (this.insertHelper == null) {
            this.insertHelper = new JPopupMenu();

            EList<String> list = new EList<>(new EListConfig());
            this.insertHelperList = this.font(list, null);

            JScrollPane scroll = new JScrollPane(this.insertHelperList);
            this.insertHelper.add(scroll);

            TreeSet<String> options = new TreeSet<>();

            for (HibernateWebResolver.ClassNode node : this.getHibernateWebResolver().getClasses()) {
                String clazz = node.getId();
                String option = clazz.substring(clazz.lastIndexOf('.') + 1) + " (" + clazz.substring(0, clazz.lastIndexOf('.')) + ")";
                options.add(option);
            }

            for (String option : options) {
                list.addRecord(new EListRecord<>(option));
            }

            Dimension size = new Dimension(500, 200);
            this.insertHelper.setPreferredSize(size);
            this.insertHelper.setSize(size);
            this.insertHelper.setMinimumSize(size);

            this.insertHelperList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if (EventHelper.isMouse1xMB1(e)) {
                            HqlBuilderFrame.this.insertHelp();
                        }
                    } catch (Exception ex) {
                        HqlBuilderFrame.logger.error("{}", ex);
                    }
                }
            });
        }

        return this.insertHelper;
    }

    private JPopupMenu getInsertHelperProperties() {
        if (this.insertClassHelper == null) {
            this.insertClassHelper = new JPopupMenu();

            this.insertPropertyHelper = new EList<>(new EListConfig());

            JScrollPane scroll = new JScrollPane(this.insertPropertyHelper);
            this.insertClassHelper.add(scroll);

            this.insertClassHelper.setPreferredSize(new Dimension(300, 150));
            this.insertClassHelper.setSize(new Dimension(300, 150));
            this.insertClassHelper.setMinimumSize(new Dimension(300, 150));

            this.insertPropertyHelper.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if (EventHelper.isMouse1xMB1(e)) {
                            HqlBuilderFrame.this.insertPropertyHelp();
                        }
                    } catch (Exception ex) {
                        HqlBuilderFrame.logger.error("{}", ex);
                    }
                }
            });
        }

        return this.insertClassHelper;
    }

    protected String getNewline() {
        return this.hqlService.getNewline();
    }

    private Set<String> getReservedKeywords() {
        if (this.reservedKeywords == null) {
            this.reservedKeywords = this.hqlService.getReservedKeywords();
        }
        return this.reservedKeywords;
    }

    private Color getSearchColor() {
        return this.searchColorAction.getValue() == null ? HIGHLIGHT_SEARCH_DEFAULT_COLOR : (Color) this.searchColorAction.getValue();
    }

    public static String fetchVersion() {
        try {
            Properties p = new Properties();
            p.load(HqlBuilderFrame.class.getClassLoader()
                    .getResourceAsStream("META-INF/maven/org.tools.hql-builder/hql-builder-client/pom.properties"));
            return p.getProperty("version").replace("-SNAPSHOT", "β").toString();
        } catch (Exception ex) {
            return HqlResourceBundle.getMessage("latest"); // actually unknown
            // try {
            // this.version = org.w3c.dom.Node.class.cast(
            // CommonUtils.getFromXml(new FileInputStream("pom.xml"), "project", "/default:project/default:version/text()")).getNodeValue();
            // } catch (Exception ex2) {
            // try {
            // this.version = org.w3c.dom.Node.class.cast(
            // CommonUtils.getFromXml(new FileInputStream("pom.xml"), "project",
            // "/default:project/default:parent/default:version/text()")).getNodeValue();
            // } catch (Exception ex3) {
            // this.version = HqlResourceBundle.getMessage("latest");
            // }
            // }
        }
    }

    public static String fetchLatestVersion() {
        String latest = "?";
        try {
            String u = HqlBuilderFrame.getText(HqlBuilderFrameConstants.PROJECT_META);
            org.w3c.dom.Text o = (org.w3c.dom.Text) CommonUtils.getFromXml(new ByteArrayInputStream(u.getBytes()), "metadata",
                    "/metadata/versioning/release/text()");
            latest = o.getData();
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
        return latest;
    }

    protected void help() {
        try {
            Desktop.getDesktop().browse(URI.create(ClientUtils.getHelpUrl()));
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void help_insert() {
        try {
            this.helpInsert();
            // scheduleQuery(null, false);
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    private void helpInsert() {
        String before;
        try {
            before = this.getHqlText().substring(0, this.hql.getCaret().getDot());
        } catch (StringIndexOutOfBoundsException ex) {
            before = this.getHqlText().substring(0, this.hql.getCaret().getDot() - 1) + " ";
        }
        if (before.toLowerCase().endsWith("from ")) {
            this.helpInsertClass();
        } else if (before.endsWith(".")) {
            this.helpInsertProperty(before);
        }
    }

    private void helpInsertClass() {
        Point magicCaretPosition = this.hql.getCaret().getMagicCaretPosition();
        this.getInsertHelperClasses().show(this.hql, magicCaretPosition == null ? 10 : (int) magicCaretPosition.getX(),
                magicCaretPosition == null ? 10 : (int) magicCaretPosition.getY());
        this.insertHelperList.grabFocus();
    }

    private void helpInsertProperty(String before) {
        int pos = Math.max(0, Math.max(before.lastIndexOf(this.getNewline()), before.lastIndexOf(' ')));
        before = before.substring(pos + 1, before.length() - 1);
        String[] parts = before.split("\\Q.\\E");
        String key = this.aliases.get(parts[0]);

        List<String> propertyNames = this.hqlService.getPropertyNames(key, parts);

        JPopupMenu insertHelper2local = this.getInsertHelperProperties();

        this.insertPropertyHelper.removeAllRecords();

        for (String prop : new TreeSet<>(propertyNames)) {
            if (!prop.startsWith("_")) {
                this.insertPropertyHelper.addRecord(new EListRecord<>(prop));
            }
        }

        Point magicCaretPosition = this.hql.getCaret().getMagicCaretPosition();
        insertHelper2local.show(this.hql, magicCaretPosition == null ? 10 : (int) magicCaretPosition.getX(),
                magicCaretPosition == null ? 10 : (int) magicCaretPosition.getY());
        this.insertPropertyHelper.grabFocus();
    }

    protected void hibernate_documentation() {
        try {
            Desktop.getDesktop().browse(URI.create(this.hqlService.getHibernateHelpURL()));
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void java_properties() {
        ETable<String[]> propertiesTable = new ETable<>(new ETableConfig());
        ETable<String[]> threadSafePropertiesTable = propertiesTable.getSimpleThreadSafeInterface();
        ETableHeaders<String[]> headers = new ETableHeaders<>();
        headers.add("key", String.class, true);
        headers.add("value", String.class, true);
        threadSafePropertiesTable.setHeaders(headers);
        System.getProperties().keySet().stream().map(String::valueOf).sorted().forEach(
                _key -> threadSafePropertiesTable.addRecord(new ETableRecordArray<>(new String[] { _key, System.getProperty(_key) })));
        JFrame frame = new JFrame();
        frame.setTitle(HqlResourceBundle.getMessage(JAVA_PROPERTIES));
        JScrollPane jsp = new JScrollPane(propertiesTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        Container cp = frame.getContentPane();
        cp.add(jsp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    protected void hibernate_properties() {
        int selectedRow = this.resultsEDT.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no row selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int selectedColumn = this.resultsEDT.getSelectedColumn();
        if (selectedColumn == -1) {
            JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no col selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object value = this.resultsEDT.getValueAt(selectedRow, selectedColumn);
        if (value == null) {
            return;
        }
        Class<?> clas = value.getClass();
        String classname = clas.getName();
        List<String> propertyNames = this.hqlService.getProperties(classname);
        StringBuilder sb = new StringBuilder();
        propertyNames.remove("id");
        propertyNames.remove("version");
        for (int i = 0; i < (propertyNames.size() - 1); i++) {
            sb.append(propertyNames.get(i)).append(",").append(this.getNewline());
        }
        if (propertyNames.size() > 0) {
            sb.append(propertyNames.get(propertyNames.size() - 1));
        }
        this.clipboard.setContents(new StringSelection(sb.toString()), this.getClipboardOwner());
    }

    protected void highlight_syntax_color() {
        Color color = this.getSyntaxHighlightColor();
        color = JColorChooser.showDialog(null, HqlResourceBundle.getMessage("Choose HQL syntax highlight color"), color);
        logger.info("highlight_syntax_color: {}", color);
        this.highlightSyntaxColorAction.setValue(color);
        this.hql.removeHighlights(this.syntaxHighlight);
        this.syntaxHighlight.setColor(color);
    }

    protected void highlight_braces_color() {
        Color color = this.getBracesHighlightColor();
        color = JColorChooser.showDialog(null, HqlResourceBundle.getMessage("Choose HQL braces highlight color"), color);
        logger.info("highlight_braces_color: {}", color);
        this.highlightBracesColorAction.setValue(color);
        this.hql.removeHighlights(this.bracesHighlight);
        this.bracesHighlight.setColor(color);
    }

    private void hilightBraces() {
        this.hql.removeHighlights(this.bracesHighlight);

        if (!this.highlightSyntaxAction.isSelected()) {
            return;
        }

        try {
            int p1 = this.hql.getCaret().getDot();
            int p2 = this.hql.getCaret().getMark();
            int min = Math.min(p1, p2);
            int max = Math.min(p1, p2);
            int len = max - min;
            if (len > 1) {
                return;
            }

            int caret = min;
            int match = HqlBuilderFrame.find(hql.getText(), caret);
            if (match != -1) {
                try {
                    this.hql.addHighlight(caret, caret + 1, this.bracesHighlight);
                    this.hql.addHighlight(match, match + 1, this.bracesHighlight);
                } catch (BadLocationException ex) {
                    HqlBuilderFrame.logger.error("{}", ex);
                }
            }
        } catch (Exception ex) {
            //
        }
    }

    private void hilightSyntax() {
        this.hql.removeHighlights(this.syntaxHighlight);

        if (!this.highlightSyntaxAction.isSelected()) {
            return;
        }

        LinkedList<int[]> pos = new LinkedList<>();

        for (String kw : this.getReservedKeywords()) {
            this.syntaxHi(pos, kw);
        }

        Collections.sort(pos, (o1, o2) -> new CompareToBuilder().append(o1[0], o2[0]).toComparison());

        String hqltext = this.hql.getText().toLowerCase();

        Iterator<int[]> iterator = new ArrayList<>(pos).iterator();

        if (iterator.hasNext()) {
            int[] pair1 = iterator.next();
            while (iterator.hasNext()) {
                int[] pair2 = iterator.next();
                if (((pair1[1] + 1) == pair2[0]) && ((hqltext.charAt(pair1[1]) == ' ') || (hqltext.charAt(pair1[1]) == '\t'))) {
                    pair1[1] = pair2[1];
                    pos.remove(pair2);
                } else {
                    pair1 = pair2;
                }
            }
            for (int[] p : pos) {
                try {
                    this.hql.addHighlight(p[0], p[1], this.syntaxHighlight);
                } catch (Throwable ex) {
                    HqlBuilderFrame.logger.error("{}", ex);
                }
            }
        }
    }

    private void hilightSyntaxException(SyntaxExceptionType syntaxExceptionType, String wrong, int line, int col) {
        this.hql.removeHighlights(this.syntaxErrorsHighlight);
        String hqltext = this.hql.getText();
        ArrayList<Integer> poss = new ArrayList<>();
        switch (syntaxExceptionType) {
            case illegal_attempt_to_dereference_collection: {
                wrong = wrong.substring(Math.max(0, 1 + Math.max(wrong.lastIndexOf('.'), wrong.lastIndexOf('{')))).replace('#', '.');
                int pos = hqltext.indexOf(wrong);
                while (pos != -1) {
                    poss.add(pos);
                    pos = hqltext.indexOf(wrong, pos + 1);
                }
                break;
            }
            case unable_to_resolve_path: {
                int pos = hqltext.indexOf(wrong);
                while (pos != -1) {
                    poss.add(pos);
                    pos = hqltext.indexOf(wrong, pos + 1);
                }
                break;
            }
            case could_not_resolve_property: {
                wrong = "." + wrong.split("#")[1];
                int pos = hqltext.indexOf(wrong);
                while (pos != -1) {
                    boolean accept = false;
                    try {
                        char nextChar = hqltext.charAt(pos + wrong.length());
                        if (!((('a' <= nextChar) && (nextChar <= 'z')) || (('A' <= nextChar) && (nextChar <= 'Z')))) {
                            accept = true;
                        }
                    } catch (StringIndexOutOfBoundsException ex) {
                        accept = true;
                    }
                    if (accept) {
                        poss.add(pos);
                    }
                    pos = hqltext.indexOf(wrong, pos + 1);
                }
                break;
            }
            case invalid_path: {
                int pos = hqltext.indexOf(wrong);
                while (pos != -1) {
                    poss.add(pos);
                    pos = hqltext.indexOf(wrong, pos + 1);
                }
                break;
            }
            case not_mapped: {
                int pos = hqltext.indexOf(wrong);
                while (pos != -1) {
                    poss.add(pos);
                    pos = hqltext.indexOf(wrong, pos + 1);
                }
                break;
            }
            case unexpected_token: {
                Caret caret = this.hql.getCaret();
                int mark = Math.min(caret.getDot(), caret.getMark());
                int lnskip = hqltext.substring(0, mark).split(HqlBuilderFrameConstants.LINESEPERATOR).length - 1;
                String[] lines = hqltext.split(HqlBuilderFrameConstants.LINESEPERATOR);
                int pos = 0;
                for (int i = 0; i < ((lnskip + line) - 1); i++) {
                    pos += lines[i].length() + 1;
                }
                pos += col - 1;
                if (hqltext.length() < pos) {
                    pos = hqltext.length();
                }
                poss.add(pos);
                break;
            }
        }
        for (int i = 0; i < poss.size(); i++) {
            int pos = poss.get(i);
            try {
                if (i == 0) {
                    this.hql.setCaret(pos);
                }
                List<Highlight> before = Arrays.asList(this.hql.getHighlighter().getHighlights());
                this.hql.addHighlight(pos, pos + wrong.length(), this.syntaxErrorsHighlight);
                List<Highlight> after = new ArrayList<>(Arrays.asList(this.hql.getHighlighter().getHighlights()));
                after.removeAll(before);
                this.errorLocs.addAll(after);
            } catch (Exception ex) {
                HqlBuilderFrame.logger.error("{}", ex);
            }
        }
    }

    protected void hql_documentation() {
        try {
            Desktop.getDesktop().browse(URI.create(this.hqlService.getHqlHelpURL()));
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void import__paste_hql_as_java_from_clipboard() {
        try {
            Transferable contents = this.clipboard.getContents(this);
            Object transferData = contents.getTransferData(DataFlavor.stringFlavor);
            String string = (String) transferData;
            String replaceAll = string.replaceAll("//", "")
                    .replaceAll("\";", "")
                    .replaceAll("hql[ ]{0,}\\+[ ]{0,}=[ ]{0,}\"", "")
                    .replaceAll("\\Q\r\n\\E", this.getNewline());
            StringBuilder sb = new StringBuilder();
            for (String line : replaceAll.split(this.getNewline())) {
                sb.append(line.trim()).append(this.getNewline());
            }
            this.hql.setText(sb.toString());
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void import_parameters() {
        String m = this.importParametersFromTextF.getText();
        List<QueryParameter> parameters = HqlBuilderFrame.convertParameterString(m);
        this.importParametersFromTextF.setText("");
        for (QueryParameter qp : parameters) {
            boolean exists = false;
            if (qp.getName() == null) {
                // just add at bottom in order
            } else {
                for (EListRecord<QueryParameter> rec : this.parametersEDT.getRecords()) {
                    if (rec.get().getName().equals(qp.getName())) {
                        rec.get().setName(qp.getName());
                        rec.get().setValueText(qp.getValueText());
                        rec.get().setIndex(qp.getIndex());
                        rec.get().setValue(qp.getValue());
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                this.parametersEDT.addRecord(new EListRecord<>(qp));
            }
        }
    }

    private void importFromFavorites(QueryFavorite selection) {
        this.clear();
        this.importFromFavoritesNoQ(selection);
    }

    private void importFromFavoritesNoQ(QueryFavorite selection) {
        this.hql.setText(selection.getFull());

        if (selection.getParameters() != null) {
            for (QueryParameter p : selection.getParameters()) {
                String valueText = p.getValueText();
                if (StringUtils.isNotBlank(valueText)) {
                    try {
                        Object value = GroovyCompiler.eval(valueText);
                        p.setValue(value);
                    } catch (Exception ex) {
                        p.setValue(valueText);
                        HqlBuilderFrame.logger.error("{}", ex);
                    }
                }
                this.parametersEDT.addRecord(new EListRecord<>(p));
            }
        }
    }

    private void insertHelp() throws Exception {
        String clazz = this.insertHelperList.getSelectedRecord().get();
        clazz = clazz.substring(0, clazz.indexOf(" ")).trim();
        this.hql.getDocument().insertString(this.hql.getCaretPosition(), clazz, null);
        this.insertHelper.setVisible(false);
    }

    private void insertPropertyHelp() throws Exception {
        String prop = this.insertPropertyHelper.getSelectedRecord().get();
        this.hql.getDocument().insertString(this.hql.getCaretPosition(), prop, null);
        this.insertClassHelper.setVisible(false);
    }

    private boolean isSeperator(char c) {
        return Character.isWhitespace(c) || (c == '\'') || (c == '(') || (c == ')') || (c == ',') || (c == '-') || (c == '/');
    }

    private void layout(Dimension size) {
        if (this.frame.getSize().height == 0) {
            size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            Insets insets = java.awt.Toolkit.getDefaultToolkit()
                    .getScreenInsets(java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
            size = new Dimension((int) (size.getWidth() - insets.left - insets.right), (int) (size.getHeight() - insets.top - insets.bottom));
        } else {
            size = this.normalContentPane.getSize();
        }

        int w = (int) size.getWidth();
        int h = (int) size.getHeight();
        // if (menu_largeLayout.getState()) {
        if (this.switchLayoutAction.isSelected()) {
            // hql_sql_tabs .... parameterspanel .... propertypanel
            // resultPanel
            this.split0.setDividerLocation((int) ((h * 1.0) / 3.0));
            this.split1.setDividerLocation((int) ((w * 1.0) / 3.0));
            this.split2.setDividerLocation((int) ((w * 1.0) / 3.0));
        } else {
            // hql_sql_tabs .... parameterspanel
            // resultPanel ..... propertypanel
            this.split0.setDividerLocation((int) ((h * 1.0) / 3.0));
            this.split1.setDividerLocation((int) ((w * 2.0) / 3.0));
            this.split2.setDividerLocation((int) ((w * 2.0) / 3.0));
        }
    }

    protected void load_named_query() {
        final Map<String, String> namedQueries = this.hqlService.getNamedQueries();
        final EList<String> list = new EList<>(new EListConfig());
        list.addRecords(EList.convert(new TreeSet<>(namedQueries.keySet())));
        JPanel container = new JPanel(new BorderLayout());
        container.add(new JScrollPane(list), BorderLayout.CENTER);
        container.add(list.getSearchComponent(), BorderLayout.NORTH);
        if (ResultType.OK == CustomizableOptionPane.showCustomDialog(this.hql, container,
                HqlResourceBundle.getMessage(HqlBuilderFrameConstants.LOAD_NAMED_QUERY), MessageType.QUESTION, OptionType.OK_CANCEL, null,
                new ListOptionPaneCustomizer<>(list))) {
            if (list.getSelectedRecord() != null) {
                this.clear();
                this.hql.setText(namedQueries.get(list.getSelectedRecord().get()));
            }
        }
    }

    private void loadFavorites() {
        QueryFavorite last = QueryFavoriteUtils.load(this.hqlService.getProject(), this.favorites);
        if (last != null) {
            this.importFromFavoritesNoQ(last);
        }
    }

    protected void Lucene_query_syntax() {
        try {
            Desktop.getDesktop().browse(URI.create(this.hqlService.getLuceneHelpURL()));
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void maximum_number_of_results() {
        Object newValue = JOptionPane.showInputDialog(this.frame, HqlResourceBundle.getMessage(HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_RESULTS),
                String.valueOf(this.maximumNumberOfResultsAction.getValue()));
        if (newValue != null) {
            int max = Integer.parseInt(String.valueOf(newValue));
            this.maximumNumberOfResultsAction.setValue(max);
            this.setMaxResults(max);
        }
    }

    protected void show_toolbars() {
        boolean b = !Boolean.FALSE.equals(this.showToolbarsAction.isSelected());
        hqltools.setVisible(b);
        resultstools.setVisible(b);
    }

    protected void maximum_number_of_search_results() {
        Object newValue = JOptionPane.showInputDialog(this.frame,
                HqlResourceBundle.getMessage(HqlBuilderFrameConstants.MAXIMUM_NUMBER_OF_SEARCH_RESULTS),
                String.valueOf(this.maximumNumberOfSearchResultsAction.getValue()));
        if (newValue != null) {
            int max = Integer.parseInt(String.valueOf(newValue));
            this.maximumNumberOfSearchResultsAction.setValue(max);
        }
    }

    protected void object_tree() {
        final int col = this.resultsEDT.getSelectedColumn();
        if (col == -1) {
            JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no column selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final int row = this.resultsEDT.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("no row selected"), "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.progressbarStart("fetching data");
        Object bean = this.resultsEDT.getModel().getValueAt(row, col);
        new ObjectTree(this, this.hqlService, bean, this.editableResultsAction.isSelected()).setIconImage(this.frame.getIconImage());
        this.progressbarStop();
    }

    private void parameterSelected() {
        if (this.ingoreParameterListSelectionListener) {
            return;
        }

        EListRecord<QueryParameter> selected = this.parametersEDT.getSelectedRecord();

        if (selected == null) {
            this.clearParameter();
            return;
        }

        this.selectedQueryParameter = this.parametersEDT.getSelectedRecord().get();
        this.parameterValue.setText(this.selectedQueryParameter.toString());
        this.parameterName.setText((this.selectedQueryParameter.getName() == null) ? "" : this.selectedQueryParameter.getName());
        this.parameterBuilder.setText(this.selectedQueryParameter.getValueText());
        this.selectedQueryParameter.getIndex();
    }

    protected void paste() {
        try {
            Transferable contents = this.clipboard.getContents(this);
            Object transferData = contents.getTransferData(DataFlavor.stringFlavor);
            String string = (String) transferData;
            int start = this.hql.getCaret().getDot();
            int end = this.hql.getCaret().getMark();
            string = this.hql.getDocument().getText(0, Math.min(start, end)) + string
                    + this.hql.getDocument().getText(Math.max(start, end), this.hql.getDocument().getLength() - Math.max(start, end));
            this.hql.setText(string);
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    private void postQuery() {
        this.startQueryAction.setEnabled(true);
        this.stopQueryAction.setEnabled(false);
        this.progressbarStop();
    }

    private void preQuery() {
        this.startQueryAction.setEnabled(false);
        this.stopQueryAction.setEnabled(true);
        this.progressbarStart(HqlResourceBundle.getMessage("quering"));
        this.errorLocs.clear();
        this.errorString = null;
        this.sql.setText("");
        this.resultsInfo.setText("");
        this.clearResults();
        this.hql_sql_tabs.setForegroundAt(0, Color.gray);
        this.hql_sql_tabs.setForegroundAt(1, Color.gray);
        try {
            this.addLast(HqlBuilderFrameConstants.LAST);
        } catch (IOException ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
        this.hql.removeHighlights(this.syntaxErrorsHighlight);
        this.hilightSyntax();
    }

    private void progressbarStart(final String text) {
        this.glass.setMessage(text);
        this.setGlassVisible(true);
    }

    private void progressbarStop() {
        this.glass.setMessage(null);
        this.setGlassVisible(false);
    }

    private void query(RowProcessor rowProcessor) {
        this.progressbarStart(HqlResourceBundle.getMessage("quering"));
        this.executeQuery(rowProcessor, false, true);
        this.progressbarStop();
        JOptionPane.showMessageDialog(this.frame, HqlResourceBundle.getMessage("done"));
    }

    private void reloadColor() {
        this.applyColor(this.getSearchColor());
    }

    protected void remark_toggle() {
        int selectionStart = this.hql.getSelectionStart();
        int selectionEnd = this.hql.getSelectionEnd();
        String hqltext = this.hql.getText();

        hqltext = this.remarkToggle(hqltext, selectionStart, selectionEnd);
        this.hql.setText(hqltext);
        this.hql.setCaret(Math.min(selectionStart, selectionEnd));
    }

    private String remarkToggle(String hqltext, int selectionStart, int selectionEnd) {
        if (selectionStart != 0) {
            int lastEnter = hqltext.lastIndexOf(this.getNewline(), selectionStart);

            if (lastEnter == -1) {
                selectionStart = 0;
            } else {
                selectionStart = lastEnter;
            }
        }

        if (selectionEnd != hqltext.length()) {
            int nextEnter = hqltext.indexOf(this.getNewline(), selectionEnd);

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

        if (geselecteerd.contains(HqlBuilderFrameConstants.REMARKTAG) || geselecteerd.contains("//")) {
            sb.append(voor);
            sb.append(geselecteerd.replace(HqlBuilderFrameConstants.REMARKTAG, "").replace("//", ""));
            sb.append(na);
        } else {
            if (selectionStart == 0) {
                sb.append(HqlBuilderFrameConstants.REMARKTAG);
            } else {
                sb.append(voor);
            }

            String[] lijnen = geselecteerd.split(this.getNewline());

            for (int i = 0; i < (lijnen.length - 1); i++) {
                sb.append(lijnen[i]).append(this.getNewline()).append(HqlBuilderFrameConstants.REMARKTAG);
            }

            sb.append(lijnen[lijnen.length - 1]);

            sb.append(na);
        }

        return sb.toString();
    }

    protected void remove() {
        this.ingoreParameterListSelectionListener = true;

        if (this.parametersEDT.getSelectedRecords().isEmpty()) {
            this.parametersEDT.removeAllRecords();
        } else {
            this.parametersEDT.removeSelectedRecords();
        }
        this.clearParameter();

        this.ingoreParameterListSelectionListener = false;
    }

    protected void resize_columns() {
        //
    }

    protected void save() {
        this.ingoreParameterListSelectionListener = true;

        String text = this.parameterBuilder.getText();
        String name = (this.parameterName.getText().length() > 0) ? this.parameterName.getText() : null;
        Object value = this.selectedQueryParameter.getValue();

        boolean contains = false;
        for (EListRecord<QueryParameter> record : this.parametersEDT.getRecords()) {
            if (record.get().equals(this.selectedQueryParameter)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            EListRecord<QueryParameter> record = new EListRecord<>(this.selectedQueryParameter);
            this.parametersEDT.addRecord(record);
            this.parametersEDT.setSelectedRecord(record);
        }

        this.selectedQueryParameter.setValueText(text);
        this.selectedQueryParameter.setName(name);
        this.selectedQueryParameter.setValue(value);
        this.selectedQueryParameter.setIndex(null);

        this.ingoreParameterListSelectionListener = false;

        this.parametersEDT.repaint();
    }

    protected void search_color() {
        Color chooseColor = this.chooseColor();
        if (chooseColor != null) {
            this.searchColorAction.setValue(this.applyColor(chooseColor));
        }
    }

    protected void set_null() {
        EListRecord<QueryParameter> selected = this.parametersEDT.getSelectedRecord();
        if (selected == null) {
            return;
        }
        this.parameterBuilder.setText("");
        this.save();
    }

    private void setGlassVisible(boolean v) {
        if (this.glass != null) {
            this.glass.setVisible(v);
        }
    }

    public void setMaxResults(int newValue) {
        this.maxResults.setText(" [ max=" + newValue + " ] ");
        this.maxResults.setForeground(newValue > 500 || newValue <= 0 ? HIGHLIGHT_ERROR_DEFAULT_COLOR : Color.BLACK);
    }

    /**
     * start
     */
    public void start(String latestVersion, String version) throws IOException {
        this.version = version;
        this.latestVersion = latestVersion;

        this.reloadColor();

        this.syntaxHighlight.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0F, new float[] { 1.0F }, 0.F));

        this.hql.addCaretListener(e -> HqlBuilderFrame.this.hilightBraces());

        this.sql.setEditable(false);

        this.resultsUnsafe.setAutoscrolls(true);
        this.resultsUnsafe.setFillsViewportHeight(true);
        this.resultsUnsafe.setColumnSelectionAllowed(true);

        this.propertypanel.add(this.values, BorderLayout.CENTER);

        Container framepanel = this.frame.getContentPane();

        JScrollPane jspResults = new JScrollPane(this.resultsUnsafe, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.resultPanel.add(jspResults, BorderLayout.CENTER);
        this.resultsUnsafe.addRowHeader(jspResults, 3);

        this.resultPanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("results")));

        this.parameterspanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("parameters")));

        this.propertypanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("properties")));

        this.hqlsp = new JScrollPane(this.hql, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.hql.withLineNumbers(this.hqlsp);
        this.font(this.hql, 0);
        this.hql_sql_tabs.addTab("HQL", this.hqlsp);
        this.hql_sql_tabs.addTab("SQL",
                new JScrollPane(this.sql, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS));

        {
            EList<String> searchresults = new EList<>(new EListConfig().setBackgroundRenderer(this.backgroundRenderer).setSortable(false));
            searchresults.setFixedCellHeight(20);
            final EList<String> searchresultsEDTSafe = searchresults.stsi();
            final ECheckBox searchClass = new ECheckBox(new ECheckBoxConfig("class", true));
            final ECheckBox searchField = new ECheckBox(new ECheckBoxConfig("field", true));
            ELabeledTextFieldButtonComponent searchinput = new ELabeledTextFieldButtonComponent() {
                private static final long serialVersionUID = 6519657911421417572L;

                @Override
                public void copy(ActionEvent e) {
                    //
                }

                @Override
                protected void doAction() {
                    searchresultsEDTSafe.removeAllRecords();
                    try {
                        List<String> results;
                        if (searchClass.isSelected() != searchField.isSelected()) {
                            if (searchClass.isSelected()) {
                                results = HqlBuilderFrame.this.hqlService.search(this.getInput().getText(), "class",
                                        (Integer) HqlBuilderFrame.this.maximumNumberOfSearchResultsAction.getValue());
                            } else {
                                results = HqlBuilderFrame.this.hqlService.search(this.getInput().getText(), "field",
                                        (Integer) HqlBuilderFrame.this.maximumNumberOfSearchResultsAction.getValue());
                            }
                        } else {
                            results = HqlBuilderFrame.this.hqlService.search(this.getInput().getText(), null,
                                    (Integer) HqlBuilderFrame.this.maximumNumberOfSearchResultsAction.getValue());
                        }
                        searchresultsEDTSafe.addRecords(EList.convert(results));
                    } catch (UnsupportedOperationException ex) {
                        JOptionPane.showMessageDialog(HqlBuilderFrame.this.frame, HqlResourceBundle.getMessage("lucene.unavailable"), "",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        HqlBuilderFrame.logger.error("{}", ex);
                    }
                }

                @Override
                protected String getAction() {
                    return "search-index";
                }

                @Override
                protected Icon getIcon() {
                    return org.tools.hqlbuilder.common.icons.CommonIcons.getIcon(ClientIcons.ZOOM);
                }

                @Override
                public JComponent getParentComponent() {
                    return null;
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
            infopanel.add(searchresults.addRowHeader(
                    new JScrollPane(searchresults, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS)),
                    BorderLayout.CENTER);
            this.hql_sql_tabs.addTab(HqlResourceBundle.getMessage("Lucene search"), infopanel);
        }

        this.hql_sql_tabs.setForegroundAt(0, Color.gray);
        this.hql_sql_tabs.setForegroundAt(1, Color.gray);

        Dimension bd = new Dimension(24, 24);
        EToolBarButton saveButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), this.saveAction));
        saveButton.setText("");
        EToolBarButton setNullButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), this.setNullAction));
        setNullButton.setText("");
        EToolBarButton toTextButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), this.toTextAction));
        toTextButton.setText("");
        EToolBarButton removeButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), this.removeAction));
        removeButton.setText("");
        EToolBarButton upButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), this.upAction));
        upButton.setText("");
        EToolBarButton addParameterButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), this.addParameterAction));
        addParameterButton.setText("");
        EToolBarButton downButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), this.downAction));
        downButton.setText("");
        EToolBarButton importParametersFromTextBtn = new EToolBarButton(
                new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), this.importParametersAction));
        importParametersFromTextBtn.setText("");

        this.parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("name") + ": "));
        this.parameterspanel.add(this.parameterName, "grow");
        this.parameterspanel.add(saveButton, "");

        this.parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("value") + ": "));
        this.parameterspanel.add(this.parameterBuilder, "grow");
        this.parameterspanel.add(toTextButton, "");

        this.parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("compiled") + ": "));
        this.parameterspanel.add(this.parameterValue, "grow");
        this.parameterspanel.add(setNullButton, "");

        this.parameterspanel.add(this.parametersUnsafe.addRowHeader(new JScrollPane(this.parametersUnsafe)), "spanx 2, spany 4, growx, growy");
        this.parameterspanel.add(addParameterButton, "bottom, shrinky");
        this.parameterspanel.add(upButton, "shrinky");
        this.parameterspanel.add(removeButton, "shrinky");
        this.parameterspanel.add(downButton, "top, shrinky, wrap");

        this.parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("import parameters") + ": "), "growx, shrinky");
        this.parameterspanel.add(this.importParametersFromTextF, "growx, shrinky");
        this.parameterspanel.add(importParametersFromTextBtn, "growx, shrinky");

        this.parameterspanel.add(new ELabel(), "growy, growx, spanx 3"); // filler

        JPanel resultsStatusPanel = new JPanel(new MigLayout("", "push[center][center][center][center][center]push", ""));
        resultsStatusPanel.add(this.resultsInfo);
        resultsStatusPanel.add(this.maxResults);
        resultsStatusPanel.add(this.backToStartResultsButton);
        resultsStatusPanel.add(this.startResults);
        resultsStatusPanel.add(this.nextResultsButton);
        this.resultPanel.add(resultsStatusPanel, BorderLayout.SOUTH);

        this.switch_layout();
        framepanel.add(this.normalContentPane, BorderLayout.CENTER);

        this.parameterBuilder.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                // extra help enkel in geval van groovy
                String text = HqlBuilderFrame.this.parameterBuilder.getText();

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
                    HqlBuilderFrame.this.parameterBuilder.setText(text);
                } catch (Exception ex2) {
                    //
                }

                HqlBuilderFrame.this.compile(text);
            }
        });
        this.hql.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (EventHelper.keyEvent(e, EventModifier.CTR_SHIFT_DOWN, ' ')) {
                    HqlBuilderFrame.this.help_insert();
                    return;
                }

                if (EventHelper.keyEvent(e, EventModifier.CTR_SHIFT_DOWN, '/')) {
                    HqlBuilderFrame.this.remark_toggle();
                    return;
                }

                if (HqlBuilderFrame.this.addEndBraceAction.isSelected() && EventHelper.keyEvent(e, '(') && (e.getModifiers() == 0)) {
                    int pos = HqlBuilderFrame.this.hql.getCaretPosition();
                    HqlBuilderFrame.this.hql
                            .setText(HqlBuilderFrame.this.hql.getText().substring(0, pos) + ')' + HqlBuilderFrame.this.hql.getText().substring(pos));
                    HqlBuilderFrame.this.hql.setCaretPosition(pos);
                    return;
                }
            }

            @Override
            public void update(Type type, DocumentEvent e) {
                //
            }
        });

        this.createHqlPopupMenu();

        this.createSqlPopupMenu();

        this.createResultsPopupMenu();

        this.addTableSelectionListener(this.resultsUnsafe, new TableSelectionListener() {
            @Override
            public void cellChanged(int row, int column) {
                HqlBuilderFrame.this.propertypanel.removeAll();
                Object data = ((row == -1) || (column == -1)) ? null : HqlBuilderFrame.this.resultsEDT.getValueAt(row, column);
                if (data == null) {
                    HqlBuilderFrame.this.propertypanel.add(ClientUtils.getPropertyFrame(HqlBuilderFrameConstants.SERIALIZABLE,
                            HqlBuilderFrame.this.editableResultsAction.isSelected()), BorderLayout.CENTER);
                } else {
                    PropertyPanel propertyFrame = ClientUtils.getPropertyFrame(data, HqlBuilderFrame.this.editableResultsAction.isSelected());
                    propertyFrame.setHqlService(HqlBuilderFrame.this.hqlService);
                    HqlBuilderFrame.this.propertypanel.add(HqlBuilderFrame.this.font(propertyFrame, null), BorderLayout.CENTER);
                }
                HqlBuilderFrame.this.propertypanel.revalidate();
            }

            @Override
            public void columnChanged(int column) {
                //
            }

            @Override
            public void rowChanged(int row) {
                //
            }
        });
        this.parametersEDT.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            HqlBuilderFrame.this.parameterSelected();
        });

        this.hql_sql_tabs_panel.add(this.hql_sql_tabs, BorderLayout.CENTER);

        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                HqlBuilderFrame.this.exit();
            }
        });

        EToolBarButtonCustomizer etbc = new EToolBarButtonCustomizer();
        {
            hqltools = new JToolBar(javax.swing.SwingConstants.VERTICAL);
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.startQueryAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.stopQueryAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.wizardAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.clearAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.findParametersAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.favoritesAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.addToFavoritesAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.formatAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.namedQueryAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.clipboardExportAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.clipboardImportAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.helpInsertAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.remarkToggleAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, this.deleteInvertedSelectionAction)));
            this.hql_sql_tabs_panel.add(hqltools, BorderLayout.WEST);
        }
        {
            resultstools = new JToolBar(javax.swing.SwingConstants.VERTICAL);
            resultstools.add(new EButton(new EButtonConfig(etbc, this.hibernatePropertiesAction)));
            resultstools.add(new EButton(new EButtonConfig(etbc, this.objectTreeAction)));
            resultstools.add(new EButton(new EButtonConfig(etbc, this.deleteObjectAction)));
            resultstools.add(new EButton(new EButtonConfig(etbc, this.copyCellAction)));
            resultstools.add(new EButton(new EButtonConfig(etbc, this.executeScriptOnColumnAction)));
            this.resultPanel.add(resultstools, BorderLayout.WEST);
        }
        show_toolbars();

        JMenuBar menuBar = new JMenuBar();
        this.frame.setJMenuBar(menuBar);
        {
            JMenu actionsMenu = new JMenu(HqlResourceBundle.getMessage("actions"));
            actionsMenu.add(new JCheckBoxMenuItem(this.switchLayoutAction));
            actionsMenu.add(new JMenuItem(this.exitAction));
            actionsMenu.add(new JMenuItem(this.forceExitAction));
            menuBar.add(actionsMenu);
        }
        {
            JMenu settingsMenu = new JMenu(HqlResourceBundle.getMessage("settings"));
            {
                settingsMenu.add(new JCheckBoxMenuItem(this.formatSqlAction));
                settingsMenu.add(this.formatSqlOptionsMenu);
                this.formatSqlOptionsMenu.add(new JCheckBoxMenuItem(this.removeJoinsAction));
                this.formatSqlOptionsMenu.add(new JCheckBoxMenuItem(this.formatLinesAction));
                this.formatSqlOptionsMenu.add(new JCheckBoxMenuItem(this.replacePropertiesAction));
            }
            {
                JMenu addmi = new JMenu(HqlResourceBundle.getMessage("language"));
                ButtonGroup lanGroup = new ButtonGroup();

                List<Locale> locales = new ArrayList<>();

                for (Locale locale : Locale.getAvailableLocales()) {
                    URL bundle = HqlBuilderFrame.class.getClassLoader().getResource("HqlResourceBundle_" + locale + ".properties");
                    HqlBuilderFrame.logger.debug("{}>{}", locale, bundle);
                    if (null != bundle) {
                        locales.add(locale);
                    }
                }

                locales.remove(HqlBuilderFrameConstants.DEFAULT_LOCALE);

                Collections.sort(locales, (o1, o2) -> new CompareToBuilder().append(o1.getDisplayLanguage(o1), o2.getDisplayLanguage(o2))
                        .append(o1.getDisplayCountry(o1), o2.getDisplayCountry(o2))
                        .toComparison());

                locales.add(0, HqlBuilderFrameConstants.DEFAULT_LOCALE);

                for (Locale locale : locales) {
                    final Locale loc = locale;
                    String title = locale.getDisplayLanguage(locale);
                    if (StringUtils.isNotBlank(locale.getCountry())) {
                        title += ", " + locale.getDisplayCountry(locale);
                    }
                    title += " (" + locale.toString() + ")";
                    JCheckBoxMenuItem lanMenu = new JCheckBoxMenuItem(title);
                    addmi.add(lanMenu);
                    lanGroup.add(lanMenu);
                    if (locale.equals(HqlBuilderFrameConstants.DEFAULT_LOCALE)
                            || locale.getLanguage().equals(SystemSettings.getCurrentLocale().getLanguage())) {
                        lanMenu.setSelected(true);
                    }
                    lanMenu.addActionListener(e -> {
                        // SystemSettings.setCurrentLocale(loc);
                        HqlBuilderFrame.this.preferences.put(HqlBuilderFrameConstants.PERSISTENT_LOCALE, loc.toString());
                        JOptionPane.showMessageDialog(HqlBuilderFrame.this.frame, HqlResourceBundle.getMessage("change visible after restart"), "",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                }

                settingsMenu.add(addmi);
            }
            {
                JMenu addmi = new JMenu(HqlResourceBundle.getMessage("additional settings"));
                addmi.add(new JCheckBoxMenuItem(this.highlightSyntaxAction));
                addmi.add(new JMenuItem(this.highlightSyntaxColorAction));
                addmi.add(new JMenuItem(this.continuousSyntaxHighlightingAction));
                addmi.add(new JCheckBoxMenuItem(this.highlightBracesAction));
                addmi.add(new JMenuItem(this.highlightBracesColorAction));
                addmi.add(new JCheckBoxMenuItem(this.resizeColumnsAction));
                addmi.add(this.maximumNumberOfResultsAction);
                addmi.add(this.fontAction);
                addmi.add(new JCheckBoxMenuItem(this.alwaysOnTopAction));
                addmi.add(new JCheckBoxMenuItem(this.editableResultsAction));
                addmi.add(new JMenuItem(this.searchColorAction));
                if (SystemTray.isSupported()) {
                    addmi.add(new JCheckBoxMenuItem(this.systrayAction));
                }
                addmi.add(new JCheckBoxMenuItem(this.addEndBraceAction));
                addmi.add(new JCheckBoxMenuItem(this.addShowErrorTooltip));
                addmi.add(new JCheckBoxMenuItem(this.addSelectExecutedHql));
                addmi.add(this.maximumNumberOfSearchResultsAction);
                addmi.add(new JCheckBoxMenuItem(this.showToolbarsAction));
                settingsMenu.add(addmi);
            }
            {
                settingsMenu.addSeparator();
            }
            {
                JMenuItem resetMi = new JMenuItem(HqlResourceBundle.getMessage("reset settings"));
                resetMi.addActionListener(e -> {
                    HqlBuilderFrame.this.fontAction.setWarnRestart(false);

                    HqlBuilderFrame.this.maximumNumberOfResultsAction.setValue(100);
                    HqlBuilderFrame.this.maximumNumberOfSearchResultsAction.setValue(2000);
                    HqlBuilderFrame.this.fontAction.setValue(ClientUtils.getDefaultFont().deriveFont(DEFAULT_FONT_SIZE));
                    HqlBuilderFrame.this.searchColorAction.setValue(HIGHLIGHT_SEARCH_DEFAULT_COLOR);
                    HqlBuilderFrame.this.highlightSyntaxColorAction.setValue(HIGHLIGHT_SYNTAXT_DEFAULT_COLOR);
                    HqlBuilderFrame.this.highlightBracesColorAction.setValue(HIGHLIGHT_BRACES_DEFAULT_COLOR);
                    HqlBuilderFrame.this.continuousSyntaxHighlightingAction.setValue(true);
                    HqlBuilderFrame.this.removeJoinsAction.setSelected(true);
                    HqlBuilderFrame.this.formatLinesAction.setSelected(true);
                    HqlBuilderFrame.this.replacePropertiesAction.setSelected(true);
                    HqlBuilderFrame.this.resizeColumnsAction.setSelected(true);
                    HqlBuilderFrame.this.formatSqlAction.setSelected(true);
                    HqlBuilderFrame.this.systrayAction.setSelected(true);
                    HqlBuilderFrame.this.highlightSyntaxAction.setSelected(true);
                    HqlBuilderFrame.this.alwaysOnTopAction.setSelected(false);
                    HqlBuilderFrame.this.editableResultsAction.setSelected(false);
                    HqlBuilderFrame.this.switchLayoutAction.setSelected(true);
                    HqlBuilderFrame.this.addEndBraceAction.setSelected(true);
                    HqlBuilderFrame.this.addShowErrorTooltip.setSelected(true);
                    HqlBuilderFrame.this.addSelectExecutedHql.setSelected(true);
                    HqlBuilderFrame.this.fontAction.setWarnRestart(true);
                    HqlBuilderFrame.this.showToolbarsAction.setSelected(true);

                    HqlBuilderFrame.this.editable_results();

                    JOptionPane.showMessageDialog(HqlBuilderFrame.this.frame, HqlResourceBundle.getMessage("change visible after restart"), "",
                            JOptionPane.INFORMATION_MESSAGE);
                });
                settingsMenu.add(resetMi);
            }
            menuBar.add(settingsMenu);
        }
        {
            JMenu hqlmenu = new JMenu("HQL");
            hqlmenu.add(new JMenuItem(this.startQueryAction));
            hqlmenu.add(new JMenuItem(this.stopQueryAction));
            hqlmenu.add(new JMenuItem(this.wizardAction));
            hqlmenu.add(new JMenuItem(this.clearAction));
            hqlmenu.add(new JMenuItem(this.findParametersAction));
            hqlmenu.add(new JMenuItem(this.favoritesAction));
            hqlmenu.add(new JMenuItem(this.addToFavoritesAction));
            hqlmenu.add(new JMenuItem(this.formatAction));
            hqlmenu.add(new JMenuItem(this.namedQueryAction));
            hqlmenu.add(new JMenuItem(this.clipboardExportAction));
            hqlmenu.add(new JMenuItem(this.clipboardImportAction));
            hqlmenu.add(new JMenuItem(this.helpInsertAction));
            hqlmenu.add(new JMenuItem(this.remarkToggleAction));
            hqlmenu.add(new JMenuItem(this.deleteInvertedSelectionAction));
            menuBar.add(hqlmenu);
        }
        {
            JMenu resultsmenu = new JMenu(HqlResourceBundle.getMessage("results"));
            resultsmenu.add(new JMenuItem(this.hibernatePropertiesAction));
            resultsmenu.add(new JMenuItem(this.objectTreeAction));
            resultsmenu.add(new JMenuItem(this.deleteObjectAction));
            resultsmenu.add(new JMenuItem(this.copyCellAction));
            resultsmenu.add(new JMenuItem(this.executeScriptOnColumnAction));
            menuBar.add(resultsmenu);
        }
        {
            JMenu helpmenu = new JMenu(HqlResourceBundle.getMessage("help"));
            helpmenu.add(new JMenuItem(this.helpHibernateAction));
            helpmenu.add(new JMenuItem(this.helpHqlAction));
            helpmenu.add(new JMenuItem(this.luceneQuerySyntaxAction));
            helpmenu.add(new JMenuItem(this.helpAction));
            helpmenu.add(new JMenuItem(this.helpJavaPropertiesAction));
            helpmenu.add(new JMenuItem(this.versionsAction));
            helpmenu.add(new JMenuItem(this.aboutAction));
            menuBar.add(helpmenu);
        }

        this.frame.setTitle(HqlBuilderFrameConstants.NAME + " v" + this.version + " - " + this.hqlService.getConnectionInfo() + " - "
                + this.hqlService.getProject() + (this.hqlService.getServiceUrl() == null ? "" : " - " + this.hqlService.getServiceUrl()));
        this.frame.setVisible(true);
        this.frame.setSize(new Dimension(1024, 768));
        this.frame.setExtendedState(this.frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        this.frame.setIconImage(HqlBuilderImages.getIcon().getImage());
        this.frame.setGlassPane(this.getGlass(this.frame));
        this.frame.setAlwaysOnTop(Boolean.TRUE.equals(this.alwaysOnTopAction.getValue()));
    }

    protected void start_query() {
        start_query(true);
    }

    protected void stop_query() {
        try {
            if (queryExecution != null) {
                queryExecution.cancel(true);
            }
            hqlService.stopQuery(queryParameters.getUuid());
        } catch (Exception ex) {
            //
        }
    }

    protected void start_query(boolean resetPointer) {
        try {
            this.executeQuery(null, false, resetPointer);
        } catch (Exception ex) {
            HqlBuilderFrame.logger.error("{}", ex);
        }
    }

    protected void startPre() {
        this.loadFavorites();
    }

    protected void switch_layout() {
        HqlBuilderFrame.logger.info("change layout");

        this.normalContentPane.setVisible(false);
        this.normalContentPane.removeAll();

        // if (menu_largeLayout.getState()) {
        if (this.switchLayoutAction.isSelected()) {
            // hql_sql_tabs .... parameterspanel .... propertypanel
            // resultPanel
            this.split0 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            this.split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            this.split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

            EComponentPopupMenu.removeAllRegisteredKeystroke(this.split0);
            EComponentPopupMenu.removeAllRegisteredKeystroke(this.split1);
            EComponentPopupMenu.removeAllRegisteredKeystroke(this.split2);

            this.split0.setLeftComponent(this.split1);
            this.split1.setRightComponent(this.split2);

            this.split1.setLeftComponent(this.hql_sql_tabs_panel);
            this.split2.setLeftComponent(this.parameterspanel);
            this.split2.setRightComponent(this.propertypanel);
            this.split0.setRightComponent(this.resultPanel);

            this.normalContentPane.add(this.split0, BorderLayout.CENTER);
        } else {
            // hql_sql_tabs .... parameterspanel
            // resultPanel ..... propertypanel
            this.split0 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            this.split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            this.split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

            EComponentPopupMenu.removeAllRegisteredKeystroke(this.split0);
            EComponentPopupMenu.removeAllRegisteredKeystroke(this.split1);
            EComponentPopupMenu.removeAllRegisteredKeystroke(this.split2);

            this.split0.setLeftComponent(this.split1);
            this.split0.setRightComponent(this.split2);

            this.split1.setLeftComponent(this.hql_sql_tabs_panel);
            this.split1.setRightComponent(this.parameterspanel);
            this.split2.setLeftComponent(this.resultPanel);
            this.split2.setRightComponent(this.propertypanel);

            this.normalContentPane.add(this.split0, BorderLayout.CENTER);
        }

        this.layout(null);

        this.normalContentPane.setVisible(true);
    }

    private void switchVisibility() {
        if (!SystemTray.isSupported()) {
            return;
        }
        if (!Boolean.TRUE.equals(this.systrayAction.getValue())) {
            return;
        }
        boolean v = !this.frame.isVisible();
        this.frame.setVisible(v);
        if (v) {
            this.frame.toFront();
            this.frame.setState(Frame.NORMAL);
        }
        if (!v) {
            try {
                SystemTray.getSystemTray().add(this.trayIcon);
            } catch (AWTException ex) {
                HqlBuilderFrame.logger.error("{}", ex);
            }
        } else {
            SystemTray.getSystemTray().remove(this.trayIcon);
        }
    }

    protected void syntaxHi(List<int[]> pos, String str) {
        str = str.toLowerCase();
        String hqltext = this.hql.getText().toLowerCase();
        int fromIndex = 0;
        int start = hqltext.indexOf(str, fromIndex);
        while (start != -1) {
            int end = start + str.length();
            boolean before = (start == 0) || this.isSeperator(hqltext.charAt(start - 1));
            boolean after = (end >= (hqltext.length() - 1)) || this.isSeperator(hqltext.charAt(end));
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

    protected void to_text() {
        if (this.parameterBuilder.getText().startsWith("'") && this.parameterBuilder.getText().endsWith("'")) {
            return;
        }
        this.parameterBuilder.setText("'" + this.parameterBuilder.getText() + "'");
        this.save();
    }

    protected void up() {
        this.parametersEDT.moveSelectedUp();
    }

    protected void versions() {
        StringBuilder sb = new StringBuilder("<html>");
        for (Map.Entry<String, String> hibernateInfo : this.hqlService.getHibernateInfo().entrySet()) {
            sb.append(hibernateInfo.getKey()).append(": ").append(hibernateInfo.getValue()).append("<br>");
        }
        sb.append("</html>");
        JOptionPane.showMessageDialog(this.frame, this.font(new ELabel(HqlResourceBundle.getMessage("versions.description", sb.toString())), 14),
                HqlResourceBundle.getMessage("versions.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    protected void wizard() {
        CommonUtils.run(() -> new HqlWizard(query -> HqlBuilderFrame.this.hql.setText(query), HqlBuilderFrame.this.frame,
                HqlBuilderFrame.this.getHibernateWebResolver()));
    }

    protected void continuous_syntax_highlighting() {
        if (Boolean.TRUE.equals(continuousSyntaxHighlightingAction.getValue())) {
            continuousSyntaxHighlightingAction.setValue(false);
            this.hql.removeHighlights(this.syntaxHighlight);
        } else {
            continuousSyntaxHighlightingAction.setValue(true);
            hilightSyntax();
        }
    }
}

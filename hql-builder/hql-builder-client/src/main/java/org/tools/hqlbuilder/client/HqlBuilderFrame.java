package org.tools.hqlbuilder.client;

import static org.tools.hqlbuilder.common.icons.ClientIcons.ADD;
import static org.tools.hqlbuilder.common.icons.ClientIcons.APPLICATION_SIDE_TREE;
import static org.tools.hqlbuilder.common.icons.ClientIcons.ATTACH;
import static org.tools.hqlbuilder.common.icons.ClientIcons.AWARD_STAR_ADD;
import static org.tools.hqlbuilder.common.icons.ClientIcons.AWARD_STAR_GOLD_3;
import static org.tools.hqlbuilder.common.icons.ClientIcons.BIN_EMPTY;
import static org.tools.hqlbuilder.common.icons.ClientIcons.BOOK_NEXT;
import static org.tools.hqlbuilder.common.icons.ClientIcons.BULLET_ARROW_DOWN;
import static org.tools.hqlbuilder.common.icons.ClientIcons.BULLET_ARROW_UP;
import static org.tools.hqlbuilder.common.icons.ClientIcons.COG;
import static org.tools.hqlbuilder.common.icons.ClientIcons.CONTROL_FASTFORWARD_BLUE_PNG;
import static org.tools.hqlbuilder.common.icons.ClientIcons.CONTROL_PLAY_BLUE;
import static org.tools.hqlbuilder.common.icons.ClientIcons.CONTROL_REWIND_BLUE_PNG;
import static org.tools.hqlbuilder.common.icons.ClientIcons.CROSS;
import static org.tools.hqlbuilder.common.icons.ClientIcons.DISK;
import static org.tools.hqlbuilder.common.icons.ClientIcons.DOOR;
import static org.tools.hqlbuilder.common.icons.ClientIcons.LAYOUT_CONTENT;
import static org.tools.hqlbuilder.common.icons.ClientIcons.PACKAGE_GO;
import static org.tools.hqlbuilder.common.icons.ClientIcons.PAGE_WHITE_STACK;
import static org.tools.hqlbuilder.common.icons.ClientIcons.PICTURE_EMPTY;
import static org.tools.hqlbuilder.common.icons.ClientIcons.TABLE_LIGHTNING;
import static org.tools.hqlbuilder.common.icons.ClientIcons.TABLE_ROW_DELETE;
import static org.tools.hqlbuilder.common.icons.ClientIcons.TEXTFIELD;
import static org.tools.hqlbuilder.common.icons.ClientIcons.TEXT_ALIGN_LEFT;
import static org.tools.hqlbuilder.common.icons.ClientIcons.TEXT_INDENT;
import static org.tools.hqlbuilder.common.icons.ClientIcons.WAND;
import static org.tools.hqlbuilder.common.icons.ClientIcons.ZOOM;
import static org.tools.hqlbuilder.common.icons.CommonIcons.getIcon;

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
import java.io.FileInputStream;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Utilities;
import javax.xml.parsers.DocumentBuilderFactory;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
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
import org.swingeasy.ELabel;
import org.swingeasy.ELabeledTextFieldButtonComponent;
import org.swingeasy.EList;
import org.swingeasy.EListConfig;
import org.swingeasy.EListRecord;
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
import org.swingeasy.system.SystemSettings;
import org.tools.hqlbuilder.client.HqlWizard.HqlWizardListener;
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

/**
 * @author Jurgen
 */
public class HqlBuilderFrame implements HqlBuilderFrameConstants {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlBuilderFrame.class);

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

    private final HqlBuilderAction formatAction;

    private final HqlBuilderAction namedQueryAction;

    private final HqlBuilderAction clipboardExportAction;

    private final HqlBuilderAction clipboardImportAction;

    private final HqlBuilderAction helpInsertAction;

    private final HqlBuilderAction remarkToggleAction;

    private final HqlBuilderAction deleteInvertedSelectionAction;

    private final HqlBuilderAction helpAction = new HqlBuilderAction(null, this, HELP, true, HELP,
            getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP), HELP, HELP, true, 'h', "F1");

    private final HqlBuilderAction exitAction = new HqlBuilderAction(null, this, EXIT, true, EXIT, getIcon(DOOR), EXIT, EXIT, true, 'x', "alt X");

    private final HqlBuilderAction aboutAction = new HqlBuilderAction(null, this, ABOUT, true, ABOUT, HqlBuilderImages.getIcon(), ABOUT, ABOUT, true,
            null, null);

    private final HqlBuilderAction versionsAction = new HqlBuilderAction(null, this, VERSIONS, true, VERSIONS, HqlBuilderImages.getIcon(), VERSIONS,
            VERSIONS, true, null, null);

    private final HqlBuilderAction helpHibernateAction = new HqlBuilderAction(null, this, HIBERNATE_DOCUMENTATION, true, HIBERNATE_DOCUMENTATION,
            getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP), HIBERNATE_DOCUMENTATION, HIBERNATE_DOCUMENTATION, true, null, null);

    private final HqlBuilderAction helpHqlAction = new HqlBuilderAction(null, this, HQL_DOCUMENTATION, true, HQL_DOCUMENTATION,
            getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP), HQL_DOCUMENTATION, HQL_DOCUMENTATION, true, null, null);

    private final HqlBuilderAction luceneQuerySyntaxAction = new HqlBuilderAction(null, this, LUCENE_QUERY_SYNTAX, true, LUCENE_QUERY_SYNTAX,
            getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.HELP), LUCENE_QUERY_SYNTAX, LUCENE_QUERY_SYNTAX, true, null, null);

    private final HqlBuilderAction forceExitAction = new HqlBuilderAction(null, this, FORCE_EXIT, true, FORCE_EXIT, getIcon(CROSS), FORCE_EXIT,
            FORCE_EXIT, true, 't', null);

    private final HqlBuilderAction removeJoinsAction = new HqlBuilderAction(null, this, IMMEDIATE_QUERY, true, REMOVE_JOINS, null, REMOVE_JOINS,
            REMOVE_JOINS, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction formatLinesAction = new HqlBuilderAction(null, this, IMMEDIATE_QUERY, true, FORMAT_LINES, null, FORMAT_LINES,
            FORMAT_LINES, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction replacePropertiesAction = new HqlBuilderAction(null, this, IMMEDIATE_QUERY, true, REPLACE_PROPERTIES, null,
            REPLACE_PROPERTIES, REPLACE_PROPERTIES, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction resizeColumnsAction = new HqlBuilderAction(null, this, RESIZE_COLUMNS, true, RESIZE_COLUMNS, null, RESIZE_COLUMNS,
            RESIZE_COLUMNS, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction formatSqlAction = new HqlBuilderAction(null, this, FORMAT_SQL, true, FORMAT_SQL, null, FORMAT_SQL, FORMAT_SQL,
            true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction maximumNumberOfResultsAction = new HqlBuilderAction(null, this, MAXIMUM_NUMBER_OF_RESULTS, true,
            MAXIMUM_NUMBER_OF_RESULTS, "org/tools/hqlbuilder/client/images/scr.png", MAXIMUM_NUMBER_OF_RESULTS, MAXIMUM_NUMBER_OF_RESULTS, true,
            null, null, PERSISTENT_ID, Integer.class, 100);

    private final HqlBuilderAction maximumNumberOfSearchResultsAction = new HqlBuilderAction(null, this, MAXIMUM_NUMBER_OF_SEARCH_RESULTS, true,
            MAXIMUM_NUMBER_OF_SEARCH_RESULTS, "org/tools/hqlbuilder/client/images/scr.png", MAXIMUM_NUMBER_OF_SEARCH_RESULTS,
            MAXIMUM_NUMBER_OF_SEARCH_RESULTS, true, null, null, PERSISTENT_ID, Integer.class, 2000);

    private HqlBuilderAction fontAction;

    private final HqlBuilderAction systrayAction = new HqlBuilderAction(null, this, null, true, SYSTEM_TRAY, null, SYSTEM_TRAY, SYSTEM_TRAY, true,
            null, null, PERSISTENT_ID);

    private final HqlBuilderAction highlightSyntaxAction = new HqlBuilderAction(null, this, null, true, HIGHLIGHT_SYNTAX, null, HIGHLIGHT_SYNTAX,
            HIGHLIGHT_SYNTAX, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction highlightColorAction = new HqlBuilderAction(null, this, HIGHLIGHT_COLOR, true, HIGHLIGHT_COLOR, null,
            HIGHLIGHT_COLOR, HIGHLIGHT_COLOR, false, null, null, PERSISTENT_ID, Color.class, new Color(0, 0, 255));

    private final HqlBuilderAction searchColorAction = new HqlBuilderAction(null, this, SEARCH_COLOR, true, SEARCH_COLOR, null, SEARCH_COLOR,
            SEARCH_COLOR, false, null, null, PERSISTENT_ID, Color.class, new Color(245, 225, 145));

    private final HqlBuilderAction alwaysOnTopAction = new HqlBuilderAction(null, this, ALWAYS_ON_TOP, true, ALWAYS_ON_TOP, null, ALWAYS_ON_TOP,
            ALWAYS_ON_TOP, false, null, null, PERSISTENT_ID);

    private final HqlBuilderAction editableResultsAction;

    private final HqlBuilderAction switchLayoutAction = new HqlBuilderAction(null, this, SWITCH_LAYOUT, true, SWITCH_LAYOUT, getIcon(LAYOUT_CONTENT),
            SWITCH_LAYOUT, SWITCH_LAYOUT, false, 'w', null, PERSISTENT_ID);

    private final HqlBuilderAction addEndBraceAction = new HqlBuilderAction(null, this, null, true, ADD_END_BRACE, null, ADD_END_BRACE,
            ADD_END_BRACE, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction addShowErrorTooltip = new HqlBuilderAction(null, this, null, true, SHOW_ERROR_TOOLTIP, null, SHOW_ERROR_TOOLTIP,
            SHOW_ERROR_TOOLTIP, true, null, null, PERSISTENT_ID);

    private final HqlBuilderAction addSelectExecutedHql = new HqlBuilderAction(null, this, null, true, SELECT_HQL_BEING_EXECUTED, null,
            SELECT_HQL_BEING_EXECUTED, SELECT_HQL_BEING_EXECUTED, true, null, null, PERSISTENT_ID);

    private final ELabel maxResults;

    private final LinkedList<QueryFavorite> favorites = new LinkedList<QueryFavorite>();

    private final JComponent values = ClientUtils.getPropertyFrame(new Serializable() {
        private static final long serialVersionUID = 1L;
    }, false);

    /** aliases query */
    private Map<String, String> aliases = new HashMap<String, String>();

    /** selected query parameter */
    private QueryParameter selectedQueryParameter = new QueryParameter();

    /** scripts being executed on column */
    private Map<Integer, String> scripts = new HashMap<Integer, String>();

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

    private ETextAreaBorderHighlightPainter syntaxHighlight = new ETextAreaBorderHighlightPainter(getHighlightColor());

    private ETextAreaBorderHighlightPainter bracesHighlight = new ETextAreaBorderHighlightPainter(getHighlightColor());

    private ETextAreaBorderHighlightPainter syntaxErrorsHighlight = new ETextAreaBorderHighlightPainter(Color.RED);

    private TrayIcon trayIcon;

    private HqlServiceClient hqlService;

    private boolean ingoreParameterListSelectionListener = false;

    private EComponentGradientRenderer backgroundRenderer = new EComponentGradientRenderer(EComponentGradientRenderer.GradientOrientation.VERTICAL,
            Color.white, new Color(212, 212, 212));

    private final List<Highlighter.Highlight> errorLocs = new ArrayList<Highlighter.Highlight>();

    private String errorString;

    private HqlBuilderFrame() {
        // needs to be first to init font
        fontAction = new HqlBuilderAction(null, this, FONT, true, FONT, getIcon(org.tools.hqlbuilder.common.icons.ClientIcons.FONT), FONT, FONT,
                true, null, null, PERSISTENT_ID, Font.class, ClientUtils.getDefaultFont());
        fontAction.setWarnRestart(true);

        UIManager.put("ToolTip.font", new FontUIResource(getFont()));
        resultsInfo = font(new ELabel(""), null);
        parameterBuilder = font(new ETextField(new ETextFieldConfig()), null);
        parameterName = font(new ETextField(new ETextFieldConfig()), null);
        parameterValue = font(new ETextField(new ETextFieldConfig(false)), null);
        hql = font(new ETextArea(new ETextAreaConfig().setTooltips(true)) {
            private static final long serialVersionUID = 5778951450821178464L;

            @Override
            public String getToolTipText(MouseEvent event) {
                if (addShowErrorTooltip.isSelected()) {
                    if (StringUtils.isNotBlank(errorString)) {
                        int offs = viewToModel(event.getPoint());
                        try {
                            Rectangle modelToView = modelToView(offs);
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
                        if (errorLocs.size() > 0) {
                            showTT = false;
                            for (Highlight hl : errorLocs) {
                                if (hl.getStartOffset() <= offs && offs <= hl.getEndOffset()) {
                                    showTT = true;
                                }
                            }
                        }
                        if (!showTT) {
                            return null;
                        }
                        if (errorString.length() > 100) {
                            return "<html><p width=800>" + errorString + "</p><html>";
                        }
                        return "<html><p>" + errorString + "</p><html>";
                    }
                }
                try {
                    int offs = this.viewToModel(event.getPoint());
                    int startIndex = Utilities.getWordStart(this, offs);
                    int endIndex = Utilities.getWordEnd(this, offs);
                    String substring = this.getText().substring(Math.max(0, startIndex - 1), endIndex);
                    if (substring.startsWith(":")) {
                        String tt = substring.substring(1);
                        for (EListRecord<QueryParameter> record : parametersEDT.getRecords()) {
                            if (tt.equals(record.get().getName())) {
                                return record.get().toString();
                            }
                        }
                    }
                    if (substring.length() == 2 && substring.charAt(1) == '?') {
                        String textUpTo = this.getText().substring(0, Math.max(0, startIndex - 1));
                        String lines[] = textUpTo.split(LINESEPERATOR);
                        int count = 0;
                        boolean remarked = false;
                        for (String line : lines) {
                            int dash = line.indexOf(REMARKTAG);
                            remarked = dash != -1;
                            if (dash != -1) {
                                line = line.substring(0, dash);
                            }
                            count += StringUtils.countMatches(line, "?");
                        }
                        if (!remarked) {
                            try {
                                return parametersEDT.getRecords().get(count).get().toString();
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
        }, null);
        ToolTipManager.sharedInstance().registerComponent(hql);
        sql = font(new ETextArea(new ETextAreaConfig(false)), null);
        maxResults = font(new ELabel(""), null, Font.BOLD);
        setMaxResults(Integer.parseInt("" + maximumNumberOfResultsAction.getValue()));
        new MouseDoubleClickAction(maximumNumberOfResultsAction).inject(maxResults);
        parametersUnsafe = font(new EList<QueryParameter>(new EListConfig().setBackgroundRenderer(backgroundRenderer)), null);
        parametersUnsafe.setFixedCellHeight(20);
        parametersEDT = parametersUnsafe.stsi();
        resultsUnsafe = font(new ETable<List<Object>>(new ETableConfig(true)), null);
        resultsEDT = resultsUnsafe.stsi();
        hibernatePropertiesAction = new HqlBuilderAction(resultsUnsafe, this, HIBERNATE_PROPERTIES, true, HIBERNATE_PROPERTIES,
                getIcon(PAGE_WHITE_STACK), HIBERNATE_PROPERTIES, HIBERNATE_PROPERTIES, true, null, "shift alt F8");
        objectTreeAction = new HqlBuilderAction(resultsUnsafe, this, OBJECT_TREE, true, OBJECT_TREE, getIcon(APPLICATION_SIDE_TREE), OBJECT_TREE,
                OBJECT_TREE, true, null, "shift alt F9");
        deleteObjectAction = new HqlBuilderAction(resultsUnsafe, this, DELETE_OBJECT, true, DELETE_OBJECT, getIcon(BIN_EMPTY), DELETE_OBJECT,
                DELETE_OBJECT, true, null, "shift alt F10");
        copyCellAction = new HqlBuilderAction(resultsUnsafe, this, COPY_SELECTED_CELL, true, COPY_SELECTED_CELL, getIcon(TABLE_LIGHTNING),
                COPY_SELECTED_CELL, COPY_SELECTED_CELL, true, null, "shift alt F11");
        executeScriptOnColumnAction = new HqlBuilderAction(resultsUnsafe, this, EXECUTE_SCRIPT_ON_COLUMN, true, EXECUTE_SCRIPT_ON_COLUMN,
                "org/tools/hqlbuilder/client/images/groovy.png", EXECUTE_SCRIPT_ON_COLUMN, EXECUTE_SCRIPT_ON_COLUMN, true, null, "shift alt F12");
        downAction = new HqlBuilderAction(parametersUnsafe, this, DOWN, true, DOWN, getIcon(BULLET_ARROW_DOWN), DOWN, DOWN, false, null, null);
        removeAction = new HqlBuilderAction(parametersUnsafe, this, REMOVE, true, REMOVE, getIcon(BIN_EMPTY), REMOVE, REMOVE, false, null, null);
        saveAction = new HqlBuilderAction(parametersUnsafe, this, SAVE, true, SAVE, getIcon(DISK), SAVE, SAVE, false, null, null);
        setNullAction = new HqlBuilderAction(parametersUnsafe, this, SET_NULL, true, SET_NULL, getIcon(PICTURE_EMPTY), SET_NULL, SET_NULL, false,
                null, null);
        toTextAction = new HqlBuilderAction(parametersUnsafe, this, TO_TEXT, true, TO_TEXT, getIcon(TEXTFIELD), TO_TEXT, TO_TEXT, false, null, null);
        upAction = new HqlBuilderAction(parametersUnsafe, this, UP, true, UP, getIcon(BULLET_ARROW_UP), UP, UP, false, null, null);
        addParameterAction = new HqlBuilderAction(parametersUnsafe, this, ADD_PARAMETER, true, ADD_PARAMETER, getIcon(ADD), ADD_PARAMETER,
                ADD_PARAMETER, false, null, null);
        ActionListener commitParam = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parametersEDT.getSelectedRecord() != null) {
                    saveAction.actionPerformed(e);
                } else {
                    addParameterAction.actionPerformed(e);
                }
            }
        };
        parameterName.addActionListener(commitParam);
        parameterBuilder.addActionListener(commitParam);
        importParametersAction = new HqlBuilderAction(parametersUnsafe, this, IMPORT_PARAMETERS, true, IMPORT_PARAMETERS, getIcon(COG),
                IMPORT_PARAMETERS, IMPORT_PARAMETERS, false, null, null);
        wizardAction = new HqlBuilderAction(hql, this, WIZARD, true, WIZARD, getIcon(WAND), WIZARD, WIZARD, true, null, "alt F1");
        clearAction = new HqlBuilderAction(hql, this, CLEAR, true, CLEAR, getIcon(BIN_EMPTY), CLEAR, CLEAR, true, null, "alt F2");
        findParametersAction = new HqlBuilderAction(hql, this, FIND_PARAMETERS, true, FIND_PARAMETERS, getIcon(BOOK_NEXT), FIND_PARAMETERS,
                FIND_PARAMETERS, true, null, "alt F3");
        favoritesAction = new HqlBuilderAction(hql, this, FAVORITES, true, FAVORITES, getIcon(AWARD_STAR_GOLD_3), FAVORITES, FAVORITES, true, null,
                "alt F4");
        addToFavoritesAction = new HqlBuilderAction(hql, this, ADD_TO_FAVORITES, true, ADD_TO_FAVORITES, getIcon(AWARD_STAR_ADD), ADD_TO_FAVORITES,
                ADD_TO_FAVORITES, true, null, "alt F5");
        // alt F6 not taken
        // alt F7 not taken
        // alt F8 not taken
        formatAction = new HqlBuilderAction(hql, this, FORMAT, true, FORMAT, getIcon(TEXT_ALIGN_LEFT), FORMAT, FORMAT, true, null, "alt F9");
        namedQueryAction = new HqlBuilderAction(hql, this, LOAD_NAMED_QUERY, true, LOAD_NAMED_QUERY, getIcon(PACKAGE_GO), LOAD_NAMED_QUERY,
                LOAD_NAMED_QUERY, true, null, "alt F10");
        clipboardExportAction = new HqlBuilderAction(hql, this, EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, true, EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD,
                getIcon(CONTROL_FASTFORWARD_BLUE_PNG), EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, EXPORT_COPY_HQL_AS_JAVA_TO_CLIPBOARD, true, null,
                "alt F11");
        clipboardImportAction = new HqlBuilderAction(hql, this, IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, true,
                IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, getIcon(CONTROL_REWIND_BLUE_PNG), IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD,
                IMPORT_PASTE_HQL_AS_JAVA_FROM_CLIPBOARD, true, null, "alt F12");
        helpInsertAction = new HqlBuilderAction(hql, this, HELP_INSERT, true, HELP_INSERT, getIcon(ATTACH), HELP_INSERT, HELP_INSERT, true, null,
                "ctrl shift +");
        remarkToggleAction = new HqlBuilderAction(hql, this, REMARK_TOGGLE, true, REMARK_TOGGLE, getIcon(TEXT_INDENT), REMARK_TOGGLE, REMARK_TOGGLE,
                true, null, "ctrl shift SLASH");
        startQueryAction = new HqlBuilderAction(hql, this, START_QUERY, true, START_QUERY, getIcon(CONTROL_PLAY_BLUE), START_QUERY, START_QUERY,
                true, null, "ctrl ENTER");
        deleteInvertedSelectionAction = new HqlBuilderAction(hql, this, DELETE_INVERTED_SELECTION, true, DELETE_INVERTED_SELECTION,
                getIcon(TABLE_ROW_DELETE), DELETE_INVERTED_SELECTION, DELETE_INVERTED_SELECTION, true, null, "ctrl DELETE");

        // other
        editableResultsAction = new HqlBuilderAction(null, this, EDITABLE_RESULTS, true, EDITABLE_RESULTS, null, EDITABLE_RESULTS, EDITABLE_RESULTS,
                false, null, null, PERSISTENT_ID);
        editable_results();
    }

    protected void down() {
        parametersEDT.moveSelectedDown();
    }

    protected void up() {
        parametersEDT.moveSelectedUp();
    }

    protected void clear() {
        aliases.clear();
        resultsInfo.setText("");
        parametersEDT.removeAllRecords();
        clearParameter();
        hql.setText("");
        sql.setText("");
        scripts.clear();
        clearResults();
        propertypanel.add(ClientUtils.getPropertyFrame(SERIALIZABLE, false), BorderLayout.CENTER);
        propertypanel.revalidate();
        hql_sql_tabs.setForegroundAt(0, Color.gray);
        hql_sql_tabs.setForegroundAt(1, Color.gray);
    }

    private void clearParameter() {
        selectedQueryParameter = new QueryParameter();
        parameterBuilder.setText("");
        parameterName.setText("");
        parameterValue.setText("");
    }

    private void clearResults() {
        recordCount = 0;
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
            logger.error("{}", ex);
        }
    }

    private void importFromFavorites(QueryFavorite selection) {
        clear();
        importFromFavoritesNoQ(selection);
    }

    private void importFromFavoritesNoQ(QueryFavorite selection) {
        hql.setText(selection.getFull());

        if (selection.getParameters() != null) {
            for (QueryParameter p : selection.getParameters()) {
                String valueText = p.getValueText();
                if (StringUtils.isNotBlank(valueText)) {
                    Object val = GroovyCompiler.eval(valueText);
                    p.setValueTypeText(val).setValueText(valueText);
                }
                parametersEDT.addRecord(new EListRecord<QueryParameter>(p));
            }
        }
    }

    private void compile(final String text) {
        logger.info("compiling");
        selectedQueryParameter.setValue(null);
        try {
            Object val = GroovyCompiler.eval(text);
            selectedQueryParameter.setValueTypeText(val).setValueText(text);
        } catch (Exception ex2) {
            logger.error("{}", ex2);
        }
        logger.info("compiled: " + selectedQueryParameter);
        parameterValue.setText(selectedQueryParameter.toString());
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
                        logger.error("{}", ex);
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

            for (HibernateWebResolver.ClassNode node : getHibernateWebResolver().getClasses()) {
                String clazz = node.getId();
                String option = clazz.substring(clazz.lastIndexOf('.') + 1) + " (" + clazz.substring(0, clazz.lastIndexOf('.')) + ")";
                options.add(option);
            }

            for (String option : options) {
                list.addRecord(new EListRecord<String>(option));
            }

            Dimension size = new Dimension(500, 200);
            insertHelper.setPreferredSize(size);
            insertHelper.setSize(size);
            insertHelper.setMinimumSize(size);

            insertHelperList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if (EventHelper.isMouse1x1(e)) {
                            insertHelp();
                        }
                    } catch (Exception ex) {
                        logger.error("{}", ex);
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
        Point magicCaretPosition = hql.getCaret().getMagicCaretPosition();
        getInsertHelperClasses().show(hql, magicCaretPosition == null ? 10 : (int) magicCaretPosition.getX(),
                magicCaretPosition == null ? 10 : (int) magicCaretPosition.getY());
        insertHelperList.grabFocus();
    }

    private void helpInsertProperty(String before) {
        int pos = Math.max(0, Math.max(before.lastIndexOf(getNewline()), before.lastIndexOf(' ')));
        before = before.substring(pos + 1, before.length() - 1);
        String[] parts = before.split("\\Q.\\E");
        String key = aliases.get(parts[0]);

        List<String> propertyNames = hqlService.getPropertyNames(key, parts);

        JPopupMenu insertHelper2local = getInsertHelperProperties();

        insertPropertyHelper.removeAllRecords();

        for (String prop : new TreeSet<String>(propertyNames)) {
            if (!prop.startsWith("_")) {
                insertPropertyHelper.addRecord(new EListRecord<String>(prop));
            }
        }

        Point magicCaretPosition = hql.getCaret().getMagicCaretPosition();
        insertHelper2local.show(hql, magicCaretPosition == null ? 10 : (int) magicCaretPosition.getX(), magicCaretPosition == null ? 10
                : (int) magicCaretPosition.getY());
        insertPropertyHelper.grabFocus();
    }

    protected String getNewline() {
        return hqlService.getNewline();
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

        if (geselecteerd.contains(REMARKTAG) || geselecteerd.contains("//")) {
            sb.append(voor);
            sb.append(geselecteerd.replaceAll(REMARKTAG, "").replaceAll("//", ""));
            sb.append(na);
        } else {
            if (selectionStart == 0) {
                sb.append(REMARKTAG);
            } else {
                sb.append(voor);
            }

            String[] lijnen = geselecteerd.split(getNewline());

            for (int i = 0; i < (lijnen.length - 1); i++) {
                sb.append(lijnen[i]).append(getNewline()).append(REMARKTAG);
            }

            sb.append(lijnen[lijnen.length - 1]);

            sb.append(na);
        }

        return sb.toString();
    }

    /**
     * start app
     */
    public static void start(String[] args, HqlServiceClientLoader serviceLoader) {
        try {
            System.out.println("arguments: " + Arrays.asList(args));

            // zet look en feel gelijk aan default voor OS
            UIUtils.systemLookAndFeel();

            Preferences preferences = Preferences.userRoot().node(HqlBuilderFrame.PERSISTENT_ID);
            String lang = preferences.get(PERSISTENT_LOCALE, SystemSettings.getCurrentLocale().getLanguage());
            SystemSettings.setCurrentLocale(new Locale(lang));

            SplashHelper.setup();
            SplashHelper.step();

            Thread tt = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            SplashHelper.progress();
                            try {
                                Thread.sleep(200l);
                            } catch (InterruptedException ex) {
                                //
                            }
                        }
                    } catch (RuntimeException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            tt.setDaemon(true);
            tt.start();

            // problem in "all in one" setup
            // sending 'wrong' locale to Oracle gives exception
            // Oracle will now give English exceptions
            SystemSettings.setCurrentLocale(Locale.UK);
            HqlServiceClient service = serviceLoader.getHqlServiceClient();
            // now load users locale
            SystemSettings.setCurrentLocale(new Locale(lang));

            SplashHelper.update(service.getConnectionInfo());
            SplashHelper.step();

            if (!PROGRAM_DIR.exists()) {
                PROGRAM_DIR.mkdirs();
            }
            if (!FAVORITES_DIR.exists()) {
                FAVORITES_DIR.mkdirs();
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

            GroovyCompiler.eval("new Integer(0)"); // warm up Groovy

            SplashHelper.step();

            // maak frame en start start
            final HqlBuilderFrame hqlBuilder = new HqlBuilderFrame();
            hqlBuilder.preferences = preferences;
            hqlBuilder.hqlService = service;

            SplashHelper.step();

            hqlBuilder.startPre();

            SplashHelper.step();

            hqlBuilder.start();

            hqlBuilder.hql.grabFocus();

            // log connectie
            logger.info(hqlBuilder.hqlService.getConnectionInfo());

            SplashHelper.step();
            SplashHelper.end();

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
            logger.error("{}", ex);
            SplashHelper.end();
            ex.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, WordUtils.wrap("" + ex, 100), "Fatal Exception", JOptionPane.ERROR_MESSAGE);
        }
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
                logger.error("{}", ex);
            }
        } else {
            SystemTray.getSystemTray().remove(trayIcon);
        }
    }

    private synchronized void query() {
        executeQuery(null, false);
    }

    private void progressbarStop() {
        glass.setMessage(null);
        setGlassVisible(false);
    }

    private void progressbarStart(final String text) {
        glass.setMessage(text);
        setGlassVisible(true);
    }

    private void query(RowProcessor rowProcessor) {
        progressbarStart(HqlResourceBundle.getMessage("quering"));
        executeQuery(rowProcessor, false);
        progressbarStop();
        JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("done"));
    }

    private synchronized void executeQuery(final RowProcessor rowProcessor, final boolean failFast) {
        logger.debug("start query");
        final long start = System.currentTimeMillis();

        if (!startQueryAction.isEnabled()) {
            return;
        }

        final String hqlGetText = getHqlText();

        if (StringUtils.isBlank(hqlGetText)) {
            return;
        }

        preQuery();

        final int maxresults = rowProcessor == null ? (Integer) maximumNumberOfResultsAction.getValue() : Integer.MAX_VALUE;

        SwingWorker<ExecutionResult, Void> sw = new SwingWorker<ExecutionResult, Void>() {
            @Override
            protected ExecutionResult doInBackground() throws Exception {
                return doQuery(hqlGetText, maxresults);
            }

            @Override
            protected void done() {
                boolean retry = false;
                try {
                    ExecutionResult rv = get();
                    afterQuery(start, rv, rowProcessor);
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    Caret caret = hql.getCaret();
                    if (!failFast && caret.getDot() != caret.getMark() && cause != null && cause.getMessage() != null
                            && cause.getMessage().contains("java.lang.IllegalArgumentException node to traverse cannot be null!")) {
                        retry = true;
                    } else {
                        afterQuery(cause);
                    }
                } catch (Exception ex) {
                    afterQuery(ex);
                } finally {
                    postQuery();
                }
                if (retry) {
                    Caret caret = hql.getCaret();
                    caret.moveDot(caret.getMark());
                    executeQuery(rowProcessor, true);
                }
            }
        };

        sw.execute();
    }

    private void postQuery() {
        startQueryAction.setEnabled(true);
        progressbarStop();
    }

    private void preQuery() {
        startQueryAction.setEnabled(false);
        progressbarStart(HqlResourceBundle.getMessage("quering"));
        errorLocs.clear();
        errorString = null;
        sql.setText("");
        resultsInfo.setText("");
        clearResults();
        hql_sql_tabs.setForegroundAt(0, Color.gray);
        hql_sql_tabs.setForegroundAt(1, Color.gray);
        try {
            addLast(LAST);
        } catch (IOException ex) {
            logger.error("{}", ex);
        }
        hql.removeHighlights(syntaxErrorsHighlight);
        hilightSyntax();
    }

    private ExecutionResult doQuery(String hqlGetText, int maxresults) {
        return hqlService.execute(new QueryParameters(hqlGetText, maxresults, EList.convertRecords(parametersEDT.getRecords()).toArray(
                new QueryParameter[parametersEDT.getRecordCount()])));
    }

    private void afterQuery(Throwable ex) {
        hql_sql_tabs.setForegroundAt(0, Color.RED);
        hql_sql_tabs.setForegroundAt(1, Color.RED);

        String sqlString = sql.getText();
        if (ex instanceof java.util.concurrent.ExecutionException) {
            ex = ex.getCause();
        }
        String exceptionString = "";
        if (ex instanceof ServiceException) {
            exceptionString = ex.getMessage();
            errorString = ex.getMessage();
            ExecutionResult partialResult = ServiceException.class.cast(ex).getPartialResult();
            if (partialResult != null && partialResult.getSql() != null) {
                sqlString = partialResult.getSql();
            }
        } else if (ex instanceof SqlException) {
            errorString = ex.getMessage();
            SqlException sqlException = SqlException.class.cast(ex);
            if (sqlException.getSql() != null) {
                sqlString = sqlException.getSql();
            }
            exceptionString += getNewline() + sqlException.getState() + " - " + sqlException.getException();
        } else {
            errorString = ex.getMessage();
            exceptionString = ex.toString();
        }
        // hql.setToolTipText(exceptionString.trim());
        sqlString = formatSql(sqlString);
        if (StringUtils.isNotBlank(sqlString)) {
            exceptionString = exceptionString + getNewline() + "-----------------------------" + getNewline() + getNewline() + sqlString
                    + getNewline() + getNewline();
        }
        sql.setText(exceptionString.trim());
        sql.setCaret(0);
        clearResults();
        if (ex instanceof SyntaxException) {
            SyntaxException se = SyntaxException.class.cast(ex);
            errorString = se.getMessage();
            int p = errorString.indexOf("[");
            if (p != -1) {
                errorString = errorString.substring(0, p - 1);
            }
            hilightSyntaxException(se.getType(), se.getWrong(), se.getLine(), se.getCol());
        }
        logger.debug("end query");
    }

    private void afterQuery(long start, ExecutionResult rv, RowProcessor rowProcessor) throws Exception {
        sql.setText(formatSql(rv));

        aliases = rv.getFromAliases();

        List<?> list = rv.getResults() == null ? null : rv.getResults().getValue();
        ETableHeaders<List<Object>> headers = null;

        List<ETableRecord<List<Object>>> records = new ArrayList<ETableRecord<List<Object>>>();
        if (list != null) {
            for (Object o : list) {
                ETableRecordCollection<Object> record = null;
                List<Object> recordItems;

                if (o instanceof Object[]) {
                    recordItems = new ArrayList<Object>(Arrays.asList((Object[]) o));
                } else if (o instanceof Collection<?>) {
                    @SuppressWarnings("unchecked")
                    Collection<Object> o2 = (Collection<Object>) o;
                    recordItems = new ArrayList<Object>(o2);
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
                        boolean script = scripts.get(i) != null;
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
                        if ((rv.getQueryReturnAliases() == null) || String.valueOf(i).equals(rv.getQueryReturnAliases()[i])) {
                            try {
                                headers.add(html + (script ? "*" : "") + name + br + rv.getScalarColumnNames()[i][0] + (script ? "*" : "") + _html,
                                        type);
                            } catch (Exception ex) {
                                logger.error("{}", ex);

                                try {
                                    headers.add(html + (script ? "*" : "") + name + br + rv.getSqlAliases()[i] + (script ? "*" : "") + _html, type);
                                } catch (Exception ex2) {
                                    logger.error("{}", ex2);
                                    headers.add(html + (script ? "*" : "") + name + br + i + (script ? "*" : "") + _html, type);
                                }
                            }
                        } else {
                            headers.add(html + name + br + rv.getQueryReturnAliases()[i] + _html, type);
                        }
                    }

                    resultsEDT.setHeaders(headers);
                }

                records.add(record);
            }
        }
        resultsEDT.addRecords(records);
        hql_sql_tabs.setForegroundAt(0, Color.GREEN);
        hql_sql_tabs.setForegroundAt(1, Color.GREEN);

        resultsUnsafe.setAutoResizeMode(ETable.AUTO_RESIZE_OFF);
        for (int i = 0; i < resultsEDT.getColumnCount(); i++) {
            resultsUnsafe.packColumn(i, 2);
        }
        if (resizeColumnsAction.isSelected()) {
            resultsUnsafe.setAutoResizeMode(ETable.AUTO_RESIZE_ALL_COLUMNS);
        }
        if (rv.getSize() == 0) {
            resultsInfo.setText(HqlResourceBundle.getMessage("No results"));
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            String d = df.format(rv.getDuration() / 1000.0);
            resultsInfo.setText(HqlResourceBundle.getMessage("results in seconds", String.valueOf(rv.getSize()), d));
        }
        logger.info("duration: {} ms", rv.getDuration());
        logger.info("overhead-server: {} ms", rv.getOverhead());
        logger.info("overhead-client: {} ms", ((System.currentTimeMillis() - start - rv.getDuration())));
        logger.debug("end query");
    }

    private String formatSql(ExecutionResult rv) {
        String sqlString = rv.getSql();
        String[] queryReturnAliases = rv.getQueryReturnAliases();
        String[][] scalarColumnNames = rv.getScalarColumnNames();
        boolean doFormat = formatLinesAction.isSelected();
        boolean doRemoveJoins = removeJoinsAction.isSelected();
        boolean doReplaceProperties = replacePropertiesAction.isSelected();
        return formatSql(sqlString, queryReturnAliases, scalarColumnNames, doFormat, doRemoveJoins, doReplaceProperties);
    }

    private String formatSql(String sqlString) {
        return formatSql(sqlString, null, null, false, false, false);
    }

    private String formatSql(String sqlString, String[] queryReturnAliases, String[][] scalarColumnNames, boolean doFormat, boolean doRemoveJoins,
            boolean doReplaceProperties) {
        if (StringUtils.isBlank(sqlString)) {
            return sqlString;
        }
        if (formatSqlAction.isSelected()) {
            sqlString = hqlService.cleanupSql(sqlString, queryReturnAliases, scalarColumnNames, doReplaceProperties, doFormat, doRemoveJoins);
        }
        return sqlString;
    }

    private void hilightSyntaxException(SyntaxExceptionType syntaxExceptionType, String wrong, int line, int col) {
        hql.removeHighlights(syntaxErrorsHighlight);
        String hqltext = this.hql.getText();
        ArrayList<Integer> poss = new ArrayList<Integer>();
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
                        if (!(('a' <= nextChar && nextChar <= 'z') || ('A' <= nextChar && nextChar <= 'Z'))) {
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
                Caret caret = hql.getCaret();
                int mark = Math.min(caret.getDot(), caret.getMark());
                int lnskip = hqltext.substring(0, mark).split(LINESEPERATOR).length - 1;
                String[] lines = hqltext.split(LINESEPERATOR);
                int pos = 0;
                for (int i = 0; i < lnskip + line - 1; i++) {
                    pos += lines[i].length() + 1;
                }
                pos += col - 1;
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
                this.hql.addHighlight(pos, pos + wrong.length(), syntaxErrorsHighlight);
                List<Highlight> after = new ArrayList<Highlighter.Highlight>(Arrays.asList(this.hql.getHighlighter().getHighlights()));
                after.removeAll(before);
                errorLocs.addAll(after);
            } catch (Exception ex) {
                logger.error("{}", ex);
            }
        }
    }

    private String getHqlText() {
        Caret caret = hql.getCaret();
        String hqlstring;
        int p1 = caret.getDot();
        int p2 = caret.getMark();
        if (p1 != p2) {
            if (p1 > p2) {
                int tmp = p2;
                p2 = p1;
                p1 = tmp;
            }
            hqlstring = hql.getText().substring(p1, p2);
            // remove trailing ;
            Matcher m = Pattern.compile("([^;]++);\\s++$").matcher(hqlstring);
            if (m.find()) {
                hqlstring = m.group(1);
            }
        } else {
            hqlstring = hql.getText();
            int p3 = hqlstring.substring(0, p1).lastIndexOf(";");
            if (p3 == -1 || p3 > p1) {
                p3 = 0;
            } else {
                p3++;
            }
            int p4 = hqlstring.indexOf(";", p1);
            if (p4 == -1) {
                p4 = hqlstring.length();
                if (StringUtils.isBlank(hqlstring.substring(p3, p4))) {
                    p4 = p3 - 1;
                    p3 = hqlstring.substring(0, p4).lastIndexOf(";");
                    if (p3 == -1) {
                        p3 = 0;
                    }
                }
            }
            final Point viewPosition = hqlsp.getViewport().getViewPosition();
            if (addSelectExecutedHql.isSelected()) {
                hql.setSelectionStart(p3);
                hql.setSelectionEnd(p4);
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    hqlsp.getViewport().setViewPosition(viewPosition);
                }
            });
            hqlstring = hqlstring.substring(p3, p4);
        }

        String lines[] = hqlstring.split(LINESEPERATOR);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            int dash = line.indexOf(REMARKTAG);
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

    /**
     * start
     */
    public void start() throws IOException {
        reloadColor();

        syntaxHighlight.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0F, new float[] { 1.0F }, 0.F));

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

        resultPanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("results")));

        parameterspanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("parameters")));

        propertypanel.setBorder(BorderFactory.createTitledBorder(HqlResourceBundle.getMessage("properties")));

        hqlsp = new JScrollPane(hql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        hql.withLineNumbers(hqlsp);
        font(hql, 0);
        hql_sql_tabs.addTab("HQL", hqlsp);
        hql_sql_tabs.addTab("SQL", new JScrollPane(sql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));

        {
            EList<String> searchresults = new EList<String>(new EListConfig().setBackgroundRenderer(backgroundRenderer).setSortable(false));
            searchresults.setFixedCellHeight(20);
            final EList<String> searchresultsEDTSafe = searchresults.stsi();
            final ECheckBox searchClass = new ECheckBox(new ECheckBoxConfig("class", true));
            final ECheckBox searchField = new ECheckBox(new ECheckBoxConfig("field", true));
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
                    return org.tools.hqlbuilder.common.icons.CommonIcons.getIcon(ZOOM);
                }

                @Override
                protected String getAction() {
                    return "search-index";
                }

                @Override
                protected void doAction() {
                    searchresultsEDTSafe.removeAllRecords();
                    try {
                        List<String> results;
                        if (searchClass.isSelected() != searchField.isSelected()) {
                            if (searchClass.isSelected()) {
                                results = hqlService.search(getInput().getText(), "class", (Integer) maximumNumberOfSearchResultsAction.getValue());
                            } else {
                                results = hqlService.search(getInput().getText(), "field", (Integer) maximumNumberOfSearchResultsAction.getValue());
                            }
                        } else {
                            results = hqlService.search(getInput().getText(), null, (Integer) maximumNumberOfSearchResultsAction.getValue());
                        }
                        searchresultsEDTSafe.addRecords(EList.convert(results));
                    } catch (UnsupportedOperationException ex) {
                        JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("lucene.unavailable"), "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        logger.error("{}", ex);
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
            infopanel.add(searchresults.addRowHeader(new JScrollPane(searchresults, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)), BorderLayout.CENTER);
            hql_sql_tabs.addTab(HqlResourceBundle.getMessage("Lucene search"), infopanel);
        }

        hql_sql_tabs.setForegroundAt(0, Color.gray);
        hql_sql_tabs.setForegroundAt(1, Color.gray);

        Dimension bd = new Dimension(24, 24);
        EToolBarButton saveButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), saveAction));
        saveButton.setText("");
        EToolBarButton setNullButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), setNullAction));
        setNullButton.setText("");
        EToolBarButton toTextButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), toTextAction));
        toTextButton.setText("");
        EToolBarButton removeButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), removeAction));
        removeButton.setText("");
        EToolBarButton upButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), upAction));
        upButton.setText("");
        EToolBarButton addParameterButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), addParameterAction));
        addParameterButton.setText("");
        EToolBarButton downButton = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd), downAction));
        downButton.setText("");
        EToolBarButton importParametersFromTextBtn = new EToolBarButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer(bd),
                importParametersAction));
        importParametersFromTextBtn.setText("");

        parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("name") + ": "));
        parameterspanel.add(parameterName, "grow");
        parameterspanel.add(saveButton, "");

        parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("value") + ": "));
        parameterspanel.add(parameterBuilder, "grow");
        parameterspanel.add(toTextButton, "");

        parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("compiled") + ": "));
        parameterspanel.add(parameterValue, "grow");
        parameterspanel.add(setNullButton, "");

        parameterspanel.add(parametersUnsafe.addRowHeader(new JScrollPane(parametersUnsafe)), "spanx 2, spany 4, growx, growy");
        parameterspanel.add(addParameterButton, "bottom, shrinky");
        parameterspanel.add(upButton, "shrinky");
        parameterspanel.add(removeButton, "shrinky");
        parameterspanel.add(downButton, "top, shrinky, wrap");

        parameterspanel.add(new ELabel(HqlResourceBundle.getMessage("import parameters") + ": "), "growx, shrinky");
        parameterspanel.add(importParametersFromTextF, "growx, shrinky");
        parameterspanel.add(importParametersFromTextBtn, "growx, shrinky");

        parameterspanel.add(new ELabel(), "growy, growx, spanx 3"); // filler

        maxResults.setMinimumSize(new Dimension(80, 22));
        maxResults.setPreferredSize(new Dimension(80, 22));
        resultsInfo.setMinimumSize(new Dimension(300, 22));
        resultsInfo.setPreferredSize(new Dimension(300, 22));

        JPanel resultsStatusPanel = new JPanel(new FlowLayout());
        resultsStatusPanel.add(resultsInfo);
        resultsStatusPanel.add(maxResults);
        resultPanel.add(resultsStatusPanel, BorderLayout.SOUTH);

        switch_layout();
        framepanel.add(normalContentPane, BorderLayout.CENTER);

        parameterBuilder.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                // extra help enkel in geval van groovy
                String text = parameterBuilder.getText();

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
                    parameterBuilder.setText(text);
                } catch (Exception ex2) {
                    //
                }

                compile(text);
            }
        });
        hql.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                //
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

                if (addEndBraceAction.isSelected() && EventHelper.keyEvent(e, '(') && e.getModifiers() == 0) {
                    int pos = hql.getCaretPosition();
                    hql.setText(hql.getText().substring(0, pos) + ')' + hql.getText().substring(pos));
                    hql.setCaretPosition(pos);
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
                    propertypanel.add(ClientUtils.getPropertyFrame(SERIALIZABLE, editableResultsAction.isSelected()), BorderLayout.CENTER);
                } else {
                    PropertyPanel propertyFrame = ClientUtils.getPropertyFrame((Serializable) data, editableResultsAction.isSelected());
                    propertyFrame.setHqlService(hqlService);
                    propertypanel.add(font(propertyFrame, null), BorderLayout.CENTER);
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

        hql_sql_tabs_panel.add(hql_sql_tabs, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        EToolBarButtonCustomizer etbc = new EToolBarButtonCustomizer();
        {
            JToolBar hqltools = new JToolBar(javax.swing.SwingConstants.VERTICAL);
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, startQueryAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, wizardAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, clearAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, findParametersAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, favoritesAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, addToFavoritesAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, formatAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, namedQueryAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, clipboardExportAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, clipboardImportAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, helpInsertAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, remarkToggleAction)));
            hqltools.add(new EToolBarButton(new EToolBarButtonConfig(etbc, deleteInvertedSelectionAction)));
            hql_sql_tabs_panel.add(hqltools, BorderLayout.WEST);
        }
        {
            JToolBar resultstools = new JToolBar(javax.swing.SwingConstants.VERTICAL);
            resultstools.add(new EButton(new EButtonConfig(etbc, hibernatePropertiesAction)));
            resultstools.add(new EButton(new EButtonConfig(etbc, objectTreeAction)));
            resultstools.add(new EButton(new EButtonConfig(etbc, deleteObjectAction)));
            resultstools.add(new EButton(new EButtonConfig(etbc, copyCellAction)));
            resultstools.add(new EButton(new EButtonConfig(etbc, executeScriptOnColumnAction)));
            resultPanel.add(resultstools, BorderLayout.WEST);
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
                formatSqlOptionsMenu.add(new JCheckBoxMenuItem(removeJoinsAction));
                formatSqlOptionsMenu.add(new JCheckBoxMenuItem(formatLinesAction));
                formatSqlOptionsMenu.add(new JCheckBoxMenuItem(replacePropertiesAction));
            }
            {
                JMenu addmi = new JMenu(HqlResourceBundle.getMessage("language"));
                ButtonGroup lanGroup = new ButtonGroup();

                List<Locale> locales = new ArrayList<Locale>();

                for (Locale locale : Locale.getAvailableLocales()) {
                    URL bundle = HqlBuilderFrame.class.getClassLoader().getResource("HqlResourceBundle_" + locale + ".properties");
                    logger.debug("{}>{}", locale, bundle);
                    if (null != bundle) {
                        locales.add(locale);
                    }
                }

                locales.remove(DEFAULT_LOCALE);

                Collections.sort(locales, new Comparator<Locale>() {
                    @Override
                    public int compare(Locale o1, Locale o2) {
                        return new CompareToBuilder().append(o1.getDisplayLanguage(o1), o2.getDisplayLanguage(o2))
                                .append(o1.getDisplayCountry(o1), o2.getDisplayCountry(o2)).toComparison();
                    }
                });

                locales.add(0, DEFAULT_LOCALE);

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
                    if (locale.equals(DEFAULT_LOCALE) || locale.getLanguage().equals(SystemSettings.getCurrentLocale().getLanguage())) {
                        lanMenu.setSelected(true);
                    }
                    lanMenu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // SystemSettings.setCurrentLocale(loc);
                            preferences.put(PERSISTENT_LOCALE, loc.toString());
                            JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("change visible after restart"), "",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }

                settingsMenu.add(addmi);
            }
            {
                JMenu addmi = new JMenu(HqlResourceBundle.getMessage("additional settings"));
                addmi.add(new JCheckBoxMenuItem(highlightSyntaxAction));
                addmi.add(new JMenuItem(highlightColorAction));
                addmi.add(new JCheckBoxMenuItem(resizeColumnsAction));
                addmi.add(maximumNumberOfResultsAction);
                addmi.add(fontAction);
                addmi.add(new JCheckBoxMenuItem(alwaysOnTopAction));
                addmi.add(new JCheckBoxMenuItem(editableResultsAction));
                addmi.add(new JMenuItem(searchColorAction));
                if (SystemTray.isSupported()) {
                    addmi.add(new JCheckBoxMenuItem(systrayAction));
                }
                addmi.add(new JCheckBoxMenuItem(addEndBraceAction));
                addmi.add(new JCheckBoxMenuItem(addShowErrorTooltip));
                addmi.add(new JCheckBoxMenuItem(addSelectExecutedHql));
                addmi.add(maximumNumberOfSearchResultsAction);
                settingsMenu.add(addmi);
            }
            {
                settingsMenu.addSeparator();
            }
            {
                JMenuItem resetMi = new JMenuItem(HqlResourceBundle.getMessage("reset settings"));
                resetMi.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fontAction.setWarnRestart(false);

                        maximumNumberOfResultsAction.setValue(100);
                        maximumNumberOfSearchResultsAction.setValue(2000);
                        fontAction.setValue(ClientUtils.getDefaultFont());
                        searchColorAction.setValue(new Color(245, 225, 145));
                        highlightColorAction.setValue(new Color(0, 0, 255));
                        removeJoinsAction.setSelected(true);
                        formatLinesAction.setSelected(true);
                        replacePropertiesAction.setSelected(true);
                        resizeColumnsAction.setSelected(true);
                        formatSqlAction.setSelected(true);
                        systrayAction.setSelected(true);
                        highlightSyntaxAction.setSelected(true);
                        alwaysOnTopAction.setSelected(false);
                        editableResultsAction.setSelected(false);
                        switchLayoutAction.setSelected(true);
                        addEndBraceAction.setSelected(true);
                        addShowErrorTooltip.setSelected(true);
                        addSelectExecutedHql.setSelected(true);
                        fontAction.setWarnRestart(true);

                        editable_results();

                        JOptionPane.showMessageDialog(frame, HqlResourceBundle.getMessage("change visible after restart"), "",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                settingsMenu.add(resetMi);
            }
            menuBar.add(settingsMenu);
        }
        {
            JMenu hqlmenu = new JMenu("HQL");
            hqlmenu.add(new JMenuItem(startQueryAction));
            hqlmenu.add(new JMenuItem(wizardAction));
            hqlmenu.add(new JMenuItem(clearAction));
            hqlmenu.add(new JMenuItem(findParametersAction));
            hqlmenu.add(new JMenuItem(favoritesAction));
            hqlmenu.add(new JMenuItem(addToFavoritesAction));
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
            helpmenu.add(new JMenuItem(luceneQuerySyntaxAction));
            helpmenu.add(new JMenuItem(helpAction));
            helpmenu.add(new JMenuItem(versionsAction));
            helpmenu.add(new JMenuItem(aboutAction));
            menuBar.add(helpmenu);
        }

        getVersion();

        frame.setTitle(NAME + " v" + version + " - " + hqlService.getConnectionInfo() + " - " + hqlService.getProject()
                + (hqlService.getServiceUrl() == null ? "" : " - " + hqlService.getServiceUrl()));
        frame.setVisible(true);
        frame.setSize(new Dimension(1024, 768));
        frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        frame.setIconImage(HqlBuilderImages.getIcon().getImage());
        frame.setGlassPane(getGlass(frame));
        frame.setAlwaysOnTop(Boolean.TRUE.equals(alwaysOnTopAction.getValue()));
    }

    public void getVersion() {
        try {
            Properties p = new Properties();
            p.load(getClass().getClassLoader().getResourceAsStream("META-INF/maven/org.tools.hql-builder/hql-builder-client/pom.properties"));
            version = p.getProperty("version").toString();
        } catch (Exception ex) {
            try {
                version = org.w3c.dom.Node.class.cast(
                        ClientUtils.getFromXml(new FileInputStream("pom.xml"), "project", "/default:project/default:version/text()")).getNodeValue();
            } catch (Exception ex2) {
                try {
                    version = org.w3c.dom.Node.class.cast(
                            ClientUtils.getFromXml(new FileInputStream("pom.xml"), "project",
                                    "/default:project/default:parent/default:version/text()")).getNodeValue();
                } catch (Exception ex3) {
                    version = HqlResourceBundle.getMessage("latest");
                }
            }
        }
    }

    protected void startPre() {
        loadFavorites();
    }

    private void layout(Dimension size) {
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
            split0.setDividerLocation((int) (h * 1.0 / 3.0));
            split1.setDividerLocation((int) (w * 1.0 / 3.0));
            split2.setDividerLocation((int) (w * 1.0 / 3.0));
        } else {
            // hql_sql_tabs .... parameterspanel
            // resultPanel ..... propertypanel
            split0.setDividerLocation((int) (h * 1.0 / 3.0));
            split1.setDividerLocation((int) (w * 2.0 / 3.0));
            split2.setDividerLocation((int) (w * 2.0 / 3.0));
        }
    }

    protected void switch_layout() {
        logger.info("change layout");

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

            split1.setLeftComponent(hql_sql_tabs_panel);
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

            split1.setLeftComponent(hql_sql_tabs_panel);
            split1.setRightComponent(parameterspanel);
            split2.setLeftComponent(resultPanel);
            split2.setRightComponent(propertypanel);

            normalContentPane.add(split0, BorderLayout.CENTER);
        }

        layout(null);

        normalContentPane.setVisible(true);
    }

    private void createSqlPopupMenu() {
        // EComponentPopupMenu.installPopupMenu((ReadableComponent) new TextComponentWritableComponent(sql));
    }

    private void createHqlPopupMenu() {
        JPopupMenu hqlpopupmenu = hql.getComponentPopupMenu();
        hqlpopupmenu.addSeparator();
        hqlpopupmenu.add(new JMenuItem(startQueryAction));
        hqlpopupmenu.add(new JMenuItem(wizardAction));
        hqlpopupmenu.add(new JMenuItem(clearAction));
        hqlpopupmenu.add(new JMenuItem(findParametersAction));
        hqlpopupmenu.add(new JMenuItem(favoritesAction));
        hqlpopupmenu.add(new JMenuItem(addToFavoritesAction));
        hqlpopupmenu.add(new JMenuItem(formatAction));
        hqlpopupmenu.add(new JMenuItem(namedQueryAction));
        hqlpopupmenu.add(new JMenuItem(clipboardExportAction));
        hqlpopupmenu.add(new JMenuItem(clipboardImportAction));
        hqlpopupmenu.add(new JMenuItem(helpInsertAction));
        hqlpopupmenu.add(new JMenuItem(remarkToggleAction));
        hqlpopupmenu.add(new JMenuItem(deleteInvertedSelectionAction));
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
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame, HqlResourceBundle.getMessage("exit_confirmation"), "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
            logger.info("exiting with 0");
            System.exit(0);
        }
    }

    protected void force_exit() {
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame, HqlResourceBundle.getMessage("force_exit_confirmation"), "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
            logger.info("exiting with -1");
            System.exit(-1);
        }
    }

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
                recordCount = 0;
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
                            recordCount++;
                            if (recordCount != 0 && recordCount % 100 == 0) {
                                logger.info(recordCount + " records");
                            }
                        } catch (Exception ex) {
                            logger.error("{}", ex);
                        }
                    }
                });
                logger.info("{} records", recordCount);
                br.flush();
                br.close();
            }
        } catch (Exception ex) {
            logger.error("{}", ex);
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
            logger.error("{}", ex);
        }
    }

    protected void format() {
        try {
            hql.setText(format(hql.getText()));
            hql.setCaret(0);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    private String format(String text) {
        return hqlService.makeMultiline(hqlService.removeBlanks(text.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ')));
    }

    protected void copy() {
        try {
            int start = hql.getCaret().getDot();
            int end = hql.getCaret().getMark();
            String string = hql.getDocument().getText(Math.min(start, end), Math.max(start, end) - Math.min(start, end));
            clipboard.setContents(new StringSelection(string), getClipboardOwner());
        } catch (Exception ex) {
            logger.error("{}", ex);
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
        } catch (Exception ex) {
            logger.error("{}", ex);
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
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    private static List<QueryParameter> convertParameterString(String map) {
        map = map.trim();
        if (map.startsWith("[") && map.endsWith("]")) {
            map = map.substring(1, map.length() - 1);
        } else {
            if (map.startsWith("{") && map.endsWith("}")) {
                map = map.substring(1, map.length() - 1);
            } else {
                return new ArrayList<QueryParameter>();
            }
        }
        List<String> parts = new ArrayList<String>();
        String splitted = null;
        for (String part : map.split(",")) {
            part = part.trim();
            if (part.contains("[")) {
                splitted = part;
            } else if (splitted == null) {
                parts.add(part);
            } else {
                splitted = splitted + "," + part;
                if (part.contains("]")) {
                    parts.add(splitted);
                    splitted = null;
                }
            }
        }
        List<QueryParameter> qps = new ArrayList<QueryParameter>();
        for (int i = 0; i < parts.size(); i++) {
            String el = parts.get(i).trim();
            try {
                logger.debug(el);
                if (el.contains("=")) {
                    String[] p = el.split("=");
                    Object val = GroovyCompiler.eval(p[1]);
                    logger.debug("{}={}={}", p[0], val.getClass(), val);
                    qps.add(new QueryParameter().setName(p[0]).setValueText(p[1]).setType(val.getClass().getName()).setValue(val));
                } else {
                    Object val = GroovyCompiler.eval(el);
                    logger.debug("{}={}", val.getClass(), val);
                    qps.add(new QueryParameter().setIndex(i + 1).setValueText(el).setType(val.getClass().getName()).setValue(val));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return qps;
    }

    protected void import_parameters() {
        String m = importParametersFromTextF.getText();
        importParametersFromTextF.setText("");

        for (QueryParameter qp : convertParameterString(m)) {
            boolean exists = false;
            if (qp.getName() == null) {
                // just add at bottom in order
            } else {
                for (EListRecord<QueryParameter> rec : parametersEDT.getRecords()) {
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
                parametersEDT.addRecord(new EListRecord<QueryParameter>(qp));
            }
        }
    }

    protected void to_text() {
        if (parameterBuilder.getText().startsWith("'") && parameterBuilder.getText().endsWith("'")) {
            return;
        }
        parameterBuilder.setText("'" + parameterBuilder.getText() + "'");
        save();
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

    public <T extends JComponent> T font(T comp, Integer size, Integer style) {
        Font f = getFont();
        if (size != null && f.getSize() != size) {
            f = f.deriveFont(f.getSize() + (float) size);
        }
        if (style != null) {
            f = f.deriveFont(style);
        }
        comp.setFont(f);
        return comp;
    }

    public <T extends JComponent> T font(T comp, Integer size) {
        return font(comp, size, null);
    }

    protected void start_query() {
        try {
            query();
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    protected void export__copy_hql_as_java_to_clipboard() {
        try {
            StringBuilder sb = new StringBuilder("String hql = \"\";" + getNewline());

            for (String line : hql.getText().replaceAll("\r\n", getNewline()).split(getNewline())) {
                if (!line.startsWith(REMARKTAG) && !line.startsWith("//")) {
                    sb.append("hql +=\" ").append(line).append("\";");
                } else {
                    sb.append("// ").append(line);
                }

                sb.append(getNewline());
            }

            clipboard.setContents(new StringSelection(sb.toString()), getClipboardOwner());
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    protected void wizard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HqlWizard(new HqlWizardListener() {
                    @Override
                    public void query(String query) {
                        hql.setText(query);
                    }
                }, frame, getHibernateWebResolver());
            }
        }).start();
    }

    protected void help() {
        try {
            Desktop.getDesktop().browse(URI.create(ClientUtils.getHelpUrl()));
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    protected void hibernate_documentation() {
        try {
            Desktop.getDesktop().browse(URI.create(hqlService.getHibernateHelpURL()));
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    protected void hql_documentation() {
        try {
            Desktop.getDesktop().browse(URI.create(hqlService.getHqlHelpURL()));
        } catch (Exception ex) {
            logger.error("{}", ex);
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
                x = GroovyCompiler.eval(script, x);
                resultsEDT.setValueAt(x, row, selectedCol);
            } catch (Exception ex) {
                logger.error("{}", ex);
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
        if (glass != null) {
            glass.setVisible(v);
        }
    }

    protected void maximum_number_of_results() {
        Object newValue = JOptionPane.showInputDialog(frame, HqlResourceBundle.getMessage(MAXIMUM_NUMBER_OF_RESULTS),
                String.valueOf(maximumNumberOfResultsAction.getValue()));
        if (newValue != null) {
            int max = Integer.parseInt(String.valueOf(newValue));
            maximumNumberOfResultsAction.setValue(max);
            setMaxResults(max);
        }
    }

    protected void maximum_number_of_search_results() {
        Object newValue = JOptionPane.showInputDialog(frame, HqlResourceBundle.getMessage(MAXIMUM_NUMBER_OF_SEARCH_RESULTS),
                String.valueOf(maximumNumberOfSearchResultsAction.getValue()));
        if (newValue != null) {
            int max = Integer.parseInt(String.valueOf(newValue));
            maximumNumberOfSearchResultsAction.setValue(max);
            setMaxResults(max);
        }
    }

    public void setMaxResults(int newValue) {
        maxResults.setText(" / " + newValue);
        maxResults.setForeground(newValue > 500 ? Color.RED : Color.BLACK);
    }

    protected void font() {
        Font font = EFontChooser.showDialog((JComponent) frame.getContentPane(), getFont().getFamily());
        if (font == null) {
            return;
        }
        fontAction.setValue(font);
    }

    public Font getFont() {
        return (Font) fontAction.getValue();
    }

    protected <T> T get(Object o, String path, Class<T> t) {
        return t.cast(new ObjectWrapper(o).get(path));
    }

    protected void format_sql() {
        formatSqlOptionsMenu.setEnabled(formatSqlAction.isSelected());
    }

    protected void add_to_favorites() {
        try {
            if (!hql_sql_tabs.getForegroundAt(1).equals(Color.GREEN)) {
                if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(frame, HqlResourceBundle.getMessage("no query"),
                        HqlResourceBundle.getMessage("add to favorites"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)) {
                    return;
                }
            }
            String name = JOptionPane.showInputDialog(frame.getContentPane(), HqlResourceBundle.getMessage("add to favorites"),
                    HqlResourceBundle.getMessage("favorite name"));
            addLast(name);
        } catch (Exception ex) {
            logger.error("{}", ex);
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
        QueryFavoriteUtils.save(hqlService.getProject(), name, favorite);
    }

    private void loadFavorites() {
        QueryFavorite last = QueryFavoriteUtils.load(hqlService.getProject(), favorites);
        if (last != null) {
            importFromFavoritesNoQ(last);
        }

    }

    protected void always_on_top() {
        boolean alwaysOnTop = Boolean.TRUE.equals(alwaysOnTopAction.getValue());
        frame.setAlwaysOnTop(alwaysOnTop);
    }

    protected void delete_object() {
        try {
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
            Serializable bean = (Serializable) resultsEDT.getModel().getValueAt(row, col);
            hqlService.delete(bean);
            resultsEDT.getModel().setValueAt(null, row, col);
            propertypanel.removeAll();
            propertypanel.revalidate();
            propertypanel.repaint();
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    protected void help_insert() {
        try {
            helpInsert();
            // scheduleQuery(null, false);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    protected void remark_toggle() {
        int selectionStart = hql.getSelectionStart();
        int selectionEnd = hql.getSelectionEnd();
        String hqltext = hql.getText();

        hqltext = remarkToggle(hqltext, selectionStart, selectionEnd);
        hql.setText(hqltext);
        hql.setCaret(Math.min(selectionStart, selectionEnd));
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
        new ObjectTree(this, hqlService, bean, editableResultsAction.isSelected()).setIconImage(frame.getIconImage());
        progressbarStop();
    }

    protected void resize_columns() {
        //
    }

    private Set<String> reservedKeywords;

    private Set<String> getReservedKeywords() {
        if (reservedKeywords == null) {
            reservedKeywords = hqlService.getReservedKeywords();
        }
        return reservedKeywords;
    }

    private void hilightSyntax() {
        this.hql.removeHighlights(syntaxHighlight);

        if (!highlightSyntaxAction.isSelected()) {
            return;
        }

        LinkedList<int[]> pos = new LinkedList<int[]>();

        for (String kw : getReservedKeywords()) {
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
                if (pair1[1] + 1 == pair2[0] && (hqltext.charAt(pair1[1]) == ' ' || hqltext.charAt(pair1[1]) == '\t')) {
                    pair1[1] = pair2[1];
                    pos.remove(pair2);
                } else {
                    pair1 = pair2;
                }
            }
            for (int[] p : pos) {
                try {
                    this.hql.addHighlight(p[0], p[1], syntaxHighlight);
                } catch (Throwable ex) {
                    logger.error("{}", ex);
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

    private boolean isSeperator(char c) {
        return Character.isWhitespace(c) || c == '\'' || c == '(' || c == ')' || c == ',' || c == '-' || c == '/';
    }

    protected void highlight_color() {
        Color color = getHighlightColor();
        color = JColorChooser.showDialog(null, HqlResourceBundle.getMessage("Choose HQL highlight color"), color);
        highlightColorAction.setValue(color);

        this.hql.removeHighlights(syntaxErrorsHighlight);
        this.hql.removeHighlights(syntaxHighlight);
        this.hql.removeHighlights(bracesHighlight);

        syntaxHighlight.setColor(color);
        bracesHighlight.setColor(color);
    }

    private Color getHighlightColor() {
        return highlightColorAction.getValue() == null ? new Color(0, 0, 255) : (Color) highlightColorAction.getValue();
    }

    private Color chooseColor() {
        Color color = getSearchColor();
        color = JColorChooser.showDialog(frame, HqlResourceBundle.getMessage("Choose search highlight color"), color);
        return color;
    }

    protected void search_color() {
        Color chooseColor = chooseColor();
        if (chooseColor != null) {
            searchColorAction.setValue(applyColor(chooseColor));
        }
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
            Desktop.getDesktop().browse(URI.create(hqlService.getLuceneHelpURL()));
        } catch (Exception ex) {
            logger.error("{}", ex);
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
        List<String> propertyNames = hqlService.getProperties(classname);
        StringBuilder sb = new StringBuilder();
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

    private void hilightBraces(String hqltext) {
        hql.removeHighlights(bracesHighlight);

        if (!highlightSyntaxAction.isSelected()) {
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
                    this.hql.addHighlight(caret, caret + 1, bracesHighlight);
                    this.hql.addHighlight(match, match + 1, bracesHighlight);
                } catch (BadLocationException ex) {
                    logger.error("{}", ex);
                }
            }
        } catch (Exception ex) {
            //
        }
    }

    protected void load_named_query() {
        final Map<String, String> namedQueries = hqlService.getNamedQueries();
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

    public void find_parameters() {
        parametersEDT.removeAllRecords();
        for (QueryParameter p : hqlService.findParameters(getHqlText())) {
            parametersEDT.addRecord(new EListRecord<QueryParameter>(p));
        }
    }

    private HibernateWebResolver getHibernateWebResolver() {
        if (hibernateWebResolver == null) {
            hibernateWebResolver = hqlService.getHibernateWebResolver();
        }
        return this.hibernateWebResolver;
    }

    protected void delete_inverted_selection() {
        Caret caret = hql.getCaret();
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
        hql.setText(hql.getText().substring(p1, p2));
        hql.setCaret(0);
    }

    protected void add_parameter() {
        ingoreParameterListSelectionListener = true;

        parametersEDT.clearSelection();
        clearParameter();

        ingoreParameterListSelectionListener = false;
    }

    protected void remove() {
        ingoreParameterListSelectionListener = true;

        if (parametersEDT.getSelectedRecords().size() == 0) {
            parametersEDT.removeAllRecords();
        } else {
            parametersEDT.removeSelectedRecords();
        }
        clearParameter();

        ingoreParameterListSelectionListener = false;
    }

    protected void save() {
        ingoreParameterListSelectionListener = true;

        String text = parameterBuilder.getText();
        String name = (parameterName.getText().length() > 0) ? parameterName.getText() : null;
        Object value = selectedQueryParameter.getValue();

        boolean contains = false;
        for (EListRecord<QueryParameter> record : parametersEDT.getRecords()) {
            if (record.get().equals(selectedQueryParameter)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            EListRecord<QueryParameter> record = new EListRecord<QueryParameter>(selectedQueryParameter);
            parametersEDT.addRecord(record);
            parametersEDT.setSelectedRecord(record);
        }

        selectedQueryParameter.setValueText(text);
        selectedQueryParameter.setName(name);
        selectedQueryParameter.setValue(value);
        selectedQueryParameter.setIndex(null);

        ingoreParameterListSelectionListener = false;

        parametersEDT.repaint();
    }

    private void parameterSelected() {
        if (ingoreParameterListSelectionListener) {
            return;
        }

        EListRecord<QueryParameter> selected = parametersEDT.getSelectedRecord();

        if (selected == null) {
            clearParameter();
            return;
        }

        selectedQueryParameter = parametersEDT.getSelectedRecord().get();
        parameterValue.setText(selectedQueryParameter.toString());
        parameterName.setText((selectedQueryParameter.getName() == null) ? "" : selectedQueryParameter.getName());
        parameterBuilder.setText(selectedQueryParameter.getValueText());
        selectedQueryParameter.getIndex();
    }

    protected void set_null() {
        EListRecord<QueryParameter> selected = parametersEDT.getSelectedRecord();
        if (selected == null) {
            return;
        }
        parameterBuilder.setText("");
        save();
    }

    protected void about() {
        try {
            String latest = "?";
            String u = "?";
            try {
                u = getText(PROJECT_META);
                org.w3c.dom.Text o = (org.w3c.dom.Text) CommonUtils.getFromXml(new ByteArrayInputStream(u.getBytes()), "metadata",
                        "/metadata/versioning/release/text()");
                latest = o.getData();
            } catch (Exception ex) {
                logger.error("{}", ex);
            }

            final JDialog d = new JDialog(frame, HqlResourceBundle.getMessage("about"), true);
            JPanel cp = new JPanel(new MigLayout("insets 0 0 0 0", "10[]", "5[]5[]5[]5"));
            Container contentPane = d.getContentPane();
            contentPane.setLayout(new BorderLayout(0, 0));
            contentPane.add(cp);

            boolean upToDate = "?".equals(latest) || version.compareTo(latest) >= 0;

            cp.add(new JLabel(new ImageIcon(HqlBuilderImages.getLogo())), "dock north");
            cp.add(font(
                    new ELabel(HqlResourceBundle.getMessage("versioning", version, latest,
                            HqlResourceBundle.getMessage(String.valueOf(upToDate), false))), 14), "wrap");
            // if (!upToDate) {
            cp.add(font(new EURILabel(URI.create(downloadLatestURI), HqlResourceBundle.getMessage("download latest")), 14), "wrap");
            // }

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
            d.setLocationRelativeTo(frame);
            d.setVisible(true);
        } catch (Exception ex) {
            logger.error("{}", ex);
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

    protected void versions() {
        StringBuilder sb = new StringBuilder("<html>");
        for (Map.Entry<String, String> hibernateInfo : hqlService.getHibernateInfo().entrySet()) {
            sb.append(hibernateInfo.getKey()).append(": ").append(hibernateInfo.getValue()).append("<br>");
        }
        sb.append("</html>");
        JOptionPane.showMessageDialog(frame, font(new ELabel(HqlResourceBundle.getMessage("versions.description", sb.toString())), 14),
                HqlResourceBundle.getMessage("versions.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    protected void editable_results() {
        deleteObjectAction.setEnabled(editableResultsAction.isSelected());
    }
}

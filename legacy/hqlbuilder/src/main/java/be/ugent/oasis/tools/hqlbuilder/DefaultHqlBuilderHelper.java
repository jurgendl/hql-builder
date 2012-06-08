package be.ugent.oasis.tools.hqlbuilder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.swingeasy.CustomizableOptionPane;
import org.swingeasy.EList;
import org.swingeasy.EListConfig;
import org.swingeasy.EListRecord;
import org.swingeasy.ListOptionPaneCustomizer;
import org.swingeasy.MessageType;
import org.swingeasy.OptionType;
import org.swingeasy.ResultType;

/**
 * cfg ea.
 * 
 * @author jdlandsh
 */
public class DefaultHqlBuilderHelper implements be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper {
    private static final Logger logger = Logger.getLogger(DefaultHqlBuilderHelper.class);

    {
        try {
            // zet log level voor deze class op ERROR
            org.apache.log4j.Logger.getLogger("org.hibernate.validator.ClassValidator").setLevel(org.apache.log4j.Level.ERROR);
        } catch (Throwable ex) {
            //
        }
    }

    private static Properties cfgprops = new Properties();

    static {
        try {
            InputStream inStream = DefaultHqlBuilderHelper.class.getResourceAsStream("/hqlbuilder.properties");

            if (inStream == null) {
                inStream = DefaultHqlBuilderHelper.class.getResourceAsStream("/default/hqlbuilder.properties");
            }

            cfgprops.load(inStream);
            inStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#getPropertyFrame(org.hibernate.SessionFactory, java.lang.Object, boolean)
     */
    @Override
    public JComponent getPropertyFrame(SessionFactory sessionFactory, Object object, boolean editable) {
        return new PropertyPanel(sessionFactory, object, editable, this);
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#getEntityDiscovery()
     */
    @Override
    public EntityDiscovery getEntityDiscovery() {
        try {
            return (EntityDiscovery) Class.forName(cfgprops.getProperty("hibernate.entityDiscovery.class")).newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#getHibernateConfiguration()
     */
    @Override
    public String getHibernateConfiguration() {
        return cfgprops.getProperty("hibernate.configuration");
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#getPersistenceContext()
     */
    @Override
    public String getPersistenceContext() {
        return cfgprops.getProperty("hibernate.persistence.context");
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#getResourceLocations()
     */
    @Override
    public String[] getResourceLocations() {
        List<String> cfgs = new ArrayList<String>();
        try {
            int index = 0;
            while (true) {
                String key = "resource.location[" + index + "]";
                String string = cfgprops.getProperty(key).toString()
                        .replaceAll("\\Q${user.home}\\E", System.getProperty("user.home").replace('\\', '/'));
                log("resource: " + key + " > " + string);
                cfgs.add(string);
                index++;
            }
        } catch (Exception ex) {
            log(ex);
        }
        return cfgs.toArray(new String[cfgs.size()]);
    };

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#getResourceLocationAllDataSources()
     */
    @Override
    public String getResourceLocationAllDataSources() {
        if (!cfgprops.containsKey("resource.location.all") || cfgprops.getProperty("resource.location.all") == null) {
            return null;
        }
        return cfgprops.getProperty("resource.location.all").replaceAll("\\Q${user.home}\\E", System.getProperty("user.home").replace('\\', '/'));
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#onInit(java.util.Properties)
     */
    @Override
    public void onInit(Properties props) {
        //
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#accept(java.lang.Class)
     */
    @Override
    public boolean accept(Class<?> type) {
        return true;
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#showDialog(javax.swing.JFrame, java.lang.String, V[])
     */
    @Override
    public <V> V showDialog(JFrame parent, String title, V... options) {
        if (options == null || options.length == 0) {
            return null;
        }

        EListConfig cfg = new EListConfig();
        cfg.setFilterable(true);
        final EList<V> list = new EList<V>(cfg);
        JPanel container = new JPanel(new BorderLayout());
        container.add(new JScrollPane(list), BorderLayout.CENTER);
        container.add(list.getFiltercomponent(), BorderLayout.NORTH);

        int borderw = 2;
        list.getFiltercomponent().setBorder(BorderFactory.createEmptyBorder(borderw, borderw, borderw, borderw));

        for (V option : options) {
            list.addRecord(new EListRecord<V>(option));
        }

        ResultType returnValue = CustomizableOptionPane.showCustomDialog(parent, container, title, MessageType.QUESTION, OptionType.OK_CANCEL, null,
                new ListOptionPaneCustomizer<V>(list) {
                    @Override
                    public void customize(Component parentComponent, MessageType messageType, OptionType optionType, final JOptionPane pane,
                            final JDialog dialog) {
                        super.customize(parentComponent, messageType, optionType, pane, dialog);
                        dialog.getRootPane().setDefaultButton(null);
                    }
                });

        return returnValue != ResultType.OK ? null : list.getSelectedRecord() == null ? null : list.getSelectedRecord().get();
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#getHelpUrl()
     */
    @Override
    public String getHelpUrl() {
        return null;
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#onFirstSessionReady(org.hibernate.SessionFactory)
     */
    @Override
    public void onFirstSessionReady(SessionFactory sessionFactory) {
        try {
            Session session = sessionFactory.openSession();
            session.persist(Class.forName("be.ugent.oasis.tools.hqlbuilder.Pojo").newInstance());
            session.flush();
            session.close();
        } catch (Exception ex) {
            //
        }
    }

    private static Font DEFAULT_FONT;

    static {
        try {
            for (Font font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
                if (font.getName().equalsIgnoreCase("DejaVu Sans Mono")) {
                    DEFAULT_FONT = font.deriveFont(12f);
                    break;
                }
            }
            if (DEFAULT_FONT == null) {
                DEFAULT_FONT = new JLabel().getFont().deriveFont(12f);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            DEFAULT_FONT = new JLabel().getFont().deriveFont(12f);
        }
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#font(javax.swing.JComponent, java.lang.Integer)
     */
    @Override
    public <T extends JComponent> T font(T comp, Integer size) {
        if (size != null) {
            comp.setFont(DEFAULT_FONT.deriveFont((float) size));
        } else {
            comp.setFont(DEFAULT_FONT);
        }

        return comp;
    }

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#log(java.lang.Object)
     */
    @Override
    public void log(Object message) {
        if (message instanceof Exception) {
            logger.error(message);
        } else {
            logger.debug(message);
        }
    }

    private Set<String> kws;

    /**
     * 
     * @see be.ugent.oasis.tools.hqlbuilder.IHqlBuilderHelper#getReservedKeywords()
     */
    @Override
    public Set<String> getReservedKeywords() {
        if (kws == null) {
            try {
                kws = new HashSet<String>();
                // ansi & transact sql keywords
                BufferedReader in = new BufferedReader(new InputStreamReader(DefaultHqlBuilderHelper.class.getClassLoader().getResourceAsStream(
                        "reserved_keywords.txt")));
                String line;
                while ((line = in.readLine()) != null) {
                    for (String kw : line.split(" ")) {
                        if (kw.length() < 2) {
                            continue;
                        }
                        kws.add(kw.trim());
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return kws;
    }
}

package org.tools.hqlbuilder.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.swingeasy.CustomizableOptionPane;
import org.swingeasy.EList;
import org.swingeasy.EListConfig;
import org.swingeasy.EListRecord;
import org.swingeasy.ListOptionPaneCustomizer;
import org.swingeasy.MessageType;
import org.swingeasy.OptionType;
import org.swingeasy.ResultType;
import org.tools.hqlbuilder.common.CommonUtils;

/**
 * @author Jurgen
 */
public class ClientUtils extends CommonUtils {
    static public PropertyPanel getPropertyFrame(Object object, boolean editable) {
        return new PropertyPanel(object, editable);
    }

    /**
     *
     * @see org.tools.hqlbuilder.client.IHqlBuilderHelper#showDialog(javax.swing.JFrame, java.lang.String, V[])
     */
    static public <V> V showDialog(JFrame parent, String title, V[] options) {
        if (options == null || options.length == 0) {
            return null;
        }

        EListConfig cfg = new EListConfig();
        cfg.setFilterable(true);
        final EList<V> list = new EList<>(cfg);
        JPanel container = new JPanel(new BorderLayout());
        container.add(new JScrollPane(list), BorderLayout.CENTER);
        container.add(list.getFiltercomponent(), BorderLayout.NORTH);

        int borderw = 2;
        list.getFiltercomponent().setBorder(BorderFactory.createEmptyBorder(borderw, borderw, borderw, borderw));

        for (V option : options) {
            list.stsi().addRecord(new EListRecord<>(option));
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
     * @see org.tools.hqlbuilder.client.IHqlBuilderHelper#getHelpUrl()
     */

    static public String getHelpUrl() {
        return "https://github.com/jurgendl/hql-builder/wiki";
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

    public static Font getDefaultFont() {
        return DEFAULT_FONT;
    }
}

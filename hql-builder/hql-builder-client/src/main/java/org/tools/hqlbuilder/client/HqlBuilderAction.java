package org.tools.hqlbuilder.client;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.swingeasy.HasParentComponent;
import org.tools.hqlbuilder.common.CommonUtilsAdd;

/**
 * @author Jurgen
 */
public class HqlBuilderAction extends Action implements HasParentComponent {
    private static final long serialVersionUID = -5268997556066400577L;

    private Object source;

    private String method;

    private JComponent component;

    protected static Icon icon(Object icon) {
        if (icon == null) {
            return null;
        }
        if (icon instanceof Icon) {
            return Icon.class.cast(icon);
        }
        return new ImageIcon(HqlBuilderAction.class.getClassLoader().getResource(String.class.cast(icon)));
    }

    public HqlBuilderAction(JComponent component, Object source, String method, boolean enabled, String name, Object iconResourcePath,
            String shortDescription, String longDescription, Boolean selected, Character mnemonic, String accelerator) {
        super(name, enabled, HqlResourceBundle.getMessage(name), icon(iconResourcePath), HqlResourceBundle.getMessage(shortDescription + ".short"),
                HqlResourceBundle.getMessage(longDescription + ".long"), selected, mnemonic, accelerator);
        this.component = component;
        init(source, method);
    }

    public HqlBuilderAction(JComponent component, Object source, String method, boolean enabled, String name, Object iconResourcePath,
            String shortDescription, String longDescription, Boolean selected, Character mnemonic, String accelerator, String parentId) {
        super(name, enabled, HqlResourceBundle.getMessage(name), icon(iconResourcePath), HqlResourceBundle.getMessage(shortDescription + ".short"),
                HqlResourceBundle.getMessage(longDescription + ".long"), selected, mnemonic, accelerator, parentId);
        this.component = component;
        init(source, method);
    }

    public <T> HqlBuilderAction(JComponent component, Object source, String method, boolean enabled, String name, Object iconResourcePath,
            String shortDescription, String longDescription, Boolean selected, Character mnemonic, String accelerator, String parentId, Class<T> type,
            T value) {
        super(name, enabled, HqlResourceBundle.getMessage(name), icon(iconResourcePath), HqlResourceBundle.getMessage(shortDescription + ".short"),
                HqlResourceBundle.getMessage(longDescription + ".long"), selected, mnemonic, accelerator, parentId, type, value);
        this.component = component;
        init(source, method);
    }

    protected void init(Object _source, String _method) {
        if (_method != null && _source == null) {
            throw new NullPointerException();
        }
        this.source = _method == null ? null : _source;
        this.method = _method == null ? null : _method.replace(' ', '_').replace(':', '_').replace('-', '_');
    }

    /**
     *
     * @see org.tools.hqlbuilder.client.Action#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (source != null && method != null) {
            try {
                CommonUtilsAdd.call(source, method);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return component;
    }
}

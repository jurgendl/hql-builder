package be.ugent.oasis.tools.hqlbuilder;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.swingeasy.HasParentComponent;
import org.swingeasy.MethodInvoker;

public class HqlBuilderAction extends Action implements HasParentComponent {
    /** serialVersionUID */
    private static final long serialVersionUID = -5268997556066400577L;

    private Object source;

    private String method;

    private JComponent component;

    public HqlBuilderAction(JComponent component, Object source, String method, boolean enabled, String name, String iconResourcePath,
            String shortDescription, String longDescription, Boolean selected, Character mnemonic, String accelerator) {
        super(name, enabled, HqlResourceBundle.getMessage(name), iconResourcePath == null ? null : new ImageIcon(HqlBuilderAction.class
                .getClassLoader().getResource(iconResourcePath)), HqlResourceBundle.getMessage(shortDescription + ".short"), HqlResourceBundle
                .getMessage(longDescription + ".long"), selected, mnemonic, accelerator);
        this.component = component;
        init(source, method);
    }

    public HqlBuilderAction(JComponent component, Object source, String method, boolean enabled, String name, String iconResourcePath,
            String shortDescription, String longDescription, Boolean selected, Character mnemonic, String accelerator, String parentId) {
        super(name, enabled, HqlResourceBundle.getMessage(name), iconResourcePath == null ? null : new ImageIcon(HqlBuilderAction.class
                .getClassLoader().getResource(iconResourcePath)), HqlResourceBundle.getMessage(shortDescription + ".short"), HqlResourceBundle
                .getMessage(longDescription + ".long"), selected, mnemonic, accelerator, parentId);
        this.component = component;
        init(source, method);
    }

    public <T> HqlBuilderAction(JComponent component, Object source, String method, boolean enabled, String name, String iconResourcePath,
            String shortDescription, String longDescription, Boolean selected, Character mnemonic, String accelerator, String parentId,
            Class<T> type, T value) {
        super(name, enabled, HqlResourceBundle.getMessage(name), iconResourcePath == null ? null : new ImageIcon(HqlBuilderAction.class
                .getClassLoader().getResource(iconResourcePath)), HqlResourceBundle.getMessage(shortDescription + ".short"), HqlResourceBundle
                .getMessage(longDescription + ".long"), selected, mnemonic, accelerator, parentId, type, value);
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
     * @see be.ugent.oasis.tools.hqlbuilder.Action#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (source != null && method != null) {
            try {
                MethodInvoker.invoke(source, method);
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

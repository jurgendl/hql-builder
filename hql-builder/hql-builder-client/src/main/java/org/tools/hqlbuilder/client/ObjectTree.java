package org.tools.hqlbuilder.client;

import java.awt.BorderLayout;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.swingeasy.ETree;
import org.swingeasy.ETreeConfig;
import org.swingeasy.ETreeNode;
import org.tools.hqlbuilder.common.HqlService;

/**
 * @author Jurgen
 */
public class ObjectTree extends JFrame {

    private static final long serialVersionUID = 3880395325775694814L;

    private final JPanel propertypanel = new JPanel(new BorderLayout());

    public ObjectTree(final HqlBuilderFrame frame, final HqlService hqlService, Object bean, final boolean editable) {
        bean = initialize(bean);
        TreeNode rootNode = new TreeNode("", bean);
        final ETree<Object> tree = new ETree<>(new ETreeConfig(), rootNode);
        tree.setEditable(false);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), propertypanel);
        getContentPane().add(split, BorderLayout.CENTER);
        try {
            setTitle(getClassname(bean) + " " + bean);
        } catch (Exception ex) {
            setTitle(getClassname(bean));
        }
        setSize(1024, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tree.addTreeSelectionListener(e -> {
            try {
                Object data = tree.getSelectionModel().getSelectionPath().getLastPathComponent();
                propertypanel.setVisible(false);
                propertypanel.removeAll();
                if (data != null) {
                    data = TreeNode.class.cast(data).bean;
                    // if (hqlBuilderHelper.accept(data.getClass())) {
                    data = initialize(data);
                    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                    for (PropertyDescriptor descriptor : propertyUtilsBean.getPropertyDescriptors(data)) {
                        try {
                            propertyUtilsBean.getProperty(data, descriptor.getName().toString());
                        } catch (Exception ex1) {
                            //
                        }
                    }
                    PropertyPanel propertyFrame = ClientUtils.getPropertyFrame(data, editable);
                    propertyFrame.setHqlService(hqlService);
                    frame.font(propertyFrame, null);
                    propertypanel.add(propertyFrame, BorderLayout.CENTER);
                    // }
                }
                propertypanel.setVisible(true);
            } catch (NullPointerException ex2) {
                //
            }
        });
        split.setDividerLocation(500);
        frame.font(tree, null);
        setVisible(true);
    }

    private class TreeNode extends ETreeNode<Object> {
        private static final long serialVersionUID = 4389106694997553842L;

        private Object bean;

        private String name;

        private String toString;

        private TreeNode(String name, Object bean) {
            super(bean);
            this.name = name;
            this.bean = bean;
            String classname = getClassname(bean);
            String valueString = "?";
            try {
                valueString = "" + bean;
            } catch (Exception ex) {
                //
            }
            toString = classname + " " + name + " = " + valueString;
        }

        /**
         *
         * @see org.swingeasy.ETreeNode#getStringValue()
         */
        @Override
        public String getStringValue() {
            return toString();
        }

        /**
         *
         * @see org.swingeasy.ETreeNode#toString()
         */
        @Override
        public String toString() {
            return toString;
        }

        /**
         *
         * @see org.swingeasy.ETreeNode#initChildren(java.util.List)
         */
        @Override
        protected void initChildren(List<ETreeNode<Object>> list) {
            try {
                PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                Object _bean = initialize(bean);
                if (_bean instanceof Collection) {
                    int idx = 0;
                    for (Object it : Collection.class.cast(_bean)) {
                        it = initialize(it);
                        list.add(new TreeNode(name + "[" + (idx++) + "]", it));
                    }
                } else {
                    for (PropertyDescriptor descriptor : propertyUtilsBean.getPropertyDescriptors(_bean)) {
                        try {
                            Object _name = descriptor.getName();
                            if (_name.equals("class")) {
                                continue;
                            }
                            Object _value = propertyUtilsBean.getProperty(_bean, _name.toString());
                            if (_value != null) {
                                if (_value instanceof Collection) {
                                    list.add(new TreeNode(_name.toString(), _bean));
                                } else /* if (hqlBuilderHelper.accept(_value.getClass())) */ {
                                    _value = initialize(_value);
                                    list.add(new TreeNode(_name.toString(), _value));
                                }
                            }
                        } catch (Exception ex) {
                            //
                        }

                    }
                }
            } catch (Exception ex) {
                //
            }
        }
    }

    private Object initialize(Object o) {
        // o = sessionFactory.openSession().merge(o);
        // if (o instanceof HibernateProxy) {
        // return ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation();
        // }
        // Hibernate.initialize(o);
        return o;
    }

    public String getClassname(Object bean) {
        String classname = bean.getClass().getSimpleName();
        if (classname.contains("$$Enhancer")) {
            classname = classname.substring(0, classname.indexOf("$$Enhancer"));
        }
        return classname;
    }
}

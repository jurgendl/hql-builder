package org.tools.hqlbuilder.client;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.Hibernate;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swingeasy.EDateEditor;
import org.swingeasy.EDateTimeEditor;
import org.swingeasy.ObjectWrapper;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.propertysheet.PropertySheetTable;
import com.l2fprod.common.propertysheet.PropertySheetTableModel;
import com.l2fprod.common.propertysheet.PropertySheetTableModel.Item;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.util.converter.NumberConverters;

/**
 * @author Jurgen
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PropertyPanel extends PropertySheetPanel {
    protected static final Logger logger = LoggerFactory.getLogger(PropertyPanel.class);

    private static final String LAZY = "*LAZY*";

    private static final long serialVersionUID = -5568670775272022905L;

    private Object bean;

    @SuppressWarnings("unused")
    private Boolean settingValue = false;

    private final SortedMap<String, PropertyDescriptor> propertyDescriptors = new TreeMap<String, PropertyDescriptor>();

    private HqlService hqlService;

    public PropertyPanel(Object bean, final boolean editable) {
        super(new PropertySheetTable() {
            private static final long serialVersionUID = 5578802576173787006L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Object value = null;
                try {
                    value = getValueAt(row, column);
                    if (!isInitialized(value)) {
                        value = LAZY;
                    } else {
                        String.valueOf(value);
                    }
                } catch (org.hibernate.LazyInitializationException ex) {
                    value = LAZY;
                } catch (Exception ex) {
                    //
                }
                boolean isSelected = isCellSelected(row, column);
                Component component = renderer.getTableCellRendererComponent(this, value, isSelected, false, row, column);
                PropertySheetTableModel.Item item = getSheetModel().getPropertySheetElement(row);
                if (item.isProperty()) {
                    component.setEnabled(item.getProperty().isEditable());
                }
                return component;
            }
        });

        logger.info("{}", String.valueOf(bean));

        this.bean = bean;

        if (bean != null) {
            try {
                for (PropertyDescriptor propertyDescriptor : new PropertyDescriptorsBean(bean.getClass()).getPropertyDescriptors().values()) {
                    try {
                        propertyDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
                        Method readMethod = propertyDescriptor.getReadMethod();
                        if (readMethod != null) {
                            Object value = readMethod.invoke(bean);
                            if (!isInitialized(value)) {
                                throw new NullPointerException();
                            }
                            String.valueOf(value);
                        }
                    } catch (Exception ex) {
                        //
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        // for (Object entry : new ObjectWrapper(propertyRendererRegistry).get("typeToRenderer", Map.class).entrySet()) {
        // Map.Entry mapEntry = (Map.Entry) entry;
        // final TableCellRenderer renderer = (TableCellRenderer) mapEntry.getValue();
        // Class type = (Class) mapEntry.getKey();
        // propertyRendererRegistry.registerRenderer(type, new TableCellRenderer() {
        // @Override
        // public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // try {
        // return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // } catch (org.hibernate.LazyInitializationException ex) {
        // return renderer.getTableCellRendererComponent(table, LAZY, isSelected, hasFocus, row, column);
        // }
        // }
        // });
        // }

        PropertyRendererRegistry propertyRendererRegistry = PropertyRendererRegistry.class.cast(getRendererFactory());
        propertyRendererRegistry.registerRenderer(Date.class, new DateEditor());
        propertyRendererRegistry.registerRenderer(Timestamp.class, new TimestampEditor());
        propertyRendererRegistry.registerRenderer(LocalDate.class, new LocalDateEditor());
        propertyRendererRegistry.registerRenderer(LocalDateTime.class, new LocalDateTimeEditor());
        propertyRendererRegistry.registerRenderer(BigInteger.class, new BigIntegerEditor());
        propertyRendererRegistry.registerRenderer(BigDecimal.class, new BigDecimalEditor());

        PropertyEditorRegistry propertyEditorRegistry = PropertyEditorRegistry.class.cast(getEditorFactory());
        propertyEditorRegistry.registerEditor(Date.class, new DateEditor());
        propertyEditorRegistry.registerEditor(Timestamp.class, new TimestampEditor());
        propertyEditorRegistry.registerEditor(LocalDate.class, new LocalDateEditor());
        propertyEditorRegistry.registerEditor(LocalDateTime.class, new LocalDateTimeEditor());
        propertyEditorRegistry.registerEditor(BigInteger.class, new BigIntegerEditor());
        propertyEditorRegistry.registerEditor(BigDecimal.class, new BigDecimalEditor());

        for (final Map.Entry<String, PropertyDescriptor> propertyDescriptorEntry : propertyDescriptors.entrySet()) {
            try {
                logger.debug(propertyDescriptorEntry.getKey());
                PropertyDescriptor propertyDescriptor = propertyDescriptorEntry.getValue();
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }
                Object value = readMethod.invoke(bean);

                Class<?> propertyType = propertyDescriptor.getPropertyType();

                if (java.lang.Enum.class.equals(propertyType.getSuperclass()) && propertyEditorRegistry.getEditor(propertyType) == null) {
                    propertyEditorRegistry.registerEditor(propertyType, new EnumEditor(EnumSet.allOf((Class<? extends Enum>) propertyType)));
                    propertyRendererRegistry.registerRenderer(propertyType, new EnumEditor(EnumSet.allOf((Class<? extends Enum>) propertyType)));
                }

                String propertyName = propertyDescriptor.getName();
                String propertyDisplayName = propertyDescriptor.getDisplayName();

                final DefaultProperty property = new DefaultProperty();
                property.setName(propertyName);
                property.setDisplayName(propertyDisplayName);
                property.setType(propertyType);
                if (!isInitialized(value)) {
                    value = LAZY;
                } else {
                    try {
                        property.setValue(value);
                    } catch (org.hibernate.LazyInitializationException ex) {
                        property.setValue(LAZY);
                    }
                }
                property.setCategory(HqlResourceBundle.getMessage("read-only"));
                property.setShortDescription(HqlResourceBundle.getMessage("read-only"));
                property.setEditable(false);

                if (editable) {
                    final Method writeMethod = propertyDescriptor.getWriteMethod();

                    if (writeMethod != null) {
                        if (null == propertyEditorRegistry.getEditor(propertyType) && !String.class.equals(propertyType)) {
                            property.setShortDescription(HqlResourceBundle.getMessage("no editor found"));
                            property.setCategory(HqlResourceBundle.getMessage("no editor found"));

                            if (Object[].class.isAssignableFrom(propertyType) || Collection.class.isAssignableFrom(propertyType)
                                    || Map.class.isAssignableFrom(propertyType)) {
                                //
                            } else {
                                logger.warn("no editor found for type {}", propertyType.getName());
                            }
                            property.setEditable(false);
                        } else {
                            property.setShortDescription(HqlResourceBundle.getMessage("editable"));
                            property.setCategory(HqlResourceBundle.getMessage("editable"));
                            property.setEditable(true);
                            property.addPropertyChangeListener(new PropertyListener(writeMethod));
                        }
                    }
                }

                addProperty(property);
            } catch (Exception ex) {
                // } catch (RuntimeException ex) {
                // System.err.println(propertyDescriptorEntry.getKey());
                // System.err.println(ex);
                // } catch (IllegalAccessException ex) {
                // System.err.println(propertyDescriptorEntry.getKey());
                // System.err.println(ex);
                // } catch (InvocationTargetException ex) {
                // System.err.println(propertyDescriptorEntry.getKey());
                // System.err.println(ex);
            }
        }

        setSorting(true);
        setSortingCategories(true);
        setMode(PropertySheetPanel.VIEW_AS_CATEGORIES);
        setDescriptionVisible(false);

        getTable().setDragEnabled(true);
        getTable().setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = -3920945303475174435L;

            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {
                return false;
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                return false;
            }

            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                try {
                    JTable table = (JTable) c;
                    if (table.getSelectedColumn() == -1) {
                        return null;
                    }
                    if (table.getSelectedRow() == -1) {
                        return null;
                    }
                    Object value = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
                    if (value == null) {
                        return null;
                    }
                    if (value instanceof com.l2fprod.common.propertysheet.PropertySheetTableModel.Item) {
                        com.l2fprod.common.propertysheet.PropertySheetTableModel.Item item = (Item) value;
                        return new StringSelection(String.valueOf(item.getName()));
                    }
                    return new StringSelection(String.valueOf(value));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        });
    }

    private static class DateEditor extends AbstractPropertyEditor implements TableCellRenderer {
        public DateEditor() {
            editor = new EDateEditor();
            EDateEditor.class.cast(editor).setShowLabel(false);
        }

        @Override
        public Object getValue() {
            Date date = EDateEditor.class.cast(editor).getDate();
            return date;
        }

        @Override
        public void setValue(Object value) {
            Date date = value == null ? null : Date.class.cast(value);
            EDateEditor.class.cast(editor).setDate(date);
        }

        private DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                EDateEditor.class.cast(editor).getFormatter().format((Date) value);
            }
            return dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class TimestampEditor extends AbstractPropertyEditor implements TableCellRenderer {
        public TimestampEditor() {
            editor = new EDateTimeEditor();
            EDateTimeEditor.class.cast(editor).setShowLabel(false);
        }

        @Override
        public Object getValue() {
            Date date = EDateTimeEditor.class.cast(editor).getDate();
            Timestamp ts = date == null ? null : new Timestamp(date.getTime());
            return ts;
        }

        @Override
        public void setValue(Object value) {
            Date date = value == null ? null : new Date(Timestamp.class.cast(value).getTime());
            EDateTimeEditor.class.cast(editor).setDate(date);
        }

        private DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                EDateTimeEditor.class.cast(editor).getFormatter().format((Date) value);
            }
            return dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class LocalDateEditor extends AbstractPropertyEditor implements TableCellRenderer {
        public LocalDateEditor() {
            editor = new EDateEditor();
            EDateEditor.class.cast(editor).setShowLabel(false);
        }

        @Override
        public Object getValue() {
            Date date = EDateEditor.class.cast(editor).getDate();
            LocalDate localDate = date == null || date.getTime() == 0 ? null : new LocalDate(date.getTime());
            return localDate;
        }

        @Override
        public void setValue(Object value) {
            Date date = value == null ? new Date(0) : LocalDate.class.cast(value).toDateTimeAtStartOfDay().toDate();
            EDateEditor.class.cast(editor).setDate(date);
        }

        private DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class LocalDateTimeEditor extends AbstractPropertyEditor implements TableCellRenderer {
        public LocalDateTimeEditor() {
            editor = new EDateTimeEditor();
            EDateTimeEditor.class.cast(editor).setShowLabel(false);
        }

        @Override
        public Object getValue() {
            Date date = EDateTimeEditor.class.cast(editor).getDate();
            LocalDateTime localDateTime = date == null ? null : new LocalDateTime(date.getTime());
            return localDateTime;
        }

        @Override
        public void setValue(Object value) {
            Date date = value == null ? null : LocalDateTime.class.cast(value).toDateTime().toDate();
            EDateTimeEditor.class.cast(editor).setDate(date);
        }

        private DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class BigIntegerEditor extends AbstractPropertyEditor implements TableCellRenderer {
        private final Class type;

        private Object lastGoodValue;

        private NumberFormat format;

        public BigIntegerEditor() {
            Class t = BigInteger.class;
            if (!Number.class.isAssignableFrom(t)) {
                throw new IllegalArgumentException("type must be a subclass of Number");
            }

            editor = new JFormattedTextField();
            this.type = t;
            ((JFormattedTextField) editor).setValue(getDefaultValue());
            ((JFormattedTextField) editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);

            // use a custom formatter to have numbers with up to 64 decimals
            format = NumberConverters.getDefaultFormat();

            ((JFormattedTextField) editor).setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(format)));
        }

        @Override
        public Object getValue() {
            String text = ((JTextField) editor).getText();
            if (text == null || text.trim().length() == 0) {
                return getDefaultValue();
            }

            // allow comma or colon
            text = text.replace(',', '.');

            // collect all numbers from this textfield
            StringBuffer number = new StringBuffer();
            number.ensureCapacity(text.length());
            for (int i = 0, c = text.length(); i < c; i++) {
                char character = text.charAt(i);
                if ('.' == character || '-' == character || (Double.class.equals(type) && 'E' == character)
                        || (Float.class.equals(type) && 'E' == character) || Character.isDigit(character)) {
                    number.append(character);
                } else if (' ' == character) {
                    continue;
                } else {
                    break;
                }
            }

            try {
                lastGoodValue = new BigInteger(number.toString());
                // lastGoodValue = ConverterRegistry.instance().convert(type, number.toString());
            } catch (Exception e) {
                UIManager.getLookAndFeel().provideErrorFeedback(editor);
            }

            return lastGoodValue;
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Number) {
                ((JFormattedTextField) editor).setText(value.toString());
            } else {
                ((JFormattedTextField) editor).setValue(getDefaultValue());
            }
            lastGoodValue = value;
        }

        private Object getDefaultValue() {
            try {
                return type.getConstructor(new Class[] { String.class }).newInstance(new Object[] { "0" });
            } catch (Exception e) {
                // will not happen
                throw new RuntimeException(e);
            }
        }

        private DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                value = format.format(value);
            }
            return dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class BigDecimalEditor extends AbstractPropertyEditor implements TableCellRenderer {
        private final Class type;

        private Object lastGoodValue;

        private NumberFormat format;

        public BigDecimalEditor() {
            Class t = BigDecimal.class;
            if (!Number.class.isAssignableFrom(t)) {
                throw new IllegalArgumentException("type must be a subclass of Number");
            }

            editor = new JFormattedTextField();
            this.type = t;
            ((JFormattedTextField) editor).setValue(getDefaultValue());
            ((JFormattedTextField) editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);

            // use a custom formatter to have numbers with up to 64 decimals
            format = NumberConverters.getDefaultFormat();

            ((JFormattedTextField) editor).setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(format)));
        }

        @Override
        public Object getValue() {
            String text = ((JTextField) editor).getText();
            if (text == null || text.trim().length() == 0) {
                return getDefaultValue();
            }

            // allow comma or colon
            text = text.replace(',', '.');

            // collect all numbers from this textfield
            StringBuffer number = new StringBuffer();
            number.ensureCapacity(text.length());
            for (int i = 0, c = text.length(); i < c; i++) {
                char character = text.charAt(i);
                if ('.' == character || '-' == character || (Double.class.equals(type) && 'E' == character)
                        || (Float.class.equals(type) && 'E' == character) || Character.isDigit(character)) {
                    number.append(character);
                } else if (' ' == character) {
                    continue;
                } else {
                    break;
                }
            }

            try {
                lastGoodValue = new BigDecimal(number.toString());
                // lastGoodValue = ConverterRegistry.instance().convert(type, number.toString());
            } catch (Exception e) {
                UIManager.getLookAndFeel().provideErrorFeedback(editor);
            }

            return lastGoodValue;
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Number) {
                ((JFormattedTextField) editor).setText(value.toString());
            } else {
                ((JFormattedTextField) editor).setValue(getDefaultValue());
            }
            lastGoodValue = value;
        }

        private Object getDefaultValue() {
            try {
                return type.getConstructor(new Class[] { String.class }).newInstance(new Object[] { "0" });
            } catch (Exception e) {
                // will not happen
                throw new RuntimeException(e);
            }
        }

        private DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                value = format.format(value);
            }

            return dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class EnumEditor extends ComboBoxPropertyEditor implements TableCellRenderer {
        public EnumEditor(EnumSet etoptions) {
            Object[] options = new Object[etoptions.size() + 1];
            int i = 1;
            Iterator iterator = etoptions.iterator();
            while (iterator.hasNext()) {
                options[i++] = iterator.next();
            }
            // System.out.println(Arrays.asList(options));
            setAvailableValues(options);
            ((JComboBox) super.editor).setRenderer(new Renderer() {
                private static final long serialVersionUID = 4777310242425067779L;

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    if (value == null) {
                        value = "";
                    }
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            });
        }

        private DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value == null) {
                value = "";
            }
            return dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private class PropertyListener implements PropertyChangeListener {
        final Method writeMethod;

        public PropertyListener(Method writeMethod) {
            this.writeMethod = writeMethod;
        }

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            settingValue = true;

            try {
                if (!new EqualsBuilder().append(evt.getOldValue(), evt.getNewValue()).isEquals()) {
                    if (bean instanceof Serializable) {
                        logger.info("changing value from '{}' to '{}'", evt.getOldValue(), evt.getNewValue());
                        writeMethod.invoke(bean, evt.getNewValue());
                        Serializable serializable = Serializable.class.cast(bean);
                        Serializable id = hqlService.save(serializable);
                        bean = hqlService.get(serializable.getClass(), id);
                    } else {
                        logger.error("bean is not serializable");
                    }
                }

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(PropertyPanel.this,
                                HqlResourceBundle.getMessage("propertypanel.edit.success") + ":\n" + evt.getOldValue() + " > " + evt.getNewValue(),
                                HqlResourceBundle.getMessage("propertypanel.edit.title"), JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            } catch (final ValidationException ex) {
                final StringBuilder sb = new StringBuilder();
                for (InvalidValue iv : ex.getInvalidValues()) {
                    sb.append("\u2022 ").append(iv.getPropertyName()).append(" ").append(iv.getMessage()).append("\n");
                }
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(PropertyPanel.this, HqlResourceBundle.getMessage("propertypanel.edit.error") + ":\n" + sb,
                                HqlResourceBundle.getMessage("propertypanel.edit.title"), JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (final Exception ex) {
                ex.printStackTrace();
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(PropertyPanel.this, HqlResourceBundle.getMessage("propertypanel.edit.error") + ":\n" + ex,
                                HqlResourceBundle.getMessage("propertypanel.edit.title"), JOptionPane.ERROR_MESSAGE);
                    }
                });
            } finally {
                settingValue = false;
            }
        }
    }

    public void setHqlService(HqlService hqlService) {
        this.hqlService = hqlService;
    }

    private static boolean isInitialized(Object object) {
        if (object == null) {
            return true;
        }
        if (object.getClass().getName().startsWith("java.util.Collections$Unmodifiable")) {
            object = new ObjectWrapper(object).get("c");
        }
        return Hibernate.isInitialized(object);
    }
}

package org.tools.hqlbuilder.client;

import java.awt.EventQueue;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
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

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.swingeasy.EDateEditor;
import org.swingeasy.EDateTimeEditor;
import org.tools.hqlbuilder.common.HqlService;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.propertysheet.PropertySheetTableModel.Item;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.util.converter.NumberConverters;

/**
 * dependency:<br>
 * -) com.l2fprod:l2fprod-common-sheet:7.3<br>
 * -) commons-lang<br>
 * -) jcalendar<br>
 * 
 * @author jdlandsh
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PropertyPanel extends PropertySheetPanel {
    /** serialVersionUID */
    private static final long serialVersionUID = -5568670775272022905L;

    private Object bean;

    @SuppressWarnings("unused")
    private Boolean settingValue = false;

    private final SortedMap<String, PropertyDescriptor> propertyDescriptors = new TreeMap<String, PropertyDescriptor>();

    private HqlService hqlService;

    /**
     * Creates a new PropertyPanel object.
     * 
     * @param bean na
     */

    public PropertyPanel(Object bean, final boolean editable) {
        ClientUtils.log(bean);

        this.bean = bean;
        BeanInfo beanInfo;

        if (bean != null) {
            try {
                beanInfo = Introspector.getBeanInfo(bean.getClass());
                for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                    propertyDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
                    Method readMethod = propertyDescriptor.getReadMethod();
                    if (readMethod != null) {
                        try {
                            String.valueOf(readMethod.invoke(bean));
                        } catch (Exception ex) {
                            //
                        }
                    }
                }
            } catch (IntrospectionException ex) {
                throw new RuntimeException(ex);
            }
        }

        PropertyEditorRegistry propertyEditorRegistry = PropertyEditorRegistry.class.cast(getEditorFactory());
        propertyEditorRegistry.registerEditor(Date.class, new DateEditor());
        propertyEditorRegistry.registerEditor(Timestamp.class, new TimestampEditor());
        propertyEditorRegistry.registerEditor(LocalDate.class, new LocalDateEditor());
        propertyEditorRegistry.registerEditor(LocalDateTime.class, new LocalDateTimeEditor());
        propertyEditorRegistry.registerEditor(BigInteger.class, new BigIntegerEditor());
        propertyEditorRegistry.registerEditor(BigDecimal.class, new BigDecimalEditor());

        for (final Map.Entry<String, PropertyDescriptor> propertyDescriptorEntry : propertyDescriptors.entrySet()) {
            try {
                ClientUtils.log(propertyDescriptorEntry.getKey());
                PropertyDescriptor propertyDescriptor = propertyDescriptorEntry.getValue();
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }
                Object value = readMethod.invoke(bean);

                Class<?> propertyType = propertyDescriptor.getPropertyType();

                if (java.lang.Enum.class.equals(propertyType.getSuperclass()) && propertyEditorRegistry.getEditor(propertyType) == null) {
                    propertyEditorRegistry.registerEditor(propertyType, new EnumEditor(EnumSet.allOf((Class<? extends Enum>) propertyType)));
                }

                String propertyName = propertyDescriptor.getName();
                String propertyDisplayName = propertyDescriptor.getDisplayName();

                final DefaultProperty property = new DefaultProperty();
                property.setName(propertyName);
                property.setDisplayName(propertyDisplayName);
                property.setType(propertyType);
                property.setValue(value);
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
                                ClientUtils.log("no editor found for type " + propertyType.getName());
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
                System.err.println(propertyDescriptorEntry.getKey());
                ex.printStackTrace();
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

    private static class DateEditor extends AbstractPropertyEditor {
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
    }

    private static class TimestampEditor extends AbstractPropertyEditor {
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
    }

    private static class LocalDateEditor extends AbstractPropertyEditor {
        public LocalDateEditor() {
            editor = new EDateEditor();
            EDateEditor.class.cast(editor).setShowLabel(false);
        }

        @Override
        public Object getValue() {
            Date date = EDateEditor.class.cast(editor).getDate();
            LocalDate localDate = date == null ? null : new LocalDate(date.getTime());
            return localDate;
        }

        @Override
        public void setValue(Object value) {
            Date date = value == null ? null : LocalDate.class.cast(value).toDateMidnight().toDate();
            EDateEditor.class.cast(editor).setDate(date);
        }
    }

    private static class LocalDateTimeEditor extends AbstractPropertyEditor {
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
    }

    private static class BigIntegerEditor extends AbstractPropertyEditor {
        private final Class type;

        private Object lastGoodValue;

        public BigIntegerEditor() {
            @SuppressWarnings("hiding")
            Class type = BigInteger.class;
            if (!Number.class.isAssignableFrom(type)) {
                throw new IllegalArgumentException("type must be a subclass of Number");
            }

            editor = new JFormattedTextField();
            this.type = type;
            ((JFormattedTextField) editor).setValue(getDefaultValue());
            ((JFormattedTextField) editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);

            // use a custom formatter to have numbers with up to 64 decimals
            NumberFormat format = NumberConverters.getDefaultFormat();

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
    }

    private static class BigDecimalEditor extends AbstractPropertyEditor {
        private final Class type;

        private Object lastGoodValue;

        public BigDecimalEditor() {
            @SuppressWarnings("hiding")
            Class type = BigDecimal.class;
            if (!Number.class.isAssignableFrom(type)) {
                throw new IllegalArgumentException("type must be a subclass of Number");
            }

            editor = new JFormattedTextField();
            this.type = type;
            ((JFormattedTextField) editor).setValue(getDefaultValue());
            ((JFormattedTextField) editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);

            // use a custom formatter to have numbers with up to 64 decimals
            NumberFormat format = NumberConverters.getDefaultFormat();

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
    }

    private static class EnumEditor extends ComboBoxPropertyEditor {
        public EnumEditor(EnumSet etoptions) {
            Object[] options = new Object[etoptions.size()];
            Iterator iterator = etoptions.iterator();
            for (int i = 0; i < options.length; i++) {
                options[i] = iterator.next();
            }
            setAvailableValues(options);
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
                    ClientUtils.log("changing value from '" + evt.getOldValue() + "' to '" + evt.getNewValue() + "'");
                    writeMethod.invoke(bean, evt.getNewValue());
                    bean = hqlService.save(bean);
                }

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(PropertyPanel.this,
                                HqlResourceBundle.getMessage("propertypanel.edit.success") + ":\n" + evt.getOldValue() + " > " + evt.getNewValue(),
                                HqlResourceBundle.getMessage("propertypanel.edit.title"), JOptionPane.INFORMATION_MESSAGE);
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
}

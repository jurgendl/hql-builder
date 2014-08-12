package org.tools.hqlbuilder.webservice.wicket.forms;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

public class FormSettings implements Serializable {
    private static final long serialVersionUID = 3682532274799101432L;

    /** add required to form elements */
    protected boolean clientsideRequiredValidation = true;

    /** activate ajax on form (per field live validation, submit by ajax) */
    protected boolean ajax = false;

    /** show label */
    protected boolean showLabel = true;

    /** requires ajax = true */
    protected boolean liveValidation = false;

    protected boolean cancelable = false;

    protected boolean inheritId = false;

    /** css class for required fields */
    protected String requiredClass = "required";

    protected String validClass = "valid";

    protected String invalidClass = "invalid";

    protected String requiredMarkerClass = "requiredMarker";

    protected FormPanelVariation variation = FormPanelVariation.label;

    protected int columns = 1;

    public FormSettings() {
        super();
    }

    public boolean isShowLabel() {
        return this.showLabel;
    }

    public boolean isAjax() {
        return this.ajax;
    }

    public String getRequiredClass() {
        return this.requiredClass;
    }

    public boolean isLiveValidation() {
        return this.liveValidation;
    }

    public String getValidClass() {
        return this.validClass;
    }

    public String getInvalidClass() {
        return this.invalidClass;
    }

    public String getRequiredMarkerClass() {
        return this.requiredMarkerClass;
    }

    public FormSettings setRequiredClass(String requiredClass) {
        this.requiredClass = requiredClass;
        return this;
    }

    public FormSettings setValidClass(String validClass) {
        this.validClass = validClass;
        return this;
    }

    public FormSettings setInvalidClass(String invalidClass) {
        this.invalidClass = invalidClass;
        return this;
    }

    public FormSettings setRequiredMarkerClass(String requiredMarkerClass) {
        this.requiredMarkerClass = requiredMarkerClass;
        return this;
    }

    public boolean isClientsideRequiredValidation() {
        return this.clientsideRequiredValidation;
    }

    public FormSettings setClientsideRequiredValidation(boolean clientsideRequiredValidation) {
        this.clientsideRequiredValidation = clientsideRequiredValidation;
        return this;
    }

    public FormSettings setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
        return this;
    }

    public FormSettings setLiveValidation(boolean liveValidation) {
        this.liveValidation = liveValidation;
        return this;
    }

    @Override
    public FormSettings clone() {
        try {
            return getClass().cast(BeanUtils.cloneBean(this));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        }
    }

    @Override
    public String toString() {
        try {
            StringBuilder builder = new StringBuilder("{");
            PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
            for (PropertyDescriptor desc : propertyUtils.getPropertyDescriptors(this)) {
                String name = desc.getName();
                if ("class".equals(name) || skipForExport(name)) {
                    continue;
                }
                Object value = propertyUtils.getSimpleProperty(this, name);
                if (value == null) {
                    continue;
                }
                if (String.class.equals(desc.getPropertyType())) {
                    if (StringUtils.isBlank(String.class.cast(value))) {
                        continue;
                    }
                    builder.append(name).append(":").append("\"").append(value).append("\"").append(",");
                } else if (Enum.class.isAssignableFrom(desc.getPropertyType())) {
                    builder.append(name).append(":").append("\"").append(value).append("\"").append(",");
                } else {
                    builder.append(name).append(":").append(value).append(",");
                }
            }
            if (builder.length() == 1) {
                return "{}";
            }
            return builder.deleteCharAt(builder.length() - 1).append("}").toString();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected boolean skipForExport(String propertyName) {
        return propertyName == null;
    }

    public FormSettings setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public boolean isCancelable() {
        return this.cancelable;
    }

    public FormSettings setAjax(boolean ajax) {
        this.ajax = ajax;
        return this;
    }

    public boolean isInheritId() {
        return this.inheritId;
    }

    public FormSettings setInheritId(boolean inheritId) {
        this.inheritId = inheritId;
        return this;
    }

    public boolean getClientsideRequiredValidation() {
        return this.clientsideRequiredValidation;
    }

    public boolean getAjax() {
        return this.ajax;
    }

    public boolean getShowLabel() {
        return this.showLabel;
    }

    public boolean getLiveValidation() {
        return this.liveValidation;
    }

    public boolean getCancelable() {
        return this.cancelable;
    }

    public boolean getInheritId() {
        return this.inheritId;
    }

    public FormPanelVariation getVariation() {
        return this.variation;
    }

    public FormSettings setVariation(FormPanelVariation variation) {
        this.variation = variation;
        return this;
    }

    public int getColumns() {
        return this.columns;
    }

    public FormSettings setColumns(int columns) {
        this.columns = columns;
        return this;
    }
}
package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

public class FormSettings implements Serializable {
    private static final long serialVersionUID = 3682532274799101432L;

    /** fixed ids */
    protected final boolean inheritId;

    /** activate ajax on form (per field live validation, submit by ajax) */
    protected final boolean ajax;

    /** show label */
    protected boolean showLabel = true;

    /** css class for required fields */
    protected String requiredClass = "required";

    protected String validClass = "valid";

    protected String invalidClass = "invalid";

    protected String requiredMarkerClass = "requiredMarker";

    /** requires ajax = true */
    protected boolean liveValidation = false;

    public FormSettings(boolean inheritId, boolean ajax) {
        this.inheritId = inheritId;
        this.ajax = ajax;
    }

    public boolean isInheritId() {
        return this.inheritId;
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

    protected String getClassInvalid() {
        return "invalid";
    }

    protected String getClassValid() {
        return "valid";
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

    public void setRequiredClass(String requiredClass) {
        this.requiredClass = requiredClass;
    }

    public void setValidClass(String validClass) {
        this.validClass = validClass;
    }

    public void setInvalidClass(String invalidClass) {
        this.invalidClass = invalidClass;
    }

    public void setRequiredMarkerClass(String requiredMarkerClass) {
        this.requiredMarkerClass = requiredMarkerClass;
    }
}
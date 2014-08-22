package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.request.resource.ResourceReference;
import org.tools.hqlbuilder.webservice.resources.kendo.KendoUI;

import com.googlecode.wicket.jquery.core.resource.JQueryGlobalizeResourceReference;
import com.googlecode.wicket.jquery.core.settings.JQueryLibrarySettings;
import com.googlecode.wicket.jquery.ui.calendar.settings.CalendarLibrarySettings;
import com.googlecode.wicket.jquery.ui.calendar.settings.ICalendarLibrarySettings;
import com.googlecode.wicket.kendo.ui.settings.IKendoUILibrarySettings;
import com.googlecode.wicket.kendo.ui.settings.KendoUILibrarySettings;

public class WicketResourceReferences extends JQueryLibrarySettings implements IKendoUILibrarySettings, ICalendarLibrarySettings {
    private ResourceReference kendoUICommonStyleSheetReference;

    private ResourceReference kendoUIThemeStyleSheetReference;

    private ResourceReference kendoUIJavaScriptReference;

    private ResourceReference calendarJavaScriptReference;

    private ResourceReference calendarStyleSheetReference;

    private ResourceReference gCalJavaScriptReference;

    public WicketResourceReferences() {
        this(true);
    }

    public WicketResourceReferences(boolean init) {
        if (init) {
            setJQueryGlobalizeReference(JQueryGlobalizeResourceReference.get()); // this one is not set by default
            kendoUICommonStyleSheetReference = KendoUI.KENDO_COMMON_CSS; // this one is not set by default
            kendoUIThemeStyleSheetReference = KendoUI.KENDO_DEFAULT_CSS; // this one is not set by default
            kendoUIJavaScriptReference = KendoUILibrarySettings.get().getKendoUIJavaScriptReference(); // redirect to default
            calendarJavaScriptReference = CalendarLibrarySettings.get().getCalendarJavaScriptReference(); // redirect to default
            calendarStyleSheetReference = CalendarLibrarySettings.get().getCalendarStyleSheetReference(); // redirect to default
            gCalJavaScriptReference = CalendarLibrarySettings.get().getGCalJavaScriptReference(); // redirect to default
        }
    }

    @Override
    public ResourceReference getKendoUICommonStyleSheetReference() {
        return kendoUICommonStyleSheetReference;
    }

    @Override
    public ResourceReference getKendoUIThemeStyleSheetReference() {
        return kendoUIThemeStyleSheetReference;
    }

    @Override
    public ResourceReference getKendoUIJavaScriptReference() {
        return kendoUIJavaScriptReference;
    }

    @Override
    public ResourceReference getCalendarJavaScriptReference() {
        return calendarJavaScriptReference;
    }

    @Override
    public ResourceReference getCalendarStyleSheetReference() {
        return calendarStyleSheetReference;
    }

    @Override
    public ResourceReference getGCalJavaScriptReference() {
        return gCalJavaScriptReference;
    }

    public void setKendoUICommonStyleSheetReference(ResourceReference kendoUICommonStyleSheetReference) {
        this.kendoUICommonStyleSheetReference = kendoUICommonStyleSheetReference;
    }

    public void setKendoUIThemeStyleSheetReference(ResourceReference kendoUIThemeStyleSheetReference) {
        this.kendoUIThemeStyleSheetReference = kendoUIThemeStyleSheetReference;
    }

    public void setKendoUIJavaScriptReference(ResourceReference kendoUIJavaScriptReference) {
        this.kendoUIJavaScriptReference = kendoUIJavaScriptReference;
    }

    public void setCalendarJavaScriptReference(ResourceReference calendarJavaScriptReference) {
        this.calendarJavaScriptReference = calendarJavaScriptReference;
    }

    public void setCalendarStyleSheetReference(ResourceReference calendarStyleSheetReference) {
        this.calendarStyleSheetReference = calendarStyleSheetReference;
    }

    public void setGCalJavaScriptReference(ResourceReference gCalJavaScriptReference) {
        this.gCalJavaScriptReference = gCalJavaScriptReference;
    }
}
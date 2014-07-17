package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.set;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.file.FileCleaner;
import org.apache.wicket.util.upload.DiskFileItemFactory;
import org.apache.wicket.util.upload.FileItemFactory;
import org.tools.hqlbuilder.webservice.js.WicketJSRoot;
import org.tools.hqlbuilder.webservice.resources.validation.JQueryValidation;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormSubmitInterceptor;

/**
 * based on markusslima bootstrap-filestyle version 1.1.0
 *
 * @see http://markusslima.github.io/bootstrap-filestyle/
 * @see http://www.surrealcms.com/blog/whipping-file-inputs-into-shape-with-bootstrap-3
 */
public class FilePickerPanel<P> extends FormRowPanel<P, List<FileUpload>, FileUploadField> implements FormSubmitInterceptor {
    private static final long serialVersionUID = -6943635423428119032L;

    public static final JavaScriptResourceReference FILEPICKER_JS = new JavaScriptResourceReference(WicketJSRoot.class, "filepicker.js");

    public static final String FILE_INPUT_CONTAINER_ID = "fileinputcontainer";

    public static final String BUTTON_TEXT_ID = "buttonText";

    protected FileItemFactory fileItemFactory;

    protected IModel<?> parentModel;

    @SuppressWarnings("unchecked")
    public FilePickerPanel(IModel<?> parentModel, P propertyPath, FormSettings formSettings, FilePickerSettings componentSettings) {
        super(propertyPath, new ListModel<FileUpload>(), formSettings, componentSettings);
        this.parentModel = parentModel;
        setPropertyType((Class<List<FileUpload>>) (new ArrayList<FileUpload>().getClass()));
    }

    @Override
    protected void addComponents() {
        this.add(getLabel());
        WebMarkupContainer fileinputcontainer = new WebMarkupContainer(FILE_INPUT_CONTAINER_ID);
        fileinputcontainer.setOutputMarkupId(true);
        fileinputcontainer.add(getComponent());
        this.add(fileinputcontainer);
        this.add(getClearFile());
        this.add(getFeedback());
    }

    protected Component getClearFile() {
        Button button = new Button("clearFileInput") {
            private static final long serialVersionUID = 8048984912207444565L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put("onclick", "clearFileInput('" + getComponent().getMarkupId() + "')");
            }
        };
        return button;
    }

    @Override
    protected FileUploadField createComponent(IModel<List<FileUpload>> model, Class<List<FileUpload>> valueType) {
        FileUploadField comp = new FileUploadField(VALUE, model) {
            private static final long serialVersionUID = 506304026413763103L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                FilePickerSettings filePickerSettings = FilePickerSettings.class.cast(getComponentSettings());
                tag(tag, "accept", filePickerSettings.getMimeType());
                tag(tag, "data-input", filePickerSettings.getShowInput());
                tag(tag, "data-icon", filePickerSettings.getShowIcon());
                tag(tag, "data-buttonText", FilePickerPanel.this.getString(BUTTON_TEXT_ID));
                tag(tag, "data-buttonName", null);
                tag(tag, "data-iconName", null);
                tag(tag, "data-disabled", null);
                tag(tag, "data-buttonBefore", null);
                tag(tag, "multiple", Boolean.TRUE.equals(filePickerSettings.getMultiple()) ? "multiple" : null);
            }
        };
        return comp;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        renderHeadConsistentLook(response);
        renderHeadClientSideValidation(response);
    }

    protected void renderHeadConsistentLook(IHeaderResponse response) {
        // response.render(JavaScriptHeaderItem.forReference(FileStyle.FILESTYLE_JS));
    }

    protected void renderHeadClientSideValidation(IHeaderResponse response) {
        // also imports JQueryValidation.VALIDATION_JS, JQueryForm.FORM_JS and JqueryUI
        response.render(JavaScriptHeaderItem.forReference(JQueryValidation.VALIDATION_ADDITIONAL_JS));

        // localisation
        response.render(JavaScriptHeaderItem.forReference(JQueryValidation.VALIDATION_LOCALIZATION_JS));

        // clear
        // response.render(JavaScriptHeaderItem.forReference(FILEPICKER_JS));

        StringBuilder sb = new StringBuilder();
        sb.append("function clearFileInput(compid) {").append(";\n");
        sb.append("var comp = $('#'+compid)").append(";\n");
        sb.append("comp.removeAttr('value')").append(";\n");
        // sb.append("comp.removeAttr('aria-invalid')").append(";\n");
        // sb.append("comp.attr('class', 'filestyle')").append(";\n");
        // sb.append("var x = comp.clone()").append(";\n");
        // sb.append("comp.replaceWith( x )").append(";\n");
        // sb.append("comp.parent().find('.error').remove()").append(";\n");
        sb.append("}");
        response.render(JavaScriptHeaderItem.forScript(sb.toString(), "s564879"));

        // validation inject
        FilePickerSettings filePickerSettings = FilePickerSettings.class.cast(getComponentSettings());
        if (filePickerSettings.getMimeType() != null) {

            String formId = getComponent().getForm().getMarkupId();
            StringBuilder initScript = new StringBuilder();
            initScript.append("$(\"#" + formId + "\").validate();").append("\n");
            initScript.append("$(\"#" + getComponent().getMarkupId() + "\").rules('add', { accept: \"" + filePickerSettings.getMimeType() + "\" })")
            .append("\n");
            response.render(OnLoadHeaderItem.forScript(initScript));
        }
    }

    public FileItemFactory getFileItemFactory() {
        if (fileItemFactory == null) {
            fileItemFactory = new DiskFileItemFactory(new FileCleaner());
        }
        return this.fileItemFactory;
    }

    public void setFileItemFactory(FileItemFactory fileItemFactory) {
        this.fileItemFactory = fileItemFactory;
    }

    @Override
    public void onBeforeSubmit() {
        FileUpload fileUpload = getComponent().getFileUpload();
        Class<?> type = type(propertyPath);
        Object parentObject = parentModel.getObject();
        if (FileUpload.class.isAssignableFrom(type)) {
            set(parentObject, (FileUpload) propertyPath, fileUpload);
            return;
        }
        if (File.class.isAssignableFrom(type)) {
            try {
                set(parentObject, (File) propertyPath, fileUpload == null ? null : fileUpload.writeToTempFile());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }
        if (byte[].class.isAssignableFrom(type)) {
            set(parentObject, (byte[]) propertyPath, fileUpload == null ? null : fileUpload.getBytes());
            return;
        }
        if (InputStream.class.isAssignableFrom(type)) {
            try {
                set(parentObject, (InputStream) propertyPath, fileUpload == null ? null : fileUpload.getInputStream());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }
        throw new UnsupportedOperationException("data transport");
    }

    @Override
    public void onAfterSubmit() {
        // nothing to do
    }
}

package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.set;
import static org.tools.hqlbuilder.webservice.wicket.WebHelper.type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.file.FileCleaner;
import org.apache.wicket.util.upload.DiskFileItemFactory;
import org.apache.wicket.util.upload.FileItemFactory;
import org.tools.hqlbuilder.webservice.resources.filestyle.FileStyle;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormRowPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel.FormSubmitInterceptor;

/**
 * based on markusslima bootstrap-filestyle version 1.1.0
 *
 * @see http://markusslima.github.io/bootstrap-filestyle/
 * @see http://www.surrealcms.com/blog/whipping-file-inputs-into-shape-with-bootstrap-3
 */
public class FilePickerPanel<P> extends FormRowPanel<P, List<FileUpload>, FileUploadField> implements FormSubmitInterceptor {
    public static final String BUTTON_TEXT_ID = "buttonText";

    private static final long serialVersionUID = -6943635423428119032L;

    protected FileItemFactory fileItemFactory;

    protected IModel<?> parentModel;

    @SuppressWarnings("unchecked")
    public FilePickerPanel(IModel<?> parentModel, P propertyPath, FormSettings formSettings, FilePickerSettings componentSettings) {
        super(propertyPath, new ListModel<FileUpload>(), formSettings, componentSettings);
        this.parentModel = parentModel;
        setPropertyType((Class<List<FileUpload>>) (new ArrayList<FileUpload>().getClass()));
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
            }
        };
        return comp;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(FileStyle.FILESTYLE_JS));
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
                set(parentObject, (File) propertyPath, fileUpload.writeToTempFile());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }
        if (byte[].class.isAssignableFrom(type)) {
            set(parentObject, (byte[]) propertyPath, fileUpload.getBytes());
            return;
        }
        if (InputStream.class.isAssignableFrom(type)) {
            try {
                set(parentObject, (InputStream) propertyPath, fileUpload.getInputStream());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return;
        }
        throw new UnsupportedOperationException("data transport");
    }

    @Override
    public void onAfterSubmit() {
        //
    }
}

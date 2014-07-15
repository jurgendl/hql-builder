package org.tools.hqlbuilder.webservice.wicket.forms;

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

/**
 * based on version 1.1.0
 *
 * @see http://markusslima.github.io/bootstrap-filestyle/
 */
public class FilePickerPanel<P> extends FormRowPanel<P, List<FileUpload>, FileUploadField> {
    public static final String BUTTON_TEXT_ID = "buttonText";

    private static final long serialVersionUID = -6943635423428119032L;

    private FileItemFactory fileItemFactory;

    @SuppressWarnings("unchecked")
    public FilePickerPanel(P propertyPath, FormSettings formSettings, FilePickerSettings componentSettings) {
        super(propertyPath, new ListModel<FileUpload>(), formSettings, componentSettings);
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
}

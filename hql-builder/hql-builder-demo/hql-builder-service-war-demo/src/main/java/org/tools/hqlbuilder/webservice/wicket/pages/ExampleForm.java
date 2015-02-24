package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.tools.hqlbuilder.webservice.jquery.ui.ckeditor.CKEditor.CKEType;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.services.ServiceInterface;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;
import org.tools.hqlbuilder.webservice.wicket.forms.CKEditorTextAreaSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.CheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.ColorPickerSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.DropDownSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FilePickerHook;
import org.tools.hqlbuilder.webservice.wicket.forms.FilePickerSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.JQueryUIColorPickerSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.ListSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.MultiListSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.NumberFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.TagItTextFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.TextAreaSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.TriStateCheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.pages.Example.ExampleOpts;
import org.tools.hqlbuilder.webservice.wicket.pages.Example.MemFile;

@SuppressWarnings("serial")
public class ExampleForm extends FormPanel<Example> {
    @SpringBean(name = "exampleService")
    private ServiceInterface exampleService;

    @SuppressWarnings("unchecked")
    public ExampleForm(String id) {
        super(id, new DefaultFormActions<Example>() {
            /***/
        }, new FormSettings().setClientsideRequiredValidation(false).setShowPlaceholder(false).setLabelWidth("18em"));

        setFormActions(new DefaultFormActions<Example>() {
            @Override
            public void submitObject(Example example) {
                _submitObject(example);
            }

            @Override
            public Example loadObject() {
                return _loadObject();
            }
        });

        Example proxy = WebHelper.proxy(Example.class);

        getForm().setMultiPart(true);
        getForm().setMaxSize(Bytes.megabytes(10));

        List<String> rndwords = new ArrayList<String>();
        for (int i = 0; i < 128; i++) {
            rndwords.add(RandomStringUtils.randomAlphabetic(16));
        }

        FormElementSettings fset = new FormElementSettings();
        final IChoiceRenderer<ExampleOpts> choiceRenderer = new EnumChoiceRenderer<ExampleOpts>(this);
        IOptionRenderer<ExampleOpts> optionRenderer = new IOptionRenderer<ExampleOpts>() {
            @Override
            public String getDisplayValue(ExampleOpts object) {
                return object == null ? getString("null") : String.valueOf(choiceRenderer.getDisplayValue(object));
            }

            @Override
            public IModel<ExampleOpts> getModel(ExampleOpts value) {
                return Model.of(value);
            }
        };
        ListModel<ExampleOpts> optsChoices = new ListModel<ExampleOpts>(Arrays.asList(ExampleOpts.values()));

        {
            Example tmp = getFormActions().loadObject();
            ArrayList<String> list = new ArrayList<String>(new TreeSet<String>(Arrays.asList(tmp.getLongText().toLowerCase()
                    .replaceAll("[^a-z ]", "").replaceAll("  ", " ").split(" "))));
            list.removeAll(tmp.getManyOptions());
            addPickList(proxy.getManyOptions(), new ListSettings(), new ListModel<String>(list), null);
        }

        nextRow();

        addLocalesDropDown(null, fset, null, null).setPropertyName("locale").setValueModel(new IModel<Locale>() {
            @Override
            public void detach() {
                //
            }

            @Override
            public Locale getObject() {
                Locale locale = WicketSession.get().getLocale();
                return locale;
            }

            @Override
            public void setObject(Locale locale) {
                WicketSession.get().setLocale(locale);
            }
        });
        addDropDown(null, new DropDownSettings(), null, new ListModel<String>(PrimeUI.getThemes())).setPropertyName("theme").inheritId()
        .setValueModel(new IModel<String>() {
            @Override
            public void detach() {
                //
            }

            @Override
            public String getObject() {
                return WicketSession.get().getJQueryUITheme();
            }

            @Override
            public void setObject(String theme) {
                WicketSession.get().setJQueryUITheme(theme);
            }
        });
        addCheckBox(null, new CheckBoxSettings()).setPropertyName("cookies").inheritId().setValueModel(new IModel<Boolean>() {
            @Override
            public void detach() {
                //
            }

            @Override
            public Boolean getObject() {
                return WicketSession.get().getCookies().getUserAllowedCookies();
            }

            @Override
            public void setObject(Boolean userAllowedCookies) {
                WicketSession.get().getCookies().setUserAllowedCookies(userAllowedCookies);
            }
        });
        nextRow();

        addTextField(proxy.getRegistration().getFirstName(), fset).inheritId();
        addTextField(proxy.getRegistration().getLastName(), fset).inheritId();
        addTextField(proxy.getRegistration().getUsername(), fset).inheritId();
        addEmailTextField(proxy.getRegistration().getEmail(), fset).inheritId();
        addPasswordTextField(proxy.getRegistration().getPassword(), new FormElementSettings()).inheritId();
        addDatePicker(proxy.getRegistration().getDateOfBirth(), fset).inheritId();
        nextRow();

        addHidden(proxy.getHidden1());
        addHidden(proxy.getHidden2());

        addTagItTextFieldPanel(proxy.getTags(), new TagItTextFieldSettings(), new ListModel<String>(rndwords));
        addTextField(proxy.getText(), fset.clone());
        addTextField(proxy.getTextAdd(), fset.clone());
        addRadioButtons(proxy.getRadio(), fset, optsChoices, choiceRenderer);
        addTriStateCheckBox(proxy.getTristate(), new TriStateCheckBoxSettings());
        addDropDown(proxy.getCombo(), new DropDownSettings().setNullValid(true), optionRenderer, optsChoices);
        addNumberTextField(proxy.getIntegerv(), new NumberFieldSettings<Integer>(0, 100, 1));
        addSliderField(proxy.getIntegerv(), new NumberFieldSettings<Integer>(0, 100, 1));
        addMultiSelectCheckBox(proxy.getMulti(), fset, optsChoices, choiceRenderer);
        addFilepicker(proxy);
        addColorPicker(proxy.getColor1(), new ColorPickerSettings());
        addColorPicker(proxy.getColor2(), new JQueryUIColorPickerSettings());

        {
            List<String> opt1 = new ArrayList<String>();
            for (int i = 1; i <= 10; i++) {
                opt1.add("option " + (i == 10 ? 0 : i));
            }
            List<String> opt2 = new ArrayList<String>();
            for (int i = 0; i < 26; i++) {
                opt2.add("option " + (char) ('A' + i));
            }
            IModel<String>[] groupLabels = new IModel[] { Model.of("numbers"), Model.of("letters") };
            IOptionRenderer<String> optionRenderer2 = new IOptionRenderer<String>() {
                @Override
                public String getDisplayValue(String object) {
                    return StringUtils.isBlank(object) ? getString("null") : object;
                }

                @Override
                public IModel<String> getModel(String value) {
                    return Model.of(value);
                }
            };
            groupLabels = null;
            ListModel<String>[] opt = new ListModel[] { new ListModel<String>(opt1), new ListModel<String>(opt2) };

            addDropDown(WebHelper.proxy(Example.class).getDropdown(), new DropDownSettings().setNullValid(true), optionRenderer2, opt, groupLabels);
            nextRow();
            addList(WebHelper.proxy(Example.class).getTextExtra(), new ListSettings().setSize(10), optionRenderer2, opt, groupLabels);
        }
        nextRow();
        addTextArea(proxy.getLongText(), new TextAreaSettings().setResizable(false).setRows(5).setCols(100));
        nextRow();
        addCKEditorTextAreaPanel(proxy.getHtmlTextExtra(), new CKEditorTextAreaSettings().setType(CKEType.full));
        nextRow();
        addMultiSelectList(proxy.getRndwords(), new MultiListSettings(), null, new ListModel<String>(rndwords));
    }

    private void addFilepicker(Example proxy) {
        FilePickerHook hook = new FilePickerHook() {
            @Override
            public void write(Collection<FileUpload> files) {
                Example example = Example.class.cast(getFormModel().getObject());
                for (FileUpload fileUpload : files) {
                    MemFile mf = new MemFile();
                    example.getFiles().add(mf);
                    mf.setData(fileUpload.getBytes());
                    mf.setFilename(fileUpload.getClientFileName());
                }
            }

            @Override
            public Collection<String> getCurrentFilenames() {
                Example example = Example.class.cast(getFormModel().getObject());
                List<String> list = new ArrayList<String>();
                for (MemFile mf : example.getFiles()) {
                    list.add(mf.getFilename());
                }
                return list;
            }

            @Override
            public IResourceStream download(String currentFileName) {
                final Example example = Example.class.cast(getFormModel().getObject());
                MemFile pick = new MemFile();
                for (MemFile mf : example.getFiles()) {
                    if (currentFileName.equals(mf.getFilename())) {
                        pick = mf;
                        break;
                    }
                }
                final MemFile picked = pick;
                return new AbstractResourceStream() {
                    @Override
                    public Time lastModifiedTime() {
                        return Time.now();
                    }

                    @Override
                    public Bytes length() {
                        return Bytes.bytes(picked.getData().length);
                    }

                    @Override
                    public InputStream getInputStream() throws ResourceStreamNotFoundException {
                        return new ByteArrayInputStream(picked.getData());
                    }

                    @Override
                    public String getContentType() {
                        return URLConnection.getFileNameMap().getContentTypeFor(picked.getFilename());
                    }

                    @Override
                    public void close() throws IOException {
                        //
                    }
                };
            }

            @Override
            public void clear(Collection<String> filenames) {
                Example example = Example.class.cast(getFormModel().getObject());
                for (MemFile mf : example.getFiles().toArray(new MemFile[example.getFiles().size()])) {
                    if (filenames.contains(mf.getFilename())) {
                        example.getFiles().remove(mf);
                    }
                }
            }
        };
        addFilePicker(proxy.getFiles(), new FilePickerSettings().setMimeType("application/pdf").setMultiple(true).setConsistentLook(true), hook);
    }

    protected void _submitObject(Example example) {
        System.out.println(">" + example);
        exampleService.save(getInstanceId(), example);
    }

    protected Example _loadObject() {
        return exampleService.getExample(getInstanceId());
    }

    protected String getInstanceId() {
        return WicketSession.get().getId();
    }
}

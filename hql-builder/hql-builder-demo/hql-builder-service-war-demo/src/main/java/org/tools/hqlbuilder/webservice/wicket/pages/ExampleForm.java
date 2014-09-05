package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.services.ServiceInterface;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
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
import org.tools.hqlbuilder.webservice.wicket.forms.NumberFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.RangeFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.TextAreaSettings;
import org.tools.hqlbuilder.webservice.wicket.pages.Example.ExampleOpts;
import org.tools.hqlbuilder.webservice.wicket.pages.Example.MemFile;

@SuppressWarnings("serial")
public class ExampleForm extends FormPanel<Example> {
    @SpringBean(name = "exampleService")
    private ServiceInterface exampleService;

    @SuppressWarnings("unchecked")
    public ExampleForm(String id) {
        super(id);
        setFormSettings(new FormSettings().setClientsideRequiredValidation(false).setColumns(2));
        setFormActions(new DefaultFormActions<Example>() {
            @Override
            public void submitObject(Example example) {
                exampleService.save(getInstanceId(), example);
                WicketSession.get().setLocale(example.getLocale());
                WicketSession.get().setJQueryUITheme(example.getTheme());
            }

            public String getInstanceId() {
                return WicketSession.get().getId();
            }

            @Override
            public Example loadObject() {
                Example example = exampleService.getExample(getInstanceId());
                example.setLocale(WicketSession.get().getLocale());
                example.setTheme(WicketSession.get().getJQueryUITheme());
                return example;
            }
        });

        Example proxy = WebHelper.proxy(Example.class);

        getForm().setMultiPart(true);
        getForm().setMaxSize(Bytes.megabytes(100));

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
        Converter<Long, Date> dateConverter = new Converter<Long, Date>() {
            @Override
            public Date toType(Long object) {
                return new Date(object == null ? 0l : object);
            }

            @Override
            public Long fromType(Date object) {
                return object == null ? 0l : object.getTime();
            }
        };

        addLocalesDropDown(proxy.getLocale(), fset, null, null);
        addDropDown(proxy.getTheme(), new DropDownSettings(), null, new ListModel<String>(PrimeUI.getThemes()));
        addTextField(proxy.getText(), fset.clone().setRequired(true));
        addTextField(proxy.getTextAdd(), fset.clone().setRequired(true));
        addEmailTextField(proxy.getEmail(), fset);
        addHidden(proxy.getHidden1());
        addCheckBox(proxy.getCheck(), fset);
        addRadioButtons(proxy.getRadio(), fset, optsChoices, choiceRenderer);
        addDropDown(proxy.getCombo(), new DropDownSettings().setNullValid(true), optionRenderer, optsChoices);
        addDatePicker(proxy.getDate1(), fset);
        addDatePicker(proxy.getDate2(), fset, dateConverter);
        addPasswordTextField(proxy.getPassword(), new FormElementSettings());
        addNumberField(proxy.getBytev(), new NumberFieldSettings<Byte>(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 1));
        addNumberField(proxy.getShortv(), new NumberFieldSettings<Short>(Short.MIN_VALUE, Short.MAX_VALUE, (short) 1));
        addNumberField(proxy.getIntegerv(), new NumberFieldSettings<Integer>(Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        addNumberField(proxy.getLongv(), new NumberFieldSettings<Long>(Long.MIN_VALUE, Long.MAX_VALUE, 1l));
        addNumberField(proxy.getFloatv(), new NumberFieldSettings<Float>(Float.MIN_VALUE, Float.MAX_VALUE, 1f));
        addNumberField(proxy.getDoublev(), new NumberFieldSettings<Double>((double) Float.MIN_VALUE, (double) Float.MAX_VALUE, 1d));
        addNumberTextField(proxy.getBytev(), new NumberFieldSettings<Byte>(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 1));
        addNumberTextField(proxy.getShortv(), new NumberFieldSettings<Short>(Short.MIN_VALUE, Short.MAX_VALUE, (short) 1));
        addNumberTextField(proxy.getIntegerv(), new NumberFieldSettings<Integer>(Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        addNumberTextField(proxy.getLongv(), new NumberFieldSettings<Long>(Long.MIN_VALUE, Long.MAX_VALUE, 1l));
        addNumberTextField(proxy.getFloatv(), new NumberFieldSettings<Float>(Float.MIN_VALUE, Float.MAX_VALUE, 1f));
        addNumberTextField(proxy.getDoublev(), new NumberFieldSettings<Double>((double) Float.MIN_VALUE, (double) Float.MAX_VALUE, 1d));
        addRangeField(proxy.getByter(), new RangeFieldSettings<Byte>((byte) 0, (byte) 100, (byte) 1));
        addRangeField(proxy.getShortr(), new RangeFieldSettings<Short>((short) 0, (short) 100, (short) 1));
        addRangeField(proxy.getIntegerr(), new RangeFieldSettings<Integer>(0, 100, 1).setTickStep(10));
        addRangeField(proxy.getLongr(), new RangeFieldSettings<Long>(0l, 100l, 1l));
        addRangeField(proxy.getFloatr(), new RangeFieldSettings<Float>(0f, 100f, 1f));
        addRangeField(proxy.getDoubler(), new RangeFieldSettings<Double>(0d, 100d, 1d));
        addMultiSelectCheckBox(proxy.getMulti(), fset, optsChoices, choiceRenderer);
        addHidden(proxy.getHidden2());
        addFilepicker(proxy);
        addColorPicker(proxy.getColor(), new ColorPickerSettings());
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

            addDropDown(WebHelper.proxy(Example.class).getText(), new DropDownSettings().setNullValid(true), optionRenderer2, opt, groupLabels);
            nextRow();
            addList(WebHelper.proxy(Example.class).getTextExtra(), new ListSettings().setSize(10), optionRenderer2, opt, groupLabels);
        }
        nextRow();
        addTextArea(proxy);
    }

    private void addTextArea(Example proxy) {
        setFormSettings(new FormSettings().setColumns(1));
        addTextArea(proxy.getLongText(), new TextAreaSettings().setResizable(false).setRows(5).setCols(100));
        setFormSettings(new FormSettings().setColumns(2));
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
}

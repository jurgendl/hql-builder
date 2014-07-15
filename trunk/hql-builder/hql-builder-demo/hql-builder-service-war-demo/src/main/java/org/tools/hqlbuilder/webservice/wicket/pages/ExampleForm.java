package org.tools.hqlbuilder.webservice.wicket.pages;

import java.util.Arrays;
import java.util.Date;

import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FilePickerSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.NumberFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.RangeFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.TextAreaSettings;
import org.tools.hqlbuilder.webservice.wicket.pages.Example.ExampleOpts;

@SuppressWarnings("serial")
public class ExampleForm extends FormPanel<Example> {
    public ExampleForm(String id) {
        super(id, Model.of(new Example()), true, new DefaultFormActions<Example>() {
            @Override
            public void submit(IModel<Example> m) {
                System.out.println(m.getObject());
                WicketApplication.get().getZussStyle().printStyling(System.out);
            }
        }.setAjax(false));

        Example proxy = WebHelper.proxy(Example.class);

        form.setMultiPart(true);

        getFormSettings().setClientsideRequiredValidation(false);

        FormElementSettings fset = new FormElementSettings();
        IChoiceRenderer<ExampleOpts> optsRenderer = new EnumChoiceRenderer<ExampleOpts>(this);
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

        boolean dont = false;
        if (dont) {
            addHidden(proxy.getHidden1());
            addHidden(proxy.getHidden2());
            addCheckBox(proxy.getCheck(), fset);
            addRadioButtons(proxy.getRadio(), fset, optsChoices, optsRenderer);
            addDropDown(proxy.getCombo(), fset, optsChoices, optsRenderer);
            addEmailTextField(proxy.getEmail(), fset);
            addTextField(proxy.getText(), fset);
            addDatePicker(proxy.getDate1(), fset);
            addDatePicker(proxy.getDate2(), fset, dateConverter);
            addPasswordTextField(proxy.getPassword(), new FormElementSettings());
            addTextArea(proxy.getLongText(), new TextAreaSettings());
            addNumberField(proxy.getBytev(), new NumberFieldSettings<Byte>(Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 1));
            addNumberField(proxy.getShortv(), new NumberFieldSettings<Short>(Short.MIN_VALUE, Short.MAX_VALUE, (short) 1));
            addNumberField(proxy.getIntegerv(), new NumberFieldSettings<Integer>(Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
            addNumberField(proxy.getLongv(), new NumberFieldSettings<Long>(Long.MIN_VALUE, Long.MAX_VALUE, 1l));
            addNumberField(proxy.getFloatv(), new NumberFieldSettings<Float>(Float.MIN_VALUE, Float.MAX_VALUE, 1f));
            addNumberField(proxy.getDoublev(), new NumberFieldSettings<Double>((double) Float.MIN_VALUE, (double) Float.MAX_VALUE, 1d));
            addRangeField(proxy.getByter(), new RangeFieldSettings<Byte>((byte) 0, (byte) 100, (byte) 1));
            addRangeField(proxy.getShortr(), new RangeFieldSettings<Short>((short) 0, (short) 100, (short) 1));
            addRangeField(proxy.getIntegerr(), new RangeFieldSettings<Integer>(0, 100, 1).setTickStep(10));
            addRangeField(proxy.getLongr(), new RangeFieldSettings<Long>(0l, 100l, 1l));
            addRangeField(proxy.getFloatr(), new RangeFieldSettings<Float>(0f, 100f, 1f));
            addRangeField(proxy.getDoubler(), new RangeFieldSettings<Double>(0d, 100d, 1d));
            addFilePicker(proxy.getData(), new FilePickerSettings().setMimeType("application/pdf"));
        }
        addMultiSelectCheckBox(proxy.getMulti(), fset, optsChoices, optsRenderer);
    }
}

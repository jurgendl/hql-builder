package org.tools.hqlbuilder.webservice.wicket.pages;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.create;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.NumberFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.TextAreaSettings;
import org.tools.hqlbuilder.webservice.wicket.pages.ExampleForm.Example;

@SuppressWarnings("serial")
public class ExampleForm extends FormPanel<Example> {
    public ExampleForm(String id) {
        super(id, Model.of(new Example()), true, new DefaultFormActions<Example>() {
            @Override
            public void submit(IModel<Example> m) {
                WicketSession.get().printStyling(System.out);
            }
        }.setAjax(false));

        Example proxy = create(Example.class);

        getFormSettings().setClientsideRequiredValidation(false);

        FormElementSettings fset = new FormElementSettings().setRequired(true);
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
        addNumberField(proxy.getDoublev(), new NumberFieldSettings<Double>(Double.MIN_VALUE, Double.MAX_VALUE, 1d));
    }

    public static enum ExampleOpts {
        OPT1, OPT2, OPT3, OPT4, OPT5;
    }

    public static class Example implements Serializable {
        private String hidden1 = "hid";

        private Integer hidden2 = 10;

        private String password = null;

        private String text = "test@gmail.com";

        private String email = "testString";

        private String longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris hendrerit accumsan libero, sed scelerisque velit posuere vehicula. Vestibulum vestibulum dignissim libero, sed porta felis auctor at. Vestibulum malesuada massa nulla, eu vestibulum leo tristique id. Fusce in lorem aliquet, imperdiet tellus consequat, viverra lorem. Vestibulum odio arcu, interdum non erat quis, commodo aliquam enim. Donec hendrerit adipiscing nisl at aliquet. Etiam vitae iaculis eros, sit amet pulvinar lacus. Vivamus est eros, suscipit eu fermentum nec, sagittis ac lorem. Cras iaculis quam a ipsum interdum facilisis quis in magna. Vestibulum eget sodales tortor. In dapibus ac diam dignissim bibendum. Duis vel leo id tortor tempor lobortis. Fusce pulvinar in est eleifend aliquam. Duis ac rhoncus sem, id rhoncus dolor.\n\nFusce mollis turpis interdum arcu mollis ultrices. Maecenas posuere convallis vestibulum. Donec interdum molestie metus, quis interdum tellus pellentesque sit amet. Pellentesque tincidunt ipsum imperdiet, aliquam ipsum vel, hendrerit diam. Duis massa augue, vehicula et condimentum non, posuere id nulla. Etiam vehicula tortor in ligula mollis semper. Ut viverra tortor nec lacinia congue. Maecenas at dui orci.";

        private ExampleOpts radio = ExampleOpts.OPT1;

        private ExampleOpts combo = ExampleOpts.OPT1;

        private Boolean check = Boolean.FALSE;

        @SuppressWarnings("deprecation")
        private Date date1 = new Date(2000, 4, 4);

        @SuppressWarnings("deprecation")
        private Long date2 = new Date(2000, 4, 4).getTime();

        private Integer integerv = Integer.MAX_VALUE;

        private Long longv = Long.MAX_VALUE;

        private Short shortv = Short.MAX_VALUE;

        private Double doublev = Double.MAX_VALUE;

        private Float floatv = Float.MAX_VALUE;

        private Byte bytev = Byte.MAX_VALUE;

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public ExampleOpts getRadio() {
            return this.radio;
        }

        public ExampleOpts getCombo() {
            return this.combo;
        }

        public void setRadio(ExampleOpts radio) {
            this.radio = radio;
        }

        public void setCombo(ExampleOpts combo) {
            this.combo = combo;
        }

        public String getText() {
            return this.text;
        }

        public String getEmail() {
            return this.email;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getCheck() {
            return this.check;
        }

        public void setCheck(Boolean check) {
            this.check = check;
        }

        public String getLongText() {
            return this.longText;
        }

        public void setLongText(String longText) {
            this.longText = longText;
        }

        public String getHidden1() {
            return this.hidden1;
        }

        public Integer getHidden2() {
            return this.hidden2;
        }

        public void setHidden1(String hidden1) {
            this.hidden1 = hidden1;
        }

        public void setHidden2(Integer hidden2) {
            this.hidden2 = hidden2;
        }

        public Date getDate1() {
            return this.date1;
        }

        public Long getDate2() {
            return this.date2;
        }

        public void setDate1(Date date1) {
            this.date1 = date1;
        }

        public void setDate2(Long date2) {
            this.date2 = date2;
        }

        public Integer getIntegerv() {
            return this.integerv;
        }

        public Long getLongv() {
            return this.longv;
        }

        public Short getShortv() {
            return this.shortv;
        }

        public Double getDoublev() {
            return this.doublev;
        }

        public Float getFloatv() {
            return this.floatv;
        }

        public void setIntegerv(Integer integerv) {
            this.integerv = integerv;
        }

        public void setLongv(Long longv) {
            this.longv = longv;
        }

        public void setShortv(Short shortv) {
            this.shortv = shortv;
        }

        public void setDoublev(Double doublev) {
            this.doublev = doublev;
        }

        public void setFloatv(Float floatv) {
            this.floatv = floatv;
        }

        public Byte getBytev() {
            return this.bytev;
        }

        public void setBytev(Byte bytev) {
            this.bytev = bytev;
        }
    }
}

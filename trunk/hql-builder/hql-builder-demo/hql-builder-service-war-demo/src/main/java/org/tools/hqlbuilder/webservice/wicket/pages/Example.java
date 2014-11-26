package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.tools.hqlbuilder.demo.Registration;

public class Example implements Serializable {
    private static final long serialVersionUID = 8505173683889617800L;

    protected Serializable id;

    public static enum ExampleOpts {
        OPT1, OPT2, OPT3, OPT4, OPT5;
    }

    public static class MemFile implements Serializable {
        private static final long serialVersionUID = -6191656254956606060L;

        protected String filename;

        protected byte[] data;

        public String getFilename() {
            return this.filename;
        }

        public byte[] getData() {
            return this.data;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return filename + ":" + data.length;
        }
    }

    private List<ExampleOpts> multi;

    private List<MemFile> files = new ArrayList<MemFile>();

    private String hidden1 = "hid";

    private Integer hidden2 = 10;

    private Registration registration = new Registration();

    private String text;

    private String dropdown;

    private String tags;

    private String textAdd;

    private String textExtra;

    private String longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris hendrerit accumsan libero, sed scelerisque velit posuere vehicula. Vestibulum vestibulum dignissim libero, sed porta felis auctor at. Vestibulum malesuada massa nulla, eu vestibulum leo tristique id. Fusce in lorem aliquet, imperdiet tellus consequat, viverra lorem. Vestibulum odio arcu, interdum non erat quis, commodo aliquam enim. Donec hendrerit adipiscing nisl at aliquet. Etiam vitae iaculis eros, sit amet pulvinar lacus. Vivamus est eros, suscipit eu fermentum nec, sagittis ac lorem. Cras iaculis quam a ipsum interdum facilisis quis in magna. Vestibulum eget sodales tortor. In dapibus ac diam dignissim bibendum. Duis vel leo id tortor tempor lobortis. Fusce pulvinar in est eleifend aliquam. Duis ac rhoncus sem, id rhoncus dolor.\n\nFusce mollis turpis interdum arcu mollis ultrices. Maecenas posuere convallis vestibulum. Donec interdum molestie metus, quis interdum tellus pellentesque sit amet. Pellentesque tincidunt ipsum imperdiet, aliquam ipsum vel, hendrerit diam. Duis massa augue, vehicula et condimentum non, posuere id nulla. Etiam vehicula tortor in ligula mollis semper. Ut viverra tortor nec lacinia congue. Maecenas at dui orci.";

    private ExampleOpts radio = ExampleOpts.OPT1;

    private ExampleOpts combo = ExampleOpts.OPT1;

    private ExampleOpts list = ExampleOpts.OPT1;

    private Boolean check = Boolean.FALSE;

    private Date date1 = new Date();

    private Long date2 = new Date().getTime();

    private Integer integerv = 50;

    private Long longv = 50l;

    private Short shortv = (short) 50;

    private Double doublev = 50d;

    private Float floatv = 50f;

    private Byte bytev = (byte) 50;

    private Integer integerr = 50;

    private Long longr = 50l;

    private Short shortr = (short) 50;

    private Double doubler = 50d;

    private Float floatr = 50f;

    private Byte byter = (byte) 50;

    private String color1 = "#005599";

    private String color2 = "#995500";

    private String htmlText = "<u>underline</u> <b>bold</b> <i>italic</i>";

    private String htmlTextExtra = "<u>underline</u> <b>bold</b> <i>italic</i>";

    private Boolean tristate;

    private List<String> manyOptions;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", id).append("tristate", tristate).append("multi", multi)
                .append("files", files).append("hidden1", hidden1).append("hidden2", hidden2).append("registration", registration)
                .append("text", text).append("tags", tags).append("textAdd", textAdd).append("textExtra", textExtra).append("radio", radio)
                .append("combo", combo).append("list", list).append("check", check).append("date1", date1).append("date2", date2)
                .append("integerv", integerv).append("longv", longv).append("shortv", shortv).append("doublev", doublev).append("floatv", floatv)
                .append("bytev", bytev).append("integerr", integerr).append("longr", longr).append("shortr", shortr).append("doubler", doubler)
                .append("floatr", floatr).append("byter", byter).append("color1", color1).append("color2", color2).append("htmlText", htmlText)
                .append("htmlTextExtra", htmlTextExtra).append("manyOptions", manyOptions).toString();
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

    public void setText(String text) {
        this.text = text;
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

    public Integer getIntegerr() {
        return this.integerr;
    }

    public Long getLongr() {
        return this.longr;
    }

    public Short getShortr() {
        return this.shortr;
    }

    public Double getDoubler() {
        return this.doubler;
    }

    public Float getFloatr() {
        return this.floatr;
    }

    public Byte getByter() {
        return this.byter;
    }

    public void setIntegerr(Integer integerr) {
        this.integerr = integerr;
    }

    public void setLongr(Long longr) {
        this.longr = longr;
    }

    public void setShortr(Short shortr) {
        this.shortr = shortr;
    }

    public void setDoubler(Double doubler) {
        this.doubler = doubler;
    }

    public void setFloatr(Float floatr) {
        this.floatr = floatr;
    }

    public void setByter(Byte byter) {
        this.byter = byter;
    }

    public List<ExampleOpts> getMulti() {
        return this.multi;
    }

    public void setMulti(List<ExampleOpts> multi) {
        this.multi = multi;
    }

    public List<MemFile> getFiles() {
        return this.files;
    }

    public void setFiles(List<MemFile> files) {
        this.files = files;
    }

    public Serializable getId() {
        return this.id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public ExampleOpts getList() {
        return this.list;
    }

    public void setList(ExampleOpts list) {
        this.list = list;
    }

    public String getTextExtra() {
        return this.textExtra;
    }

    public void setTextExtra(String textExtra) {
        this.textExtra = textExtra;
    }

    public String getTextAdd() {
        return this.textAdd;
    }

    public void setTextAdd(String textAdd) {
        this.textAdd = textAdd;
    }

    public String getColor2() {
        return this.color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public Registration getRegistration() {
        return this.registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public String getHtmlText() {
        return this.htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getHtmlTextExtra() {
        return this.htmlTextExtra;
    }

    public void setHtmlTextExtra(String htmlTextExtra) {
        this.htmlTextExtra = htmlTextExtra;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getTristate() {
        return this.tristate;
    }

    public void setTristate(Boolean tristate) {
        this.tristate = tristate;
    }

    public String getColor1() {
        return this.color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public List<String> getManyOptions() {
        return this.manyOptions;
    }

    public void setManyOptions(List<String> manyOptions) {
        this.manyOptions = manyOptions;
    }

    public String getDropdown() {
        return this.dropdown;
    }

    public void setDropdown(String dropdown) {
        this.dropdown = dropdown;
    }
}

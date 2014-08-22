package org.tools.hqlbuilder.webservice.wicket.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private String password = null;

    private String email = "test@gmail.com";

    private String text;

    private String textExtra;

    private String longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris hendrerit accumsan libero, sed scelerisque velit posuere vehicula. Vestibulum vestibulum dignissim libero, sed porta felis auctor at. Vestibulum malesuada massa nulla, eu vestibulum leo tristique id. Fusce in lorem aliquet, imperdiet tellus consequat, viverra lorem. Vestibulum odio arcu, interdum non erat quis, commodo aliquam enim. Donec hendrerit adipiscing nisl at aliquet. Etiam vitae iaculis eros, sit amet pulvinar lacus. Vivamus est eros, suscipit eu fermentum nec, sagittis ac lorem. Cras iaculis quam a ipsum interdum facilisis quis in magna. Vestibulum eget sodales tortor. In dapibus ac diam dignissim bibendum. Duis vel leo id tortor tempor lobortis. Fusce pulvinar in est eleifend aliquam. Duis ac rhoncus sem, id rhoncus dolor.\n\nFusce mollis turpis interdum arcu mollis ultrices. Maecenas posuere convallis vestibulum. Donec interdum molestie metus, quis interdum tellus pellentesque sit amet. Pellentesque tincidunt ipsum imperdiet, aliquam ipsum vel, hendrerit diam. Duis massa augue, vehicula et condimentum non, posuere id nulla. Etiam vehicula tortor in ligula mollis semper. Ut viverra tortor nec lacinia congue. Maecenas at dui orci.";

    private ExampleOpts radio = ExampleOpts.OPT1;

    private ExampleOpts combo = ExampleOpts.OPT1;

    private ExampleOpts list = ExampleOpts.OPT1;

    private Boolean check = Boolean.FALSE;

    @SuppressWarnings("deprecation")
    private Date date1 = new Date(2000, 4, 4);

    @SuppressWarnings("deprecation")
    private Long date2 = new Date(2000, 4, 4).getTime();

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

    private Locale locale = Locale.getDefault();

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

    @Override
    public String toString() {
        return "Example[" + hashCode() + ",id=" + id + ",files=" + getFiles() + ", hidden1=" + this.hidden1 + ", hidden2=" + this.hidden2
                + ", password=" + this.password + ", text=" + this.text + ", email=" + this.email + ", longText?=" + (this.longText != null)
                + ", radio=" + this.radio + ", combo=" + this.combo + ", check=" + this.check + ", date1=" + this.date1 + ", date2=" + this.date2
                + ", integerv=" + this.integerv + ", longv=" + this.longv + ", shortv=" + this.shortv + ", doublev=" + this.doublev + ", floatv="
                + this.floatv + ", bytev=" + this.bytev + ", integerr=" + this.integerr + ", longr=" + this.longr + ", shortr=" + this.shortr
                + ", doubler=" + this.doubler + ", floatr=" + this.floatr + ", byter=" + this.byter + ", multi=" + this.multi + "]";
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

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
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
}

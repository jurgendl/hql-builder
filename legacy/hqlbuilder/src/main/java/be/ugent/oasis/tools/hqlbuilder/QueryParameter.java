package be.ugent.oasis.tools.hqlbuilder;

import java.io.Serializable;

/**
 * parameter
 * 
 * @author jdlandsh
 */
public class QueryParameter implements Serializable, Comparable<QueryParameter> {
    /** serialVersionUID */
    private static final long serialVersionUID = 2308140856360992628L;

    /** waarde */
    private transient Object value;

    /** naam */
    private String name;

    /** text */
    private String text;

    /** toString */
    private transient String toString;

    /** type */
    private String type;

    public QueryParameter() {
        super();
    }

    /**
     * Instantieer een nieuwe JSParameter
     * 
     * @param text
     * @param name
     * @param value
     */
    public QueryParameter(String text, String name, Object value, String type) {
        this.text = text;
        this.name = name;
        this.type = type;
        this.value = value;

        afterInit();
    }

    /**
     * afterInit
     */
    public void afterInit() {
        StringBuilder sb = new StringBuilder();

        if (name != null) {
            sb.append(name).append("=");
        }

        if (value == null) {
            sb.append("null");
        } else {
            String cb = value.getClass().getName().replaceAll("java.lang.", "");
            sb.append(cb).append(" ").append(value.toString());
        }

        toString = sb.toString();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return toString;
    }

    /**
     * Getter voor value
     * 
     * @return Returns the value.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Setter voor value
     * 
     * @param value The value to set.
     */
    public void setValue(Object value) {
        this.value = value;
        afterInit();
    }

    /**
     * Getter voor name
     * 
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter voor name
     * 
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
        afterInit();
    }

    /**
     * Getter voor text
     * 
     * @return Returns the text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Setter voor text
     * 
     * @param text The text to set.
     */
    public void setText(String text) {
        this.text = text;
        afterInit();
    }

    /**
     * Getter voor toString
     * 
     * @return Returns the toString.
     */
    public String getToString() {
        return this.toString;
    }

    /**
     * Setter voor toString
     * 
     * @param toString The toString to set.
     */
    public void setToString(String toString) {
        this.toString = toString;
    }

    /**
     * Getter voor type
     * 
     * @return Returns the type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter voor type
     * 
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
        afterInit();
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(QueryParameter o) {
        return 0;
    }
}

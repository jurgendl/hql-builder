package org.tools.hqlbuilder.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.hibernate.Hibernate;
import org.hibernate.type.AdaptedImmutableType;
import org.hibernate.type.DbTimestampType;
import org.hibernate.type.Type;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class DefaultTypeFactory {
    private static final Map BASIC_TYPES;

    static {
        HashMap basics = new HashMap();
        basics.put(boolean.class.getName(), Hibernate.BOOLEAN);
        basics.put(long.class.getName(), Hibernate.LONG);
        basics.put(short.class.getName(), Hibernate.SHORT);
        basics.put(int.class.getName(), Hibernate.INTEGER);
        basics.put(byte.class.getName(), Hibernate.BYTE);
        basics.put(float.class.getName(), Hibernate.FLOAT);
        basics.put(double.class.getName(), Hibernate.DOUBLE);
        basics.put(char.class.getName(), Hibernate.CHARACTER);
        basics.put(Hibernate.CHARACTER.getName(), Hibernate.CHARACTER);
        basics.put(Hibernate.INTEGER.getName(), Hibernate.INTEGER);
        basics.put(Hibernate.STRING.getName(), Hibernate.STRING);
        basics.put(Hibernate.DATE.getName(), Hibernate.DATE);
        basics.put(Hibernate.TIME.getName(), Hibernate.TIME);
        basics.put(Hibernate.TIMESTAMP.getName(), Hibernate.TIMESTAMP);
        basics.put("dbtimestamp", new DbTimestampType());
        basics.put(Hibernate.LOCALE.getName(), Hibernate.LOCALE);
        basics.put(Hibernate.CALENDAR.getName(), Hibernate.CALENDAR);
        basics.put(Hibernate.CALENDAR_DATE.getName(), Hibernate.CALENDAR_DATE);
        basics.put(Hibernate.CURRENCY.getName(), Hibernate.CURRENCY);
        basics.put(Hibernate.TIMEZONE.getName(), Hibernate.TIMEZONE);
        basics.put(Hibernate.CLASS.getName(), Hibernate.CLASS);
        basics.put(Hibernate.TRUE_FALSE.getName(), Hibernate.TRUE_FALSE);
        basics.put(Hibernate.YES_NO.getName(), Hibernate.YES_NO);
        basics.put(Hibernate.BINARY.getName(), Hibernate.BINARY);
        basics.put(Hibernate.TEXT.getName(), Hibernate.TEXT);
        basics.put(Hibernate.BLOB.getName(), Hibernate.BLOB);
        basics.put(Hibernate.CLOB.getName(), Hibernate.CLOB);
        basics.put(Hibernate.BIG_DECIMAL.getName(), Hibernate.BIG_DECIMAL);
        basics.put(Hibernate.BIG_INTEGER.getName(), Hibernate.BIG_INTEGER);
        basics.put(Hibernate.SERIALIZABLE.getName(), Hibernate.SERIALIZABLE);
        basics.put(Hibernate.OBJECT.getName(), Hibernate.OBJECT);
        basics.put(Boolean.class.getName(), Hibernate.BOOLEAN);
        basics.put(Long.class.getName(), Hibernate.LONG);
        basics.put(Short.class.getName(), Hibernate.SHORT);
        basics.put(Integer.class.getName(), Hibernate.INTEGER);
        basics.put(Byte.class.getName(), Hibernate.BYTE);
        basics.put(Float.class.getName(), Hibernate.FLOAT);
        basics.put(Double.class.getName(), Hibernate.DOUBLE);
        basics.put(Character.class.getName(), Hibernate.CHARACTER);
        basics.put(String.class.getName(), Hibernate.STRING);
        basics.put(java.util.Date.class.getName(), Hibernate.TIMESTAMP);
        basics.put(Time.class.getName(), Hibernate.TIME);
        basics.put(Timestamp.class.getName(), Hibernate.TIMESTAMP);
        basics.put(java.sql.Date.class.getName(), Hibernate.DATE);
        basics.put(BigDecimal.class.getName(), Hibernate.BIG_DECIMAL);
        basics.put(BigInteger.class.getName(), Hibernate.BIG_INTEGER);
        basics.put(Locale.class.getName(), Hibernate.LOCALE);
        basics.put(Calendar.class.getName(), Hibernate.CALENDAR);
        basics.put(GregorianCalendar.class.getName(), Hibernate.CALENDAR);
        // if (CurrencyType.CURRENCY_CLASS != null) {
        // basics.put(CurrencyType.CURRENCY_CLASS.getName(), Hibernate.CURRENCY);
        // }
        basics.put(TimeZone.class.getName(), Hibernate.TIMEZONE);
        basics.put(Object.class.getName(), Hibernate.OBJECT);
        basics.put(Class.class.getName(), Hibernate.CLASS);
        basics.put(byte[].class.getName(), Hibernate.BINARY);
        basics.put("byte[]", Hibernate.BINARY);
        basics.put(Byte[].class.getName(), Hibernate.WRAPPER_BINARY);
        basics.put("Byte[]", Hibernate.WRAPPER_BINARY);
        basics.put(char[].class.getName(), Hibernate.CHAR_ARRAY);
        basics.put("char[]", Hibernate.CHAR_ARRAY);
        basics.put(Character[].class.getName(), Hibernate.CHARACTER_ARRAY);
        basics.put("Character[]", Hibernate.CHARACTER_ARRAY);
        basics.put(Blob.class.getName(), Hibernate.BLOB);
        basics.put(Clob.class.getName(), Hibernate.CLOB);
        basics.put(Serializable.class.getName(), Hibernate.SERIALIZABLE);

        Type type = new AdaptedImmutableType(Hibernate.DATE);
        basics.put(type.getName(), type);
        type = new AdaptedImmutableType(Hibernate.TIME);
        basics.put(type.getName(), type);
        type = new AdaptedImmutableType(Hibernate.TIMESTAMP);
        basics.put(type.getName(), type);
        type = new AdaptedImmutableType(new DbTimestampType());
        basics.put(type.getName(), type);
        type = new AdaptedImmutableType(Hibernate.CALENDAR);
        basics.put(type.getName(), type);
        type = new AdaptedImmutableType(Hibernate.CALENDAR_DATE);
        basics.put(type.getName(), type);
        type = new AdaptedImmutableType(Hibernate.SERIALIZABLE);
        basics.put(type.getName(), type);
        type = new AdaptedImmutableType(Hibernate.BINARY);
        basics.put(type.getName(), type);

        basics.remove("java.lang.Object");
        basics.remove("object");

        BASIC_TYPES = Collections.unmodifiableMap(basics);
    }

    public static Type resolve(String name) {
        return basic(name);
    }

    public static Type resolve(Class clas) {
        return basic(clas.getName());
    }

    public static Type basic(String name) {
        return (Type) BASIC_TYPES.get(name);
    }
}

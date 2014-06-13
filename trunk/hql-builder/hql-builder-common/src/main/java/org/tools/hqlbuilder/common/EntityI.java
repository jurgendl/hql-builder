package org.tools.hqlbuilder.common;

import java.io.Serializable;

import org.hibernate.annotations.AccessType;

@AccessType("field")
public interface EntityI extends Serializable {
    public static final String ID = "id";

    public static final String VERSION = "version";
}

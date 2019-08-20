package org.tools.hqlbuilder.common;

import java.io.Serializable;


@SuppressWarnings("deprecation")
@org.hibernate.annotations.AccessType("field")
public interface EntityI extends Serializable {
    public static final String ID = "id";

    public static final String VERSION = "version";
}

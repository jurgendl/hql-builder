package org.tools.hqlbuilder.test;

import java.io.Serializable;

import org.hibernate.annotations.AccessType;

@AccessType("field")
public interface EntityI extends Serializable {
    //
}

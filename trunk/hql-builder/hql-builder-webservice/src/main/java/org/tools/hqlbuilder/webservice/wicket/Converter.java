package org.tools.hqlbuilder.webservice.wicket;

import java.io.Serializable;

public interface Converter<X, T> extends Serializable {
    public abstract T toType(X object);

    public abstract X fromType(T object);
}
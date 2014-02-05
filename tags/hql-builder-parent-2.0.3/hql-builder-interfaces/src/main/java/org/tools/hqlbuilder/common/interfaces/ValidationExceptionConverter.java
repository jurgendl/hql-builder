package org.tools.hqlbuilder.common.interfaces;

import org.tools.hqlbuilder.common.exceptions.ValidationException;

public interface ValidationExceptionConverter {
    public ValidationException convert(Exception ex);
}

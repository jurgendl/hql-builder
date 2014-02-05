package org.tools.hqlbuilder.common.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.InvalidStateException;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.common.interfaces.ValidationExceptionConverter;

public class HibernateValidationConverter implements ValidationExceptionConverter {
    /**
     * @see org.tools.hqlbuilder.common.interfaces.ValidationExceptionConverter#convert(java.lang.Exception)
     */
    @Override
    public ValidationException convert(Exception e) {
        org.hibernate.validator.InvalidStateException ex = (InvalidStateException) e;
        List<org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue> ivs = new ArrayList<org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue>();
        for (org.hibernate.validator.InvalidValue iv : ex.getInvalidValues()) {
            ivs.add(new org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue(iv.getBean(), iv.getBeanClass(), iv.getMessage(), iv
                    .getPropertyName(), iv.getPropertyPath(), iv.getRootBean(), iv.getValue()));
        }
        return new ValidationException(ex.getMessage(), ivs);
    }
}

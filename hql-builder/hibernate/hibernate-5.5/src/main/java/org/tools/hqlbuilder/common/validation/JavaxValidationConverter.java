package org.tools.hqlbuilder.common.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.common.interfaces.ValidationExceptionConverter;

public class JavaxValidationConverter implements ValidationExceptionConverter {
    /**
     * @see org.tools.hqlbuilder.common.interfaces.ValidationExceptionConverter#convert(java.lang.Exception)
     */
    @Override
    public ValidationException convert(Exception e) {
        javax.validation.ConstraintViolationException ex = (ConstraintViolationException) e;
        List<org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue> ivs = new ArrayList<org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue>();
        for (javax.validation.ConstraintViolation<?> iv : ex.getConstraintViolations()) {
            Object bean = iv.getLeafBean();
            Class<?> beanClass = iv.getRootBeanClass();
            String message = iv.getMessage();
            Path path = iv.getPropertyPath();
            Iterator<javax.validation.Path.Node> it = path.iterator();
            Path.Node node = it.next();
            while (it.hasNext()) {
                node = it.next();
            }
            String propertyName = String.valueOf(node);
            String propertyPath = String.valueOf(path);
            Object rootBean = iv.getRootBean();
            Object value = iv.getInvalidValue();
            org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue tmp = new org.tools.hqlbuilder.common.exceptions.ValidationException.InvalidValue(
                    bean, beanClass, message, propertyName, propertyPath, rootBean, value);
            ivs.add(tmp);
        }
        return new ValidationException(ex.getMessage(), ivs);
    }
}

package org.tools.hqlbuilder.common.exceptions;

import java.io.Serializable;
import java.util.List;

public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 6238718287643857217L;

    private final List<InvalidValue> invalidValues;

    public ValidationException(String message, List<InvalidValue> invalidValues) {
        super(message);
        this.invalidValues = invalidValues;
    }

    public static class InvalidValue implements Serializable {
        private static final long serialVersionUID = -6181329358582271367L;

        public InvalidValue(Object bean, Class<?> beanClass, String message, String propertyName, String propertyPath, Object rootBean,
                Object value) {
            super();
            this.bean = bean;
            this.beanClass = beanClass;
            this.message = message;
            this.propertyName = propertyName;
            this.propertyPath = propertyPath;
            this.rootBean = rootBean;
            this.value = value;
        }

        private final String message;

        private final Object value;

        private final String propertyName;

        private final Class<?> beanClass;

        private final Object bean;

        private final Object rootBean;

        private final String propertyPath;

        public String getMessage() {
            return this.message;
        }

        public Object getValue() {
            return this.value;
        }

        public String getPropertyName() {
            return this.propertyName;
        }

        public Class<?> getBeanClass() {
            return this.beanClass;
        }

        public Object getBean() {
            return this.bean;
        }

        public Object getRootBean() {
            return this.rootBean;
        }

        public String getPropertyPath() {
            return this.propertyPath;
        }

        @Override
        public String toString() {
            return "InvalidValue [beanClass=" + this.beanClass + ", propertyName=" + this.propertyName + ", propertyPath=" + this.propertyPath
                    + ", message=" + this.message + ", value=" + this.value + ", bean=" + this.bean + ", rootBean=" + this.rootBean + "]";
        }
    }

    public List<InvalidValue> getInvalidValues() {
        return this.invalidValues;
    }

    @Override
    public String toString() {
        return "ValidationException [message=" + getMessage() + "[invalidValues=" + this.invalidValues + "]";
    }
}

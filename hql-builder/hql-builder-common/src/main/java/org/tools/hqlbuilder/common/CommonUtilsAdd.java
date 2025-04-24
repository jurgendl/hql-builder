package org.tools.hqlbuilder.common;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;

public class CommonUtilsAdd extends CommonUtils {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static <T> T call(Object object, String methodName, Class<T> type, Object... params) {
        // logger.debug(String.valueOf(object));
        // logger.debug(methodName);
        // logger.debug(Arrays.toString(params));
        MethodInvokingFactoryBean mi = new MethodInvokingFactoryBean();
        mi.setTargetObject(object);
        mi.setTargetMethod(methodName);
        mi.setArguments(params);
        try {
            mi.afterPropertiesSet();
            T value = type.cast(mi.getObject());
            // logger.debug(String.valueOf(value));
            return value;
        } catch (RuntimeException ex) {
            logger.error("call(Object, String, Class<T>, Object...)");
            logger.error(ex.getClass().getName());
            logger.error(String.valueOf(ex));
            throw ex;
        } catch (Exception ex) {
            logger.error("call(Object, String, Class<T>, Object...)");
            logger.error(ex.getClass().getName());
            logger.error(String.valueOf(ex));
            throw new RuntimeException(ex);
        }
    }

    public static <T> T create(Class<T> modelType) {
        return BeanUtils.instantiateClass(modelType);
    }

    //public static <A> void set(Object bean, A arg, Object value) {
    //    org.springframework.beans.PropertyAccessorFactory.forBeanPropertyAccess(bean).setPropertyValue(Lambda.argument(arg).getInkvokedPropertyName(), value);
    //}

    public static void call(Object object, String methodName) {
        call(object, methodName, Void.TYPE);
    }
}

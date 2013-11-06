package org.tools.hqlbuilder.common;

import java.util.Arrays;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;

public class MethodInvoker {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodInvoker.class);

    public static void call(Object object, String methodName) {
        call(object, methodName, Void.TYPE);
    }

    public static <T> T call(Object object, String methodName, Class<T> type, Object... params) {
        logger.debug(String.valueOf(object));
        logger.debug(methodName);
        logger.debug(Arrays.toString(params));
        MethodInvokingFactoryBean mi = new MethodInvokingFactoryBean();
        mi.setTargetObject(object);
        mi.setTargetMethod(methodName);
        mi.setArguments(params);
        try {
            mi.afterPropertiesSet();
            T value = type.cast(mi.getObject());
            logger.debug(String.valueOf(value));
            return value;
        } catch (Exception ex) {
            logger.error("org.tools.hqlbuilder.service.HqlServiceImpl.call(Object, String, Class<T>, Object...)");
            logger.error(ex.getClass().getName());
            logger.error(String.valueOf(ex));
            throw new RuntimeException(ex);
        }
    }
}

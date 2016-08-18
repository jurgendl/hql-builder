package org.tools.hqlbuilder.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

@SuppressWarnings({ "rawtypes" })
public class MethodLogger implements AfterReturningAdvice, ThrowsAdvice, MethodBeforeAdvice {
    protected static final Logger defaultlogger = LoggerFactory.getLogger(MethodLogger.class);

    protected static final Map<Class, Logger> loggers = new HashMap<>();

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        Logger logger = defaultlogger;
        try {
            logger = getLogger(target);
            logger.trace("Before\n"//
                    + "---------- Method={}\n"//
                    + "---------- Object={}\n"//
                    + "---------- Params={}", method, target, args);
        } catch (RuntimeException exx) {
            logger.trace("Before\n---------- Method={}", method);
        }
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        Logger logger = defaultlogger;
        try {
            logger = getLogger(target);
            logger.trace("After.Return\n"//
                    + "---------- Method={}\n"//
                    + "---------- Object={}\n"//
                    + "---------- Params={}\n"//
                    + "---------- Value={}", method, target, args, returnValue);
        } catch (RuntimeException exx) {
            logger.trace("After.Return\n"//
                    + "---------- Method={}", method);
        }
    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        Logger logger = defaultlogger;
        try {
            logger = getLogger(target);
            logger.trace("After.Error\n"//
                    + "---------- Method={}\n"//
                    + "---------- Object={}\n"//
                    + "---------- Params={}\n"//
            /* + "---------- Exception={}" */, method, target, args/* , ex */);
        } catch (RuntimeException exx) {
            logger.trace("After.Error\n---------- Method={}\n"//
            /* + "---------- Exception={}" */, method/* , ex */);
        }
    }

    public Logger getLogger(Object o) {
        if (o == null) {
            return defaultlogger;
        }
        Class c;
        if (o instanceof Class) {
            c = (Class) o;
        } else {
            c = o.getClass();
        }
        Logger logger = loggers.get(c);
        if (logger == null) {
            logger = LoggerFactory.getLogger(c);
            loggers.put(c, logger);
        }
        return logger;
    }
}

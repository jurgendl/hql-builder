package org.tools.hqlbuilder.webservice.wicket;

import org.springframework.beans.PropertyAccessorFactory;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.argument.Argument;

public class WebHelper {
    public static <A> Argument<A> arg(A arg) {
        return Lambda.argument(arg);
    }

    public static <T> T proxy(Class<T> type) {
        return Lambda.on(type);
    }

    public static <A> String name(A arg) {
        return arg(arg).getInkvokedPropertyName();
    }

    public static <A> Class<A> type(A arg) {
        return Lambda.argument(arg).getReturnType();
    }

    public static <A> A get(A arg, Object value) {
        return Lambda.argument(arg).evaluate(value);
    }

    public static <A> void set(Object bean, A arg, Object value) {
        PropertyAccessorFactory.forBeanPropertyAccess(bean).setPropertyValue(Lambda.argument(arg).getInkvokedPropertyName(), value);
    }
}

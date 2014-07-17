package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.springframework.beans.BeanUtils;
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

    public static <T> IModel<T> model(Class<T> modelType) {
        return model(create(modelType));
    }

    public static <T> T create(Class<T> modelType) {
        return BeanUtils.instantiate(modelType);
    }

    public static <T> IModel<T> model(T model) {
        return new CompoundPropertyModel<T>(model);
    }
}

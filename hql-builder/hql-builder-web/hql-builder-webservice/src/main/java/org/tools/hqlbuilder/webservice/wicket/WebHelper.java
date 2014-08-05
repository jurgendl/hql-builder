package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.common.CommonUtils;

import ch.lambdaj.function.argument.Argument;

public class WebHelper {
    public static <A> Argument<A> arg(A arg) {
        return CommonUtils.arg(arg);
    }

    public static <T> T proxy(Class<T> type) {
        return CommonUtils.proxy(type);
    }

    public static <A> String name(A arg) {
        return CommonUtils.name(arg);
    }

    public static <A> Class<A> type(A arg) {
        return CommonUtils.type(arg);
    }

    public static <A> A get(A arg, Object value) {
        return CommonUtils.get(arg, value);
    }

    public static <A> void set(Object bean, A arg, Object value) {
        CommonUtils.set(bean, arg, value);
    }

    public static <T> T create(Class<T> modelType) {
        return CommonUtils.create(modelType);
    }

    public static <T> IModel<T> model(Class<T> modelType) {
        return model(create(modelType));
    }

    public static <T> IModel<T> model(T model) {
        return new CompoundPropertyModel<T>(model);
    }
}

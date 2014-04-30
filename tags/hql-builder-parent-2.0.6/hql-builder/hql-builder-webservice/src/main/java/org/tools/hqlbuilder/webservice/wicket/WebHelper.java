package org.tools.hqlbuilder.webservice.wicket;

import ch.lambdaj.Lambda;

public class WebHelper {
    public static <T> T create(Class<T> type) {
        return Lambda.on(type);
    }

    public static <A> String name(A arg) {
        return Lambda.argument(arg).getInkvokedPropertyName();
    }
}

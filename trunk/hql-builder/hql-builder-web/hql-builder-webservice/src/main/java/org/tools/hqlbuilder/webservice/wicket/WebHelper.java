package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.common.CommonUtils;

public class WebHelper extends CommonUtils {
    public static <T> IModel<T> model(Class<T> modelType) {
        return model(create(modelType));
    }

    public static <T> IModel<T> model(T model) {
        return new CompoundPropertyModel<T>(model);
    }

    public static <C extends Component> C show(C component) {
        component.setOutputMarkupPlaceholderTag(true);
        component.setRenderBodyOnly(false);
        component.setOutputMarkupId(true);
        return component;
    }
}
package org.tools.hqlbuilder.webservice.wicket;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
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

    public static <C extends Component> C hide(C component) {
        component.setOutputMarkupPlaceholderTag(false);
        component.setRenderBodyOnly(true);
        component.setOutputMarkupId(false);
        return component;
    }

    public static void untag(ComponentTag tag, String tagId) {
        tag(tag, tagId, null);
    }

    public static void tag(ComponentTag tag, String tagId, Object value) {
        if (value == null || (value instanceof String && StringUtils.isBlank(String.class.cast(value)))) {
            tag.getAttributes().remove(tagId);
        } else {
            tag.getAttributes().put(tagId, value);
        }
    }

    public static PageParameters clone(PageParameters pageParameters, String... ids) {
        PageParameters pp = new PageParameters();
        for (String id : ids) {
            if (StringUtils.isNotBlank(pageParameters.get(id).toOptionalString())) {
                pp.add(id, pageParameters.get(id).toOptionalString());
            }
        }
        return pp;
    }
}

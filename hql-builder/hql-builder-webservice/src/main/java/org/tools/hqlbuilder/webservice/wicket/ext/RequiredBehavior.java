package org.tools.hqlbuilder.webservice.wicket.ext;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.request.Response;

public class RequiredBehavior extends Behavior {
    private static final long serialVersionUID = -8228381818440709004L;

    @SuppressWarnings("rawtypes")
    @Override
    public void afterRender(Component component) {
        Response response = component.getResponse();
        StringBuffer asterisktHtml = new StringBuffer(200);
        if (component instanceof FormComponent && ((FormComponent) component).isRequired()) {
            // asterisktHtml.append("<span class=\"requiredMarker\">*</span>");
            asterisktHtml.append("<span class=\"fontawesome-asterisk requiredMackup\"/>");
        }
        response.write(asterisktHtml);
    }
}
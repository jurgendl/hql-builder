package org.tools.hqlbuilder.webservice.jquery.ui.tagit;

import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see http://aehlke.github.io/tag-it/
 */
public class TagIt {
    public static JavaScriptResourceReference TAG_IT_JS = new JavaScriptResourceReference(TagIt.class, "js/tag-it.js")
            .addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());

    public static CssResourceReference TAG_IT_CSS = new CssResourceReference(TagIt.class, "css/jquery.tagit.css");

    public static CssResourceReference TAG_IT_ZEN_CSS = new CssResourceReference(TagIt.class, "css/tagit.ui-zendesk.css");
}

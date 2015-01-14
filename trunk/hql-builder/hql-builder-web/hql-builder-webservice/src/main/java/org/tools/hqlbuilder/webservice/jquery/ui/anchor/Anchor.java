package org.tools.hqlbuilder.webservice.jquery.ui.anchor;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * $(function() { $('a[href*=#]').anchor({ transitionDuration : 1200 }); });<br/>
 * <a href="#section1">Jump to first section</a><section id="section1">first section</section>
 *
 * @author http://cbopp-art.github.io/anchor/
 */
public class Anchor {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(Anchor.class, "anchor.js");

    static {
        try {
            Anchor.JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }
}

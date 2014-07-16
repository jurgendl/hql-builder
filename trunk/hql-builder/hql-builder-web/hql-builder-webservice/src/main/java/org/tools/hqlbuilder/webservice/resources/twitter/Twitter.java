package org.tools.hqlbuilder.webservice.resources.twitter;

import org.tools.hqlbuilder.webservice.WicketRoot;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * used version 3.2.0
 *
 * @see http://getbootstrap.com/getting-started/#download
 */
public class Twitter {
    public static JavaScriptResourceReference TWITTER_JS = new JavaScriptResourceReference(WicketRoot.class,
            "twitter/bootstrap-3.2.0-dist/js/bootstrap.js");

    public static CssResourceReference TWITTER_CSS = new CssResourceReference(WicketRoot.class, "twitter/bootstrap-3.2.0-dist/css/bootstrap.css");

    public static CssResourceReference TWITTER_THEME_CSS = new CssResourceReference(WicketRoot.class,
            "twitter/bootstrap-3.2.0-dist/css/bootstrap-theme.css").addCssResourceReferenceDependency(TWITTER_CSS);
}

package org.tools.hqlbuilder.webservice.jquery.ui.blazy;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see http://dinbror.dk/blog/blazy/
 */
public class BLazy {
    public static final String IMAGE_PLACEHOLDER = "data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==";

    public static final String BLAZY_CLASS = "b-lazy";

    public static final String BLAZY_SRC = "data-src";

    public static final JavaScriptResourceReference BLAZY_JS = new JavaScriptResourceReference(BLazy.class, "blazy.js");

    public static final CssResourceReference BLAZY_CSS = new CssResourceReference(BLazy.class, "blazy.css");

    public static final String BLAZY_FACTORY_JS = "new Blazy({offset:333});";
}

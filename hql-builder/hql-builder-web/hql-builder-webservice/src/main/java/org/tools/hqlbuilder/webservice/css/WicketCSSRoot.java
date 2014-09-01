package org.tools.hqlbuilder.webservice.css;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.tools.hqlbuilder.webservice.jquery.WicketJQueryRoot;
import org.tools.hqlbuilder.webservice.resources.spectrum.Spectrum;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

import com.googlecode.htmlcompressor.compressor.YuiCssCompressor;

public class WicketCSSRoot {
    public static CssResourceReference RESET = new CssResourceReference(WicketCSSRoot.class, "meyer.reset.css");

    public static CssResourceReference NORMALIZE = new CssResourceReference(WicketCSSRoot.class, "normalize.css");

    public static CssResourceReference GENERAL = new CssResourceReference(WicketCSSRoot.class, "general.css");

    public static CssResourceReference CLEARFIX = new CssResourceReference(WicketCSSRoot.class, "clearfix.css");

    public static void main(String[] args) {
        minify(WicketCSSRoot.class, "", new String[] {
                "clearfix",
                "general",
                "meyer.reset",
                "normalize",
                "socialcolorshover",
                "socialcolors",
                "weloveiconfonts",
                "weloveiconfonts-social" });
        minify(WicketJQueryRoot.class, Spectrum.PATH, new String[] { "spectrum" });
    }

    protected static void minify(Class<?> root, String path, String[] sources) {
        try {
            String ext = "css";
            YuiCssCompressor compressor = new YuiCssCompressor();
            for (String source : sources) {
                File css = new File("src/main/resources/" + root.getPackage().getName().replace('\\', '/').replace('.', '/') + path + "/" + source
                        + "." + ext);
                byte[] buffer = new byte[(int) css.length()];
                IOUtils.readFully(new FileInputStream(css), buffer);
                IOUtils.write(compressor.compress(new String(buffer)), new FileOutputStream(new File("src/main/resources/"
                        + root.getPackage().getName().replace('\\', '/').replace('.', '/') + path + "/" + source + ".mini." + ext)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

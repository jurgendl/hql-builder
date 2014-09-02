package org.tools.hqlbuilder.webservice.js;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.tools.hqlbuilder.webservice.jquery.ui.spectrum.Spectrum;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import com.googlecode.htmlcompressor.compressor.YuiJavaScriptCompressor;
import com.googlecode.wicket.jquery.core.settings.IJQueryLibrarySettings;

public class WicketJSRoot {
    public static JavaScriptResourceReference FLOATING_BAR = new JavaScriptResourceReference(WicketJSRoot.class, "floatingbar.js");

    static {
        try {
            FLOATING_BAR.addJavaScriptResourceReferenceDependency(((IJQueryLibrarySettings) WicketApplication.get().getJavaScriptLibrarySettings())
                    .getJQueryUIReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        minify(WicketJSRoot.class, "", new String[] { "colors", "JQDatePicker", "RangeField", "floatingbar" });
        minify(Spectrum.class, "", new String[] { "spectrum" });
    }

    protected static void minify(Class<?> base, String path, String[] sources) {
        try {
            YuiJavaScriptCompressor compressor = new YuiJavaScriptCompressor();
            String ext = "js";
            for (String source : sources) {
                File css = new File("src/main/resources/" + base.getPackage().getName().replace('\\', '/').replace('.', '/') + path + "/" + source
                        + "." + ext);
                byte[] buffer = new byte[(int) css.length()];
                IOUtils.readFully(new FileInputStream(css), buffer);
                IOUtils.write(compressor.compress(new String(buffer)), new FileOutputStream(new File("src/main/resources/"
                        + base.getPackage().getName().replace('\\', '/').replace('.', '/') + path + "/" + source + ".mini." + ext)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

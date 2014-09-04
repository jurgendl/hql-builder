package org.tools.hqlbuilder.webservice.js;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.tools.hqlbuilder.webservice.jquery.ui.colors.Colors;
import org.tools.hqlbuilder.webservice.jquery.ui.datepicker.JQueryDatePicker;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.jquery.ui.spectrum.Spectrum;

import com.googlecode.htmlcompressor.compressor.YuiJavaScriptCompressor;

public class WicketJSRoot {
    public static void main(String[] args) {
        minify(JQueryDatePicker.class, "", new String[] { "JQDatePicker" });
        minify(Colors.class, "", new String[] { "colors" });
        minify(Spectrum.class, "", new String[] { "spectrum" });
        minify(PrimeUI.class, "", new String[] { "primeui-factory" });
        minify(JQueryUI.class, "", new String[] { "jquery-ui-factory" });
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

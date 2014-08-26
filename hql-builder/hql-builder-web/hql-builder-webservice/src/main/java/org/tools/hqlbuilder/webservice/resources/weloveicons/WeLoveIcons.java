package org.tools.hqlbuilder.webservice.resources.weloveicons;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

public class WeLoveIcons {
    public static CssResourceReference WE_LOVE_ICONS_CSS = new CssResourceReference(WicketCSSRoot.class, "weloveiconfonts.css");

    public static CssResourceReference WE_LOVE_ICONS_SOCIAL_CSS = new CssResourceReference(WicketCSSRoot.class, "weloveiconfonts-social.css")
            .addCssResourceReferenceDependency(WE_LOVE_ICONS_CSS);

    public static void main(String[] args) {
        try {
            InputStream in = URI.create("http://weloveiconfonts.com/").toURL().openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.close();
            String html = new String(out.toByteArray());
            // brandico
            // entypo
            // fontawesome
            // fontelico
            // iconicfill
            // iconicstroke
            // maki
            // openwebicons
            // typicons
            // zocial
            Matcher matcher = Pattern.compile("class=\"brandico-[^\"]+\"").matcher(html);
            while (matcher.find()) {
                System.out.println(matcher.group());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

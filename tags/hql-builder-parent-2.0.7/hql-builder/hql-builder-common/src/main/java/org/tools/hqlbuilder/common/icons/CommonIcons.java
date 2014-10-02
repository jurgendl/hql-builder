package org.tools.hqlbuilder.common.icons;

import java.net.URL;

import javax.swing.ImageIcon;

public class CommonIcons {
    public static ImageIcon getIcon(String path) {
        if (path == null) {
            return null;
        }
        path = CommonIcons.class.getPackage().getName().replace('.', '/') + '/' + path;
        URL resource = CommonIcons.class.getClassLoader().getResource(path);
        if (resource == null) {
            System.err.println("resource not found: " + path);
            return null;
        }
        return new ImageIcon(resource);
    }
}

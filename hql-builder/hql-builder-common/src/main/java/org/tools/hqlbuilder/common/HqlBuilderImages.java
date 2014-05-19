package org.tools.hqlbuilder.common;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class HqlBuilderImages {
    public static final String LOGO = "hql-builder-logo.png";

    public static final String ICON16 = "bricks-icon.png";

    public static ImageIcon getIcon() {
        return new ImageIcon(HqlBuilderImages.class.getClassLoader().getResource(ICON16));
    }

    public static BufferedImage getLogo() throws IOException {
        return ImageIO.read(HqlBuilderImages.class.getClassLoader().getResource(LOGO));
    }
}

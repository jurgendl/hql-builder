package org.tools.hqlbuilder.client;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;

public class SplashHelper {
    private static Splash splash;

    private static Window window;

    private static int step = 0;

    private static String[] stepInfo = {
            "Loading service ...",
            "Checking filesystem ...",
            "Setting up Groovy scripting ...",
            "Creating GUI ...",
            "Loading favorites ...",
            "Starting ...",
            "" };

    private static StringBuilder splashtimessb;

    private static long splashtimest;

    private static long splashtimesc;

    private static long[] splashtimesd;

    private static long time = 0;

    private static Preferences cfgp;

    private static boolean stopped = false;

    public static void setup() throws IOException {
        System.out.println(SplashHelper.class.getClassLoader().getResource("hql-builder-logo.png"));
        System.out.println(SplashHelper.class.getClassLoader().getResource("/hql-builder-logo.png"));
        System.out.println(ClassLoader.getSystemClassLoader().getResource("hql-builder-logo.png"));
        System.out.println(ClassLoader.getSystemClassLoader().getResource("/hql-builder-logo.png"));
        BufferedImage logo = ImageIO.read(SplashHelper.class.getClassLoader().getResourceAsStream("hql-builder-logo.png"));
        splash = new Splash(logo);
        splash.setTextLocation(new Point(500, 83));
        splash.setProgressBarLocation(new Rectangle(290, 78, 200, 6));
        splash.setColor(Color.white);
        window = splash.showSplash();
        window.setAlwaysOnTop(true);
        splashtimessb = new StringBuilder();
        splashtimesc = 0;
        splash.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stopped = true;
                window.dispose();
            }
        });
    }

    public static void step() {
        if (stopped) {
            return;
        }

        if (step > 0) {
            splashtimessb.append(System.currentTimeMillis() - time);
            if (step < splashtimesd.length) {
                splashtimessb.append(",");
            }
        }

        time = System.currentTimeMillis();
        splash.setText(stepInfo[step]);
        splash.setProgress((float) splashtimesc / splashtimest);

        if (step > 0 && step < splashtimesd.length) {
            splashtimesc += splashtimesd[step];
        }

        step++;
    }

    public static void end() {
        stopped = true;
        window.dispose();

        if (stopped) {
            return;
        }

        cfgp.put("splashtimes", splashtimessb.toString());
    }

    public static void update(String connectionInfo) {
        if (stopped) {
            return;
        }

        String key = connectionInfo.replaceAll("jdbc:oracle:thin", "").replaceAll("\\?", " ").replaceAll(":", " ").replaceAll("@", " ")
                .replaceAll("/", " ").trim();
        cfgp = Preferences.userRoot().node(HqlBuilderFrame.PERSISTENT_ID).node(key);
        splashtimest = 0;
        String splashtimes = cfgp.get("splashtimes", "1,1,1,1,1,1");
        String[] sptp = splashtimes.split(",");
        splashtimesd = new long[sptp.length];
        int i = 0;
        for (String p : sptp) {
            splashtimesd[i] = Long.parseLong(p);
            splashtimest += splashtimesd[i];
            i++;
        }

        splashtimesc += splashtimesd[0];
    }
}

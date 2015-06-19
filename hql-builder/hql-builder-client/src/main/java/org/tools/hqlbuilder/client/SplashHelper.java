package org.tools.hqlbuilder.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.JLabel;

import org.swingeasy.Splash;
import org.swingeasy.UIUtils;
import org.tools.hqlbuilder.common.HqlBuilderImages;

public class SplashHelper {
    private static Splash splash;

    private static Window window;

    private static int step = 0;

    private static String[] stepInfo = {
            "Loading service",
            "Checking filesystem",
            "Setting up Groovy scripting",
            "Creating GUI",
            "Loading favorites",
            "Starting",
            "" };

    private static StringBuilder splashtimessb;

    private static long splashtimest;

    private static long splashtimesc;

    private static long[] splashtimesd;

    private static long time = 0;

    private static Preferences cfgp;

    private static boolean stopped = false;

	public static void setup(String version) throws IOException {
        BufferedImage logo = HqlBuilderImages.getLogo();
        splash = new Splash(logo);
        splash.setFont(new JLabel().getFont().deriveFont(Font.BOLD));
		splash.setVersionFont(new JLabel().getFont().deriveFont(Font.BOLD).deriveFont(18f));
		splash.setVersionLocation(new Point(644, 44));
		splash.setVersion(version);
        splash.setTextLocation(new Point(500, 88));
        splash.setProgressBarLocation(new Rectangle(294, 82, 200, 4));
        splash.setColor(Color.white);
		splash.setVersionColor(Color.black);
        window = splash.showSplash();
        window.setAlwaysOnTop(true);
        splashtimessb = new StringBuilder();
        splashtimesc = 0;
        splash.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _end(1);
            }
        });
    }

    public static void progress() {
        if (splashtimesd == null) {
            return;
        }
        // long current = Math.min(System.currentTimeMillis() - time, splashtimesc + splashtimesd[step]);
        // logger.debug(System.currentTimeMillis() + "-" + time + "//" + splashtimesc + "+" + splashtimesd[step] + "//"
        // + (System.currentTimeMillis() - time) + "//" + (splashtimesc + splashtimesd[step]) + "==" + current);
        // splash.setProgress((float) current / splashtimest);
    }

    public static void step() {
        if (splashtimesd == null) {
            return;
        }
        if (step > 0) {
            splashtimessb.append(System.currentTimeMillis() - time);
            if (step < splashtimesd.length) {
                splashtimessb.append(",");
            }
        }

        time = System.currentTimeMillis();

        if (!stopped) {
            splash.setText(HqlResourceBundle.getMessage(stepInfo[step]) + " ...");
            splash.setProgress((float) splashtimesc / splashtimest);
        }

        if (step > 0 && step < splashtimesd.length) {
            splashtimesc += splashtimesd[step];
        }

        step++;
    }

    public static void end() {
        if (cfgp != null) {
            cfgp.put("splashtimes", splashtimessb.toString());
        }
        _end(100);
    }

    protected static void _end(int max) {
        stopped = true;
        try {
            float fmax = max;
            for (int i = 0; i < max; i++) {
                UIUtils.translucent(window, Math.max(0f, Math.min(1f, (max - i) / fmax)));
                Thread.sleep(10);
            }
        } catch (Exception ex) {
            //
        }
        window.dispose();
    }

    public static void update(String connectionInfo) {
        try {
            String key = connectionInfo.replaceAll("\\?", " ").replaceAll(":", " ").replaceAll("@", " ").replaceAll("/", " ").trim();
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
        } catch (RuntimeException ex) {
            //
        }
    }
}

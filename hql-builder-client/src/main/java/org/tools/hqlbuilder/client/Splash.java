package org.tools.hqlbuilder.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JWindow;

public class Splash extends JComponent {
    private static final long serialVersionUID = -2472231955593958968L;

    private String text;

    private float progress;

    private final BufferedImage logo;

    private Color color = Color.black;

    private Color colorInv = Color.white;

    private Point textLocation;

    private Rectangle progressBarLocation;

    private int INS = 10; // inset bar

    private int INSINT = 1; // inset inner bar

    private int H = 10; // height

    private boolean frame = true;

    public Splash(BufferedImage logo) {
        this.logo = logo;
    }

    /**
     * @see javax.swing.JComponent#getMaximumSize()
     */
    @Override
    public Dimension getMaximumSize() {
        return this.getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.logo.getWidth(), this.logo.getHeight());
    }

    /**
     * @see java.awt.Component#getSize()
     */
    @Override
    public Dimension getSize() {
        return this.getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawImage(this.logo, 0, 0, this);

        if (progressBarLocation == null) {
            progressBarLocation = new Rectangle(INS, this.getHeight() - INS / 2 - H, this.getWidth() - (2 * INS), H);
        }

        g2.setColor(color);
        g2.fillRect(progressBarLocation.x, progressBarLocation.y, progressBarLocation.width, progressBarLocation.height);
        g2.fillOval(progressBarLocation.x - progressBarLocation.height / 2, progressBarLocation.y, progressBarLocation.height,
                progressBarLocation.height);
        g2.fillOval(progressBarLocation.x + progressBarLocation.width - progressBarLocation.height / 2, progressBarLocation.y,
                progressBarLocation.height, progressBarLocation.height);
        g2.setColor(colorInv);
        int rx = progressBarLocation.x + INSINT;
        int ry = progressBarLocation.y + INSINT;
        int rw = progressBarLocation.width - INSINT * 2;
        int rh = progressBarLocation.height - INSINT * 2;
        g2.fillRect(rx, ry, (int) (rw * progress), rh);
        int or = progressBarLocation.height - INSINT * 2;
        int oy = progressBarLocation.y + INSINT;
        int ox1 = progressBarLocation.x - progressBarLocation.height / 2 + INSINT;
        g2.fillOval(ox1, oy, or, or);
        int ox2 = progressBarLocation.x + progressBarLocation.width - progressBarLocation.height / 2 + INSINT;
        int ox = ox1 + (int) ((ox2 - ox1) * progress);
        g2.fillOval(ox, oy, or, or);

        if (progressBarLocation.height >= 10) {
            g2.setColor(color);
            g2.fillOval(ox + (INSINT * 1), oy + (INSINT * 1), or - (INSINT * 2), or - (INSINT * 2));
            g2.setColor(colorInv);
            g2.fillOval(ox + (INSINT * 2), oy + (INSINT * 2), or - (INSINT * 4), or - (INSINT * 4));
        }

        if (this.text != null) {
            g2.setColor(colorInv);
            if (textLocation == null) {
                textLocation = new Point(progressBarLocation.x, progressBarLocation.y - getFont().getSize() / 2);
            }
            g2.drawString(this.text, textLocation.x, textLocation.y);
        }
    }

    public void setProgress(float f) {
        if (f < 0.0) {
            throw new IllegalArgumentException();
        }
        if (f > 1.0) {
            throw new IllegalArgumentException();
        }
        this.progress = f;
        this.repaint();
    }

    public void setText(String string) {
        this.text = string;
        this.repaint();
    }

    public Window showSplash() {
        Window f;
        if (frame) {
            f = new JFrame();
            JFrame jf = (JFrame) f;
            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            jf.setUndecorated(true);
            jf.setResizable(true);
        } else {
            f = new JWindow();
        }
        f.add(this);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        return f;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.colorInv = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }

    public Point getTextLocation() {
        return this.textLocation;
    }

    public void setTextLocation(Point textLocation) {
        this.textLocation = textLocation;
    }

    public Rectangle getProgressBarLocation() {
        return this.progressBarLocation;
    }

    public void setProgressBarLocation(Rectangle progressBarLocation) {
        this.progressBarLocation = progressBarLocation;
    }

    public static void main(String[] args) throws Exception {
        BufferedImage logo = new BufferedImage(400, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = logo.createGraphics();
        g2.setColor(Color.lightGray);
        g2.fillRect(0, 0, 400, 200);
        Splash splash = new Splash(logo);
        splash.showSplash();
        splash.setProgress(.0f);
        splash.setText("text");
        for (int i = 0; i <= 10; i++) {
            splash.setProgress(i / 10f);
            if (i == 0) {
                Thread.sleep(5000l);
            } else {
                Thread.sleep(1000l);
            }
        }
    }
}

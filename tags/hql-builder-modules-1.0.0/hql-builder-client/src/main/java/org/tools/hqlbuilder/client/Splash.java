package org.tools.hqlbuilder.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class Splash extends JComponent {
    private static final long serialVersionUID = -2472231955593958968L;

    private String text;

    private float progress;

    private final BufferedImage logo;

    public Splash(BufferedImage logo) {
        this.logo = logo;
    }

    @Override
    public Dimension getMaximumSize() {
        return this.getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.logo.getWidth(), this.logo.getHeight());
    }

    @Override
    public Dimension getSize() {
        return this.getPreferredSize();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.logo, 0, 0, this);
        if (this.text != null) {
            g2.drawString(this.text, 10, this.getHeight() - 20 - 5);
        }
        g2.fillRoundRect(10, this.getHeight() - 20, (int) (this.progress * this.getWidth()) - 10 - 10, 10, 5, 5);
    }

    public void setProgress(float f) {
        this.progress = f;
        this.repaint();
    }

    public void setText(String string) {
        this.text = string;
        this.repaint();
    }

    public Window showSplash() {
        Window f = new Window(null);
        f.add(this);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        return f;
    }
}

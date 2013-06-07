package org.tools.hqlbuilder.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class Splash extends JComponent {
    private static final long serialVersionUID = -2472231955593958968L;

    private String text;

    private float progress;

    private final BufferedImage logo;

    private Color color = Color.black;

    private Color colorInv = Color.white;

    private Point textLocation;

    private Rectangle progressBarLocation;

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
        g2.setColor(color);
        g2.drawImage(this.logo, 0, 0, this);
        if (this.text != null) {
            if (textLocation != null) {
                g2.drawString(this.text, textLocation.x, textLocation.y);
            } else {
                g2.drawString(this.text, 10, this.getHeight() - 20 - 5);
            }
        }
        if (progressBarLocation != null) {
            g2.setColor(colorInv);
            g2.fillRoundRect(progressBarLocation.x, progressBarLocation.y, (int) (1.0f * progressBarLocation.width), progressBarLocation.height, 5, 5);
            g2.setColor(color);
            g2.fillRoundRect(progressBarLocation.x + 1, progressBarLocation.y + 1, (int) (this.progress * progressBarLocation.width) - 2,
                    progressBarLocation.height - 2, 5, 5);
        } else {
            g2.setColor(colorInv);
            g2.fillRoundRect(10, this.getHeight() - 20, (int) (1.0f * this.getWidth()) - 10 - 10, 10, 5, 5);
            g2.setColor(color);
            g2.fillRoundRect(10 + 1, this.getHeight() - 20 + 1, (int) (this.progress * this.getWidth()) - 10 - 10 - 2, 10 - 2, 5, 5);
        }
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

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        colorInv = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
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
}

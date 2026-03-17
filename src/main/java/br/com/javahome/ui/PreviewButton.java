package br.com.javahome.ui;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

import static br.com.javahome.ui.RoomUI.DEFAULT;

public class PreviewButton extends JButton {

    private float alpha = 1f;
    private static final int ARC = 4;

    public PreviewButton() {

        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, DEFAULT));
        setForeground(Color.BLACK);
        setUI(createCustomUI());
    }

    public void setAlpha(float alpha) {
        this.alpha = Math.max(0f, Math.min(alpha, 1f));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintComponent(g2);
        g2.dispose();
    }

    private ButtonUI createCustomUI() {
        return new BasicButtonUI() {

            private static final Color PREVIEW_BTN_COLOR = Color.decode("#004A07");

            @Override
            public void paint(Graphics g, JComponent c) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(PREVIEW_BTN_COLOR);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), ARC, ARC);
                g2.dispose();
                super.paint(g, c);
            }
        };
    }

}

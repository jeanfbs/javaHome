package br.com.javahome.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static br.com.javahome.ui.RoomUI.DEFAULT;

public class ToggleButtonUI extends JToggleButton {

    private boolean hover = false;
    private final int ARC = 8;
    private final Color GREEN = Color.decode("#006D07");;

    public ToggleButtonUI(String text) {
        super(text);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(DEFAULT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (isSelected()) {
            g2.setColor(hover ? GREEN : DEFAULT);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
            setForeground(Color.BLACK);

        } else {

            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
            g2.setColor(hover ? GREEN : DEFAULT);
            setForeground(hover ? GREEN : DEFAULT);

            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, ARC, ARC);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
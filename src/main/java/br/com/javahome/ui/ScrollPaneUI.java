package br.com.javahome.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

import static br.com.javahome.ui.RoomUI.DEFAULT;

public class ScrollPaneUI extends JScrollPane {

    private static final Color BACKGROUND = Color.BLACK;

    public ScrollPaneUI(Component view) {
        super(view);
        configure();
    }

    private void configure() {

        setBorder(BorderFactory.createEmptyBorder());

        setBackground(BACKGROUND);
        getViewport().setBackground(BACKGROUND);

        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);

        getVerticalScrollBar().setUI(new TerminalScrollBarUI());
        getHorizontalScrollBar().setUI(new TerminalScrollBarUI());
    }

    private static class TerminalScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            trackColor = Color.BLACK;
            thumbColor = DEFAULT;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new ArrowButton(orientation);
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new ArrowButton(orientation);
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {

            g.setColor(Color.BLACK);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle bounds) {

            if (!scrollbar.isEnabled()) return;

            g.setColor(DEFAULT);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    private static class ArrowButton extends JButton {

        public ArrowButton(int direction) {

            setPreferredSize(new Dimension(16, 16));
            setMinimumSize(new Dimension(16, 16));
            setMaximumSize(new Dimension(16, 16));
            setBorder(BorderFactory.createLineBorder(DEFAULT));
            setBackground(Color.BLACK);
            setFocusable(false);

            setUI(new BasicButtonUI() {

                @Override
                public void paint(Graphics g, JComponent c) {

                    JButton b = (JButton) c;
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = b.getWidth();
                    int h = b.getHeight();

                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, w, h);
                    g2.setColor(DEFAULT);

                    Polygon arrow = new Polygon();

                    switch (direction) {

                        case SwingConstants.NORTH -> {
                            arrow.addPoint(w / 2, 4);
                            arrow.addPoint(4, h - 4);
                            arrow.addPoint(w - 4, h - 4);
                        }

                        case SwingConstants.SOUTH -> {
                            arrow.addPoint(4, 4);
                            arrow.addPoint(w - 4, 4);
                            arrow.addPoint(w / 2, h - 4);
                        }
                    }
                    g2.fillPolygon(arrow);
                    g2.dispose();
                }
            });
        }
    }
}

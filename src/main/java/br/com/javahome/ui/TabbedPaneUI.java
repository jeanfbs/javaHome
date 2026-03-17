package br.com.javahome.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

import static br.com.javahome.ui.RoomUI.DEFAULT;

public class TabbedPaneUI extends JTabbedPane {

    public TabbedPaneUI() {
        setUI(new CustomUI());
    }

    private static class CustomUI extends BasicTabbedPaneUI {

        private String title;

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            int width = tabPane.getWidth();
            int height = tabPane.getHeight();
            g.setColor(DEFAULT);
            g.drawRect(0, 24, width - 1, height - 25);
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w,
                                      int h, boolean isSelected) {
            // do nothing
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex,
                                 String title, Rectangle textRect, boolean isSelected) {
            super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
            if (isSelected) {
                this.title = title;
            }
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
                                           Rectangle iconRect, Rectangle textRect, boolean isSelected) {
            Rectangle rect = rects[tabIndex];
            if (isSelected) {
                g.setColor(DEFAULT);
                g.fillRoundRect(rect.x, 1, rect.width, rect.height + 3, 2, 2);
                g.setColor(Color.BLACK);
                g.drawString(title, textRect.x, textRect.height + 2);
            }
        }
    }
}

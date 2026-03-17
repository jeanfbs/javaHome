package br.com.javahome.views;

import br.com.javahome.ui.HousePlanUI;
import br.com.javahome.ui.RoomUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static br.com.javahome.ui.RoomUI.DEFAULT;

public class RoomView extends JPanel{

    private final HousePlanUI housePlanUI;
    private final RoomUI room;
    private boolean isHovering = false;

    public RoomView(HousePlanUI housePlanUI) {
        this.housePlanUI = housePlanUI;
        room = housePlanUI.rooms().stream().findFirst().orElseThrow(() -> new RuntimeException("Room cannot is null."));
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            room.setHovered(room.contains(e.getPoint()));
            isHovering = room.isHovered();
            setCursor(isHovering ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR): Cursor.getDefaultCursor());
            repaint();
            }
        });

        room.enable();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintPlan(g2);
        room.draw(g2);
    }

    private void paintPlan(final Graphics2D g2) {

        var plan = housePlanUI.plan();
        g2.setColor(Color.BLACK);
        g2.fillRect(plan.x, plan.y, plan.width, plan.height);

        BasicStroke stroke = new BasicStroke(3);
        g2.setStroke(stroke);
        g2.setColor(DEFAULT);
        g2.drawRect(plan.x, plan.y, plan.width, plan.height);
    }
}

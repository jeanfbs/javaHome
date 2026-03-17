package br.com.javahome.views;

import br.com.javahome.ApplicationContext;
import br.com.javahome.di.Qualifier;
import br.com.javahome.enums.ViewType;
import br.com.javahome.event.EventBus;
import br.com.javahome.ui.HousePlanUI;
import br.com.javahome.ui.RoomUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Comparator;

import static br.com.javahome.event.EventType.ROOM_SELECTED;
import static br.com.javahome.ui.RoomUI.DEFAULT;


public class HouseView extends JPanel{

    private final HousePlanUI housePlanUI;
    private boolean isHovering = false;
    private final EventBus eventBus;

    public HouseView(@Qualifier("rightPanel") final JPanel rightPanel, final HousePlanUI housePlanUI, final EventBus eventBus) {
        this.housePlanUI = housePlanUI;
        this.eventBus = eventBus;

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                housePlanUI.resetHover();
                for (RoomUI r : housePlanUI.rooms()) {
                    r.setHovered(r.contains(e.getPoint()));
                    isHovering = r.isHovered();
                    if(r.isHovered()){
                        break;
                    }
                }
                setCursor(isHovering ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR): Cursor.getDefaultCursor());
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                for (RoomUI r : housePlanUI.rooms()) {
                    if(r.contains(e.getPoint())){
                        r.enable();
                        changeLayout(getParent(), r.getType());
                        eventBus.publish(ROOM_SELECTED, () -> r);
                        ApplicationContext.roomTypeCurrent = r.getType();

                        housePlanUI.rooms().sort(Comparator.comparing(RoomUI::isHovered));
                        changeLayout(rightPanel, ViewType.ROOM_DETAIL);
                        ApplicationContext.currentViewType = ViewType.ROOM_DETAIL;
                        break;
                    }
                }
            }
        });
    }

    private void changeLayout(final Container container, Enum<?> comp) {
        CardLayout layout = (CardLayout) container.getLayout();
        layout.show(container, comp.name());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintPlan(g2);

        for (var room : housePlanUI.rooms()){
            room.draw(g2);
        }
        var footer = ApplicationContext.getProperty("app.footer");
        g2.drawString(footer.toUpperCase(), 5, getHeight() - 7);
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

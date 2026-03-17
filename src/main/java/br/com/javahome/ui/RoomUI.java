package br.com.javahome.ui;

import br.com.javahome.enums.RoomType;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class RoomUI {

    public static final Color DEFAULT = new Color(0, 101, 7);
    public static final Color SELECTED = new Color(255, 196, 0);

    private final RoomType type;
    private String name;
    private Point namePoint;
    private final List<Shape> walls = new ArrayList<>();
    private Shape hitArea;
    private boolean hovered;
    private boolean enable = false;
    private AffineTransform transform = new AffineTransform();

    public RoomUI(RoomType type, String name){
        this.type = type;
        this.name = name;
    }

    public void draw(Graphics2D g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.transform(transform);
        g2.setColor(enable ? SELECTED : DEFAULT);
        g2.setStroke(new BasicStroke(3));

        for (Shape wall : walls) {
            g2.draw(wall);
        }
        g2.setFont(new Font("consoles", Font.PLAIN, 9));
        g2.drawString(name.toUpperCase(), namePoint.x, namePoint.y);
        if (hovered && hitArea != null) {
            g2.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f)
            );
            g2.fill(hitArea);
        }
        g2.dispose();
    }

    public boolean contains(Point2D screenPoint) {
        try {

            Point2D local =
                    transform.createInverse().transform(screenPoint, null);
            return hitArea != null && hitArea.contains(local);
        } catch (NoninvertibleTransformException e) {
            return false;
        }
    }

    public RoomType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addWall(Shape wall) {
        walls.add(wall);
    }

    public void setHitArea(Shape hitArea) {
        this.hitArea = hitArea;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isHovered() {
        return hovered;
    }

    public Point getNamePoint() {
        return namePoint;
    }

    public void setNamePoint(Point namePoint) {
        this.namePoint = namePoint;
    }

    public void enable() {
        enable = true;
    }
    public void disable() {
        enable = false;
    }

    public boolean isEnable() {
        return enable;
    }
}

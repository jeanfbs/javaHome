package br.com.javahome.util;

import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public final class SvgUtils {

    private SvgUtils() {
        // Nothing
    }

    public static Path2D parsePath(String d) {

        Path2D path = new Path2D.Double();
        String[] tokens = d.trim().split("\\s+");

        for (int i = 0; i < tokens.length; i++) {
            switch (tokens[i]) {
                case "M" -> setMoveToPath(tokens, i, path::moveTo);
                case "L" -> setMoveToPath(tokens, i, path::lineTo);
                case "Z" -> path.closePath();
            }
        }
        return path;
    }

    private static void setMoveToPath(String[] tokens, int i, BiConsumer<Double, Double> consumer) {
        double x = Math.round(Double.parseDouble(tokens[++i]));
        double y = Math.round(Double.parseDouble(tokens[++i]));
        consumer.accept(x, y);
    }

    public static AffineTransform parseTransform(String value) {

        var empty = new AffineTransform();
        if (value == null || value.isEmpty()) return empty;

        value = value.replace("matrix(", "").replace(")", "");

        String[] values = value.split(",");

        if(values.length == 0) return empty;

        return new AffineTransform(
                Math.round(Double.parseDouble(values[0])),
                Math.round(Double.parseDouble(values[1])),
                Math.round(Double.parseDouble(values[2])),
                Math.round(Double.parseDouble(values[3])),
                Math.round(Double.parseDouble(values[4])),
                Math.round(Double.parseDouble(values[5]))
        );
    }

    public static Rectangle parseRect(final Element e){
        Objects.requireNonNull(e, "Element not must be null");
        String[] attributes = new String[]{"x", "y", "width", "height"};
        boolean isValid = Stream.of(attributes).allMatch(e::hasAttribute);
        if(!isValid) throw new RuntimeException("Attributes x, y, width and height is required, but not found!");

        int x = Integer.parseInt(e.getAttribute(attributes[0]));
        int y = Integer.parseInt(e.getAttribute(attributes[1]));
        int width = Integer.parseInt(e.getAttribute(attributes[2]));
        int height = Integer.parseInt(e.getAttribute(attributes[3]));
        return new Rectangle(x, y, width, height);
    }
}

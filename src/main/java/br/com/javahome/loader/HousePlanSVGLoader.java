package br.com.javahome.loader;

import br.com.javahome.enums.RoomType;
import br.com.javahome.ui.HousePlanUI;
import br.com.javahome.ui.RoomUI;
import br.com.javahome.util.SvgUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class HousePlanSVGLoader implements LoaderSVG {

    private HousePlanSVGLoader() {
    }

    public static HousePlanSVGLoader getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public HousePlanUI load(final String svgPath) throws Exception {
        var inputStreamSvg = Objects.requireNonNull(this.getClass().getResourceAsStream(svgPath), "Resource %s not found".formatted(svgPath));

        List<RoomUI> rooms = new ArrayList<>();
        Document doc = getDocument(inputStreamSvg);
        Rectangle rect = createRectangle(doc);
        NodeList groups = doc.getElementsByTagName("g");

        for (int i = 0; i < groups.getLength(); i++) {

            Element g = (Element) groups.item(i);
            String type = g.getAttribute("id");
            if (type.isEmpty()) continue;

            RoomUI room = createRoom(g, RoomType.fromKey(type));
            extractAndAddWalls(room, g);
            rooms.add(room);
        }
        return new HousePlanUI(rect, rooms);
    }
    private Document getDocument(final InputStream inputStreamSvg)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder =
                factory.newDocumentBuilder();
        return builder.parse(inputStreamSvg);
    }

    private Rectangle createRectangle(final Document doc) {
        NodeList nodes = doc.getElementsByTagName("rect");
        if(nodes.getLength() == 0){
            throw new RuntimeException("Rectangle of house plan is required!");
        }
        Element e = (Element) nodes.item(0);
        return SvgUtils.parseRect(e);
    }

    private RoomUI createRoom(final Element group, final RoomType type) {
        var texts = group.getElementsByTagName("text");
        if(texts.getLength() == 0 || texts.getLength() > 1){
            throw new RuntimeException("The Text Element must be unique and not null.");
        }
        Element text = (Element) texts.item(0);
        RoomUI room = new RoomUI(type, text.getTextContent());
        room.setNamePoint(new Point(Integer.parseInt(text.getAttribute("x")), Integer.parseInt(text.getAttribute("y"))));
        return room;
    }

    private void extractAndAddWalls(final RoomUI room, final Element g) {

        room.setTransform(SvgUtils.parseTransform(g.getAttribute("transform")));
        NodeList paths = g.getElementsByTagName("path");

        for (int i = 0; i < paths.getLength(); i++) {
            Element p = (Element) paths.item(i);

            String d = p.getAttribute("d");
            if (d.isEmpty()) continue;

            String pathId = p.getAttribute("id");
            Path2D path = SvgUtils.parsePath(d);
            if ("hitbox".equals(pathId)) {
                room.setHitArea(path);
            } else {
                room.addWall(path);
            }
        }
    }

    private static class Holder {
        private static final HousePlanSVGLoader INSTANCE = new HousePlanSVGLoader();
    }
}

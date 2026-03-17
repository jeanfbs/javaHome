package br.com.javahome.ui;

import java.awt.*;
import java.util.List;

public record HousePlanUI(
        Rectangle plan,
        List<RoomUI> rooms
){
    public void resetHover(){
        rooms.forEach(r -> r.setHovered(false));
    }
}

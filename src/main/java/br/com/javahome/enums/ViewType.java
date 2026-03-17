package br.com.javahome.enums;

public enum ViewType {

    HOME,
    HOME_DETAIL,
    ROOM_DETAIL,
    LIGHT,
    ELECTRICAL,
    TEMPERATURE,
    SECURITY,
    ;



    public ViewType preview(){
        return switch (this){
            case ROOM_DETAIL -> HOME_DETAIL;
            case HOME_DETAIL -> null;
            default -> ROOM_DETAIL;
        };
    }

}

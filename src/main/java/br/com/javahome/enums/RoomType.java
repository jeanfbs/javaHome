package br.com.javahome.enums;

public enum RoomType {

    KITCHEN("kitchen"),
    HALL("hall"),
    WORK_ROOM("workRoom"),
    VISIT_ROOM("visitRoom"),
    BATHROOM("bathroom"),
    SUITE_BATHROOM("suiteBathroom"),
    ROOM("room"),
    GOURMET("gourmet")
    ;

    private final String key;

    RoomType(String key) {
        this.key = key;
    }

    public static RoomType fromKey(String key){
        RoomType type = null;
        for (RoomType t : values()){
            if(key.equals(t.getKey())){
                type = t;
            }
        }
        return type;
    }

    public String getKey() {
        return key;
    }
}

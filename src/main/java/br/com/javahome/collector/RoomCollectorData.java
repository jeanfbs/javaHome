package br.com.javahome.collector;

import br.com.javahome.ApplicationContext;
import br.com.javahome.client.RoomClient;
import br.com.javahome.event.EventBus;
import br.com.javahome.logging.Logger;
import br.com.javahome.logging.LoggerFactory;

import static br.com.javahome.event.EventType.ROOM_DATA_FETCH;

public class RoomCollectorData implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RoomCollectorData.class);
    private final RoomClient roomClient;
    private final EventBus eventBus;

    public RoomCollectorData(RoomClient roomClient, EventBus eventBus) {
        log.info("Initializing room collector data.");
        this.roomClient = roomClient;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        var roomType = ApplicationContext.roomTypeCurrent;
        if(roomType != null){
            roomClient.fetchRoomData(roomType.getKey())
                    .thenAccept(roomData -> eventBus.publish(ROOM_DATA_FETCH, () -> roomData));
        }
    }
}

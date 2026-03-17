package br.com.javahome.client;

import br.com.javahome.client.core.JsonWebSocketClient;
import br.com.javahome.di.Qualifier;
import br.com.javahome.model.RoomData;

import java.util.concurrent.CompletableFuture;

public class RoomClient {

    private final JsonWebSocketClient client;
    private final String path;

    public RoomClient(JsonWebSocketClient jsonWebSocketClient, @Qualifier("app.resource.room.path") String path) {
        this.path = path;
        client = jsonWebSocketClient;
    }

    public CompletableFuture<RoomData> fetchRoomData(String id){
        client.connect();
        return client.fetchAndReceive(path.formatted(id), null, RoomData.class);
    }
}

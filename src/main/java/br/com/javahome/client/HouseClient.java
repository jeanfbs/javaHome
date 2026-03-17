package br.com.javahome.client;

import br.com.javahome.client.core.JsonWebSocketClient;
import br.com.javahome.di.Qualifier;
import br.com.javahome.model.HomeData;

import java.util.concurrent.CompletableFuture;

public class HouseClient{

    private final JsonWebSocketClient client;
    private final String path;

    public HouseClient(JsonWebSocketClient jsonWebSocketClient, @Qualifier("app.resource.home.path") String path) {
        this.path = path;
        client = jsonWebSocketClient;
    }

    public CompletableFuture<HomeData> fetchHomeData(){
        client.connect();
        return client.fetchAndReceive(path, null, HomeData.class);
    }
}

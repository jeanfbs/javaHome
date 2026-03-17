package br.com.javahome.client.core;

import tools.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

public class JsonWebSocketClient {

    private final Client client;
    private final ObjectMapper objectMapper;

    public JsonWebSocketClient(Client client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<WebSocket> connect() {
        return client.connect();
    }

    public <T> CompletableFuture<T> fetchAndReceive(String path, Object request, Class<T> clazz) {
        String json = request instanceof String ? String.valueOf(request) : objectMapper.writeValueAsString(request);
        return client.fetch(path, json).thenApply(response -> (T) objectMapper.readValue(response, clazz));
    }

    public <T> CompletableFuture<T> updateAndReceive(String path, Object request, Class<T> clazz) {
        String json = request instanceof String ? String.valueOf(request) : objectMapper.writeValueAsString(request);
        return client.update(path, json).thenApply(response -> (T) objectMapper.readValue(response, clazz));
    }

    public <T> CompletableFuture<T> dropAndReceive(String path, Object request, Class<T> clazz) {
        String json = request instanceof String ? String.valueOf(request) : objectMapper.writeValueAsString(request);
        return client.drop(path, json).thenApply(response -> (T) objectMapper.readValue(response, clazz));
    }

}

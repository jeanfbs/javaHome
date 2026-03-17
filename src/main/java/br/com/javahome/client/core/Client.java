package br.com.javahome.client.core;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

public interface Client {

    CompletableFuture<WebSocket> connect();
    CompletableFuture<String> fetch(String path, Object msg);
    CompletableFuture<String> update(String path, Object msg);
    CompletableFuture<String> drop(String path, Object msg);
}

package br.com.javahome.client.core;


import br.com.javahome.client.HomeSocketProtocol;
import br.com.javahome.di.Qualifier;
import br.com.javahome.di.Shutdown;
import br.com.javahome.enums.Type;
import br.com.javahome.logging.Logger;
import br.com.javahome.logging.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultWebSocketClientAsync implements Client {

    private static final Logger log = LoggerFactory.getLogger(DefaultWebSocketClientAsync.class);

    private final String uri;
    private final int timeout;
    private final int maxRetry;

    private final HttpClient httpClient;
    private final AtomicReference<CompletableFuture<WebSocket>> connectionRef = new AtomicReference<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<String>> pending = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public DefaultWebSocketClientAsync(@Qualifier("websocket.uri") String uri, @Qualifier("websocket.timeout") String timeout,
                                       @Qualifier("websocket.max-retry") String maxRetry, Executor executor) {
        this.uri = uri;
        this.timeout = Integer.parseInt(timeout);
        this.maxRetry = Integer.parseInt(maxRetry);
        httpClient = HttpClient.newBuilder().executor(executor).build();
    }

    @Shutdown
    public void shutdown(){
        CompletableFuture<WebSocket> webSocketCompletableFuture = connectionRef.get();
        webSocketCompletableFuture.thenAccept(webSocket -> webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "bye"));
        webSocketCompletableFuture.cancel(true);
        pending.values().forEach(future -> future.cancel(true));
        pending.clear();
        scheduler.shutdown();
    }

    @Override
    public CompletableFuture<WebSocket> connect() {
        // applying idem potencia
        CompletableFuture<WebSocket> existing = connectionRef.get();

        if (existing != null && !existing.isCompletedExceptionally()) {
            return existing;
        }

        var newConnection =
                httpClient.newWebSocketBuilder()
                        .connectTimeout(Duration.ofSeconds(timeout))
                        .buildAsync(URI.create(uri), new Listener())
                        .toCompletableFuture();

        if (!connectionRef.compareAndSet(existing, newConnection)) {
            return connectionRef.get();
        }

        return newConnection.whenComplete((ws, error) -> {
            if (error != null) {
                scheduleReconnect(1);
            }
        });
    }

    @Override
    public CompletableFuture<String> fetch(String path, Object msg) {
        return send(Type.FETCH, path, msg);
    }

    @Override
    public CompletableFuture<String> update(String path, Object msg) {
        return send(Type.UPDATE, path, msg);
    }

    @Override
    public CompletableFuture<String> drop(String path, Object msg) {
        return send(Type.DROP, path, msg);
    }

    private CompletableFuture<String> send(Type method, String path, Object msg) {

        UUID correlationId = UUID.randomUUID();
        HomeSocketProtocol request = HomeSocketProtocol.builder()
                .method(method)
                .path(path)
                .headers(Map.of("Origin", "java-channel-backend", "Correlation-Id", correlationId.toString()))
                .build();
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        pending.put(correlationId, responseFuture);

        return send(request.writeAsString())
                .thenCompose(v -> responseFuture.orTimeout(timeout, TimeUnit.SECONDS))
                .whenComplete((r, e) -> pending.remove(correlationId));
    }

    private CompletableFuture<WebSocket> send(String msg){
        CompletableFuture<WebSocket> connection = connectionRef.get();
        if (connection == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("Not connected"));
        }
        return connection.thenCompose(ws -> ws.sendText(msg, true));
    }

    private void scheduleReconnect(int attempt) {

        long delay = Math.min(1 << attempt, 60);
        if(attempt > maxRetry){
            return;
        }
        scheduler.schedule(() -> {
            connect().exceptionally(ex -> {
                scheduleReconnect(attempt + 1);
                return null;
            });
        }, delay, TimeUnit.SECONDS);
    }

    private void failAllPending(Throwable error) {
        pending.forEach((id, future) -> future.completeExceptionally(error));
        pending.clear();
    }

    private class Listener implements WebSocket.Listener {
        private AtomicReference<StringBuilder> sbRef = new AtomicReference<>(new StringBuilder());

        @Override
        public void onOpen(WebSocket webSocket) {
            log.debug("Connected");
            webSocket.request(1);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            log.error("Error in connection: ", error);
            connectionRef.set(null);
            failAllPending(error);
            scheduleReconnect(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last) {
            ws.request(1);
            StringBuilder sb = sbRef.get();
            sb.append(data.toString());
            if(!last){
                return CompletableFuture.completedFuture(null);
            }

            String content = sb.toString();
            sbRef.set(new StringBuilder());
            HomeSocketProtocol response = HomeSocketProtocol.fromText(content);
            String correlationId = response.headers().getOrDefault("Correlation-Id", "");
            CompletableFuture<String> future = pending.get(UUID.fromString(correlationId));

            if (future != null) {
                future.complete(response.body());
            }
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            log.debug("Disconnected");
            connectionRef.set(null);
            failAllPending(new RuntimeException("Connection closed"));
            scheduleReconnect(1);
            return CompletableFuture.completedFuture(null);
        }
    }
}

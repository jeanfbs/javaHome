package br.com.javahome.event;

import br.com.javahome.logging.Logger;
import br.com.javahome.logging.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class DefaultEventBus implements EventBus {

    private static final Logger log = LoggerFactory.getLogger(DefaultEventBus.class);

    private final Map<String, List<Consumer<?>>> subscribers = new ConcurrentHashMap<>();
    private final Executor executor;

    public DefaultEventBus(Executor executor) {
        this.executor = executor;
    }

    @Override
    public <T> void subscribe(String topic, final Consumer<T> handler) {
        if(topic == null || topic.isEmpty()) throw new NullPointerException("Topic cannot be null.");
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>())
                .add(handler == null ? obj -> {} : handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void publish(String topic, final Event<T> event) {
        Objects.requireNonNull(event, "Event object cannot be null.");
        List<Consumer<?>> handlers = subscribers.getOrDefault(topic, Collections.emptyList());
        for (Consumer<?> handler : handlers) {
            Consumer<T> typed = (Consumer<T>) handler;

            executor.execute(() -> {
                try {
                    typed.accept((T)event.value());
                } catch (Exception e) {
                    log.error("", e);
                }
            });
        }
    }

}

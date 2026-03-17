package br.com.javahome.event;

import java.util.function.Consumer;

public interface EventBus {

    <T> void publish(String topic, Event<T> event);
    <T> void subscribe(String topic, Consumer<T> handler);
}

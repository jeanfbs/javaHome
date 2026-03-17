package br.com.javahome.event;

@FunctionalInterface
public interface Event<T> {

    T value();
}

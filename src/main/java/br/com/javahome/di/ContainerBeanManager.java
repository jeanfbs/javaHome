package br.com.javahome.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

public class ContainerBeanManager {

    private final Map<String, Object> beans = new HashMap<>();

    public final void register(final Class<?> clazz){
        Objects.requireNonNull(clazz, "Bean class cannot be null.");
        Object instance = createInstance(clazz);
        beans.putIfAbsent(clazz.getSimpleName(), instance);
    }

    public final void register(final Class<?> clazz, final Object instance){
        Objects.requireNonNull(instance, "Bean class cannot be null.");
        beans.putIfAbsent(clazz.getSimpleName(), instance);
    }

    public final void register(String name, final Object instance){
        if(name == null || name.isEmpty()) throw new NullPointerException("Name bean cannot be null.");
        beans.putIfAbsent(name, instance);
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = findInjectConstructor(clazz);
            Object[] dependencies = resolveDependencies(constructor);
            return constructor.newInstance(dependencies);
        } catch (Exception e) {
            throw new RuntimeException("Error while create bean: " + clazz, e);
        }
    }

    private Constructor<?> findInjectConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors()).findFirst()
                .orElseThrow(() -> new RuntimeException("No valid constructor in class " + clazz.getName()));
    }

    private Object[] resolveDependencies(final Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "Bean constructor cannot be null.");
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Parameter[] parameters = constructor.getParameters();

        Object[] deps = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> depType = paramTypes[i];
            Parameter param = parameters[i];
            Object dep = Optional.of(param).filter(p -> p.isAnnotationPresent(Qualifier.class))
                    .map(p -> p.getAnnotation(Qualifier.class))
                    .map(qualifier -> beans.getOrDefault(qualifier.value(), null))
                    .orElse(beans.getOrDefault(depType.getSimpleName(), null));
            if(dep == null){
                dep = beans.values().stream().filter(depType::isInstance).findFirst().orElse(null);
            }
            deps[i] = dep;
        }
        return deps;
    }


    public <T> T getBean(Class<T> type) {
        return type.cast(beans.get(type.getSimpleName()));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        return (T) beans.get(name);
    }


    public Map<String, Object> getBeans() {
        return beans;
    }
}

package com.radkovich.eventbus;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    private final ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Subscriber>> subscribers = new ConcurrentHashMap<>();

    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {

                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("Method " + method.getName() + " must have only one parameter!");
                }

                Class<?> eventType = parameterTypes[0];
                method.setAccessible(true);

                Subscriber subscriber = new Subscriber(listener, method);

                subscribers
                        .computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                        .add(subscriber);
            }
        }
    }

    public void unregister(Object listener) {
        for (List<Subscriber> subscriberList : subscribers.values()) {
            subscriberList.removeIf(sub -> sub.target.equals(listener));
        }
    }

    public void post(Object event) {
        Class<?> eventClass = event.getClass();

        for (Map.Entry<Class<?>, CopyOnWriteArrayList<Subscriber>> entry : subscribers.entrySet()) {
            Class<?> subscriberClass = entry.getKey();

            if (subscriberClass.isAssignableFrom(eventClass)) {
                for (Subscriber subscriber : entry.getValue()) {
                    invoke(subscriber, event);
                }
            }
        }
    }

    private void invoke(Subscriber subscriber, Object event) {
        try {
            subscriber.method.invoke(subscriber.target, event);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking " + subscriber.target, e);
        }
    }

    private static class Subscriber {
        final Object target;
        final Method method;

        Subscriber(Object target, Method method) {
            this.target = target;
            this.method = method;
        }
    }
}

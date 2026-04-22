package com.radkovich.eventbus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventBusTest {

    @Test
    void subscriptionTest() {
        EventBus eventBus = new EventBus();
        ListenerA listenerA = new ListenerA();

        eventBus.register(listenerA);
        eventBus.post(new BaseEvent("Hello from base event!"));

        assertEquals(1, listenerA.counterA);
    }

    @Test
    void hierarchyTest() {
        EventBus eventBus = new EventBus();
        ListenerA listenerA = new ListenerA();

        eventBus.register(listenerA);
        eventBus.post(new ChildEvent("test for child"));

        assertEquals(1, listenerA.counterA);
    }

    @Test
    void unregisterTest() {
        EventBus eventBus = new EventBus();
        ListenerA listenerA = new ListenerA();

        eventBus.register(listenerA);
        eventBus.unregister(listenerA);
        eventBus.post(new ChildEvent("empty"));

        assertEquals(0, listenerA.counterA);
    }

    @Test
    void severalSubscribersTest() {
        EventBus eventBus = new EventBus();
        ListenerA listenerA1 = new ListenerA();
        ListenerA listenerA2 = new ListenerA();

        eventBus.register(listenerA1);
        eventBus.register(listenerA2);

        eventBus.post(new ChildEvent("empty"));

        assertEquals(1, listenerA1.counterA);
        assertEquals(1, listenerA2.counterA);
    }

    @Test
    void threadTest() throws InterruptedException {
        EventBus eventBus = new EventBus();

        Runnable task = () -> {
            ListenerA listenerA = new ListenerA();
            eventBus.register(listenerA);
            eventBus.post(new BaseEvent("something"));
            eventBus.unregister(listenerA);
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        Thread thread3 = new Thread(task);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        assertTrue(true);
    }

    @Test
    void shouldReceiveOnlyChildEventTest(){
        EventBus eventBus = new EventBus();
        ListenerB listenerB = new ListenerB();

        eventBus.register(listenerB);
        eventBus.post(new BaseEvent("Base event test!"));
        eventBus.post(new ChildEvent("Hello from child!"));

        assertEquals(1, listenerB.counterB);
    }
}


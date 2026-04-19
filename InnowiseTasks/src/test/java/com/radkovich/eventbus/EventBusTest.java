package com.radkovich.eventbus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventBusTest {

    @Test
    void subscriptionTest() {
        EventBus eventBus = new EventBus();
        ListenerA listenerA = new ListenerA();

        eventBus.register(listenerA);
        eventBus.post(new BaseEvent("Hello from base event!"));

        assertEquals(1, listenerA.counter);
    }
}

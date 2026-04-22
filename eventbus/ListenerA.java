package com.radkovich.eventbus;

public class ListenerA {
    int counterA = 0;

    @Subscribe
    public void handle(BaseEvent event) {
        counterA++;
        System.out.println("ListenerA received: " + event.message);
    }
}

class ListenerB {
    int counterB = 0;

    @Subscribe
    public void handle(ChildEvent event) {
        counterB++;
        System.out.println("ListenerB received child: " + event.message);
    }
}
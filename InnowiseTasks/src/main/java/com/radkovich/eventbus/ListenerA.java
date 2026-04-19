package com.radkovich.eventbus;

public class ListenerA {
    public int counter = 0;

    @Subscribe
    public void handle(BaseEvent event) {
        counter++;
        System.out.println("ListenerA received: " + event.message);
    }
}

class ListenerB {

    @Subscribe
    public void handle(ChildEvent event) {
        System.out.println("ListenerB received child: " + event.message);
    }
}
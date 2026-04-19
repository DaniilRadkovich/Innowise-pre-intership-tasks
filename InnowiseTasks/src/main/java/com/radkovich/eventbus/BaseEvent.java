package com.radkovich.eventbus;

public class BaseEvent {
    public final String message;

    public BaseEvent(String message) {
        this.message = message;
    }
}

class ChildEvent extends BaseEvent {
    public ChildEvent(String message) {
        super(message);
    }
}

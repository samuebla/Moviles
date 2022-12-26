package com.example.engineandroid;

import com.example.lib.IEventHandler;

public class EventHandlerAndroid implements IEventHandler {
    @Override
    public IEvent getEvent() {
        return event;
    }

    @Override
    public void sendEvent(EventType type) {
        event.eventType = type;
    }
}

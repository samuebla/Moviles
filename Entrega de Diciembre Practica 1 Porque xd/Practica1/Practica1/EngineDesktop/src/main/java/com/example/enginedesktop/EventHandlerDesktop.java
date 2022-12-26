package com.example.enginedesktop;

import com.example.lib.IEventHandler;

public class EventHandlerDesktop implements IEventHandler {
    @Override
    public IEvent getEvent() {
        return event;
    }

    @Override
    public void sendEvent(EventType type) {
        event.eventType = type;
    }
}

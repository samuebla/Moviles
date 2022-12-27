package com.example.lib;

public interface IEventHandler {

    enum EventType {NONE,MOUSE, TOUCH, KEYBOARD}
    class IEvent{
        public EventType eventType;
    }
    IEvent event = new IEvent();
    IEvent getEvent();
    void sendEvent(EventType type);
}


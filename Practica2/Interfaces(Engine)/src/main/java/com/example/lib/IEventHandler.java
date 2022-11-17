package com.example.lib;

public interface IEventHandler {

    public enum EventType {NONE,MOUSE, TOUCH, KEYBOARD};
    class IEvent{
        public EventType eventType;
    }
    IEvent event = new IEvent();
    public IEvent getEvent();
    public void sendEvent(EventType type);
}


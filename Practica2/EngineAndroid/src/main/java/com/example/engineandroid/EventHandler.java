package com.example.engineandroid;

public class EventHandler {

    IEvent event;

    public EventHandler(){
         this.event= new IEvent();
    }

    public enum EventType {NONE, TOUCH,RELEASE};

    class IEvent{
        public EventType eventType;
    }
    public IEvent getEvent(){
        return event;
    };

    public EventType getEventType(){ return event.eventType;}
    public void sendEvent(EventType type){
        event.eventType = type;
    };
}


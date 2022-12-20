package com.example.engineandroid;

//Event handler que contiene informacion del ultimo evento de input recibido
public class EventHandler {

    IEvent event;

    public EventHandler(){
         this.event= new IEvent();
    }

    public enum EventType {NONE, TOUCH,LONG_TOUCH}

    static class IEvent{
        public EventType eventType;
    }

    public EventType getEventType(){ return event.eventType;}
    public void sendEvent(EventType type){
        event.eventType = type;
    }
}


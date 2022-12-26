package com.example.lib;


import java.awt.geom.Point2D;

public interface Scene {
    //Main Functions
    public void update(double deltaTime);
    public void render(IGraphics render);
    //Allows the loading of resources
    public void loadResources(Engine engine);
    public void handleInput(IEventHandler.EventType type, IAudio audio, Input input, ISceneMngr sceneMngr);
    public void init();
    }

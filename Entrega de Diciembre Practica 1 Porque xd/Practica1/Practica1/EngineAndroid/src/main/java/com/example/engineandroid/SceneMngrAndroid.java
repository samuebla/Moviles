package com.example.engineandroid;

import com.example.lib.IEventHandler;
import com.example.lib.IGraphics;
import com.example.lib.ISceneMngr;
import com.example.lib.ISound;
import com.example.lib.Input;
import com.example.lib.Scene;

import java.util.Stack;

public class SceneMngrAndroid implements ISceneMngr {

    Stack<Scene> stack;

    public SceneMngrAndroid(){
        stack = new Stack<Scene>();
    }

    @Override
    public void popScene(){
        stack.pop();
    }

    @Override
    public void pushScene(Scene scene){
        stack.push(scene);
    }

    @Override
    public void update(double deltaTime){
        stack.peek().update(deltaTime);
    }

    @Override
    public void render(IGraphics render){
        stack.peek().render(render);
    }

    @Override
    public void handleInput(IEventHandler.EventType type, ISound sound, Input input, ISceneMngr sceneMngr){
        stack.peek().handleInput(type, sound, input, sceneMngr);
    }

    @Override
    public Scene getScene(){return stack.peek();}
}

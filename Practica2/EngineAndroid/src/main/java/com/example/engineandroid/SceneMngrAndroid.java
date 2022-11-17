package com.example.engineandroid;

import com.example.lib.ISceneMngr;
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
    public void render(){
        stack.peek().render();
    }

    @Override
    public void handleInput(){
        stack.peek().handleInput();
    }

    @Override
    public Scene getScene(){return stack.peek();}
}

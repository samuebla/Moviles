package com.example.engineandroid;


import java.util.Stack;

public class SceneMngrAndroid {

    Stack<Scene> stack;

    public SceneMngrAndroid(){
        stack = new Stack<Scene>();
    }

    public void popScene(){
        stack.pop();
    }

    public void pushScene(Scene scene){
        stack.push(scene);
    }

    public void update(double deltaTime){
        stack.peek().update(deltaTime);
    }

    public void render(){
        stack.peek().render();
    }

    public void handleInput(){
        stack.peek().handleInput();
    }

    public Scene getScene(){return stack.peek();}
}

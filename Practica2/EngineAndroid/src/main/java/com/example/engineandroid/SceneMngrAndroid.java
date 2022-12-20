package com.example.engineandroid;


import java.util.Stack;

public class SceneMngrAndroid {

    Stack<Scene> stack;

    public SceneMngrAndroid(){
        stack = new Stack<>();
    }

    public void popScene(){
        stack.pop();
        if(!stack.empty()){
            stack.peek().onResume();
        }
    }

    public void pushScene(Scene scene){
        stack.push(scene);
    }

    public void update(double deltaTime){
        stack.peek().update(deltaTime);
    }

    public void handleInput(EventHandler.EventType type, AdManager adManager, InputAndroid input, AudioAndroid audio, RenderAndroid render){
        stack.peek().handleInput(type, adManager, input, this, audio, render);
    }

    public void render(RenderAndroid render){
        stack.peek().render(render);
    }

    public Scene getScene(){return stack.peek();}

    public void onStop(){
        stack.peek().onStop();
    }
}

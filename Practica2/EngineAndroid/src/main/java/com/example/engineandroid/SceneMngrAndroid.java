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

    public void update(double deltaTime, AdManager adManager){
        stack.peek().update(deltaTime, adManager);
    }

    public void render(RenderAndroid render){
        stack.peek().render(render);
    }

    public Scene getScene(){return stack.peek();}
}

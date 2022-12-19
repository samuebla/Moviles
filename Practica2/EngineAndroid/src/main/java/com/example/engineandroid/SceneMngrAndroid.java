package com.example.engineandroid;


import java.io.Serializable;
import java.util.Stack;

public class SceneMngrAndroid {

    Stack<Scene> stack;

    public SceneMngrAndroid(){
        stack = new Stack<Scene>();
    }

    public void popScene(){
        stack.pop();
        if(!stack.empty()){
            stack.peek().onResume();
        }
    }

    //Returns the scene identifier that's currently on pop and warns the scene about the close action
//    public int handleClosed(){
//        if (!stack.empty())
//            return stack.peek().onClosed();
//        else
//            return -1;
//    }

    public void pushScene(Scene scene){
        stack.push(scene);
    }

    public void update(double deltaTime, AdManager adManager){
        stack.peek().update(deltaTime, adManager);
    }

    public void render(){
        stack.peek().render();
    }

    public Scene getScene(){return stack.peek();}
}

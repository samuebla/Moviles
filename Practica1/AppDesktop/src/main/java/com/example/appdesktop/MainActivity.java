package com.example.appdesktop;

import com.example.enginedesktop.EngineDesktop;
import com.example.logica.MyScene;
import javax.swing.JFrame;

public class MainActivity {

    public static void main(String[] args) {

//        renderView.setVisible(true);
        JFrame renderView = new JFrame("Mi aplicación");

        renderView.setSize(600, 400);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);

//        MyScene scene = new MyScene();

//        MyRenderClass render = new MyRenderClass(renderView);
//        scene.init(render);
//        render.setScene(scene);
//        render.resume();
        EngineDesktop engine = new EngineDesktop(renderView);


        MyScene scene = new MyScene(engine);


//        scene.init(render);
//        render.setScene(scene);
//        render.resume();
    }


}
package com.example.appdesktop;

import com.example.enginedesktop.EngineDesktop;
import com.example.logica.MyScene;
import javax.swing.JFrame;

public class MainActivity {

    public static void main(String[] args) {

//        renderView.setVisible(true);
        JFrame renderView = new JFrame("Mondongo");

        renderView.setSize(800, 600);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);

        EngineDesktop engine = new EngineDesktop(renderView);

        MyScene scene = new MyScene(engine,10,10);

//      scene.init(render);
        engine.setScene(scene);
        engine.resume();
    }


}
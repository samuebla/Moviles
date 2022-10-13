package com.example.appdesktop;

import com.example.enginedesktop.EngineDesktop;
import com.example.logica.MyScene;
import javax.swing.JFrame;

public class MainActivity {

    public static void main(String[] args) {

        renderView.setVisible(true);
        EngineDesktop engine = new EngineDesktop(renderView);

        MyScene scene = new MyScene(engine);

        engine.setScene(scene);
        engine.resume();







        JFrame renderView = new JFrame("Mi aplicación");

        renderView.setSize(600, 400);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                renderView.createBufferStrategy(2);
                break;
            }
            catch(Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }
        else {
            // En "modo debug" podríamos querer escribir esto.
            //System.out.println("BufferStrategy tras " + (100 - intentos) + " intentos.");
        }

        MyScene scene = new MyScene();

        MyRenderClass render = new MyRenderClass(renderView);
        scene.init(render);
        render.setScene(scene);
        render.resume();
    }


}
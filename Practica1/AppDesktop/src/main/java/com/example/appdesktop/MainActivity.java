package com.example.appdesktop;

import com.example.enginedesktop.EngineDesktop;
import com.example.enginedesktop.FontDesktop;
import com.example.logica.MyScene;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;

public class MainActivity {

    public static void main(String[] args) {

//        renderView.setVisible(true);
        JFrame renderView = new JFrame("Mondongo");

        renderView.setSize(720, 720);
        renderView.setBackground(Color.GRAY);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        renderView.setResizable(true);

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

        EngineDesktop engine = new EngineDesktop(renderView);

        //Inicializamos las fuentes y cargamos las que queramos
        FontDesktop[] fonts = new FontDesktop[2];

        fonts[0] = new FontDesktop(new File("Assets\\CooperBlackRegular.ttf"),0,20);
        fonts[1] = new FontDesktop(new File("Assets\\CalibriRegular.ttf"),0,20);

        String[] keys = new String[]{"Cooper","Calibri"};

        MyScene scene = new MyScene(engine,10,10,fonts,keys);

//      scene.init(render);
        engine.setScene(scene);
        engine.resume();
    }
}
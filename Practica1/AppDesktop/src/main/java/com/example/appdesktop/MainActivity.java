package com.example.appdesktop;

import com.example.enginedesktop.EngineDesktop;
import com.example.enginedesktop.FontDesktop;
import com.example.enginedesktop.ImageDesktop;
import com.example.enginedesktop.SceneMngrDesktop;
import com.example.enginedesktop.SoundDesktop;
import com.example.logica.MainMenuScene;
import com.example.logica.MyScene;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;

public class MainActivity {

    public static void main(String[] args) {

        JFrame renderView = new JFrame("NONOGRAMA");

        renderView.setSize(720, 1080);
        renderView.setBackground(Color.GRAY);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        renderView.setResizable(true);

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while (intentos-- > 0) {
            try {
                renderView.createBufferStrategy(2);
                break;
            } catch (Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }

        EngineDesktop engine = new EngineDesktop(renderView);

        //Inicializamos las fuentes y cargamos las que queramos
        FontDesktop[] fonts = new FontDesktop[5];

//        fonts[0] = new FontDesktop();
//        fonts[1] = new FontDesktop();
//        fonts[2] = new FontDesktop(new File("Assets\\CalibriRegular.ttf"), 1, 18);
//        fonts[3] = new FontDesktop(new File("Assets\\CooperBlackRegular.ttf"), 1, 40);
//        fonts[4] = new FontDesktop(new File("Assets\\CalibriRegular.ttf"), 1, 20);

//        String[] keys = new String[]{"Cooper", "Calibri", "CalibriSmall", "CooperBold", "CalibriBold"};

        //Inicializamos las imagenes y cargamos las que queramos
        ImageDesktop[] images = new ImageDesktop[2];

        try {
//            images[0] = new ImageDesktop(new File("Assets\\arrow.png"));
            images[1] = new ImageDesktop(new File("Assets\\lupa.png"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        String[] keysImages = new String[]{"Flecha", "Lupa"};

//        SoundDesktop[] sounds = new SoundDesktop[2];
//
//        try {
//            sounds[0] = new SoundDesktop(new File());
//            sounds[1] = new SoundDesktop(new File());
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//
//        String[] keysSound = new String[]{"background", "effect"};

        MainMenuScene scene = new MainMenuScene(engine);

        SceneMngrDesktop sceneMngr = new SceneMngrDesktop();

        engine.setSceneMngr(sceneMngr);
        engine.setScene(scene);
        engine.resume();
    }
}
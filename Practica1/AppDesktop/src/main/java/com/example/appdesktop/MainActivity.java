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
        FontDesktop[] fonts = new FontDesktop[4];

        fonts[0] = new FontDesktop(new File("Assets\\CooperBlackRegular.ttf"),0,40);
        fonts[1] = new FontDesktop(new File("Assets\\CalibriRegular.ttf"),0,25);
        fonts[2] = new FontDesktop(new File("Assets\\CooperBlackRegular.ttf"),1,40);
        fonts[3] = new FontDesktop(new File("Assets\\CalibriRegular.ttf"),1,20);

        String[] keys = new String[]{"Cooper","Calibri","CooperBold","CalibriBold"};

        //Inicializamos las imagenes y cargamos las que queramos
        ImageDesktop[] images = new ImageDesktop[2];

        images[0] = new ImageDesktop(new File("Assets\\arrow.png"));
        images[1] = new ImageDesktop(new File("Assets\\lupa.png"));

        String[] keysImages = new String[]{"Flecha","Lupa"};

        SoundDesktop[] sounds = new SoundDesktop[2];

        String[] keysSound = new String[]{"background","effects"};

        sounds[0] = new SoundDesktop(new File("Assets\\WiiBackgroundMusic.wav"));
        sounds[1] = new SoundDesktop(new File("Assets\\wiiClickSound.wav"));

        MainMenuScene scene = new MainMenuScene(engine, fonts, keys, images, keysImages,keysSound,sounds);

        SceneMngrDesktop sceneMngr = new SceneMngrDesktop();

        engine.setSceneMngr(sceneMngr);
        engine.setScene(scene);
        engine.resume();
    }
}
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
        renderView.setBackground(Color.WHITE);
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

        MainMenuScene scene = new MainMenuScene(engine);

        SceneMngrDesktop sceneMngr = new SceneMngrDesktop();

        engine.setSceneMngr(sceneMngr);
        engine.setScene(scene);
        engine.resume();
    }
}
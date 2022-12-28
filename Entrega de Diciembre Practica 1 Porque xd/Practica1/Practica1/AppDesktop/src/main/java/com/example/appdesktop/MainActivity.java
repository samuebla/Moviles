package com.example.appdesktop;

import com.example.enginedesktop.EngineDesktop;
import com.example.logica.MainMenuScene;

import java.awt.Color;

import javax.swing.JFrame;

public class MainActivity {

    public static void main(String[] args) {

        //Creación y customización de la ventana
        JFrame renderView = new JFrame("NONOGRAMA");

        //Tamaño inicial de la ventana
        renderView.setSize(400, 600);
        renderView.setBackground(Color.WHITE);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        renderView.setResizable(true);

        //Inicialización del engine
        EngineDesktop engine = new EngineDesktop(renderView);

        //Escena principal que tambien realiza la carga de recursos
        MainMenuScene scene = new MainMenuScene();
        engine.getSceneMngr().pushScene(scene);
        engine.setResourceScene(scene);

        //Run del engine
        engine.resume();
    }
}
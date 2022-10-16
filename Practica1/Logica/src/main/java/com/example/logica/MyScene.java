package com.example.logica;
import com.example.lib.*;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MyScene implements Scene {

    //Estuvimos pensando acerca de qué método resultaría más eficiente a la hora de comrpobar las casillas, y tuvimos 2 opciones:
    //No comprobar ni cambiar nada cuando seleccionas alguna casilla y al pulsar comprobar recorrer toda la matriz
    //O cambiar el valor de la celda en cuanto la selecciones, guardar los valores de casillas restantes y erroneas en variables
    //y al pulsar comprobar solo muestra las variables en pantalla. Guardaríamos las casillas erroneas en un array, el problema es
    //que tendriamos que volver a recorrer la matriz entera para pintarlas, así que pensamos en guardarlas en un Mapa Ordenado
    //De esta manera el coste máximo siempre seria logarítmico y en caso de que deselecciones una casilla la busqueda del mapa
    //Para eliminarla es mucho más eficiente.

    //Tenemos un Mapa Ordenado donde guardaremos las casillas seleccionadas
    private Cell[][] matriz;

    int remainingCells, wrongCells;

    JPanel panel;
    JButton playButton;
    JButton backButton;
    JButton checkButton;
    JButton giveUpButton;

    //Este mapa ordenado guarda las casillas erroneas pulsadas
    TreeMap<Integer, Integer> wrongCellsPosition = new TreeMap<>();

    private Engine engine;

    public MyScene(Engine engine, int rows, int cols) {

        //Asociamos el engine correspondiente
        this.engine = engine;

        //Creamos la matriz con el tamaño
        //AAAAAAAAAAAAAAAAAAAAA MODIFICAR TAMAÑO
        this.matriz = new Cell[rows][cols];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j] = new Cell(50+80*i, 50+80*j, 72, 72);
            }
        }

        panel = new JPanel();
        panel.setBackground(Color.RED);
        panel.setBounds(0,0,300,300);
        checkButton = new JButton("Comprobar");
        giveUpButton = new JButton("Rendirse");

        checkButton.setBounds(500,500,100,100);
        giveUpButton.setBounds(40,120,100,100);


        checkButton.setVisible(true);
        giveUpButton.setVisible(true);

        panel.add(checkButton);
        panel.add(giveUpButton);
        panel.setVisible(true);
//        engine.addComponent(panel);
//        engine.addComponent(checkButton);
//        engine.addComponent(giveUpButton);


        //Tenemos un array de listas de Ints, que son los que muestran las "posiciones" de
        //las casillas azules. Uno el horizontal y otro el vertical
        ArrayList<Integer>[] xPositionsWidth;
        ArrayList<Integer>[] xPositionsHeight;
        //Así es como se añade una posicion como si hicieras un emplace_back
        //xPositionsWidth[0].add(8);



        //Input del botón de comproabr
        checkButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                //PROBABLEMENTE VACIO
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                //Cambiar de color o algo no se.
                //CAMBIAR EL TAMAÑO Y HACERLO MAS PEQUEÑITO
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                //NO ES JTEXTFIELD PERO NO ME APETECE MIRAR
                //Mostramos el mensaje en rojo si faltan casillas o tienes alguna mal seleccionada
                if(remainingCells == 1) {
                    JTextField wiwi = new JTextField("Te falta " + remainingCells + " casilla");
                }
                else{
                    JTextField wiwi = new JTextField("Te faltan " + remainingCells + " casillas");
                }
                if(wrongCells == 1) {
                    JTextField wiwi = new JTextField("Tienes mal " + wrongCells + " casilla");
                }
                else{
                    JTextField wiwi = new JTextField("Tienes mal " + wrongCells + " casillas");
                }
            }

            //Esto lo que nos salga del higo
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });

    }

    @Override
    public void update(double deltaTime) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].update(deltaTime);
            }
        }
    }

    @Override
    public void render() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].render(engine);
            }
        }
    }
}
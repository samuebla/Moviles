package com.example.logica;
import com.example.lib.Engine;

import com.example.lib.Engine;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;

public class MyScene {

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

    JButton playButton;
    JButton backButton;
    JButton checkButton;
    JButton giveUpButton;

    //Este mapa ordenado guarda las casillas erroneas pulsadas
    TreeMap<Integer, Integer> wrongCellsPosition = new TreeMap<>();

    private Engine engine;

    public MyScene(Engine engine) {

        //Asociamos el engine correspondiente
        this.engine = engine;

        //Creamos la matriz con el tamaño
        //AAAAAAAAAAAAAAAAAAAAA MODIFICAR TAMAÑO
        this.matriz = new Cell[20][20];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j] = new Cell(50, 50, 10, 150);
            }
        }

//        this.matriz = new Cell(50, 50, 10, 150, engine.getWidth());

        //Tenemos un array de listas de Ints, que son los que muestran las "posiciones" de
        //las casillas azules. Uno el horizontal y otro el vertical
        ArrayList<Integer>[] xPositionsWidth;
        ArrayList<Integer>[] xPositionsHeight;
        //Así es como se añade una posicion como si hicieras un emplace_back
        //xPositionsWidth[0].add(8);

        checkButton.setVisible(true);
        giveUpButton.setVisible(true);
    }

    public void update(double deltaTime) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].update(deltaTime);
            }
        }
    }

    public void render() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].render(engine);
            }
        }
    }
}
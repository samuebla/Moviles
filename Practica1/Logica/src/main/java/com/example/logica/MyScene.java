package com.example.logica;
import com.example.lib.Engine;

import java.util.TreeMap;
import javax.swing.JButton;



//Struct
public class Cell{

    public enum cellType {EMPTY,SELECTED,CROSSED,WRONG};

    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private String color;

    cellType type;
    boolean solution = false;


    public Cell(float x1aux, float y1aux,float x2aux, float y2aux){
        this.x1=x1aux;
        this.y1=y1aux;
        this.x2=x2aux;
        this.y2=y2aux;
    }

    public void update(double deltaTime){

    }
    public void render(Engine engine) {
        engine.paintCell(this.x1, this.y1,this.x2, this.y2, type);
    }

    //PROVISIONAL
    int size;
    public void setSize(int sizeAux) { size = sizeAux;}
    public int getSize(){ return size; }


    public void setType(cellType aux){
        type = aux;
    }

    public void setSolution(boolean aux){
        solution = aux;
    }

    public cellType getType(){
        return type;
    }

    public boolean getSolution(){
        return solution;
    }

    public void setColor(String color) { this.color=color; }
}

public class MyScene {

    //Estuvimos pensando acerca de qué método resultaría más eficiente a la hora de comrpobar las casillas, y tuvimos 2 opciones:
    //No comprobar ni cambiar nada cuando seleccionas alguna casilla y al pulsar comprobar recorrer toda la matriz
    //O cambiar el valor de la celda en cuanto la selecciones, guardar los valores de casillas restantes y erroneas en variables
    //y al pulsar comprobar solo muestra las variables en pantalla. Guardaríamos las casillas erroneas en un array, el problema es
    //que tendriamos que volver a recorrer la matriz entera para pintarlas, así que pensamos en guardarlas en un Mapa Ordenado
    //De esta manera el coste máximo siempre seria logarítmico y en caso de que deselecciones una casilla la busqueda del mapa
    //Para eliminarla es mucho más eficiente.

    //Tenemos un Mapa Ordenado donde guardaremos las casillas seleccionadas
    private Cell [][] matriz = new Cell[2][2];
    int remainingCells,wrongCells;

    JButton playButton;
    JButton backButton;
    JButton checkButton;
    JButton giveUpButton;


    TreeMap<Integer,Integer> wewe = new TreeMap<>();

    private Engine engine;

    public MyScene(Engine engine){
        this.engine = engine;

        this.matriz = new Cell(50,50,10,150,engine.getWidth());

        for(int i = 0; i< matriz.length;i++){
            for(int j=0;j<matriz.length;j++){
                this.matriz[i][j].setColor("blue");
            }

        }
    }

    public void update(double deltaTime){
        for(int i = 0; i< matriz.length;i++)
            for(int j=0;j<matriz.length;j++) {
                this.matriz[i][j].update(deltaTime);
            }
    }

    public void render(){
        for(int i = 0; i< matriz.length;i++)
            for(int j=0;j<matriz.length;j++) {
                this.matriz[i][j].render(engine);
            }
    }
}
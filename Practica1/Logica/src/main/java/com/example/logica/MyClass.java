package com.example.logica;
import java.util.TreeMap;

enum cellType {EMPTY,SELECTED,CROSSED,WRONG};

//Struct
class Cell{

    cellType type;
    boolean solution = false;

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
}

public class MyClass {

    //Estuvimos pensando acerca de qué método resultaría más eficiente a la hora de comrpobar las casillas, y tuvimos 2 opciones:
    //No comprobar ni cambiar nada cuando seleccionas alguna casilla y al pulsar comprobar recorrer toda la matriz
    //O cambiar el valor de la celda en cuanto la selecciones, guardar los valores de casillas restantes y erroneas en variables
    //y al pulsar comprobar solo muestra las variables en pantalla. Guardaríamos las casillas erroneas en un array, el problema es
    //que tendriamos que volver a recorrer la matriz entera para pintarlas, así que pensamos en guardarlas en un Mapa Ordenado
    //De esta manera el coste máximo siempre seria logarítmico y en caso de que deselecciones una casilla la busqueda del mapa
    //Para eliminarla es mucho más eficiente.

    //Tenemos un Mapa Ordenado donde guardaremos las casillas seleccionadas
    Cell [][] matriz = new Cell[2][2];
    int remainingCells,wrongCells;

    TreeMap<Integer,Integer> wewe = new TreeMap<>();
}
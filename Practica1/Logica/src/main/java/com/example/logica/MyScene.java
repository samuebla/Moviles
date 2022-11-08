package com.example.logica;

import com.example.lib.*;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import sun.reflect.generics.tree.Tree;

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

    int rows_, cols_;

    //Tenemos un array de listas de Ints, que son los que muestran las "posiciones" de
    //las casillas azules. Uno el horizontal y otro el vertical
    private ArrayList<Integer>[] xPositionsTopToBottom;
    private ArrayList<Integer>[] xPositionsLeftToRight;
    private String[] xNumberTopToBottom;
    private ArrayList<String>[] xNumberLeftToRight;


    PriorityQueue<Vector2D> wrongCellsPosition;
    int remainingCells, wrongCells, maxCellsSolution;

    HashMap<String, IFont> fonts;


    private Button checkButton;
    private Button giveUpButton;

    boolean showAnswers;

    private Engine engine;

    public MyScene(Engine engine, int rows, int cols, HashMap<String, IFont> fontsAux) {

        //Asociamos el engine correspondiente
        this.engine = engine;

        this.fonts = fontsAux;

        this.checkButton = new Button(600, 500, 70, 50);

        //Creamos el random
        Random random = new Random();

        //Creamos la matriz con el tamaño
        this.matriz = new Cell[cols][rows];

        //Inizializamos el treeMap de Posiciones
        wrongCellsPosition = new PriorityQueue<>();

        remainingCells = 0;
        wrongCells = 0;
        showAnswers = false;

        rows_ = rows;
        cols_ = cols;

        xPositionsTopToBottom = new ArrayList[rows_];
        xPositionsLeftToRight = new ArrayList[cols_];
        xNumberTopToBottom = new String[rows_];
        xNumberLeftToRight = new ArrayList[cols_];


        //Iniziamos la matriz
        for (int i = 0; i < rows_; i++) {
            for (int j = 0; j < cols_; j++) {
                //Primero J que son las columnas en X y luego las filas en I
                this.matriz[j][i] = new Cell(80 + 60 * j, 150 + 60 * i, 54, 54);
            }
        }

        //Variable auxiliar solo para que la creacion aleatoria tenga más sentido
        ArrayList<Integer> colums = new ArrayList<>();

        for (int i = 0; i < rows_; i++) {
            xPositionsTopToBottom[i] = new ArrayList<>();
        }


        int[] numAnterior = new int[cols_];
        int[] contadorCols = new int[cols_];
        //Inicializamos los valores a -1
        for (int i = 0; i < cols_; i++) {
            colums.add(0);
            numAnterior[i] = -1;
            contadorCols[i] = 1;
            xPositionsLeftToRight[i] = new ArrayList<>();
            xNumberLeftToRight[i] = new ArrayList<>();
        }

        //CREACION ALEATORIA DEL TABLERO
        for (int i = 0; i < rows_; i++) {
            //Contador de celdas azules por cada columna
            int numSolutionPerRows = 0;
            int contAux = 0;

            for (int j = 0; j < cols_; j++) {

                int aux = random.nextInt(2);

                //Si es 0 NO SE RELLENA
                if (aux == 0) {
                    //Si estabas sumando y luego te llego a 0...
                    if (contAux != 0) {
                        xPositionsTopToBottom[i].add(contAux);
                        contAux = 0;
                    }
                    this.matriz[j][i].setSolution(false);

                    //Para el valor de las columnas...
                    if (numAnterior[j] == 0) {
                        //Reseteamos
                        contadorCols[j] = 1;
                        numAnterior[j] = -1;

                    }
                }
                //Si es 1 se rellena
                else {
                    //Lo añadimos a la lista de celdas que tiene que acertar el jugador
                    remainingCells++;


                    this.matriz[j][i].setSolution(true);
                    numSolutionPerRows++;
                    //Para averiguar los numeros laterales de las celdas
                    contAux++;

                    //Para que no haya ninguna fila o columna vacía
                    //todo wewe
                    colums.set(j, colums.get(j) + 1);

                    //PARA AUXILIAR
                    //Si nunca se han añadido...
                    if (numAnterior[j] == -1) {
                        //Metemos el primero...
                        xPositionsLeftToRight[j].add(1);
                        //Y por lo tanto ya tenemos uno añadito
                        numAnterior[j] = 0;
                        //Con esto solo entra si se ha añadido algo alguna vez
                    } else if (numAnterior[j] == 0) {
                        contadorCols[j]++;
                        //Sumamos el valor +1 porque la columna continua
                        //Eliminamos el anterior
                        xPositionsLeftToRight[j].remove(xPositionsLeftToRight[j].size() - 1);
                        //Y metemos el nuevo
                        xPositionsLeftToRight[j].add(xPositionsLeftToRight[j].size(), contadorCols[j]);
                    }
                }
            }

            //Si casualmente la fila se ha quedado totalmente vacia
            if (numSolutionPerRows == 0) {
                //Minimo rellenamos una
                this.matriz[random.nextInt(cols_)][i].setSolution(true);
                xPositionsTopToBottom[i].add(1);
            }
            //Si por el contrario todas se han rellenado
            else if (numSolutionPerRows == cols_) {
                //AAA MENCIONAR TODO EN EL PDF QUE ESTO COMPLICA TODO PERO SE QUEDA UN CUADRADO MAS BONITO Y CURRAO
                int aux = random.nextInt(cols_);
                //Dejamos al menos una vacia
                this.matriz[aux][i].setSolution(false);

                //Y añadimos al lateral los 2 valores seccionados
                xPositionsTopToBottom[i].add(aux + 1);
                xPositionsTopToBottom[i].add(contAux - aux);
            }

            //Para meter en el lateral si el ultimo valor de la fila se ha seleccionado
            if (contAux != 0) {
                xPositionsTopToBottom[i].add(contAux);
            }
        }

        //REVISAR PQ CREO QUE ESTO DA PROBLEMITAS todo
        //Ahora hacemos lo mismo pero para las columnas
        for (int i = 0; i < cols_; i++) {
            //Si casualmente la columna se ha quedado totalmente vacia
            if (colums.get(i) == 0) {
                //Minimo rellenamos una
                this.matriz[i][random.nextInt(rows_)].setSolution(true);
            }
            //EN UN NONOGRAMA ES NORMAL UNA FILA/COLUMNA CON TO SELECCIONADO, pero hago la comprobacion en las filas
            //Para evitar cubos grandes que no tengan forma y solo sean relleno y evitar que salga algo compacto
            //Si por el contrario todas se han rellenado
//            else if(colums.get(i) == rows_){
//                //Dejamos al menos una vacia
//                this.matriz[random.nextInt(rows_)][i].setSolution(false);
//            }
        }

        //Establecemos el numero completo de casillas que resolver
        maxCellsSolution = remainingCells;

        //REVISAR
        for (int i = 0; i < xPositionsLeftToRight.length; i++) {
            for (int j = 0; j < xPositionsLeftToRight[i].size(); j++) {
                xNumberLeftToRight[i].add(xPositionsLeftToRight[i].get(j).toString());
            }
        }

        for (int i = 0; i < xPositionsTopToBottom.length; i++) {
            xNumberTopToBottom[i] = "";
            for (int j = 0; j < xPositionsTopToBottom[i].size(); j++) {
                xNumberTopToBottom[i] += xPositionsTopToBottom[i].get(j).toString() + " ";
            }
        }

    }

    public boolean inputReceived(Vector2D pos, Vector2D size) {
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getRawCoords().getX(), engine.getInput().getRawCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    @Override
    public void update(double deltaTime) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].update(deltaTime);
            }
        }

        if (engine.getEventMngr().getEvent().eventType != IEventHandler.EventType.NONE) {
            handleInput();
            engine.getEventMngr().sendEvent(IEventHandler.EventType.NONE);
        }
    }


    @Override
    public void render() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].render(engine);
            }
        }
        for (int i = 0; i < xNumberTopToBottom.length; i++) {
            engine.drawText(xNumberTopToBottom[i], 20, 180 + 60 * i, "Black", fonts.get("Calibri"));

        }
        for (int i = 0; i < xNumberLeftToRight.length; i++) {
            for (int j = 0; j < xNumberLeftToRight[i].size(); j++) {
                engine.drawText(xNumberLeftToRight[i].get(j), 100 + 60 * i, 50 + 30 * j, "Black", fonts.get("Calibri"));
            }
        }

        //Botones
        this.engine.drawText("Comprobar", (int) (checkButton.getPos().getX() + checkButton.getSize().getX() / 3.5), (int) (checkButton.getPos().getY() + checkButton.getSize().getY() / 2), "Black", fonts.get("Calibri"));

        if (showAnswers) {
            this.engine.drawText("Te falta(n) " + remainingCells + " casilla(s)", 100, 600, "red", fonts.get("Calibri"));
            this.engine.drawText("Tienes mal " + wrongCells + " casilla(s)", 100, 630, "red", fonts.get("Calibri"));
        }
    }

    @Override
    public void handleInput() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (inputReceived(this.matriz[i][j].getPos(), this.matriz[i][j].getSize())) {
                    //Aqui se guarda si te has equivocado...
                    this.matriz[i][j].handleInput();
                    //1 Si esta mal
                    //2 Si lo seleccionas y esta bien
                    //3 Si estaba mal seleccionado y lo deseleccionas
                    //4 Si estaba bien seleccionado y lo deseleccionas
                    int key = this.matriz[i][j].keyCell();
                    if (key == 1) {
                        //Lo metemos en el treeMap
//                        wrongCellsPosition.add(new Vector2D(i,j));
                        wrongCells++;
                    } else if (key == 2) {
                        remainingCells--;
                    } else if (key == 3) {
                        wrongCells--;
                    } else if (key == 4) {
                        remainingCells++;
                    }
                }
            }
        }

        //BOTONES
        if (inputReceived(this.checkButton.getPos(), this.checkButton.getSize())) {
            //Mostramos el texto en pantalla
            showAnswers = !showAnswers;
        }
    }
}
package com.example.logica;

import com.example.lib.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

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

    int remainingCells, wrongCells, maxCellsSolution;

    int widthAestheticCellX,heightAestheticCellX,widthAestheticCellY,heightAestheticCellY;

    HashMap<String, IFont> fonts;
    HashMap<String, IImage> images;
    HashMap<String, ISound> sounds;

    private Button checkButton;
    private Button giveUpButton;
    private Button backButton;

    boolean won;
    boolean showAnswers;
    double timer;
    private static final int timeCheckButton = 5;

    private Engine engine;

    public MyScene(Engine engine, int rows, int cols, HashMap<String, IFont> fontsAux, HashMap<String, IImage> imagesAux,HashMap<String, ISound> soundsAux) {

        //Asociamos el engine correspondiente
        this.engine = engine;

        this.fonts = fontsAux;
        this.images = imagesAux;
        this.sounds = soundsAux;

        this.checkButton = new Button(560, 40, 140, 30);
        this.giveUpButton = new Button(10, 50, 120, 30);
        this.backButton = new Button(320, 960, 60, 30);

        //Creamos el random
        Random random = new Random();

        //Creamos la matriz con el tamaño
        this.matriz = new Cell[cols][rows];

        remainingCells = 0;
        wrongCells = 0;
        showAnswers = false;
        won = false;
        timer = 0;

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
                this.matriz[j][i] = new Cell(90 + 60 * j, 320 + 60 * i, 54, 54);
            }
        }
        //Tamaño de las cuadriculas que recubren el nonograma
        widthAestheticCellX = (int)(this.matriz[cols_-1][rows_-1].getPos().getX()) + 45;
        heightAestheticCellX = (int)( this.matriz[cols_-1][rows_-1].getPos().getY() - this.matriz[0][0].getPos().getY() + 65);

        widthAestheticCellY = (int)((this.matriz[cols_-1][0].getPos().getX()) - this.matriz[0][0].getPos().getX()) + 65;
        heightAestheticCellY = (int)( this.matriz[cols_-1][rows_-1].getPos().getY() -200 + 60);

        for (int i = 0; i < rows_; i++) {
            xPositionsTopToBottom[i] = new ArrayList<>();
        }

        int[] numAnterior = new int[cols_];
        int[] contadorCols = new int[cols_];
        //Inicializamos los valores a -1
        for (int i = 0; i < cols_; i++) {
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

                int aux = random.nextInt(10);

                //40% de probabilidad
                //Si es 0 NO SE RELLENA
                if (aux < 4) {
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
                //60% de probabilidad
                //Si es 1 se rellena
                else {
                    //Lo añadimos a la lista de celdas que tiene que acertar el jugador
                    remainingCells++;


                    this.matriz[j][i].setSolution(true);
                    numSolutionPerRows++;
                    //Para averiguar los numeros laterales de las celdas
                    contAux++;

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

        //Ahora hacemos lo mismo pero para las columnas
        for (int i = 0; i < cols_; i++) {
            //Si casualmente la columna se ha quedado totalmente vacia
            if (xPositionsLeftToRight[i].size() == 0) {
                int randAux = random.nextInt(rows_);
                //Minimo rellenamos una
                this.matriz[i][randAux].setSolution(true);
                xPositionsLeftToRight[i].add(1);

                //Limpiamos la lateral
                xPositionsTopToBottom[i].clear();

                int cont = 0;
                //Recorremos la columna otra vez para rellenar correctamente la fila
                for(int j=0;j<cols_;j++){
                    if(this.matriz[j][i].getSolution()) {
                        cont++;
                    }
                    if(cont!=0){
                        xPositionsTopToBottom[i].add(cont);
                    }
                }
                if(cont!=0){
                    xPositionsTopToBottom[i].add(cont);
                }
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
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

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

        if(timer>0){
            timer -=deltaTime;
        }
        else{
            showAnswers = false;
        }
    }

    @Override
    public void render() {
        if(won){
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    this.matriz[i][j].solutionRender(engine);
                }
            }

            //Mensaje de enhorabuena
            this.engine.drawText("ENHORABUENA!", 200, 120, "Black", fonts.get("Cooper"));

            //BackButton
            this.engine.drawText("Volver", (int)(backButton.getPos().getX()), (int)(backButton.getPos().getY() + 20), "Black", fonts.get("CalibriBold"));
        }
        else{
            //Si tienes pulsado el boton de comprobar...
            if (showAnswers) {
                //Muestra el texto...
                this.engine.drawText("Te falta(n) " + remainingCells + " casilla(s)", 250, 120, "red", fonts.get("Calibri"));
                this.engine.drawText("Tienes mal " + wrongCells + " casilla(s)", 250, 150, "red", fonts.get("Calibri"));

                //Renderiza rojo si esta mal
                for (int i = 0; i < matriz.length; i++) {
                    for (int j = 0; j < matriz[i].length; j++) {
                        this.matriz[i][j].trueRender(engine);
                    }
                }
            } else {
                //Render normal
                for (int i = 0; i < matriz.length; i++) {
                    for (int j = 0; j < matriz[i].length; j++) {
                        this.matriz[i][j].render(engine);
                    }
                }
            }
            //Numeros laterales
            for (int i = 0; i < xNumberTopToBottom.length; i++) {
                engine.drawText(xNumberTopToBottom[i], 20, 350 + 60 * i, "Black", fonts.get("CalibriSmall"));
            }
            for (int i = 0; i < xNumberLeftToRight.length; i++) {
                for (int j = 0; j < xNumberLeftToRight[i].size(); j++) {
                    engine.drawText(xNumberLeftToRight[i].get(j), 100 + 60 * i, 200 + 30 * j, "Black", fonts.get("CalibriSmall"));
                }
            }
            //Cuadriculas
            //Ancha
            this.engine.paintCell(15,315, widthAestheticCellX,heightAestheticCellX, -1);
            //Larga
            this.engine.paintCell(85,180, widthAestheticCellY,heightAestheticCellY, -1);

            //Botones
            this.engine.drawImage(570, 45, 590, 65, this.images.get("Lupa"));
            this.engine.drawText("Comprobar", (int) (checkButton.getPos().getX() + checkButton.getSize().getX() / 3.5), (int) (checkButton.getPos().getY() + checkButton.getSize().getY() / 1.7), "Black", fonts.get("CalibriBold"));

            this.engine.drawImage(10, 50, 50, 75, this.images.get("Flecha"));
            this.engine.drawText("Rendirse", (int)(giveUpButton.getPos().getX() +50), (int)(giveUpButton.getPos().getY() + 20), "Black", fonts.get("CalibriBold"));
        }
    }

    @Override
    public void handleInput() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (inputReceived(this.matriz[i][j].getPos(), this.matriz[i][j].getSize())) {
                    //Aqui se guarda si te has equivocado...
                    this.matriz[i][j].handleInput(engine);
                    //1 Si esta mal
                    //2 Si lo seleccionas y esta bien
                    //3 Si estaba mal seleccionado y lo deseleccionas
                    //4 Si estaba bien seleccionado y lo deseleccionas
                    int key = this.matriz[i][j].keyCell();
                    if (key == 1) {
                        wrongCells++;
                    } else if (key == 2) {
                        remainingCells--;
                        if(win()){
                            won = true;
                        }
                    } else if (key == 3) {
                        //Lo eliminamos de estas celdas
                        wrongCells--;
                        if(win()){
                            won = true;
                        }
                    } else if (key == 4) {
                        remainingCells++;
                    }
                    //Y playeamos el sonido
                    sounds.get("effect").play();
                }
            }
        }

        //BOTONES
        if (inputReceived(this.checkButton.getPos(), this.checkButton.getSize())) {
            //Mostramos el texto en pantalla
            showAnswers = true;
            timer = timeCheckButton;

        }
        if (inputReceived(this.giveUpButton.getPos(), this.giveUpButton.getSize())) {
            this.engine.popScene();
        }
        //Solo funciona si has ganado
        if (won && inputReceived(this.backButton.getPos(), this.backButton.getSize())) {
            this.engine.popScene();
            this.engine.popScene();

        }
    }
    private boolean win() { return remainingCells == 0 && wrongCells == 0;}
}


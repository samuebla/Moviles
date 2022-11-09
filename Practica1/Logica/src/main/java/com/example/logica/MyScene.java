package com.example.logica;

import com.example.lib.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class MyScene implements Scene {

    private Engine engine;

    //Tenemos una matriz donde guardaremos las casillas seleccionadas
    private Cell[][] matriz;

    int rows_, cols_;

    //Para mostrar en pantallas la info de las celdas
    int remainingCells, wrongCells, maxCellsSolution;

    private Button checkButton;
    private Button giveUpButton;
    private Button backButton;

    //Tenemos un array de listas de Ints, que son los que muestran las "posiciones" de las casillas azules. Uno el horizontal y otro el vertical
    private ArrayList<Integer>[] xPositionsTopToBottom;
    private ArrayList<Integer>[] xPositionsLeftToRight;

    //Esto es para coger los numeros de antes y mostrarlos en pantalla como un string
    private String[] xNumberTopToBottom;
    private ArrayList<String>[] xNumberLeftToRight;

    //Para los rectangulos que recubren las celdas y son meramente esteticos
    int widthAestheticCellX, heightAestheticCellX, widthAestheticCellY, heightAestheticCellY;

    private static final int timeCheckButton = 5;
    double timer;
    boolean won;
    boolean showAnswers;
    boolean auxShowAnswer;

    public MyScene(Engine engine, int rows, int cols) {

        //Asociamos el engine correspondiente
        this.engine = engine;

        init();

        //Creamos el random
        Random random = new Random();

        //Creamos la matriz con el tamaño
        this.matriz = new Cell[cols][rows];

        //Seteamos valores iniciales
        remainingCells = 0;
        wrongCells = 0;
        showAnswers = false;
        auxShowAnswer = false;
        won = false;
        timer = 0;

        rows_ = rows;
        cols_ = cols;

        xPositionsTopToBottom = new ArrayList[rows_];
        xPositionsLeftToRight = new ArrayList[cols_];
        xNumberTopToBottom = new String[rows_];
        xNumberLeftToRight = new ArrayList[cols_];

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

        //Iniciamos la matriz
        for (int i = 0; i < rows_; i++) {
            for (int j = 0; j < cols_; j++) {
                //Primero J que son las columnas en X y luego las filas en Y
                this.matriz[j][i] = new Cell((int)((double)this.engine.getWidth()*0.125) + (int)((double)this.engine.getWidth()*0.083333) * j,
                        (int)((double)this.engine.getHeight()*0.296296296) + (int)((double)this.engine.getHeight()*0.055555555) * i, (int)((double)this.engine.getWidth()*0.075), (int)((double)this.engine.getHeight()*0.05));
            }
        }

        //Tamaño de las cuadriculas que recubren el nonograma
        widthAestheticCellX = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getX()) + (int)((double)this.engine.getWidth()*0.0625);
        heightAestheticCellX = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getY() - this.matriz[0][0].getPos().getY() + (int)((double)this.engine.getHeight()*0.0601851));

        widthAestheticCellY = (int) ((this.matriz[cols_ - 1][0].getPos().getX()) - this.matriz[0][0].getPos().getX()) + (int)((double)this.engine.getWidth()*0.0902777);
        heightAestheticCellY = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getY() - (int)((double)this.engine.getHeight()*0.111111111));

        //CREACION ALEATORIA DEL TABLERO
        for (int i = 0; i < rows_; i++) {

            //Contador de celdas azules por cada fila
            int contAux = 0;

            //Para evitar Filas vacias o llenas
            int numSolutionPerRows = 0;

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

                    //Si nunca se han añadido en la celda de arriba suya...
                    if (numAnterior[j] == -1) {

                        //Metemos el primero...
                        xPositionsLeftToRight[j].add(1);

                        //Y por lo tanto ya tenemos uno añadido
                        numAnterior[j] = 0;

                        //Con esto solo entra si se ha añadido algo alguna vez
                    } else if (numAnterior[j] == 0) {

                        //Sumamos el valor +1 porque la columna continua
                        contadorCols[j]++;

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

                //Y la contabilizamos
                remainingCells++;

            }
            //Si por el contrario todas se han rellenado
            else if (numSolutionPerRows == cols_) {
                int aux = random.nextInt(cols_);
                //Dejamos al menos una vacia
                this.matriz[aux][i].setSolution(false);
                System.out.println("KAPASAOOOOOOOOOOO  " + aux);

                //Vaciamos la lista
                xPositionsTopToBottom[i].clear();
                //Y añadimos al lateral los 2 valores seccionados
                //Si has quitado la primera celda no añades un 0
                if (aux != 0)
                    xPositionsTopToBottom[i].add(aux);
                //Si has quitado la ultima celda no se añade un 0
                if (cols - aux - 1 != 0)
                    xPositionsTopToBottom[i].add(cols - aux - 1);

                //Y contabilizamos esa resta
                remainingCells--;

                //Ahora limpiamos la columna correspondiente y volvemos a contar
                xPositionsLeftToRight[aux].clear();

                int cont = 0;
                //Recorremos la columna otra vez para rellenar correctamente la fila
                for (int j = 0; j < rows_; j++) {
                    if (this.matriz[aux][j].getSolution()) {
                        cont++;
                    } else if (cont != 0) {
                        xPositionsLeftToRight[aux].add(cont);
                        cont = 0;
                    }
                }
                if (cont != 0) {
                    xPositionsLeftToRight[aux].add(cont);
                }
            }
            //Para meter en el lateral si el ultimo valor de la fila se ha seleccionado
            else if (contAux != 0) {
                System.out.println("KAPASAOOOOOOOOOOO123123  " );

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

                //Contabilizamos esa suma
                remainingCells++;

                //Limpiamos el lateral
                xPositionsTopToBottom[randAux].clear();

                int cont = 0;
                //Recorremos la columna otra vez para rellenar correctamente la fila
                for (int j = 0; j < cols_; j++) {
                    if (this.matriz[j][randAux].getSolution()) {
                        cont++;
                    } else if (cont != 0) {
                        xPositionsTopToBottom[randAux].add(cont);
                        cont = 0;
                    }
                }
                if (cont != 0) {
                    xPositionsTopToBottom[randAux].add(cont);
                }
            }
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

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size) {
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {
        //Seteamos los botones
        this.checkButton = new Button((double)this.engine.getWidth()*0.8, (double)this.engine.getHeight()*0.06,
                (double)this.engine.getWidth()*0.1666666, (double)this.engine.getHeight()*0.10);
        this.giveUpButton = new Button((double)this.engine.getWidth()*0.01388888, (double)this.engine.getHeight()*0.04629629,
                (double)this.engine.getWidth()*0.1666666, (double)this.engine.getHeight()*0.10);
        this.backButton = new Button((double)this.engine.getWidth()*0.44444444, (double)this.engine.getHeight()*0.888888888,
                (double)this.engine.getWidth()*0.0833333, (double)this.engine.getHeight()*0.02777777);
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

        //Timer del boton de comprobar
        if (timer > 0) {
            timer -= deltaTime;
        } else if (auxShowAnswer) {
            showAnswers = false;
            wrongCells = 0;
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    this.matriz[i][j].changeEmptyCells();
                }
            }
            auxShowAnswer = false;
            if (win())
                won = true;
        }
    }

    @Override
    public void render() {

        Vector2D auxCuadradoFinal = this.matriz[cols_-1][rows_-1].getPos();
        Vector2D auxCuadradoInicio = this.matriz[0][0].getPos();

        //El cuadrado se mantiene aunque ganes porque es muy bonito
        this.engine.drawImage((int)(auxCuadradoInicio.getX()-((double)(this.engine.getWidth())/72.0)), (int)(auxCuadradoInicio.getY()-((double)(this.engine.getHeight())/108)),
                (int)(auxCuadradoFinal.getX()-auxCuadradoInicio.getX() + engine.getWidth()/36 + this.engine.getWidth()*0.075)
               , (int)(auxCuadradoFinal.getY()-auxCuadradoInicio.getY() + engine.getHeight()/54 + this.engine.getHeight()*0.05), "Board");

        //Si ya he ganado...
        if (won) {
            //Solo renderizo las azules
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    this.matriz[i][j].solutionRender(engine);
                }
            }

            //Mensaje de enhorabuena
            this.engine.drawText("ENHORABUENA!", (int)((double)this.engine.getWidth()*0.27777), (int)((double)this.engine.getHeight()*0.1111111), "Black", "Cooper");

            //BackButton
            this.engine.drawText("Volver", (int) (backButton.getPos().getX()), (int) (backButton.getPos().getY() + (int)((double)this.engine.getHeight()*0.0185185)), "Black", "CalibriBold");

            //Si sigo jugando...
        } else {
            //Si tienes pulsado el boton de comprobar...
            if (showAnswers) {
                //Muestra el texto...
                this.engine.drawText("Te falta(n) " + remainingCells + " casilla(s)", (int)((double)this.engine.getWidth()*0.34722222), (int)((double)this.engine.getHeight()*0.1111111), "red", "Calibri");
                this.engine.drawText("Tienes mal " + wrongCells + " casilla(s)", (int)((double)this.engine.getWidth()*0.3472222222), (int)((double)this.engine.getHeight()*0.1388888), "red", "Calibri");

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

            //NUMEROS LATERALES
            for (int i = 0; i < xNumberTopToBottom.length; i++) {
                engine.drawText(xNumberTopToBottom[i], (int)(this.matriz[0][0].getPos().getX()/2), (int)((double)this.engine.getHeight()*0.3240740) + (int)((double)this.engine.getHeight()*0.0555555) * i, "Black", "CalibriSmall");
            }
            for (int i = 0; i < xNumberLeftToRight.length; i++) {
                for (int j = 0; j < xNumberLeftToRight[i].size(); j++) {
                    engine.drawText(xNumberLeftToRight[i].get(j), (int)((double)this.engine.getWidth()*0.1388888) + (int)((double)this.engine.getWidth()*0.083333) * i, (int)((double)this.engine.getHeight()*0.185185) + (int)((double)this.engine.getHeight()*0.027777) * j, "Black", "CalibriSmall");
                }
            }

            //BOTONES
            this.engine.drawImage((int)((double)checkButton.getPos().getX()), (int)((double)checkButton.getPos().getY()), (int)((double)checkButton.getSize().getX()), (int)((double)checkButton.getSize().getY()), "Check");

            this.engine.drawImage((int)((double)giveUpButton.getPos().getX()), (int)((double)giveUpButton.getPos().getY()), (int)((double)giveUpButton.getSize().getX()), (int)((double)giveUpButton.getSize().getY()), "GiveUp");
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
                        if (win()) {
                            won = true;
                        }
                    } else if (key == 3) {
                        //Lo eliminamos de estas celdas
                        wrongCells--;
                        if (win()) {
                            won = true;
                        }
                    } else if (key == 4) {
                        remainingCells++;
                    }
                    //Y playeamos el sonido
                    engine.getAudio().playSound("effect", 1);
                }
            }
        }

        //BOTONES
        //Boton de comprobar
        if (inputReceived(this.checkButton.getPos(), this.checkButton.getSize())) {
            //Mostramos el texto en pantalla
            showAnswers = true;
            auxShowAnswer = true;
            timer = timeCheckButton;
        }
        //Si te rindes vuelves a la seleccion de nivel
        if (inputReceived(this.giveUpButton.getPos(), this.giveUpButton.getSize())) {
            this.engine.popScene();
        }
        //Solo funciona si has ganado
        if (won && inputReceived(this.backButton.getPos(), this.backButton.getSize())) {
            this.engine.popScene();
            this.engine.popScene();
        }
    }

    //Comprueba si has ganado o no
    private boolean win() {
        return remainingCells == 0 && wrongCells == 0;
    }
}
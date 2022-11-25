package com.example.practica1;

import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HistoryModeGameScene implements Scene {
    private EngineApp engine;

    //Tenemos una matriz donde guardaremos las casillas seleccionadas
    private CellHistoryMode[][] matriz;

    int rows_, cols_;

    //LAS MONEDAS TIENEN QUE ESTAS EN UN TXT A PARTE PARA SUMAR Y RESTAR SIN IMPORTAR EL NIVEL Y QUE SIEMPRE SE GUARDEN AAAAA
    int lives, coins;

    //Para mostrar en pantallas la info de las celdas
    int remainingCells, maxCellsSolution;

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

    public HistoryModeGameScene(EngineApp engine, int rows, int cols, String file) {

        //Asociamos el engine correspondiente
        this.engine = engine;


        //Creamos la matriz con el tamaño
        this.matriz = new CellHistoryMode[cols][rows];

        //Seteamos valores iniciales
        remainingCells = 0;
        showAnswers = false;
        won = false;
        timer = 0;
        lives = 3;

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

        //Iniciamos la matriz segun el fichero
        for (int i = 0; i < rows_; i++) {
            for (int j = 0; j < cols_; j++) {
                //Primero J que son las columnas en X y luego las filas en Y
                this.matriz[j][i] = new CellHistoryMode((int) ((double) this.engine.getWidth() * 0.125) + (int) ((double) this.engine.getWidth() * 0.083333) * j,
                        (int) ((double) this.engine.getHeight() * 0.296296296) + (int) ((double) this.engine.getHeight() * 0.055555555) * i, (int) ((double) this.engine.getWidth() * 0.075), (int) ((double) this.engine.getHeight() * 0.05));
            }
        }
        //Cargamos la info de la matriz
        loadFromFile(file);

        init();

        //Tamaño de las cuadriculas que recubren el nonograma
        widthAestheticCellX = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getX()) + (int) ((double) this.engine.getWidth() * 0.0625);
        heightAestheticCellX = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getY() - this.matriz[0][0].getPos().getY() + (int) ((double) this.engine.getHeight() * 0.0601851));

        widthAestheticCellY = (int) ((this.matriz[cols_ - 1][0].getPos().getX()) - this.matriz[0][0].getPos().getX()) + (int) ((double) this.engine.getWidth() * 0.0902777);
        heightAestheticCellY = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getY() - (int) ((double) this.engine.getHeight() * 0.111111111));
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

        //Seteamos los botones
        this.giveUpButton = new Button((double) this.engine.getWidth() * 0.01388888, (double) this.engine.getHeight() * 0.04629629,
                (double) this.engine.getWidth() * 0.1666666, (double) this.engine.getHeight() * 0.10);
        this.backButton = new Button((double) this.engine.getWidth() * 0.44444444, (double) this.engine.getHeight() / 1.1,
                (double) this.engine.getWidth() / 10, (double) this.engine.getHeight() / 15);
    }

    @Override
    public void update(double deltaTime) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].update(deltaTime);
            }
        }

        if (engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput();
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }

        //Timer del boton de comprobar
        if (timer > 0) {
            timer -= deltaTime;
        }
    }

    @Override
    public void render() {

        Vector2D auxCuadradoFinal = this.matriz[cols_ - 1][rows_ - 1].getPos();
        Vector2D auxCuadradoInicio = this.matriz[0][0].getPos();

        //El cuadrado se mantiene aunque ganes porque es muy bonito
        this.engine.drawImage((int) (auxCuadradoInicio.getX() - ((double) (this.engine.getWidth()) / 100.0)), (int) (auxCuadradoInicio.getY() - ((double) (this.engine.getHeight()) / 150)),
                (int) (auxCuadradoFinal.getX() - auxCuadradoInicio.getX() + engine.getWidth() / 50 + this.engine.getWidth() * 0.075)
                , (int) (auxCuadradoFinal.getY() - auxCuadradoInicio.getY() + engine.getHeight() / 65 + this.engine.getHeight() * 0.05), "Board");

        //Si ya he ganado...
        if (won) {
            //Solo renderizo las azules
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    this.matriz[i][j].solutionRender(engine);
                }
            }

            //Mensaje de enhorabuena
            this.engine.drawText("ENHORABUENA!", (int) ((double) this.engine.getWidth() * 0.5), (int) ((double) this.engine.getHeight() * 0.1111111), "Black", "Cooper", 0);

            //BackButton
            this.engine.drawImage((int) (backButton.getPos().getX()), (int) (backButton.getPos().getY()), (int) (backButton.getSize().getX()), (int) (backButton.getSize().getY()), "Back");
            //Si sigo jugando...
        } else {
            //Si tienes pulsado el boton de comprobar...
            //Renderiza rojo si esta mal
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    this.matriz[i][j].render(engine);
                }
            }

            //NUMEROS LATERALES
            for (int i = 0; i < xNumberTopToBottom.length; i++) {
                engine.drawText(xNumberTopToBottom[i], (int) (auxCuadradoInicio.getX() - ((double) (this.engine.getWidth()) / 90.0)), (int) ((double) this.engine.getHeight() * 0.3240740) + (int) ((double) this.engine.getHeight() * 0.0555555) * i, "Black", "CalibriSmall", 1);
            }
            for (int i = 0; i < xNumberLeftToRight.length; i++) {
                for (int j = 0; j < xNumberLeftToRight[i].size(); j++) {
                    engine.drawText(xNumberLeftToRight[i].get(j), (int) ((double) this.engine.getWidth() * 0.155) + (int) ((double) this.engine.getWidth() * 0.083333) * i, (int) ((double) this.engine.getHeight() * 0.185185) + (int) ((double) this.engine.getHeight() * 0.027777) * j, "Black", "CalibriSmall", 0);
                }
            }

            //BOTONES
            this.engine.drawImage((int) ((double) giveUpButton.getPos().getX()), (int) ((double) giveUpButton.getPos().getY()), (int) ((double) giveUpButton.getSize().getX()), (int) ((double) giveUpButton.getSize().getY()), "GiveUp");

            //ESTO ESTA SIN TESTEAR.
            //MONEDAS
            this.engine.drawText(Integer.toString(coins), engine.getWidth() - 50, 15, "Black", "CalibriSmall", 0);
            this.engine.drawImage((int) (engine.getWidth() - 30), 30, 30, 30, "Back");
            //CORAZONES
            for (int i = 0; i < lives; i++) {
                this.engine.drawImage((int) (engine.getWidth() / 2) + i * 20, (int) (engine.getHeight() / 1.4), 20, 20, "Back");
            }
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
                    int key = this.matriz[i][j].keyCell();
                    //Fallo
                    if (key == 1) {
                        //Restamos una vida
                        lives--;
                        //5 Si lo seleccionas
                        this.matriz[i][j].key = 5;
                        //Acierto
                    } else if (key == 2) {
                        remainingCells--;
                        //5 Si lo seleccionas
                        this.matriz[i][j].key = 5;
                        if (win()) {
                            won = true;
                        }
                    }
                    //Y playeamos el sonido
                    engine.getAudio().playSound("effect", 1);
                }
            }
        }

        //BOTONES
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

    //Metodos de lectura y guardado
    public void loadFromFile(String file) {
        try {
            InputStream inputStream = this.engine.getContext().getAssets().open(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while (bufferedReader.ready()) {
                    receiveString += bufferedReader.readLine();

                }
                String[] fileRead;
                fileRead = receiveString.split(" ");
                rows_ = Integer.parseInt(fileRead[0]);
                cols_ = Integer.parseInt(fileRead[1]);

                for (int i = 0; i < rows_; i++) {

                    for (int j = 2; j < cols_ + 2; j++) {
                        System.out.print(fileRead[rows_ * i + j]);

                        int aux = Integer.parseInt(fileRead[rows_ * i + j]);

                        //Si es 0 NO SE RELLENA
                        if (aux == 0) {

                            this.matriz[j - 2][i].setSolution(false);
                        }
                        //Si es 1 se rellena
                        else if (aux == 1) {
                            //Lo añadimos a la lista de celdas que tiene que acertar el jugador
                            remainingCells++;

                            this.matriz[j - 2][i].setSolution(true);

                        }
                        //Si esta mal seleccionada y esta roja...
                        else if (aux == 2) {
                            this.matriz[j - 2][i].setSolution(true);

                            //Con esto seteamos que no es la solucion pero está mal seleccionado
                            this.matriz[j - 2][i].key = 1;
                        }
                    }
                    System.out.println();
                }

                int contador = 0;
                int numAux = 0;
                //Ahora leemos las filas y columnas para colocar los indicadores laterales

                //LECTURA INDICACION VERTICAL IZQUIERDA
                for (int i = 0; i < rows_; i++) {
                    numAux = Integer.parseInt(fileRead[rows_ * cols_ + 2 + contador]);
                    for (int j = 0; j < numAux; j++) {
                        xPositionsTopToBottom[i].add(Integer.parseInt(fileRead[(rows_ * cols_ + 2) + contador + j + 1]));
                    }
                    contador += numAux + 1;
                }


                inputStream.close();
            }
        } catch (
                FileNotFoundException e) {
            Log.e("Error", "File not found: " + e.toString());
        } catch (
                IOException e) {
            Log.e("Reading Error", "Can not read file: " + e.toString());
        }

    }

    public void saveToFile(String fileName) {

    }

    //Comprueba si has ganado o no
    private boolean win() {
        return remainingCells == 0;
    }
}

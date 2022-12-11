package com.example.practica1;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class HistoryModeGameScene implements Scene, Serializable {
    private EngineApp engine;

    //Tenemos una matriz donde guardaremos las casillas seleccionadas
    private CellHistoryMode[][] matriz;

    int rows_, cols_;

    int lives;

    //Para mostrar en pantallas la info de las celdas
    int remainingCells, maxCellsSolution;

    int mode;

    private AtomicReference<Integer> coins;
    private AtomicReference<Integer> progress;
    private Integer currentLevelNumber;
    private Integer coinSize;
    boolean showNewCoins;

    //Tamaño proporcional de las celdas adaptado a la pantalla
    float tamProporcional;

    //Variables auxiliares para posicionar correctamente a las celdas en funcion del tamaño de la pantalla
    double xPos;
    double yPos;
    int posYTextAuxTopToBottom;
    int posXTextAuxLeftToRight;

    private Button rewardButton;

    //Patron de colores
    int colorfulPattern[];
    int actualColorPattern;

    private InputButton escapeInputButton;
    private InputButton winBackInputButton;
    private InputButton getLifeInputButton;

    private InputButton[] colorsInputButtons;

    //Tenemos un array de listas de Ints, que son los que muestran las "posiciones" de las casillas azules. Uno el horizontal y otro el vertical
    private ArrayList<Integer>[] xPositionsTopToBottom;
    private ArrayList<Integer>[] xPositionsLeftToRight;

    //Esto es para coger los numeros de antes y mostrarlos en pantalla como un string
    private String[] xNumberTopToBottom;
    private ArrayList<String>[] xNumberLeftToRight;

    //Archivo de guardado
    String fileName;
    String fileToOpen;

    //Para los rectangulos que recubren las celdas y son meramente esteticos
    int widthAestheticCellX, heightAestheticCellX, widthAestheticCellY, heightAestheticCellY;

    private static final int timeCheckButton = 5;
    double timer;
    boolean won;
    boolean showAnswers;

    public HistoryModeGameScene(EngineApp engine, int rows, int cols, String file, int modeAux, AtomicReference<Integer> coinsAux, AtomicReference<Integer> progressAux, Integer currentLevelNumberAux, Button rewardButtonAux) {

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

        coins = coinsAux;
        progress = progressAux;
        this.currentLevelNumber = currentLevelNumberAux;

        this.rewardButton = rewardButtonAux;
//        this.rewardButton.setVisibility(View.VISIBLE);

        //AAAAAAAAAAAAAAAAAAA DEBUG
//        coins.set(coins.get() + 1);

        //Patron de colores
        colorfulPattern = new int[4];
        actualColorPattern = modeAux - 1;
        colorsInputButtons = new InputButton[4];

        rows_ = rows;
        cols_ = cols;

        mode = modeAux;

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

        this.fileToOpen = file;

        init();
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
        coinSize = engine.getGraphics().getWidth() / 10;

        //Cargamos la info de la matriz
        loadFromFile(this.fileToOpen);

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
        this.escapeInputButton = new InputButton(10 + engine.getGraphics().getWidth()/44, 30, engine.getGraphics().getWidth()/10, engine.getGraphics().getHeight()/15);
        this.winBackInputButton = new InputButton((double) this.engine.getGraphics().getWidth() * 0.44444444, (double) this.engine.getGraphics().getHeight() / 1.1,
                (double) this.engine.getGraphics().getWidth() / 10, (double) this.engine.getGraphics().getHeight() / 15);
        this.getLifeInputButton = new InputButton(0, this.engine.getGraphics().getHeight() - this.engine.getGraphics().getHeight()/10,
                (double) this.engine.getGraphics().getWidth() / 6, (double) this.engine.getGraphics().getHeight() / 10);

        for (int i = 0; i < colorsInputButtons.length; i++) {
            this.colorsInputButtons[i] = new InputButton((double) this.engine.getGraphics().getWidth() / 2.5 + 200 * i, (double) this.engine.getGraphics().getHeight()-  this.engine.getGraphics().getHeight() * 0.10,
                    (double) this.engine.getGraphics().getWidth() * 0.1666666, (double) this.engine.getGraphics().getHeight() * 0.10);
        }
        //CYA
        colorfulPattern[0] = 0xA0FFFFFF;
        colorfulPattern[1] = 0xA000FFFF;
        colorfulPattern[2] = 0xA0FF00FF;
        colorfulPattern[3] = 0xA0FFFF00;

        //Tamaño de las cuadriculas que recubren el nonograma
        widthAestheticCellX = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getX()) + (int) ((double) this.engine.getGraphics().getWidth() * 0.0625);
        heightAestheticCellX = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getY() - this.matriz[0][0].getPos().getY() + (int) ((double) this.engine.getGraphics().getHeight() * 0.0601851));

        widthAestheticCellY = (int) ((this.matriz[cols_ - 1][0].getPos().getX()) - this.matriz[0][0].getPos().getX()) + (int) ((double) this.engine.getGraphics().getWidth() * 0.0902777);
        heightAestheticCellY = (int) (this.matriz[cols_ - 1][rows_ - 1].getPos().getY() - (int) ((double) this.engine.getGraphics().getHeight() * 0.111111111));
    }

    @Override
    public void loadResources(EngineApp engineAux) {

    }

    @Override
    public void update(double deltaTime) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].update(deltaTime);
            }
        }

        if (engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType());
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
        this.engine.getGraphics().drawImage((int) (auxCuadradoInicio.getX() - tamProporcional*0.1), (int) (auxCuadradoInicio.getY() -tamProporcional*0.1),
                (int) (auxCuadradoFinal.getX() -auxCuadradoInicio.getX()+ tamProporcional + tamProporcional*0.2)
                , (int) (auxCuadradoFinal.getY()-auxCuadradoInicio.getY()+ tamProporcional+tamProporcional*0.2), "Board");

        //Si ya he ganado...
        if (won) {
            //Solo renderizo las azules
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    this.matriz[i][j].solutionRender(engine);
                }
            }

            //Mostramos las monedas obtenidas
            if (showNewCoins) {
                this.engine.getGraphics().drawText("+10", (int) (this.engine.getGraphics().getWidth() / 2), (int) (engine.getGraphics().getHeight() / 12 + coinSize/1.7), "Black", "Cooper", 0);
                this.engine.getGraphics().drawImage((int) (engine.getGraphics().getWidth() / 1.8), (int) (engine.getGraphics().getHeight() / 12), coinSize, coinSize, "Coin");

            }

            //Mensaje de enhorabuena
            this.engine.getGraphics().drawText("¡ENHORABUENA!", (int) ((double) this.engine.getGraphics().getWidth() * 0.5), (int) ((double) this.engine.getGraphics().getHeight() / 15), "Black", "Cooper", 0);

            //BackButton
            this.engine.getGraphics().drawImage((int) (winBackInputButton.getPos().getX()), (int) (winBackInputButton.getPos().getY()), (int) (winBackInputButton.getSize().getX()), (int) (winBackInputButton.getSize().getY()), "Back");
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
                //Con el margen de 1 celda no tendremos problema con las otras resoluciones
                engine.getGraphics().drawText(xNumberTopToBottom[i], (int) (auxCuadradoInicio.getX() - tamProporcional/2), posYTextAuxTopToBottom + (int) (tamProporcional * 1.1 * i), "Black", "CalibriSmall", 1);
            }
            for (int i = 0; i < xNumberLeftToRight.length; i++) {
                for (int j = xNumberLeftToRight[i].size()-1; j >= 0; j--) {
                    engine.getGraphics().drawText(xNumberLeftToRight[i].get(j), posXTextAuxLeftToRight + (int) (tamProporcional * 1.1 * i), (int) (auxCuadradoInicio.getY() - tamProporcional/1.5 + (int) (tamProporcional/2.7f * j)), "Black", "CalibriSmall", 0);
                }
            }

            //BOTONES
            //TODO AAA NO SE HACER LOS PUTOS COLORES
            this.engine.getGraphics().drawRectangle((int) ((double) colorsInputButtons[actualColorPattern].getPos().getX()), (int) ((double) colorsInputButtons[actualColorPattern].getPos().getY()), (int) ((double) colorsInputButtons[actualColorPattern].getSize().getX()), (int) ((double) colorsInputButtons[actualColorPattern].getSize().getY()), true, (int) (colorfulPattern[actualColorPattern]+0xAF000000));

            this.engine.getGraphics().drawImage((int) ((double) escapeInputButton.getPos().getX()), (int) ((double) escapeInputButton.getPos().getY()), (int) ((double) escapeInputButton.getSize().getX()), (int) ((double) escapeInputButton.getSize().getY()), "Back");
            for (int i = 0; i < colorsInputButtons.length; i++) {
                this.engine.getGraphics().drawImage((int) ((double) colorsInputButtons[i].getPos().getX()), (int) ((double) colorsInputButtons[i].getPos().getY()), (int) ((double) colorsInputButtons[i].getSize().getX()), (int) ((double) colorsInputButtons[i].getSize().getY()), "GiveUp");
            }

            //AD PARA CONSEGUIR VIDAS
            this.engine.getGraphics().drawImage((int) ((double) getLifeInputButton.getPos().getX()), (int) ((double) getLifeInputButton.getPos().getY()), (int) ((double) getLifeInputButton.getSize().getX()), (int) ((double) getLifeInputButton.getSize().getY()), "GiveUp");

            //ESTO ESTA SIN TESTEAR.
            //MONEDAS
            this.engine.getGraphics().drawText(Integer.toString(coins.get()), engine.getGraphics().getWidth() - coinSize-10, (int)(engine.getGraphics().getHeight()/72 + coinSize/1.7f), "Black", "CooperBold", 1);
            this.engine.getGraphics().drawImage(engine.getGraphics().getWidth() - coinSize - 10, (int) engine.getGraphics().getHeight() / 72, coinSize, coinSize, "Coin");

            //CORAZONES
            for (int i = lives; i > 0; i--) {
                this.engine.getGraphics().drawImage((int) (engine.getGraphics().getWidth() / 2) + i * 100, (int) (engine.getGraphics().getHeight() / 1.4), 100, 100, "Heart");
            }

            if (lives <= 0) {
                //Mensaje de enhorabuena
                this.engine.getGraphics().drawText("¡HAS PERDIDO!", (int) ((double) this.engine.getGraphics().getWidth() * 0.5), (int) ((double) this.engine.getGraphics().getHeight() / 15), "Black", "Cooper", 0);

            }
        }
    }

    @Override
    public void handleInput(EventHandler.EventType type) {
        //Solo podra interactuar con el tablero si tiene vidas
        if (lives > 0) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    if (inputReceived(this.matriz[i][j].getPos(), this.matriz[i][j].getSize())) {

                        //Si es un long touch...
                        if (type == EventHandler.EventType.LONG_TOUCH) {
                            //Intentamos cruzarlo
                            this.matriz[i][j].setCrossed();
                            //Y playeamos el sonido
                            engine.getAudio().playSound("effect", 1);
                        } else {
                            //Fallo
                            if (this.matriz[i][j].getCellType() == CellBase.cellType.EMPTY) {
                                if (!this.matriz[i][j].solution) {
                                    //Restamos una vida
                                    lives--;
                                    //Y playeamos el sonido
                                    engine.getAudio().playSound("effect", 1);
                                }
                                //Acierto
                                else {
                                    remainingCells--;
                                    if (win()) {
                                        won = true;
                                        //Si no te has pasado el nivel nunca...
                                        if (currentLevelNumber == this.progress.get()) {
                                            //Sumamos las monedas del nivel
                                            coins.set(coins.get() + 10);
                                            showNewCoins = true;
                                        }
                                    }
                                    //Y playeamos el sonido
                                    engine.getAudio().playSound("effect", 1);
                                }
                            }
                            //Aqui se guarda si te has equivocado...
                            this.matriz[i][j].handleInput(engine);
                        }
                    }
                }
            }
        }

        //BOTONES
        //Si te rindes vuelves a la seleccion de nivel
        if (!won && inputReceived(this.escapeInputButton.getPos(), this.escapeInputButton.getSize())) {
            if (lives <= 0) {
                saveToFile(true);
            } else {
                saveToFile(false);
            }
            this.engine.getSceneMngr().popScene();
            this.engine.setColorBackground(0xFFFFFFFF);
        }
        //Solo funciona si has ganado
        if (won && inputReceived(this.winBackInputButton.getPos(), this.winBackInputButton.getSize())) {
            if (currentLevelNumber == this.progress.get()) {
                this.progress.set(this.progress.get() + 1);
            }
            saveToFile(true);
            this.engine.getSceneMngr().popScene();
            this.engine.setColorBackground(0xFFFFFFFF);
        }
        for (int i = 0; i < colorsInputButtons.length; i++) {
            if (inputReceived(this.colorsInputButtons[i].getPos(), this.colorsInputButtons[i].getSize())) {
                actualColorPattern = i;
                this.engine.setColorBackground(colorfulPattern[i]);
                for (int h = 0; h < matriz.length; h++) {
                    for (int j = 0; j < matriz[h].length; j++) {
                        //TODO AAA NO SE HACER COSAS EN HEXADECIMAL
                        this.matriz[h][j].setPalleteColor(this.colorfulPattern[i] + 0xAF808080);
                    }
                }
            }
        }
        //Si necesitas o quieres alguna vida...
        if (lives < 3 && inputReceived(this.getLifeInputButton.getPos(), this.getLifeInputButton.getSize())) {
            //TODO AAA o compras vida por X dionero o miras anuncio
        }
    }

    //Se llama cuando la escena posterior se elimina y se vuelve aqui, por si hay que actualizar algo
    @Override
    public void onResume() {

    }

    //Metodos de lectura y guardado
    public void loadFromFile(String file) {
        try {
            //Carga de archivo
            String receiveString = "";
            try {//Comprobar si existe en el almacenamiento interno
                FileInputStream fis = this.engine.getContext().openFileInput(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while (bufferedReader.ready()) {
                    receiveString += bufferedReader.readLine();
                }
                inputStreamReader.close();
            } catch (FileNotFoundException e) { //Si no existe, crea un nuevo archivo en almacenamiento interno como copia desde assets
                e.printStackTrace();
            }

            String fileCarpet = "";
            switch (mode) {
                case 1:
                    fileCarpet = "alfabeto/" + file;
                    break;
                case 2:
                    fileCarpet = "fiesta/" + file;
                    break;
                case 3:
                    fileCarpet = "animales/" + file;
                    break;
                case 4:
                    fileCarpet = "geometria/" + file;
                    break;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(this.engine.getContext().getAssets().open("files/" + fileCarpet));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while (bufferedReader.ready()) {
                receiveString += bufferedReader.readLine();
            }

            inputStreamReader.close();
            //Copia del fichero
            FileOutputStream fos = this.engine.getContext().openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(receiveString.getBytes());
            fos.close();
            //Carga el nivel desde el string "RAW" de lectura
            String[] fileRead;
            fileRead = receiveString.split(" ");
            rows_ = Integer.parseInt(fileRead[0]);
            cols_ = Integer.parseInt(fileRead[1]);
            lives = Integer.parseInt(fileRead[2]);

            int numCeldas;
            int tamTextoAux;

            //AAA TODO Tamaño de la celda
            //Nos quedamos con el mayor numero para hacer el reescalado
            if (rows_ > cols_) {
                //Si hay 10 filas como maximo puede haber 5 numeros + 5 espacios = rows
                tamTextoAux = Math.round(rows_ / 3.0f);
                //Hacemos una proporcion aproximada. 3 unidades (letras/espacios) es 1 casilla
                numCeldas = rows_ + tamTextoAux;
                //Y con eso sacamos el tamaño promedio de la celda
                tamProporcional = this.engine.getGraphics().getWidth() / (numCeldas + 1);
            } else {
                //Si hay 10 columnas como maximo puede haber 5 numeros + 5 espacios = cols
                tamTextoAux = Math.round(cols_ / 3.0f);
                //Hacemos una proporcion aproximada. 3 unidades (letras/espacios) es 1 casilla
                numCeldas = cols_ + tamTextoAux;
                //Y con eso sacamos el tamaño promedio de la celda
                tamProporcional = this.engine.getGraphics().getWidth() / (numCeldas + 1); //+1 Para que haya margen y quede bonito

            }


            //Iniciamos la matriz segun el fichero
            for (int i = 0; i < rows_; i++) {
                for (int j = 3; j < cols_ + 3; j++) {
                    System.out.print(fileRead[rows_ * i + j]);

                    int aux = Integer.parseInt(fileRead[rows_ * i + j]);

                    //Con la relacion del juego el largo siempre va a ser mayor que el ancho
//                    double yPos = tamTextoAux * tamProporcional + ((tamProporcional * 1.1) * i);

                    //Si es mas ancho que largo...
                    if (rows_ >= cols_) {
                        //Lo ajustamos al centro de la pantalla de largo
                        yPos = this.engine.getGraphics().getHeight() / 2 - ((rows_ / 2.0f) * tamProporcional) + ((tamProporcional * 1.1) * i);

                        xPos = tamTextoAux * tamProporcional + ((tamProporcional * 1.1) * (j - 3));

                    }
                    //Si es mas largo que ancho
                    else {
                        yPos = tamTextoAux * tamProporcional + ((tamProporcional * 1.1) * i);

                        //Ajustamos el ancho al centro de la pantalla
                        xPos = this.engine.getGraphics().getWidth() / 2 - ((cols_ / 2.0f) * tamProporcional) + ((tamProporcional * 1.1) * (j - 3));

                    }


                    //Si es 0 Esta EMPTY pero no es true
                    if (aux == 0) {
                        this.matriz[j - 3][i] = new CellHistoryMode((int) xPos,
                                (int) yPos, (int) tamProporcional, (int) tamProporcional, CellBase.cellType.EMPTY, false);
                    }
                    //Si es 1 Esta Empty pero es correcto
                    else {

                        if (aux == 1) {
                            //Lo añadimos a la lista de celdas que tiene que acertar el jugador
                            remainingCells++;

                            this.matriz[j - 3][i] = new CellHistoryMode((int) xPos,
                                    (int) yPos, (int) tamProporcional, (int) tamProporcional, CellBase.cellType.EMPTY, true);

                        }
                        //Si esta mal seleccionada y esta roja...
                        else if (aux == 2) {
                            this.matriz[j - 3][i] = new CellHistoryMode((int) xPos,
                                    (int) yPos, (int) tamProporcional, (int) tamProporcional, CellBase.cellType.WRONG, false);

                        }
                        //Si esta bien seleccionada y esta azul
                        else if (aux == 3) {
                            this.matriz[j - 3][i] = new CellHistoryMode((int) xPos,
                                    (int) yPos, (int) tamProporcional, (int) tamProporcional, CellBase.cellType.SELECTED, true);
                        }
                    }
                }
            }

            //Para las posiciones del texto indicativo
            posYTextAuxTopToBottom = (int) (matriz[0][0].getPos().getY() + (tamProporcional / 2));
            posXTextAuxLeftToRight = (int) (matriz[0][0].getPos().getX() + (tamProporcional / 2));

            int contador = 0;
            int numAux = 0;
            //Ahora leemos las filas y columnas para colocar los indicadores laterales

            //LECTURA INDICACION VERTICAL IZQUIERDA
            for (int i = 0; i < rows_; i++) {
                numAux = Integer.parseInt(fileRead[rows_ * cols_ + 3 + contador]);
                for (int j = 0; j < numAux; j++) {
                    xPositionsTopToBottom[i].add(Integer.parseInt(fileRead[(rows_ * cols_ + 3) + contador + j + 1]));
                }
                contador += numAux + 1;
            }

            //LECTURA INDICACION HORIZONTAL SUPERIOR
            for (int i = 0; i < cols_; i++) {
                numAux = Integer.parseInt(fileRead[rows_ * cols_ + 3 + contador]);
                for (int j = 0; j < numAux; j++) {
                    xPositionsLeftToRight[i].add(Integer.parseInt(fileRead[(rows_ * cols_ + 3) + contador + j + 1]));
                }
                contador += numAux + 1;
            }
            fileName = file;
        } catch (
                FileNotFoundException e) {
            Log.e("Error", "File not found: " + e.toString());
        } catch (
                IOException e) {
            Log.e("Reading Error", "Can not read file: " + e.toString());
        }

    }

    public void saveToFile(boolean reset) {
        try {
            FileOutputStream fos = this.engine.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            //Guardado de la partida
            String auxiliar = rows_ + " " + cols_ + " ";
            if (reset) {
                auxiliar += 3 + " \n";
            } else {
                auxiliar += lives + " \n";
            }
            fos.write(auxiliar.getBytes(StandardCharsets.UTF_8));
            auxiliar = "";
            for (int i = 0; i < rows_; i++) {
                for (int j = 0; j < cols_; j++) {
                    if (reset) {
                        if (matriz[j][i].solution) {
                            auxiliar += "1 ";
                        } else {
                            auxiliar += "0 ";
                        }
                    } else {
                        if (matriz[j][i].solution) {
                            if (matriz[j][i].getCellType() == CellBase.cellType.EMPTY) {
                                auxiliar += "1 ";
                            } else {
                                auxiliar += "3 ";
                            }
                        } else {
                            if (matriz[j][i].getCellType() == CellBase.cellType.EMPTY) {
                                auxiliar += "0 ";
                            } else {
                                auxiliar += "2 ";
                            }
                        }
                    }
                }
                auxiliar += "\n";
            }
            fos.write(auxiliar.getBytes(StandardCharsets.UTF_8));

            for (int i = 0; i < xNumberTopToBottom.length; i++) {
                auxiliar = xNumberTopToBottom[i].length() / 2 + " " + xNumberTopToBottom[i] + "\n";
                fos.write(auxiliar.getBytes(StandardCharsets.UTF_8));
            }

            for (int i = 0; i < xNumberLeftToRight.length; i++) {
                auxiliar = xNumberLeftToRight[i].size() + " ";
                for (int j = 0; j < xNumberLeftToRight[i].size(); j++) {
                    auxiliar += xNumberLeftToRight[i].get(j) + " ";
                }
                auxiliar += "\n";
                fos.write(auxiliar.getBytes(StandardCharsets.UTF_8));
            }

            fos.close();

        } catch (FileNotFoundException e) {
            Log.e("Error", "File not found: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Comprueba si has ganado o no
    private boolean win() {
        return remainingCells == 0;
    }
}

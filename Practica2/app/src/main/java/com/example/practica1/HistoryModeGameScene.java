package com.example.practica1;

import android.content.Context;
import android.util.Log;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.RenderAndroid;
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

public class HistoryModeGameScene implements Scene {
    private EngineApp engine;

    //A partir de ahora tenemos una escala de 1000x1000, asi que no usamos mas engine.getWidth ni engine.getHeight
    int scaleWidth;
    int scaleHeight;
    //Tenemos una matriz donde guardaremos las casillas seleccionadas
    private CellHistoryMode[][] matriz;

    int rows_, cols_;
    AtomicReference<Integer> lives;

    //Para mostrar en pantallas la info de las celdas
    int remainingCells, maxCellsSolution;

    int mode;

    private AtomicReference<Integer> coins;
    private AtomicReference<Integer> progress;
    private Integer currentLevelNumber;
    private Integer coinSize;
    boolean showNewCoins;

    //Tamaño proporcional de las celdas adaptado a la pantalla
    float tamProporcionalAncho;
    float tamProporcionalAlto;
    int tamTexto;

    //Variables auxiliares para posicionar correctamente a las celdas en funcion del tamaño de la pantalla
    double xPos;
    double yPos;
    int posYTextAuxTopToBottom;
    int posXTextAuxLeftToRight;

    //Patron de colores
    int colorfulPattern[];
    int colorfulPatternCells[];

    int actualColorPattern;

    private InputButton escapeInputButton;
    private InputButton winBackInputButton;
    private InputButton getLifeInputButton;
    private InputButton shareButton;

    //Con esta variable controlamos las paletas que hayas comprado
    private AtomicReference<Integer>[] palettes;

    private InputButton[] colorsInputButtons;
    private String[] colorsButtonsName = {"WhitePalette", "BluePalette", "PinkPalette", "YellowPalette"};

    //Tenemos un array de listas de Ints, que son los que muestran las "posiciones" de las casillas azules. Uno el horizontal y otro el vertical
    private ArrayList<Integer>[] xPositionsTopToBottom;
    private ArrayList<Integer>[] xPositionsLeftToRight;

    //Esto es para coger los numeros de antes y mostrarlos en pantalla como un string
    private String[] xNumberTopToBottom;
    private ArrayList<String>[] xNumberLeftToRight;

    //Archivo de guardado
    String fileName;
    String fileToOpen;

    private static final int timeCheckButton = 5;
    double timer;
    boolean won;
    boolean showAnswers;

    public HistoryModeGameScene(EngineApp engine, String file, int modeAux, AtomicReference<Integer> coinsAux, AtomicReference<Integer> progressAux, Integer currentLevelNumberAux, AtomicReference<Integer>[] palettesAux) {

        //Asociamos el engine correspondiente
        this.engine = engine;

        scaleHeight = 1000;
        scaleWidth = 1000;

        lives = new AtomicReference<Integer>();

        //Seteamos valores iniciales
        remainingCells = 0;
        showAnswers = false;
        won = false;
        timer = 0;
        lives.set(3);

        coins = coinsAux;
        progress = progressAux;
        this.palettes = palettesAux;

        this.currentLevelNumber = currentLevelNumberAux;

//        this.rewardButton.setVisibility(View.VISIBLE);

        //Patron de colores
        colorfulPattern = new int[4];
        colorfulPatternCells = new int[4];
        actualColorPattern = 0;
        colorsInputButtons = new InputButton[4];


        mode = modeAux;

        this.fileToOpen = file;

        //Leemos el archivo y seteamos todos los datos
        init();
    }

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size) {
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX() * scaleWidth / engine.getGraphics().getWidth() >= pos.getX() && coords.getX() * scaleWidth / engine.getGraphics().getWidth() <= pos.getX() + size.getX() &&
                coords.getY() * scaleHeight / engine.getGraphics().getHeight() >= pos.getY() && coords.getY() * scaleHeight / engine.getGraphics().getHeight() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {
        coinSize = scaleWidth / 10;

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
        this.escapeInputButton = new InputButton(10, 10, scaleWidth / 10, scaleHeight / 15);
        this.winBackInputButton = new InputButton((double) scaleWidth / 2, (double) scaleHeight / 1.1,
                (double) scaleWidth / 10, (double) scaleHeight / 15);
        this.getLifeInputButton = new InputButton(0, scaleHeight - scaleHeight / 10,
                (double) scaleWidth / 7, (double) scaleHeight / 10);
        this.shareButton = new InputButton((double) scaleWidth / 4, (double) scaleHeight * 0.9,
                (double) scaleWidth / 8, (double) scaleHeight / 10);

        for (int i = 0; i < colorsInputButtons.length; i++) {
            this.colorsInputButtons[i] = new InputButton((scaleWidth / 7) + (scaleWidth / 7) * i, (double) scaleHeight - scaleHeight * 0.10,
                    (double) scaleWidth / 7, (double) scaleHeight * 0.10);
        }
        //ACYA
        colorfulPattern[0] = 0xA0FFFFFF;
        colorfulPattern[1] = 0xA000FFFF;
        colorfulPattern[2] = 0xA0FF00FF;
        colorfulPattern[3] = 0xA0FFFF00;

        //RGB
        colorfulPatternCells[0] = 0xFFFFFFFF;
        colorfulPatternCells[1] = 0xFF0000FF;
        colorfulPatternCells[2] = 0xFFFF00A0;
        colorfulPatternCells[3] = 0xFF00FF00;
    }

    @Override
    public void loadResources(EngineApp engineAux) {

    }

    @Override
    public void update(double deltaTime, AdManager adManager) {
        for (int i = 0; i < rows_; i++) {
            for (int j = 0; j < cols_; j++) {
                this.matriz[j][i].update(deltaTime);
            }
        }

        if (engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType(), adManager);
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }

        //Timer del boton de comprobar
        if (timer > 0) {
            timer -= deltaTime;
        }
    }

    @Override
    public void render(RenderAndroid renderEngine) {
        Vector2D auxCuadradoFinal = this.matriz[cols_ - 1][rows_ - 1].getPos();
        Vector2D auxCuadradoInicio = this.matriz[0][0].getPos();

        //El cuadrado se mantiene aunque ganes porque es muy bonito
        renderEngine.drawImage((int) (auxCuadradoInicio.getX() - tamProporcionalAncho * 0.1), (int) (auxCuadradoInicio.getY() - tamProporcionalAlto * 0.1),
                (int) (auxCuadradoFinal.getX() - auxCuadradoInicio.getX() + tamProporcionalAncho + tamProporcionalAncho * 0.2)
                , (int) (auxCuadradoFinal.getY() - auxCuadradoInicio.getY() + tamProporcionalAlto + tamProporcionalAlto * 0.2), "Board");

        //Si ya he ganado...
        if (won) {
            //Solo renderizo las azules
            for (int i = 0; i < rows_; i++) {
                for (int j = 0; j < cols_; j++) {
                    this.matriz[j][i].solutionRender(renderEngine);
                }
            }

            //Mostramos las monedas obtenidas
            if (showNewCoins) {
                renderEngine.drawText("+10", (int) (scaleWidth / 2), (int) (scaleHeight / 12 + coinSize / 2.5f), "Black", "Cooper", 0,scaleWidth/27);
                renderEngine.drawImage((int) (scaleWidth / 1.8), (int) (scaleHeight / 12), coinSize, coinSize / 2, "Coin");
            }

            //Mensaje de enhorabuena
            renderEngine.drawText("¡ENHORABUENA!", (int) ((double) scaleWidth * 0.5), (int) ((double) scaleHeight / 15), "Black", "Cooper", 0,scaleWidth/27);

            //BackButton
            renderEngine.drawImage((int) (winBackInputButton.getPos().getX()), (int) (winBackInputButton.getPos().getY()), (int) (winBackInputButton.getSize().getX()), (int) (winBackInputButton.getSize().getY()), "Back");
            renderEngine.drawImage((int) (shareButton.getPos().getX()), (int) (shareButton.getPos().getY()), (int) (shareButton.getSize().getX()), (int) (shareButton.getSize().getY()), "TwitterIcon");

            //Si sigo jugando...
        } else {
            //Si tienes pulsado el boton de comprobar...
            //Renderiza rojo si esta mal
            for (int i = 0; i < rows_; i++) {
                for (int j = 0; j < cols_; j++) {
                    this.matriz[j][i].render(renderEngine);
                }
            }

            //NUMEROS LATERALES
            for (int i = 0; i < xNumberTopToBottom.length; i++) {
                //Con el margen de 1 celda no tendremos problema con las otras resoluciones
                renderEngine.drawText(xNumberTopToBottom[i], (int) (auxCuadradoInicio.getX() - (tamProporcionalAlto * 0.1)), posYTextAuxTopToBottom + (int) (tamProporcionalAlto * 1.1 * i), "Black", "Calibri", 1,tamTexto);
            }
            for (int i = 0; i < xNumberLeftToRight.length; i++) {
                for (int j = xNumberLeftToRight[i].size() - 1; j >= 0; j--) {
                    renderEngine.drawText(xNumberLeftToRight[i].get(j), posXTextAuxLeftToRight + (int) (tamProporcionalAncho * 1.1 * i), (int) (auxCuadradoInicio.getY() - (xNumberLeftToRight[i].size() * tamProporcionalAlto * 0.7 / (rows_ / 2)) + (int) ((tamProporcionalAlto / rows_ * 1.3 * j))), "Black", "Calibri", 0,tamTexto);
                }
            }

            //BOTONES
            renderEngine.drawRectangle((int) ((double) colorsInputButtons[actualColorPattern].getPos().getX()), (int) ((double) colorsInputButtons[actualColorPattern].getPos().getY()), (int) ((double) colorsInputButtons[actualColorPattern].getSize().getX()), (int) ((double) colorsInputButtons[actualColorPattern].getSize().getY()), true, (int) (colorfulPattern[actualColorPattern] - 0xAF000000));

            //BackButton
            renderEngine.drawImage((int) ((double) escapeInputButton.getPos().getX()), (int) ((double) escapeInputButton.getPos().getY()), (int) ((double) escapeInputButton.getSize().getX()), (int) ((double) escapeInputButton.getSize().getY()), "Back");

            //Paleta de colores
            for (int i = 0; i < colorsInputButtons.length; i++) {
                renderEngine.drawImage((int) ((double) colorsInputButtons[i].getPos().getX()), (int) ((double) colorsInputButtons[i].getPos().getY()), (int) ((double) colorsInputButtons[i].getSize().getX()), (int) ((double) colorsInputButtons[i].getSize().getY()), colorsButtonsName[i]);
                //Si no has desbloqueado todavia la paleta...
                if (palettes[i].get() != 1)
                    //Ponemos una imagen de bloqueo por encima con el numero de monedas
                    renderEngine.drawImage((int) ((double) colorsInputButtons[i].getPos().getX()), (int) ((double) colorsInputButtons[i].getPos().getY()), (int) ((double) colorsInputButtons[i].getSize().getX()), (int) ((double) colorsInputButtons[i].getSize().getY()), "CoinsCost");
            }

            //AD PARA CONSEGUIR VIDAS
            renderEngine.drawImage((int) ((double) getLifeInputButton.getPos().getX()), (int) ((double) getLifeInputButton.getPos().getY()), (int) ((double) getLifeInputButton.getSize().getX()), (int) ((double) getLifeInputButton.getSize().getY()), "HeartAD");

            //MONEDAS
            renderEngine.drawText(Integer.toString(coins.get()), scaleWidth - coinSize - scaleWidth / 100, (int) (scaleHeight / 72 + coinSize / 2.5f), "Black", "CooperBold", 1,scaleWidth/20);
            renderEngine.drawImage(scaleWidth - coinSize - scaleWidth / 100, (int) scaleHeight / 72, coinSize, coinSize / 2, "Coin");

            //CORAZONES
            for (int i = lives.get(); i > 0; i--) {
                renderEngine.drawImage((int) (scaleWidth *5 / 7) + (scaleWidth * 2 / 21) * (i - 1), (int) (scaleHeight - scaleHeight / 15 - 10), scaleWidth * 2 / 21, scaleHeight / 15, "Heart");
            }

            if (lives.get() <= 0) {
                //Mensaje de enhorabuena
                renderEngine.drawText("¡HAS PERDIDO!", (int) ((double) scaleWidth / 2), (int) ((double) scaleHeight / 15), "Black", "Cooper", 0,scaleWidth/20);

            }
        }
    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager) {
        //Solo podra interactuar con el tablero si tiene vidas
        if (lives.get() > 0) {
            for (int i = 0; i < rows_; i++) {
                for (int j = 0; j < cols_; j++) {
                    if (inputReceived(this.matriz[j][i].getPos(), this.matriz[j][i].getSize())) {

                        //Si es un long touch...
                        if (type == EventHandler.EventType.LONG_TOUCH) {
                            //Intentamos cruzarlo
                            this.matriz[j][i].setCrossed();
                            //Y playeamos el sonido
                            engine.getAudio().playSound("effect", 1);
                        } else {
                            //Fallo
                            if (this.matriz[j][i].getCellType() == CellBase.cellType.EMPTY) {
                                if (!this.matriz[j][i].solution) {
                                    //Restamos una vida
                                    lives.set(lives.get() - 1);
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
                            this.matriz[j][i].handleInput(engine);
                        }
                    }
                }
            }
        }

        //BOTONES
        //Si te rindes vuelves a la seleccion de nivel
        if (!won && inputReceived(this.escapeInputButton.getPos(), this.escapeInputButton.getSize())) {
            if (lives.get() <= 0) {
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

        //Solo funciona si has ganado
        if (won && inputReceived(this.shareButton.getPos(), this.shareButton.getSize())) {
            switch (mode){
                case 1:
                    adManager.sendIntent( "https://twitter.com/intent/tweet", "oh wow soy la ostia pasandome el nivel " + currentLevelNumber + " de la categoria de alfabeto");
                    break;
                case 2:
                    adManager.sendIntent( "https://twitter.com/intent/tweet", "oh wow soy la ostia pasandome el nivel " + currentLevelNumber + " de la categoria de fiesta");
                    break;
                case 3:
                    adManager.sendIntent( "https://twitter.com/intent/tweet", "oh wow soy la ostia pasandome el nivel " + currentLevelNumber + " de la categoria de animales");
                    break;
                case 4:
                    adManager.sendIntent( "https://twitter.com/intent/tweet", "oh wow soy la ostia pasandome el nivel " + currentLevelNumber + " de la categoria de geometria");
                    break;
                default:
                    adManager.sendIntent( "https://twitter.com/intent/tweet", "oh wow soy la ostia jugando al nonograma que va a aprobar Antonio");
                    break;
            }

        }

        for (int i = 0; i < colorsInputButtons.length; i++) {
            if (!won && inputReceived(this.colorsInputButtons[i].getPos(), this.colorsInputButtons[i].getSize())) {
                //Si tienes la paleta bloqueada...
                if (palettes[i].get() == 0) {

                    //Si tienes monedas de sobra...
                    if (coins.get() >= 10) {
                        //Se resta
                        coins.set(coins.get() - 10);

                        //Lo cambiamos como desbloqueado en el archivo de datos
                        palettes[i].set(1);

                        //Y seteamos la nueva paleta
                        actualColorPattern = i;
                        this.engine.setColorBackground(colorfulPattern[i]);
                        for (int h = 0; h < rows_; h++) {
                            for (int j = 0; j < cols_; j++) {
                                //ARGB
                                this.matriz[j][h].setPalleteColor(this.colorfulPattern[i] + 0x00FF0000);
                            }
                        }
                    }
                } else {

                    actualColorPattern = i;
                    this.engine.setColorBackground(colorfulPattern[i]);
                    for (int h = 0; h < rows_; h++) {
                        for (int j = 0; j < cols_; j++) {
                            //TODO AAA NO SE HACER COSAS EN HEXADECIMAL
                            this.matriz[j][h].setPalleteColor(this.colorfulPatternCells[i]);
                        }
                    }
                }
            }
        }

        //Si necesitas o quieres alguna vida...
        if (lives.get() < 3 && inputReceived(this.getLifeInputButton.getPos(), this.getLifeInputButton.getSize())) {
            //Al mirar el anuncio se restaura un corazon
            adManager.showRewardedAd(lives, 1);
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
            lives.set(Integer.parseInt(fileRead[2]));

            //Creamos la matriz con el tamaño
            this.matriz = new CellHistoryMode[cols_][rows_];

            //Seteamos el resto de valores
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

            //Dividimos la pantalla en casillas.
            // Ancho: Cols +1(Para los numeros)
            // Alto: Rows +1( Sin contar la interfaz de por encima y por debajo)

            //+1 para los numeros laterales
            //0.1f por los margenes de espacio que hay entre cada celda
            //Y con eso sacamos el tamaño promedio de la celda
            tamProporcionalAncho = scaleWidth / ((cols_ + 1) + (0.1f*cols_));

            //Restamos la interfaz de las paletas y los botones de arriba
            tamProporcionalAlto = (scaleHeight - scaleHeight / 7 - scaleHeight / 20) / ((rows_ + 1) +(0.1f*rows_));

            tamTexto = (int) (tamProporcionalAncho / 4.5f);
            if (tamProporcionalAlto > tamProporcionalAncho)
                //Nos quedamos con el tamaño mas grande para que el texto se ajuste a la peor situacion
                tamTexto = (int) (tamProporcionalAlto / 4.5f);

            //Iniciamos la matriz segun el fichero
            for (int i = 0; i < rows_; i++) {
                //Contador de celdas azules por cada fila
                int contAux = 0;

                //Para evitar Filas vacias o llenas
                int numSolutionPerRows = 0;

                for (int j = 3; j < cols_ + 3; j++) {

                    //Cogemos el numero obtenido
                    int aux = Integer.parseInt(fileRead[rows_ * i + j]);

                    //Lo ajustamos al centro de la pantalla de largo
                    //Scale/15 para la interfaz de arriba + 1Celda para las letras
                    yPos = (scaleHeight / 15 + tamProporcionalAlto) + ((tamProporcionalAlto * 1.1) * i);

                    //+1Celda para las letras
                    xPos = tamProporcionalAncho + ((tamProporcionalAncho * 1.1) * (j - 3));

                    //Si es 0 Esta EMPTY pero no es true
                    if (aux == 0) {
                        this.matriz[j - 3][i] = new CellHistoryMode((int) xPos,
                                (int) yPos, (int) tamProporcionalAncho, (int) tamProporcionalAlto, CellBase.cellType.EMPTY, false);

                        //Si estabas sumando y luego te llego a 0...
                        if (contAux != 0) {
                            xPositionsTopToBottom[i].add(contAux);
                            contAux = 0;
                        }
                        //Para el valor de las columnas...
                        if (numAnterior[j - 3] == 0) {
                            //Reseteamos
                            contadorCols[j - 3] = 1;
                            numAnterior[j - 3] = -1;
                        }
                    }
                    //Si es 1 Esta Empty pero es correcto
                    else {
                        if (aux == 1) {
                            //Lo añadimos a la lista de celdas que tiene que acertar el jugador
                            remainingCells++;

                            this.matriz[j - 3][i] = new CellHistoryMode((int) xPos,
                                    (int) yPos, (int) tamProporcionalAncho, (int) tamProporcionalAlto, CellBase.cellType.EMPTY, true);

                            numSolutionPerRows++;

                            //Para averiguar los numeros laterales de las celdas
                            contAux++;

                            //Si nunca se han añadido en la celda de arriba suya...
                            if (numAnterior[j - 3] == -1) {

                                //Metemos el primero...
                                xPositionsLeftToRight[j - 3].add(1);

                                //Y por lo tanto ya tenemos uno añadido
                                numAnterior[j - 3] = 0;

                                //Con esto solo entra si se ha añadido algo alguna vez
                            } else if (numAnterior[j - 3] == 0) {

                                //Sumamos el valor +1 porque la columna continua
                                contadorCols[j - 3]++;

                                //Eliminamos el anterior
                                xPositionsLeftToRight[j - 3].remove(xPositionsLeftToRight[j - 3].size() - 1);

                                //Y metemos el nuevo
                                xPositionsLeftToRight[j - 3].add(xPositionsLeftToRight[j - 3].size(), contadorCols[j - 3]);
                            }
                        }
                        //Si esta mal seleccionada y esta roja...
                        else if (aux == 2) {
                            this.matriz[j - 3][i] = new CellHistoryMode((int) xPos,
                                    (int) yPos, (int) tamProporcionalAncho, (int) tamProporcionalAlto, CellBase.cellType.WRONG, false);

                            //Si estabas sumando y luego te llego a 0...
                            if (contAux != 0) {
                                xPositionsTopToBottom[i].add(contAux);
                                contAux = 0;
                            }
                            //Para el valor de las columnas...
                            if (numAnterior[j - 3] == 0) {
                                //Reseteamos
                                contadorCols[j - 3] = 1;
                                numAnterior[j - 3] = -1;
                            }
                        }
                        //Si esta bien seleccionada y esta azul
                        else if (aux == 3) {
                            this.matriz[j - 3][i] = new CellHistoryMode((int) xPos,
                                    (int) yPos, (int) tamProporcionalAncho, (int) tamProporcionalAlto, CellBase.cellType.SELECTED, true);
                            numSolutionPerRows++;

                            //Para averiguar los numeros laterales de las celdas
                            contAux++;

                            //Si nunca se han añadido en la celda de arriba suya...
                            if (numAnterior[j - 3] == -1) {

                                //Metemos el primero...
                                xPositionsLeftToRight[j - 3].add(1);

                                //Y por lo tanto ya tenemos uno añadido
                                numAnterior[j - 3] = 0;

                                //Con esto solo entra si se ha añadido algo alguna vez
                            } else if (numAnterior[j - 3] == 0) {

                                //Sumamos el valor +1 porque la columna continua
                                contadorCols[j - 3]++;

                                //Eliminamos el anterior
                                xPositionsLeftToRight[j - 3].remove(xPositionsLeftToRight[j - 3].size() - 1);

                                //Y metemos el nuevo
                                xPositionsLeftToRight[j - 3].add(xPositionsLeftToRight[j - 3].size(), contadorCols[j - 3]);
                            }
                        }
                    }

                }
                //Para meter en el lateral si el ultimo valor de la fila se ha seleccionado
                if (contAux != 0) {
                    xPositionsTopToBottom[i].add(contAux);
                }
            }

            //Para las posiciones del texto indicativo
            posYTextAuxTopToBottom = (int) (matriz[0][0].getPos().getY() + (tamProporcionalAlto / 2));
            posXTextAuxLeftToRight = (int) (matriz[0][0].getPos().getX() + (tamProporcionalAncho / 2));

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

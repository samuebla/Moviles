package com.example.practica1;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class QuickGameScene implements Scene, Serializable {

    private EngineApp engine;

    //A partir de ahora tenemos una escala de 1000x1000, asi que no usamos mas engine.getWidth ni engine.getHeight
    int scaleWidth;
    int scaleHeight;

    //Tenemos una matriz donde guardaremos las casillas seleccionadas
    private CellQuickGame[][] matriz;

    int rows_, cols_;
    int posYTextAuxTopToBottom;
    int posXTextAuxLeftToRight;

    //Tamaño proporcional de las celdas adaptado a la pantalla
    float tamProporcionalAncho;
    float tamProporcionalAlto;

    //Para mostrar en pantallas la info de las celdas
    int remainingCells, wrongCells, maxCellsSolution;

    private InputButton checkInputButton;
    private InputButton giveUpInputButton;
    private InputButton backInputButton;

    //Tenemos un array de listas de Ints, que son los que muestran las "posiciones" de las casillas azules. Uno el horizontal y otro el vertical
    private ArrayList<Integer>[] xPositionsTopToBottom;
    private ArrayList<Integer>[] xPositionsLeftToRight;

    //Esto es para coger los numeros de antes y mostrarlos en pantalla como un string
    private String[] xNumberTopToBottom;
    private ArrayList<String>[] xNumberLeftToRight;


    private static final int timeCheckButton = 5;
    double timer;
    boolean won;
    boolean showAnswers;
    boolean auxShowAnswer;

    public QuickGameScene(EngineApp engine, int rows, int cols) {

        //Asociamos el engine correspondiente
        this.engine = engine;

        scaleHeight = 1000;
        scaleWidth = 1000;


        this.engine.setColorBackground(0xFFFFFFFF);

        //Creamos el random
        Random random = new Random();

        //Creamos la matriz con el tamaño
        this.matriz = new CellQuickGame[cols][rows];

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

        init();

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
                    this.matriz[j][i].solution = false;

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

                    this.matriz[j][i].solution = true;
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
                int aux = random.nextInt(cols_);
                //Minimo rellenamos una
                this.matriz[aux][i].solution = true;
                xPositionsTopToBottom[i].add(1);

                //Ahora limpiamos la columna correspondiente y volvemos a contar
                xPositionsLeftToRight[aux].clear();

                int cont = 0;
                //Recorremos la columna otra vez para rellenar correctamente la fila
                for (int j = 0; j < rows_; j++) {
                    if (this.matriz[aux][j].solution) {
                        cont++;
                    } else if (cont != 0) {
                        xPositionsLeftToRight[aux].add(cont);
                        cont = 0;
                    }
                }
                if (cont != 0) {
                    xPositionsLeftToRight[aux].add(cont);
                }

                //Y la contabilizamos
                remainingCells++;

            }
            //Si por el contrario todas se han rellenado
            else if (numSolutionPerRows == cols_) {
                int aux = random.nextInt(cols_);
                //Dejamos al menos una vacia
                this.matriz[aux][i].solution = false;

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
                    if (this.matriz[aux][j].solution) {
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
                xPositionsTopToBottom[i].add(contAux);
            }
        }

        //Ahora hacemos lo mismo pero para las columnas
        for (int i = 0; i < cols_; i++) {
            //Si casualmente la columna se ha quedado totalmente vacia
            if (xPositionsLeftToRight[i].size() == 0) {
                int randAux = random.nextInt(rows_);

                //Minimo rellenamos una
                this.matriz[i][randAux].solution = true;
                xPositionsLeftToRight[i].add(1);

                //Contabilizamos esa suma
                remainingCells++;

                //Limpiamos el lateral
                xPositionsTopToBottom[randAux].clear();

                int cont = 0;
                //Recorremos la columna otra vez para rellenar correctamente la fila
                for (int j = 0; j < cols_; j++) {
                    if (this.matriz[j][randAux].solution) {
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

        return (coords.getX() * scaleWidth / engine.getGraphics().getWidth() >= pos.getX() && coords.getX() * scaleWidth / engine.getGraphics().getWidth() <= pos.getX() + size.getX() &&
                coords.getY() * scaleHeight / engine.getGraphics().getHeight() >= pos.getY() && coords.getY() * scaleHeight / engine.getGraphics().getHeight() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {

        //Dividimos la pantalla en casillas.
        // Ancho: Cols +1(Para los numeros)
        // Alto: Rows +1( Sin contar la interfaz de por encima y por debajo)

        //+1 para los numeros laterales
        //Y con eso sacamos el tamaño promedio de la celda
        tamProporcionalAncho = scaleWidth / ((cols_ + 1) + 1);

        //Restamos la interfaz de las paletas y los botones de arriba
        tamProporcionalAlto = (scaleHeight - scaleHeight / 7 - scaleHeight / 15) / ((rows_ + 1) + 1);

        int tamTexto = (int) (tamProporcionalAncho / 4.5f);
        if (tamProporcionalAlto > tamProporcionalAncho)
            //Nos quedamos con el tamaño mas grande para que el texto se ajuste a la peor situacion
            tamTexto = (int) (tamProporcionalAlto / 4.5f);
        //Con este tamaño ajustamos el valor del texto tambien
        this.engine.getGraphics().changeSizeText("CalibriSmall", (int) (tamTexto));

        //Lo ajustamos al centro de la pantalla de largo
        //Scale/15 para la interfaz de arriba + 1Celda para las letras
        double yPos;

        //+1Celda para las letras
        double xPos;

        //Iniciamos la matriz
        for (int i = 0; i < rows_; i++) {
            for (int j = 0; j < cols_; j++) {
                yPos = ((scaleHeight / 15 + tamProporcionalAlto) + ((tamProporcionalAlto * 1.1) * i));
                 xPos= (tamProporcionalAncho + ((tamProporcionalAncho * 1.1) * j));
                //Primero J que son las columnas en X y luego las filas en Y
                this.matriz[j][i] = new CellQuickGame((int)(xPos),
                        (int)yPos, (int)(tamProporcionalAncho), (int)(tamProporcionalAlto));
            }
        }

        //Para las posiciones del texto indicativo
        posYTextAuxTopToBottom = (int) (matriz[0][0].getPos().getY() + (tamProporcionalAlto / 2));
        posXTextAuxLeftToRight = (int) (matriz[0][0].getPos().getX() + (tamProporcionalAncho / 2));

        //Seteamos los botones
        this.checkInputButton = new InputButton(scaleWidth*0.8, scaleHeight*0.06,
                scaleWidth * 0.1666666, scaleHeight*0.10);
        this.giveUpInputButton = new InputButton(10,10,scaleWidth / 10, scaleHeight / 15);
        this.backInputButton = new InputButton(scaleWidth/2, scaleHeight/1.1,
                scaleWidth/10, scaleHeight/15);
    }

    @Override
    public void loadResources(EngineApp engineAux) {

    }

    @Override
    public void update(double deltaTime, AdManager adManager) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].update(deltaTime);
            }
        }

        if (engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType(), adManager);
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
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
        this.engine.getGraphics().drawImage((int) (auxCuadradoInicio.getX() - tamProporcionalAncho * 0.1), (int) (auxCuadradoInicio.getY() - tamProporcionalAlto * 0.1),
                (int) (auxCuadradoFinal.getX() - auxCuadradoInicio.getX() + tamProporcionalAncho + tamProporcionalAncho * 0.2)
                , (int) (auxCuadradoFinal.getY() - auxCuadradoInicio.getY() + tamProporcionalAlto + tamProporcionalAlto * 0.2), "Board");

        //Si ya he ganado...
        if (won) {
            //Solo renderizo las azules
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    this.matriz[i][j].solutionRender(engine);
                }
            }

            //Mensaje de enhorabuena
            this.engine.getGraphics().drawText("¡ENHORABUENA!", (int) ((double) scaleWidth * 0.5), (int) ((double) scaleHeight / 15), "Black", "Cooper", 0);

            //BackButton
            this.engine.getGraphics().drawImage((int)(backInputButton.getPos().getX()), (int)(backInputButton.getPos().getY()), (int)(backInputButton.getSize().getX()), (int)(backInputButton.getSize().getY()), "Back");

            //Si sigo jugando...
        } else {
            //Si tienes pulsado el boton de comprobar...
            if (showAnswers) {
                //Muestra el texto...
                this.engine.getGraphics().drawText("Te falta(n) " + remainingCells + " casilla(s)", (int)((double)scaleWidth/2), (int)((double)scaleHeight*0.1111111), "red", "Calibri", 0);
                this.engine.getGraphics().drawText("Tienes mal " + wrongCells + " casilla(s)", (int)((double)scaleWidth/2), (int)((double)scaleHeight*0.1388888), "red", "Calibri", 0);

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
                engine.getGraphics().drawText(xNumberTopToBottom[i], (int) (auxCuadradoInicio.getX() - (tamProporcionalAlto * 0.1)), posYTextAuxTopToBottom + (int) (tamProporcionalAlto * 1.1 * i), "Black", "CalibriSmall", 1);
            }
            for (int i = 0; i < xNumberLeftToRight.length; i++) {
                for (int j = 0; j < xNumberLeftToRight[i].size(); j++) {
                    engine.getGraphics().drawText(xNumberLeftToRight[i].get(j), posXTextAuxLeftToRight + (int) (tamProporcionalAncho * 1.1 * i), (int) (auxCuadradoInicio.getY() - (xNumberLeftToRight[i].size() * tamProporcionalAlto * 0.7 / (rows_ / 2)) + (int) ((tamProporcionalAlto / rows_ * 1.3 * j))), "Black", "CalibriSmall", 0);
                }
            }

            //BOTONES
            this.engine.getGraphics().drawImage((int)((double) checkInputButton.getPos().getX()), (int)((double) checkInputButton.getPos().getY()), (int)((double) checkInputButton.getSize().getX()), (int)((double) checkInputButton.getSize().getY()), "Check");

            this.engine.getGraphics().drawImage((int)((double) giveUpInputButton.getPos().getX()), (int)((double) giveUpInputButton.getPos().getY()), (int)((double) giveUpInputButton.getSize().getX()), (int)((double) giveUpInputButton.getSize().getY()), "GiveUp");
        }
    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager) {
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
        if (inputReceived(this.checkInputButton.getPos(), this.checkInputButton.getSize())) {
            //Mostramos el texto en pantalla
            showAnswers = true;
            auxShowAnswer = true;
            timer = timeCheckButton;
        }
        //Si te rindes vuelves a la seleccion de nivel
        if (inputReceived(this.giveUpInputButton.getPos(), this.giveUpInputButton.getSize())) {
            this.engine.getSceneMngr().popScene();
        }
        //Solo funciona si has ganado
        if (won && inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())) {
            this.engine.getSceneMngr().popScene();
            this.engine.getSceneMngr().popScene();
        }
    }

    //Se llama cuando la escena posterior se elimina y se vuelve aqui, por si hay que actualizar algo
    @Override
    public void onResume() {
        init();
    }

    //Comprueba si has ganado o no
    private boolean win() {
        return remainingCells == 0 && wrongCells == 0;
    }
}
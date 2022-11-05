package com.example.logica;

import com.example.lib.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

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

    int remainingCells, wrongCells;

    HashMap<String, IFont> fonts;

    JPanel panel;
    JButton playButton;
    JButton backButton;
    JButton checkButton;
    JButton giveUpButton;

    //Este mapa ordenado guarda las casillas erroneas pulsadas
    TreeMap<Integer, Integer> wrongCellsPosition = new TreeMap<>();

    private Engine engine;

    public MyScene(Engine engine, int rows, int cols, IFont[] fontsAux, String[] keys) {

        //Asociamos el engine correspondiente
        this.engine = engine;

        fonts = new HashMap<>();

        //Y las fuentes
        for (int i = 0; i < fontsAux.length; i++) {
            fonts.put(keys[i], fontsAux[i]);
        }

        //Creamos el random
        Random random = new Random();


        //CREO QUE HAY QUE QUITAR TODO ESTO
        //Creamos un JPanel para mostrar la tabla
//        JPanel panel = new JPanel();

        //Creamos la matriz con el tamaño
        //AAAAAAAAAAAAAAAAAAAAA MODIFICAR TAMAÑO
        this.matriz = new Cell[rows][cols];

        rows_ = rows;
        cols_ = cols;

        xPositionsTopToBottom = new ArrayList[cols_];
        xPositionsLeftToRight = new ArrayList[rows_];
        xNumberTopToBottom = new String[cols_];
        xNumberLeftToRight = new ArrayList[rows_];


        //Iniziamos la matriz
        for (int i = 0; i < rows_; i++) {
            for (int j = 0; j < cols_; j++) {
                this.matriz[i][j] = new Cell(80 + 60 * i, 150 + 60 * j, 54, 54);
            }
        }

        //Variable auxiliar solo para que la creacion aleatoria tenga más sentido
        ArrayList<Integer> colums = new ArrayList<>();
        for (int i = 0; i < rows_; i++) {
            colums.add(0);
            xPositionsLeftToRight[i] = new ArrayList<>();
        }


        int[] numAnterior = new int[cols_];
        int[] contadorCols = new int[cols_];
        //Inicializamos los valores a -1
        for (int i = 0; i < cols_; i++) {
            numAnterior[i] = -1;
            contadorCols[i] = 1;
            xPositionsTopToBottom[i] = new ArrayList<>();
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
                        xPositionsLeftToRight[i].add(contAux);
                        contAux = 0;
                    }
                    this.matriz[i][j].setSolution(false);

                    //Para el valor de las columnas...
                    if (numAnterior[j] == 0) {
                        //Reseteamos
                        contadorCols[j] = 1;
                        numAnterior[j] = -1;

                    }
                }
                //Si es 1 se rellena
                else {
                    this.matriz[i][j].setSolution(true);
                    numSolutionPerRows++;
                    //Para averiguar los numeros laterales de las celdas
                    contAux++;

                    //Para que no haya ninguna fila o columna vacía
                    colums.set(j, colums.get(j) + 1);

                    //PARA AUXILIAR
                    //Si nunca se han añadido...
                    if (numAnterior[j] == -1) {
                        //Metemos el primero...
                        xPositionsTopToBottom[j].add(1);
                        //Y por lo tanto ya tenemos uno añadito
                        numAnterior[j] = 0;
                        //Con esto solo entra si se ha añadido algo alguna vez
                    } else if (numAnterior[j] == 0) {
                        contadorCols[j]++;
                        //Sumamos el valor +1 porque la columna continua
                        //Eliminamos el anterior
                        xPositionsTopToBottom[j].remove(xPositionsTopToBottom[j].size() - 1);
                        //Y metemos el nuevo
                        xPositionsTopToBottom[j].add(xPositionsTopToBottom[j].size(), contadorCols[j]);
                    }
                }
            }

            //Si casualmente la fila se ha quedado totalmente vacia
            if (numSolutionPerRows == 0) {
                //Minimo rellenamos una
                this.matriz[i][random.nextInt(cols_)].setSolution(true);
                xPositionsLeftToRight[i].add(1);
            }
            //Si por el contrario todas se han rellenado
            else if (numSolutionPerRows == cols_) {
                //AAA MENCIONAR TODO EN EL PDF QUE ESTO COMPLICA TODO PERO SE QUEDA UN CUADRADO MAS BONITO Y CURRAO
                int aux = random.nextInt(cols_);
                //Dejamos al menos una vacia
                this.matriz[i][aux].setSolution(false);

                //Y añadimos al lateral los 2 valores seccionados
                xPositionsLeftToRight[i].add(aux + 1);
                xPositionsLeftToRight[i].add(contAux - aux);
            }

            //Para meter en el lateral si el ultimo valor de la fila se ha seleccionado
            if (contAux != 0) {
                xPositionsLeftToRight[i].add(contAux);
            }
        }

        //Ahora hacemos lo mismo pero para las columnas
        for (int i = 0; i < rows_; i++) {
            //Si casualmente la columna se ha quedado totalmente vacia
            if (colums.get(i) == 0) {
                //Minimo rellenamos una
                this.matriz[random.nextInt(rows_)][i].setSolution(true);
            }
            //EN UN NONOGRAMA ES NORMAL UNA FILA/COLUMNA CON TO SELECCIONADO, pero hago la comprobacion en las filas
            //Para evitar cubos grandes que no tengan forma y solo sean relleno y evitar que salga algo compacto
            //Si por el contrario todas se han rellenado
//            else if(colums.get(i) == rows_){
//                //Dejamos al menos una vacia
//                this.matriz[random.nextInt(rows_)][i].setSolution(false);
//            }
        }


        for (int i = 0; i < xPositionsTopToBottom.length; i++) {
            xNumberTopToBottom[i] = "";
            for (int j = 0; j < xPositionsTopToBottom[i].size(); j++) {
                xNumberTopToBottom[i] += xPositionsTopToBottom[i].get(j).toString() + " ";
            }
        }

        for (int i = 0; i < xPositionsLeftToRight.length; i++) {
            for (int j = 0; j < xPositionsLeftToRight[i].size(); j++) {
                xNumberLeftToRight[i].add(xPositionsLeftToRight[i].get(j).toString());
            }
        }


        //QUE FUNCIONA JODER FUNCIONA OSTIA PUTA
//        for(int i=0;i<xPositionsWidth.length;i++){
//            for(int j=0;j<xPositionsWidth[i].size();j++){
//                System.out.print(xPositionsWidth[i].get(j));
//            }
//            System.out.println("     -       ");
//
//        }
//        panel = new JPanel();
//        panel.setBackground(Color.RED);
//        panel.setBounds(0,0,300,300);
//        checkButton = new JButton("Comprobar");
//        giveUpButton = new JButton("Rendirse");

//        checkButton.setBounds(500,500,100,100);
//        giveUpButton.setBounds(40,120,100,100);


//        checkButton.setVisible(true);
//        giveUpButton.setVisible(true);

//        panel.add(checkButton);
//        panel.add(giveUpButton);
//        panel.setVisible(true);
//        engine.addComponent(panel);
//        engine.addComponent(checkButton);
//        engine.addComponent(giveUpButton);

        //Input del botón de comproabr
//        checkButton.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent mouseEvent) {
//                //PROBABLEMENTE VACIO
//            }
//
//            @Override
//            public void mousePressed(MouseEvent mouseEvent) {
//                //Cambiar de color o algo no se.
//                //CAMBIAR EL TAMAÑO Y HACERLO MAS PEQUEÑITO
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent mouseEvent) {
//                //NO ES JTEXTFIELD PERO NO ME APETECE MIRAR
//                //Mostramos el mensaje en rojo si faltan casillas o tienes alguna mal seleccionada
////                if(remainingCells == 1) {
////                    engine.setRemainingCells("Te falta " + remainingCells + " casilla");
////                }
////                else{
////                    engine.setRemainingCells("Te faltan " + remainingCells + " casillas");
////                }
////                if(wrongCells == 1) {
////                    engine.setWrongCells("Tienes mal " + wrongCells + " casilla");
////                }
////                else{
////                    engine.setWrongCells("Tienes mal " + wrongCells + " casillas");
////                }
//            }
//
//            //Esto lo que nos salga del higo
//            @Override
//            public void mouseEntered(MouseEvent mouseEvent) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent mouseEvent) {
//            }
//        });
    }

    @Override
    public void update(double deltaTime) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                this.matriz[i][j].update(deltaTime);
            }
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
            engine.drawText(xNumberTopToBottom[i], 20, 180 + 60*i, "Black", fonts.get("Calibri"));

        }
        for (int i = 0; i < xNumberLeftToRight.length; i++) {
            for(int j=0;j<xNumberLeftToRight[i].size();j++){
                engine.drawText(xNumberLeftToRight[i].get(j),100 + 60*i, 50 + 30*j,"Black",fonts.get("Calibri"));

            }
        }

    }
}
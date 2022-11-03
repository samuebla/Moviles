package com.example.enginedesktop;

import com.example.lib.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;


import javax.swing.JFrame;
import javax.swing.JTextPane;

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class EngineDesktop implements Engine,Runnable{

    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;

    private RenderDesktop render;

    private Thread renderThread;

    private boolean running;

    private Scene scene;

    //TEXTO DE REMAINING CELLS Y WRONG CELLS DE PRUEBA AAAAA
    JTextPane remainingCells = new JTextPane();
    JTextPane wrongCells = new JTextPane();

    public EngineDesktop(final JFrame myView){
        // Intentamos crear el buffer strategy con 2 buffers.
        this.myView = myView;

        int intentos = 100;
        while(intentos-- > 0) {
            try {
                this.myView.createBufferStrategy(2);
                break;
            }
            catch(Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }

        this.bufferStrategy = this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

//        this.myView.addComponentListener(new ComponentListener() {
//            @Override
//            public void componentResized(ComponentEvent componentEvent) {
//                System.out.println(componentEvent.getComponent().getWidth());
//                getGraphics().setResolution();
//            }
//
//            @Override
//            public void componentMoved(ComponentEvent componentEvent) {
//
//            }
//
//            @Override
//            public void componentShown(ComponentEvent componentEvent) {
//
//            }
//
//            @Override
//            public void componentHidden(ComponentEvent componentEvent) {
//
//            }
//        });
//
        this.render = new RenderDesktop(myView);
    }

//    //<<< API >>>
//    public void pintarCirculo(float x, float y, float r, String color){
//        Color c;
//        if(color == "blue"){
//            c = Color.blue;
//        } else if(color == "red"){
//            c = Color.red;
//        } else {
//            c= Color.white;
//        }
//        this.graphics2D.setColor(c);
//        this.graphics2D.fillOval((int)x, (int)y, (int)r*2, (int)r*2);
//        this.graphics2D.setPaintMode();
//    }

    //EMPTY(gray) = 0
    //SELECTED(blue) = 1
    //CROSSED(crossed) = 2
    //WRONG(red) = 3
    //blank = marco vacio para la interfaz = -1
    @Override
    public void paintCell(int x, int y, int w, int h, int celltype){
        Color c;
        if(celltype == 1){
            c = Color.blue;
        } else if(celltype == 3){
            c = Color.red;
        } else if(celltype == 0){
            c = Color.gray;
        }
        //none and blank
        else{
            c = Color.black;
        }
        this.graphics2D.setColor(c);

        if (celltype== -1 || celltype == 2){
            this.graphics2D.drawRect(x, y, w, h);
            //Cuadrado de la interfaz
            if (celltype == 2){
                this.graphics2D.drawLine(x,y,w+x,h+y);
            }
        }else{
            //Cambiar para que tenga en cuenta las dimensiones de la ventana, los últimos dos valores son el ancho y alto
            this.graphics2D.fillRect(x, y, w, h);
        }
        this.graphics2D.setPaintMode();
    }

    public void drawText(String text, int x, int y, String color){
        Color c;
        if(color == "red"){
            c = Color.red;
        } else{
            c = Color.black;
        }
        this.graphics2D.setColor(c);
        this.graphics2D.drawString(text, x, y);
        //No estoy muy seguro de este metodo, quitar si no funciona bien
        this.graphics2D.setPaintMode();
    }

//    @Override
//    public void addComponent(Component aux){
//        myView.add(aux);
//    }


    public int getWidth(){
        return this.myView.getWidth();
    }
    public int getHeight(){
        return this.myView.getHeight();
    }
    //<< Fin API>>

    //<<Motor>>
    @Override
    public IGraphics getGraphics(){
        return null;
    }
    @Override
    public IAudio getAudio(){
        return null;
    }
    @Override
    public IState getState(){
        return null;
    }

    @Override
    public void run() {
        if (this.renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(this.running && this.myView.getWidth() == 0);
        // Espera activa. Sería más elegante al menos dormir un poco.

        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        // Bucle de juego principal.
        while(running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
//            this.update(elapsedTime);
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            // Pintamos el frame
            do {
                do {
                    Graphics graphics = this.bufferStrategy.getDrawGraphics();
                    try {
                        this.render.render();
                    }
                    finally {
                        graphics.dispose(); //Elimina el contexto gráfico y libera recursos del sistema realacionado
                    }
                } while(this.bufferStrategy.contentsRestored());
                this.bufferStrategy.show();
            } while(this.bufferStrategy.contentsLost());

            /*
            // Posibilidad: cedemos algo de tiempo. Es una medida conflictiva...
            try { Thread.sleep(1); } catch(Exception e) {}
            */
        }
    }


    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.running = true;
            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    public void pause() {
        if (this.running) {
            this.running = false;
            while (true) {
                try {
                    this.renderThread.join();
                    this.renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca...
                }
            }
        }
    }

    @Override
    public void setWrongCells(String text){
        wrongCells.setText(text);
    }

    @Override
    public void setRemainingCells(String text){
        remainingCells.setText(text);
    }

    @Override
    public void setScene(Scene newScene){
        this.render.setScene(newScene);
    }
}


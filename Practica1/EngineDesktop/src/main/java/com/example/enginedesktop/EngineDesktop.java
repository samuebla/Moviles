package com.example.enginedesktop;

import com.example.lib.Engine;
import com.example.lib.IAudio;
import com.example.lib.IGraphics;
import com.example.lib.IState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;


import javax.swing.JFrame;

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class EngineDesktop extends Engine{

    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;

    private Thread renderThread;

    private boolean running;

    private MyScene scene;

    @Override
    public IGraphics getGraphics(){

    }
    @Override
    public IAudio getAudio(){

    }
    @Override
    public IState getState(){

    }

    public EngineDesktop(JFrame myView){
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
    }

    //<<< API >>>
    public void pintarCirculo(float x, float y, float r, String color){
        Color c;
        if(color == "blue"){
            c = Color.blue;
        } else if(color == "red"){
            c = Color.red;
        } else {
            c= Color.white;
        }
        this.graphics2D.setColor(c);
        this.graphics2D.fillOval((int)x, (int)y, (int)r*2, (int)r*2);
        this.graphics2D.setPaintMode();
    }

    //red = 2
    //blue = 1
    //gray = 3
    //none = celda negada del tablero = 0
    //blank = cuadrado vacio para la interfaz = -1
    @Override
    public void paintCell(int x1, int y1, int x2, int y2, int celltype){
        Color c;
        if(celltype == 1){
            c = Color.blue;
        } else if(celltype == 2){
            c = Color.red;
        } else if(celltype == 3){
            c = Color.gray;
        }
        //none and blank
        else{
            c = Color.white;
        }
        this.graphics2D.setColor(c);

        if (celltype== -1 || celltype == 0){
            this.graphics2D.drawRect(x1, y1, x2 - x1, y2 - y1);
            //Cuadrado de la interfaz
            if (celltype == 0){
                this.graphics2D.drawLine(x1,y1,x2,y2);
            }
        }else{
            //        Cambiar para que tenga en cuenta las dimensiones de la ventana, los últimos dos valores son el ancho y alto
            this.graphics2D.fillRect(x1, y1, x2 - x1, y2 - y1);
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


    public int getWidth(){
        return this.myView.getWidth();
    }
    public int getHeight(){
        return this.myView.getHeight();
    }
    //<< Fin API>>

    //<<Motor>>

    public void setScene(MyScene scene) {
        this.scene = scene;
    }

    //blucle principal
//    @Override
//    public void run() {
//        if (renderThread != Thread.currentThread()) {
//            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
//            // Programación defensiva
//            throw new RuntimeException("run() should not be called directly");
//        }
//        // Si el Thread se pone en marcha
//        // muy rápido, la vista podría todavía no estar inicializada.
//        while(this.running && this.myView.getWidth() == 0);
//        // Espera activa. Sería más elegante al menos dormir un poco.
//        long lastFrameTime = System.nanoTime();
//        long informePrevio = lastFrameTime; // Informes de FPS
//        int frames = 0;
//
//        // Bucle de juego principal.
//        while(running) {
//            long currentTime = System.nanoTime();
//            long nanoElapsedTime = currentTime - lastFrameTime;
//            lastFrameTime = currentTime;
//
//            // Actualizamos
//            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
//            this.update(elapsedTime);
//
//            // Pintamos el frame
//            do {
//                do {
//                    Graphics graphics = this.bufferStrategy.getDrawGraphics();
//                    try {
//                        this.render();
//                    }
//                    finally {
//                        graphics.dispose(); //Elimina el contexto gráfico y libera recursos del sistema realacionado
//                    }
//                } while(this.bufferStrategy.contentsRestored());
//                this.bufferStrategy.show();
//            } while(this.bufferStrategy.contentsLost());
//        }
//    }

//    protected void update(double deltaTime) {
//        this.scene.update(deltaTime);
//    }

//    protected void render() {
//        // "Borramos" el fondo.
//        this.graphics2D.setColor(Color.black);
//        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
//        // Pintamos la escena
//        this.scene.render();
//    }

    //Métodos sincronización (parar y reiniciar aplicación)
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
}


package com.example.enginedesktop;

import com.example.lib.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;


import javax.swing.JFrame;

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class EngineDesktop implements Engine{

    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;

    private RenderDesktop render;

    private Thread renderThread;

    private boolean running;

    private Scene scene;

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

    //Métodos sincronización (parar y reiniciar aplicación)
    public void resume() {
        this.render.resume();
    }

    public void pause() {
        this.render.pause();
    }

    @Override
    public void setScene(Scene newScene){
        this.render.setScene(newScene);
    }
}


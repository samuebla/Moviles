package com.example.enginedesktop;

import com.example.lib.*;
import  com.example.lib.IGraphics.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class RenderDesktop implements IGraphics {

    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;

    //AAAA ESTO NO TENDRIA QUE ESTAR CREO YO
    private Scene scene;

    public RenderDesktop(JFrame myView){
        this.myView = myView;
        this.myView.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                //Component c = (Component)evt.getSource();
                System.out.println("componentResized: "+evt.getSource());
                graphics2D.dispose();

                bufferStrategy.show();
                graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
                setResolution(evt.getComponent().getWidth(), evt.getComponent().getHeight());
            }
        });

        this.bufferStrategy = this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

    }

    public int getWidth(){
        return this.myView.getWidth();
    }

    public int getHeight(){
        return this.myView.getHeight();
    }


////    protected void update(double deltaTime) {
////        this.scene.update(deltaTime);
////    }
//
////    public void setScene(MyScene scene) {
////        this.scene = scene;
////    }
//
//    protected void renderCircle(float x, float y, float r){
//        this.graphics2D.setColor(Color.white);
//        this.graphics2D.fillOval((int)x, (int)y, (int)r*2, (int)r*2);
//        this.graphics2D.setPaintMode();
//    }
//
//    //EL INT TIPO ACABARA SIENDO UN ENUM aAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//    protected void renderSquare(int x, int y, int w,int h,int tipo ){
//        this.graphics2D.setColor(Color.black);
//        this.graphics2D.fillRect(x-1,y-1,w+2,h+2);
//
//        this.graphics2D.setColor(Color.blue);
//        this.graphics2D.fillRect(x,y,w,h);
//
//        this.graphics2D.setColor(Color.black);
//
//        if(tipo == 2)
//            this.graphics2D.drawLine(x,y,x+w,y+h);
//
//        //this.graphics2D.drawLine(x,y,x+w,y);
//        //this.graphics2D.drawLine(x,y,x,y+h);
//        //this.graphics2D.drawLine(x+w,y,x,y+h);
//        //this.graphics2D.drawLine(x,y+h,x+w,y);
//
//
//        this.graphics2D.setPaintMode();
//    }

    //CREO QUE ESTA MIERDA NO TIENE QUE ESTAR AQUI NONONO
    protected void render() {
        // "Borramos" el fondo.
        this.graphics2D.setColor(Color.white);
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
        // Pintamos la escena
        this.scene.render();
    }

    //CREO QUE ESTO TAMPOCO TIENE QUE ESTAR AYAYAY
    public void setScene(Scene newScene){
        this.scene = newScene;
    }

//    @Override
//    public IImage newImage() {
//        return null;
//    }
//
//    @Override
//    public IFont newFont() {
//        return null;
//    }

    @Override
    public void setResolution(double width, double height) {
        this.graphics2D.scale(width, height);


    }

    @Override
    public void setColor(int color) {
        //El color se pasa en Hexadecimal
        int r,g,b;
        //AAA REVISAR NO SE SI ES ASÍ
        //creo que es 16, 8 y nada
        r = (color & 0xFF0000)>>24;
        g = (color & 0xFF00)>>16;
        b = (color & 0xFF)>>8;

        //AAA NO SE COMO FUNCIONA EL ALPHA

        this.graphics2D.setColor(new Color(r,b,g,255));
    }

    @Override
    public void setFont(int size, int fontType) {

        //Le tienes que pasar la fuente wtf
        //this.graphics2D.setFont();
    }

    @Override
    public void drawImage() {

    }

    @Override
    public void drawRectangle(int x,int y,int w,int h) {
        this.graphics2D.drawRect(x,y,w,h);
        //No recuerdo si esto iba antes o despues, creo que despues
        this.graphics2D.setPaintMode();
    }

    @Override
    public void fillRectangle(int x,int y,int w,int h) {
        //CREO QUE AQUI NO VA ANCHO Y ALGO SINO X+W E Y+H
        this.graphics2D.fillRect(x,y,w,h);
        //No recuerdo si esto iba antes o despues, creo que despues
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawLine(int x,int y,int w,int h) {
        //Creo que el w y h n es lo que hay que pasarle
        this.graphics2D.drawLine(x,y,w,h);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawText(int x,int y,String text) {

        //this.graphics2D.setFont();

    }
}


package com.example.enginedesktop;

import com.example.lib.*;
import  com.example.lib.IGraphics.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

class WindowSize{
    public double w;
    public double h;
};

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class RenderDesktop implements IGraphics {

    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;

    private Insets insets;

    WindowSize baseSize;
    WindowSize windowSize;
    WindowSize nextWindowSize;
    WindowSize nextScale;
    WindowSize prevScale;

    public RenderDesktop(JFrame myView){
        this.myView = myView;

        this.bufferStrategy = this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D)bufferStrategy.getDrawGraphics();

        this.insets = this.myView.getInsets();
        this.myView.setSize(this.myView.getWidth() + this.insets.right + this.insets.left,
                this.myView.getHeight() + this.insets.bottom + this.insets.top);
        this.graphics2D.translate(this.insets.top, this.insets.left);

        this.windowSize = new WindowSize();
        this.windowSize.w = this.myView.getSize().width;
        this.windowSize.h = this.myView.getSize().height;

        this.nextWindowSize = new WindowSize();
        this.nextWindowSize.w = this.myView.getSize().width;
        this.nextWindowSize.h = this.myView.getSize().height;

        this.nextScale = new WindowSize();
        this.nextScale.w = 1.0;
        this.nextScale.h = 1.0;

        this.prevScale = new WindowSize();
        this.prevScale.w = 1.0;
        this.prevScale.h = 1.0;

        this.myView.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                graphics2D.dispose();
                bufferStrategy.show();
                graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
                double w = e.getComponent().getWidth(); double h = e.getComponent().getHeight();
                nextScale.w = w/ windowSize.w;
                nextScale.h = h/ windowSize.h;
                nextWindowSize.w = w;
                nextWindowSize.h = h;
            }
        });

        baseSize = new WindowSize();
        baseSize.h = this.myView.getHeight();
        baseSize.w = this.myView.getWidth();

        this.myView.setResizable(true);
    }

    public int getWidth(){
        return this.myView.getWidth();
    }

    public int getHeight(){
        return this.myView.getHeight();
    }


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

    public void initFrame(){
        this.graphics2D = (Graphics2D) this.bufferStrategy.getDrawGraphics();
        this.graphics2D.setColor(Color.white);
        this.graphics2D.fillRect(0,0, getWidth(), getHeight());

        this.graphics2D.scale(this.prevScale.w, this.prevScale.h);
    }

    //CREO QUE ESTA MIERDA NO TIENE QUE ESTAR AQUI NONONO
    protected void render() {
        // "Borramos" el fondo.
        this.graphics2D.setColor(Color.white);
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
    }

    public void clearFrame(){
        this.graphics2D.scale(this.nextScale.w, this.nextScale.h);
//        this.myView.setSize((int)nextWindowSize.w, (int)nextWindowSize.h);
        this.prevScale.w = this.nextScale.w;
        this.prevScale.h = this.nextScale.h;
        this.windowSize.w = (double)this.myView.getSize().width;
        this.windowSize.h = (double)this.myView.getSize().height;

        this.bufferStrategy.getDrawGraphics().dispose();
    }

    public boolean swap(){
        if (this.bufferStrategy.contentsRestored())
            return false;

        this.bufferStrategy.show();
        return this.bufferStrategy.contentsLost();
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
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, IImage imageAux) {
        ImageDesktop image = (ImageDesktop) imageAux;
        this.graphics2D.drawImage(image.getImage(), x, y, desiredWidth, desiredHeight,
                0, 0, image.getWidth(), image.getHeight(), null);
        this.graphics2D.setPaintMode();
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
    public void drawText(String text, int x, int y, String color,IFont font){
        //Casteamos esta mierda porque sino no nos funciona
        FontDesktop f = (FontDesktop) font;

        Color c;
        if(color == "red"){
            c = Color.red;
        } else{
            c = Color.black;
        }
        this.graphics2D.setColor(c);
        this.graphics2D.setFont(f.getFont());
        this.graphics2D.drawString(text, x, y);
        //No estoy muy seguro de este metodo, quitar si no funciona bien
        this.graphics2D.setPaintMode();
    }

    //EMPTY(gray) = 0
    //SELECTED(blue) = 1
    //CROSSED(crossed) = 2
    //WRONG(red) = 3
    //blank = marco vacio para la interfaz = -1
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
}


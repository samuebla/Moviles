package com.example.enginedesktop;

import com.example.lib.*;
import com.example.lib.IGraphics.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;

//class WindowSize{
//    public double w;
//    public double h;
//};

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class RenderDesktop implements IGraphics {

    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;

    private Insets insets;

    Vector2D canvasSize;
    Vector2D margins;

    public double factorScale = 1.0;
    public double scaleProportion = 1.0;

    HashMap<String,FontDesktop> fonts;
    HashMap<String,ImageDesktop> images;

//    WindowSize baseSize;
//    WindowSize windowSize;
//    WindowSize nextWindowSize;
//    WindowSize nextScale;
//    WindowSize prevScale;

    public RenderDesktop(JFrame myView) {
        //Canvas y ventana
        this.myView = myView;
        this.bufferStrategy = this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
        this.insets = this.myView.getInsets();

        fonts = new HashMap<>();
        images = new HashMap<>();

        //Ajuste tamaño de la ventana y desplazamiento del canvas con margenes
        this.myView.setSize(this.myView.getWidth() + this.insets.right + this.insets.left,
                this.myView.getHeight() + this.insets.bottom + this.insets.top);
        this.graphics2D.translate(this.insets.top, this.insets.left);

        //Tamaño inicial del canvas
        this.canvasSize = new Vector2D(this.myView.getSize().width, this.myView.getSize().height);

        //Guardado del escalado y las proporciones a mantener
        this.scaleProportion = this.graphics2D.getTransform().getScaleX();

//        this.nextWindowSize = new Vector2D();
//        this.nextWindowSize.w = this.myView.getSize().width;
//        this.nextWindowSize.h = this.myView.getSize().height;
//
//        this.nextScale = new WindowSize();
//        this.nextScale.w = 1.0;
//        this.nextScale.h = 1.0;
//
//        this.prevScale = new WindowSize();
//        this.prevScale.w = 1.0;
//        this.prevScale.h = 1.0;

//        this.myView.addComponentListener(new ComponentAdapter() {
//            public void componentResized(ComponentEvent e) {
//                    graphics2D.dispose();
//                    bufferStrategy.show();
//                try {
//                    graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
//                }
//                catch (Exception exception){
//                    System.err.println("Bad thread buffering management");
//                }
//                    double w = e.getComponent().getWidth();
//                    double h = e.getComponent().getHeight();
//                    nextScale.w = w / windowSize.w;
//                    nextScale.h = h / windowSize.h;
//                    nextWindowSize.w = w;
//                    nextWindowSize.h = h;
//            }
//        });
//
//        baseSize = new WindowSize();
//        baseSize.h = this.myView.getHeight();
//        baseSize.w = this.myView.getWidth();

        this.myView.setResizable(true);
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

    public void initFrame() {
        //Siguiente buffer a procesar
        this.graphics2D = (Graphics2D) this.bufferStrategy.getDrawGraphics();
        //Ajustar escala del canvas
        this.scaleCanvas();

        this.graphics2D.setColor(Color.white);
        this.graphics2D.fillRect(0, 0, getWidth(), getHeight());
    }

    protected void render() {
        // "Borramos" el fondo.
        this.graphics2D.setColor(Color.white);
        this.graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void clearFrame() {
        //dibujar los margenes por encima
        drawMargins();

        this.bufferStrategy.getDrawGraphics().dispose();
    }

    public boolean swap() {
        if (this.bufferStrategy.contentsRestored())
            return false;

        this.bufferStrategy.show();
        return this.bufferStrategy.contentsLost();
    }


    @Override
    public IImage newImage(String imageName,String path) {
        return images.put(imageName,new ImageDesktop(new File(path)));
    }

    @Override
    public IFont newFont(String fontName,String path,int type, int size) {
        return fonts.put(fontName,new FontDesktop(new File(path),type,size));
    }

    @Override
    public void setResolution(double width, double height) {
        this.graphics2D.scale(width, height);
    }

    @Override
    public int getWidth() {
        return (int) this.canvasSize.getX();
    }

    @Override
    public int getHeight() {
        return (int) this.canvasSize.getY();
    }

    @Override
    public void setColor(int color) {
        //El color se pasa en Hexadecimal
        int r, g, b;
        //AAA REVISAR NO SE SI ES ASÍ
        //creo que es 16, 8 y nada
        r = (color & 0xFF0000) >> 24;
        g = (color & 0xFF00) >> 16;
        b = (color & 0xFF) >> 8;

        //AAA NO SE COMO FUNCIONA EL ALPHA

        this.graphics2D.setColor(new Color(r, b, g, 255));
    }


    @Override
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String imageName) {
        ImageDesktop image = images.get(imageName);
        this.graphics2D.drawImage(image.getImage(), x, y, desiredWidth, desiredHeight,
                0, 0, image.getWidth(), image.getHeight(), null);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawRectangle(int x, int y, int w, int h, boolean fill) {
        if (!fill)
            this.graphics2D.drawRect(x, y, w, h);
        else
            this.graphics2D.fillRect(x, y, w, h);

        this.graphics2D.setPaintMode();
    }

    @Override
    public void fillRectangle(int x, int y, int w, int h) {
        //CREO QUE AQUI NO VA ANCHO Y ALGO SINO X+W E Y+H
        this.graphics2D.fillRect(x, y, w, h);
        //No recuerdo si esto iba antes o despues, creo que despues
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawLine(int x, int y, int w, int h) {
        //Creo que el w y h n es lo que hay que pasarle
        this.graphics2D.drawLine(x, y, w, h);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawText(String text, int x, int y, String color, String fontKey) {

        Color c;
        if (color == "red") {
            c = Color.red;
        } else {
            c = Color.black;
        }
        this.graphics2D.setColor(c);
        this.graphics2D.setFont(fonts.get(fontKey).getFont());
        this.graphics2D.drawString(text, x, y);
        this.graphics2D.setPaintMode();
    }


    private void scaleCanvas() {
        //Averiguamos la escala más pequeña para mantener la proporción
        double scaleX = (myView.getWidth() - insets.left - insets.right) / canvasSize.getX();
        double scaleY = (myView.getHeight() - insets.top - insets.bottom) / canvasSize.getY();
        factorScale = Math.min(scaleX, scaleY) * scaleProportion;

        Vector2D tr = new Vector2D((int) ((this.myView.getWidth() / 2 - canvasSize.getX() * (factorScale / scaleProportion) / 2) * scaleProportion),
                (int) (((this.myView.getHeight() + this.insets.top - this.insets.bottom) / 2 - canvasSize.getY() * (factorScale / scaleProportion) / 2) * scaleProportion));

        //Margenes de la pantalla
        margins = new Vector2D(Math.max(tr.getX() - this.insets.left, 0), Math.max(tr.getY() - this.insets.top, 0));

        //Proceso de escalado
        AffineTransform aTr = this.graphics2D.getTransform();
        aTr.setToTranslation(tr.getX(), tr.getY());
        this.graphics2D.setTransform(aTr);
        aTr.setToScale((factorScale / aTr.getScaleX()), (factorScale / aTr.getScaleY()));
        this.graphics2D.transform(aTr);
    }

    private void drawMargins() {
        this.graphics2D.setColor(Color.gray);

        drawRectangle((int) (-margins.getX() / factorScale), 0, (int) (margins.getX() / factorScale), (int) canvasSize.getY(), true);
        drawRectangle((int) canvasSize.getX(), 0, (int) (margins.getX() / factorScale), (int) canvasSize.getY(), true);

        drawRectangle(0, (int) (-margins.getY() / factorScale), (int) canvasSize.getX(), (int) (margins.getY() / factorScale), true);
        drawRectangle(0, (int) canvasSize.getY(), (int) canvasSize.getX(), (int) (margins.getY() / factorScale), true);
    }

    //EMPTY(gray) = 0
    //SELECTED(blue) = 1
    //CROSSED(crossed) = 2
    //WRONG(red) = 3
    //blank = marco vacio para la interfaz = -1
    public void paintCell(int x, int y, int w, int h, int celltype) {
        Color c;
        if (celltype == 1) {
            c = Color.blue;
        } else if (celltype == 3) {
            c = Color.red;
        } else if (celltype == 0) {
            c = Color.gray;
        }
        //none and blank
        else {
            c = Color.black;
        }
        this.graphics2D.setColor(c);

        if (celltype == -1 || celltype == 2) {
            this.graphics2D.drawRect(x, y, w, h);

            //Cuadrado de la interfaz
            if (celltype == 2) {
                this.graphics2D.drawLine(x, y, w + x, h + y);
            }
        } else {
            //Cambiar para que tenga en cuenta las dimensiones de la ventana, los últimos dos valores son el ancho y alto
            this.graphics2D.fillRect(x, y, w, h);
        }
        this.graphics2D.setPaintMode();
    }


    public double getScale() {
        return this.factorScale / this.scaleProportion;
    }
}


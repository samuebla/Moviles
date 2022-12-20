package com.example.engineandroid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;

public class RenderAndroid {

    //Maxima escala de la logica, por defecto: 1000, 1000.
    //Siendo 0 la izquierda del surface y 1000 la derecha del surface para scaleWidth.
    //Y 0 arriba del surface y 1000 abajo del surface para scaleHeight.
    int scaleWidth, scaleHeight;

    //Surface y variables necesarias para el renderizado
    private final SurfaceView myView;
    private final SurfaceHolder holder;
    private Canvas canvas;
    private final Paint paint;

    //Color de fondo
    int resetColor;
    int colorBackground;

    //Hasmaps para los recursos de fuentes e imagenes, permite acceder a cada una mediante strings
    HashMap<String, Font_Android> fonts;
    HashMap<String, ImageAndroid> images;

    //Todos los assets cargados desde la escena
    private AssetManager assets;

    public RenderAndroid(SurfaceView myView) {
        // Intentamos crear el buffer strategy con 2 buffers.
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFF000000);

        this.fonts = new HashMap<>();
        this.images = new HashMap<>();

        colorBackground = 0XFFFFFFFF;
        //Por defecto la escala es 1000x1000
        scaleHeight = 1000;
        scaleWidth = 1000;
    }

    public void prepareFrame() {
        // Pintamos el framefsefsefdfs
        while (!this.holder.getSurface().isValid()) ;
        this.canvas = this.holder.lockCanvas();

        // "Borramos" el fondo.
        this.canvas.drawColor(0xFFFFFFFF); // ARGB
        drawRectangle(0, 0, scaleWidth, scaleHeight, true, colorBackground);
    }

    //Permite cambiar el color de fondo de la escena
    public void setColorBackground(int newColor) {
        colorBackground = newColor;
    }

    //Desbloquea el canvas para que se pueda renderizar el frame
    public void clear() {
        this.holder.unlockCanvasAndPost(canvas);
    }

    public void paintCell(int x, int y, int w, int h, int celltype, int palleteColor) {
        int c;
        if (celltype == 1) {
            //Si hemos seteado una nueva paleta...
            if (palleteColor != -1) {
                //Seteamos el color deseado
                c = palleteColor;
            } else {
                c = 0xFF0000FF;
            }
        } else if (celltype == 3) {
            c = 0xFFFF0000;
        } else if (celltype == 0) {
            c = 0xFF808080;
        }
        //none and blank
        else {
            c = 0xFF000000;
        }
        resetColor = this.paint.getColor();
        this.paint.setColor(c);

        //Distinto renderizado dependiendo de si es una celda azul, roja o tachada
        if (celltype == -1 || celltype == 2) {

            float stroke = this.paint.getStrokeWidth();
            this.paint.setStyle(Paint.Style.STROKE);

            this.paint.setStrokeWidth(3);
            this.canvas.drawRect(x * getWidth() / (float)scaleWidth, y * getHeight() / (float)scaleHeight, (x + w) * getWidth() / (float)scaleWidth, (y + h) * getHeight() / (float)scaleHeight, this.paint);
            //Cuadrado de la interfaz
            if (celltype == 2) {
                this.canvas.drawLine(x * getWidth() / (float)scaleWidth, y * getHeight() / (float)scaleHeight, (w + x) * getWidth() / (float)scaleWidth, (h + y) * getHeight() / (float)scaleHeight, this.paint);
            }
            this.paint.setStrokeWidth(stroke);

        } else {
            //Cambiar para que tenga en cuenta las dimensiones de la ventana, los últimos dos valores son el ancho y alto
            this.paint.setStyle(Paint.Style.FILL);
            this.canvas.drawRect(x * getWidth() / (float)scaleWidth, y * getHeight() / (float)scaleHeight, (x + w) * getWidth() / (float)scaleWidth, (y + h) * getHeight() / (float)scaleHeight, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
        }
        this.paint.setColor(resetColor);
    }

    //Dibuja un circulo del color especificado
    public void drawCircle(float x, float y, float r, String color) {
        int c;
        switch (color) {
            case "blue":
                c = 0xFF0000FF;
                break;
            case "red":
                c = 0xFFFF0000;
                break;
            case "purple":
                c = 0xFF6960EC;
                break;
            default:
                c = 0xFFFFFFFF;
                break;
        }

        this.paint.setColor(c);
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawCircle((x + r)* getWidth() / scaleWidth, (y + r)* getHeight() / scaleHeight, r* getWidth() / scaleWidth, this.paint);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(Color.BLACK);
        this.canvas.drawCircle((x + r)* getWidth() / scaleWidth, (y + r)* getHeight() / scaleHeight, r* getWidth() / scaleWidth, this.paint);

    }


    //Dimensiones del surface
    public int getWidth() {
        return this.myView.getWidth();
    }

    public int getHeight() {
        return this.myView.getHeight();
    }

    //Cambia el color de la pintura
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    //Asigna la estructura assets donde estan cargados todos los assets del juego
    public void setAssetContext(AssetManager assetsAux) {
        this.assets = assetsAux;
    }

    //Añade una imagen al hashmap
    public void newImage(String imageName, String path) {
        images.put(imageName, new ImageAndroid(this.assets, path));
    }

    //Añade una fuente al hashmap
    public void newFont(String fontName, String path, int type) {
        try {
            fonts.put(fontName, new Font_Android(path, type, this.assets));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Dibuja un bitmap, este bitmap debe de estar cargado previamente en el hashmap de imagenes
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String imageAux) {
        ImageAndroid image = images.get(imageAux);
        Bitmap map = null;
        if (image != null) {
            map = image.getImage();
        }
        Bitmap scaledMap = Bitmap.createScaledBitmap(map, desiredWidth * getWidth() / scaleWidth, desiredHeight * getHeight() / scaleHeight, false);
        canvas.drawBitmap(scaledMap, x * getWidth() / (float)scaleWidth, y * getHeight() / (float)scaleHeight, this.paint);
    }

    //Dibuja un rectangulo en una posicion especifica, tamaño y color
    public void drawRectangle(int x, int y, int w, int h, boolean fill, int color) {
        resetColor = this.paint.getColor();
        setColor(color);

        if (!fill)
            this.canvas.drawRect(x * getWidth() / (float)scaleWidth, y * getHeight() / (float)scaleHeight, (x + w) * getWidth() / (float)scaleWidth, (y + h) * getHeight() / (float)scaleHeight, this.paint);
        else {
            this.paint.setStyle(Paint.Style.FILL);
            this.canvas.drawRect(x * getWidth() / (float)scaleWidth, y * getHeight() / (float)scaleHeight, (x + w) * getWidth() / (float)scaleWidth, (y + h) * getHeight() / (float)scaleHeight, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
        }
        this.paint.setColor(resetColor);
    }

    //Dibuja texto en pantalla
    //AlignType:
    //-1 Alineamiento a la izquierda
    //0 Alineamiento en el centro
    //1 Alineamiento a la derecho
    public void drawText(String text, int x, int y, String color, String fontAux, int alignType, int size) {
        int prevColor = this.paint.getColor();
        Font_Android font = this.fonts.get(fontAux);
        int f = size*getWidth()/scaleWidth;
        this.paint.setTextSize(f);
        if (font != null) {
            this.paint.setTypeface(font.getFont());
        }

        switch (alignType) {
            case 0:
                this.paint.setTextAlign(Paint.Align.CENTER);
                break;
            case 1:
                this.paint.setTextAlign(Paint.Align.RIGHT);
                break;
            case -1:
                this.paint.setTextAlign(Paint.Align.LEFT);
                break;
            default:
                break;
        }

        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int currentColor;
        if (color == "red") {
            currentColor = 0xFFFF0000;
        } else {
            currentColor = 0xFF000000;
        }
        this.paint.setColor(currentColor);
        this.canvas.drawText(text, (float) x * getWidth() / scaleWidth, (float) y * getHeight() / scaleHeight, this.paint);
        this.paint.setColor(prevColor);
        this.paint.setStyle(Paint.Style.STROKE);
    }

}
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

    //TODO Aqui guarda la relacion
    int scaleWidth, scaleHeight;


    private final SurfaceView myView;
    private final SurfaceHolder holder;
    private Canvas canvas;
    private final Paint paint;

    int resetColor;
    int colorBackground;

    HashMap<String, Font_Android> fonts;
    HashMap<String, ImageAndroid> images;

    private AssetManager assets;

    public RenderAndroid(SurfaceView myView) {
        // Intentamos crear el buffer strategy con 2 buffers.
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFF000000);

//        this.frameSize = new Vector2D(720, 1080);

        this.fonts = new HashMap<>();
        this.images = new HashMap<>();

        colorBackground = 0XFFFFFFFF;
        //Por defecto la escala es 1000x1000 pero creamos un setter por si alguien quiere alguna modificacion
        scaleHeight = 1000;
        scaleWidth = 1000;
    }

    public void prepareFrame() {
        // Pintamos el framefsefsefdfs
        while (!this.holder.getSurface().isValid()) ;
        this.canvas = this.holder.lockCanvas();

        // "Borramos" el fondo.
        this.canvas.drawColor(0xFFFFFFFF); // ARGB
//        this.canvas.translate(0, this.posCanvas.getY());
        drawRectangle(0, 0, scaleWidth, scaleHeight, true, colorBackground);
    }

    public void setColorBackground(int newColor) {
        colorBackground = newColor;
    }

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


    public int getWidth() {
        return this.myView.getWidth();
    }

    public int getHeight() {
        return this.myView.getHeight();
    }

//    public void scaleAppView() {
//        //obtenemos el tamaño del frame y lo guardamos como copia para la escala
//        Vector2D surfaceFrame = new Vector2D(this.holder.getSurfaceFrame().width(), this.holder.getSurfaceFrame().height());
//        Vector2D scale = new Vector2D(surfaceFrame.getX() / frameSize.getX(), surfaceFrame.getY() / frameSize.getY());
//
//        if (scale.getX() * this.factorScale < scale.getY()) scale.setY(scale.getX() / factorScale);
//        else
//            scale.setX(scale.getY() / factorScale);
//
//        posCanvas.set((int) (surfaceFrame.getX() - frameSize.getX()), (int) (surfaceFrame.getY() - frameSize.getY()) / 2 * factorScale);
//
//    }

//    public void setFrameSize() {
//        while (this.holder.getSurfaceFrame().width() == 0) ;
//        this.frameSize = new Vector2D(this.myView.getWidth(), this.myView.getHeight());
//        if (this.screenLayout.getWidth() - this.myView.getHeight() * (4.0 / 6.0) <= 0) {
//            this.myView.setLeft(0);
//            this.myView.setRight(this.screenLayout.getRight());
//        } else {
//            this.myView.setLeft((this.screenLayout.getWidth() - (int) (this.myView.getHeight() * (4.0 / 6.0))) / 2);
//            this.myView.setRight((this.screenLayout.getWidth() - (int) (this.myView.getHeight() * (4.0 / 6.0))) / 2 + (int) (this.myView.getHeight() * (4.0 / 6.0)));
//        }
//
//
//    }


    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public void setAssetContext(AssetManager assetsAux) {
        this.assets = assetsAux;
    }


    public void newImage(String imageName, String path) {
        images.put(imageName, new ImageAndroid(this.assets, path));
    }


    public void newFont(String fontName, String path, int type) {
        try {
            fonts.put(fontName, new Font_Android(path, type, this.assets));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String imageAux) {
        ImageAndroid image = images.get(imageAux);
        Bitmap map = null;
        if (image != null) {
            map = image.getImage();
        }
        Bitmap scaledMap = Bitmap.createScaledBitmap(map, desiredWidth * getWidth() / scaleWidth, desiredHeight * getHeight() / scaleHeight, false);
        canvas.drawBitmap(scaledMap, x * getWidth() / (float)scaleWidth, y * getHeight() / (float)scaleHeight, this.paint);
    }


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

    //AlignType:
    //-1 Alineamiento a la izquierda
    //0 Alineamiento en el centro
    //1 Alineamiento a la derecho
    public void drawText(String text, int x, int y, String color, String fontAux, int alignType,int size) {
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
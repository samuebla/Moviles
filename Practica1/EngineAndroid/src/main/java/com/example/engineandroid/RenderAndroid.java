package com.example.engineandroid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.lib.IFont;
import com.example.lib.IGraphics;
import com.example.lib.IImage;

import java.io.File;
import java.util.HashMap;

public class RenderAndroid implements IGraphics {

    private SurfaceView myView;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;

    HashMap<String,Font_Android> fonts;
    HashMap<String,ImageAndroid> images;

    private AssetManager assets;

//    private float scale = 1.5f;

    public RenderAndroid(SurfaceView myView) {
        // Intentamos crear el buffer strategy con 2 buffers.
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFF000000);

        this.fonts = new HashMap<>();
        this.images = new HashMap<>();
    }

    public void prepareFrame() {
        // Pintamos el frame
        while (!this.holder.getSurface().isValid()) ;
        this.canvas = this.holder.lockCanvas();
    }

    public void render() {
        // "Borramos" el fondo.
        this.canvas.drawColor(0xFFFFFFFF); // ARGB
    }

    public void clear() {
        this.holder.unlockCanvasAndPost(canvas);
    }

    public void paintCell(int x, int y, int w, int h, int celltype) {
        int c;
        if (celltype == 1) {
            c = 0xFF0000FF;
        } else if (celltype == 3) {
            c = 0xFFFF0000;
        } else if (celltype == 0) {
            c = 0xFF808080;
        }
        //none and blank
        else {
            c = 0xFF000000;
        }
        this.paint.setColor(c);

        if (celltype == -1 || celltype == 2) {

            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(3);
            this.canvas.drawRect(x, y, x + w, y + h, this.paint);
            //Cuadrado de la interfaz
            if (celltype == 2) {
                this.canvas.drawLine(x, y, w + x, h + y, this.paint);
            }
            this.paint.setStrokeWidth(1);

        } else {
            //Cambiar para que tenga en cuenta las dimensiones de la ventana, los últimos dos valores son el ancho y alto
            this.paint.setStyle(Paint.Style.FILL);
            this.canvas.drawRect(x, y, x + w, y + h, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
        }
    }

    public void pintarCirculo(float x, float y, float r, String color) {
        int c;
        if (color == "blue") {
            c = 0xFF0000FF;
        } else if (color == "red") {
            c = 0xFFFF0000;
        } else {
            c = 0xFFFFFFFF;
        }
        this.paint.setColor(c);
        this.canvas.drawCircle(x, y, r, this.paint);
    }

    public int getWidth() {
        return this.myView.getWidth();
    }

    public int getHeight() {
        return this.myView.getHeight();
    }

    @Override
    public void setResolution(double width, double height) {
        this.canvas.scale((float) width, (float) height);
    }

    @Override
    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public void setAssetContext(AssetManager assetsAux){
        this.assets = assetsAux;
    }

    @Override
    public IImage newImage(String imageName,String path) {
        return images.put(imageName,new ImageAndroid(this.assets, path));
    }

    @Override
    public IFont newFont(String fontName,String path,int type, int size) {
        try{
            return fonts.put(fontName,new Font_Android(path,type,size, this.assets));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String imageAux) {
        ImageAndroid image = images.get(imageAux);
        Bitmap map = image.getImage();
        Bitmap scaledMap = Bitmap.createScaledBitmap(map, (int)(desiredWidth), (int)(desiredHeight), false);
        canvas.drawBitmap(scaledMap, (int)(x), (int)(y), this.paint);
    }

    @Override
    public void drawRectangle(int x, int y, int w, int h, boolean fill) {
        this.canvas.drawRect(x, y, x + w, y + h, this.paint);
    }

    @Override
    public void fillRectangle(int x, int y, int w, int h) {
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(x, y, x + w, y + h, this.paint);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void drawLine(int x, int y, int w, int h) {
        this.canvas.drawLine(x, y, x + w, y + h, this.paint);
    }

    @Override
    public void drawText(String text, int x, int y, String color, String fontAux) {
        int prevColor = this.paint.getColor();
        Font_Android font = this.fonts.get(fontAux);
        this.paint.setTextSize(font.getSize());
        int currentColor;
        if (color == "red"){
            currentColor = 0xFFFF0000;
        } else{
            currentColor = 0xFF000000;
        }
        this.paint.setColor(currentColor);
        //AAAAAAAAAAAAA Aplicar la fuente

        this.canvas.drawText(text, (float)x, (float)y, this.paint);
        this.paint.setColor(prevColor);
    }

}

package com.example.engineandroid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.lib.IFont;
import com.example.lib.IGraphics;
import com.example.lib.IImage;
import com.example.lib.Vector2D;

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

    private Vector2D posCanvas;
    private Vector2D frameSize;
    private float factorScale;

    public RenderAndroid(SurfaceView myView, float scale) {
        // Intentamos crear el buffer strategy con 2 buffers.
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFF000000);

        this.frameSize = new Vector2D(720, 1080);
        this.posCanvas = new Vector2D();

        this.fonts = new HashMap<>();
        this.images = new HashMap<>();

        this.factorScale = scale;
    }

    public void prepareFrame() {
        // Pintamos el framefsefsefdfs
        while (!this.holder.getSurface().isValid()) ;
        this.canvas = this.holder.lockCanvas();
        // "Borramos" el fondo.
        this.canvas.drawColor(0xFFFFFFFF); // ARGB
        this.canvas.translate(0, this.posCanvas.getY());
        setColor(0XFFFFFFFF);
        drawRectangle(0,0, this.getViewWidth(), (int)(this.frameSize.getY()/factorScale), true);
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
            //Cambiar para que tenga en cuenta las dimensiones de la ventana, los ??ltimos dos valores son el ancho y alto
            this.paint.setStyle(Paint.Style.FILL);
            this.canvas.drawRect(x, y, x + w, y + h, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
        }
    }

    public void drawCircle(float x, float y, float r, String color) {
        int c;
        if (color == "blue") {
            c = 0xFF0000FF;
        } else if (color == "red") {
            c = 0xFFFF0000;
        }else if(color == "purple"){
            c = 0xFF6960EC;
        }
        else {
            c = 0xFFFFFFFF;
        }

        this.paint.setColor(c);
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawCircle(x+r, y+r, r, this.paint);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(Color.BLACK);
        this.canvas.drawCircle(x+r, y+r, r, this.paint);

    }

    @Override
    public int getWidth() {
        return (int)(this.frameSize.getX() + posCanvas.getX());
    }
    @Override
    public int getHeight() {
        return (int)(this.frameSize.getY() + posCanvas.getY());
    }

    public int getViewWidth() {
        return this.myView.getWidth();
    }

    public int getViewHeight() {
        return this.myView.getHeight();
    }

    public void scaleAppView(){
        while(this.holder.getSurfaceFrame().width() == 0);
        //obtenemos el tama??o del frame y lo guardamos como copia para la escala
        Vector2D surfaceFrame = new Vector2D(this.holder.getSurfaceFrame().width(), this.holder.getSurfaceFrame().height());
        Vector2D scale = new Vector2D(surfaceFrame.getX()/frameSize.getX(), surfaceFrame.getY()/frameSize.getY());

        if(scale.getX() * this.factorScale < scale.getY()) scale.setY(scale.getX()/factorScale);
        else
            scale.setX(scale.getY()/factorScale);

        posCanvas.set((int)(surfaceFrame.getX()-frameSize.getX()), (int)(surfaceFrame.getY()-frameSize.getY())/2*factorScale);

    }


    public Vector2D getOffset() {
        return this.posCanvas;
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
        if(!fill)
            this.canvas.drawRect(x, y, x + w, y + h, this.paint);
        else{
            this.paint.setStyle(Paint.Style.FILL);
            this.canvas.drawRect(x, y, x + w, y + h, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    public void drawLine(int x, int y, int w, int h) {
        this.canvas.drawLine(x, y, x + w, y + h, this.paint);
    }

    //AlignType:
    //-1 Alineamiento a la izquierda
    //0 Alineamiento en el centro
    //1 Alineamiento a la derecho
    @Override
    public void drawText(String text, int x, int y, String color, String fontAux, int alignType) {
        int prevColor = this.paint.getColor();
        Font_Android font = this.fonts.get(fontAux);
        int f = (int)(font.getSize() /factorScale);
        this.paint.setTextSize(f);
        this.paint.setTypeface(font.getFont());

        switch (alignType){
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
        if (color == "red"){
            currentColor = 0xFFFF0000;
        } else{
            currentColor = 0xFF000000;
        }
        this.paint.setColor(currentColor);

        this.canvas.drawText(text, (float)x, (float)y, this.paint);
        this.paint.setColor(prevColor);
        this.paint.setStyle(Paint.Style.STROKE);
    }

}

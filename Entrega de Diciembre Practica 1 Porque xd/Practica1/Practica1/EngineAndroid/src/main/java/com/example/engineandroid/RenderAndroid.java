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

import java.util.HashMap;

public class RenderAndroid implements IGraphics {

    private final SurfaceView myView;
    private final SurfaceHolder holder;
    private Canvas canvas;
    private final Paint paint;

    HashMap<String,Font_Android> fonts;
    HashMap<String,ImageAndroid> images;

    private final AssetManager assets;

    private final Vector2D posCanvas;
    private final float factorScale;
    //ANCHO Y ALTO REAL DE LA PANTALLA
    Vector2D surfaceFrame;

    //Escala logica del juego, por defecto 400 x 600, lo que significa que el usuario puede colocar elementos entre las posiciones 0 y 1000
    //De esta manera los elementos se escalan automaticamente al tamaño de la pantalla
    private final int scaleWidth;
    private final int scaleHeight;

    //Factor por el que se tiene que escalar el canvas para pasar de 400x600 a los pixeles reales
    private float scaleX;
    private float scaleY;

    //Informs about the amount of top or left margin
    private int leftMargin;
    private int topMargin;

    //Size of the canvas after the resize
    private int canvasWidth;
    private int canvasHeight;

    public RenderAndroid(SurfaceView myView, float scale) {
        // Intentamos crear el buffer strategy con 2 buffers.
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFF000000);

        this.posCanvas = new Vector2D();
        this.surfaceFrame = new Vector2D();

        this.fonts = new HashMap<>();
        this.images = new HashMap<>();

        this.factorScale = scale;

        this.assets = myView.getContext().getAssets();

        scaleWidth = 400;
        scaleHeight = 600;
    }

    //Convierte una coordenada del juego a coordenada del view
    public Vector2D convertLogicCoordsToView(Vector2D logicCoords){
        return new Vector2D(logicCoords.getX() * scaleX + leftMargin, logicCoords.getY() * scaleY + topMargin);
    }

    //Convierte un tamaño del juego a un tamaño del view
    public Vector2D convertLogicSizeToView(Vector2D logicSize){
        return new Vector2D(logicSize.getX() * scaleX, logicSize.getY() * scaleY);
    }

    public void prepareFrame() {
        // Pintamos el framefsefsefdfs
        while (!this.holder.getSurface().isValid()) ;
        this.canvas = this.holder.lockCanvas();
        // "Borramos" el fondo.
        this.canvas.drawColor(0xFFFFFFFF); // ARGB
        this.canvas.translate(this.posCanvas.getX(), this.posCanvas.getY());
        canvas.scale(scaleX,scaleY);
        setColor(0XFFFFFFFF);
        drawRectangle(0,0, scaleWidth, scaleHeight, true);
    }

    public void clear() {
        this.holder.unlockCanvasAndPost(canvas);
    }

    @Override
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
            this.canvas.drawRect(x, y, (x + w), (y + h), this.paint);
            //Cuadrado de la interfaz
            if (celltype == 2) {
                this.canvas.drawLine(x, y, (w + x), (h + y), this.paint);
            }
            this.paint.setStrokeWidth(1);

        } else {
            //Cambiar para que tenga en cuenta las dimensiones de la ventana, los últimos dos valores son el ancho y alto
            this.paint.setStyle(Paint.Style.FILL);
            this.canvas.drawRect(x, y, (x + w), (y + h), this.paint);
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
        this.canvas.drawCircle((x+r), (y+r), r, this.paint);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(Color.BLACK);
        this.canvas.drawCircle((x+r), (y+r), r, this.paint);

    }

    @Override
    public int getWidth() {
        return canvasWidth;
    }

    public int getViewWidth() {
        return this.myView.getWidth();
    }
    @Override
    public int getHeight() {
        return canvasHeight;
    }

    public void scaleAppView(){
        while(this.holder.getSurfaceFrame().width() == 0);
        //obtenemos el tamaño del frame y lo guardamos como copia para la escala
        surfaceFrame = new Vector2D(this.holder.getSurfaceFrame().width(), this.holder.getSurfaceFrame().height());


        if (surfaceFrame.getX() / surfaceFrame.getY() > factorScale){
            //Si es mas ancho que alto para la relacion de aspecto especificada
            leftMargin = (int)((surfaceFrame.getX() - surfaceFrame.getY()*factorScale)/2.0f);
            topMargin = 0;
            posCanvas.set(leftMargin, myView.getY());
            //Factor de escalado para el dibujado
            scaleX = (surfaceFrame.getY()*factorScale) / scaleWidth;
            scaleY = surfaceFrame.getY() / scaleHeight;
            //Nuevos width y height del canvas
            canvasWidth = (int)(surfaceFrame.getY()*factorScale);
            canvasHeight = (int) surfaceFrame.getY();
        }else{
            //Si es mas alto que ancho para la relacion de aspecto especificada
            leftMargin = 0;
            topMargin = (int)((surfaceFrame.getY() - surfaceFrame.getX()/factorScale)/2.0f);
            posCanvas.set((int)(myView.getX()), topMargin);
            //Factor de escalado para el dibujado
            scaleX = surfaceFrame.getX() / scaleWidth;
            scaleY = (surfaceFrame.getX()/factorScale) / scaleHeight;
            //Nuevos width y height del canvas
            canvasWidth = (int)surfaceFrame.getX();
            canvasHeight = (int) (surfaceFrame.getX()/factorScale);
        }

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

    @Override
    public IImage newImage(String imageName,String path) {
        return images.put(imageName,new ImageAndroid(this.assets, path));
    }

    @Override
    public IFont newFont(String fontName,String path,int type) {
        try{
            return fonts.put(fontName,new Font_Android(path,type, this.assets));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String imageAux) {
        ImageAndroid image = images.get(imageAux);
        Bitmap map = image.getImage();
        Bitmap scaledMap = Bitmap.createScaledBitmap(map,desiredWidth, desiredHeight, false);
        canvas.drawBitmap(scaledMap, x, y, this.paint);
    }

    @Override
    public void drawRectangle(int x, int y, int w, int h, boolean fill) {
        if(!fill)
            this.canvas.drawRect(x, y, (x + w), (y + h), this.paint);
        else{
            this.paint.setStyle(Paint.Style.FILL);
            this.canvas.drawRect(x, y, (x + w), (y + h), this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    public void drawLine(int x, int y, int w, int h) {
        this.canvas.drawLine(x, y, (x + w), (y + h), this.paint);
    }

    //AlignType:
    //-1 Alineamiento a la izquierda
    //0 Alineamiento en el centro
    //1 Alineamiento a la derecho
    @Override
    public void drawText(String text, int x, int y, String color, String fontAux, int alignType,int size) {
        int prevColor = this.paint.getColor();
        Font_Android font = this.fonts.get(fontAux);
        int f = (int) (size);

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

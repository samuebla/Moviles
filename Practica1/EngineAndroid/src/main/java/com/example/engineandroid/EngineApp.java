package com.example.engineandroid;

import com.example.lib.*;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class EngineApp implements Engine,Runnable{

    private SurfaceView myView;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;

    private Thread renderThread;

    private boolean running;

    private Scene scene;

    public EngineApp(SurfaceView myView){
        // Intentamos crear el buffer strategy con 2 buffers.
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(0xFF000000);
    }


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

    @Override
    public void paintCell(int x, int y, int w, int h, int celltype){
        int c;
        if(celltype == 1){
            c = 0xFF0000FF;
        } else if(celltype == 3){
            c = 0xFFFF0000;
        } else if(celltype == 0){
            c = 0xFF808080;
        }
        //none and blank
        else{
            c = 0xFF000000;
        }
        this.paint.setColor(c);

        if (celltype== -1 || celltype == 2){

            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(3);
            this.canvas.drawRect(x,y,x+w,y+h,this.paint);
            //Cuadrado de la interfaz
            if (celltype == 2){
                this.canvas.drawLine(x,y,w+x,h+y,this.paint);
            }
            this.paint.setStrokeWidth(1);

        }else{
            //Cambiar para que tenga en cuenta las dimensiones de la ventana, los últimos dos valores son el ancho y alto
            this.paint.setStyle(Paint.Style.FILL);
            this.canvas.drawRect(x,y,x+w,y+h,this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
        }
    }


    //<<< API >>>
//    public void pintarCirculo(float x, float y, float r, String color){
//        int c;
//        if(color == "blue"){
//            c = 0xFF0000FF;
//        } else if(color == "red"){
//            c = 0xFFFF0000;
//        } else {
//            c = 0xFFFFFFFF;
//        }
//        this.paint.setColor(c);
//        this.canvas.drawCircle(x, y, r, this.paint);
//    }
//
//    public int getWidth(){
//        return this.myView.getWidth();
//    }
//    public int getHeight(){
//        return this.myView.getHeight();
//    }
    //<< Fin API>>

    //<<Motor>>

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    //blucle principal
    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(this.running && this.myView.getWidth() == 0);
        // Espera activa. Sería más elegante al menos dormir un poco.

        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        // Bucle de juego principal.
        while(running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
            this.update(elapsedTime);
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            // Pintamos el frame
            while (!this.holder.getSurface().isValid());
            this.canvas = this.holder.lockCanvas();
            this.render();
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    protected void update(double deltaTime) {
        this.scene.update(deltaTime);
    }

    protected void render() {
        // "Borramos" el fondo.
        this.canvas.drawColor(0xFFFFFFFF); // ARGB
        this.scene.render();
    }

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
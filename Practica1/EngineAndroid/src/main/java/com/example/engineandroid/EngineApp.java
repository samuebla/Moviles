package com.example.engineandroid;

import com.example.lib.*;

import android.content.res.AssetManager;
import android.view.SurfaceView;

public class EngineApp implements Engine,Runnable{

    private SurfaceView view;

    private RenderAndroid render;
    private InputAndroid input;
    private IEventHandler eventHandler;
    private AudioAndroid audioMngr;

    private Thread renderThread;

    private boolean running;

    private SceneMngrAndroid sceneMngr;

    public EngineApp(SurfaceView myView){
        this.view = myView;
        this.render = new RenderAndroid(this.view, 4.0f/6.0f);
        this.eventHandler = new IEventHandler() {
            @Override
            public IEvent getEvent() {
                return event;
            }

            @Override
            public void sendEvent(EventType type) {
                event.eventType = type;
            }
        };
        this.input = new InputAndroid(this.eventHandler);
        this.view.setOnTouchListener(this.input.getTouchListener());

        this.audioMngr = new AudioAndroid();
    }


    @Override
    public IGraphics getGraphics(){
        return render;
    }
    @Override
    public IAudio getAudio(){
        return audioMngr;
    }

    //<<Input>>
    @Override
    public Input getInput(){
        return input;
    }

    @Override
    public IEventHandler getEventMngr() {
        return eventHandler;
    }

    @Override
    public int getWidth() {
        int w = this.render.getWidth();
        return w;
    }

    @Override
    public int getHeight() {
        return this.render.getHeight();
    }
    //<<Fin Input>>

    @Override
    public void paintCell(int x, int y, int w, int h, int celltype){
        this.render.paintCell(x, y, w, h, celltype);
    }

    public void setAssetsContext(AssetManager assetsAux){
        this.render.setAssetContext(assetsAux);
        this.audioMngr.setAssetsManager(assetsAux);
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

    @Override
    public void drawText(String text, int x, int y, String color, String font){
        this.render.drawText(text, x, y, color,font);
    }

    @Override
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String image){
        this.render.drawImage(x, y, desiredWidth, desiredHeight, image);
    }

    @Override
    public void setScene(Scene newScene) {
        this.sceneMngr.pushScene(newScene);
    }

    @Override
    public void setSceneMngr(ISceneMngr sceneMngrAux){this.sceneMngr = (SceneMngrAndroid) sceneMngrAux;}

    @Override
    public void popScene() {
        this.sceneMngr.popScene();
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
        while(this.running && this.render.getWidth() == 0);

        //Escalado de la app
        this.render.scaleAppView();
        this.input.setOffset(0,this.render.getOffset().getY());
        this.sceneMngr.getScene().init();
        // Espera activa. Sería más elegante al menos dormir un poco.

        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        long actualTime = System.currentTimeMillis();

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

            long deltaTime = System.currentTimeMillis() - actualTime;
            actualTime += deltaTime;

            this.sceneMngr.update(deltaTime / 1000.0);

            //Renderizado
            this.render.prepareFrame();
            this.sceneMngr.render();
            this.render.clear();
        }
    }

    protected void update(double deltaTime) {
        this.sceneMngr.update(deltaTime);
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
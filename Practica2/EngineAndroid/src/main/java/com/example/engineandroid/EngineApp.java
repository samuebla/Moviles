package com.example.engineandroid;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.SurfaceView;
import android.widget.LinearLayout;

public class EngineApp implements Runnable {

    private SurfaceView view;

    private RenderAndroid render;
    private InputAndroid input;
    private EventHandler eventHandler;
    private AudioAndroid audioMngr;
    private AdManager adManager;

    private Thread renderThread;

    private boolean running;

    private SceneMngrAndroid sceneMngr;

    private Context context;

    private Scene primaryScene;

    public EngineApp(SurfaceView myView, LinearLayout screenLayout, Activity mainActivity){
        this.view = myView;



        this.render = new RenderAndroid(this.view, 4.0f/6.0f, screenLayout);
        this.eventHandler = new EventHandler();
        this.input = new InputAndroid(this.eventHandler);
        this.view.setOnTouchListener(this.input.getTouchListener());
        this.view.setOnLongClickListener(this.input.getLongTouchListener());
        this.audioMngr = new AudioAndroid();

        this.render.setAssetContext(myView.getContext().getAssets());
        this.audioMngr.setAssetsManager(myView.getContext().getAssets());

        this.context = myView.getContext();

        this.sceneMngr = new SceneMngrAndroid();

        this.adManager = new AdManager(mainActivity);

    }

    public void restart(SurfaceView newView, LinearLayout newScreenLayout){
        this.view = newView;
        this.render.restart(newView, newScreenLayout);

        this.view.setOnTouchListener(this.input.getTouchListener());
        this.view.setOnLongClickListener(this.input.getLongTouchListener());

        this.render.setAssetContext(newView.getContext().getAssets());
        this.audioMngr.setAssetsManager(newView.getContext().getAssets());

        this.context = newView.getContext();
    }


    public RenderAndroid getGraphics() {
        return render;
    }

    public AudioAndroid getAudio() {
        return audioMngr;
    }

    public AdManager getAdManager() { return adManager; }

    //<<Input>>
    public InputAndroid getInput() {
        return input;
    }

    public EventHandler getEventMngr() {
        return eventHandler;
    }

    //<<Fin Input>>

    public SceneMngrAndroid getSceneMngr() { return sceneMngr; }

    public Context getContext() {
        return this.context;
    }

    //<<< API >>>

    public void setColorBackground(int newColor) {
        this.render.setColorBackground(newColor);
    }

    public void setPrimaryScene(Scene sceneAux){ this.primaryScene = sceneAux;}

    //blucle principal
    public void run() {
        if (renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while (this.running && this.render.getWidth() == 0) ;



        //Escalado de la app
//        this.render.setFrameSize();
//        this.render.scaleAppView();
        this.input.setOffset(0, this.render.getOffset().getY());
        this.primaryScene.loadResources(this);
        this.sceneMngr.getScene().init();
        // Espera activa. Sería más elegante al menos dormir un poco.

        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        long actualTime = System.currentTimeMillis();

//        System.out.println(this.render.getViewWidth());
//        System.out.println(this.render.getViewWidth());
//        System.out.println(this.render.getViewWidth());
//        System.out.println(this.render.getViewWidth());
//        System.out.println(this.render.getViewWidth());

        // Bucle de juego principal.
        while(running) {
//            this.render.setFrameSize();
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            long deltaTime = System.currentTimeMillis() - actualTime;
            actualTime += deltaTime;

            this.sceneMngr.update(deltaTime / 1000.0, this.adManager);

            //Renderizado
            this.render.prepareFrame();
            this.sceneMngr.render(this.render);
            this.render.clear();
        }
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

    //Closes the thread and returns the id of the last scene in the app
//    public int onForcedClose(){
//        if (this.running) {
//            this.running = false;
//            while (true) {
//                try {
//                    this.renderThread.join();
//                    this.renderThread = null;
//                    return this.sceneMngr.handleClosed();
//                } catch (InterruptedException ie) {
//                    // Esto no debería ocurrir nunca...
//                    return -1;
//                }
//            }
//        }else
//            return -1;
//    }

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
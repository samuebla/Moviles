package com.example.engineandroid;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceView;
import android.widget.LinearLayout;

public class EngineApp implements Runnable {

    private final RenderAndroid render;
    private final InputAndroid input;
    private final EventHandler eventHandler;
    private final AudioAndroid audioMngr;
    private final AdManager adManager;

    private Thread renderThread;

    private boolean running;

    private final SceneMngrAndroid sceneMngr;

    private final Context context;

    private Scene primaryScene;

    public EngineApp(SurfaceView myView, Activity mainActivity){
        this.render = new RenderAndroid(myView);
        this.eventHandler = new EventHandler();
        this.input = new InputAndroid(this.eventHandler);
        myView.setOnTouchListener(this.input.getTouchListener());
        myView.setOnLongClickListener(this.input.getLongTouchListener());
        this.audioMngr = new AudioAndroid();

        this.render.setAssetContext(myView.getContext().getAssets());
        this.audioMngr.setAssetsManager(myView.getContext().getAssets());

        this.context = myView.getContext();

        this.sceneMngr = new SceneMngrAndroid();

        this.adManager = new AdManager(mainActivity);

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

        //Cargamos los recursos del juego
        //Se tiene que hacer por aqui para que el render ya este inicializado
        this.primaryScene.loadResources(this);
        this.sceneMngr.getScene().init();

        long informePrevio = System.nanoTime(); // Informes de FPS
        int frames = 0;

        long actualTime = System.currentTimeMillis();

        // Bucle de juego principal.
        while(running) {
//            this.render.setFrameSize();
            long currentTime = System.nanoTime();

            // Informe de FPS
            if (currentTime - informePrevio > 1000000000L) {
                long fps = frames * 1000000000L / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            long deltaTime = System.currentTimeMillis() - actualTime;
            actualTime += deltaTime;

            //Update de la escena
            this.sceneMngr.update(deltaTime / 1000.0, this.adManager);

            //AAAAAAAAAAAAAAAAA Añadir aqui handleInput de la escena

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
package com.example.engineandroid;

import com.example.lib.*;

import android.view.SurfaceView;

public class EngineApp implements Engine,Runnable{

    private final SurfaceView view;

    private final RenderAndroid render;
    private final InputAndroid input;
    private final IEventHandler eventHandler;
    private final AudioAndroid audioMngr;

    private Thread renderThread;

    private boolean running;

    private final SceneMngrAndroid sceneMngr;

    private Scene resourceScene;

    public EngineApp(SurfaceView myView){
        this.view = myView;
        //Inicialización del render con la relacion de aspecto de la pantalla
        this.render = new RenderAndroid(this.view, 4.0f/6.0f);

        this.eventHandler = new EventHandlerAndroid();
        this.input = new InputAndroid(this.eventHandler, this.render);
        //Redefinicion del listener de tocar en la pantalla para poder procesar el input que necesitamos
        this.view.setOnTouchListener(this.input.getTouchListener());

        this.audioMngr = new AudioAndroid(myView.getContext().getAssets());

        this.sceneMngr = new SceneMngrAndroid();
    }


    @Override
    public IGraphics getGraphics(){
        return render;
    }
    @Override
    public IAudio getAudio(){
        return audioMngr;
    }

    @Override
    public SceneMngrAndroid getSceneMngr() { return sceneMngr; }

    //<<Input>>
    @Override
    public Input getInput(){
        return input;
    }

    @Override
    public IEventHandler getEventMngr() {
        return eventHandler;
    }
    //<<Fin Input>>

    //<<< API >>>

    //Sets the scene that's loading the resources of the game
    @Override
    public void setResourceScene(Scene scene){
        this.resourceScene = scene;
    }

    //bucle principal
    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(this.running && this.render.getViewWidth() == 0);

        //Escalado de la app
        this.render.scaleAppView();
        //Loads resources such as sound, images, etc
        this.resourceScene.loadResources(this);
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
            if (currentTime - informePrevio > 1000000000L) {
                long fps = frames * 1000000000L / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            long deltaTime = System.currentTimeMillis() - actualTime;
            actualTime += deltaTime;

            this.sceneMngr.update(deltaTime / 1000.0);
            //si hay algun evento de input, lo procesamos
            if (eventHandler.getEvent().eventType != EventHandlerAndroid.EventType.NONE) {
                sceneMngr.handleInput(eventHandler.getEvent().eventType, audioMngr,input);
                eventHandler.sendEvent(EventHandlerAndroid.EventType.NONE);
            }

            //Renderizado
            this.render.prepareFrame();
            this.sceneMngr.render(render);
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
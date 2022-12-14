package com.example.enginedesktop;

import com.example.lib.*;

import javax.swing.JFrame;

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class EngineDesktop implements Engine,Runnable{
    private Thread renderThread;
    private boolean running;

    private final RenderDesktop render;
    private final SceneMngrDesktop sceneManager;

    private final InputDesktop input;
    private final EventHandlerDesktop eventHandler;
    private final AudioDesktop audioMngr;

    private Scene resourceScene;

    public EngineDesktop(final JFrame myView){
        this.render = new RenderDesktop(myView);
        //Event handler que detecta los eventos de raton
        this.eventHandler = new EventHandlerDesktop();
        this.input = new InputDesktop(this.render, this.eventHandler);
        //Pone nuestro listener a la ventana para que nos lleguen los eventos
        myView.addMouseListener(this.input.getListener());

        this.audioMngr = new AudioDesktop();
        this.sceneManager = new SceneMngrDesktop();
    }

    //<<Partes Motor>>
    @Override
    public IGraphics getGraphics(){
        return this.render;
    }
    @Override
    public IAudio getAudio(){
        return audioMngr;
    }

    @Override
    public ISceneMngr getSceneMngr() { return sceneManager; }

    //<<Fin Partes Motor>>

    //<<Input>>
    public Input getInput(){
        return input;
    }

    @Override
    public IEventHandler getEventMngr() {
        return eventHandler;
    }
    //<<Fin Input>>

    //Sets the scene that's loading the resources of the game
    @Override
    public void setResourceScene(Scene scene){
        this.resourceScene = scene;
    }

    //Hebra principal
    @Override
    public void run() {
        if (this.renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(this.running && this.render.getWidth() == 0);
        // Espera activa. Sería más elegante al menos dormir un poco.

//        long lastFrameTime = System.nanoTime();

//        long informePrevio = lastFrameTime; // Informes de FPS
//        int frames = 0;

        //Loads resources such as sound, images, etc
        this.resourceScene.loadResources(this);

        long actualTime = System.currentTimeMillis();

        // Bucle de juego principal.
        while(running) {
//            long currentTime = System.nanoTime();

//            // Informe de FPS
//            if (currentTime - informePrevio > 1000000000l) {
//                long fps = frames * 1000000000l / (currentTime - informePrevio);
//                System.out.println("" + fps + " fps");
//                frames = 0;
//                informePrevio = currentTime;
//            }
//            ++frames;

            //Calculo del delta time para el update
            long deltaTime = System.currentTimeMillis() - actualTime;
            actualTime += deltaTime;


            this.sceneManager.update(deltaTime / 1000.0);
            //Si hay algun evento de input lo procesamos
            if (eventHandler.getEvent().eventType != EventHandlerDesktop.EventType.NONE) {
                sceneManager.handleInput(eventHandler.getEvent().eventType, audioMngr, input);
                eventHandler.sendEvent(EventHandlerDesktop.EventType.NONE);
            }

            //Bucle de renderizado
            do{
                this.render.initFrame();
                this.sceneManager.render(render);
                this.render.clearFrame();
            } while(this.render.swap());

        }
    }


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
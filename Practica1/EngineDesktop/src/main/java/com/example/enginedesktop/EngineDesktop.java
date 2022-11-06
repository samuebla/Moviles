package com.example.enginedesktop;

import com.example.lib.*;

import javax.swing.JFrame;

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class EngineDesktop implements Engine,Runnable{
    private Thread renderThread;
    private boolean running;

    private RenderDesktop render;
    private SceneMngrDesktop sceneManager;

    private InputDesktop input;
    private IEventHandler eventHandler;

    public EngineDesktop(final JFrame myView){
        this.render = new RenderDesktop(myView);
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
        this.input = new InputDesktop(this.eventHandler);
        myView.addMouseListener(this.input.getListener());
    }

//    //<<< API >>>

    //EMPTY(gray) = 0
    //SELECTED(blue) = 1
    //CROSSED(crossed) = 2
    //WRONG(red) = 3
    //blank = marco vacio para la interfaz = -1
    @Override
    public void paintCell(int x, int y, int w, int h, int celltype){
        this.render.paintCell(x, y, w, h, celltype);
    }

    @Override
    public void drawText(String text, int x, int y, String color, IFont font){
        this.render.drawText(text, x, y, color,font);
    }

//    @Override
//    public void addComponent(Component aux){
//        myView.add(aux);
//    }


    public int getWidth(){
        return this.render.getWidth();
    }
    public int getHeight(){
        return this.render.getHeight();
    }
    //<< Fin API>>

    //<<Motor>>
    @Override
    public IGraphics getGraphics(){
        return this.render;
    }
    @Override
    public IAudio getAudio(){
        return null;
    }
    @Override
    public IState getState(){
        return null;
    }
    //<<Fin Motor>>

    @Override
    public void setScene(Scene newScene){
        this.sceneManager.pushScene(newScene);
    }

    @Override
    public void setSceneMngr(ISceneMngr sceneMngrAux){this.sceneManager = (SceneMngrDesktop) sceneMngrAux;}

    @Override
    public void popScene(){ this.sceneManager.popScene();}


    //<<Input>>
    public Input getInput(){
        return input;
    }

    @Override
    public IEventHandler getEventMngr() {
        return eventHandler;
    }
    //<<Fin Input>>

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
//            this.update(elapsedTime);
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            long deltaTime = System.currentTimeMillis() - actualTime;
            actualTime += deltaTime;


            this.sceneManager.update(deltaTime / 1000.0);

            //Bucle de renderizado
            do{
                this.render.initFrame();
                this.sceneManager.render();
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



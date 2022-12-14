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
    private AudioDesktop audioMngr;

    public EngineDesktop(final JFrame myView){
        this.render = new RenderDesktop(myView);
        //Event handler que detecta los eventos de raton
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

        this.audioMngr = new AudioDesktop();
    }

    //Pinta una celda del tablero
    //EMPTY(gray) = 0
    //SELECTED(blue) = 1
    //CROSSED(crossed) = 2
    //WRONG(red) = 3
    //blank = marco vacio para la interfaz = -1
    @Override
    public void paintCell(int x, int y, int w, int h, int celltype){
        this.render.paintCell(x, y, w, h, celltype);
    }

    //AlignType:
    //-1 Alineamiento a la izquierda
    //0 Alineamiento en el centro
    //1 Alineamiento a la derecho
    @Override
    public void drawText(String text, int x, int y, String color, String font, int alignType){
        this.render.drawText(text, x, y, color,font, alignType);
    }

    @Override
    public void drawCircle(float x, float y, float r, String color) {
        this.render.drawCircle(x,y,r,color);
    }

    @Override
    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String image){
        this.render.drawImage(x, y, desiredWidth, desiredHeight, image);
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

    //<<Fin Partes Motor>>

    @Override
    public int getWidth(){
        return this.render.getWidth();
    }
    @Override
    public int getHeight(){
        return this.render.getHeight();
    }

    //Cambia la escena activa
    @Override
    public void setScene(Scene newScene){
        this.sceneManager.pushScene(newScene);
    }

    //Guarda el sceneManager
    @Override
    public void setSceneMngr(ISceneMngr sceneMngrAux){this.sceneManager = (SceneMngrDesktop) sceneMngrAux;}

    //Vuelve a la escena anterior del stack
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

    //Hebra principal
    @Override
    public void run() {
        if (this.renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programaci??n defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy r??pido, la vista podr??a todav??a no estar inicializada.
        while(this.running && this.render.getWidth() == 0);
        // Espera activa. Ser??a m??s elegante al menos dormir un poco.

//        long lastFrameTime = System.nanoTime();

//        long informePrevio = lastFrameTime; // Informes de FPS
//        int frames = 0;
        this.sceneManager.getScene().init();

        long actualTime = System.currentTimeMillis();

        // Bucle de juego principal.
        while(running) {
            long currentTime = System.nanoTime();

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

            //Bucle de renderizado
            do{
                this.render.initFrame();
                //Actualizo el posicionamiento para el input
                this.input.setScaleFactor((float)this.render.getScale());
                this.input.setOffset(this.render.getMargins());

                this.sceneManager.render();
                this.render.clearFrame();
            } while(this.render.swap());

        }
    }


    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos est??bamos ejecutando ya
            // (programaci??n defensiva)
            this.running = true;
            // Lanzamos la ejecuci??n de nuestro m??todo run() en un nuevo Thread.
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
                    // Esto no deber??a ocurrir nunca...
                }
            }
        }
    }
}



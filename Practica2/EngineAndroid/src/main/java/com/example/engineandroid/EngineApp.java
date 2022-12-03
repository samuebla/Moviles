package com.example.engineandroid;

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

    private Thread renderThread;

    private boolean running;

    private SceneMngrAndroid sceneMngr;

    private Context context;

    public EngineApp(SurfaceView myView, LinearLayout screenLayout){
        this.view = myView;
        this.render = new RenderAndroid(this.view, 4.0f/6.0f, screenLayout);
        this.eventHandler = new EventHandler();
        this.input = new InputAndroid(this.eventHandler);
        this.view.setOnTouchListener(this.input.getTouchListener());
        this.view.setOnLongClickListener(this.input.getLongTouchListener());
        this.audioMngr = new AudioAndroid();
    }


    public RenderAndroid getGraphics() {
        return render;
    }

    public AudioAndroid getAudio() {
        return audioMngr;
    }

    //<<Input>>
    public InputAndroid getInput() {
        return input;
    }

    public EventHandler getEventMngr() {
        return eventHandler;
    }

    public int getWidth() {
        int w = this.render.getWidth();
        return w;
    }

    public int getHeight() {
        return this.render.getHeight();
    }
    //<<Fin Input>>

    public void paintCell(int x, int y, int w, int h, int celltype,int palleteColor) {
        this.render.paintCell(x, y, w, h, celltype,palleteColor);
    }

    public void setAssetsContext(AssetManager assetsAux) {
        this.render.setAssetContext(assetsAux);
        this.audioMngr.setAssetsManager(assetsAux);
    }

    public void setBaseContext(Context con) {
        this.context = con;
    }

    public Context getContext() {
        return this.context;
    }

    //<<< API >>>

    //AlignType:
    //-1 Alineamiento a la izquierda
    //0 Alineamiento en el centro
    //1 Alineamiento a la derecho
    public void drawText(String text, int x, int y, String color, String font, int alignType) {
        this.render.drawText(text, x, y, color, font, alignType);
    }

    public void drawCircle(float x, float y, float r, String color) {
        this.render.drawCircle(x, y, r, color);
    }

    public void drawImage(int x, int y, int desiredWidth, int desiredHeight, String image) {
        this.render.drawImage(x, y, desiredWidth, desiredHeight, image);
    }
    public void drawRectangle(int x, int y, int w, int h,boolean fill,int color) {
        this.render.drawRectangle(x, y, h,w,true,color);
    }

    public void setScene(Scene newScene) {
        this.sceneMngr.pushScene(newScene);
    }

    public void setSceneMngr(SceneMngrAndroid sceneMngrAux) {
        this.sceneMngr = sceneMngrAux;
    }

    public void setColorBackground(int newColor) {
        this.render.setColorBackground(newColor);
    }

    public void popScene() {
        this.sceneMngr.popScene();
    }

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
        this.render.setFrameSize();
        this.render.scaleAppView();
        this.input.setOffset(0, this.render.getOffset().getY());
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
            this.render.setFrameSize();
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

    public void updateSurfaceSize(){
        this.render.setFrameSize();
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
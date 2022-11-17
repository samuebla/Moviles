package com.example.logica;

public class HistoryModeMenu implements Scene {

    private Button themeButtonMode;
    private Button dificultyButtonMode;

    private Button backButton;

    private Engine engine;

    public HistoryModeMenu(Engine engineAux){

        this.engine = engineAux;

        init();
    }

    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX() >= pos.getX() && coords.getX() <= pos.getX() + size.getX() &&
                coords.getY() >= pos.getY() && coords.getY() <= pos.getY() + size.getY());
    }

    public void init() {
        //Botones selectores del nivel
        this.themeButtonMode = new Button(engine.getWidth()/4  - engine.getWidth()/8, engine.getHeight()/2, engine.getWidth()/4, engine.getHeight()/6);
        this.dificultyButtonMode = new Button(engine.getWidth()*3/4 - engine.getWidth()/8, engine.getHeight()/2, engine.getWidth()/4, engine.getHeight()/6);
        this.backButton = new Button(engine.getWidth()/72 + engine.getWidth()/44, engine.getHeight()/22, engine.getWidth()/10, engine.getHeight()/15);
    }

    public void update(double deltaTime){
        //Para los eventos...
        if(engine.getEventMngr().getEvent().eventType != IEventHandler.EventType.NONE) {
            handleInput();
            engine.getEventMngr().sendEvent(IEventHandler.EventType.NONE);
        }
    }

    public void render(){
        //ThemeMode
        this.engine.drawImage((int)this.themeButtonMode.getPos().getX(), (int)this.themeButtonMode.getPos().getY(),(int)this.themeButtonMode.getSize().getX(),(int)this.themeButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Temático", (int)(themeButtonMode.getPos().getX() + themeButtonMode.getSize().getX()/2), (int)(themeButtonMode.getPos().getY() - themeButtonMode.getSize().getY()*0.5), "Black","Amor", 0);

        //DificultyMode
        this.engine.drawImage((int)this.dificultyButtonMode.getPos().getX(), (int)this.dificultyButtonMode.getPos().getY(),(int)this.dificultyButtonMode.getSize().getX(),(int)this.dificultyButtonMode.getSize().getY(),"PlayButton");
        this.engine.drawText("Dificultad", (int)(dificultyButtonMode.getPos().getX() + dificultyButtonMode.getSize().getX()/2), (int)(dificultyButtonMode.getPos().getY() - dificultyButtonMode.getSize().getY()*0.5), "Black","Amor", 0);

        //Back Button
        this.engine.drawImage(engine.getWidth()/72, engine.getHeight()/22,(int)(backButton.getSize().getX()),(int)(backButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.drawText("Selecciona el modo de Juego", (int)(engine.getWidth()/2), (int)(engine.getHeight()/5.4), "Black", "Amor", 0);
    }

    public void handleInput(){
        //ThemeMode
        if (inputReceived(this.themeButtonMode.getPos(), this.themeButtonMode.getSize())){
            MyScene playScene = new MyScene(this.engine, 5, 5);
            this.engine.setScene(playScene);
        }

        //DificultyMode
        if (inputReceived(this.dificultyButtonMode.getPos(), this.dificultyButtonMode.getSize())){
            MyScene playScene = new MyScene(this.engine, 8, 8);
            this.engine.setScene(playScene);
        }

        //Back button
        if (inputReceived(this.backButton.getPos(), this.backButton.getSize())){
            this.engine.popScene();
        }
    }
}

package com.example.practica1;

import com.example.engineandroid.AdManager;
import com.example.engineandroid.EngineApp;
import com.example.engineandroid.EventHandler;
import com.example.engineandroid.Scene;
import com.example.engineandroid.Vector2D;
import android.widget.Button;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

public class HistoryModeMenu implements Scene, Serializable {

    //TODO Aqui guarda la relacion
    int scaleWidth, scaleHeight;

    private InputButton themeInputButtonMode;
    private InputButton dificultyInputButtonMode;

    private InputButton backInputButton;

    AtomicReference<Integer> coins;
    private Integer coinSize;
    int[] unlockedThemedLevels;

    private AtomicReference<Integer>[] progress;
    private AtomicReference<Integer>[] palettes;

    private EngineApp engine;

    public HistoryModeMenu(EngineApp engineAux, AtomicReference<Integer> coinsAux, AtomicReference<Integer>[] progressAux,AtomicReference<Integer>[] palettesAux){

        this.engine = engineAux;

        this.coins = coinsAux;
        this.progress = progressAux;
        this.palettes = palettesAux;

        //Por defecto la escala es 1000x1000 pero creamos un setter por si alguien quiere alguna modificacion
        scaleHeight=1000;
        scaleWidth=1000;

        init();
    }

    @Override
    public boolean inputReceived(Vector2D pos, Vector2D size){
        Vector2D coords = new Vector2D();
        coords.set(engine.getInput().getScaledCoords().getX(), engine.getInput().getScaledCoords().getY());

        return (coords.getX()*scaleWidth/engine.getGraphics().getWidth() >= pos.getX()  && coords.getX()*scaleWidth/engine.getGraphics().getWidth() <= pos.getX() + size.getX() &&
                coords.getY()*scaleHeight/engine.getGraphics().getHeight() >= pos.getY() && coords.getY()*scaleHeight/engine.getGraphics().getHeight() <= pos.getY() + size.getY());
    }

    @Override
    public void init() {
        coinSize = scaleWidth/10;
//        loadCoinsFromFile();
        //Botones selectores del nivel
        this.themeInputButtonMode = new InputButton(scaleWidth/4  - scaleWidth/8, scaleHeight/2, scaleWidth/4, scaleHeight/6);
        this.dificultyInputButtonMode = new InputButton(scaleWidth*3/4 - scaleWidth/8, scaleHeight/2, scaleWidth/4, scaleHeight/6);
        this.backInputButton = new InputButton(10,10, scaleWidth/10, scaleHeight/15);
    }

    @Override
    public void loadResources(EngineApp engineAux) {

    }

    @Override
    public void update(double deltaTime, AdManager adManager){
        //Para los eventos...
        if(engine.getEventMngr().getEventType() != EventHandler.EventType.NONE) {
            handleInput(engine.getEventMngr().getEventType(), adManager);
            engine.getEventMngr().sendEvent(EventHandler.EventType.NONE);
        }
    }

    @Override
    public void render(){
        //ThemeMode
        this.engine.getGraphics().drawImage((int)this.themeInputButtonMode.getPos().getX(), (int)this.themeInputButtonMode.getPos().getY(),(int)this.themeInputButtonMode.getSize().getX(),(int)this.themeInputButtonMode.getSize().getY(),"ThemePlay");
        this.engine.getGraphics().drawText("Temático", (int)(themeInputButtonMode.getPos().getX() + themeInputButtonMode.getSize().getX()/2), (int)(themeInputButtonMode.getPos().getY() - themeInputButtonMode.getSize().getY()*0.5), "Black","Cooper", 0);

        //DificultyMode
        this.engine.getGraphics().drawImage((int)this.dificultyInputButtonMode.getPos().getX(), (int)this.dificultyInputButtonMode.getPos().getY(),(int)this.dificultyInputButtonMode.getSize().getX(),(int)this.dificultyInputButtonMode.getSize().getY(),"ChallengePlay");
        this.engine.getGraphics().drawText("Dificultad", (int)(dificultyInputButtonMode.getPos().getX() + dificultyInputButtonMode.getSize().getX()/2), (int)(dificultyInputButtonMode.getPos().getY() - dificultyInputButtonMode.getSize().getY()*0.5), "Black","Cooper", 0);

        //Back Button
        this.engine.getGraphics().drawImage((int) backInputButton.getPos().getX(), (int) backInputButton.getPos().getY(),(int)(backInputButton.getSize().getX()),(int)(backInputButton.getSize().getY()), "Back");

        //Texto indicativo
        this.engine.getGraphics().drawText("Selecciona el modo de Juego", (int)(scaleWidth/2), (int)(scaleHeight/5.4), "Black", "Cooper", 0);

        //Moneda
        //MONEDAS
        this.engine.getGraphics().drawText(Integer.toString(coins.get()),scaleWidth - coinSize-scaleWidth/100, (int)(scaleHeight/72 + coinSize/2.5 ), "Black", "CooperBold", 1);
        this.engine.getGraphics().drawImage(scaleWidth-coinSize -scaleWidth/100, (int)scaleHeight/72,coinSize,coinSize/2,"Coin");
    }

    @Override
    public void handleInput(EventHandler.EventType type, AdManager adManager){
        //ThemeMode
        if (inputReceived(this.themeInputButtonMode.getPos(), this.themeInputButtonMode.getSize())){
            ThemeModeMenu playScene = new ThemeModeMenu(this.engine, this.coins, this.progress,this.palettes);
            this.engine.getSceneMngr().pushScene(playScene);
        }

        //DificultyMode
        if (inputReceived(this.dificultyInputButtonMode.getPos(), this.dificultyInputButtonMode.getSize())){
            QuickGameScene playScene = new QuickGameScene(this.engine, 8, 8);
            this.engine.getSceneMngr().pushScene(playScene);
        }

        //Back button
        if (inputReceived(this.backInputButton.getPos(), this.backInputButton.getSize())){
            this.engine.getSceneMngr().popScene();
        }
    }

    //Se llama cuando la escena posterior se elimina y se vuelve aqui, por si hay que actualizar algo
    @Override
    public void onResume() {
        init();
    }

//    private void loadCoinsFromFile() {
//        try {
//            //Carga de archivo
//            String receiveString = "";
//            try {//Comprobar si existe en el almacenamiento interno
//                FileInputStream fis = this.engine.getContext().openFileInput("saveData");
//                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                while (bufferedReader.ready()) {
//                    receiveString += bufferedReader.readLine();
//                }
//                inputStreamReader.close();
//            } catch (FileNotFoundException e) { //Si no existe, crea un nuevo archivo en almacenamiento interno como copia desde assets
//                e.printStackTrace();
//                InputStreamReader inputStreamReader = new InputStreamReader(this.engine.getContext().getAssets().open("files/" + "saveData"));
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                while (bufferedReader.ready()) {
//                    receiveString += bufferedReader.readLine();
//                }
//
//                inputStreamReader.close();
//                //Copia del fichero
//                FileOutputStream fos = this.engine.getContext().openFileOutput("saveData", Context.MODE_PRIVATE);
//                fos.write(receiveString.getBytes());
//                fos.close();
//            }
//            //Carga el numero de monedas
//            String[] fileRead;
//            fileRead = receiveString.split(" ");
//            //Monedas
//            coins = Integer.parseInt(fileRead[0]);
//            //Niveles
//            for(int i = 1; i < fileRead.length;i++)
//                unlockedThemedLevels[i-1] = Integer.parseInt(fileRead[i]);
//
//        }
//        catch (
//                FileNotFoundException e) {
//            Log.e("Error", "File not found: " + e.toString());
//        } catch (
//                IOException e) {
//            Log.e("Reading Error", "Can not read file: " + e.toString());
//        }
//
//    }

    //Y aqui el guardado, recomiendo que este metodo lo pongamos aqui y podamos acceder a el desde todas las escenas para
    //que cada desbloqueo y cada transaccion de monedas se guarde al instante y no se tenga que salir
    //Tambien habria que hacer un getter en esta clase para saber cuantas monedas y niveles tienes
//    public void saveDataHistoryMode(int coins_, int idTheme, int unlockedLevels){   //Idtheme siempre debe ser desde 1
//        try {
//            FileOutputStream fos = this.engine.getContext().openFileOutput("saveData", Context.MODE_PRIVATE);
//            String writer = "";
//            //Monedas
//            if(coins_ >= 0)
//                writer += coins_ + " \n";
//            else
//                writer += coins + " \n";
//
//            //Nivel desbloqueado
//            if(idTheme > 0){
//                unlockedThemedLevels[idTheme] = unlockedLevels;
//            }
//            //Escribimos los niveles
//            for(int i = 1; i < unlockedThemedLevels.length;i++)
//                writer += unlockedThemedLevels[i];
//
//            fos.write(writer.getBytes(StandardCharsets.UTF_8));
//            fos.close();
//        } catch (FileNotFoundException e) {
//            Log.e("Error", "File not found: " + e.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}

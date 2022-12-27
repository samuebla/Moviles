package com.example.engineandroid;

import static android.graphics.Typeface.*;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.example.lib.IFont;

import java.io.IOException;

public class Font_Android implements IFont {

    Typeface font;
    int size;

    public Font_Android(String filePath, int type, AssetManager assets) throws IOException {
        //Abrimos el asset de la fuente y creamos una fuente base
        String newFilePath = filePath.replaceAll("assets/", "");
        assets.open(newFilePath);
        Typeface baseFont = Typeface.createFromAsset(assets, newFilePath);

        //Cambiamos la base para poder crear fuentes que esten en negrita, cursiva o normal
        switch (type) {
            //NEGRITA
            case 1:
                font = Typeface.create(baseFont, BOLD);
                break;
            //ITALICA
            case 2:
                font = Typeface.create(baseFont, ITALIC);
                break;
            //NORMAL
            default:
                font = Typeface.create(baseFont, NORMAL);
                break;
        }
    }

    public Typeface getFont(){
        return font;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void setSize(int type, int size) {
        this.size = size;
    }

    @Override
    public boolean isBold() {
        return font.isBold();
    }

    @Override
    public boolean isItalic() {
        return font.isItalic();
    }
}

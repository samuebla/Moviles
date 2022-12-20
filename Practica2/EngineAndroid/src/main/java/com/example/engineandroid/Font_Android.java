package com.example.engineandroid;

import static android.graphics.Typeface.*;

import android.content.res.AssetManager;
import android.graphics.Typeface;


import java.io.IOException;

//Almacena la información de la fuente, presente de esta manera para compatibilidad con practica 1
public class Font_Android {

    Typeface font;

    public Font_Android(String filePath, int type, AssetManager assets) throws IOException {
        String newFilePath = filePath.replaceAll("assets/", "");
        assets.open(newFilePath);
        Typeface baseFont = Typeface.createFromAsset(assets, newFilePath);
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

    public Typeface getFont() {
        return font;
    }
}

package com.example.engineandroid;

import static android.graphics.Typeface.*;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontStyle;


import java.io.IOException;

public class Font_Android{

    Typeface font;
    int size;

    public Font_Android(String filePath, int type, int sizeAux, AssetManager assets) throws IOException {
        String newFilePath = filePath.replaceAll("assets/", "");
        assets.open(newFilePath);
        Typeface baseFont = Typeface.createFromAsset(assets, newFilePath);
        size = sizeAux;

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

    public int getSize() {
        return this.size;
    }

    public void setSize(int type, int size) {
        this.size = size;
    }

    public boolean isBold() {
        return font.isBold();
    }

    public boolean isItalic() {
        return font.isItalic();
    }
}

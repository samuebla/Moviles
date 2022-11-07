package com.example.engineandroid;

import static android.graphics.Typeface.*;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontStyle;

import com.example.lib.IFont;

import java.io.IOException;

public class Font_Android implements IFont {

    Typeface font;
    int size;

    public Font_Android(String filePath, int type, int sizeAux, AssetManager assets) throws IOException {
        assets.open(filePath);
        Typeface baseFont = Typeface.createFromAsset(assets, filePath);
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

    @Override
    public int getSize() {
        //Esto creo que esta bien :D
        return this.size;
    }

    @Override
    public void setSize(int type, int size) {
//        switch (type){
//            //NEGRITA
//            case 1:
//                font = font.deriveFont(Font.BOLD, size);
//                break;
//            //ITALICA
//            case 2:
//                font = font.deriveFont(Font.ITALIC, size);
//                break;
//            //No se cual es esta la verdad
//            default:
//                font = font.deriveFont(Font.TRUETYPE_FONT, size);
//                break;
//        }
    }

    @Override
    public boolean isBold() {
        //Algo mal hay por aqui me huele extraño
        return font.isBold();
    }

    @Override
    public boolean isItalic() {
        return font.isItalic();
    }
}

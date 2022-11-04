package com.example.engineandroid;

import android.graphics.fonts.Font;
import android.graphics.fonts.FontStyle;

import com.example.lib.IFont;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Font_Android implements IFont {

    Font font;

    public Font_Android(File file, int type, int size){

        try {
            InputStream is = new FileInputStream(file);
//            font = Font.Builder(file);
            //CREO QUE VOY A QUITAR ESTA PUTA BASURA
            switch (type){
                //NEGRITA
                case 1:
//                    font = font.deriveFont(Font.BOLD, 40);
                    break;
                //ITALICA
                case 2:
//                    font = font.deriveFont(Font.ITALIC, 40);
                    break;
                //No se cual es esta la verdad
                default:
//                    font = font.deriveFont(Font.TRUETYPE_FONT, 40);
                    break;
            }
        }
        catch(IOException e) {
            //It tells you what happened and where in the code this happened.
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        //Esto creo que esta bien :D
        return font.getStyle().getWeight();
    }

    @Override
    public boolean isBold() {
        //Algo mal hay por aqui me huele extraño
        return font.getStyle().getWeight() == FontStyle.FONT_WEIGHT_BOLD;
    }

    @Override
    public boolean isItalic() {
        return font.getStyle().getSlant() == FontStyle.FONT_SLANT_ITALIC;
    }
}

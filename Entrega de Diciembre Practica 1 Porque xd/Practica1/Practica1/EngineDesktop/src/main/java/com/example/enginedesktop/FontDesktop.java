package com.example.enginedesktop;

import com.example.lib.IFont;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FontDesktop implements IFont {

    private Font font;

    public FontDesktop(File file, int type){
        //Intenta inicializar la fuente del fichero especificado
        try {
            InputStream is = new FileInputStream(file);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch(IOException | FontFormatException e) {
            //Te devuelve el recorrido de llamadas en caso de que de error
            e.printStackTrace();
        }
    }

    public Font getFont() { return font; }

    @Override
    public int getSize() {
        return font.getSize();
    }

    //Cambia el tamano y el tipo de la fuente
    @Override
    public void setSize(int type,int size){
        switch (type){
            //NEGRITA
            case 1:
                font = font.deriveFont(Font.BOLD, size);
                break;
            //ITALICA
            case 2:
                font = font.deriveFont(Font.ITALIC, size);
                break;
            //No se cual es esta la verdad
            default:
                font = font.deriveFont(Font.TRUETYPE_FONT, size);
                break;
        }
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

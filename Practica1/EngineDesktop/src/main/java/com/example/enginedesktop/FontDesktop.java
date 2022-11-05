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

    public FontDesktop(File file, int type, int size){
        try {
            InputStream is = new FileInputStream(file);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            switch (type){
                //NEGRITA
                case 1:
                    font = font.deriveFont(Font.BOLD, 40);
                    break;
                //ITALICA
                case 2:
                    font = font.deriveFont(Font.ITALIC, 40);
                    break;
                //No se cual es esta la verdad
                default:
                    font = font.deriveFont(Font.TRUETYPE_FONT, 40);
                    break;
            }
        }
        catch(IOException | FontFormatException e) {
            //It tells you what happened and where in the code this happened.
            e.printStackTrace();
        }
    }

    public Font getFont() { return font; }

    @Override
    public int getSize() {
        return font.getSize();
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

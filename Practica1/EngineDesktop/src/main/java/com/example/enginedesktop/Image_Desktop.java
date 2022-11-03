package com.example.enginedesktop;

import com.example.lib.IImage;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image_Desktop implements IImage {

    private Image image;

    public Image_Desktop(File file){
        try{
            image = ImageIO.read(file);
        }
        catch (IOException e){
            //It tells you what happened and where in the code this happened
            //Eso dice google tia yo que se
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        //Ni puta idea de qe cojones tengo que pasarle
        return image.getWidth(null);
    }

    @Override
    public int getHeight() {
        return image.getHeight(null);
    }
}

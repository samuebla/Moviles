package com.example.enginedesktop;

import com.example.lib.IImage;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDesktop implements IImage {

    private Image image;

    public ImageDesktop(File file){
        //Intenta leer la imagen del archivo
        try{
            image = ImageIO.read(file);
        }
        catch (IOException e){
            //Te devuelve la cola de llamadas
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        //Al parecer el parametro adicional es un observer que no necesitamos de momento
        return image.getWidth(null);
    }

    @Override
    public int getHeight() {
        return image.getHeight(null);
    }

    public Image getImage(){
        return this.image;
    }
}

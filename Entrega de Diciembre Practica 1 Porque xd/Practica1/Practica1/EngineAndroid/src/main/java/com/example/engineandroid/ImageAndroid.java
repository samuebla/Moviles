package com.example.engineandroid;

import com.example.lib.IImage;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class ImageAndroid implements IImage {

    Bitmap image;

    public ImageAndroid(AssetManager assets, String path) {
        //Carga de la imagen generando un bitmap que sera el utilizado para renderizarla
        String newFilePath = path.replaceAll("assets/", "");
        try {
            InputStream input = assets.open(newFilePath);
            image = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}

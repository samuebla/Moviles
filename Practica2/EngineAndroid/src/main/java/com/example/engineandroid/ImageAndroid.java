package com.example.engineandroid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

//Carga de la imagen por bitmap
public class ImageAndroid {

    Bitmap image;

    public ImageAndroid(AssetManager assets, String path) {
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
}

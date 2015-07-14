package com.textures;


import android.os.Environment;

public class Constants {

    public static final String TEXTURES = "/Textures";

    public static String IMAGE_FOLDER = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES) + TEXTURES;

    public static String FILE_FOLDER = Environment.getExternalStorageDirectory()+ TEXTURES;
    public static  class Parameters{
        private Parameters(){
        }
        public static String IMAGE_NAME="imageName";
    }
}

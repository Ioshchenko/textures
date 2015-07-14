package com.textures.camera;


import android.hardware.Camera;
import android.util.Log;

import com.textures.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraFunctions implements Camera.PictureCallback {

    private static final String TAG = "CameraFunctions";

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFile = FileUtils.getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
}

package com.textures.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.textures.Constants;
import com.textures.R;
import com.textures.services.OpenCVService;

import java.io.File;
import java.io.IOException;

public class ImageDetectionActivity extends  BaseActivity {

    private OpenCVService openCVService;
    private String mockImagePath = Constants.IMAGE_FOLDER + File.separator + "test1.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detection);
        loadImage(mockImagePath,R.id.originalImage);
    }

    @Override
    public void onResume() {
        super.onResume();
        openCVService = new OpenCVService(this);
        openCVService.initial();
    }


    private void loadImage(String imagePath, int id){
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageProcessing = (ImageView) findViewById(id);
            imageProcessing.setImageBitmap(bitmap);
        }
    }

    public void runDetection(View view) throws IOException {
        String result = openCVService.detectPaper(mockImagePath);
        loadImage(result,R.id.detectImage);
    }
}
package com.textures.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.textures.Constants;
import com.textures.R;
import com.textures.services.OpenCVService;

import java.io.File;

public class ImageDetectionActivity extends BaseActivity {

    private OpenCVService openCVService;
    private String mockImagePath = Constants.IMAGE_FOLDER + File.separator + "test1.jpg";
    private Bitmap bitmap;
    private ImageView imageProcessing;
    private int color = Color.rgb(149, 229, 213);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detection);
        imageProcessing = (ImageView) findViewById(R.id.canvasImage);

        Intent intent = getIntent();
        String imageName = intent
                .getStringExtra(Constants.Parameters.IMAGE_NAME);

        if (imageName != null) {
            convertImageToCanvas(Constants.IMAGE_FOLDER + File.separator + imageName);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        openCVService = new OpenCVService(this);
        openCVService.initial();
    }


    private void convertImageToCanvas(String imagePath) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
        }
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);

        imageProcessing.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }

    public void back(View view) {
        this.finish();
    }


}
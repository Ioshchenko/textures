package com.textures.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import com.textures.Constants;
import com.textures.R;
import com.textures.component.CanvasImageView;
import com.textures.convertors.CanvasPointPixelConvertor;
import com.textures.services.OpenCVService;

import java.io.File;

public class ImageDetectionActivity extends BaseActivity {

    private OpenCVService openCVService;
    private String mockImagePath = Constants.IMAGE_FOLDER + File.separator + "test1.jpg";
    private Bitmap bitmap;
    private CanvasImageView canvasImageView;
    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detection);
        canvasImageView = (CanvasImageView) findViewById(R.id.canvasImage);

        Intent intent = getIntent();
        String imageName = intent
                .getStringExtra(Constants.Parameters.IMAGE_NAME);

        if (imageName != null) {
            imagePath = imageName;
            convertImageToCanvas(imageName);
        } else {
            imagePath = mockImagePath;
            convertImageToCanvas(mockImagePath);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        openCVService = new OpenCVService(this);
        openCVService.initial();
    }

    public void back(View view) {
        this.finish();
    }

    public void crop(View view) {
        CanvasPointPixelConvertor helper = new CanvasPointPixelConvertor(canvasImageView);
        String resultImage = Constants.IMAGE_FOLDER + File.separator + "temp.jpg";
        openCVService.perspectiveTransformation(imagePath, resultImage, helper);
    }

    private void convertImageToCanvas(String imagePath) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
        }
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(bitmap, 0, 0, null);

        canvasImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
        scaleImageView(tempBitmap);
    }

    private void scaleImageView(Bitmap tempBitmap) {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        double scale = (double) tempBitmap.getWidth() / (double) width;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, (int) (tempBitmap.getHeight() / scale));
        canvasImageView.setLayoutParams(parms);
        canvasImageView.setScale(scale);
    }

}
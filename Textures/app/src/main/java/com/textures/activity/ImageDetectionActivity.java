package com.textures.activity;

import android.os.Bundle;

import com.textures.R;
import com.textures.services.OpenCVService;

public class ImageDetectionActivity extends  BaseActivity {

    private OpenCVService openCVService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detection);

    }

    @Override
    public void onResume() {
        super.onResume();
        openCVService = new OpenCVService(this);
        openCVService.initial();
    }
}
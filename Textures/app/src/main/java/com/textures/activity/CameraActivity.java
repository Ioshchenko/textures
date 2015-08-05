package com.textures.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.textures.Constants;
import com.textures.R;
import com.textures.camera.CameraFunctions;
import com.textures.camera.CameraPreview;


public class CameraActivity extends BaseActivity {

    private static String TAG = "CameraActivity";

    private Camera camera;
    private CameraPreview cameraPreview;
    private CameraFunctions cameraFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initCamera();

        ImageButton captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, cameraFunctions);
            }
        });


        FileObserver observer = new FileObserver(Constants.IMAGE_FOLDER) {
            @Override
            public void onEvent(int event, String file) {
                if (event == FileObserver.CREATE) {
                    startImageDetection(file);
                }
            }
        };
        observer.startWatching();
    }

    private void initCamera() {
        camera = getCameraInstance();
        cameraPreview = new CameraPreview(this, camera);
        cameraFunctions = new CameraFunctions();

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);
    }


    private void startImageDetection(String imageName) {
        Intent intent = new Intent(this, ImageDetectionActivity.class);
        intent.putExtra(Constants.Parameters.IMAGE_NAME, imageName);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
        FrameLayout parentPreview = (FrameLayout) findViewById(R.id.camera_preview);
        parentPreview.removeView(cameraPreview);
        cameraPreview = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (camera == null) {
            initCamera();
        }
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
            camera.setParameters(populateCameraParameters(camera));
            camera.setDisplayOrientation(getCameraDisplayOrientation());
        } catch (Exception e) {
            Log.e(TAG, "Camera is not available", e);
        }
        return camera;
    }

    private Camera.Parameters populateCameraParameters(Camera camera) {
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        params.setRotation(getCameraDisplayOrientation());
        return params;
    }

    private int getCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);

        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

}

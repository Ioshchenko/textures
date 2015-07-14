package com.textures.camera;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class CameraEventReciver extends BroadcastReceiver {
    private static String TAG = "CameraEventReciver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"start");
        Cursor cursor = context.getContentResolver().query(intent.getData(), null, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
        Log.d(TAG, imagePath);
    }
}

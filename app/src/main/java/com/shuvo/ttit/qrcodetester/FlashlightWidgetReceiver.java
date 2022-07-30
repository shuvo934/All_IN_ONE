package com.shuvo.ttit.qrcodetester;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class FlashlightWidgetReceiver extends BroadcastReceiver {

    boolean state = false;
    public static final String SHARED_PREFS = "SHARED_BOOLEAN";
    public static final String FLASH_STATE = "FLASH_STATE";
    SharedPreferences sharedpreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        //System.out.println("123");
        sharedpreferences = context.getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        state = sharedpreferences.getBoolean(FLASH_STATE,false);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flash_layout_widget);


        //System.out.println("123");

        if (state) {
            views.setImageViewResource(R.id.example_widget_button, R.drawable.flashlight_off_24);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(FLASH_STATE);

            //System.out.println("1234");
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId,false);
                state = false;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            editor.putBoolean(FLASH_STATE,state);
            editor.apply();
            editor.commit();
        }
        else {
            views.setImageViewResource(R.id.example_widget_button, R.drawable.flashlight_on_24);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(FLASH_STATE);
            //System.out.println("12345");
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId,true);
                state = true;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            editor.putBoolean(FLASH_STATE,state);
            editor.apply();
            editor.commit();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, FlashAppWidget.class),views);

//        if (state) {
//            if (camera != null) {
//                camera.stopPreview();
//                camera.release();
//                camera = null;
//                state = false;
//            }
//
//        }
//        else {
//            // Open the default i.e. the first rear facing camera.
//            camera = Camera.open();
//
//            if(camera == null) {
//                Toast.makeText(context, "NO CAMERA", Toast.LENGTH_SHORT).show();
//            } else {
//                // Set the torch flash mode
//                android.hardware.Camera.Parameters param = camera.getParameters();
//                param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                try {
//                    camera.setParameters(param);
//                    camera.startPreview();
//                    state = true;
//                } catch (Exception e) {
//                    Toast.makeText(context, "NO FLASH", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }
}

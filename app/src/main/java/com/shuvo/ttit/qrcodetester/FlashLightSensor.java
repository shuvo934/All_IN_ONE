package com.shuvo.ttit.qrcodetester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class FlashLightSensor extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView textView;
    private Float changeVal;
    private Sensor lightSensor;
    private String cameraId;
    private CameraManager cameraManager;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light_sensor);

        textView = findViewById(R.id.text_sensor_data);
        imageButton = findViewById(R.id.torch_button_sensor);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {

            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        }
        else {
            Toast.makeText(this, "Sorry, No Light Sensor in your phone", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        changeVal = sensorEvent.values[0];
        textView.setText(String.valueOf(changeVal));

        if (changeVal< 40) {
            try {
                cameraManager.setTorchMode(cameraId, true);
                imageButton.setImageResource(R.drawable.torch_on);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                cameraManager.setTorchMode(cameraId, false);
                imageButton.setImageResource(R.drawable.torch_off);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        try {
            cameraManager.setTorchMode(cameraId, false);
            imageButton.setImageResource(R.drawable.torch_off);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
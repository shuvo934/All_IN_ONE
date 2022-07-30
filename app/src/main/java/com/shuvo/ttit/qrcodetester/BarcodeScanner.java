package com.shuvo.ttit.qrcodetester;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.jaredrummler.android.device.DeviceName;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private TextView txtResult;

    boolean flash = false;
    boolean focus = true;

    ImageView flashSwitch;
    ImageView focusSwitch;
    ImageView cameraSwitch;

    int cameraId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        scannerView = findViewById(R.id.zxscan_dm7);
        //txtResult = findViewById(R.id.txt_result);
        flashSwitch = findViewById(R.id.flash_switch);
        flashSwitch.setImageResource(R.drawable.flash_off_24);

        focusSwitch = findViewById(R.id.focus_switch);
        focusSwitch.setImageResource(R.drawable.center_focus_strong_24);

        cameraSwitch = findViewById(R.id.camera_switch);


        scannerView.setFlash(flash);
        scannerView.setAutoFocus(focus);

//        DeviceName.init(this);
//        String deviceName = DeviceName.getDeviceName();
//        System.out.println(deviceName);
//        if (deviceName.contains("HUAWEI")) {
//            scannerView.setAspectTolerance(0.5f);
//        }

        String deviceName = getDeviceName();
        System.out.println(deviceName);
        if (deviceName.contains("HUAWEI")) {
            scannerView.setAspectTolerance(0.5f);
        }



//        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//            String[] ids = cameraManager.getCameraIdList();
//            for (String id: ids) {
//                System.out.println(id);
//            }
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }


//        scannerView.startCamera();

//        Dexter.withContext(this)
//                .withPermission(Manifest.permission.CAMERA)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                        Toast.makeText(BarcodeScanner.this, "You must accept this permission", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//
//                    }
//                })
//                .check();

        flashSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flash) {
                    flashSwitch.setImageResource(R.drawable.flash_on_24);
                    flash = true;
                    scannerView.setFlash(flash);
                }
                else {
                    flashSwitch.setImageResource(R.drawable.flash_off_24);
                    flash = false;
                    scannerView.setFlash(flash);
                }
            }
        });

        focusSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!focus) {
                    focusSwitch.setImageResource(R.drawable.center_focus_strong_24);
                    focus = true;
                    scannerView.setAutoFocus(focus);
                }
                else {
                    focusSwitch.setImageResource(R.drawable.center_focus_weak_24);
                    focus = false;
                    scannerView.setAutoFocus(focus);
                }
            }
        });

        cameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraId == 0) {
                    flashSwitch.setImageResource(R.drawable.flash_off_24);
                    flash = false;
                    scannerView.setFlash(flash);

                    focusSwitch.setImageResource(R.drawable.center_focus_strong_24);
                    focus = true;
                    scannerView.setAutoFocus(focus);

                    scannerView.stopCamera();

                    cameraId = 1;
                    scannerView.setResultHandler(BarcodeScanner.this);
                    scannerView.startCamera(cameraId);
                }
                else {
                    flashSwitch.setImageResource(R.drawable.flash_off_24);
                    flash = false;
                    scannerView.setFlash(flash);

                    focusSwitch.setImageResource(R.drawable.center_focus_strong_24);
                    focus = true;
                    scannerView.setAutoFocus(focus);

                    scannerView.stopCamera();

                    cameraId = 0;
                    scannerView.setResultHandler(BarcodeScanner.this);
                    scannerView.startCamera(cameraId);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(BarcodeScanner.this);
        scannerView.startCamera(cameraId);
//        scannerView.resumeCameraPreview(BarcodeScanner.this);
//        scannerView.setFlash(true);
    }

    @Override
    protected void onPause() {
        flashSwitch.setImageResource(R.drawable.flash_off_24);
        flash = false;
        scannerView.setFlash(flash);

        focusSwitch.setImageResource(R.drawable.center_focus_strong_24);
        focus = true;
        scannerView.setAutoFocus(focus);

        scannerView.stopCamera();
        super.onPause();
    }


    @Override
    public void handleResult(Result rawResult) {
        //txtResult.setText(rawResult.getText());
        Intent intent = new Intent(BarcodeScanner.this, BarcodeResult.class);
        intent.putExtra("RESULT",rawResult.getText());
        startActivity(intent);
//        scannerView.resumeCameraPreview(BarcodeScanner.this);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }
}
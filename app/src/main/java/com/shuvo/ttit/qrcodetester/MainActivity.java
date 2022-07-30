package com.shuvo.ttit.qrcodetester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity {


    int REQUEST_CODE_PERMISSION_STORAGE = 100;
    TextView textView;
    Button scanner;
    Button zxingScanner;
    Button barcode;
    Button textScan;
    Button flash;
    Button flashSensor;
    Button spanView;
    Button imageDatabase;
    Button treeMenu;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.welcome_text);


        scanner = findViewById(R.id.scan_qr_code);
        scanner.setEnabled(false);

        zxingScanner = findViewById(R.id.scan_qr_code_zxing);
        zxingScanner.setEnabled(false);

        barcode = findViewById(R.id.scan_qr_code_bcs);
        barcode.setEnabled(false);

        textScan = findViewById(R.id.scan_text);
        textScan.setEnabled(false);

        flash = findViewById(R.id.flash_light);
        flash.setEnabled(false);

        flashSensor = findViewById(R.id.flash_light_with_sensor);
        flashSensor.setEnabled(false);

        spanView = findViewById(R.id.span_recyclerview);

        imageDatabase = findViewById(R.id.image_from_database_button);

        treeMenu = findViewById(R.id.tree_menu_2nd_button);

        treeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TreeMenu2nd.class);
                startActivity(intent);
                showSystemUI();
            }
        });

        imageDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ImageDatabase.class);
                startActivity(intent);
                showSystemUI();
            }
        });

        spanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SpanRecycle.class);
                startActivity(intent);
                showSystemUI();
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,QrScanner.class);
                startActivity(intent);
                showSystemUI();
            }
        });

        zxingScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ZxingScanner.class);
                startActivity(intent);
                showSystemUI();
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,BarcodeImageScanner.class);
                startActivity(intent);
                showSystemUI();
            }
        });

        textScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TextScanner.class);
                startActivity(intent);
                showSystemUI();
            }
        });

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,FlashLight.class);
                startActivity(intent);
                showSystemUI();
            }
        });

        flashSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,FlashLightSensor.class);
                startActivity(intent);
                showSystemUI();
            }
        });


        CheckPermission();


    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

//    private void enableCamera() {
//        if (Build.VERSION.SDK_INT >= 23) {
//
//            String[] permission = {
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.VIBRATE
//            };
//
//            for (String str : permission) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
//                    return;
//                }
//
//            }
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
////                mCodeScanner.startPreview();
//            }
//
//        }
//    }



//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_PERMISSION_STORAGE) {
//            if (grantResults.length > 0 &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                mCodeScanner.startPreview();
//                // Permission is granted. Continue the action or workflow
//                // in your app.
//            }
//        }
//    }

    private void CheckPermission() {
        int REQUEST_CODE_PERMISSION_STORAGE = 100;

        try {
            String[] permission = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE
            };

            for (String str : permission) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
                    return;
                } else {
                    checkIfCameraPermissionIsGranted();
                }

            }

        } catch (IllegalArgumentException e) {
            checkIfCameraPermissionIsGranted();
        }

    }

    private void checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted: start the preview
            scanner.setEnabled(true);
            zxingScanner.setEnabled(true);
            barcode.setEnabled(true);
            textScan.setEnabled(true);
            flash.setEnabled(true);
            flashSensor.setEnabled(true);
        } else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                    .setCancelable(false)
                    .setTitle("Permission required")
                    .setMessage("This application needs to access the camera to process barcodes")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CheckPermission();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);
                        }
                    });
            AlertDialog alertDialog = materialAlertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        checkIfCameraPermissionIsGranted();
    }

}
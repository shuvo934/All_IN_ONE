package com.shuvo.ttit.qrcodetester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;

public class BarcodeImageScanner extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    Button gallery, camera;
    BarcodeScannerOptions options;
    com.google.mlkit.vision.barcode.BarcodeScanner scanner = BarcodeScanning.getClient();

    InputImage image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_image_scanner);

        imageView = findViewById(R.id.cropped_image_for_barcode);
        textView = findViewById(R.id.text_data_barcode);

        gallery = findViewById(R.id.button_gallery);
        camera = findViewById(R.id.button_camera);

        options = new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_ALL_FORMATS)
                        .build();



        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarcodeImageScanner.this, BarcodeScanner.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri croppedImage = data.getData();

            Glide.with(BarcodeImageScanner.this)
                    .load(croppedImage)
                    .into(imageView);


            try {
                image = InputImage.fromFilePath(BarcodeImageScanner.this, croppedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {

                            for (Barcode barcode: barcodes) {
                                Rect bounds = barcode.getBoundingBox();
                                Point[] corners = barcode.getCornerPoints();

                                String rawValue = barcode.getRawValue();

                                int valueType = barcode.getValueType();
                                // See API reference for complete list of supported types
                                switch (valueType) {
                                    case Barcode.TYPE_WIFI:
                                        String ssid = barcode.getWifi().getSsid();
                                        String password = barcode.getWifi().getPassword();
                                        int type = barcode.getWifi().getEncryptionType();
                                        textView.setText("WIFI:-\n"+ "NAME: "+ ssid + "\nPassword: "+ password+"\nType: " + type);
                                        break;
                                    case Barcode.TYPE_URL:
                                        String title = barcode.getUrl().getTitle();
                                        String url = barcode.getUrl().getUrl();
                                        textView.setText("URL:-\n"+ "Title: "+ title + "\nAddress: "+ url);
                                        break;
                                    default:
                                        textView.setText(rawValue);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BarcodeImageScanner.this, "Can't Scan the Image",Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
}
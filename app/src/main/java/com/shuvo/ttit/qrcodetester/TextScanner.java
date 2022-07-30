package com.shuvo.ttit.qrcodetester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class TextScanner extends AppCompatActivity {

    Button captureButton;
    Button copyButton;
    TextView textData;

    Bitmap bitmap;
    ImageView cropped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_scanner);

        captureButton = findViewById(R.id.button_capture);
        copyButton = findViewById(R.id.button_copy);
        copyButton.setVisibility(View.GONE);
        textData = findViewById(R.id.text_data);
        cropped = findViewById(R.id.cropped_iamge);


        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(TextScanner.this);

            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scannedText = textData.getText().toString();
                copyToClipboard(scannedText);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    cropped.setImageBitmap(bitmap);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void getTextFromImage(Bitmap bitmap) {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();

        if (!recognizer.isOperational()) {
            Toast.makeText(TextScanner.this, "Error Occured", Toast.LENGTH_SHORT).show();
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            textData.setText(stringBuilder.toString());
            captureButton.setText("Retake");
            copyButton.setVisibility(View.VISIBLE);
        }



        //--- Below Code also Works
//        int rotation = 0;
//
//        com.google.mlkit.vision.text.TextRecognizer textRecognizer;
//        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
//
//        InputImage image = InputImage.fromBitmap(bitmap, rotation);
//        Task<Text> result = textRecognizer.process(image)
//                .addOnSuccessListener(new OnSuccessListener<Text>() {
//                    @Override
//                    public void onSuccess(Text text) {
//
//
//                        StringBuilder stringBuilder = new StringBuilder();
//                        String resultText = text.getText();
//                        System.out.println("ResultText = "+ resultText);
//                        for (Text.TextBlock block : text.getTextBlocks()) {
//                            String blockText = block.getText();
//                            System.out.println("BlockText = "+ blockText);
//                            Point[] blockCornerPoints = block.getCornerPoints();
//                            Rect blockFrame = block.getBoundingBox();
//                            for (Text.Line line : block.getLines()) {
//                                String lineText = line.getText();
//                                System.out.println("LineText = "+ lineText);
//                                Point[] lineCornerPoints = line.getCornerPoints();
//                                Rect lineFrame = line.getBoundingBox();
//                                for (Text.Element element : line.getElements()) {
//                                    String elementText = element.getText();
//                                    stringBuilder.append(elementText);
//                                    System.out.println("ElementText = "+ elementText);
//                                    Point[] elementCornerPoints = element.getCornerPoints();
//                                    Rect elementFrame = element.getBoundingBox();
//                                }
//                                textData.setText(resultText);
//                            }
//                        }
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(TextScanner.this, "Error Occured", Toast.LENGTH_SHORT).show();
//                    }
//                });


    }

    private void copyToClipboard(String text) {

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied Data", text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(TextScanner.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

//    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
//    static {
//        ORIENTATIONS.append(Surface.ROTATION_0, 0);
//        ORIENTATIONS.append(Surface.ROTATION_90, 90);
//        ORIENTATIONS.append(Surface.ROTATION_180, 180);
//        ORIENTATIONS.append(Surface.ROTATION_270, 270);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private int getRotationCompensation(String cameraId, Activity activity, boolean isFrontFacing)
//            throws CameraAccessException {
//        // Get the device's current rotation relative to its "native" orientation.
//        // Then, from the ORIENTATIONS table, look up the angle the image must be
//        // rotated to compensate for the device's rotation.
//        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        int rotationCompensation = ORIENTATIONS.get(deviceRotation);
//
//        // Get the device's sensor orientation.
//        CameraManager cameraManager = (CameraManager) activity.getSystemService(CAMERA_SERVICE);
//        int sensorOrientation = cameraManager
//                .getCameraCharacteristics(cameraId)
//                .get(CameraCharacteristics.SENSOR_ORIENTATION);
//
//        if (isFrontFacing) {
//            rotationCompensation = (sensorOrientation + rotationCompensation) % 360;
//        } else { // back-facing
//            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360;
//        }
//        return rotationCompensation;
//    }
}
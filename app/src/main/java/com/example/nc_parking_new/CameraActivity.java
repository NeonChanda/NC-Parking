package com.example.nc_parking_new;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class CameraActivity extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture; // // Future that provides the CameraX instance
    PreviewView previewView;
    Button bTakePicture; // UI Elements
    private ImageCapture imageCapture;  // Image Capture instance




    @Override
    protected void onCreate(Bundle savedInstanceState) { // Checks for permissions camera and storage.
        //If permissions are granted, starts the camera.
        //If not granted, requests them.
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera);
        bTakePicture = findViewById(R.id.bCapture); // initialise the ui elements
        previewView = findViewById(R.id.previewView);
        bTakePicture.setOnClickListener(v -> capturePhoto());   // Set up click listener for capturing photos
        // Check for Camera & Storage permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, // If permissions are not granted, request them
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }else {  // If permissions are granted, initialise the camera
            cameraProviderFuture = ProcessCameraProvider.getInstance(this);  // Get the CameraX provider
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    startCameraX(cameraProvider); // Start the camera preview
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, getExecutor());
        }

    }
       //  Provides an executor to run camera tasks
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }
    @SuppressLint("RestrictedApi")     // Starts CameraX preview and image capture functionality
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();     // Unbind any existing camera instances before starting a new one
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)   // Select the back-facing camera
                .build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture); // Bind the camera to lifecycle and associate it with preview & image capture
    }

    @Override //  Handles permission request results
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101) { // Check if the permission request was for camera access
            if(grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                cameraProviderFuture = ProcessCameraProvider.getInstance(this);
                cameraProviderFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        startCameraX(cameraProvider);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, getExecutor());
            } else {
                Toast.makeText(this, "Camera permission is required!", // Permissions denied, show error message
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void capturePhoto() {  // Captures and saves an image to the gallery
        long timeStamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp); // set the metadata for the image
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        imageCapture.takePicture( // Capture the image and save it
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override   // Called when the image is successfully saved
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults
                                                     outputFileResults) {
                        Toast.makeText(CameraActivity.this, "Image has been saved to the Gallery",Toast.LENGTH_SHORT).show();
                    }
                    @Override  // Called when an error occurs during saving
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this,"Error: "+exception.getMessage(),Toast.LENGTH_SHORT).show();
                                exception.printStackTrace();
                    }
                });
    }
}
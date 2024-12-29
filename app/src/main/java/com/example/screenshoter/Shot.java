package com.example.screenshoter;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.screenshoter.databinding.ActivityShotBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

public class Shot extends AppCompatActivity {

    ActivityShotBinding binding;
    private static String action;
    private static final int REQUEST_ID = 1;
    private static final int REQUEST_ID_VIDEO = 1000;
    private  static ScreenshotManager screenshotManager;
    private static ScreenRecordManager screenRecordManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        action=getIntent().getStringExtra("action");
        if (action.equals("VIDEO_ON")){
            screenRecordManager=new ScreenRecordManager(Shot.this,REQUEST_ID_VIDEO);
            Intent intent=new Intent(Shot.this,ScreenRecordManager.class);
            intent.putExtra("action","VIDEO_ON");
            startService(intent);

        }else if(action.equals("VIDEO_OFF")){
            stopService(new Intent(Shot.this,ScreenRecordManager.class));

        }else if (action.equals("SHOT")){
            screenshotManager=new ScreenshotManager(Shot.this,REQUEST_ID);
            Intent intent=new Intent(Shot.this,ScreenshotManager.class);
            startService(intent);
        }else{
            Toast.makeText(this, "Error2", Toast.LENGTH_SHORT).show();
        }








    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID_VIDEO){

                screenRecordManager.onActivityResult(resultCode,data);
            }else if(REQUEST_ID==requestCode){
                screenshotManager.onActivityResult(resultCode, data);
            }else{

        }

    }



















        /*ActivityCompat.requestPermissions( this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.FOREGROUND_SERVICE
                }, 1
        );

        binding.txtSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takeScreenshot();
                takeShot2();
            }
        });

    }

    private void takeScreenshot() {


        try {


            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

// Get root view
            View view = getWindow().getDecorView().getRootView();

// Create the bitmap to use to draw the screenshot
            final Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_4444);
            final Canvas canvas = new Canvas(bitmap);

// Get current theme to know which background to use

            final Resources.Theme theme = getTheme();
            final TypedArray ta = theme
                    .obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
            final int res = ta.getResourceId(0, 0);
            final Drawable background = getResources().getDrawable(res);

// Draw background
            background.draw(canvas);

// Draw views
            view.draw(canvas);


            // image naming and path  to include sd card  appending name you choose for file
            File dest=new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)),System.currentTimeMillis() + ".jpg");
            FileOutputStream fileOutputStream=new FileOutputStream(dest);


            if (fileOutputStream != null) {
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)) {
                    Toast.makeText(Shot.this,"Error 2",Toast.LENGTH_SHORT).show();
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            File imageFile = new File(mPath);
            imageFile.mkdirs();

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            binding.testImg.setImageBitmap(bitmap);
            Toast.makeText(Shot.this,"OK",Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            Toast.makeText(Shot.this,"Error1",Toast.LENGTH_SHORT).show();
        }
    }

    private void takeShot2() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;
        displayHeight = size.y;

        flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        density = metrics.densityDpi;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            imageReader = ImageReader.newInstance(displayWidth, displayHeight, ImageFormat.JPEG, 1);
            if (imageReader==null) {
                Toast.makeText(getBaseContext(), "E1", Toast.LENGTH_SHORT).show();
            }else {


                mProjectionManager = (MediaProjectionManager) getSystemService
                        (Context.MEDIA_PROJECTION_SERVICE);
                mMediaProjectionCallback = new MediaProjectionCallback();
                startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);

            }
        }







    }
*/
    public static byte[] getDataFromImage(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] data = new byte[buffer.capacity()];
        buffer.get(data);

        return data;
    }

/*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {

            mMediaProjection = null;
            Log.i(TAG, "MediaProjection Stopped");
        }
    }


   private VirtualDisplay createVirtualDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mMediaProjection.createVirtualDisplay("MainActivity",
                    DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mMediaRecorder.getSurface(), null /*Callbacks*/
      //          , null /*Handler*/
              /*);}
        return null;
    }
    private void myTask(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {




        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PERMISSION_CODE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {




                mMediaProjection=mProjectionManager.getMediaProjection(resultCode,data);
                mMediaProjection.createVirtualDisplay("test",
                        displayWidth,
                        displayHeight,
                        density,
                        flags,
                        imageReader.getSurface(),
                        null, null);

                image=imageReader.acquireLatestImage();
                byte[] data2 = getDataFromImage(image);
                bitmap = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                binding.testImg.setImageBitmap(bitmap);

            }
        }
    }*/
}



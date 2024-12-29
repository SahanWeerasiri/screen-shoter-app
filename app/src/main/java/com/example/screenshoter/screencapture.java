/*public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PERMISSION_CODE = 1;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 480;
    private static final int DISPLAY_HEIGHT = 640;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private ToggleButton mToggleButton;
    private MediaRecorder mMediaRecorder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        mMediaRecorder = new MediaRecorder();
        initRecorder();
        prepareRecorder();

        mProjectionManager = (MediaProjectionManager) getSystemService
                (Context.MEDIA_PROJECTION_SERVICE);

        mToggleButton = (ToggleButton) findViewById(R.id.toggle);
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleScreenShare(v);
            }
        });

        mMediaProjectionCallback = new MediaProjectionCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }



    public void onToggleScreenShare(View view) {
        if (((ToggleButton) view).isChecked()) {
            shareScreen();
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.v(TAG, "Recording Stopped");
            stopScreenSharing();
            initRecorder();
            prepareRecorder();
        }
    }

    private void shareScreen() {
        if (mMediaProjection == null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);
            return;
        }
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        //mMediaRecorder.release();
    }

    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("MainActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/
/*, null /*Handler*/
/*);
    }



    private void prepareRecorder() {
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    private void initRecorder() {
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        mMediaRecorder.setOutputFile("/sdcard/capture.mp4");
    }
}
*/


//MAin Activity

/*
public projector mProjector;
    private MediaProjectionManager mProjectionManager;
    private MediaProjection mMediaProjection;
    private static final int PERMISSION_CODE = 1;

    int displayWidth, displayHeight;
    ImageReader imageReader;
    int density,flags;
    Bitmap bitmap;
    Surface surface;
    Image image;


     displayHeight=getIntent().getIntExtra("height",-1);
        displayWidth=getIntent().getIntExtra("width",-1);
        density=getIntent().getIntExtra("density",-1);
        flags=getIntent().getIntExtra("flags",-1);



        if(displayWidth!=-1 && displayHeight!=-1 && density!=-1 && flags!=-1 ){
            HashMap<String ,Object> map= (HashMap<String, Object>) getIntent().getExtras().get("img");
            imageReader = ImageReader.newInstance(displayWidth, displayHeight, ImageFormat.JPEG, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (imageReader==null) {
                    Toast.makeText(getBaseContext(), "E1", Toast.LENGTH_SHORT).show();
                }else {


                    mProjectionManager = (MediaProjectionManager) getSystemService
                            (Context.MEDIA_PROJECTION_SERVICE);
                    startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);

                }



            }
        }



        if(requestCode==PERMISSION_CODE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                takeScreenshot();

            }
        }




        public static byte[] getDataFromImage(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] data = new byte[buffer.capacity()];
        buffer.get(data);

        return data;
    }






 */



//Widget

/*




    int displayWidth, displayHeight;
    ImageReader imageReader;
    int density,flags;
    Bitmap bitmap;

    private static final String TAG = "MainActivity";
    private static final int PERMISSION_CODE = 1;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 480;
    private static final int DISPLAY_HEIGHT = 640;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/Downloads/" + now + ".jpg";

            // create bitmap screen capture


            /*View v1 = ((Activity)getApplicationContext()).getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            takeShot2();
                    if (bitmap != null) {
                    File imageFile = new File(mPath);

                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_SHORT).show();
                    } else {
                    Toast.makeText(getBaseContext(), "Error1", Toast.LENGTH_SHORT).show();
                    }


                    } catch (Throwable e) {
                    // Several error may come out with file handling or DOM
                    e.printStackTrace();
                    }
                    }

 private void takeShot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DisplayMetrics metrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metrics);
            mScreenDensity = metrics.densityDpi;

            // mMediaRecorder = new MediaRecorder();
            //initRecorder();
            // prepareRecorder();


            mProjectionManager = (MediaProjectionManager) getSystemService
                    (Context.MEDIA_PROJECTION_SERVICE);

            mMediaProjectionCallback = new MediaProjectionCallback();


        }

    }

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
            /*return mMediaProjection.createVirtualDisplay("MainActivity",
                    DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mMediaRecorder.getSurface(), null /*Callbacks*/
//, null /*Handler*/);
/*
import androidx.annotation.Nullable;}
        return null;
        }

private void takeShot2() {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;
        displayHeight = size.y;

        flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        density = metrics.densityDpi;


        imageReader = ImageReader.newInstance(displayWidth, displayHeight, ImageFormat.JPEG, 1);


        Intent intent=new Intent(Widget.this,MainActivity.class);
        intent.putExtra("flags",flags);
        intent.putExtra("density",density);
        intent.putExtra("width",displayWidth);
        intent.putExtra("height",displayHeight);
        HashMap<String,Object> map=new HashMap<>();
        map.put("A",imageReader.getSurface());
        map.put("B",imageReader.acquireLatestImage());

        intent.putExtra("img",map);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);









        }

public static byte[] getDataFromImage(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] data = new byte[buffer.capacity()];
        buffer.get(data);

        return data;
        }
@Override
public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        }


 */



/////////////////////////////////////////////////////////////
//Shot

/*
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

    private static final int REQUEST_ID = 1;


    int displayWidth, displayHeight;
    ImageReader imageReader;
    int density,flags;
    Bitmap bitmap;
    Image image;

    private static final String TAG = "MainActivity";
    private static final int PERMISSION_CODE = 1;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 480;
    private static final int DISPLAY_HEIGHT = 640;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private MediaRecorder mMediaRecorder;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        mMediaRecorder = new MediaRecorder();
        //initRecorder();
       // prepareRecorder();

        mProjectionManager = (MediaProjectionManager) getSystemService
                (Context.MEDIA_PROJECTION_SERVICE);

        binding.txtSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenshotManager.INSTANCE.requestScreenshotPermission(Shot.this, REQUEST_ID);
                ScreenshotManager.INSTANCE.takeScreenshot(Shot.this);
                //shareScreen();
            }
        });

        mMediaProjectionCallback = new MediaProjectionCallback();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID)
            ScreenshotManager.INSTANCE.onActivityResult(resultCode, data);
        if (requestCode != PERMISSION_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            return;
        }else{
            mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
            mMediaProjection.registerCallback(mMediaProjectionCallback, null);
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



       // mVirtualDisplay = createVirtualDisplay();
        //mMediaRecorder.start();
    }

   /* @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onToggleScreenShare(View view) {
        if (((ToggleButton) view).isChecked()) {
            shareScreen();
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.v(TAG, "Recording Stopped");
            stopScreenSharing();
            //initRecorder();
            prepareRecorder();
        }
    }

import android.media.Image;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.nio.ByteBuffer;*/
/*

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
private void shareScreen() {
        if (mMediaProjection == null) {
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);
        return;
        }
        //mVirtualDisplay = createVirtualDisplay();
        // mMediaRecorder.start();
        }

private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
        return;
        }
        mVirtualDisplay.release();
        //mMediaRecorder.release();
        }

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("MainActivity",
        DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
        mMediaRecorder.getSurface(), null /*Callbacks*/
//, null /*Handler*/);
/*
        }

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
private class MediaProjectionCallback extends MediaProjection.Callback {
    @Override
    public void onStop() {

        mMediaRecorder.stop();
        mMediaRecorder.reset();
        Log.v(TAG, "Recording Stopped");
        initRecorder();
        prepareRecorder();

        mMediaProjection = null;
        stopScreenSharing();
        Log.i(TAG, "MediaProjection Stopped");
    }
}

    private void prepareRecorder() {
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    private void initRecorder() {
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        mMediaRecorder.setOutputFile("/sdcard/capture.mp4");
    }


















    ActivityCompat.requestPermissions( this,
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

    public static byte[] getDataFromImage(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] data = new byte[buffer.capacity()];
        buffer.get(data);

        return data;
    }


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
    }
}



 */

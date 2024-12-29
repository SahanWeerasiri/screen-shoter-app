package com.example.screenshoter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;

public class ScreenRecordManager extends Service {

   /* private static Intent vIntent;
    private static MediaProjectionManager vmediaProjectionManager;
    private static MediaProjection vmediaProjection;
    private static MediaProjectionCallback vMediaProjectionCallback;
    private static Context vcontext_;
    private static Activity vactivity;
    private  static MediaRecorder vmediaRecorder;
    private static VirtualDisplay vVirtualDisplay;
    private static int vrequestId;
    private static int vScreenDensity;
    private static int vDISPLAY_WIDTH,vDISPLAY_HEIGHT;
    private static final SparseArray ORIENTATION=new SparseArray();
    static {
        ORIENTATION.append(Surface.ROTATION_0,90);
        ORIENTATION.append(Surface.ROTATION_90,0);
        ORIENTATION.append(Surface.ROTATION_180,270);
        ORIENTATION.append(Surface.ROTATION_270,180);

    }
    private static String VIDEO_ON="VIDEO_ON",VIDEO_OFF="VIDEO_OFF";
    private  static String ACTION;*/

    private static final String TAG = "MainActivity";
    private static int PERMISSION_CODE;
    private static int mScreenDensity;
    private static MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 480;
    private static final int DISPLAY_HEIGHT = 640;
    private static MediaProjection mMediaProjection;
    private static VirtualDisplay mVirtualDisplay;
    private static MediaProjectionCallback mMediaProjectionCallback;
    private static MediaRecorder mMediaRecorder;
    private static Activity activity;
    private static Context context_;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        mMediaRecorder = new MediaRecorder();
        initRecorder();
        prepareRecorder();

        mProjectionManager = (MediaProjectionManager) getSystemService
                (Context.MEDIA_PROJECTION_SERVICE);


        onToggleScreenShare();


        mMediaProjectionCallback = new MediaProjectionCallback();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }

        System.exit(0);
    }
    public ScreenRecordManager(){

    }
    public ScreenRecordManager(Activity activity,int requestId){
        this.activity=activity;
        this.PERMISSION_CODE=requestId;
        this.context_=activity.getBaseContext();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onActivityResult(int resultCode, Intent data) {

        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onToggleScreenShare() {
            shareScreen();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void shareScreen() {
        if (mMediaProjection == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NotificationChannel channel=new NotificationChannel("My_notification","My_notification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager=context_.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context_, "My_notification");
                builder.setContentTitle("Sample");
                builder.setContentText("Hello");
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context_);
                // managerCompat.notify(0, builder.build());


                Intent intent2=mProjectionManager.createScreenCaptureIntent();
                startForeground(1,builder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);

                activity.startActivityForResult(intent2, PERMISSION_CODE);

            }
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("MainActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null /*Handler*/);
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
            activity.finish();
        } catch (IOException e) {
            e.printStackTrace();
            activity.finish();
        }
    }

    private void initRecorder() {
        //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        mMediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+System.currentTimeMillis()+".mp4");
    }
    /* @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ACTION=intent.getStringExtra("action");

        DisplayMetrics metrics=new DisplayMetrics();
        vactivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        vScreenDensity=metrics.densityDpi;

        vDISPLAY_WIDTH=metrics.widthPixels;
        vDISPLAY_HEIGHT=metrics.heightPixels;
        vmediaRecorder=new MediaRecorder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            initRecorder();

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vmediaProjectionManager = (MediaProjectionManager) vactivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel=new NotificationChannel("My_notification","My_notification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager=vcontext_.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(vcontext_, "My_notification");
                builder.setContentTitle("Sample");
                builder.setContentText("Hello");
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(vcontext_);
                // managerCompat.notify(0, builder.build());


                Intent intent2=vmediaProjectionManager.createScreenCaptureIntent();
                startForeground(1,builder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);

                vactivity.startActivityForResult(intent2, vrequestId);

            }
        }



        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {

        stopScreenSharing();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stopRecord();
        }
        super.onDestroy();

    }
public ScreenRecordManager(){

}
    public ScreenRecordManager(@NonNull Activity activity, int requestId) {
        this.vactivity=activity;
        this.vrequestId=requestId;
        vcontext_=activity.getBaseContext();

    }


    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            vIntent = data;
            //SystemClock.sleep(1000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vIntent.setPackage(vcontext_.getPackageName());
                takeVideo(vcontext_);


            }

        }
        else vIntent = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @UiThread
    public boolean takeVideo(@NonNull Context context) {
        if (vIntent == null)
            return false;

        vMediaProjectionCallback = new MediaProjectionCallback();


        vmediaProjection = vmediaProjectionManager.getMediaProjection(Activity.RESULT_OK, vIntent);
        if (vmediaProjection == null)
            return false;

        vmediaProjection.registerCallback(new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
                if (vVirtualDisplay != null)
                    vVirtualDisplay.release();
                vmediaProjection.unregisterCallback(this);
            }
        }, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            vVirtualDisplay = createVirtualDisplay();


            vmediaRecorder.start();

        }
        Toast.makeText(context,"Start",Toast.LENGTH_SHORT).show();

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {

            vmediaRecorder.stop();
            vmediaRecorder.reset();


            vmediaRecorder = null;
            stopScreenSharing();
            Log.i(TAG, "MediaProjection Stopped");
            super.onStop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void initRecorder() {


        //vmediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        vmediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        vmediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        vmediaRecorder.setOutputFile(path+"/"+  System.currentTimeMillis()+".mp4");

        vmediaRecorder.setVideoSize(vDISPLAY_WIDTH,vDISPLAY_HEIGHT);
        //vmediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        vmediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        vmediaRecorder.setVideoFrameRate(30);
        //
        vmediaRecorder.setVideoEncodingBitRate(512 * 1000);








        int rotation=vactivity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation= (int) ORIENTATION.get(rotation*90);
        vmediaRecorder.setOrientationHint(orientation);

        prepareRecorder();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private VirtualDisplay createVirtualDisplay() {
        return vmediaProjection.createVirtualDisplay("MainActivity",
                vDISPLAY_WIDTH, vDISPLAY_HEIGHT, vScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                vmediaRecorder.getSurface(), null /*Callbacks*/
    /*, null /*Handler*/
    //);
    /*

        }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopRecord() {

        vmediaRecorder.release();

            Log.v(TAG, "Recording Stopped");
            stopScreenSharing();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopScreenSharing() {
        if (vVirtualDisplay == null) {
            return;
        }
        vVirtualDisplay.release();
        if(vmediaProjection==null)
            return;

        vmediaProjection.unregisterCallback(vMediaProjectionCallback);
        vmediaProjection.stop();
        vmediaProjection=null;
    }

    private void prepareRecorder() {
        try {
            vmediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            vactivity.finish();
        } catch (IOException e) {
            e.printStackTrace();
            vactivity.finish();
        }
    }*/
}

package com.example.screenshoter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ForegroundServiceStartNotAllowedException;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class ScreenshotManager extends Service{
    private static final String SCREENCAP_NAME = "screencap";
    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    private static Intent sIntent;
    private static Bitmap sbitmap;
    private static MediaProjectionManager smediaProjectionManager;
    private static MediaProjection smediaProjection;
    private static MediaProjectionCallback sMediaProjectionCallback;
    private static VirtualDisplay svirtualDisplay;
    private static Context scontext_;
    private static Activity sactivity;
    private static int srequestId;
    public ScreenshotManager(){

    }
    public ScreenshotManager(@NonNull Activity activity, int requestId) {
        this.sactivity=activity;
        this.srequestId=requestId;

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void requestScreenshotPermission(@NonNull Activity activity, int requestId) {
        scontext_=activity.getBaseContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        smediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel=new NotificationChannel("My_notification","My_notification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager=scontext_.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(scontext_, "My_notification");
                builder.setContentTitle("Sample");
                builder.setContentText("Hello");
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(scontext_);
                // managerCompat.notify(0, builder.build());


                Intent intent=smediaProjectionManager.createScreenCaptureIntent();
                startForeground(1,builder.build(),ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);

                activity.startActivityForResult(intent, requestId);

            }




    }
    }


    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            sIntent = data;
            SystemClock.sleep(1000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                sIntent.setPackage(scontext_.getPackageName());
                takeScreenshot(scontext_);


            }

        }
        else sIntent = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @UiThread
    public boolean takeScreenshot(@NonNull Context context) {
        if (sIntent == null)
            return false;
        sMediaProjectionCallback = new MediaProjectionCallback();


        //mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        smediaProjection = smediaProjectionManager.getMediaProjection(Activity.RESULT_OK, sIntent);
        if (smediaProjection == null)
            return false;
        final int density = context.getResources().getDisplayMetrics().densityDpi;
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        final int width = size.x, height = size.y;

        // start capture reader
        @SuppressLint("WrongConstant") final ImageReader imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1);
        //
        svirtualDisplay = smediaProjection.createVirtualDisplay(SCREENCAP_NAME, width, height, density, VIRTUAL_DISPLAY_FLAGS, imageReader.getSurface(), null, null);


        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(final ImageReader reader) {
                Log.d("AppLog", "onImageAvailable");

                smediaProjection.stop();
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(final Void... params) {
                        Image image = null;
                        sbitmap = null;
                        try {
                            image = reader.acquireLatestImage();
                            if (image != null) {
                                Image.Plane[] planes = image.getPlanes();
                                ByteBuffer buffer = planes[0].getBuffer();
                                int pixelStride = planes[0].getPixelStride(), rowStride = planes[0].getRowStride(), rowPadding = rowStride - pixelStride * width;
                                sbitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                                sbitmap.copyPixelsFromBuffer(buffer);
                                saveImage();

                                return sbitmap;
                            }
                        } catch (Exception e) {
                            if (sbitmap != null)
                                sbitmap.recycle();
                            e.printStackTrace();
                        }
                        if (image != null){
                            image.close();
                        }

                        reader.close();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(final Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        Log.d("AppLog", "Got bitmap?" + (bitmap != null));
                    }
                }.execute();
            }
        }, null);

       smediaProjection.registerCallback(new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
                if (svirtualDisplay != null)
                    svirtualDisplay.release();
                imageReader.setOnImageAvailableListener(null, null);
                smediaProjection.unregisterCallback(this);
            }
        }, null);
        return true;
    }
    public void saveImage(){
        try{
        File dest=new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)),System.currentTimeMillis() + ".jpg");
        FileOutputStream fileOutputStream=new FileOutputStream(dest);


        if (fileOutputStream != null) {
            if (!sbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            stopSelf();
            System.exit(0);

        }else{
            Log.e("12345","Error1");
        }


    } catch (Throwable e) {
        // Several error may come out with file handling or DOM
        e.printStackTrace();
            Log.e("12345","Error2");

    }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestScreenshotPermission(sactivity,srequestId);

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {

            smediaProjection = null;
            Log.i(TAG, "MediaProjection Stopped");
        }
    }


}


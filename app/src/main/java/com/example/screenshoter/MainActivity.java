package com.example.screenshoter;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.example.screenshoter.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnStart.setVisibility(View.VISIBLE);

        getPermission();

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Widget.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.canDrawOverlays(MainActivity.this)){
                        getPermission();
                    }else {

                        binding.btnStart.setVisibility(View.GONE);
                        startService(intent);
                        finish();


                    }
                }

            }
        });

    }
    public void getPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
            Intent intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent,2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!Settings.canDrawOverlays(MainActivity.this)){
                    Toast.makeText(this, "Permission denied by user.", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }



}
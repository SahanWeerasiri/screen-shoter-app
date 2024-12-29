package com.example.screenshoter;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;

public class Widget extends Service {


    Button btn_take_screen_shot;
    ToggleButton btn_take_video;
    ConstraintLayout layout;

    int LAYOUT_FLAG,height,width;
    View mFloatingView;
    WindowManager windowManager;
    ImageView imageClose;
    long starClickTime;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_widget, null);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams.x = 0;
        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        layoutParams.y = 100;


        WindowManager.LayoutParams imageParams = new WindowManager.LayoutParams(140,
                140,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        imageParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        imageParams.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        imageClose = new ImageView(this);
        imageClose.setImageResource(R.drawable.batman);
        imageClose.setVisibility(View.INVISIBLE);
        windowManager.addView(imageClose, imageParams);
        windowManager.addView(mFloatingView, layoutParams);
        mFloatingView.setVisibility(View.VISIBLE);

        height=windowManager.getDefaultDisplay().getHeight();
        width=windowManager.getDefaultDisplay().getWidth();



        btn_take_screen_shot = (Button) mFloatingView.findViewById(R.id.btn_screenshot);
        btn_take_video=(ToggleButton)mFloatingView.findViewById(R.id.btn_video);
        layout=(ConstraintLayout)mFloatingView.findViewById(R.id.btn_widget);

        btn_take_screen_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Widget.this,Shot.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("action","SHOT");
                //btn_take_screen_shot.setVisibility(View.GONE);
                startActivity(intent1);
            }
        });
        btn_take_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((ToggleButton)v).isChecked()){
                    Intent intent1=new Intent(Widget.this,Shot.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("action","VIDEO_ON");
                    startActivity(intent1);
                }else{
                    Intent intent1=new Intent(Widget.this,Shot.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("action","VIDEO_OFF");
                    startActivity(intent1);

                }

            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            int initialX,initialY;
            float initialTouchX,initialTouchY;
            int MAX_CLICK_DURATION=200;
            @Override
            public boolean onTouch(View v,MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        starClickTime= Calendar.getInstance().getTimeInMillis();
                        imageClose.setVisibility(View.VISIBLE);

                        initialX=layoutParams.x;
                        initialY=layoutParams.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        return true;
                    case MotionEvent.ACTION_UP:
                        long clickDuration=Calendar.getInstance().getTimeInMillis()-starClickTime;
                        imageClose.setVisibility(View.GONE);



                        layoutParams.x=initialX+(int)(initialTouchX-event.getRawX());
                        layoutParams.y=initialY+(int)(event.getRawY()-initialTouchY);

                        if(clickDuration>MAX_CLICK_DURATION){
                            if(layoutParams.y>(height*0.8)){
                                stopSelf();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x=initialX+(int)(initialTouchX-event.getRawX());
                        layoutParams.y=initialY+(int)(event.getRawY()-initialTouchY);


                        windowManager.updateViewLayout(mFloatingView,layoutParams);
                        if(layoutParams.y>(height*0.8)){
                            imageClose.setImageResource(R.drawable.batman);
                        }else{
                            imageClose.setImageResource(R.drawable.logo);
                        }
                        return true;
                }
                return false;
            }


        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatingView!=null){
            windowManager.removeView(mFloatingView);
        }
        if(imageClose!=null){
            windowManager.removeView(imageClose);
        }
    }





}
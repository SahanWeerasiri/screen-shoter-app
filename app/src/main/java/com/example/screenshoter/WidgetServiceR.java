package com.example.screenshoter;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface WidgetServiceR {
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
}

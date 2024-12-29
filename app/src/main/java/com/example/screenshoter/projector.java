package com.example.screenshoter;

import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Parcel;
import android.os.Parcelable;

public class projector implements Parcelable {
    ImageReader imageReader;

    protected projector(Parcel in) {

    }

    public static final Creator<projector> CREATOR = new Creator<projector>() {
        @Override
        public projector createFromParcel(Parcel in) {
            return new projector(in);
        }

        @Override
        public projector[] newArray(int size) {
            return new projector[size];
        }
    };

    public projector(ImageReader imageReader) {
        this.imageReader = imageReader;
    }

    public ImageReader getImageReader() {
        return imageReader;
    }

    public void setImageReader(ImageReader imageReader) {
        this.imageReader = imageReader;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}


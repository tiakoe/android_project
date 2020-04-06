package com.example.myapplication.custom_video;

import android.content.Context;
import android.widget.VideoView;

public class ExampleCustomVideo extends VideoView {
    public ExampleCustomVideo(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(1024, widthMeasureSpec);
        int height = getDefaultSize(576, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}

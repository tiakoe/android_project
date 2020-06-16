package com.a.fun_module.customview.view.wave;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.a.fun_module.customview.view.BaseView;


public class WaveView extends BaseView {

    //每个波浪的宽度占据View宽度的默认比例
    private static final float DEFAULT_WAVE_SCALE_WIDTH = 0.5f;

    //每个波浪的高度占据View高度的默认比例
    private static final float DEFAULT_WAVE_SCALE_HEIGHT = 0.03f;

    //波浪的默认速度
    private static final long DEFAULT_SPEED = 2000;

    private float waveScaleWidth;

    private float waveScaleHeight;

    private Paint paint;

    private int contentWidth;

    private int contentHeight;

    //每个波浪的起伏高度
    private float waveHeight;

    //每个波浪的宽度
    private float waveWidth;

    //波浪的速度
    private long speed = DEFAULT_SPEED;

    private float animatedValue;

    private ValueAnimator valueAnimator;

    @ColorInt
    private int bgColor = Color.parseColor("#f54183");

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAnimation();
        setWaveScaleWidth(DEFAULT_WAVE_SCALE_WIDTH);
        setWaveScaleHeight(DEFAULT_WAVE_SCALE_HEIGHT);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL);
    }

    public void initAnimation() {
        valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(speed);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getSize(widthMeasureSpec, getResources().getDisplayMetrics().widthPixels),
                getSize(heightMeasureSpec, getResources().getDisplayMetrics().heightPixels));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        contentWidth = w;
        contentHeight = h;
        resetWaveParams();
    }

    private Path path = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.moveTo(-waveWidth + animatedValue, contentHeight / 2);
        for (float i = -waveWidth; i < contentWidth + waveWidth; i += waveWidth) {
            path.rQuadTo(waveWidth / 4, -waveHeight, waveWidth / 2, 0);
            path.rQuadTo(waveWidth / 4, waveHeight, waveWidth / 2, 0);
        }
        path.lineTo(contentWidth, contentHeight);
        path.lineTo(0, contentHeight);
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void start() {
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    public void stop() {
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
    }

    public void setWaveScaleWidth(float waveScaleWidth) {
        if (waveScaleWidth <= 0 || waveScaleWidth > 1) {
            return;
        }
        this.waveScaleWidth = waveScaleWidth;
        resetWaveParams();
    }

    public float getWaveScaleWidth() {
        return waveScaleWidth;
    }

    public void setWaveScaleHeight(float waveScaleHeight) {
        if (waveScaleWidth <= 0 || waveScaleWidth > 1) {
            return;
        }
        this.waveScaleHeight = waveScaleHeight;
        resetWaveParams();
    }

    public float getWaveScaleHeight() {
        return waveScaleHeight;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
        resetWaveParams();
    }

    private void resetWaveParams() {
        waveWidth = contentWidth * waveScaleWidth;
        waveHeight = contentHeight * waveScaleHeight;
        if (valueAnimator != null) {
            valueAnimator.setFloatValues(0, waveWidth);
            valueAnimator.setDuration(speed);
        }
    }

}

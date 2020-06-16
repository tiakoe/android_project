package com.a.fun_module.customview.view.waveLoading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a.fun_module.R;
import com.a.fun_module.customview.view.BaseView;


public class WaveLoadingView extends BaseView {

    //每个波浪的宽度占据View宽度的默认比例
    private static final float DEFAULT_WAVE_SCALE_WIDTH = 0.8f;

    //每个波浪的高度占据View高度的默认比例
    private static final float DEFAULT_WAVE_SCALE_HEIGHT = 0.13f;

    //波浪的默认颜色
    private static final int DEFAULT_WAVE_COLOR = Color.parseColor("#f54183");

    //文本下方的默认颜色
    private static final int DEFAULT_DOWN_TEXT_COLOR = Color.WHITE;

    //默认文本大小，sp
    private static final int DEFAULT_TEXT_SIZE = 150;

    //波浪的默认速度
    private static final int DEFAULT_SPEED = 900;

    //View的默认大小，dp
    private static final int DEFAULT_SIZE = 220;

    private float waveScaleWidth;

    private float waveScaleHeight;

    @ColorInt
    private int waveColor;

    @ColorInt
    private int downTextColor;

    //每个波浪的起伏高度
    private float waveHeight;

    //每个波浪的宽度
    private float waveWidth;

    //波浪的速度
    private long speed = DEFAULT_SPEED;

    private float textSize;

    private char text;

    private Paint wavePaint;

    private Paint textPaint;

    private float size;

    private float animatedValue;

    private ValueAnimator valueAnimator;

    public WaveLoadingView(Context context) {
        this(context, null);
    }

    public WaveLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
        initPaint();
        initAnimation();
        resetWaveParams();
    }

    private void initAttributeSet(Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WaveLoadingView);
        waveScaleWidth = array.getFloat(R.styleable.WaveLoadingView_scaleWidth, DEFAULT_WAVE_SCALE_WIDTH);
        waveScaleHeight = array.getFloat(R.styleable.WaveLoadingView_scaleHeight, DEFAULT_WAVE_SCALE_HEIGHT);
        waveColor = array.getColor(R.styleable.WaveLoadingView_waveColor, DEFAULT_WAVE_COLOR);
        downTextColor = array.getColor(R.styleable.WaveLoadingView_downTextColor, DEFAULT_DOWN_TEXT_COLOR);
        textSize = array.getDimension(R.styleable.WaveLoadingView_textSize, sp2px(DEFAULT_TEXT_SIZE));
        speed = array.getInteger(R.styleable.WaveLoadingView_speed, DEFAULT_SPEED);
        String centerText = array.getString(R.styleable.WaveLoadingView_centerText);
        if (centerText != null && centerText.length() > 0) {
            text = centerText.charAt(0);
        }
        array.recycle();
    }

    private void initPaint() {
        wavePaint = new Paint();
        wavePaint.setAntiAlias(true);
        wavePaint.setDither(true);
        wavePaint.setColor(waveColor);
        wavePaint.setStyle(Paint.Style.FILL);
        wavePaint.setStrokeWidth(0f);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextSize(textSize);
    }

    private void initAnimation() {
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
        int defaultSize = dp2px(DEFAULT_SIZE);
        int width = getSize(widthMeasureSpec, defaultSize);
        int height = getSize(heightMeasureSpec, defaultSize);
        width = height = Math.min(width, height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        size = Math.min(w, h);

        radius = size / 2f;
        centerX = radius;
        centerY = radius;

        resetWaveParams();

        Log.e(TAG, "size: " + size);
        Log.e(TAG, "radius: " + radius);
        Log.e(TAG, "centerX: " + centerX);
        Log.e(TAG, "centerY: " + centerY);
    }

    private Path circlePath = new Path();

    private Path wavePath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        textPaint.setColor(waveColor);
        drawText(canvas, textPaint, String.valueOf(text));

        wavePath.reset();
        wavePath.moveTo(-waveWidth + animatedValue, size / 2.2f);
        for (float i = -waveWidth; i < size + waveWidth; i += waveWidth) {
            wavePath.rQuadTo(waveWidth / 4f, -waveHeight, waveWidth / 2f, 0);
            wavePath.rQuadTo(waveWidth / 4f, waveHeight, waveWidth / 2f, 0);
        }
        wavePath.lineTo(size, size);
        wavePath.lineTo(0, size);
        wavePath.close();

        circlePath.reset();
        circlePath.addCircle(centerX, centerY, radius - 1, Path.Direction.CCW);
        circlePath.op(wavePath, Path.Op.INTERSECT);

        canvas.drawPath(circlePath, wavePaint);
        canvas.clipPath(circlePath);

        textPaint.setColor(downTextColor);
        drawText(canvas, textPaint, String.valueOf(text));
    }

    private void drawText(Canvas canvas, Paint textPaint, String text) {
        RectF rect = new RectF(0, 0, size, size);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        float centerY = rect.centerY() - top / 2f - bottom / 2f;
        canvas.drawText(text, rect.centerX(), centerY, textPaint);
    }

    private float radius;

    private float centerX;

    private float centerY;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
        Log.e(TAG, "onAttachedToWindow: ");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
        Log.e(TAG, "onDetachedFromWindow: ");
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        switch (visibility) {
            case View.VISIBLE: {
                start();
                break;
            }
            case View.INVISIBLE:
            case View.GONE: {
                stop();
                break;
            }
        }
        Log.e(TAG, "onVisibilityChanged: " + visibility);
    }

    public void start() {
        if (valueAnimator != null && !valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    public void stop() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
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
        waveWidth = size * waveScaleWidth;
        waveHeight = size * waveScaleHeight;
        if (valueAnimator != null) {
            valueAnimator.setFloatValues(0, waveWidth);
            valueAnimator.setDuration(speed);
        }
    }

    public void setText(char text) {
        this.text = text;
    }

}

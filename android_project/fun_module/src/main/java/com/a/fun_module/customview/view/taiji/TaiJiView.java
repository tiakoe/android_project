package com.a.fun_module.customview.view.taiji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.a.fun_module.customview.view.BaseView;


public class TaiJiView extends BaseView {

    private Paint paint;

    public TaiJiView(Context context) {
        this(context, null);
    }

    public TaiJiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaiJiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getSize(widthMeasureSpec, getResources().getDisplayMetrics().widthPixels);
        int height = getSize(heightMeasureSpec, getResources().getDisplayMetrics().heightPixels);
        width = height = Math.min(width, height);
        setMeasuredDimension(width, height);
    }

    private float radius;

    private float centerX;

    private float centerY;

    private float degrees;

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        w = w - getPaddingLeft() - getPaddingRight();
        h = h - getPaddingTop() - getPaddingBottom();
        w = Math.min(w, h);
        radius = w / 2f;
        centerX = getPaddingLeft() + radius;
        centerY = getPaddingTop() + radius;
    }

    private RectF rectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        //稍稍留一点间距
        float realRadius = radius - 8;
        float temp1 = realRadius / 2f;
        float temp2 = temp1 / 8f;

        canvas.translate(centerX, centerY);
        canvas.rotate(degrees);

        //绘制边框
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.4f);
        canvas.drawCircle(0, 0, realRadius, paint);

        //绘制左右半圆
        rectF.set(-realRadius, -realRadius, realRadius, realRadius);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);

        paint.setColor(Color.BLACK);
        canvas.drawArc(rectF, 90, 180, true, paint);
        paint.setColor(Color.WHITE);
        canvas.drawArc(rectF, -90, 180, true, paint);

        //绘制上边的白色圆
        canvas.save();
        canvas.translate(0, -temp1);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        canvas.drawCircle(0, 0, temp1, paint);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(0, 0, temp2, paint);
        canvas.restore();

        //绘制上边的黑色圆
        canvas.save();
        canvas.translate(0, temp1);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, temp1, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(0, 0, temp2, paint);
        canvas.restore();
    }

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
        postInvalidate();
    }

}

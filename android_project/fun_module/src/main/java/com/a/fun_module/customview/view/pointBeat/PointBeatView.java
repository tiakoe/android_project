package com.a.fun_module.customview.view.pointBeat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a.fun_module.customview.view.BaseView;


public class PointBeatView extends BaseView {

    private static class Point {

        private float x;

        private float y;

        private float radius;

    }

    //小球
    private Point ballPoint;

    //贝塞尔曲线控制点
    private Point controlPoint;

    private ValueAnimator downAnimator;

    private ValueAnimator upAnimator;

    private float lineY;

    private float lineXLeft;

    private float lineXRight;

    //小球最高点Y坐标
    private float pointYMin;

    private Paint paint;

    public PointBeatView(Context context) {
        this(context, null);
    }

    public PointBeatView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointBeatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ballPoint = new Point();
        controlPoint = new Point();
        initPaint();
        initAnimator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getSize(widthMeasureSpec, getResources().getDisplayMetrics().widthPixels);
        int height = getSize(heightMeasureSpec, getResources().getDisplayMetrics().heightPixels);
        setMeasuredDimension(width, height);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int contentWidth, int contentHeight, int oldW, int oldH) {
        super.onSizeChanged(contentWidth, contentHeight, oldW, oldH);
        lineY = contentHeight * 0.5f;
        lineXLeft = contentWidth * 0.15f;
        lineXRight = contentWidth * 0.85f;

        //小球最低点Y坐标
        float pointYMax = contentHeight * 0.55f;
        pointYMin = contentHeight * 0.22f;

        ballPoint.x = contentWidth * 0.5F;
        ballPoint.radius = 26;
        ballPoint.y = pointYMin;

        controlPoint.x = ballPoint.x;

        long speed = 1500;
        downAnimator.setFloatValues(pointYMin, pointYMax);
        upAnimator.setFloatValues(pointYMax, pointYMin);
        downAnimator.setDuration(speed);
        upAnimator.setDuration((long) (0.8 * speed));
        start();
    }

    private Path path = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8f);

        path.reset();
        path.moveTo(lineXLeft, lineY);
        path.quadTo(controlPoint.x, controlPoint.y, lineXRight, lineY);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(lineXLeft, lineY, 16, paint);
        canvas.drawCircle(lineXRight, lineY, 16, paint);

        paint.setColor(Color.parseColor("#f7584d"));
        paint.setStrokeWidth(0f);
        canvas.drawCircle(ballPoint.x, ballPoint.y, ballPoint.radius, paint);
    }

    private void initAnimator() {
        downAnimator = new ValueAnimator();
        //加速下降
        downAnimator.setInterpolator(new AccelerateInterpolator());
        downAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ballPoint.y = (float) animation.getAnimatedValue();
                if (ballPoint.y + ballPoint.radius <= lineY) {
                    controlPoint.y = lineY;
                } else {
                    controlPoint.y = lineY + 2 * (ballPoint.y + ballPoint.radius - lineY);
                }
                invalidate();
            }
        });
        downAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startUpAnimator();
            }
        });

        upAnimator = new ValueAnimator();
        //减速上升
        upAnimator.setInterpolator(new DecelerateInterpolator());
        upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ballPoint.y = (float) animation.getAnimatedValue();
                if (ballPoint.y + ballPoint.radius >= lineY) { //还处于水平线以下
                    controlPoint.y = lineY + 2 * (ballPoint.y + ballPoint.radius - lineY);
                } else {
                    //小球总的要上升的距离
                    float tempY = lineY - pointYMin;
                    //小球最低点距离水平线的距离，即小球已上升的距离
                    float distance = lineY - ballPoint.y - ballPoint.radius;
                    //上升比例
                    float percentage = distance / tempY;
                    if (percentage <= 0.2) {  //线从水平线升高到最高点
                        controlPoint.y = lineY + 2 * (ballPoint.y + ballPoint.radius - lineY);
                    } else if (percentage <= 0.28) { //线从最高点降落到水平线
                        controlPoint.y = lineY - (distance - tempY * 0.2f);
                    } else if (percentage <= 0.34) { //线从水平线降落到最低点
                        controlPoint.y = lineY + (distance - tempY * 0.28f);
                    } else if (percentage <= 0.39) { //线从最低点升高到水平线
                        controlPoint.y = lineY - (distance - tempY * 0.34f);
                    } else {
                        controlPoint.y = lineY;
                    }
                }
                invalidate();
            }
        });
        upAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startDownAnimator();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
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
        startDownAnimator();
    }

    public void stop() {
        stopDownAnimator();
        stopUpAnimator();
    }

    private void startDownAnimator() {
        if (downAnimator != null && downAnimator.getValues() != null && downAnimator.getValues().length > 0 && !downAnimator.isRunning()) {
            downAnimator.start();
        }
    }

    private void stopDownAnimator() {
        if (downAnimator != null && downAnimator.isRunning()) {
            downAnimator.cancel();
        }
    }

    private void startUpAnimator() {
        if (upAnimator != null && upAnimator.getValues() != null && upAnimator.getValues().length > 0 && !upAnimator.isRunning()) {
            upAnimator.start();
        }
    }

    private void stopUpAnimator() {
        if (upAnimator != null && upAnimator.isRunning()) {
            upAnimator.cancel();
        }
    }

}

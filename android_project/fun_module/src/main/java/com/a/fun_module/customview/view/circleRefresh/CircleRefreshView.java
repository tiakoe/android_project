package com.a.fun_module.customview.view.circleRefresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.a.fun_module.customview.view.BaseView;

import java.util.ArrayList;
import java.util.List;


public class CircleRefreshView extends BaseView {

    private final static class Circle {

        private float x;
        private float y;
        private float radius;
        private int color;

        private Circle(float x, float y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }
    }

    private final int LEFT = 0;

    private final int RIGHT = 1;

    private final int CENTER = 2;

    //View的默认宽度，dp
    private static final int DEFAULT_WIDTH = 80;

    //View的默认高度，dp
    private static final int DEFAULT_HEIGHT = 50;

    //每个圆的默认最大半径
    private static final int DEFAULT_MAX_RADIUS = 8;

    //每个圆的默认最小半径
    private static final int DEFAULT_MIN_RADIUS = 6;

    //默认速度
    private static final long DEFAULT_SPEED = 3000;

    private long speed = DEFAULT_SPEED;

    private int contentWidth;

    private int contentHeight;

    //中心圆与相邻两个圆的圆心间隔
    private int gap;

    //圆最大半径
    private int maxRadius;

    //圆最小半径
    private int minRadius;

    //绿色
    private int colorCircleLeft = Color.parseColor("#008577");
    //橙色
    private int colorCircleCenter = Color.parseColor("#f96630");
    //红色
    private int colorCircleRight = Color.parseColor("#f54183");

    private List<Circle> circleList;

    private Paint paint;

    private ValueAnimator animator;

    public CircleRefreshView(Context context) {
        this(context, null);
    }

    public CircleRefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circleList = new ArrayList<>(3);
        maxRadius = dp2px(DEFAULT_MAX_RADIUS);
        minRadius = dp2px(DEFAULT_MIN_RADIUS);
        initPaint();
        initAnimator();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    private void initAnimator() {
        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(speed);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //当动画启动时，将三个圆的位置重置到准备开启动画的临界状态
                resetToStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //循环刷新三个圆的位置
                for (int i = 0; i < circleList.size(); i++) {
                    updateCircle(i, animation.getAnimatedFraction());
                }
                invalidate();
            }
        });
    }

    //将三个圆的位置重置到准备开启动画的临界状态
    private void resetToStart() {
        Circle circle = circleList.get(LEFT);
        circle.x = minRadius;
        circle.radius = minRadius;

        circle = circleList.get(RIGHT);
        circle.x = contentWidth - minRadius;
        circle.radius = minRadius;

        circle = circleList.get(CENTER);
        circle.x = contentWidth / 2;
        circle.radius = maxRadius;

        invalidate();
    }

    private void updateCircle(int index, float fraction) {
        //             x              x               x
        // ------------||-------------||--------------||------------
        //            1/4            2/4             3/4
        //      1/4            2/4            3/4            4/4

        //   左边-绿色
        // 半径从0到min  半径从min到max    半径从max到min   半径从min到0

        //   中间-橙色
        //                                半径从max到min   半径从min到0
        //半径从0到min   半径从min到max

        //   右边-红色
        //                                                 半径从min到0
        //半径从0到min  半径从min到max     半径从max到min

        float radius = 0;
        float x = 0;
        switch (index) {
            case LEFT: {
                if (fraction <= 1f / 4f) {
                    radius = minRadius * (4f * fraction);
                    x = minRadius;
                } else if (fraction <= 0.5f) {
                    float percent = (fraction - 1f / 4f) * 4f;
                    radius = minRadius + percent * (maxRadius - minRadius);
                    x = minRadius + percent * (contentWidth / 2f - minRadius);
                } else if (fraction <= 3f / 4f) {
                    float percent = (fraction - 0.5f) * 4f;
                    radius = maxRadius - percent * (maxRadius - minRadius);
                    x = contentWidth / 2f + percent * (contentWidth / 2f - minRadius);
                } else {
                    radius = minRadius - (fraction - 3f / 4f) * 4f * minRadius;
                    x = contentWidth - minRadius;
                }
                break;
            }
            case CENTER: {
                if (fraction <= 1f / 4f) {
                    float percent = fraction * 4f;
                    radius = maxRadius - (maxRadius - minRadius) * percent;
                    x = contentWidth / 2f + (contentWidth / 2f - minRadius) * percent;
                } else if (fraction <= 0.5f) {
                    radius = minRadius - (fraction - 1f / 4f) * 4f * minRadius;
                    x = contentWidth - minRadius;
                } else if (fraction <= 3f / 4f) {
                    radius = minRadius * (4f * (fraction - 0.5f));
                    x = minRadius;
                } else {
                    float percent = (fraction - 3f / 4f) * 4f;
                    radius = minRadius + (maxRadius - minRadius) * percent;
                    x = minRadius + (contentWidth / 2f - minRadius) * percent;
                }
                break;
            }
            case RIGHT: {
                if (fraction <= 1f / 4f) {
                    radius = minRadius - 4f * fraction * minRadius;
                    x = contentWidth - minRadius;
                } else if (fraction <= 0.5f) {
                    radius = minRadius * (4f * (fraction - 1f / 4f));
                    x = minRadius;
                } else if (fraction <= 3f / 4f) {
                    float percent = (fraction - 0.5f) * 4f;
                    radius = minRadius + (maxRadius - minRadius) * percent;
                    x = minRadius + (contentWidth / 2f - minRadius) * percent;
                } else {
                    float percent = (fraction - 3f / 4f) * 4f;
                    radius = maxRadius - (maxRadius - minRadius) * percent;
                    x = contentWidth / 2f + (contentWidth / 2f - minRadius) * percent;
                }
                break;
            }
        }
        Circle circle = circleList.get(index);
        circle.radius = radius;
        circle.x = x;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        contentWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        contentHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        resetCircles();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getSize(widthMeasureSpec, dp2px(DEFAULT_WIDTH));
        int height = getSize(heightMeasureSpec, dp2px(DEFAULT_HEIGHT));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Circle circle : circleList) {
            paint.setColor(circle.color);
            canvas.drawCircle(circle.x + getPaddingLeft(), circle.y + getPaddingTop(), circle.radius, paint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void drag(float fraction) {
        if (animator != null && animator.isRunning()) {
            return;
        }
        if (fraction > 1) {
            return;
        }
        circleList.get(LEFT).x = minRadius + gap * (1f - fraction);
        circleList.get(RIGHT).x = contentWidth / 2 + gap * fraction;
        invalidate();
    }

    private void resetCircles() {
        int x = contentWidth / 2;
        int y = contentHeight / 2;
        if (circleList.isEmpty()) {
            gap = x - minRadius;
            Circle circleLeft = new Circle(x, y, minRadius, colorCircleLeft);
            Circle circleCenter = new Circle(x, y, maxRadius, colorCircleCenter);
            Circle circleRight = new Circle(x, y, minRadius, colorCircleRight);
            circleList.add(LEFT, circleLeft);
            circleList.add(RIGHT, circleRight);
            circleList.add(CENTER, circleCenter);
        } else {
            for (int i = 0; i < circleList.size(); i++) {
                Circle circle = circleList.get(i);
                circle.x = x;
                circle.y = y;
                if (i == CENTER) {
                    circle.radius = maxRadius;
                } else {
                    circle.radius = minRadius;
                }
            }
        }
    }

    public void start() {
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    public void stop() {
        if (animator.isRunning()) {
            animator.cancel();
            resetCircles();
            invalidate();
        }
    }

    public void setSpeed(long speed) {
        this.speed = speed;
        animator.setDuration(speed);
    }

    public long getSpeed() {
        return speed;
    }

}

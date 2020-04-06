package com.example.myapplication.sensor_event;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication.R;

/**
 * 自定义的“水平仪”控件
 * 通过设置{@link #setAngle(double, double)}
 */
public class LevelView extends View {

    /**
     * 最大圈半径
     */
    private float mLimitRadius = 0;

    /**
     * 气泡半径
     */
    private float mBubbleRadius;

    /**
     * 最大限制圈颜色
     */
    private int mLimitColor;

    /**
     * 限制圈宽度
     */
    private float mLimitCircleWidth;


    /**
     * 气泡中心标准圆颜色
     */
    private int mBubbleRuleColor;

    /**
     * 气泡中心标准圆宽
     */
    private float mBubbleRuleWidth;

    /**
     * 气泡中心标准圆半径
     */
    private float mBubbleRuleRadius;

    /**
     * 水平后的颜色
     */
    private int mHorizontalColor;

    /**
     * 气泡颜色
     */
    private int mBubbleColor;

    /**
     * 气泡绘制
     */
    private Paint mBubblePaint;
    /**
     * 外限制区绘制
     */
    private Paint mLimitPaint;
    /**
     * 标准圈绘制
     */
    private Paint mBubbleRulePaint;

    /**
     * 中心点坐标
     */
    private PointF centerPnt = new PointF();

    /**
     * 计算后的气泡点
     */
    private PointF bubblePoint;

    /**
     * 振子,震动
     */
    private Vibrator vibrator;

    public LevelView(Context context) {
        super(context);
        init(null, 0);
    }

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LevelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.LevelView, defStyle, 0);

        //        水平后颜色
        mHorizontalColor = typedArray.getColor(R.styleable.LevelView_horizontalColor,
                mHorizontalColor);

        //        移动圆
        mBubbleColor = typedArray.getColor(R.styleable.LevelView_bubbleColor, mBubbleColor);
        mBubbleRadius = typedArray.getDimension(R.styleable.LevelView_bubbleRadius, mBubbleRadius);


        //        限制圆
        mLimitColor = typedArray.getColor(R.styleable.LevelView_limitColor, mLimitColor);
        mLimitRadius = typedArray.getDimension(R.styleable.LevelView_limitRadius, mLimitRadius);
        mLimitCircleWidth = typedArray.getDimension(R.styleable.LevelView_limitCircleWidth,
                mLimitCircleWidth);

        //        标准圆
        mBubbleRuleColor = typedArray.getColor(R.styleable.LevelView_bubbleRuleColor,
                mBubbleRuleColor);
        mBubbleRuleRadius = typedArray.getDimension(R.styleable.LevelView_bubbleRuleRadius,
                mBubbleRuleRadius);
        mBubbleRuleWidth = typedArray.getDimension(R.styleable.LevelView_bubbleRuleWidth,
                mBubbleRuleWidth);

        typedArray.recycle();

        //        气泡绘制
        mBubblePaint = new Paint();
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);
        mBubblePaint.setAntiAlias(true);

        //        限制圈绘制
        mLimitPaint = new Paint();
        mLimitPaint.setStyle(Paint.Style.STROKE);
        mLimitPaint.setColor(mLimitColor);
        mLimitPaint.setStrokeWidth(mLimitCircleWidth);
        mLimitPaint.setAntiAlias(true);//抗锯齿

        //        标准圈绘制
        mBubbleRulePaint = new Paint();
        mBubbleRulePaint.setColor(mBubbleRuleColor);
        mBubbleRulePaint.setStyle(Paint.Style.STROKE);
        mBubbleRulePaint.setStrokeWidth(mBubbleRuleWidth);
        mBubbleRulePaint.setAntiAlias(true);

        //振动器
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateCenter(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算中心
     *
     * @param widthMeasureSpec  宽度
     * @param heightMeasureSpec 高度
     */
    private void calculateCenter(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.UNSPECIFIED);
        int center = Math.min(width, height) / 2;
        centerPnt.set(center, center);
    }


    /**
     * @param pitchAngle （弧度）
     * @param rollAngle  (弧度)
     */
    protected void setAngle(double rollAngle, double pitchAngle) {

        //气泡圆心的活动半径
        float limitRadius = mLimitRadius - mBubbleRadius;
        bubblePoint = convertCoordinate(rollAngle, pitchAngle, mLimitRadius);
        outLimit(bubblePoint, limitRadius);

        //坐标超出最大圆，取法向圆上的点
        if (outLimit(bubblePoint, limitRadius)) {
            onCirclePoint(bubblePoint, limitRadius);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean isCenter = isCenter(bubblePoint);
        int limitCircleColor = isCenter ? mHorizontalColor : mLimitColor;
        int bubbleColor = isCenter ? mHorizontalColor : mBubbleColor;

        //水平时振动
        if (isCenter) {
            vibrator.vibrate(1000);
        }

        mBubblePaint.setColor(bubbleColor);
        mLimitPaint.setColor(limitCircleColor);

        //        绘制标准圈
        canvas.drawCircle(centerPnt.x, centerPnt.y, mBubbleRuleRadius, mBubbleRulePaint);
        //        绘制限制圈
        canvas.drawCircle(centerPnt.x, centerPnt.y, mLimitRadius, mLimitPaint);
        //        绘制气泡
        drawBubble(canvas);

    }

    private boolean isCenter(PointF bubblePoint) {
        if (bubblePoint == null) {
            return false;
        }
        return Math.abs(bubblePoint.x - centerPnt.x) < 1 && Math.abs(bubblePoint.y - centerPnt.y) < 1;
    }

    private void drawBubble(Canvas canvas) {
        if (bubblePoint != null) {
            canvas.drawCircle(bubblePoint.x, bubblePoint.y, mBubbleRadius, mBubblePaint);
        }
    }

    /**
     * Convert angle to screen coordinate point.
     *
     * @param rollAngle  横滚角(弧度)
     * @param pitchAngle 俯仰角(弧度)
     * @return 弧度转化后的坐标
     */
    private PointF convertCoordinate(double rollAngle, double pitchAngle, double radius) {
        double scale = radius / Math.toRadians(90);

        //以圆心为原点，使用弧度表示坐标
        double x0 = -(rollAngle * scale);
        double y0 = -(pitchAngle * scale);

        //使用屏幕坐标表示气泡点
        double x = centerPnt.x - x0;
        double y = centerPnt.y - y0;

        return new PointF((float) x, (float) y);
    }

    /**
     * 验证气泡点是否超过限制{@link #mLimitRadius}
     *
     * @param bubblePnt 气泡坐标
     * @return false
     */
    protected boolean outLimit(PointF bubblePnt, float limitRadius) {
        float cSqrt = (bubblePnt.x - centerPnt.x) * (bubblePnt.x - centerPnt.x)
                + (centerPnt.y - bubblePnt.y) * +(centerPnt.y - bubblePnt.y);
        return cSqrt - limitRadius * limitRadius > 0;
    }

    /**
     * 计算圆心到 bubblePnt点在圆上的交点坐标
     * 即超出圆后的最大圆上坐标
     *
     * @param bubblePnt   气泡点
     * @param limitRadius 限制圆的半径
     */
    private void onCirclePoint(PointF bubblePnt, double limitRadius) {
        double azimuth = Math.atan2((bubblePnt.y - centerPnt.y), (bubblePnt.x - centerPnt.x));
        azimuth = azimuth < 0 ? 2 * Math.PI + azimuth : azimuth;

        //圆心+半径+角度 求圆上的坐标
        double x1 = centerPnt.x + limitRadius * Math.cos(azimuth);
        double y1 = centerPnt.y + limitRadius * Math.sin(azimuth);

        bubblePnt.set((float) x1, (float) y1);

    }

}

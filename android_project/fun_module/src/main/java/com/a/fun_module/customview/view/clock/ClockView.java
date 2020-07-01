package com.a.fun_module.customview.view.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.a.fun_module.customview.view.BaseView;

import java.lang.ref.WeakReference;
import java.util.TimeZone;


public class ClockView extends BaseView {

    private final BroadcastReceiver timerBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                //监听时区的变化
                if (Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
                    time = new Time(TimeZone.getTimeZone(intent.getStringExtra("time-zone")).getID());
                }
            }
        }
    };

    private static final int MSG_INVALIDATE = 10;

    private static final class TimerHandler extends Handler {

        private WeakReference<ClockView> clockViewWeakReference;

        private TimerHandler(ClockView clockView) {
            clockViewWeakReference = new WeakReference<>(clockView);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INVALIDATE) {
                ClockView view = clockViewWeakReference.get();
                if (view != null) {
                    view.onTimeChanged();
                    view.invalidate();
                    sendEmptyMessageDelayed(MSG_INVALIDATE, 1000);
                }
            }
        }
    }

    private Paint clockPaint;

    private Paint textPaint;

    //View的默认大小，dp
    private static final int DEFAULT_SIZE = 320;

    //表盘边缘颜色
    private int aroundColor = Color.parseColor("#083476");

    //表盘中心点颜色
    private int clockCenterColor = Color.parseColor("#008577");

    //表盘边缘线的宽度
    private int aroundStockWidth = 12;

    //字体大小
    private int textSize = 28;

    private volatile Time time;

    private float hour;

    private float minute;

    private float second;

    private TimerHandler timerHandler;

    private Rect rect = new Rect();

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClockPaint();
        initTextPaint();
        time = new Time();
        timerHandler = new TimerHandler(this);
    }

    private void initClockPaint() {
        clockPaint = new Paint();
        clockPaint.setStyle(Paint.Style.STROKE);
        clockPaint.setAntiAlias(true);
        clockPaint.setDither(true);
        clockPaint.setStrokeWidth(aroundStockWidth);
    }

    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStrokeWidth(12);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = dp2px(DEFAULT_SIZE);
        int widthSize = getSize(widthMeasureSpec, defaultSize);
        int heightSize = getSize(heightMeasureSpec, defaultSize);
        widthSize = heightSize = Math.min(widthSize, heightSize);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, "onDetachedFromWindow");
        stopTimer();
        unregisterTimezoneAction();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.e(TAG, "onVisibilityChanged visibility: " + visibility);
        if (visibility == View.VISIBLE) {
            registerTimezoneAction();
            startTimer();
        } else {
            stopTimer();
            unregisterTimezoneAction();
        }
    }

    private void startTimer() {
        Log.e(TAG, "startTimer 开启定时任务");
        timerHandler.removeMessages(MSG_INVALIDATE);
        timerHandler.sendEmptyMessage(MSG_INVALIDATE);
    }

    private void stopTimer() {
        Log.e(TAG, "stopTimer 停止定时任务");
        timerHandler.removeMessages(MSG_INVALIDATE);
    }

    private void registerTimezoneAction() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        getContext().registerReceiver(timerBroadcast, filter);
    }

    private void unregisterTimezoneAction() {
        getContext().unregisterReceiver(timerBroadcast);
    }

    private void onTimeChanged() {
        time.setToNow();
        minute = time.minute;
        hour = time.hour + minute / 60.0f;
        second = time.second;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //中心点的横纵坐标
        float pointWH = getWidth() / 2.0f;
        //内圆的半径
        float radiusIn = pointWH - aroundStockWidth;

        canvas.translate(pointWH, pointWH);

        //绘制表盘
        if (aroundStockWidth > 0) {
            clockPaint.setStrokeWidth(aroundStockWidth);
            clockPaint.setStyle(Paint.Style.STROKE);
            clockPaint.setColor(aroundColor);
            canvas.drawCircle(0, 0, pointWH - aroundStockWidth / 2.0f, clockPaint);
        }
        clockPaint.setStyle(Paint.Style.FILL);
        clockPaint.setColor(Color.WHITE);
        canvas.drawCircle(0, 0, radiusIn, clockPaint);

        //绘制小短线
        canvas.save();
        canvas.rotate(-90);
        float longLineLength = radiusIn / 16.0f;
        float longStartY = radiusIn - longLineLength;
        float longStopY = longStartY - longLineLength;
        float longStockWidth = 2;
        float temp = longLineLength / 4.0f;
        float shortStartY = longStartY - temp;
        float shortStopY = longStopY + temp;
        float shortStockWidth = longStockWidth / 2.0f;
        clockPaint.setColor(Color.BLACK);
        float degrees = 6;
        for (int i = 0; i <= 360; i += degrees) {
            if (i % 30 == 0) {
                clockPaint.setStrokeWidth(longStockWidth);
                canvas.drawLine(0, longStartY, 0, longStopY, clockPaint);
            } else {
                clockPaint.setStrokeWidth(shortStockWidth);
                canvas.drawLine(0, shortStartY, 0, shortStopY, clockPaint);
            }
            canvas.rotate(degrees);
        }
        canvas.restore();

        //绘制时钟数字
        if (textSize > 0) {
            float x, y;
            for (int i = 1; i <= 12; i += 1) {
                textPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), rect);
                float textHeight = rect.height();
                float distance = radiusIn - 2 * longLineLength - textHeight;
                double tempVa = i * 30.0f * Math.PI / 180.0f;
                x = (float) (distance * Math.sin(tempVa));
                y = (float) (-distance * Math.cos(tempVa));
                canvas.drawText(String.valueOf(i), x, y + textHeight / 3, textPaint);
            }
        }

        canvas.rotate(-90);

        clockPaint.setStrokeWidth(2);
        //绘制时针
        canvas.save();
        canvas.rotate(hour / 12.0f * 360.0f);
        canvas.drawLine(-30, 0, radiusIn / 2.0f, 0, clockPaint);
        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(minute / 60.0f * 360.0f);
        canvas.drawLine(-30, 0, radiusIn * 0.7f, 0, clockPaint);
        canvas.restore();
        //绘制秒针
        clockPaint.setColor(Color.parseColor("#fff2204d"));
        canvas.save();
        canvas.rotate(second / 60.0f * 360.0f);
        canvas.drawLine(-30, 0, radiusIn * 0.85f, 0, clockPaint);
        canvas.restore();
        //绘制中心小圆点
        clockPaint.setStyle(Paint.Style.FILL);
        clockPaint.setColor(clockCenterColor);
        canvas.drawCircle(0, 0, radiusIn / 20.0f, clockPaint);
    }

    public void setAroundColor(int aroundColor) {
        this.aroundColor = aroundColor;
        invalidate();
    }

    public void setClockCenterColor(int clockCenterColor) {
        this.clockCenterColor = clockCenterColor;
        invalidate();
    }

    public void setAroundStockWidth(int aroundStockWidth) {
        this.aroundStockWidth = aroundStockWidth;
        invalidate();
    }

    public void setClockTextSize(int textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        invalidate();
    }

}

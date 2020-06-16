package com.a.fun_module.customview.view.plan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.a.fun_module.R;
import com.a.fun_module.customview.utils.DisplayUtils;

import java.util.List;

public class PlanView extends View {

    private static final String TAG = "PlanView";

    private static final int START_TIME = 5;

    private static final int END_TIME = 24;

    private static final String[] DAYS = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private static final String[] TIMES = new String[END_TIME - START_TIME + 1];

    static {
        for (int i = 0; i < (END_TIME - START_TIME + 1); i++) {
            StringBuilder time;
            int temp = i + START_TIME;
            if (temp < 10) {
                time = new StringBuilder("0");
                time.append(temp);
            } else {
                time = new StringBuilder(String.valueOf(temp));
            }
            time.append(":00");
            TIMES[i] = time.toString();
        }
    }

    private static final int DAY_TEXT_SIZE = 14;

    private static final int TIME_TEXT_SIZE = 12;

    private static final int PLAN_TEXT_SIZE = 12;

    private int lineColor = Color.parseColor("#e7e7e7");

    private int rectColor = Color.parseColor("#F8F8F8");

    private Rect textBounds = new Rect();

    private RectF planRectF = new RectF();

    private Paint bgPaint;

    private TextPaint planTextPaint;

    private int width;

    private int height;

    private int realHeight;

    private float itemHeight;

    private float itemWidth;

    private float headerHeight;

    private float leftTimeWidth;

    private float singleMinuteHeight;

    private List<PlanBean> planListBeanList;

    public interface OnPlanClickListener {

        void onClick(String planId, boolean isEllipsis);

    }

    private OnPlanClickListener planClickListener;

    public PlanView(Context context) {
        this(context, null);
    }

    public PlanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PlanView);
        final float itemSizeInScreen = array.getFloat(R.styleable.PlanView_planOffset, 9.5f);
        array.recycle();

        headerHeight = dp2px(35);
        width = getScreenWidth();
        itemWidth = getScreenWidth() / 8;
        leftTimeWidth = itemWidth;

        itemHeight = getScreenHeight() / itemSizeInScreen;
        height = (int) (itemHeight * (END_TIME - START_TIME) + headerHeight);
        realHeight = height + dp2px(20);
        singleMinuteHeight = itemHeight / 60;
    }

    private void initPaint() {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);

        planTextPaint = new TextPaint();
        planTextPaint.setColor(Color.WHITE);
        planTextPaint.setAntiAlias(true);
        planTextPaint.setDither(true);
        planTextPaint.setTextSize(sp2px(PLAN_TEXT_SIZE));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, realHeight);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        //先画背景
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, realHeight, bgPaint);

        //画左边和上边的边框
        bgPaint.setColor(rectColor);
        bgPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, leftTimeWidth, height, bgPaint);
        canvas.drawRect(leftTimeWidth, 0, width, headerHeight, bgPaint);

        //画线
        canvas.save();
        canvas.translate(leftTimeWidth, 0);
        bgPaint.setColor(lineColor);
        bgPaint.setStrokeWidth(getResources().getDisplayMetrics().density);
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(itemWidth * i, 0, itemWidth * i, height, bgPaint);
        }
        canvas.translate(0, headerHeight);
        for (int i = 0; i < 20; i++) {
            canvas.drawLine(0, i * itemHeight, width - leftTimeWidth + 2, i * itemHeight, bgPaint);
        }
        canvas.restore();

        //画星期数
        canvas.save();
        canvas.translate(leftTimeWidth, 0);
        bgPaint.setTextSize(sp2px(DAY_TEXT_SIZE));
        bgPaint.setColor(Color.BLACK);
        bgPaint.setTextAlign(Paint.Align.CENTER);
        for (String day : DAYS) {
            bgPaint.getTextBounds(day, 0, day.length(), textBounds);
            float offSet = (textBounds.top + textBounds.bottom) >> 1;
            canvas.drawText(day, itemWidth / 2, headerHeight / 2 - offSet, bgPaint);
            canvas.translate(itemWidth, 0);
        }
        canvas.restore();

        //画时间
        for (int i = 0; i < TIMES.length; i++) {
            String time = TIMES[i];
            bgPaint.getTextBounds(time, 0, time.length(), textBounds);
            float offSet = (textBounds.top + textBounds.bottom) >> 1;
            canvas.drawText(time, leftTimeWidth / 2, headerHeight + itemHeight * i - offSet, bgPaint);
        }

        if (planListBeanList != null && planListBeanList.size() > 0) {
            for (PlanBean bean : planListBeanList) {
                bgPaint.setColor(Color.parseColor(bean.getColor()));
                measurePlanBound(bean, planRectF);
                canvas.drawRect(planRectF, bgPaint);
                String planName = bean.getPlanName();
                if (TextUtils.isEmpty(planName)) {
                    continue;
                }
                float planItemHeight = planRectF.bottom - planRectF.top;
                StaticLayout staticLayout = null;
                for (int length = planName.length(); length > 0; length--) {
                    staticLayout = new StaticLayout(planName, planTextPaint, (int) (itemWidth - 4 * getResources().getDisplayMetrics().density),
                            Layout.Alignment.ALIGN_CENTER, 1.1f, 1.1f, true);
                    if (staticLayout.getHeight() > planItemHeight) {
                        planName = planName.substring(0, length) + "...";
                        bean.setEllipsis(true);
                    }
                }

                if (staticLayout == null) {
                    staticLayout = new StaticLayout(planName, planTextPaint, (int) (itemWidth - 4 * getResources().getDisplayMetrics().density),
                            Layout.Alignment.ALIGN_CENTER, 1.1f, 1.1f, true);
                }

                if (staticLayout.getHeight() > planItemHeight) {
                    continue;
                }

                canvas.save();

                canvas.translate(planRectF.left + (itemWidth - staticLayout.getWidth()) / 2, planRectF.top + (planItemHeight - staticLayout.getHeight()) / 2);

                staticLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    private void measurePlanBound(PlanBean bean, RectF rect) {
        measurePlanBound(bean.getDayIndex(), bean.getPlanStartTime(), bean.getPlanEndTime(), rect);
        RectF rectF = new RectF(rect);
        bean.setRectF(rectF);
    }

    private void measurePlanBound(int day, String startTime, String endTime, RectF rect) {
        try {
            float left = leftTimeWidth + itemWidth * (day - 1);
            float right = left + itemWidth;
            String[] split = startTime.split(":");
            int startHour = Integer.parseInt(split[0]);
            int startMinute = Integer.parseInt(split[1]);
            float top = ((startHour - START_TIME) * 60 + startMinute) * singleMinuteHeight + headerHeight;
            split = endTime.split(":");
            int endHour = Integer.parseInt(split[0]);
            int endMinute = Integer.parseInt(split[1]);
            float bottom = ((endHour - START_TIME) * 60 + endMinute) * singleMinuteHeight + headerHeight;

            float offset = 1;

            rect.set(left + offset, top + offset, right - offset, bottom - offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                return true;
            }
            case MotionEvent.ACTION_UP: {
                float x = event.getX();
                float y = event.getY();
                for (PlanBean bean : planListBeanList) {
                    if (bean.getRectF().left < x && bean.getRectF().right > x
                            && bean.getRectF().top < y && y < bean.getRectF().bottom) {
                        if (planClickListener != null) {
                            planClickListener.onClick(bean.getPlanId(), bean.isEllipsis());
                        }
                    }
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setPlanListBeanList(List<PlanBean> planListBeanList) {
        this.planListBeanList = planListBeanList;
        invalidate();
    }

    public void setPlanClickListener(OnPlanClickListener planClickListener) {
        this.planClickListener = planClickListener;
    }

    public int getScreenWidth() {
        return DisplayUtils.getScreenWidth(getContext());
    }

    public int getScreenHeight() {
        return DisplayUtils.getScreenHeight(getContext());
    }

    public int dp2px(float dp) {
        return DisplayUtils.dp2px(getContext(), dp);
    }

    public int sp2px(float sp) {
        return DisplayUtils.sp2px(getContext(), sp);
    }

}

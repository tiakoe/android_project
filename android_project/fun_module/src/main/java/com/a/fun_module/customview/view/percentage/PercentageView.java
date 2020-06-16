package com.a.fun_module.customview.view.percentage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.a.fun_module.customview.view.BaseView;

import java.util.List;

public class PercentageView extends BaseView {

    private static final int[] COLORS = {0xff2f7e76, 0xff1ff749, 0xfff42872, 0xff4643f4, 0xe51581da, 0xff8527e4, 0xfff1b00d, 0xff26020f};

    //View的默认大小，dp
    private static final int DEFAULT_SIZE = 320;

    private float startAngle;

    private List<PercentageModel> percentageModelList;

    private Paint paint;

    public PercentageView(Context context) {
        this(context, null);
    }

    public PercentageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = dp2px(DEFAULT_SIZE);
        int width = getSize(widthMeasureSpec, defaultSize);
        int height = getSize(heightMeasureSpec, defaultSize);
        width = height = Math.min(width, height);
        setMeasuredDimension(width, height);
        Log.e(TAG, "onMeasure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged");
    }

    private RectF rect = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        if (percentageModelList == null || percentageModelList.size() == 0) {
            return;
        }
        float currentStartAngle = startAngle;
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float r = (float) (Math.min(getWidth(), getHeight()) / 2 * 0.95);
        rect.left = -r;
        rect.top = -r;
        rect.right = r;
        rect.bottom = r;
        for (PercentageModel percentageModel : percentageModelList) {
            paint.setColor(percentageModel.getColor());
            canvas.drawArc(rect, currentStartAngle, percentageModel.getAngle(), true, paint);
            currentStartAngle += percentageModel.getAngle();
        }
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    public void setData(List<PercentageModel> percentageModelList) {
        this.percentageModelList = percentageModelList;
        initData(percentageModelList);
        invalidate();
    }

    private void initData(List<PercentageModel> percentageModelList) {
        if (percentageModelList == null || percentageModelList.size() == 0) {
            return;
        }
        float sumValue = 0;
        for (int i = 0; i < percentageModelList.size(); i++) {
            PercentageModel percentageModel = percentageModelList.get(i);
            sumValue += percentageModel.getValue();
            percentageModel.setColor(COLORS[i % COLORS.length]);
        }
        float sumAngle = 0;
        for (PercentageModel percentageModel : percentageModelList) {
            float per = percentageModel.getValue() / sumValue;
            percentageModel.setAngle(per * 360);
            sumAngle += percentageModel.getAngle();
        }
        //计算百分比时可能有一些精度损失，此处需要判断是否需要把差值补回来
        if (sumAngle < 360) {
            for (PercentageModel percentageModel : percentageModelList) {
                if (percentageModel.getAngle() != 0) {
                    percentageModel.setAngle(360 - sumAngle + percentageModel.getAngle());
                    break;
                }
            }
        }
    }

}

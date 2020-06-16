package com.a.fun_module.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.a.fun_module.customview.utils.DisplayUtils;


public class BaseView extends View {

    protected final String TAG = getClass().getSimpleName();

    public BaseView(Context context) {
        super(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int getSize(int measureSpec, int defaultSize) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = 0;
        switch (mode) {
            case MeasureSpec.AT_MOST: {
                size = Math.min(MeasureSpec.getSize(measureSpec), defaultSize);
                break;
            }
            case MeasureSpec.EXACTLY: {
                size = MeasureSpec.getSize(measureSpec);
                break;
            }
            case MeasureSpec.UNSPECIFIED: {
                size = defaultSize;
                break;
            }
        }
        return size;
    }

    protected int dp2px(float dpValue) {
        return DisplayUtils.dp2px(getContext(), dpValue);
    }

    protected int sp2px(float spValue) {
        return DisplayUtils.sp2px(getContext(), spValue);
    }

}

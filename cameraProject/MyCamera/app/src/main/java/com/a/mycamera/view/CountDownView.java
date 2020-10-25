package com.a.mycamera.view;

import android.os.CountDownTimer;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

/**
 * create by 72088385
 * on 2020/9/24
 */

public class CountDownView extends CountDownTimer{

    public static final int TIME_COUNT = 4000;
    private TextView textView;
    private String endStr;

    public CountDownView(long millisInFuture, long countDownInterval, TextView textView, String endStr) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.endStr = endStr;
    }

    public CountDownView(TextView btn, String endStr) {
        super(TIME_COUNT, 1000);
        this.textView = btn;
        this.endStr = endStr;
    }

    @Override
    public void onFinish() {
        textView.setText(endStr);
        textView.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setEnabled(false);
        //每隔一秒修改一次UI
        textView.setText(millisUntilFinished / 1000+"");

        // 设置透明度渐变动画
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        textView.startAnimation(alphaAnimation);

        // 设置缩放渐变动画
        final ScaleAnimation scaleAnimation =new ScaleAnimation(0.5f, 2f, 0.5f,2f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        textView.startAnimation(scaleAnimation);
    }
}

package com.a.fun_module.custom_animation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.a.fun_module.R;
import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/fun/MyCircleAnimationActivity")
public class MyCircleAnimationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fun_my_circle_activity);

    }

//    void initAnimationView() {
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivShowPic, "rotation", 0f, 360f);
//        objectAnimator.setDuration(20 * 1000);
//        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
//        objectAnimator.setInterpolator(new LinearInterpolator());
//        objectAnimator.setRepeatCount(-1);
//        objectAnimator.start();
//    }
//
//   void  initData(){
//       Paint  mPaint = new Paint();
//       mPaint.setAntiAlias(true);
//       mPaint.setColor(Color.RED);
//       mPaint.setStyle(Paint.Style.FILL);
//
//       Path   mPath = new Path();
//       //绘制 12 个点。
//       ArrayList<PointF> mCurPointList = new ArrayList<>();
//       mCurPointList.add(new PointF(0, dpToPx(-89)));
//       mCurPointList.add(new PointF(dpToPx(50), dpToPx(-89)));
//       mCurPointList.add(new PointF(dpToPx(90), dpToPx(-49)));
//       mCurPointList.add(new PointF(dpToPx(90), 0));
//       mCurPointList.add(new PointF(dpToPx(90), dpToPx(50)));
//       mCurPointList.add(new PointF(dpToPx(50), dpToPx(90)));
//       mCurPointList.add(new PointF(0, dpToPx(90)));
//       mCurPointList.add(new PointF(dpToPx(-49), dpToPx(90)));
//       mCurPointList.add(new PointF(dpToPx(-89), dpToPx(50)));
//       mCurPointList.add(new PointF(dpToPx(-89), 0));
//       mCurPointList.add(new PointF(dpToPx(-89), dpToPx(-49)));
//       mCurPointList.add(new PointF(dpToPx(-49), dpToPx(-89)));
//   }
}

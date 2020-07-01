package com.a.fun_module.custom_animation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.a.fun_module.R;
import com.alibaba.android.arouter.launcher.ARouter;

public class CustomAnimationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fun_custom_animation_activity);

    }


    public void onClickCircleAnimation(View view) {
        ARouter.getInstance().build("/fun/MyCircleAnimationActivity").navigation();
    }
}

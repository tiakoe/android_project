package com.example.myapplication.function_pages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.myapplication.R;

public class LoginAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_app_example);
    }

    public void login(View view) {

        Button button = findViewById(R.id.id_login_btn);
        if (button.getText() == "登录") {
            button.setText("已登录");
        } else {
            button.setText("登录");
        }
    }

    public void toMain(View view) {
        ARouter.getInstance().build("/com/mainActivity").navigation();
    }

    public void toComponentLogin(View view) {
        //        ARouter.openLog();     // 打印日志
        //        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        //        ARouter.init(getApplication()); // 尽可能早，推荐在Application中初始化
        ARouter.getInstance().build("/account/loginMainActivity").navigation();
        //        ARouter.getInstance().build("/account/loginMainActivity").navigation(this,
        //                new NavigationCallback() {
        //            @Override
        //            public void onFound(Postcard postcard) {
        //                Log.d("aRout", "onFound");
        //            }
        //
        //            @Override
        //            public void onLost(Postcard postcard) {
        //                Log.d("aRout", "onLost");
        //            }
        //
        //            @Override
        //            public void onArrival(Postcard postcard) {
        //                Log.d("aRout", "onArrival");
        //            }
        //
        //            @Override
        //            public void onInterrupt(Postcard postcard) {
        //                Log.d("aRout", "onInterrupt");
        //            }
        //        });
    }
}

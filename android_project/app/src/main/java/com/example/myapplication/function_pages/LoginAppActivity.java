package com.example.myapplication.function_pages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
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


        ARouter.getInstance().build("/login/loginMainActivity").navigation(this,
                new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {
                        Log.d("aRout", "onFound");
                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        Log.d("aRout", "onLost");
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                        Log.d("aRout", "onArrival");
                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {
                        Log.d("aRout", "onInterrupt");
                    }
                });
    }
}

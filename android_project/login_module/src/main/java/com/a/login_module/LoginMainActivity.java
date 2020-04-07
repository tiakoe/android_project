package com.a.login_module;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/account/loginMainActivity")
public class LoginMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void toLoginIn(View view) {

        ARouter.getInstance().build("/account/mainHome").navigation(this, new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {
                Log.d("login_module", "onFound");
            }

            @Override
            public void onLost(Postcard postcard) {
                Log.d("login_module", "onLost");
            }

            @Override
            public void onArrival(Postcard postcard) {
                Log.d("login_module", "onArrival");
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                Log.d("login_module", "onInterrupt");
            }
        });
    }

    public void toLoginUp(View view) {

    }

}

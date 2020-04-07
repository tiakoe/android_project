package com.a.login_module;

import android.app.Application;

import com.a.base_module.BaseApp;

public class LoginApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        initModule(this);
    }

    @Override
    public void initModule(Application application) {

    }
}

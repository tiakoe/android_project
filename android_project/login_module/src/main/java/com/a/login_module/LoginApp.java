package com.a.login_module;

import android.app.Application;

import com.a.base_module.BaseApp;
import com.alibaba.android.arouter.launcher.ARouter;

public class LoginApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        initModule(this);
    }

    @Override
    public void initModule(Application application) {
        if ("com.a.login_module".equals(application.getPackageName())) {
            ARouter.openLog();
            ARouter.openDebug();
            ARouter.init(this);
        }
    }
}

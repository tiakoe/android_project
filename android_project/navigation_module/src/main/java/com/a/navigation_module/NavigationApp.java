package com.a.navigation_module;

import android.app.Application;

import com.a.base_module.BaseApp;
import com.alibaba.android.arouter.launcher.ARouter;

public class NavigationApp extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initModule(Application application) {
        if ("com.a.navigation_module".equals(application.getPackageName())) {
            ARouter.openLog();
            ARouter.openDebug();
            ARouter.init(this);
        }
    }
}

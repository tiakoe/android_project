package com.a.fun_module;

import android.app.Application;

import com.a.base_module.BaseApp;
import com.alibaba.android.arouter.launcher.ARouter;

public class FunApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        initModule(this);
    }

    @Override
    public void initModule(Application application) {
        if ("com.a.fun_module.FunApp".equals(application.getPackageName())) {
            ARouter.openLog();
            ARouter.openDebug();
            ARouter.init(this);
        }
    }
}

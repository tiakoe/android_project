package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.a.base_module.AppConfig;
import com.a.base_module.BaseApp;
import com.alibaba.android.arouter.launcher.ARouter;


public class App extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        initARouter();
        initModule(this);
    }

    @Override
    public void initModule(Application application) {
        for (String moduleApp : AppConfig.moduleApps) {
            try {
                Class clazz = Class.forName(moduleApp);
                BaseApp baseApp = (BaseApp) clazz.newInstance();
                baseApp.initModule(this);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isDebug() {
        Context context = getApplicationContext();
        if (context.getPackageName() == null) {
            return false;
        }
        try {
            ApplicationInfo ai =
                    context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            return (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void initARouter() {

        if (isDebug()) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
    }
}

package com.a.note_module;

import android.app.Application;

import com.a.base_module.BaseApp;
import com.alibaba.android.arouter.launcher.ARouter;

public class NoteApp extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void initModule(Application application) {
        if ("com.a.note_module".equals(application.getPackageName())) {
            ARouter.openLog();
            ARouter.openDebug();
            ARouter.init(this);
        }
    }
}

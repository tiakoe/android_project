package com.a.base_module;

import android.app.Application;

public abstract class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    public abstract void initModule(Application application);
}

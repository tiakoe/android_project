package com.a.myapplication;

import android.app.Application;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//            }
//        });
    }
}

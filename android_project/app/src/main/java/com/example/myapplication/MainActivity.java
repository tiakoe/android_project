package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.myapplication.function_pages.LoginAppActivity;
import com.example.myapplication.function_pages.MyMmkvActivity;
import com.example.myapplication.function_pages.ScrollConflictActivity;
import com.example.myapplication.function_pages.SensorActivity;
import com.tencent.mmkv.MMKV;

import butterknife.ButterKnife;

@Route(path = "/com/mainActivity")
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //        initARouter();
        ButterKnife.bind(this);
        //        ContextCompat.checkSelfPermission(activity, permission);
        //        checkSelfPermission();
        //        requestPermissions();

        mmkvTest();
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
        ARouter.init(getApplication());
    }

    public void mmkvTest() {
        // 设置初始化的根目录
        String rootDir = MMKV.initialize(getFilesDir().getAbsolutePath() + "/mmkv_1");
        Log.i("MMKV", "mmkv rootDir: " + rootDir);

        String root = MMKV.initialize(this);
        System.out.println("mmkv root: " + root);
        // 获取默认的全局实例
        MMKV kv = MMKV.defaultMMKV();
        // 根据业务区别存储, 附带一个自己的 ID


        MMKV kvid = MMKV.mmkvWithID("MyID");

        // 多进程同步支持
        MMKV kvd = MMKV.mmkvWithID("MyID", MMKV.MULTI_PROCESS_MODE);


        kv.encode("bool", true);
        boolean bValue = kv.decodeBool("bool");

        kv.encode("int", Integer.MIN_VALUE);
        int iValue = kv.decodeInt("int");


        kv.encode("string", "Hello from mmkv");
        String str = kv.decodeString("string");
        System.out.println(bValue + ":" + iValue + ":" + str);

        kv.encode("obj", new MyMmkvActivity().toString());
        String obj = kv.decodeString("obj");
        Log.d("obj", obj);
    }

    public void showScrollConflict(View view) {
        startActivity(new Intent(this, ScrollConflictActivity.class));
    }

    public void showSensorEvent(View view) {
        startActivity(new Intent(this, SensorActivity.class));
    }

    public void showLoginActivity(View view) {
        startActivity(new Intent(this, LoginAppActivity.class));
    }
}



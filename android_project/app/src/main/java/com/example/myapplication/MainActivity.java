package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.function_pages.LoginAppActivity;
import com.example.myapplication.function_pages.MyMmkvActivity;
import com.example.myapplication.function_pages.ScrollConflictActivity;
import com.example.myapplication.function_pages.SensorActivity;
import com.tencent.mmkv.MMKV;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //        ContextCompat.checkSelfPermission(activity, permission);
        //        checkSelfPermission();
        //        requestPermissions();

        mmkvTest();
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
        System.out.println(kv.getAll());
        MMKV kvid = MMKV.mmkvWithID("MyID");
        System.out.println(kvid.getAll());

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
        System.out.println(obj);
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



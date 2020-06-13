package com.a.ndk_module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class NdkMainActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ndk_activity_main);
        TextView _tv_msg = findViewById(R.id.tv_msg);
        TextView _tv_res = findViewById(R.id.tv_res);
        String msg = new CalUtil().getMsg();
        String res = String.valueOf(new CalUtil().getNumber());
        _tv_msg.setText(msg);
        _tv_res.setText(res);
    }

    public void downLoadBar(View view) {
        startActivity(new Intent(this,AsyncTaskActivity.class));
    }
}



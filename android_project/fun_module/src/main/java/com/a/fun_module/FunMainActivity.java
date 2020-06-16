package com.a.fun_module;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/fun/FunMainActivity")
public class FunMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fun_activity_main);

//        startActivity(new Intent(this, RefreshListPage.class));

    }

    public void toCustomView(View view) {
        ARouter.getInstance().build("/custom/CustomViewMainActivity").navigation();
    }


}

package com.a.fun_module;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.a.fun_module.recycle_page.RefreshListPage;

public class FunMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fun_activity_main);

        startActivity(new Intent(this, RefreshListPage.class));

    }
}

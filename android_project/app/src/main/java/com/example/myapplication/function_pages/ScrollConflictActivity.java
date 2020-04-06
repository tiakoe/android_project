package com.example.myapplication.function_pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.scroll_conflict.ExampleActivity01;
import com.example.myapplication.scroll_conflict.ExampleActivity02;
import com.example.myapplication.scroll_conflict.ExampleActivity03;
import com.example.myapplication.scroll_conflict.ExampleActivity04;
import com.example.myapplication.scroll_conflict.ExampleActivity05;


public class ScrollConflictActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_conflict_example);
    }

    public void Example01(View view) {
        startActivity(new Intent(this, ExampleActivity01.class));
    }


    public void Example02(View view) {
        startActivity(new Intent(this, ExampleActivity02.class));
    }


    public void Example03(View view) {
        startActivity(new Intent(this, ExampleActivity03.class));
    }

    public void Example04(View view) {
        startActivity(new Intent(this, ExampleActivity04.class));
    }

    public void Example05(View view) {
        startActivity(new Intent(this, ExampleActivity05.class));
    }
}

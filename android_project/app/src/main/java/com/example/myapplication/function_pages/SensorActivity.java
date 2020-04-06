package com.example.myapplication.function_pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.sensor_event.ExampleSensorEvent01;

public class SensorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_event_example);
    }

    public void showSensorExample01(View view) {
        startActivity(new Intent(this, ExampleSensorEvent01.class));
    }


}

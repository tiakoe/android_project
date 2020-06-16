package com.a.fun_module.customview.view.rain;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.a.fun_module.R;
import com.a.fun_module.customview.widget.OnSeekBarChangeSimpleListener;


public class RainViewActivity extends AppCompatActivity {

    private static final String TAG = "RainViewActivity";

    private RainView rainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rain_view);
        rainView = findViewById(R.id.rainView);
        final AppCompatSeekBar speedSeekBar = findViewById(R.id.speedSeekBar);
        speedSeekBar.setMax(100);
        speedSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 30;
                int max = 90;
                int speed = (int) (min + (max - min) * (progress * 1.0f / speedSeekBar.getMax()));
                rainView.setSpeed(speed);

                Log.e(TAG, "speed: " + speed);
            }
        });
        speedSeekBar.setProgress(30);

        final AppCompatSeekBar degreeSeekBar = findViewById(R.id.degreeSeekBar);
        degreeSeekBar.setMax(100);
        degreeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 50;
                int max = min + 100;
                int degree = (int) (min + (max - min) * (progress * 1.0f / degreeSeekBar.getMax()));
                rainView.setDegree(degree);

                Log.e(TAG, "degree: " + degree);
            }
        });
        degreeSeekBar.setProgress(30);
    }

}

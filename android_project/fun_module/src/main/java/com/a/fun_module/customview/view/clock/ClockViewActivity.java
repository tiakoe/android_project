package com.a.fun_module.customview.view.clock;

import android.os.Bundle;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.a.fun_module.R;
import com.a.fun_module.customview.widget.OnSeekBarChangeSimpleListener;

import java.util.Random;


public class ClockViewActivity extends AppCompatActivity {

    private ClockView clockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_view);
        clockView = findViewById(R.id.clockView);

        AppCompatSeekBar seekBar1 = findViewById(R.id.seekBar1);
        seekBar1.setMax(100);
        seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Random myRandom = new Random();
                int ranColor = 0xff000000 | myRandom.nextInt(0x00ffffff);
                clockView.setAroundColor(ranColor);
            }
        });

        AppCompatSeekBar seekBar2 = findViewById(R.id.seekBar2);
        seekBar2.setMax(100);
        seekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                clockView.setAroundStockWidth(progress);
            }
        });

        AppCompatSeekBar seekBar3 = findViewById(R.id.seekBar3);
        seekBar3.setMax(100);
        seekBar3.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                clockView.setClockTextSize(progress);
            }
        });
    }

}

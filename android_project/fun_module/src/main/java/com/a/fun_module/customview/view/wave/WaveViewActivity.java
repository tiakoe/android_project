package com.a.fun_module.customview.view.wave;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.a.fun_module.R;
import com.a.fun_module.customview.widget.OnSeekBarChangeSimpleListener;


public class WaveViewActivity extends AppCompatActivity {

    private static final String TAG = "WaveViewActivity";

    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_view);
        waveView = findViewById(R.id.waveView);

        final AppCompatSeekBar seekBarWidth = findViewById(R.id.seekBarWidth);
        seekBarWidth.setMax(100);
        seekBarWidth.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float width = (progress / 100.0f) * 1.0f;
                waveView.setWaveScaleWidth(width);
                Log.e(TAG, "width: " + width);
            }
        });
        seekBarWidth.setProgress(50);

        final AppCompatSeekBar seekBarHeight = findViewById(R.id.seekBarHeight);
        seekBarHeight.setMax(100);
        seekBarHeight.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float height = (progress / 100.0f) * 0.1f;
                waveView.setWaveScaleHeight(height);
                Log.e(TAG, "height: " + height);
            }
        });
        seekBarHeight.setProgress(40);

        final AppCompatSeekBar seekBarSpeed = findViewById(R.id.seekBarSpeed);
        seekBarSpeed.setMax(4000);
        seekBarSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long speed = progress + 300;
                waveView.setSpeed(speed);
                Log.e(TAG, "speed: " + speed);
            }
        });
        seekBarSpeed.setProgress(1000);
    }

}

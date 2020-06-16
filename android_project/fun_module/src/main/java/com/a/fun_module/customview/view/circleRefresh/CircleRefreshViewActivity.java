package com.a.fun_module.customview.view.circleRefresh;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.a.fun_module.R;
import com.a.fun_module.customview.widget.OnSeekBarChangeSimpleListener;


public class CircleRefreshViewActivity extends AppCompatActivity {

    private CircleRefreshView circleRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_refresh_view);
        circleRefreshView = findViewById(R.id.refresh_view);
        SeekBar seekBarDrag = findViewById(R.id.seekBarDrag);
        seekBarDrag.setMax(100);
        seekBarDrag.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circleRefreshView.drag(progress / 100f);
            }
        });
        SeekBar seekBarSeed = findViewById(R.id.seekBarSeed);
        seekBarSeed.setMax(100);
        seekBarSeed.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circleRefreshView.setSpeed(progress * 40);
            }
        });
        seekBarSeed.setProgress((int) (circleRefreshView.getSpeed() / 40));
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnStart) {
            circleRefreshView.start();
        } else if (id == R.id.btnStop) {
            circleRefreshView.stop();
        }
    }

}

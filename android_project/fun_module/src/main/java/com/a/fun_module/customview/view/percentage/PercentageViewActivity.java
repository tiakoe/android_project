package com.a.fun_module.customview.view.percentage;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.a.fun_module.R;
import com.a.fun_module.customview.widget.OnSeekBarChangeSimpleListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PercentageViewActivity extends AppCompatActivity {

    private PercentageView percentageView;

    private List<PercentageModel> percentageModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage_view);
        percentageView = findViewById(R.id.percentageView);
        percentageModelList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PercentageModel percentageModel = new PercentageModel();
            percentageModel.setValue((float) (new Random().nextInt(30) * (i + 1)));
            percentageModelList.add(percentageModel);
        }
        percentageView.setData(percentageModelList);
        AppCompatSeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(360);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeSimpleListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percentageView.setStartAngle(progress);
            }
        });
    }

    public void addData(View view) {
        PercentageModel percentageModel = new PercentageModel();
        percentageModel.setValue((float) (new Random().nextInt(50)));
        percentageModelList.add(percentageModel);
        percentageView.setData(percentageModelList);
    }

    public void removeData(View view) {
        if (percentageModelList.size() > 2) {
            percentageModelList.remove(percentageModelList.size() - 1);
            percentageView.setData(percentageModelList);
        }
    }

}

package com.a.fun_module.customview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.a.fun_module.R;
import com.a.fun_module.customview.view.circleRefresh.CircleRefreshViewActivity;
import com.a.fun_module.customview.view.clock.ClockViewActivity;
import com.a.fun_module.customview.view.percentage.PercentageViewActivity;
import com.a.fun_module.customview.view.plan.PlanViewActivity;
import com.a.fun_module.customview.view.pointBeat.PointBeatViewActivity;
import com.a.fun_module.customview.view.rain.RainViewActivity;
import com.a.fun_module.customview.view.taiji.TaiJiViewActivity;
import com.a.fun_module.customview.view.wave.WaveViewActivity;
import com.a.fun_module.customview.view.waveLoading.WaveLoadingViewActivity;
import com.alibaba.android.arouter.facade.annotation.Route;


@Route(path = "/custom/CustomViewMainActivity")
public class CustomViewMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_activity);
    }

    private void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    public void startClockActivity(View view) {
        startActivity(ClockViewActivity.class);
    }

    public void startPercentageViewActivity(View view) {
        startActivity(PercentageViewActivity.class);
    }

    public void startCircleRefreshViewActivity(View view) {
        startActivity(CircleRefreshViewActivity.class);
    }

    public void startRainViewActivity(View view) {
        startActivity(RainViewActivity.class);
    }

    public void startWaveViewActivity(View view) {
        startActivity(WaveViewActivity.class);
    }

    public void startTaiJiViewActivity(View view) {
        startActivity(TaiJiViewActivity.class);
    }

    public void startWaveLoadingViewActivity(View view) {
        startActivity(WaveLoadingViewActivity.class);
    }

    public void startPointBeatViewActivity(View view) {
        startActivity(PointBeatViewActivity.class);
    }

    public void startPlanViewActivity(View view) {
        startActivity(PlanViewActivity.class);
    }

}

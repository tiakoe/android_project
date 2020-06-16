package com.a.fun_module.customview.view.taiji;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.a.fun_module.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class TaiJiViewActivity extends AppCompatActivity {

    private TaiJiView taijiView;

    private float degrees;

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> scheduledFuture;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            degrees = degrees + 1;
            taijiView.setDegrees(degrees);
            if (degrees == 360) {
                degrees = 0;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_ji_view);
        taijiView = findViewById(R.id.taiJiView);
        start();
    }

    private void start() {
        clear();
        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(runnable, 300, 10, TimeUnit.MILLISECONDS);
    }

    private void clear() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }

}

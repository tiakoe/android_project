package com.a.http_module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.a.http_module.http.ApiService;
import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava2 + Retrofit demo.
 */
@Route(path = "/http/HttpMainActivity")
public class HttpMainActivity extends Activity {
    private static final String TAG = "HttpMainActivity";
    private TextView resultTV;
    private ProgressDialog dialog;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_activity_main);
        compositeDisposable = new CompositeDisposable();
        initView();
    }

    private void initView() {
        resultTV = findViewById(R.id.tv_result);
        dialog = new ProgressDialog(this);
        dialog.setMessage("加载中");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 进行网络请求
     */
    public void getHttpResult(View view) {
        compositeDisposable
                .add(Flowable
                        .just(1)
                        .doOnNext(o -> dialog.show())
                        .delay(1, TimeUnit.SECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .flatMap(o -> ApiService.getInstance().getHttpResult())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(subject -> resultTV.setText(subject.toString()))
                        .doOnError(throwable -> {
                            Toast.makeText(HttpMainActivity.this, "结果异常", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));
                        })
                        .doOnTerminate(() -> dialog.dismiss())
                        .retry(3)
                        .subscribe(o -> {
                        }, throwable -> Log.e(TAG, throwable.toString())));
    }

    public void loadImage(View view) {
        startActivity(new Intent(this, HttpImageActivity.class));
    }

    public void onDownLoadMax(View view) {
        startActivity(new Intent(this, HttpDownloadActivity.class));
    }

    public void onDownLoadMany(View view) {
        startActivity(new Intent(this, ManyDownLoadActivity.class));
    }

    public void onAIDLConnection(View view) {
        startActivity(new Intent(this, AidlMainActivity.class));
    }
}

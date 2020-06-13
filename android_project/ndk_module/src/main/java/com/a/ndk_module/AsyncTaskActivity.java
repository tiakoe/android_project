package com.a.ndk_module;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;


public class AsyncTaskActivity extends AppCompatActivity {
    private Button start_btn;
    private Button cancel_btn;
    private ProgressBar progressBar;
    private TextView contentRes;
    private DownLoadTask downLoadTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_task_activity);
        initView();

    }

    void initView() {
        start_btn = findViewById(R.id.start);
        cancel_btn = findViewById(R.id.cancel);
        progressBar = findViewById(R.id.pro_bar);
        contentRes = findViewById(R.id.label);

    }

    public void startDownload(View view) {
//        if (downLoadTask == null) {
            downLoadTask = new DownLoadTask(start_btn, cancel_btn, progressBar, contentRes);
//        }
        //下载的延迟时间
        downLoadTask.execute(100);
    }

    public void cancelDownload(View view) {
        if (downLoadTask != null && downLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
            downLoadTask.cancel(true);
            downLoadTask = null;
        }
    }

    /**
     * AsyncTask<Integer, Integer, String>后是三个函数的参数类型
     * downLoadTask.execute(100);传入第二个方法中；
     * publishProgress(i);传入第三个方法中；
     * doInBackground() ;返回结果传入第四个方法中；
     */
    private static class DownLoadTask extends AsyncTask<Integer, Integer, String> {

        private final WeakReference<Button> start_btn;
        private final WeakReference<Button> cancel_btn;
        private final WeakReference<ProgressBar> progressBar;
        private final WeakReference<TextView> contentRes;

        DownLoadTask(Button start, Button cancel, ProgressBar p, TextView c) {
            start_btn = new WeakReference<>(start);
            cancel_btn = new WeakReference<>(cancel);
            progressBar = new WeakReference<>(p);
            contentRes = new WeakReference<>(c);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            start_btn.get().setEnabled(false);
            cancel_btn.get().setEnabled(true);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i < 100; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(integers[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) {
                    return null;
                }
            }
            return "Download Progress：finished";
        }

        /**
         * 在doInBackground返回后触发onCancelled
         */
        @SuppressLint("SetTextI18n")
        @Override
        protected void onCancelled() {
            super.onCancelled();
            start_btn.get().setEnabled(true);
            cancel_btn.get().setEnabled(false);
            progressBar.get().setProgress(0);
            contentRes.get().setText("Download Progress：cancelled");
        }

        /**
         * @param values i:0-99
         */
        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (isCancelled()) {
                return;
            }
            progressBar.get().setProgress(values[0]);
            contentRes.get().setText("Download Progress：" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            start_btn.get().setEnabled(true);
            cancel_btn.get().setEnabled(false);
            contentRes.get().setText(s);
        }
    }
}

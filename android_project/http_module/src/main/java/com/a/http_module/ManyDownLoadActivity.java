package com.a.http_module;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManyDownLoadActivity extends AppCompatActivity {
    @BindView(R2.id.m_down_imageView)
    ImageView mImage;
    @BindView(R2.id.m_down_textView)
    TextView mContent;
    @BindView(R2.id.m_down_ProgressBar)
    ProgressBar mProgressBar;

    ArrayList<SingleDownloadTask> arrayList;

    private static int finished = 0;
    private static boolean isPause = false;
    private static boolean isCancel = false;

    private static int sumLen;
    private static String filePath;

    private static Handler handler = new Handler();

    //创建基本线程池
    final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(50));


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.many_download_activity);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        filePath =
                new File(ManyDownLoadActivity.this.getFilesDir(), "myTest1.txt").getPath();

        final String url_resource = "http://image.biaobaiju" +
                ".com/uploads/20190807/13/1565156399-SGrnbwhBjR.jpg";
        try {
            URL url = new URL(url_resource);
            onMultithreadingRun(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void onMultithreadingRun(URL url) {
        Runnable runnable = () -> {
            HttpURLConnection conn;
            try {
                arrayList = new ArrayList<>();
                conn = (HttpURLConnection) url.openConnection();
                sumLen = conn.getContentLength();
                int sizePer = sumLen / 6;
                for (int i = 0; i < 6; i++) {
                    arrayList.add(new SingleDownloadTask(url, i * sizePer,
                            i * sizePer + sizePer, mProgressBar, mContent, mImage));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        threadPoolExecutor.execute(runnable);
    }


    private static class SingleDownloadTask {

        private URL url;
        private RandomAccessFile file;
        private int start;
        private int end;
        private int curRead;
        private int schedule = 0;

        private ProgressBar mProgressBar;
        private TextView mTextView;
        private ImageView mImageView;


        public SingleDownloadTask(URL url, int start, int end,
                                  ProgressBar mProgressBar, TextView mTextView,
                                  ImageView mImageView) {
            this.url = url;
            this.start = start;
            this.end = end;
            this.curRead = start;
            this.mImageView = mImageView;
            this.mProgressBar = mProgressBar;
            this.mTextView = mTextView;
        }


        @SuppressLint("SetTextI18n")
        public Runnable download() {
            return (Runnable) () -> {
                HttpURLConnection conn = null;
                InputStream is = null;
                BufferedInputStream bis = null;
                try {
                    file = new RandomAccessFile(filePath, "rw");
                    file.seek(curRead);
                    conn = (HttpURLConnection) url.openConnection();
                    System.out.println(start + "---" + end);
                    conn.setRequestProperty("Range", "bytes=" + curRead + "-" + end);
                    is = conn.getInputStream();
                    bis = new BufferedInputStream(is);
                    byte[] bytes = new byte[1024];
                    int len;
                    while (!isPause && ((len = bis.read(bytes, 0, bytes.length))) != -1) {

                        file.write(bytes, 0, len);
                        curRead += len;
                        schedule = curRead - start;
                        handler.post(() -> {
                            mTextView.setText((int) (finished / sumLen) * 100 + "%");
                            mProgressBar.setProgress((int) (finished / sumLen) * 100);
                            mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        });
                    }
                    file.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                            if (is != null) {
                                is.close();
                            }
                            if (conn != null) {
                                conn.disconnect();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
    }

    @SuppressLint("SetTextI18n")
    public void onManyStartDownLoad(View view) {
        finished = 0;
        isPause = false;
        isCancel = false;
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                threadPoolExecutor.execute(arrayList.get(i).download());
                finished += arrayList.get(i).schedule;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onManyPauseDownLoad(View view) {
        isPause = true;
    }

    @SuppressLint("SetTextI18n")
    public void onManyCancelDownLoad(View view) {
        isCancel = true;
        File file = new File(filePath);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mContent.setText((int) (finished / sumLen) * 100 + "%");
        mProgressBar.setProgress((int) (finished / sumLen) * 100);
        mImage.setImageBitmap(BitmapFactory.decodeFile(filePath));

        initData();
    }
}

package com.a.http_module;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

//
//class MyThread extends Thread {
//    private FileInfo info = null;
//
//    public MyThread(FileInfo threadInfo) {
//        this.info = threadInfo;
//    }
//
//    @Override
//    public void run() {
//        HttpURLConnection urlConnection = null;
//        RandomAccessFile randomFile = null;
//        InputStream inputStream = null;
//        try {
//            URL url = new URL("url");
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setConnectTimeout(3000);
//            urlConnection.setRequestMethod("GET");
//            //设置下载位置
//            int start = info.getStart() + info.getNow();
//            urlConnection.setRequestProperty("Range", "bytes=" + start + "-" + info.getLength());
//
//            //设置文件写入位置
//            File file = new File(DOWNLOAD_PATH, FILE_NAME);
//            randomFile = new RandomAccessFile(file, "rwd");
//            randomFile.seek(start);
//
//            //向Activity发广播
//            Intent intent = new Intent(ACTION_UPDATE);
//            finished += info.getNow();
//
//            if (urlConnection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
//                //获得文件流
//                inputStream = urlConnection.getInputStream();
//                byte[] buffer = new byte[512];
//                int len = -1;
//                long time = System.currentTimeMillis();
//                while ((len = inputStream.read(buffer)) != -1) {
//                    //写入文件
//                    randomFile.write(buffer, 0, len);
//                    //把进度发送给Activity
//                    finished += len;
//                    //看时间间隔，时间间隔大于500ms再发
//                    if (System.currentTimeMillis() - time > 500) {
//                        time = System.currentTimeMillis();
//                        intent.putExtra("now", finished * 100 / fileInfo.getLength());
//                        context.sendBroadcast(intent);
//                    }
//                    //判断是否是暂停状态
//                    if (isPause) {
//                        threadDAO.update(info.getUrl(), finished);
//                        return; //结束循环
//                    }
//                }
//                //删除线程信息
//                threadDAO.delete(info.getUrl());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {//回收工作略
//        }
//    }
//}


public class HttpDownloadActivity extends AppCompatActivity {

    @BindView(R2.id.down_imageView)
    ImageView mImage;
    @BindView(R2.id.down_textView)
    TextView mContent;
    @BindView(R2.id.down_ProgressBar)
    ProgressBar mProgressBar;

    private final static String url_resource = "http://image.biaobaiju" +
            ".com/uploads/20190807/13/1565156399-SGrnbwhBjR.jpg";

    SingleDownloadTask singleDownloadTask;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_download_activity);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        File file = new File(HttpDownloadActivity.this.getFilesDir(), "myTestFile.txt");
        singleDownloadTask = new SingleDownloadTask(url_resource, file.getPath(),
                mProgressBar, mContent, mImage);
    }


    private class SingleDownloadTask {

        private String filePath;
        private int contentLength;
        private int readLength;
        private RandomAccessFile file = null;
        private boolean isPause = false;
        private URL url;

        private ProgressBar mProgressBar;
        private TextView mTextView;
        private ImageView mImageView;


        public boolean isDownloading() {
            return isDownloading;
        }

        private boolean isDownloading = false;

        private Handler mHandler = new Handler();

        public SingleDownloadTask(String urlStr, String path, ProgressBar p, TextView t,
                                  ImageView i) {
            mProgressBar = p;
            mTextView = t;
            mImageView = i;
            filePath = path;
            readLength = 0;
            contentLength = 0;
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                file = new RandomAccessFile(filePath, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        @SuppressLint("SetTextI18n")
        public void download() {
            new Thread(() -> {
                isDownloading = true;
                HttpURLConnection conn = null;
                InputStream is = null;
                BufferedInputStream bis = null;
                try {
                    file.seek(readLength);
                    conn = (HttpURLConnection) url.openConnection();
                    if (readLength == 0) {
                        contentLength = conn.getContentLength();
                        System.out.println("contentLength:" + contentLength);
                    } else {
                        conn.setRequestProperty("Range", "bytes=" + readLength + "-");
                    }
                    is = conn.getInputStream();
                    bis = new BufferedInputStream(is);
                    byte[] bytes = new byte[1024];
                    int len;
                    while (!isPause && ((len = bis.read(bytes, 0, bytes.length))) != -1) {
                        file.write(bytes, 0, len);
                        readLength += len;
                        System.out.println("readLength" + readLength + "  len" + len);
                        mHandler.post(() -> {
                            float rate = ((float) readLength) / contentLength;
                            mProgressBar.setProgress((int) (100 * rate));
                            mTextView.setText((int) (100 * rate) + "%");
                        });
                    }
                    isDownloading = false;
                    if (readLength >= contentLength) {
                        mHandler.post(() -> {
                            Toast.makeText(HttpDownloadActivity
                                            .this, "文件下载成功,保存在" + filePath
                                    , Toast.LENGTH_SHORT)
                                    .show();
                            mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        });
                    }
                    file.close();
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
                            conn.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        public void start() {
            isPause = false;
            download();
        }

        public void pause() {
            isPause = true;
        }

    }


    public void onStartDownLoad(View view) {
        singleDownloadTask.start();
    }

    public void onPauseDownLoad(View view) {
        singleDownloadTask.pause();
    }

    public void onCancelDownLoad(View view) {
    }


}

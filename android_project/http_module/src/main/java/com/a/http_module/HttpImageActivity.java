package com.a.http_module;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;


public class HttpImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private static Bitmap bitmap;
    private static final String image_path = "http://image.biaobaiju" +
            ".com/uploads/20190807/13/1565156399-SGrnbwhBjR.jpg";

    private  Handler handler;

    private static class MyHandler extends Handler {
        ImageView imageView;

        private MyHandler(ImageView imageView1) {
            imageView = new WeakReference<>(imageView1).get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                imageView.setImageBitmap(bitmap);
                Log.d("ffff", "fdf");
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_image_activity);
        initView();

        handler = new MyHandler(imageView);

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
        editor.putString("fff", "kkk");
        editor.apply();
        String res = sp.getString("fff", "");
        System.out.println(res);

    }

    private void initView() {
        imageView = findViewById(R.id.image);
    }

    public void onClickRequestImage(View view) {
        new Thread(() -> {
            try {
                URL url = new URL(image_path);
                InputStream is = url.openStream();
                bitmap = BitmapFactory.decodeStream(is);
                handler.sendEmptyMessage(0x123);
                OutputStream os = openFileOutput("my_app_test_image.jpg", 0);
                byte[] buff = new byte[1024];
                int hasRead;
                while ((hasRead = is.read(buff)) > 0) {
                    os.write(buff, 0, hasRead);
                }
                is.close();
                os.close();
            } catch (Exception e) {
                Log.d("ff", "ffff");
                e.printStackTrace();
            }
        }).start();
    }
}

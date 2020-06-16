package com.a.login_module;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.HashMap;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Route(path = "/login/loginMainActivity")
public class LoginMainActivity extends AppCompatActivity {

    //不可以使用:错误: 元素值必须为常量表达式
//    @BindView(R.id.requestContent)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        ButterKnife.bind(this);
        textView = findViewById(R.id.requestContent);
        createRetrofit();

    }

    public void toLoginIn(View view) {
        ARouter.getInstance().build("/login/sign_in").navigation();
        //        ARouter.getInstance().build("/login/sign_in").navigation(this, new
        //        NavigationCallback() {
        //            @Override
        //            public void onFound(Postcard postcard) {
        //                Log.d("login_module", "onFound");
        //            }
        //
        //            @Override
        //            public void onLost(Postcard postcard) {
        //                Log.d("login_module", "onLost");
        //            }
        //
        //            @Override
        //            public void onArrival(Postcard postcard) {
        //                Log.d("login_module", "onArrival");
        //            }
        //
        //            @Override
        //            public void onInterrupt(Postcard postcard) {
        //                Log.d("login_module", "onInterrupt");
        //            }
        //        });
    }

    public void toLoginUp(View view) {
        ARouter.getInstance().build("/login/sign_up").navigation();
    }

    public void createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("page", "1");
        hashMap.put("cat", "index.htm");
        Call<ApiResult> call = retrofit.create(RetrofitAPI.class).postJijin(hashMap);
        call.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                if (response.body() != null) {
                    Log.i("ss", "onRespons:" + response.body().toString());
                    textView.setText(response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.i("ee", "onFailure: " + t);
            }
        });

    }

}

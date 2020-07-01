package com.a.myapplication;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Glide.with(this).load(url).placeholder(R.drawable.loading).error(R.drawable.error)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
//                .into(imageView);
//
//        requestData();
    }

//    private void requestData() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://ceshi.app.beisu100.com/Testapi/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        API service = retrofit.create(API.class);
//        Call<Result<ItemData>> call = service.getDataApi1();
//
//        call.enqueue(new Callback<Result<ItemData>>() {
//            @Override
//            public void onResponse(Call<Result<ItemData>> call, Response<Result<ItemData>> response) {
//                if (response.isSuccessful()) {
//                    assert response.body() != null;
//                    Log.d("dd1", "onResponse: " + response.body().getData());
//                }
//            }
//            @Override
//            public void onFailure(Call<Result<ItemData>> call, Throwable t) {
//                Log.d("dd1", "onFailure: " + t.getMessage());
//            }
//        });
//    }


}

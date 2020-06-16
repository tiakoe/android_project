package com.a.http_module.http;


import android.annotation.SuppressLint;
import android.util.Log;

import com.a.http_module.entity.Subject;
import com.a.http_module.entity.HttpResult;
import com.a.http_module.interceptor.CommonInterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiService {
    private static final String TAG = "ApiService";
    private static final String BASE_URL = "https://617bde90-e39e-4c0a-8cc8-47493fa89115.mock" +
            ".pstmn.io/";
    private static final int DEFAULT_TIMEOUT = 5;
    private IApiService service;

    /**
     * 构造方法私有
     */
    private ApiService() {
        // 手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //启用Log日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        // 添加通用拦截器
        builder.addInterceptor(new CommonInterceptor());
        Retrofit retrofit = new Retrofit
                .Builder()
                .client(builder.build())
                // 对http请求结果进行统一的预处理 GosnResponseBodyConvert
                //                转化成对象
                .addConverterFactory(ResponseConvertFactory.create())
                //                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        service = retrofit.create(IApiService.class);
    }

    /**
     * 在访问 ApiService 时创建单例
     */
    private static class SingletonHolder {
        private static final ApiService INSTANCE = new ApiService();
    }

    /**
     * 获取单例
     *
     * @return ApiService
     */
    public static ApiService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用于获取测试接口的数据
     */
    @SuppressLint("CheckResult")
    public Flowable<Subject> getHttpResult() {
        return service
                .getHttpResult()
                .map(new HttpResultFunc<>());
    }

    /**
     * 用来统一处理 Http 的 resultCode,并将 HttpResult 的 Data 部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private static class HttpResultFunc<T> implements Function<HttpResult<T>, T> {
        @Override
        public T apply(HttpResult<T> tHttpResult) {
            if (tHttpResult == null) {
                Log.e(TAG, "tHttpResult is null");
                throw new ApiException(100);
            }
            return tHttpResult.getSubjects();
        }
    }

}

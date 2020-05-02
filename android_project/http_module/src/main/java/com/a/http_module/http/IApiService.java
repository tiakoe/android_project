package com.a.http_module.http;


import com.a.http_module.entity.HttpResult;
import com.a.http_module.entity.Subject;

import io.reactivex.Flowable;
import retrofit2.http.GET;


/**
 * 请求接口。
 */
public interface IApiService {

    @GET("userinfo/")
    Flowable<HttpResult<Subject>> getHttpResult();

    //    @GET("test/get")
    //    Flowable<Subject> getSubject();
}

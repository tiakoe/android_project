package com.a.login_module;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RetrofitAPI {

    @FormUrlEncoded
    @POST("fund/fundranking/ajax.htm")
    Call<ApiResult> postJijin(@FieldMap Map<String, String> fieldMap);

    @GET("posts")
    Call<List<ApiResult>> getPosts(@QueryMap Map<String, String> parameters);

    @GET("posts/{id}/comments")
    Call<List<ApiResult>> getComments(@Path("id")Integer postid);

    @GET
    Call<List<ApiResult>> getComments(@Url String url);
}

package com.a.http_module.http;

import android.util.Log;

import com.a.http_module.entity.HttpResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Gson 转换并包含具体的预处理实现。
 */
class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Log.d("Network", "response>>" + response);
        //httpResult 只解析result字段
        HttpResult httpResult = gson.fromJson(response, HttpResult.class);
        // code 为 0 的时候，是正确的返回，其余为错误的返回
        if (httpResult.getCode() != 0) {
            throw new ApiException(httpResult.getCode());
        }
        return gson.fromJson(response, type);
    }
}

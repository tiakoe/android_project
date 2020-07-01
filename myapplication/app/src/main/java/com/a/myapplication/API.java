package com.a.myapplication;

import com.a.myapplication.model.ItemData;
import com.a.myapplication.model.ItemData2;
import com.a.myapplication.model.ItemData3;
import com.a.myapplication.model.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    String Base1 = "http://ceshi.app.beisu100.com/Testapi/";
    String Base2 = "http://ceshi.diandu.beisu100.com/Api/";

//    String URL1 = "http://ceshi.app.beisu100.com/Testapi/api1";  // 顶部轮播图接口
//
//    String URL2 =
//            "http://ceshi.diandu.beisu100.com/Api/getgrade?token=zheshidianduxiaochengxu";   //
//    // 年级列表的接口
//
//    String URL3 = "http://ceshi.app.beisu100.com/Testapi/api3";  // 中间工具的接口

    // 顶部轮播图接口
    @GET("api1")
    Observable<Result<ItemData>> getDataApi1();

    // 年级列表的接口
    @GET("getgrade")
    Observable<Result<ItemData2>> getDataApi2(@Query("token") String token);

    // 中间工具的接口
    @GET("api3")
    Observable<Result<ItemData3>> getDataApi3();

}

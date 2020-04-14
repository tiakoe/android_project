package com.a.login_module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResult {
    @SerializedName("list")
    @Expose
    private String[] field1;

    public String[] getField1(){
        return field1;
    }


}

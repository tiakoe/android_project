package com.a.http_module.entity;


import com.google.gson.annotations.SerializedName;

/**
 * 接口返回数据整体封装。
 * <p>
 * 后台接口返回 json 格式为：
 * {
 * "msg": "success",
 * "code": 0,
 * "data": {
 * "name": "arthinking",
 * "age": 24
 * }
 * }
 */
@SuppressWarnings("unused")
public class HttpResult<T> {

    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private int code;
    //用来模仿Data
    @SerializedName("data")
    private T subjects;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getSubjects() {
        return subjects;
    }

    public void setSubjects(T subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", subjects=" + subjects +
                '}';
    }
}

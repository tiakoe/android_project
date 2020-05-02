package com.a.http_module.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 接口返回的数据中包含的对象封装
 */
@SuppressWarnings("unused")
public class Subject {
    @SerializedName("name")
    private String name;
    @SerializedName("age")
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

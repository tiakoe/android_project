package com.a.myapplication.model;

import com.google.gson.annotations.SerializedName;

//category: "2"
//en_name: ""
//id: "31"
//info: "课本点读，口语评测"
//menu_color: ""
//menu_img: "http://apk.beisuapp.beisu100
// .com//uploads/topmenu/origin/2019/05/20190527111258_30653.png"
//menu_rgb_color: ""
//name: "英语点读"
//sort: "10"
//type: "1,2,3"
//url: "https://study.beisu100.com/English_reader/index.html"

public class ItemData3 {

    @SerializedName("category")
    private String category;
    @SerializedName("en_name")
    private String en_name;
    @SerializedName("id")
    private String id;
    @SerializedName("info")
    private String info;
    @SerializedName("menu_color")
    private String menu_color;
    @SerializedName("menu_img")
    private String menu_img;
    @SerializedName("menu_rgb_color")
    private String menu_rgb_color;
    @SerializedName("name")
    private String name;
    @SerializedName("sort")
    private String sort;
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;

    public String getCategory() {
        return category;
    }

    public String getEn_name() {
        return en_name;
    }

    public String getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public String getMenu_color() {
        return menu_color;
    }

    public String getMenu_img() {
        return menu_img;
    }

    public String getMenu_rgb_color() {
        return menu_rgb_color;
    }

    public String getName() {
        return name;
    }

    public String getSort() {
        return sort;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }



    public void setCategory(String category) {
        this.category = category;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMenu_color(String menu_color) {
        this.menu_color = menu_color;
    }

    public void setMenu_img(String menu_img) {
        this.menu_img = menu_img;
    }

    public void setMenu_rgb_color(String menu_rgb_color) {
        this.menu_rgb_color = menu_rgb_color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }



}

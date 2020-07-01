package com.a.myapplication.model;

import com.google.gson.annotations.SerializedName;

//explain: ""
//        id: "128"
//        img: "http://apk.beisuapp.beisu100.com//uploads/navimg/origin/2019/12/1577696133.png"
//        item_type: "9,11,14,15,16,17"
//        small_img: "http://apk.beisuapp.beisu100.com//uploads/navimg/origin/2019/12/small1577696133.png"
//        type: "1"
//        url: "http://www.beisu100.com/youjingxiaodian/"

public class ItemData {
    @SerializedName("explain")
    private String explain;
    @SerializedName("id")
    private String id;
    @SerializedName("img")
    private String img;
    @SerializedName("item_type")
    private String item_type;
    @SerializedName("small_img")
    private String small_img;
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;

    public String getExplain() {
        return explain;
    }

    public String getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getItem_type() {
        return item_type;
    }

    public String getSmall_img() {
        return small_img;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }



    public void setExplain(String explain) {
        this.explain = explain;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public void setSmall_img(String small_img) {
        this.small_img = small_img;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "ItemData{" +
                "explain='" + explain + '\'' +
                "id='" + id + '\'' +
                "img='" + img + '\'' +
                "item_type='" + item_type + '\'' +
                "small_img='" + small_img + '\'' +
                "type='" + type + '\'' +
                ", url=" + url +
                '}';
    }
}

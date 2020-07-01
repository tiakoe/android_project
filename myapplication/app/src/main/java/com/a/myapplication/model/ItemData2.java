package com.a.myapplication.model;

import com.google.gson.annotations.SerializedName;

//        create_time: "2018-07-10 16:55:33"
//        id: "43"
//        image: "http://ceshidiandu.beisu100.com/202006301757/f2346d3b5ad0f5ed69c059cea6331ef0/upload/1553592237.png"
//        module: "1"
//        name: "国际音标"
//        sort: "105"
public class ItemData2 {

    @SerializedName("create_time")
    private String create_time;
    @SerializedName("id")
    private String id;
    @SerializedName("image")
    private String image;
    @SerializedName("module")
    private String module;
    @SerializedName("name")
    private String name;
    @SerializedName("sort")
    private String sort;

    public String getCreate_time() {
        return create_time;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public String getSort() {
        return sort;
    }



    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }




    @Override
    public String toString() {
        return "ItemData2{" +
                "create_time='" + create_time + '\'' +
                "id='" + id + '\'' +
                "image='" + image + '\'' +
                "module='" + module + '\'' +
                "name='" + name + '\'' +
                "sort='" + sort + '\'' +
                '}';
    }
}

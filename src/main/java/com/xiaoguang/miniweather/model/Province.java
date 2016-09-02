package com.xiaoguang.miniweather.model;

/**
 * Province（省份）的实体类
 * Created by 11655 on 2016/9/2.
 */
public class Province {

    //省份ID
    private int id;

    //省份名称
    private String provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}

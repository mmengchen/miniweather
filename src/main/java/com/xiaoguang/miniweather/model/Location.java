package com.xiaoguang.miniweather.model;

/**
 * 位置相关信息的实体类
 * Created by 11655 on 2016/9/1.
 */
public class Location {
    //定义错误码
    private String errorCode;
    //所在的省份
    private String province;

    //所在城市
    private String city;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Location{" +
                "errorCode='" + errorCode + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

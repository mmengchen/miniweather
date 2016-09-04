package com.xiaoguang.miniweather.model;

/**
 * ListView中item项的参数
 * Created by 11655 on 2016/8/29.
 */
public class ItemWeather {

    //星期
    private String itemWeek;
    //图标
//    private int itemIcon;
    private String weather;
    //最高温度
    private String hightTemperature;

    public String getItemWeek() {
        return itemWeek;
    }

    public void setItemWeek(String itemWeek) {
        this.itemWeek = itemWeek;
    }

    //因天气状态不确定，暂时放弃图标
//    public int getItemIcon() {
//        return itemIcon;
//    }
//
//    public void setItemIcon(int itemIcon) {
//        this.itemIcon = itemIcon;
//    }


    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getHightTemperature() {
        return hightTemperature;
    }

    public void setHightTemperature(String hightTemperature) {
        this.hightTemperature = hightTemperature;
    }

}

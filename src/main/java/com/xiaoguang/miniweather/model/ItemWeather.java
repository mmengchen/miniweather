package com.xiaoguang.miniweather.model;

/**
 * ListView中item项的参数
 * Created by 11655 on 2016/8/29.
 */
public class ItemWeather {

    //星期
    private String itemWeek;
    //图标
    private int itemIcon;
    //最高温度
    private String hightTemperature;
    //最低温度
    private String lowTemperature;

    public String getItemWeek() {
        return itemWeek;
    }

    public void setItemWeek(String itemWeek) {
        this.itemWeek = itemWeek;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getHightTemperature() {
        return hightTemperature;
    }

    public void setHightTemperature(String hightTemperature) {
        this.hightTemperature = hightTemperature;
    }

    public String getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(String lowTemperature) {
        this.lowTemperature = lowTemperature;
    }
}

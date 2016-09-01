package com.xiaoguang.miniweather.model;

/**
 * 未来一周的天气的类
 * Created by 11655 on 2016/8/29.
 */
public class Future {
    //风向
    private String wind;
    //晚上天气
    private String night;
    //时间
    private String date;
    //星期
    private String week;
    //温度
    private String temperature;
    //白天天气
    private String dayTime;

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Future{" +
                "wind='" + wind + '\'' +
                ", night='" + night + '\'' +
                ", date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", temperature='" + temperature + '\'' +
                ", dayTime='" + dayTime + '\'' +
                '}';
    }
}

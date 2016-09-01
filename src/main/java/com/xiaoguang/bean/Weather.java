package com.xiaoguang.bean;

import java.util.List;

/**
 * 天气信息类
 * Created by 11655 on 2016/8/29.
 */
public class Weather {

    //感冒指数
    private String coldIndex;
    //风向
    private String wind;
    //更新时间
    private String updateTime;
    //区县
    private String distrct;
    //穿衣指数
    private String dressingIndex;
    //空气质量
    private String airCondition;
    //日期
    private String date;
    //温度
    private String temperature;
    //城市
    private String city;
    //洗车指数
    private String washIndex;
    //天气更新时时间
    private String time;
    //空气质量指数
    private String pollutionIdex;
    //湿度
    private String humidity;
    //日落时间
    private String sunset;

    //未来一周的天气
    private List<Future> futureList;

    public List<Future> getFutureList() {
        return futureList;
    }

    public void setFutureList(List<Future> futureList) {
        this.futureList = futureList;
    }

    public String getColdIndex() {
        return coldIndex;
    }

    public void setColdIndex(String coldIndex) {
        this.coldIndex = coldIndex;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDistrct() {
        return distrct;
    }

    public void setDistrct(String distrct) {
        this.distrct = distrct;
    }

    public String getDressingIndex() {
        return dressingIndex;
    }

    public void setDressingIndex(String dressingIndex) {
        this.dressingIndex = dressingIndex;
    }

    public String getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(String airCondition) {
        this.airCondition = airCondition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWashIndex() {
        return washIndex;
    }

    public void setWashIndex(String washIndex) {
        this.washIndex = washIndex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPollutionIdex() {
        return pollutionIdex;
    }

    public void setPollutionIdex(String pollutionIdex) {
        this.pollutionIdex = pollutionIdex;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "coldIndex='" + coldIndex + '\'' +
                ", wind='" + wind + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", distrct='" + distrct + '\'' +
                ", dressingIndex='" + dressingIndex + '\'' +
                ", airCondition='" + airCondition + '\'' +
                ", date='" + date + '\'' +
                ", temperature='" + temperature + '\'' +
                ", city='" + city + '\'' +
                ", washIndex='" + washIndex + '\'' +
                ", time='" + time + '\'' +
                ", pollutionIdex='" + pollutionIdex + '\'' +
                ", humidity='" + humidity + '\'' +
                ", sunset='" + sunset + '\'' +
                ", futureList=" + futureList +
                '}';
    }
}

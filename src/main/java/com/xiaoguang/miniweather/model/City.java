package com.xiaoguang.miniweather.model;

/**
 * City（城市） 的实体类
 * Created by 11655 on 2016/8/29.
 */
public class City {
   //城市的Id
   private int id;

   //城市的名称
   private String cityName;

   //省份的ID
   private int provinceId;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getCityName() {
      return cityName;
   }

   public void setCityName(String cityName) {
      this.cityName = cityName;
   }

   public int getProvinceId() {
      return provinceId;
   }

   public void setProvinceId(int provinceId) {
      this.provinceId = provinceId;
   }
}

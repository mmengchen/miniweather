package com.xiaoguang.miniweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiaoguang.miniweather.model.City;
import com.xiaoguang.miniweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用的数据库操作
 * Created by 11655 on 2016/9/2.
 */
public class MiniWeatherDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "mini_weather";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    private static MiniWeatherDB miniWeatherDB;
    private SQLiteDatabase db;
    /**
     * 将构造方法私有化
     */
    private MiniWeatherDB(Context context) {
        MiniWeatherOpenHelper dbHelper = new MiniWeatherOpenHelper(context,
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }
    /**
     * 获取 MiniWeatherDB 的实例。
     */
    public synchronized static MiniWeatherDB getInstance(Context context) {
        if (miniWeatherDB == null) {
            miniWeatherDB = new MiniWeatherDB(context);
        }
        return miniWeatherDB;
    }
    /**
     * 将 Province 实例存储到数据库。
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            db.insert("Province", null, values);
        }
    }
    /**
     * 从数据库读取全国所有的省份信息。
     */
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }
    /**
     * 将 City 实例存储到数据库。
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }
    /**
     * 从数据库读取某省下所有的城市信息。
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]
                { String.valueOf(provinceId) }, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
    }
}

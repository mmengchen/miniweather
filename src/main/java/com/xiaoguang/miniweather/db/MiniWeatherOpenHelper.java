package com.xiaoguang.miniweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *  数据库的操作类
 *  进行建表操作
 * Created by 11655 on 2016/9/2.
 */
public class MiniWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     *  Province 表建表语句
     */
    public static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement, "
            + "province_name text)";
    /**
     *  City 表建表语句
     */
    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city_name text, "
            + "province_id integer)";

    public MiniWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROVINCE); // 创建 Province 表
        sqLiteDatabase.execSQL(CREATE_CITY); // 创建 City 表
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

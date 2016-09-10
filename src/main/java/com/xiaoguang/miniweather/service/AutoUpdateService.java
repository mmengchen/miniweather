package com.xiaoguang.miniweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.xiaoguang.miniweather.receiver.AutoUpdateReceiver;
import com.xiaoguang.miniweather.utils.HttpCallbackListener;
import com.xiaoguang.miniweather.utils.HttpUtil;
import com.xiaoguang.miniweather.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 自动更新的服务
 * 没隔8小时执行一次更新服务
 * Created by 11655 on 2016/9/8.
 */
public class AutoUpdateService extends Service {
    private static final String SHAREDPREFERENCES_NAME ="first_pref";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        timedExecute();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 定时任务，设置8小时执行一次
     */
    private void timedExecute() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000;
        long triggerAtTieme = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTieme,pi);
    }

    /**
     * 更新天气
     */
    private void updateWeather() {
        SharedPreferences prefs = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        String city = prefs.getString("city","");
        String province = prefs.getString("province","");
        String address = "";
        try {
            //定义请求天气的URL地址
            address = "http://apicloud.mob.com/v1/weather/query?key=1686180062336&city="
                    + URLEncoder.encode(city, "UTF-8") + "&province=" + URLEncoder.encode(province, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //获取天气信息
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //处理天气情况
                Utility.handleWeatherResponse(AutoUpdateService.this,response);
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}

package com.xiaoguang.miniweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xiaoguang.miniweather.service.AutoUpdateService;

/**
 * 自动更新天气的广播接收者
 * Created by 11655 on 2016/9/8.
 */
public class AutoUpdateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        //启动这个后台的服务
        context.startService(i);
    }
}

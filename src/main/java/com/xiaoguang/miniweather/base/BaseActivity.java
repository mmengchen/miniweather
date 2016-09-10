package com.xiaoguang.miniweather.base;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * 功能 :所有Activity的父类 extends AppCompatActivity
 * Created by xiaoguang on 2016/8/28.
 */
public  abstract class BaseActivity extends AppCompatActivity {

    /**
     *  功能：实现沉浸式通知栏，使通知栏和APP的标题颜色一样
     */
    protected void immersiveNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    /**
     * 功能:初始化视图
     * */
    protected abstract void initView();

    /**
     * 功能:初始化数据
     */
    protected abstract void initData();

    /**
     * 功能:初始化操作，对各个控件的监听事件进行初始化操作
     */
    protected abstract void initOperate();
}

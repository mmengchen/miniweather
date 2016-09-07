package com.xiaoguang.miniweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.base.BaseActivity;

/**
 *      启动画面
 *      (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *      (2)是，则进入ChooseAreaActivity；否，则进入WeatherActivity
 *      (3)3s后执行(2)操作
 */
public class SplashActivity extends BaseActivity {
    private static final int GO_HOME = 1000;
    private static final int GO_CHOOSEAREA = 1001;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;

    /**
     *  保存的文件名
     */
    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    private SharedPreferences preferences;

    /*
    * Handler:跳转到不同界面
    */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_CHOOSEAREA:
                    goChooseArea();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
        initOperate();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initOperate() {

    }

    @Override
    protected void initData() {
        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的是否已经选过城市
        preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
        if (isFirstRun){//判断是否为第一次启动
            // 使用Handler的postDelayed方法，3秒后执行跳转到ChooseAreaActivity
            mHandler.sendEmptyMessageDelayed(GO_CHOOSEAREA, SPLASH_DELAY_MILLIS);
        }else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        }
    }
    private void goHome() {
        //读取SharedPreferences中需要的数据,获取省份和城市的信息
        String province = preferences.getString("province","");
        String city = preferences.getString("city","");
        Bundle bundle = new Bundle();

        //将省份和城市信息存放到Bundle中
        bundle.putString("province",province);
        bundle.putString("city",city);
        Intent intent = new Intent(SplashActivity.this, WeatherActivity.class);

        //将bundle放入到intent中
        intent.putExtra("info",bundle);

        //跳转到天气的主页面
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private void goChooseArea() {
        Intent intent = new Intent(SplashActivity.this,ChooseAreaActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}

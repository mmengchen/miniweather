package com.xiaoguang.miniweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.base.BaseActivity;

/**
 *      启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *     (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 */
public class SplashActivity extends BaseActivity {
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;

    /**
     *
     *  保存的文件名
     */
    private static final String SHAREDPREFERENCES_NAME = "first_pref";

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
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
        if (isFirstRun){
            Log.i("myTag","我是第一次启动");
            //启动选择城市的界面
            Intent intent = new Intent(SplashActivity.this,ChooseAreaActivity.class);
            startActivity(intent);
        }else {
            Log.i("myTag","我不是第一次启动");
            //从文件中获取省份和城市的信息
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
            startActivity(intent);
        }
    }
}

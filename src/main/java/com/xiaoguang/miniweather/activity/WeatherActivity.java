package com.xiaoguang.miniweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.base.BaseActivity;
import com.xiaoguang.miniweather.control.MyAdapter;
import com.xiaoguang.miniweather.model.ItemWeather;
import com.xiaoguang.miniweather.model.Weather;
import com.xiaoguang.miniweather.utils.HttpCallbackListener;
import com.xiaoguang.miniweather.utils.HttpUtil;
import com.xiaoguang.miniweather.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页面的Activity
 */
public class WeatherActivity extends BaseActivity{

    //声明Xml中使用的控件对象
    private Button buttonTest;
    private ListView listView;
    private ImageButton imageButtonMenu;
    private List<ItemWeather> itemWeatherList;
    private static TextView mTextViewCity;
    private TextView mTextViewWeather;
    private static TextView mTextViewTemps;
    private TextView mTextViewTextTody;

    //用于测试权限
    private static final int BAIDU_READ_PHONE_STATE =100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initOperate();
    }
    @Override
    protected void initView() {
        //调用父类的沉浸式通知栏的方法
        super.immersiveNotification();

        //初始化控件
        buttonTest = (Button)findViewById(R.id.act_main_btn_test);
        listView = (ListView)findViewById(R.id.act_main_lv_weekp);
        imageButtonMenu = (ImageButton) findViewById(R.id.act_main_iv_add);
        mTextViewCity = (TextView) findViewById(R.id.act_main_tv_city);
        mTextViewTemps = (TextView)findViewById(R.id.act_main_tv_temp);
        mTextViewTextTody = (TextView) findViewById(R.id.act_main_tv_today);
        mTextViewWeather = (TextView) findViewById(R.id.act_main_tv_weather);
    }

    /**
     * 为控件添加点击事件
     */
    @Override
    protected void initOperate() {
        //为测试按钮添加事件
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //操作数据
                //获取位置信息
                Log.i("myTag","我正在进行数据测试");
            }
        });
        //为图片按钮添加监听事件
        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到添加城市的界面
                startActivity(new Intent(WeatherActivity.this,AddCityActivity.class));
            }
        });
    }

    @Override
    protected void initData() {
        //获取Intent对象
        Intent intent = getIntent();
        //从intent 取出 Bundle
        Bundle bundle = intent.getBundleExtra("info");
        String province = bundle.getString("province");
        String city = bundle.getString("city");

        //为控件设置值
        mTextViewCity.setText(city);
        String address = "";
        try {
            //定义请求天气的URL地址
            address = "http://apicloud.mob.com/v1/weather/query?key=1686180062336&city="
                    + URLEncoder.encode(city,"UTF-8")+"&province="+URLEncoder.encode(province,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //获取天气信息
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //解析天气数据
                final Weather weather = Utility.handleWeatherResponse(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置天气情况
                        mTextViewWeather.setText(weather.getFutureList().get(0).getNight());
                        //设置温度
                        mTextViewTemps.setText(weather.getTemperature());
                        //设置今天是

                        //设置未来天气
                        itemWeatherList = new ArrayList<ItemWeather>();
                        for (int i = 1;i<weather.getFutureList().size();i++){
                            ItemWeather itemWeather = new ItemWeather();
                            itemWeather.setItemWeek(weather.getFutureList().get(i).getWeek());
                            //图标暂时不进行设置
                            itemWeather.setWeather(weather.getFutureList().get(i).getNight());
                            itemWeather.setHightTemperature(weather.getFutureList().get(i).getTemperature());
                            //向ListView中添加数据
                            itemWeatherList.add(itemWeather);
                        }
                        //绑定适配器
                        listView.setAdapter(new MyAdapter(WeatherActivity.this,itemWeatherList,R.layout.list_item));
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 返回键直接退出系统
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

package com.xiaoguang.miniweather.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.base.BaseActivity;
import com.xiaoguang.miniweather.control.MyAdapter;
import com.xiaoguang.miniweather.model.ItemWeather;
import com.xiaoguang.miniweather.model.Weather;

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

    /*
        声明LocationClient 和 BDLocationListener 类
    */
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化参数
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initView();
        initData();
        initOperate();
        initLocation();

        Log.i("myTag","on create ");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        //在APP第一次打开时开始定位，存在问题
//        synchronized (this){
//            mLocationClient.start();
//        }

        //获取位置，由于无法获取位置
//        Log.i("myTag","on Start ");
//
//        Location location = MyUtils.location;
//        Log.i("myTag","location 是否为空"+location.toString());
//        //获取省份和城市
//        String province = location.getProvince();
//        String city = location.getCity();
//        //设置UI中的城市信息
//        mTextViewCity.setText(city);
//        //当程序开始启动时获取城市信息
//
//        // 获取天气信息
//        MyUtils.getInfo(handler,2,city,province);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("myTag","on resume ");

        //当程序可操作时进行定位
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("myTag","on pause ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("myTag","on stop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("myTag","on restart ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("myTag","我销毁了");
    }
    /**
     * 获取权限的回调方法6.0中需要注意
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                Log.i("","我被调用了，并且获取到了相应的权限");
            }
            else
            {
                // 没有获取到权限，做特殊处理
            }
            break;
            default:
                break;
        }
    }

    //声明一个消息传输机制
    static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此处用于更新UI操作
            switch (msg.what){
                case 1:
                    break;
                case 2:
                    //获取天气信息
                    Weather weather = (Weather) msg.obj;
                    mTextViewTemps.setText(weather.getTemperature());
                    mTextViewCity.setText(weather.getCity());
                    break;
            }
        }
    };
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

//                if(mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) !=PackageManager.PERMISSION_GRANTED)
//                {
//                    // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
//                    requestPermissions( new String[]{ Manifest.permission.READ_PHONE_STATE },BAIDU_READ_PHONE_STATE);
//                }
                //获取地理位置
//                mLocationClient.start();
                //获取城市列表
//                MyUtils.getInfo(handler,1,null,null);
                //获取天气信息
//                MyUtils.getInfo(handler,2,"葫芦岛","辽宁");
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
//        //定义一个Json 字符串（此字符串应该是从服务器获取的）
//        String strJson ="{\"msg\":\"success\",\"result\":[{\"airCondition\":\"良\",\"city\":\"葫芦岛\",\"coldIndex\":\"低发期\",\"date\":\"2016-08-27\",\"distrct\":\"葫芦岛\",\"dressingIndex\":\"单衣类\",\"exerciseIndex\":\"适宜\",\"future\":[{\"date\":\"2016-08-27\",\"night\":\"晴\",\"temperature\":\"14°C\",\"week\":\"今天\",\"wind\":\"北风 3～4级\"},{\"date\":\"2016-08-28\",\"dayTime\":\"晴\",\"night\":\"晴\",\"temperature\":\"29°C / 14°C\",\"week\":\"星期日\",\"wind\":\"北风 3～4级\"},{\"date\":\"2016-08-29\",\"dayTime\":\"晴\",\"night\":\"晴\",\"temperature\":\"29°C / 15°C\",\"week\":\"星期一\",\"wind\":\"北风 4～5级\"},{\"date\":\"2016-08-30\",\"dayTime\":\"晴\",\"night\":\"阵雨\",\"temperature\":\"26°C / 14°C\",\"week\":\"星期二\",\"wind\":\"北风 4～5级\"},{\"date\":\"2016-08-31\",\"dayTime\":\"阵雨\",\"night\":\"阵雨\",\"temperature\":\"27°C / 15°C\",\"week\":\"星期三\",\"wind\":\"西北风 4～5级\"},{\"date\":\"2016-09-01\",\"dayTime\":\"阵雨\",\"night\":\"晴\",\"temperature\":\"28°C / 16°C\",\"week\":\"星期四\",\"wind\":\"南风 3～4级\"},{\"date\":\"2016-09-02\",\"dayTime\":\"晴\",\"night\":\"晴\",\"temperature\":\"27°C / 17°C\",\"week\":\"星期五\",\"wind\":\"东南风 3～4级\"},{\"date\":\"2016-09-03\",\"dayTime\":\"阵雨\",\"night\":\"零散阵雨\",\"temperature\":\"27°C / 18°C\",\"week\":\"星期六\",\"wind\":\"东北偏东风 2级\"},{\"date\":\"2016-09-04\",\"dayTime\":\"阵雨\",\"night\":\"局部多云\",\"temperature\":\"27°C / 18°C\",\"week\":\"星期日\",\"wind\":\"东北风 2级\"},{\"date\":\"2016-09-05\",\"dayTime\":\"局部多云\",\"night\":\"局部多云\",\"temperature\":\"27°C / 19°C\",\"week\":\"星期一\",\"wind\":\"东北偏北风 2级\"}],\"humidity\":\"湿度：52%\",\"pollutionIndex\":\"59\",\"province\":\"辽宁\",\"sunrise\":\"05:19\",\"sunset\":\"18:37\",\"temperature\":\"25℃\",\"time\":\"18:10\",\"updateTime\":\"20160827183422\",\"washIndex\":\"不太适宜\",\"weather\":\"多云\",\"week\":\"周六\",\"wind\":\"南风2级\"}],\"retCode\":\"200\"}";
//        //进行Json解析
//        //获取Control对象
//        Controls controls = Controls.getControlsInstance();
//        //暂不进行接收数据，操作在其他类中处理
//        controls.JsonToObject(strJson);


        //向ListView中添加数据
        itemWeatherList = new ArrayList<ItemWeather>();

        /*
        * 数据应该为从网络上接收，暂时使用自定义的数据
        * */
        ItemWeather itemWeather = new ItemWeather();
        itemWeather.setItemWeek("星期二");
        itemWeather.setItemIcon(R.drawable.duoyun);
        itemWeather.setHightTemperature("35");
        itemWeather.setLowTemperature("15");

        ItemWeather itemWeather2 = new ItemWeather();
        itemWeather2.setItemWeek("星期三");
        itemWeather2.setItemIcon(R.drawable.duoyun);
        itemWeather2.setHightTemperature("35");
        itemWeather2.setLowTemperature("15");

        ItemWeather itemWeather3 = new ItemWeather();
        itemWeather3.setItemWeek("星期四");
        itemWeather3.setItemIcon(R.drawable.duoyun);
        itemWeather3.setHightTemperature("35");
        itemWeather3.setLowTemperature("15");

        ItemWeather itemWeather4 = new ItemWeather();
        itemWeather4.setItemWeek("星期五");
        itemWeather4.setItemIcon(R.drawable.duoyun);
        itemWeather4.setHightTemperature("35");
        itemWeather4.setLowTemperature("15");

        ItemWeather itemWeather5 = new ItemWeather();
        itemWeather5.setItemWeek("星期六");
        itemWeather5.setItemIcon(R.drawable.duoyun);
        itemWeather5.setHightTemperature("35");
        itemWeather5.setLowTemperature("15");

        ItemWeather itemWeather6 = new ItemWeather();
        itemWeather6.setItemWeek("星期日");
        itemWeather6.setItemIcon(R.drawable.duoyun);
        itemWeather6.setHightTemperature("35");
        itemWeather6.setLowTemperature("15");

        ItemWeather itemWeather7 = new ItemWeather();
        itemWeather7.setItemWeek("星期一");
        itemWeather7.setItemIcon(R.drawable.duoyun);
        itemWeather7.setHightTemperature("38");
        itemWeather7.setLowTemperature("20");

        //将数据添加到List集合中
        itemWeatherList.add(itemWeather);
        itemWeatherList.add(itemWeather2);
        itemWeatherList.add(itemWeather3);
        itemWeatherList.add(itemWeather4);
        itemWeatherList.add(itemWeather5);
        itemWeatherList.add(itemWeather6);
        itemWeatherList.add(itemWeather7);

        //绑定适配器
        listView.setAdapter(new MyAdapter(this,itemWeatherList,R.layout.list_item));
    }

    /**
     * 功能：初始化SDK，用于获取用户所在位置
     * */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
}

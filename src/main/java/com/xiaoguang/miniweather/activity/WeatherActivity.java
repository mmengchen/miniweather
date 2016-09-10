package com.xiaoguang.miniweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.adapter.WeatherListAdapter;
import com.xiaoguang.miniweather.base.BaseActivity;
import com.xiaoguang.miniweather.model.ItemWeather;
import com.xiaoguang.miniweather.utils.HttpCallbackListener;
import com.xiaoguang.miniweather.utils.HttpUtil;
import com.xiaoguang.miniweather.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示天气的Activity
 */
public class WeatherActivity extends BaseActivity implements View.OnTouchListener {

    //声明Xml中使用的控件对象
    private Button buttonTest;
    private ListView listView;
    private ImageButton imageButtonMenu;
    private static TextView mTextViewCity;
    private TextView mTextViewWeather;
    private static TextView mTextViewTemps;
    private TextView mTextViewTextTody;
    private TextView mTextViewPublish;

    // 顶部刷新视图的view
    private View top;
    // 顶部刷新视图中的文字提示
    private TextView topTV;
    // 顶部刷新试图的图片提示
    private ImageView topIv;
    // 顶部刷新视图的参数，通过此参数来更改topMargin的值
    private LinearLayout.LayoutParams topParams;

    //ScrollViw

    private ScrollView scrollView;

    /**
     * 顶部刷新视图的高度，通过topmargin和这个值进行比较来判断刷新视图的状态
     */
    private int topHeight = 80;

    /**
     * 当listview里面的第一个item可见的时候，初始化此值。即当刷新视图出现的时候手指所在的Y轴的位置
     */
    private int currentY;

    // 这三个值分别是手指按下，移动，和抬起的时候手指的Y轴位置
    private int moveY;
    private int downY;
    private int upY;

    /**
     * 这个值用于判断
     */
    private boolean isRead = false;

    /**
     * 这个值用于判断当前是不是正在加载数据，如果正在加载，则不对刷新视图重新进行操作;
     * 如果没有更新，则对刷新视图进行操作
     */
    private boolean isLoading = false;

    //判断是否来自ChooseAreaActivity
    private boolean isFromChooseAreaAtivity = false;
    /**
     * 定义的数据源
     */
    private List<ItemWeather> itemWeatherList;
    //用于接收城市和省份信息
    private String province;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //初始化数据
        initData();
        initOperate();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initView() {
        //调用父类的沉浸式通知栏的方法
        super.immersiveNotification();

        //初始化控件
        top = findViewById(R.id.load_layout);
        scrollView = (ScrollView) findViewById(R.id.act_main_sv);
        topTV = (TextView) findViewById(R.id.main_activity_top_tv);
        mTextViewTextTody = (TextView) findViewById(R.id.act_main_tv_today);
        mTextViewWeather = (TextView) findViewById(R.id.act_main_tv_weather);
        mTextViewCity = (TextView) findViewById(R.id.act_main_tv_city);
        mTextViewTemps = (TextView) findViewById(R.id.act_main_tv_temp);
        mTextViewPublish = (TextView)findViewById(R.id.act_main_tv_publish);
        topIv = (ImageView) findViewById(R.id.main_activity_top_iv);
        buttonTest = (Button) findViewById(R.id.act_main_btn_test);
        listView = (ListView) findViewById(R.id.act_main_lv_weekp);
        imageButtonMenu = (ImageButton) findViewById(R.id.act_main_iv_add);
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
                Log.i("myTag", "我正在进行数据测试");
            }
        });
        //为图片按钮添加监听事件
        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将城市信息返回到
                //跳转到添加城市的界面
                String city = mTextViewCity.getText().toString().trim();
                String temps = mTextViewTemps.getText().toString().trim();
                Intent intent = new Intent(WeatherActivity.this, AddCityActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("temps", temps);
                startActivity(intent);

            }
        });

        //为上部的 ScrollView设置触摸事件
        scrollView.setOnTouchListener(this);
    }

    @Override
    protected void initData() {
        //初始化各值
        topParams = (LinearLayout.LayoutParams) top.getLayoutParams();
        topParams.height = topHeight;
        topParams.topMargin = -topHeight;

        //获取Intent对象
        Intent intent = getIntent();
        isFromChooseAreaAtivity = intent.getBooleanExtra("isFromChooseAreaAtivity",false);
        //从intent 取出 Bundle
        Bundle bundle = intent.getBundleExtra("info");
        province = bundle.getString("province");
        city = bundle.getString("city");
        if(isFromChooseAreaAtivity){
            //从服务器获取天气情况
            queryFromServer(city, province);
        }else{
            //从本地读取天气情况
            showWeather();
        }
        //为控件设置值
        mTextViewCity.setText(city);
    }

    /**
     * 功能：请求服务器上的天气信息，并显示到控件上
     *
     * @param city
     * @param province
     */
    private void queryFromServer(String city, String province) {
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
               Utility.handleWeatherResponse(WeatherActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();

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
     * 从 SharedPreferences 文件中读取存储的天气信息，并显示到界面上。
     *
     */
    private void showWeather() {
        //获取Preferences 对象
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //读取文件中的天气情况，并显示到控件上
        //设置天气情况
        mTextViewWeather.setText(prefs.getString("weather",""));
        //设置温度
        mTextViewTemps.setText(prefs.getString("temp",""));
        //设置今天
        mTextViewTextTody.setText(Utility.getTodayWeek()+"  今天");

        //设置发布时间
        mTextViewPublish.setText("发布时间:" + prefs.getString("update_time",""));
        //获取未来天气的个数
        int cout = prefs.getInt("count",0);
        itemWeatherList = new ArrayList<ItemWeather>();
        //获取出未来的天气
        for (int i = 1;i<cout;i++){
            //创建一个 itemweather对象
            ItemWeather itemWeather = new ItemWeather();
            String week = prefs.getString("week"+i,"");
            String night = prefs.getString("night"+i,"");
            String temps = prefs.getString("temps"+i,"");
            //为itemweather设置数据
            itemWeather.setItemWeek(week);
            itemWeather.setWeather(night);
            itemWeather.setHightTemperature(temps);
            //向ListView中添加数据
            itemWeatherList.add(itemWeather);
        }
        //绑定适配器
        listView.setAdapter(new WeatherListAdapter(WeatherActivity.this, itemWeatherList, R.layout.list_item));
        //关闭刷新栏
        new ScrollTask().execute(-10);
        Toast.makeText(getApplicationContext(), "数据刷新完成", Toast.LENGTH_SHORT).show();
    }

    /**
     * 屏蔽返回键，直接退出系统
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 触摸事件,实现刷新功能
     *
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int actionValue = motionEvent.getAction();
        int margin = 0;
        switch (actionValue) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //如果没有加载数据则对刷新界面进行操作
                if (!isLoading) {
                    moveY = (int) motionEvent.getRawY();
                    if (scrollView.getVerticalScrollbarPosition() == 0 && !isRead) {
                        isRead = true;
                        currentY = moveY;
                    }
                    if (isRead && currentY >= downY) {
                        margin = -(topHeight - (moveY - currentY));
                        changeTop(margin);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isRead = false;
                if (!isLoading) {
                    upY = (int) motionEvent.getRawY();
                    // 计算手指离开的时候刷新视图的margin值
                    margin = upY - currentY - topHeight;
                    if (margin < -topHeight) {
                        margin = -topHeight;
                    }
                    if (margin > 0) {
                        margin = 0;
                    }
                    // 如果手指离开的时候margin小于top布局的高度的话则就不进行刷新,否则就进行刷新
                    if (Math.abs(margin) == 0) {
                        topIv.setImageResource(R.drawable.loading);
                        topTV.setText("正在加载...");
                        //从服务器上查询天气
                        queryFromServer(city, province);
                        isLoading = true;
                    } else if (margin > -topHeight) {
                        new ScrollTask().execute(-10);
                        isLoading = true;
                    }
                    break;
                }
        }
        return false;
    }

    /**
     * 根据margin的值来改变刷新视图的状态
     *
     * @param margin
     */
    private void changeTop(int margin) {
        //如果margin的值大于刷新界面的宽度的话，就把它设置为刷新界面的宽度，因为不能让整个刷新界面离开顶部
        if (margin < (-topHeight)) {
            margin = (-topHeight);
        }
        //同理，为保证整个刷新界面不离开顶部，margin也不能大于零
        if (margin > 0) {
            margin = 0;
            topTV.setText("松开进行刷新...");
            topIv.setRotation(180F);
        }
        topParams.topMargin = margin;
        top.setLayoutParams(topParams);
    }

    /**
     * 数据加载完成的时候调用，将刷新视图初始化 并将是否正在加载数据的判断属性设置为false
     */
    public void loadComplete() {
        topIv.setImageResource(R.drawable.loading);
        topIv.setRotation(0F);
        topTV.setText("下拉刷新...");
        isLoading = false;
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int topMargin = topParams.topMargin;
            while (true) {
                topMargin = topMargin + params[0];
                if (topMargin > 0) {
                    topMargin = 0;
                    break;
                }
                if (topMargin < -topHeight) {
                    topMargin = -topHeight;
                    break;
                }
                publishProgress(topMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动画面
                sleep(20);
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            topParams.topMargin = values[0];
            top.setLayoutParams(topParams);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            topParams.topMargin = result;
            top.setLayoutParams(topParams);
            loadComplete();
        }

        /**
         * 使当前线程睡眠指定的毫秒数
         *
         * @param i
         */
        private void sleep(int i) {
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.xiaoguang.miniweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.base.BaseActivity;
import com.xiaoguang.miniweather.db.MiniWeatherDB;
import com.xiaoguang.miniweather.model.City;
import com.xiaoguang.miniweather.model.Province;
import com.xiaoguang.miniweather.utils.HttpCallbackListener;
import com.xiaoguang.miniweather.utils.HttpUtil;
import com.xiaoguang.miniweather.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends BaseActivity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private MiniWeatherDB miniWeatherDB;
    private List<String> dataList = new ArrayList<String>();
    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;
    /**
     * 是否从WeatherActivity中跳转过来。
     */
    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        initView();
        initData();
        initOperate();
    }

    @Override
    protected void initView() {
        titleText  = (TextView)findViewById(R.id.title_text);
        listView = (ListView) findViewById(R.id.list_view);
    }

    @Override
    protected void initOperate() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long arg3) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(index);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                        selectedCity = cityList.get(index);
                    Bundle bundle = new Bundle();
                    //将省份和城市信息存放到Bundle中
                    bundle.putString("province",selectedProvince.getProvinceName());
                    bundle.putString("city",selectedCity.getCityName());
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    //将bundle放入到intent中
                    intent.putExtra("info",bundle);
                    //跳转到天气的主页面
                    startActivity(intent);
                }
            }
        });
    }

    protected void initData() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        //获取数据库操作对象
        miniWeatherDB =miniWeatherDB.getInstance(this);
        queryProvinces();  // 加载省级数据
    }

    /**
     * 查询省份信息，如果数据库存在则读取不存在则去服务器获取
     */
    private void queryProvinces() {
        provinceList = miniWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
             }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            //调用服务器的
            queryFromServer();
        }
    }
    /**
     * 查询城市信息
     */
    private void queryCities() {
        //通过省份的ID查询数据库中的城市信息
        cityList = miniWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city: cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(""+selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
           Toast.makeText(ChooseAreaActivity.this,"加载城市信息失败",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从服务器查询数据
     */
    private void queryFromServer() {

        //定义连接服务器的地址
        String address  ="http://apicloud.mob.com/v1/weather/citys?key=1686180062336";
        showProgressDialog();//打开一个对话框
        //向服务器发送请求
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //设置一个标记
                boolean result = false;
                result  = Utility.handleProvincesResponse(miniWeatherDB,response);

                if (result) {//判断返回值
                    // 通过 runOnUiThread()方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();//关闭对话框
                            queryProvinces();//加载省份
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                // 通过 runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,
                                "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度条对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     *    捕获 Back 按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            if (isFromWeatherActivity) {
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }
}

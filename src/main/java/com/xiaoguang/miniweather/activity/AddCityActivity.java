package com.xiaoguang.miniweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.adapter.CityListAdapter;
import com.xiaoguang.miniweather.base.BaseActivity;
import com.xiaoguang.miniweather.model.Item;
import com.xiaoguang.miniweather.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 添加城市的Activity
 * */
public class AddCityActivity extends BaseActivity{
    private ListView listView;
    private List<Item> itemList;
    private TextView tvTime;
    private ImageView imgbtnAdd;
    private String city;
    private String temps = "-----";
    private String province;
    //用于记录保存文件中个数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        initView();
        initData();
        initOperate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void initView() {
        super.immersiveNotification();
        listView = (ListView)findViewById(R.id.act_add_city_lv);
        imgbtnAdd = (ImageView) findViewById(R.id.act_add_city_ib_add);
        tvTime = (TextView) findViewById(R.id.act_add_city_tv_datetime);
    }

    @Override
    protected void initData() {

        //获取的本地时间
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String time = format.format(curDate);
        //设置本地时间
        tvTime.setText(time);
        itemList = new ArrayList<Item>();
        loadInfoData();
        //绑定适配器
        listView.setAdapter(new CityListAdapter(this,itemList,R.layout.list_item_city));
    }
    @Override
    protected void initOperate() {
        imgbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCityActivity.this,ChooseAreaActivity.class);
                //设置来自本Activity属性
                intent.putExtra("isFromAddACtivity",true);
                //跳转到选择城市界面
                startActivity(intent);
                finish();
            }
        });
    }


    /**
     * 读取文件中的省份和城市信息
     */
    private void loadInfoData() {
        //获取SharePreferences 对象
        SharedPreferences pref = getSharedPreferences("citys_pref",MODE_APPEND);
        int count = pref.getInt("count",1);
        LogUtil.i("myTag","我要的数量为"+count);
        itemList.clear();
        for(int i = 1;i<=count;i++){
            String city = pref.getString("city"+i,"");
            String province = pref.getString("province"+i,"");
            Log.i("myTag","我从文件中读取的"+city);
            Item item = new Item();
            item.setCity(city);
            item.setProvince(province);
            item.setT(temps);
            itemList.add(item);
        }
    }
}

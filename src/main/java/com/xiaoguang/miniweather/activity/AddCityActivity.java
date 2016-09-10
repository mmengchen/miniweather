package com.xiaoguang.miniweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.adapter.CityListAdapter;
import com.xiaoguang.miniweather.base.BaseActivity;
import com.xiaoguang.miniweather.model.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 添加城市的Activity
 * */
public class AddCityActivity extends BaseActivity {
    private ListView listView;
    private List<Item> itemList;
    private ImageView imgbtnAdd;
    private String city;
    private String temps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        initView();
        initData();
        initOperate();
    }

    @Override
    protected void initView() {
        super.immersiveNotification();
        listView = (ListView)findViewById(R.id.act_add_city_lv);
        imgbtnAdd = (ImageView) findViewById(R.id.act_add_city_ib_add);
    }

    @Override
    protected void initData() {
        itemList = new ArrayList<Item>();
        //获取主页面传过来的数据
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        temps = intent.getStringExtra("temps");
        Item item = new Item();
        //此时间应该为获取的获取的本地时间，显示格式有问题
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String time = format.format(curDate);
        item.setDateTime(time);
        item.setCity(city);
        item.setT(temps);
        itemList.add(item);
        //绑定适配器
        listView.setAdapter(new CityListAdapter(this,itemList,R.layout.list_item_city));
        listView.deferNotifyDataSetChanged();
    }
    @Override
    protected void initOperate() {
        imgbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCityActivity.this,ChooseAreaActivity.class);
                //设置来自本Activity属性
                intent.putExtra("isFromAddACtivity",true);
                //设置带返回值跳转
                startActivityForResult(intent,5566);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5566&&requestCode==5566){
            Log.i("myTag","我是从选择天气中国过来的"+data.getBundleExtra("info").getString("city"));
            //获取城市信息显示到ListView上
            String city = data.getBundleExtra("info").getString("city");
            String province = data.getBundleExtra("info").getString("province");
            Item item = new Item();
            item.setCity(city);
            //存在问题，无法获取温度情况
            item.setDateTime("");
            item.setCity(city);
            item.setT("25℃ ");
            itemList.add(item);
            listView.deferNotifyDataSetChanged();
            //将信息保存到本地，当起来的时候，读取文件信息
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(new CityListAdapter(this,itemList,R.layout.list_item_city));
    }
}

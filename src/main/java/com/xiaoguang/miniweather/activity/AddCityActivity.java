package com.xiaoguang.miniweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.base.BaseActivity;
import com.xiaoguang.miniweather.control.MyAdapter2;
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
    private ImageButton imgbtnAdd;
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
        imgbtnAdd = (ImageButton) findViewById(R.id.act_add_city_ib_add);
    }

    @Override
    protected void initOperate() {
        imgbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCityActivity.this,SelectCityActivity.class);
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void initData() {
        itemList = new ArrayList<Item>();
        //获取主页面传过来的数据
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String temps = intent.getStringExtra("temps");
        Item item = new Item();
        //此时间应该为获取的获取的本地时间
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String time = format.format(curDate);
        item.setDateTime(time);
        item.setCity(city);
        item.setT(temps);
        itemList.add(item);
        //绑定适配器
        listView.setAdapter(new MyAdapter2(this,itemList,R.layout.list_item_city));
        listView.deferNotifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==100){
                //获取选择城市的数据，并添加到集合中，实现数据的更新操作
            }
        }
    }
}

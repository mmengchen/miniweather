package com.xiaoguang.miniweather.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.base.BaseActivity;

public class SelectCityActivity extends BaseActivity {

    private AutoCompleteTextView autoText;
    private TextView mTextViewShow;
    String[] province;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initView();
        initData();
        initOperate();
    }

    @Override
    protected void initView() {
        autoText = ((AutoCompleteTextView) findViewById(R.id.act_select_city_at));
        mTextViewShow = (TextView)findViewById(R.id.act_select_city_tv_show);
    }

    @Override
    protected void initOperate() {

    }

    @Override
    protected void initData() {

        //定义数组（此处也可以直接使用定义一个字符串数组的方式）
         province = getResources().getStringArray(R.array.city);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout. simple_dropdown_item_1line,province);
        autoText.setAdapter(arrayAdapter);
    }
}

package com.xiaoguang.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoguang.bean.City;
import com.xiaoguang.bean.Item;
import com.xiaoguang.bean.ItemWeather;
import com.xiaoguang.miniweather.R;

import java.util.List;

/**
 * 处理添加天气界面的适配器（以后优化时将放在一个里面写）
 * Created by 11655 on 2016/8/29.
 */
public class MyAdapter2 extends BaseAdapter {
    private Context context;
    private List<Item>  stringList ;
    private int itemLayoutID;

    public MyAdapter2(Context context, List<Item> stringList , int itemLayoutID) {
        this.context = context;
        this.stringList  = stringList ;
        this.itemLayoutID = itemLayoutID;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int i) {
        return stringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view ==null){
            //根据不同的item 布局ID 显示不同的效果
            view = LayoutInflater.from(context).inflate(itemLayoutID,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewTime = (TextView)view.findViewById(R.id.list_item_city_tv_datetime);
            viewHolder.textViewCity = (TextView)view.findViewById(R.id.list_item_city_tv_city);
            viewHolder.textViewT = (TextView)view.findViewById(R.id.list_item_city_tv_t);
            view.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) view.getTag());
        }
        //设置数据
        viewHolder.textViewTime.setText(stringList.get(i).getDateTime());
        viewHolder.textViewCity.setText(stringList.get(i).getCity());
        viewHolder.textViewT.setText(stringList.get(i).getT());
        return view;
    }
    class ViewHolder{
        TextView textViewTime,textViewCity,textViewT;
    }
}

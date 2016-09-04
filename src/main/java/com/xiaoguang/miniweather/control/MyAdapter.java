package com.xiaoguang.miniweather.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.model.ItemWeather;

import java.util.List;

/**
 * 适配器
 * Created by 11655 on 2016/8/29.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<ItemWeather> itemWeathers ;
    private int itemLayoutID;

    public MyAdapter(Context context, List<ItemWeather> itemWeathers,int itemLayoutID) {
        this.context = context;
        this.itemWeathers = itemWeathers;
        this.itemLayoutID = itemLayoutID;
    }

    @Override
    public int getCount() {
        return itemWeathers.size();
    }

    @Override
    public Object getItem(int i) {
        return itemWeathers.get(i);
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
            viewHolder.textViewWeek = ((TextView) view.findViewById(R.id.list_item_tv_week));
            viewHolder.textViewHightT = (TextView) view.findViewById(R.id.list_item_tv_ht);
//            viewHolder.imageViewIcon = (ImageView) view.findViewById(R.id.list_item_iv_weather);
            viewHolder.textViewWeather = (TextView)view.findViewById(R.id.list_item_tv_weather);
            view.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) view.getTag());
        }

        //设置数据
        viewHolder.textViewWeek.setText(itemWeathers.get(i).getItemWeek());
        viewHolder.textViewHightT .setText(itemWeathers.get(i).getHightTemperature());;
        viewHolder.textViewWeather.setText(itemWeathers.get(i).getWeather());
        return view;
    }
    class ViewHolder{
        TextView textViewWeek,textViewHightT,textViewWeather;
    }
}

package com.xiaoguang.miniweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoguang.miniweather.R;
import com.xiaoguang.miniweather.activity.WeatherActivity;
import com.xiaoguang.miniweather.model.Item;
import com.xiaoguang.miniweather.utils.LogUtil;

import java.util.List;

/**
 * 城市列表的Adapter
 * Created by 11655 on 2016/9/7.
 */
public class CityListAdapter extends BaseAdapter {
    /**
     * 保存的文件名
     */
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    private Context context;
    private List<Item> itemsList ;
    private int itemLayoutID;

    public CityListAdapter(Context context, List<Item> itemsList , int itemLayoutID) {
        this.context = context;
        this.itemsList  = itemsList ;
        this.itemLayoutID = itemLayoutID;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view ==null){
            //根据不同的item 布局ID 显示不同的效果
            view = LayoutInflater.from(context).inflate(itemLayoutID,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewProvince = (TextView) view.findViewById(R.id.list_item_city_tv_province);
            viewHolder.textViewCity = (TextView)view.findViewById(R.id.list_item_city_tv_city);
            viewHolder.textViewT = (TextView)view.findViewById(R.id.list_item_city_tv_t);
            viewHolder.relativeLayout = (RelativeLayout) view.findViewById(R.id.delete_button);
            viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.list_item_ln_1);
            view.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) view.getTag());
        }
        //设置数据
        viewHolder.textViewProvince.setText(itemsList.get(i).getProvince());
        viewHolder.textViewCity.setText(itemsList.get(i).getCity());
        viewHolder.textViewT.setText(itemsList.get(i).getT());
        //为每个设置长按事件
        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bundle bundle = new Bundle();
                //将省份和城市信息存放到Bundle中
                bundle.putString("province",viewHolder.textViewProvince.getText().toString());
                bundle.putString("city",viewHolder.textViewCity.getText().toString());
                //跳转到显示天气的界面
                Intent intent = new Intent(context, WeatherActivity.class);
                intent.putExtra("info",bundle);
                intent.putExtra("isFromChooseAreaAtivity",true);
                saveDataFile(viewHolder);
                context.startActivity(intent);
                return false;
            }
        });
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemsList.remove(i);
                removeFromFiles(i);
                CityListAdapter.this.notifyDataSetChanged();
            }
        });
        return view;
    }

    /**
     *     将选择的天气覆盖到文件中
     */
    private void saveDataFile(ViewHolder viewHolder) {
        SharedPreferences preferences = context.getSharedPreferences(SHAREDPREFERENCES_NAME, context.MODE_PRIVATE);
        //获取编辑器对象
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstRun", false);
        editor.putString("province",viewHolder.textViewProvince.getText().toString());
        editor.putString("city",viewHolder.textViewCity.getText().toString());
        editor.commit();
    }

    /**
     * 将删除的天气从文件中移除
     * @param i
     */
    private void removeFromFiles(int i) {
        SharedPreferences preferences = context.getSharedPreferences("citys_pref", context.MODE_APPEND);
        //获取文件中的城市列表的个数
        int count = preferences.getInt("count",1);
        LogUtil.i("myTag","我的读取的数据为"+count);
        //获取编辑器对象
        SharedPreferences.Editor editor = preferences.edit();
        //将count值-1覆盖到文件中
       editor.putInt("count",(count-1));
        editor.commit();
        //从文件中移除省份和城市新
        editor.remove("province"+(i++));
        editor.remove("city"+(i++));
        editor.commit();
    }
    class ViewHolder{
        TextView textViewProvince,textViewCity,textViewT;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;
    }
}

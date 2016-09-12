package com.xiaoguang.miniweather.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.xiaoguang.miniweather.db.MiniWeatherDB;
import com.xiaoguang.miniweather.model.City;
import com.xiaoguang.miniweather.model.Future;
import com.xiaoguang.miniweather.model.Province;
import com.xiaoguang.miniweather.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 功能：解析和处理服务器返回的数据的工具类
 * Created by 11655 on 2016/9/2.
 */
public class Utility {

    private static ProgressDialog progressDialog;

    /**
     *功能: 解析和处理服务器返回的省市数据
     */
    public synchronized static boolean handleProvincesResponse(MiniWeatherDB miniWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            //声明一个Map集合用于存放省市信息
            Map<String, List<String>> citysMap = new HashMap<String, List<String>>();
            //声明一个String类型的集合用于存放城市的信息
            List<String> cityList;
            //获取JsonObject对象
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                String msg = object.getString("msg");
                //判断服务器发来的信息是否为正确数据,首先判断请求码和msg 是否成功
                if ("success".equals(msg)) {
                    Log.i("myTag", "请求码返回为成功，可以获取到相关的情况");
                    JSONArray jsonArray = object.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //实例化数组用于存放城市信息
                        cityList = new ArrayList<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //获取省份信息
                        String provinceStr = jsonObject.getString("province");
                        Province province = new Province();
                        province.setId(i+1);
                        province.setProvinceName(provinceStr);
                        // 将解析出来的数据存储到 Province 表中
                        miniWeatherDB.saveProvince(province);
                        //获取城市信息
                        JSONArray jsonArrayCitys = jsonObject.getJSONArray("city");
                        for (int j = 0; j < jsonArrayCitys.length(); j++) {
                            JSONObject jsonObjectCity = jsonArrayCitys.getJSONObject(j);
                            String cityStr = jsonObjectCity.getString("city");
                            City city = new City();
                            city.setCityName(cityStr);
                            city.setProvinceId(province.getId());
                            //将城市信息放入City表中
                            miniWeatherDB.saveCity(city);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 功能:解析和处理服务器返回的省市数据
     * @param response
     * @return
     */
    public synchronized static void handleWeatherResponse(Context context,String response){
        Weather weather = null;
        //声明一个数组用于存放未来的天气数据
        List<Future> futureList = new ArrayList<Future>();
        try {
            JSONObject object = new JSONObject(response);  //获取JsonObject对象
            String msg = object.getString("msg");
            if ("success".equals(msg)) {  //判断服务器发来的信息是否为正确数据,首先判断请求码和msg 是否成功
                JSONArray jsonArray = object.getJSONArray("result");
                weather = new Weather(); // 实例化Weather 对象
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                //将解析出来的数据放入weather对象中
                weather.setColdIndex(jsonObject.getString("coldIndex"));
                weather.setWind(jsonObject.getString("wind"));
                weather.setUpdateTime(jsonObject.getString("updateTime"));
                weather.setDistrct(jsonObject.getString("distrct"));
                weather.setDressingIndex(jsonObject.getString("dressingIndex"));
                weather.setAirCondition(jsonObject.getString("airCondition"));
                weather.setDate(jsonObject.getString("date"));
                weather.setTemperature(jsonObject.getString("temperature"));
                weather.setCity(jsonObject.getString("city"));
                weather.setWashIndex(jsonObject.getString("washIndex"));
                weather.setTime(jsonObject.getString("time"));
                weather.setPollutionIdex(jsonObject.getString("pollutionIndex"));
                weather.setHumidity(jsonObject.getString("humidity"));
                weather.setSunset(jsonObject.getString("sunset"));

                //解析未来的天气
                JSONArray jsonArrayFuture = jsonObject.getJSONArray("future");
                for (int i = 0; i < jsonArrayFuture.length(); i++) {
                    Future future = new Future();
                    future.setWind(jsonArrayFuture.getJSONObject(i).getString("wind"));
                    future.setNight(jsonArrayFuture.getJSONObject(i).getString("night"));
                    future.setDate(jsonArrayFuture.getJSONObject(i).getString("date"));
                    future.setWeek(jsonArrayFuture.getJSONObject(i).getString("week"));
                    future.setTemperature(jsonArrayFuture.getJSONObject(i).getString("temperature"));
                    if (i != 0) {//存在第一条数据不存在这个值的情况,进行判断
                        future.setDayTime(jsonArrayFuture.getJSONObject(i).getString("dayTime"));
                    }
                    futureList.add(future);
                }
                //设置未来的天气
                weather.setFutureList(futureList);
                //暂时打印数据
                LogUtil.i("myTag", "解析出来的天气数据为" + weather);
                //将天气数据存储到本地
                saveWeatherInfo(context,weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：将服务器所返回的数据存储到本地本地文件中
     * @param context
     * @param weather
     */
    public static  void saveWeatherInfo(Context context,Weather weather){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        //获取编辑器对象
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        //将天气信息存储到文件中
        editor.putString("weather",weather.getFutureList().get(0).getNight());
        editor.putString("temp",weather.getTemperature());
        editor.putString("update_time",weather.getTime());
        editor.putInt("count",weather.getFutureList().size());
        for (int i=1;i<weather.getFutureList().size();i++){
            String week = weather.getFutureList().get(i).getWeek();
            String night = weather.getFutureList().get(i).getNight();
            String temps = weather.getFutureList().get(i).getTemperature();
            editor.putString("week"+i,week);
            editor.putString("night"+i,night);
            editor.putString("temps"+i,temps);
        }
        //提交数据
        editor.commit();
    }


    /**
     * 功能：获取今天的星期
     * @return  星期几,（）
     */
    public static String getTodayWeek(){
        final Calendar c = Calendar.getInstance();
        //获取当前的星期
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return "星期"+mWay;
    }

    /**
     * 功能：显示进度条对话框
     */
    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**
     * 功能：关闭进度对话框
     */
    public static void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

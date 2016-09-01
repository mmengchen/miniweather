package com.xiaoguang.miniweather.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xiaoguang.miniweather.model.Future;
import com.xiaoguang.miniweather.model.Location;
import com.xiaoguang.miniweather.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json解析的工具类
 * 暂未判断没有网络的情况
 * Created by 11655 on 2016/8/31.
 */
public class MyUtils {

    //定义城市的请求码
    public static  final int REQUSEST_CITYS = 1;

    //定义天气的请求码
    public static  final int REQUSEST_WEATTER = 2;

    //定义一个final 的String 类型请求码
    private static final  String REQUEST_URL = "http://apicloud.mob.com/v1/weather/";

    //定义一个申请的Mob的应用API 的Key
    private static final String APP_KEY = "1686180062336";

    //定义一个字符串用于拼接URL
    private static String requestUrlStr ="";

    //定义一个Location类用于存放位置信息
    public static Location location;
    //定义一个URl对象
    private URL url;

    /**
     /**
     * 从网络上获取Json字符串并且解析成为bean对象
     * 1.获取城市的json字符串
     * 2.获取天气的json 字符串
     *  请求天气Url：http://apicloud.mob.com/v1/weather/query?key=1686180062336&city=北京&province=北京";
     *  请求城市信息Url:http://apicloud.mob.com/v1/weather/citys?key=1686180062336
     * @param handler
     * @param requestCode 请求码： REQUSEST_CITYS 为请求城市列表信息  REQUSEST_WEATTER 为请求天气数据
     * @param city 城市  当请求码为1 时，可以传入空值
     * @param province 省份  当请求码为1 时，可以传入空值
     * @return
     */
    public static void getInfo(final Handler handler, final int  requestCode, String city, String province){
        //判断参数个数，1个参数为申请城市列表 2个参数为获取天气信息
        if (requestCode==REQUSEST_CITYS){
            requestUrlStr = REQUEST_URL+"citys?key="+APP_KEY;
        }else if (requestCode==REQUSEST_WEATTER){
            try {
                //将城市和省份信息进行转码，转换为URL编码
                String urlCityStr = URLEncoder.encode(city,"UTF-8");
                String urlProvince = URLEncoder.encode(province,"UTF-8");
                requestUrlStr = REQUEST_URL+"query?key="+APP_KEY+"&city="+urlCityStr+"&province="+urlProvince;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            //如果传入非法数据，则直接返回
            return;
        }
        Log.i("myTag","我正在请求的URL："+requestUrlStr);
        //创建一个StringBuffer对象，用于接收服务器发来的Json字符串
         final StringBuffer stringBuffer = new StringBuffer();
        //开启一个线程，请求网络服务，获取Json字符串
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建URL对象
                    URL url = new URL(requestUrlStr);
                    //获取URLConnection对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //打开连接
                    connection.connect();
                    //获取输入流对象
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    //通过循环读取每行的内容
                    String inputLine =null;
                    while((inputLine = buffer.readLine())!=null){
                        stringBuffer.append(inputLine);
                    }
                    //断开连接
                    connection.disconnect();
                    Log.i("myTag",stringBuffer.toString());
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }

                //对服务器发来的Json数据进行解析
                jsonParse(handler,requestCode,stringBuffer.toString());

            }
        }).start();
    }

    /**
     * 解析Json 字符串
     * @param handler
     * @param requestCode  请求码请求码： REQUSEST_CITYS 为解析城市列表信息  REQUSEST_WEATTER 为解析天气数据
     * @param jsonStr   用于被解析的Json字符串
     */
    public static void jsonParse(Handler handler,int requestCode,String jsonStr){
        //声明一个weather对象
        Weather weather = null;
        //声明一个future对象
        Future future = null;
        //声明一个数组用于存放未来的天气数据
        List<Future> futureList = new ArrayList<Future>();
        //声明一个Map集合用于存放省市信息
        Map<String,List<String>> citysMap = new HashMap<String, List<String>>();
        //声明一个String类型的集合用于存放城市的信息
        List<String> cityList;
        try {
            //获取JsonObject对象
            JSONObject object = new JSONObject(jsonStr);
            String msg = object.getString("msg");
            //判断服务器发来的信息是否为正确数据,首先判断请求码和msg 是否成功
            if ("success".equals(msg)){
                Log.i("myTag","请求码返回为成功，可以获取到相关的情况");
                JSONArray jsonArray = object.getJSONArray("result");
                //根据请求码的不同获取不同的参数信息
                if (requestCode==REQUSEST_CITYS){//解析城市列表
                    for (int i= 0;i<jsonArray.length();i++){
                        //实例化数组用于存放城市信息
                        cityList = new ArrayList<>();
                        JSONObject jsonObject  = jsonArray.getJSONObject(i);
                        //获取省份信息
                        String province = jsonObject.getString("province");
                        Log.i("myTag","解析出的省份信息为"+province);
                        //获取城市信息
                        JSONArray jsonArrayCitys  = jsonObject.getJSONArray("city");
                        for (int j= 0;j<jsonArrayCitys.length();j++){
                            JSONObject jsonObjectCity = jsonArrayCitys.getJSONObject(j);
                            String city = jsonObjectCity.getString("city");
                            Log.i("myTag","解析出的城市为"+city);
                            cityList.add(city);
                        }
                        //将省份和对应城市的信息放入Map中
                        citysMap.put(province,cityList);
                        //暂时打印数据
                        Log.i("myTag","我的城市信息为"+province);
                    }
                }else if (requestCode==REQUSEST_WEATTER){//解析天气信息

                    //实例化Weather 对象
                    weather = new Weather();
                    JSONObject jsonObject  = jsonArray.getJSONObject(0);

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
                    for (int i=0;i<jsonArrayFuture.length();i++){
                        future = new Future();
                        future.setWind(jsonArrayFuture.getJSONObject(i).getString("wind"));
                        future.setNight(jsonArrayFuture.getJSONObject(i).getString("night"));
                        future.setDate(jsonArrayFuture.getJSONObject(i).getString("date"));
                        future.setWeek(jsonArrayFuture.getJSONObject(i).getString("week"));
                        future.setTemperature(jsonArrayFuture.getJSONObject(i).getString("temperature"));
                        if(i!=0){
                            future.setDayTime(jsonArrayFuture.getJSONObject(i).getString("dayTime")); //存在第一条数据不存在这个值的情况
                        }
                        futureList.add(future);
                    }
                    //设置未来的天气
                    weather.setFutureList(futureList);
                    //暂时打印数据
                    Log.i("myTag","请求的天气数据为"+weather);
                    //将天气数据发送到主线程中
                    Message message = new Message();
                    message.what = 2;
                    message.obj = weather;
                    handler.sendMessage(message);
                } else {
                    return;
                }
            }else{
                Log.i("myTag","请求码返回为错误，存在相关问题 retCode为："+object.getString("retCode"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置定位的信息
     */
    public static void setLocationInfo(Location locationInfo){
        location = locationInfo;
        Log.i("myTag","我是在工具类中设置的"+location.toString());
    }
    /**
     * 获取定位的信息
     */
    public static Location getLocationInfo(){
        return location;
    }
}

package com.xiaoguang.control;

import android.util.Log;

import com.xiaoguang.bean.Weather;

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

/**
 * 控制层<br/>
 * 相关方法的集合
 * Created by 11655 on 2016/8/28.
 */
public class Controls {


    //    private final String URL = "http://apicloud.mob.com/v1/weather/query?key=appkey&city=通州&province=北京";
    private final String REQUEST_URL = "http://apicloud.mob.com/v1/weather/query?key=";
    private final String APP_KEY = "1686180062336&city=";
    /*
    单例设计模式，保持程序只有一个本类对象的存在
    * */
    //构造方法私有化
    private Controls(){}

    //维护一个本类对象
    private static Controls controls;

    //对外提供一个访问方法
    public static Controls getControlsInstance(){
        if (controls == null){
            controls = new Controls();
        }
        return controls;
    }

    /**
     * 功能：将普通的中文汉字的UTF-8编码格式转换成为URL编码格式
     * */
    public String [] UrlEnCode(String province,String city){
        //定义一个数组用于返回数据
        String [] urlStrs= new String [2];
        String provinceStr = "";
        String cityStr = "";
        try {
            provinceStr=new String(java.net.URLEncoder.encode(province,"utf-8").getBytes());
            cityStr=new String(java.net.URLEncoder.encode(city,"utf-8").getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlStrs[0] = provinceStr;
        urlStrs[1] = cityStr;
        return urlStrs;
    }

    /**
     * 功能：将URl的编码格式转换成为普通中文的UTF-8编码格式
     * */
    public String [] UrlDeCode(String [] urlStrs){
        String [] str= new String [2];
        String provinceStr = "";
        String cityStr = "";
        try {
            //将URl编码格式转换成ISO-8859-1
            provinceStr = new String(java.net.URLEncoder.encode(urlStrs[0], "UTF-8").getBytes(), "ISO-8859-1");
            //URL解码
            provinceStr = java.net.URLDecoder.decode(new String(provinceStr.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");

            //将URl编码格式转换成ISO-8859-1
            cityStr = new String(java.net.URLEncoder.encode(urlStrs[1], "UTF-8").getBytes(), "ISO-8859-1");
            //URL解码
            cityStr = java.net.URLDecoder.decode(new String(cityStr.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");

            //将数据放到数组中，用于返回相关数据
            str[0] = provinceStr;
            str[1] = cityStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
    /**
     * 功能：向服务器发送请求，获取返回天气的Json字符串
     * @param province
     * @param city
     * @return jsonStr
     * */
    public String getJsonFromClient(String province,String city){
        //将普通中文编码转换成Url编码
        String strs[] = UrlEnCode(province, city);
        final String PROVINCE  = strs[0];
        final String CITY = strs[1]+"&province=";
        //REQUEST_URL_PARH  拼接请求字符串（ requestURL 测试正常）
        final String REQUEST_URL_PARH = REQUEST_URL+APP_KEY+CITY+PROVINCE;
        Log.i("myTag","我正在请求的URL："+REQUEST_URL_PARH);
        String jsonWeather = "";
        try {
            //创建URL对象
            URL url = new URL(REQUEST_URL_PARH);
            //获取URLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //打开连接
            connection.connect();
            //获取输入流对象
            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //通过循环读取每行的内容
            String inputLine =null;
            while((inputLine = buffer.readLine())!=null){
                jsonWeather+=inputLine;
            }
            //断开连接
            connection.disconnect();
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return jsonWeather;
    }
    /**
     * 功能：将Json字符串转换成为Object对象
     * @param jsonStr
     * @return o
     */
    public Object JsonToObject(String jsonStr){
        Object o = new Object();
        Log.i("myTag","我被调用了，我正在执行解析json的操作");

        //可能会出现在主线程中访问网络的问题
//        String str = getJsonFromClient("北京","北京");
        //开启一个线程，获取网络中的天气的Json字符串,暂时注释,进行解析操作
       /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str  = getJsonFromClient("北京","北京");
                Log.i("myTag","我在测试请求的天气数据："+str);
            }
        }).start();
        */
//        String jsonStr = "";

        //获取Jon的第一个字段
//        msg":"success 和 retCode":"200"
        //判读mes 的状态，如果是成功则进行解析操作
        String str = "{\"msg\":\"success\",\"result\":[{\"airCondition\":\"良\",\"city\":\"葫芦岛\",\"coldIndex\":\"低发期\",\"date\":\"2016-08-27\",\"distrct\":\"葫芦岛\",\"dressingIndex\":\"单衣类\",\"exerciseIndex\":\"适宜\",\"future\":[{\"date\":\"2016-08-27\",\"night\":\"晴\",\"temperature\":\"14°C\",\"week\":\"今天\",\"wind\":\"北风 3～4级\"},{\"date\":\"2016-08-28\",\"dayTime\":\"晴\",\"night\":\"晴\",\"temperature\":\"29°C / 14°C\",\"week\":\"星期日\",\"wind\":\"北风 3～4级\"},{\"date\":\"2016-08-29\",\"dayTime\":\"晴\",\"night\":\"晴\",\"temperature\":\"29°C / 15°C\",\"week\":\"星期一\",\"wind\":\"北风 4～5级\"},{\"date\":\"2016-08-30\",\"dayTime\":\"晴\",\"night\":\"阵雨\",\"temperature\":\"26°C / 14°C\",\"week\":\"星期二\",\"wind\":\"北风 4～5级\"},{\"date\":\"2016-08-31\",\"dayTime\":\"阵雨\",\"night\":\"阵雨\",\"temperature\":\"27°C / 15°C\",\"week\":\"星期三\",\"wind\":\"西北风 4～5级\"},{\"date\":\"2016-09-01\",\"dayTime\":\"阵雨\",\"night\":\"晴\",\"temperature\":\"28°C / 16°C\",\"week\":\"星期四\",\"wind\":\"南风 3～4级\"},{\"date\":\"2016-09-02\",\"dayTime\":\"晴\",\"night\":\"晴\",\"temperature\":\"27°C / 17°C\",\"week\":\"星期五\",\"wind\":\"东南风 3～4级\"},{\"date\":\"2016-09-03\",\"dayTime\":\"阵雨\",\"night\":\"零散阵雨\",\"temperature\":\"27°C / 18°C\",\"week\":\"星期六\",\"wind\":\"东北偏东风 2级\"},{\"date\":\"2016-09-04\",\"dayTime\":\"阵雨\",\"night\":\"局部多云\",\"temperature\":\"27°C / 18°C\",\"week\":\"星期日\",\"wind\":\"东北风 2级\"},{\"date\":\"2016-09-05\",\"dayTime\":\"局部多云\",\"night\":\"局部多云\",\"temperature\":\"27°C / 19°C\",\"week\":\"星期一\",\"wind\":\"东北偏北风 2级\"}],\"humidity\":\"湿度：52%\",\"pollutionIndex\":\"59\",\"province\":\"辽宁\",\"sunrise\":\"05:19\",\"sunset\":\"18:37\",\"temperature\":\"25℃\",\"time\":\"18:10\",\"updateTime\":\"20160827183422\",\"washIndex\":\"不太适宜\",\"weather\":\"多云\",\"week\":\"周六\",\"wind\":\"南风2级\"}],\"retCode\":\"200\"}";
        try {
            JSONObject jsonObject = new JSONObject(str);//方法返回的是一个对象数组
            Log.i("myTag",jsonObject.toString());
//            JSONObject weatherObject = jsonObject.getJSONObject("result");
//            Log.i("myTag","空气质量："+weatherObject.getString("airCondition"));
            //创建一个天气对象用于存放数据
            Weather weather = null;
            //解析对象数组
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //打印获取的数组
            Log.i("myTag","数组为："+jsonArray.toString());
            //遍历数组获取数据
            for (int i=0;i<jsonArray.length();i++){
                weather = new Weather();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                //将解析出来的数据放入weather对象中
                weather.setColdIndex(jsonObject2.getString("coldIndex"));
                weather.setWind(jsonObject2.getString("wind"));
                weather.setUpdateTime(jsonObject2.getString("updateTime"));
                weather.setDistrct(jsonObject2.getString("distrct"));
                weather.setDressingIndex(jsonObject2.getString("dressingIndex"));
                weather.setAirCondition(jsonObject2.getString("airCondition"));
                weather.setDate(jsonObject2.getString("date"));
                weather.setTemperature(jsonObject2.getString("temperature"));
                weather.setCity(jsonObject2.getString("city"));
                weather.setWashIndex(jsonObject2.getString("washIndex"));
                weather.setTime(jsonObject2.getString("time"));
                weather.setPollutionIdex(jsonObject2.getString("pollutionIndex"));
                weather.setHumidity(jsonObject2.getString("humidity"));
                weather.setSunset(jsonObject2.getString("sunset"));
                //未来的天气暂不设置
//                weather.setFutures(jsonObject2.getJSONArray("future"));

                Log.i("myTag","您解析出来的天气数据为："+weather.toString());//没有问题
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return o;
    }

//    public void()

    /**
     * 获取城市列表的json
     * @return
     */
    public String getCitysJson(){
        String [] province = new String [34];
        String [] citys = new String[1024];
        //请求城市列表的Url
        final String REQUEST_URL_PARH ="http://apicloud.mob.com/v1/weather/citys?key=1686180062336";
        Log.i("myTag","我正在请求的URL："+REQUEST_URL_PARH);
        String jsonWeather = "";
        try {
            //创建URL对象
            URL url = new URL(REQUEST_URL_PARH);
            //获取URLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //打开连接
            connection.connect();
            //获取输入流对象
            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //通过循环读取每行的内容
            String inputLine =null;
            while((inputLine = buffer.readLine())!=null){
                jsonWeather+=inputLine;
            }
            //断开连接
            connection.disconnect();


            //进行解析数据
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return jsonWeather;
    }
}

package com.xiaoguang.miniweather.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求的工具类
 * Created by 11655 on 2016/9/2.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    //创建URL对象
                    URL url = new URL(address);
                    //获取URLConnection对象
                    connection = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    connection.setRequestMethod("GET");
                    //设置连接超时时间为8秒
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //打开连接
                    connection.connect();
                    //获取输入流
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    //循环读取内容
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) { // 回调方法onFinish();
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) { // 回调 onError()  方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        //关闭连接
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}

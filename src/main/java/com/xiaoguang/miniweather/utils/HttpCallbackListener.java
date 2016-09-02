package com.xiaoguang.miniweather.utils;

/**
 * 回调网络请求返回的结果
 * Created by 11655 on 2016/9/2.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}

package com.xiaoguang.miniweather.utils;

/**
 * 回调网络请求返回的结果
 * Created by 11655 on 2016/9/2.
 */
public interface HttpCallbackListener {
    /**
     * 成功的回调方法
     * @param response
     */
    void onFinish(String response);

    /**
     * 失败的回调方法
     * @param e
     */
    void onError(Exception e);
}

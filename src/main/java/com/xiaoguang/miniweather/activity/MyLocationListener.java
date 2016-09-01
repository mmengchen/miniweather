package com.xiaoguang.miniweather.activity;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xiaoguang.miniweather.model.Location;
import com.xiaoguang.miniweather.utils.MyUtils;

/**
 * BDLocationListener接口有1个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
 * Created by 11655 on 2016/8/30.
 */
public class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {
        //声明一个Bean对象用于存放城市和省份信息
        Location mylocation = new Location();
        //Receive Location
        StringBuffer sb = new StringBuffer(256);

        //声明一个Location对象用于获取位置信息
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果

            sb.append("\n城市为");
            sb.append(location.getCity());
            sb.append("\n省份为为");
            sb.append(location.getProvince());
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

            //如果不存在省份,则将省份信息设置为城市
            if (location.getProvince().equals("")||location==null){
                mylocation.setProvince(location.getCity());
            }else {
                mylocation.setProvince(location.getProvince());
            }
            mylocation.setCity(location.getCity());

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            //运营商信息
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
            sb.append("\ndescribe : ");
            sb.append("网络定位成功");

            //如果不存在省份,则将省份信息设置为城市,设置属性值
            if (location.getProvince().equals("")||location==null){
                mylocation.setProvince(location.getCity());
            }else {
                mylocation.setProvince(location.getProvince());
            }
            mylocation.setCity(location.getCity());

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
            //如果不存在省份,则将省份信息设置为城市
            if (location.getProvince().equals("")||location==null){
                mylocation.setProvince(location.getCity());
            }else {
                mylocation.setProvince(location.getProvince());
            }
            mylocation.setCity(location.getCity());
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            mylocation.setProvince("");
            mylocation.setCity("暂时无法获取位置");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
            mylocation.setProvince("");
            mylocation.setCity("暂时无法获取位置");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            mylocation.setProvince("");
            mylocation.setCity("暂时无法获取位置");
        }
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());// 位置语义化信息
        Log.i("BaiduLocationApiDem", sb.toString());

      //将值设置到工具类中，用于UI调用
        MyUtils.setLocationInfo(mylocation);
        //
        Log.i("myTag","我在监听事件中获取的"+mylocation.toString());
    }
    /*
    返回值
61 ： GPS定位结果，GPS定位成功。
62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
65 ： 定位缓存的结果。
66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
161： 网络定位结果，网络定位定位成功。
162： 请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件。
167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
502： key参数错误，请按照说明文档重新申请KEY。
505： key不存在或者非法，请按照说明文档重新申请KEY。
601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
602： key mcode不匹配，您的ak配置过程中安全码设置有问题，
    请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
501～700：key验证失败，请按照说明文档重新申请KEY。

    * */
}

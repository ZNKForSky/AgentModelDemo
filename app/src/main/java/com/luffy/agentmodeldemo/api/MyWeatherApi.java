package com.luffy.agentmodeldemo.api;

import com.luffy.agentmodeldemo.retrofit.Field;
import com.luffy.agentmodeldemo.retrofit.GET;
import com.luffy.agentmodeldemo.retrofit.POST;
import com.luffy.agentmodeldemo.retrofit.Query;

import okhttp3.ResponseBody;
import okhttp3.Call;


/**
 * 作者：<a href="https://blog.csdn.net/qq_35101450">张宁科CSDN主页</a><p>
 * 创建时间：2020/11/24 13:58<p>
 * 描述：我的天气接口
 */
public interface MyWeatherApi {
    @POST("/v3/weather/weatherInfo")
    Call postWeather(@Field("city") String city, @Field("key") String key);


    @GET("/v3/weather/weatherInfo")
    Call getWeather(@Query("city") String city, @Query("key") String key);
}

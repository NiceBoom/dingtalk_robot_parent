package com.robot.msg.service;

import com.robot.enity.Result;

public interface WeatherService {
    /**
     * 获取当天天气
     * @param city_id 城市id
     */
    Result getTodayWeather(String city_id);

    /**
     * 获取明天天气
     * @param city_id 城市id
     * @return
     */
    Result getTomorrowWeather(String city_id);

    /**
     * 获取当前天气
     * @param city_id
     * @return
     */
    Result getNowWeather(String city_id);

    /**
     * 刷新全部天气情况
     * @param city_id
     * @return
     */
    Result refreshAllWeather(String city_id);


}

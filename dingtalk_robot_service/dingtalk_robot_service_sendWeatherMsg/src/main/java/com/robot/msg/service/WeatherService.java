package com.robot.msg.service;

import com.robot.enity.Result;
import com.robot.msg.service.dto.GetTomorrowWeatherInputDto;
import com.robot.msg.service.dto.GetTomorrowWeatherOutputDto;

import java.util.Map;




public interface WeatherService {

    public class NotFoundError extends Exception {

    }

    GetTomorrowWeatherOutputDto getTomorrowWeather(GetTomorrowWeatherInputDto inputDto) throws NotFoundError;

    /**
     * 获取当天天气
     * @param city_id 城市id
     */
    Map<String, String> getTodayWeather(String city_id);

    /**
     * 获取明天天气
     * @param city_id 城市id
     * @return
     */
    Map<String, String> getTomorrowWeather(String city_id);

    /**
     * 获取当前天气
     * @param city_id
     * @return
     */
    Map<String, String> getNowWeather(String city_id);

    /**
     * 刷新全部天气情况
     * @param city_id
     * @return
     */
    Map<String, String> refreshAllWeather(String city_id);


}

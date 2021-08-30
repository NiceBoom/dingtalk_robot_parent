package com.robot.msg.service;

import com.robot.enity.Result;

import java.util.Map;

public interface MsgService {

    /**
     * 发送天气消息
     * @param city_id 城市id
     * @param weatherData 查询的天气结果
     * @param weatherCode 日期代码
     * @return
     */
    Map<String, String> sendWeatherMsg(String city_id, Map<String, String> weatherData, Integer weatherCode);

}

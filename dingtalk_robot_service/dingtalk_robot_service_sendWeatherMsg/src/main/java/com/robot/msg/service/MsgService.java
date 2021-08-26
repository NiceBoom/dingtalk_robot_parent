package com.robot.msg.service;

import com.robot.enity.Result;

public interface MsgService {

    /**
     * 发送天气消息
     * @param city_id 城市id
     * @param weatherResult 查询的天气结果
     * @param weatherCode 日期代码
     * @return
     */
    Result sendWeatherMsg(String city_id, Result weatherResult, Integer weatherCode);

}

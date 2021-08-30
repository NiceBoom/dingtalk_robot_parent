package com.robot.msg.controller;

import com.robot.enity.Result;
import com.robot.msg.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/weather")
public class WeatherController {

    final static String CITY_KEY = "101010100";//北京城市码

    @Autowired
    private WeatherService weatherService;

    //更新天气数据
    //每天4点0、9点0、13点0、19点更新天气缓存
    @Scheduled(cron = "0 0 4,9,13,19 1/1 * ? ")
    void refreshAllWeather(){
        Map<String, String> result = weatherService.refreshAllWeather(WeatherController.CITY_KEY);
    }

    //缓存天气数据

}

package com.robot.msg.controller;

import com.robot.enity.Result;
import com.robot.msg.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/weather")
public class WeatherController {

    final static String CITY_KEY = "101010100";//北京城市码

    @Autowired
    private WeatherService weatherService;

    //更新天气数据
    //每天4点30、9点30、13点30、19点30更新天气缓存
    @Scheduled(cron = "0 30 4,9,13,19 1/1 * ? ")
    void refreshAllWeather(){
        Result result = weatherService.refreshAllWeather(WeatherController.CITY_KEY);
        System.out.println(result.isFlag());

    }
}

package com.robot.msg.controller;

import com.robot.enity.Result;
import com.robot.enity.StatusCode;
import com.robot.msg.service.MsgService;
import com.robot.msg.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/msg")
public class MsgController {

    final static String CITYKEY = "101010100";//北京城市码

    @Autowired
    private MsgService msgService;

    @Autowired
    private WeatherService weatherService;

    //每天下午五点半发送明天天气提醒
    @Scheduled(cron = "0 30 17 1/1 * ?")
    void everyDaySendMsg(){

        //查询现在天气情况，返回Result结果
        Result nowWeather = weatherService.getNowWeather(MsgController.CITYKEY);
        //发送当前天气消息
        Result result1 = msgService.sendWeatherMsg(MsgController.CITYKEY, nowWeather, StatusCode.NOW_WEATHER);

        //查询明天天气情况，返回Result结果
        Result tomorrowWeather = weatherService.getTomorrowWeather(MsgController.CITYKEY);
        //发送明天天气消息
        Result result = msgService.sendWeatherMsg(MsgController.CITYKEY, tomorrowWeather, StatusCode.TOMORROW_WEATHER);
        System.out.println("返回消息:" + result.getData().toString() + "当前天气查询结果：" + result1.getCode());
    }

    //每天早上八点半发送当前天气提醒
    @Scheduled(cron = "0 30 8 * * ?")
    void sendTime830NowWeather(){

        //查询现在天气情况，返回Result结果
        Result nowWeather = weatherService.getNowWeather(MsgController.CITYKEY);
        //发送当前天气消息
        Result result1 = msgService.sendWeatherMsg(MsgController.CITYKEY, nowWeather, StatusCode.NOW_WEATHER);

        //查询今天天气
        Result todayWeather = weatherService.getTodayWeather(MsgController.CITYKEY);
        //发送今天天气消息
        Result result2 = msgService.sendWeatherMsg(MsgController.CITYKEY, todayWeather, StatusCode.TODAY_WEATHER);
    }

    //每天早上九点发送当前天气提醒
    @Scheduled(cron = "0 0 9 * * ?")
    void sendTime900NowWeather(){
        //查询现在天气情况，返回Result结果
        Result tomorrowWeather = weatherService.getNowWeather(MsgController.CITYKEY);
        //发送当前天气消息
        Result result = msgService.sendWeatherMsg(MsgController.CITYKEY, tomorrowWeather, StatusCode.NOW_WEATHER);
        System.out.println("返回消息:" + result.getData().toString());

        //查询今天天气
        Result todayWeather = weatherService.getTodayWeather(MsgController.CITYKEY);
        //发送今天天气消息
        Result result2 = msgService.sendWeatherMsg(MsgController.CITYKEY, todayWeather, StatusCode.TODAY_WEATHER);
    }

    //发送今天天气消息
    //@GetMapping("/sendTodayWeatherMsg")
    @Scheduled(cron = "0/3 * * * * ?")
    void sendTodayWeatherMsg(){
        //查询今天天气
        Result todayWeather = weatherService.getTodayWeather(MsgController.CITYKEY);
        //发送今天天气消息
        Result result = msgService.sendWeatherMsg(MsgController.CITYKEY, todayWeather, StatusCode.TODAY_WEATHER);
        System.out.println("返回消息:" + result.getData().toString() );
    }

    //手动发送明天天气消息
    @GetMapping("/sendTomorrowWeatherMsg")
//    @Scheduled(cron = "0/5 * * * * ?")//用来测试系统的定时任务，完成后注释掉
    Result sendTomorrowWeatherMsg(){
        //查询明天天气情况，返回Result结果
        Result tomorrowWeather = weatherService.getTomorrowWeather(MsgController.CITYKEY);
        //发送明天天气消息
        Result result = msgService.sendWeatherMsg(MsgController.CITYKEY, tomorrowWeather, StatusCode.TOMORROW_WEATHER);
        System.out.println("返回消息:" + result.getData().toString() );
        return result;
    }

    //TODO 发送将来N天天气
}

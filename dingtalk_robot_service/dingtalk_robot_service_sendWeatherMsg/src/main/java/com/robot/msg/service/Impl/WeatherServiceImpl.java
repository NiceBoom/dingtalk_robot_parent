package com.robot.msg.service.Impl;

import com.alibaba.fastjson.JSON;
import com.robot.enity.*;
import com.robot.msg.service.WeatherService;
import com.robot.msg.service.dto.GetTomorrowWeatherInputDto;
import com.robot.msg.service.dto.GetTomorrowWeatherOutputDto;
import com.robot.util.JedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeatherServiceImpl implements WeatherService {
    //请求URL
    final static String SOJSON_WEATHER_URL =
            "http://t.weather.itboy.net/api/weather/city/";

    /**
     * 刷新天气情况
     * @param city_id 城市id
     * @return
     */
    @Override
    public Map<String, String> refreshAllWeather(String city_id){
        //拼接请求URL
        String cityUrl = WeatherServiceImpl.SOJSON_WEATHER_URL + city_id;

        try{
            //发送get请求并以String返回查询结果
            RestTemplate restTemplate = new RestTemplate();
            String weatherResult = restTemplate.getForObject(cityUrl, String.class);

            //TODO 保存刷新数据(存到mysql或者redis)
            //获取当天时间yyyyMMddHH
            String timeStamp = new SimpleDateFormat("yyyyMMddHH").format(Calendar.getInstance().getTime());
            System.out.println(timeStamp );

            Jedis jedisResource = JedisUtils.getResource();
            jedisResource.set(timeStamp, weatherResult);

            System.out.println(weatherResult.toString());
            return new Result(true, StatusCode.OK, "获取成功", weatherResult);
        }catch(Exception e){
            System.out.println(e);
            return new Result(false, StatusCode.ERROR, "获取失败");
        }
    }

    @Override
    public GetTomorrowWeatherOutputDto getTomorrowWeather(GetTomorrowWeatherInputDto inputDto) throws NotFoundError {

        if ((new Random()).nextBoolean()) {
            throw new WeatherService.NotFoundError();
        }
        return null;
    }

    /**
     * 获取当日天气
     * @param city_id 城市id
     * @return
     */
    @Override
    public Map<String, String> getTodayWeather(String city_id){
        return getWeather(city_id, StatusCode.TODAY_WEATHER);
    }

    /**
     * 获取明日天气
     * @param city_id
     * @return
     */
    @Override
    public Map<String, String> getTomorrowWeather(String city_id){
        return getWeather(city_id, StatusCode.TOMORROW_WEATHER);
    }

    /**
     * 获取当前天气
     * @param city_id
     * @return
     */
    @Override
    public Map<String, String> getNowWeather(String city_id){
        return getWeather(city_id, StatusCode.NOW_WEATHER);
    }

    /**
     * 根据日期获取天气
     * @param city_id 城市id
     * @param weatherCode 日期代码
     * @return
     */
    public Map<String, String> getWeather(String city_id, Integer weatherCode){
        //拼接请求URL
        String cityUrl = WeatherServiceImpl.SOJSON_WEATHER_URL + city_id;
        //错误结果
        Map<String, String> errorResultMap = new HashMap<>();

        try{
            RestTemplate restTemplate = new RestTemplate();
            //获得查询的所有天气String数据
            String weatherJsonstr = restTemplate.getForObject(cityUrl, String.class);
            System.out.println("这里是查询后返回的天气数据:" + weatherJsonstr);
            //将String对象转换为DTO对象
            WeatherResult weatherResult = JSON.parseObject(weatherJsonstr,WeatherResult.class);
            System.out.println("转换后的数据结果："+weatherResult);
            //获取天气数据列表
            List<JsonData> data1 = weatherResult.getData();
            //获取第二层嵌套数据
            JsonData jsonData = data1.get(0);

            // 获取当前天气具体情况
            String qualityNow = jsonData.getQuality();//当前空气质量
            String pm25Now = jsonData.getPm25();//当前PM25浓度
            String shiduNow = jsonData.getShidu();//当前空气湿度
            String wenduNow = jsonData.getWendu();//当前温度
            String noticeNow = jsonData.getGanmao();//提示
            //将当前天气封装到map中
            Map<String, String> nowWeatherMap = new HashMap<>();
            nowWeatherMap.put("qualityNow", qualityNow);
            nowWeatherMap.put("pm25Now", pm25Now);
            nowWeatherMap.put("shiduNow", shiduNow);
            nowWeatherMap.put("wenduNow", wenduNow);
            nowWeatherMap.put("noticeNow", noticeNow);
            //获取第三层嵌套列表
            List<Forecast> forecast = jsonData.getForecast();
            System.out.println("forecast=" + forecast);


            //获取当天天气详细
            Forecast todayWeather = forecast.get(0);
            Map<String, String> todayWeatherMap = new HashMap<>();
            String highToday = todayWeather.getHigh();//最高气温
            String lowToday = todayWeather.getLow();//最低气温
            String typeToday = todayWeather.getType();//天气情况
            String noticeToday = todayWeather.getNotice();//出行建议
            todayWeatherMap.put("highToday", highToday);
            todayWeatherMap.put("lowToday", lowToday);
            todayWeatherMap.put("typeToday", typeToday);
            todayWeatherMap.put("noticeToday", noticeToday);

            //获取明天天气详细
            Forecast tomorrowWeather = forecast.get(1);
            Map<String, String> tomorrowWeatherMap = new HashMap<>();
            String highTomorrow = tomorrowWeather.getHigh();//最高气温
            String lowTomorrow = tomorrowWeather.getLow();//最低气温
            String typeTomorrow = tomorrowWeather.getType();//天气情况
            String noticeTomorrow = tomorrowWeather.getNotice();//出行建议
            tomorrowWeatherMap.put("highTomorrow",highTomorrow);
            tomorrowWeatherMap.put("lowTomorrow",lowTomorrow);
            tomorrowWeatherMap.put("typeTomorrow",typeTomorrow);
            tomorrowWeatherMap.put("noticeTomorrow",noticeTomorrow);

            if(weatherCode == StatusCode.TODAY_WEATHER)
                //返回当天天气
                return todayWeatherMap;
            if(weatherCode == StatusCode.TOMORROW_WEATHER)
                //返回明天天气
                return tomorrowWeatherMap;
            if(weatherCode == StatusCode.NOW_WEATHER)
                //返回当前天气
                return nowWeatherMap;

            return errorResultMap;
        }catch(Exception e){
            System.out.println(e);
            return errorResultMap;
        }
        // TODO 从数据库获取天气

        // TODO 从缓存获取天气

    }
}

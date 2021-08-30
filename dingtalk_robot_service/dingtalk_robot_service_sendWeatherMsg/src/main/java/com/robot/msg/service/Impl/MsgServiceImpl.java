package com.robot.msg.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.robot.enity.Result;
import com.robot.enity.StatusCode;
import com.robot.msg.service.MsgService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 定时发消息测试
 */
@Service
public class MsgServiceImpl implements MsgService {

    final static String DINGDINGTALK_ROBOT_URL =
            "https://oapi.dingtalk.com/robot/send?";
    final static String DINGDINGTALK_ROBOT_URL_TOKEN =
            "access_token=ffaabe93a835ff732b8053c0cd54c1e8315a8f906ddc0cc722dad5e833ff281c";

    @Override
    public Map<String, String> sendWeatherMsg(String city_id, Map<String, String> weatherData, Integer weatherCode){

        //初始化变量
        String requestBody = "";

        //初始化返回值
        Map resultMap = new HashMap();

        //使用RestTemplate发送http请求
        RestTemplate restTemplate = new RestTemplate();
        //请求路径
        String url = MsgServiceImpl.DINGDINGTALK_ROBOT_URL +
                MsgServiceImpl.DINGDINGTALK_ROBOT_URL_TOKEN;

        //今天天气消息模板
        if(weatherCode == StatusCode.TODAY_WEATHER) {
            //今天天气消息请求体
            requestBody =
                    "{\n" +
                            "    \"at\": {\n" +
                            "        \"isAtAll\": " + StatusCode.NOTATALL + "\n" +
                            "    },\n" +
                            "    \"text\": {\n" +
                            "        \"content\":\"[提醒]" + "北京今天天气" + weatherData.get("typeToday")
                            +"，"+weatherData.get("highToday")+"，"+ weatherData.get("lowToday") + "。" + weatherData.get("noticeToday")+ "\"\n" +
                            "    },\n" +
                            "    \"msgtype\":\"text\"\n" +
                            "}";
        }

        //明天天气消息模板
        if(weatherCode == StatusCode.TOMORROW_WEATHER) {
            //明天天气消息请求体
            requestBody =
                    "{\n" +
                            "    \"at\": {\n" +
                            "        \"isAtAll\": " + StatusCode.NOTATALL + "\n" +
                            "    },\n" +
                            "    \"text\": {\n" +
                            "        \"content\":\"[提醒]" + "北京明天天气" + weatherData.get("typeTomorrow")
                            +"，"+weatherData.get("highTomorrow")+"，"+ weatherData.get("lowTomorrow") + "。" + weatherData.get("noticeTomorrow")+ "\"\n" +
                            "    },\n" +
                            "    \"msgtype\":\"text\"\n" +
                            "}";
        }
        //现在天气消息模板
        if(weatherCode == StatusCode.NOW_WEATHER){
            //现在天气消息请求模板
            requestBody =
                    "{\n" +
                            "    \"at\": {\n" +
                            "        \"isAtAll\": " + StatusCode.NOTATALL + "\n" +
                            "    },\n" +
                            "    \"text\": {\n" +
                            "        \"content\":\"[提醒]" + "北京当前空气质量" + weatherData.get("qualityNow")
                            +"，PM2.5浓度为"+weatherData.get("pm25Now")+"，当前湿度为"+ weatherData.get("shiduNow") + "，室外温度为"
                            + weatherData.get("wenduNow")+"度。"+weatherData.get("noticeNow")+"。"+ "\"\n" +
                            "    },\n" +
                            "    \"msgtype\":\"text\"\n" +
                            "}";
        }
        //json对象
        JSONObject jsonObject = new JSONObject();
        //设置请求头为APPLICATION_JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);//Content-Type

        //请求体，包括请求body与请求头
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        try{
            //发送请求，以String接收返回的数据
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            //解析返回数据
            JSONObject jsTemp = JSONObject.parseObject(responseEntity.getBody());
            System.out.println("这里是发送信息请求返回的消息"+jsTemp.toString());
            //TODO 提问返回值是什么类型，放什么参数
            resultMap.put("true", jsTemp.toString());
            return resultMap;
        }catch (Exception e){
            System.out.println(e);
            return resultMap;
        }
    }
}

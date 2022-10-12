package com.firespread.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.firespread.service.FireBoundryService;
import com.firespread.service.WeatherPointService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @description: 读取外部数据
 * @author: DelucaWu
 * @createDate: 2022/8/2 11:29
 */
@Component
public class loadwinddata {

    public String loadwinddatafd(String zh){
        String windspeeddir="";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String template="风速风向的外部请求接口（可以用和风天气代替）";
        String bodys = template.replace("\"", "%22")
                .replace("{", "%7b").replace("}", "%7d");
        HttpGet httpGet = new HttpGet(bodys);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonString= EntityUtils.toString(entity,"UTF-8");
                JSONObject jsonObject= JSONObject.parseObject(jsonString);
                String message=jsonObject.getString("b");
                JSONObject index= JSONObject.parseObject(message);
                String detail= index.getString("grid_forecast_new");
                JSONObject index2= JSONObject.parseObject(detail);
                String detail2= index2.getString("today");
                JSONArray index4=JSONObject.parseArray(detail2);
                for (int i=0;i<6;i++){
                    long windspeed = Math.round(Double.parseDouble(index4.getJSONObject(i).get("windspeed").toString()));
                    String winddirls = index4.getJSONObject(i).get("winddir").toString();
                    String winddir = towinddir(winddirls);
                    windspeeddir=windspeeddir+windspeed+","+winddir+",";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return windspeeddir;
    }

    public String loadwinddatasn(String zh){
        System.out.println(zh);
        String windspeeddir="";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String template="同上";
        String bodys = template.replace("\"", "%22")
                .replace("{", "%7b").replace("}", "%7d");
        HttpGet httpGet = new HttpGet(bodys);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonString= EntityUtils.toString(entity,"UTF-8");
                JSONObject jsonObject= JSONObject.parseObject(jsonString);
                String message=jsonObject.getString("b");
                JSONObject index= JSONObject.parseObject(message);
                String detail= index.getString("grid_forecast_new");
                JSONObject index2= JSONObject.parseObject(detail);
                String detail2= index2.getString("today");
                JSONArray index4=JSONObject.parseArray(detail2);
                for (int i=0;i<6;i++){
                    long windspeed = Math.round(Double.parseDouble(index4.getJSONObject(i).get("windspeed").toString()));
                    String winddirls = index4.getJSONObject(i).get("winddir").toString();
                    String winddir = towinddir(winddirls);
                    windspeeddir=windspeeddir+windspeed+","+winddir+",";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return windspeeddir;
    }

    public String towinddir(String windname){
        String winddirname="";
        switch (windname){
            case "东南":
                winddirname="135";
                break;
            case "南":
                winddirname="180";
                break;
            case "西南":
                winddirname="225";
                break;
            case "西":
                winddirname="270";
                break;
            case "西北":
                winddirname="315";
                break;
            case "北":
                winddirname="0";
                break;
            case "东北":
                winddirname="45";
                break;
            case "东":
                winddirname="90";
                break;
        }
        return winddirname;
    }
}

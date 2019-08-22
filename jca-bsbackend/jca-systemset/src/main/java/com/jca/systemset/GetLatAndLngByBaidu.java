package com.jca.systemset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 获取经纬度
 *C0a9eecc5b366bde412021d4d7e5d8c4
 * @author jueyue 返回格式：Map map map.put("status",
 * reader.nextString());//状态 map.put("result", list);//查询结果
 * list>
 * 密钥:C0a9eecc5b366bde41202sljdflsjfsldjf   需要自己去申请
 * Created by hellcat on 2016/7/27.
 */
@Slf4j
public class GetLatAndLngByBaidu {

    public static Map getLngAndLat(String address){
        Map map=new HashMap();
        String url = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=A1LU7iHS0avqQwPLAxbhKn0UYSQCuRVH";
        try {
            String json = loadJSON(url);
            JSONObject obj = JSONObject.parseObject(json);
            System.out.println(json);
            if(obj.get("status").toString().equals("0")){
                double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
                double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
                map.put("lng", getDecimal(lng));
                map.put("lat", getDecimal(lat));
                //System.out.println("经度："+lng+"---纬度："+lat);
            }else{
                log.debug("未找到相匹配的经纬度！");
                //System.out.println("未找到相匹配的经纬度！");
            }
        }catch (Exception e){
        	e.printStackTrace();
            log.error("未找到相匹配的经纬度，请检查地址");
        }
        return map;
    }

    public static double getDecimal(double num) {
        if (Double.isNaN(num)) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num;
    }

    public static String loadJSON (String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(),"UTF-8"));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }
}
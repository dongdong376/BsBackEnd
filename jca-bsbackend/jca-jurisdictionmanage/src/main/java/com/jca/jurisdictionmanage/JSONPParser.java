package com.jca.jurisdictionmanage;
import java.util.Map;  
  
import com.alibaba.fastjson.JSON;  
  
@SuppressWarnings("rawtypes")
public class JSONPParser {  
      
    
	public static Map parseJSONP(String jsonp){  
          
        int startIndex = jsonp.indexOf("(");  
        int endIndex = jsonp.lastIndexOf(")");  
        String json = jsonp.substring(startIndex+1, endIndex);  
        System.out.println(json);  
        return JSON.parseObject(json);  
    }        
  
}  
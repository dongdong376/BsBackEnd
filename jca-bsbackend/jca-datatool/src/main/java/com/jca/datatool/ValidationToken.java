package com.jca.datatool;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jca.databeans.pojo.TFOperator;

/**
 * 
 * @author Administrator
 *
 */
public class ValidationToken {

    private Logger logger = Logger.getLogger(ValidationToken.class);

    private RedisAPI redisAPI;

    public RedisAPI getRedisAPI() {
        return redisAPI;
    }
    public void setRedisAPI(RedisAPI redisAPI) {
        this.redisAPI = redisAPI;
    }
    public TFOperator getCurrentUser(String tokenString){
        //根据token从redis中获取用户信息
			/*
			 test token:
			 key : token:1qaz2wsx
			 value : {"id":"100078","userCode":"myusercode","userPassword":"78ujsdlkfjoiiewe98r3ejrf","userType":"1","flatID":"10008989"}

			*/
    	TFOperator User = null;
        if(null == tokenString || "".equals(tokenString)){
            return null;
        }
        try{
            String userInfoJson = redisAPI.get(tokenString);
            User = JSONObject.parseObject(userInfoJson,TFOperator.class);
        }catch(Exception e){
            User = null;
            logger.error("get userinfo from redis but is error : " + e.getMessage());
        }
        return User;
    }

}

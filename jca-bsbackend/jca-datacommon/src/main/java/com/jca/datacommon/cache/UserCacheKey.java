package com.jca.datacommon.cache;

/**
 * 用户缓存key
 * @author Administrator
 *
 */
public class UserCacheKey {
	
	/**
	 * 到期时间
	 */
    public static final Integer EXPIRE_TIME = 30;
    
    /**
     * 所有权限key
     */
    public static final String ALL_PERMISSION_KEY = "permissions";

    /**
     * 用户id
     */
    public static final String USER_ID_KEY = "user:token:${token}:id";
    /**
     *  用户权限
     */
    public static final String USER_PERMISSION_KEY = "user:id:${id}:permissions";
}

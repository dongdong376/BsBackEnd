package com.jca.datatool;

import java.util.UUID;

/**
 * uuid工具类
 * @author Administrator
 *
 */
public class UUIDUtils {
	/**
	 * 固定UUID
	 * @return
	 */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 指定位数UUID
     * @param len
     * @return
     */
    public static String generateUUID(Integer len) {
        String uuid=UUID.randomUUID().toString();
        String newUuid=uuid.replace("-", "");
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < len; i++) {
			sb.append(newUuid.charAt(i));
		}
        return sb.toString();
    }
}

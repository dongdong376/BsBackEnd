package com.jca.datatool;

/**
 * 
 * @author Administrator
 *
 */
public class BeanUtils extends com.jca.datacommon.tool.BeanUtils {


    public static<T> T copyProperties(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T target;
        try {
            target = targetClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return target;
    }
}

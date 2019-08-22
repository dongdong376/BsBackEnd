package com.jca.datacommon.freemarker;

import java.util.List;

/**
 * 表结构
 * @author Administrator
 *
 */
public class MyClass {
    //类名
    private String className;
    //字段的集合
    private List<Field> fieldList;
 
    public String getClassName() {
        return className;
    }
 
    public void setClassName(String className) {
        this.className = className;
    }
 
    public List<Field> getFieldList() {
        return fieldList;
    }
 
    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }
}
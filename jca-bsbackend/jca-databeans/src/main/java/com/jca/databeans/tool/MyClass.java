package com.jca.databeans.tool;

import java.util.List;

import lombok.Data;

/**
 * 表结构
 * @author Administrator
 *
 */
@Data
public class MyClass {
    //类名
    private String className;
    //表名
    private String tableName;
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
package com.jca.databeans.tool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 类构造器
 * @author Administrator
 *
 */
@Slf4j
public class ClassGenerator {
 
    public DatabaseMetaData init(){
        DatabaseMetaData databaseMetaData = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://127.0.0.1:3306/jcafaceone";
            String user = "root";
            String password = "123456";
            Connection connection = DriverManager.getConnection(url, user, password);
            databaseMetaData = connection.getMetaData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return databaseMetaData;
    }
 
    /**
     * 生产类
     * @param tableName
     * @return
     */
    public MyClass generateClass(String tableName){
        MyClass myClass = new MyClass();
        List<Field> fieldList = new ArrayList<Field>();
        DatabaseMetaData databaseMetaData = init();
        try{
            ResultSet rs = databaseMetaData.getColumns(null, "%", tableName, "%");
            while(rs.next()){
                //列名
                String columnName = rs.getString("COLUMN_NAME");
                //类型
                String typeName = rs.getString("TYPE_NAME");
                System.out.println("typeName:" + typeName);
                //注释
                String remarks = rs.getString("REMARKS");
                if(remarks.equals("预留")) {
                	continue;
                }
                Field field = new Field();
                field.setDataBaseFieldName(columnName);
                field.setFieldName(lowerFirstLetter(columnName));
                field.setFieldType(columnTypeToFieldType(typeName));
                field.setFieldRemarks(remarks);
                field.setFieldNameUpperFirstLetter(upperFirstLetter(columnName));
                fieldList.add(field);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        myClass.setTableName(tableName);
        myClass.setClassName(tableNameToClassName(tableName));
        myClass.setFieldList(fieldList);
 
        return myClass;
    }
    
    public Map<String, MyClass> generatorMyclassList(){
    	Map<String, MyClass> map=new HashMap<>();
    	DatabaseMetaData databaseMetaData = init();
        try{
            ResultSet rs = databaseMetaData.getTables(null, null, "%", new String [] {"TABLE"});
            while(rs.next()){
                //表名
                String tableName = rs.getString("TABLE_NAME");
                //生成类
                MyClass myClass=this.generateClass(tableName);              
                map.put(myClass.getClassName(), myClass);              
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }    
        return map;
    }
 
    //字段类型转换成属性类型
    public String columnTypeToFieldType(String columnType){
        String fieldType = null;
        switch (columnType) {
            case "INT":
                fieldType = "Integer";
                break;
            case "VARCHAR":
                fieldType = "String";
                break;
            case "CHAR":
                fieldType = "String";
                break;
            case "DATETIME":
                fieldType = "String";
                break;
            case "BIGINT":
                fieldType = "Long";
                break;
            default:
                fieldType = "String";
                break;
        }
        return fieldType;
    }
 
    /**
     * 首字母大写
     * @param src
     * @return
     */
    public String upperFirstLetter(String src){
        String firstLetter = src.substring(0, 1).toUpperCase();
        String otherLetters = src.substring(1);
        return firstLetter + otherLetters;
    }
    
    public String lowerFirstLetter(String src){
        String firstLetter = src.substring(0, 1).toLowerCase();
        String otherLetters = src.substring(1);
        return firstLetter + otherLetters;
    }
 
    /**
     * 表名转类名
     * @param tableName
     * @return
     */
    public String tableNameToClassName(String tableName){
        StringBuilder className = new StringBuilder();
        //aa_bb_cc  AaBbCc
        String[] split = tableName.split("_");
        for (String item : split) {
            className.append(upperFirstLetter(item));
        }
        return className.toString();
    }
    
    /**
     * 处理字段
     * @param fieldName
     * @return
     */
    public String dealFieledName(String fieldName) {
    	StringBuilder sb=new StringBuilder();
    	String[] split = fieldName.split("_");
    	for (int i = 0; i < split.length; i++) {
			if(i!=0) {
				sb.append(upperFirstLetter(split[i]));
			}else {
				sb.append(split[i]);
			}
		}
    	return sb.toString();
    }
}
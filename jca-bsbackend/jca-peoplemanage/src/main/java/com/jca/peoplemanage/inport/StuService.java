package com.jca.peoplemanage.inport;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jca.databeans.pojo.TFEmployInfo;

import jxl.Sheet;
import jxl.Workbook;

/**
 * @author Javen
 * @Email zyw205@gmail.com
 * 
 */
public class StuService {
    /**
     * 查询stu表中所有的数据
     * @return 
     */
    public static List<TFEmployInfo> getAllByDb(){
        List<TFEmployInfo> list=new ArrayList<TFEmployInfo>();
        try {
            DBhepler db=new DBhepler();
            String sql="select * from t_f_employ_info";
            ResultSet rs= db.Search(sql, null);
            while (rs.next()) {
                int id=rs.getInt("employ_id");
                String name=rs.getString("employ_name");
                Integer sex=rs.getInt("sex");
                String num=rs.getString("card_no");              
                //System.out.println(id+" "+name+" "+sex+ " "+num);
                list.add(TFEmployInfo.builder().employId(id).employName(name).sex(sex).cardNo(num).build());
            }
            
        } catch (SQLException e) {
          
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * 查询指定目录中电子表格中所有的数据
     * @param file 文件完整路径
     * @return
     */
    public static List<StuEntity> getAllByExcel(String file){
        List<StuEntity> list=new ArrayList<StuEntity>();
        try {
            Workbook rwb=Workbook.getWorkbook(new File(file));
            Sheet rs=rwb.getSheet("Test Shee 1");//或者rwb.getSheet(0)
            int clos=rs.getColumns();//得到所有的列
            int rows=rs.getRows();//得到所有的行
            
            System.out.println(clos+" rows:"+rows);
            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < clos; j++) {
                    //第一个是列数，第二个是行数
                    String id=rs.getCell(j++, i).getContents();//默认最左边编号也算一列 所以这里得j++
                    String name=rs.getCell(j++, i).getContents();
                    String sex=rs.getCell(j++, i).getContents();
                    String num=rs.getCell(j++, i).getContents();
                    
                    System.out.println("id:"+id+" name:"+name+" sex:"+sex+" num:"+num);
                    list.add(new StuEntity(Integer.parseInt(id), name, sex, Integer.parseInt(num)));
                }
            }
        } catch (Exception e) {
          
            e.printStackTrace();
        } 
        return list;
        
    }
    
    /**
     * 通过Id判断是否存在
     * @param id
     * @return
     */
    public static boolean isExist(int id){
        try {
            DBhepler db=new DBhepler();
            ResultSet rs=db.Search("select * from t_f_employ_info where employ_id=?", new String[]{id+""});
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void main(String[] args) {
        /*List<StuEntity> all=getAllByDb();
        for (StuEntity stuEntity : all) {
            System.out.println(stuEntity.toString());
        }*/
        
        System.out.println(isExist(20));
        
    }
    
}
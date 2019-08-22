package com.jca.databeans.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jca.datacommon.annotation.NoColumn;
import com.jca.datacommon.annotation.Primary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 
 * @author:
 * @date: 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TFEventRecord implements Serializable {

        //记录ID
		@Primary
        private Integer eventRecordId;
		@NoColumn
		private Integer index;
		@NoColumn
		private Date tempDate;
        //时间
        private String recordDateTime;
        //密钥
        private String secretKey;
        //人员NO
        private String employNo;
        //姓名
        private String employName;
        //卡号
        private String cardNo;
        private Integer recordStates;
        //身份证
        private String idCardNo;
        //联系方式
        private String telephone;
        //性别（0：男；1：女）
        private String sex;
        //民族
        private String nation;
        //籍贯
        private String birthPlace;
        //机构名称
        private String organName;
        //房号
        private String roomNo;
        //部门名称
        private String departmentName;
        //设备NO(就是设备序列号)
        private String faceNo;
        //设备名称
        private String faceName;
        //区域名称
        private String areaName;
        //物业名称
        private String propertyName;
        //位置
        private String location;
        //地址
        private String address;
        //出入口（1:进, 2:出）
        private Integer faceInOut;
        @NoColumn
        private String inOutType;
        //记录类型（验证方式）
        private String recordType;
        //
        private String recordAll;
        //
        private String jpgPath;
        @NoColumn
        private Integer count;
        @NoColumn
        private TFDepartment department;
        @NoColumn
        private TFDevice tFDevice;
        @NoColumn
        private TFArea tFArea;
        @NoColumn
        private TFEmployInfo tFEmployInfo;
        @NoColumn
        private String startDate;
        @NoColumn
        private String endDate;
}


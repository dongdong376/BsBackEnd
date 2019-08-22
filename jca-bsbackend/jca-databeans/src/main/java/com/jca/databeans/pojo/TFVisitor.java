package com.jca.databeans.pojo;

import java.io.Serializable;
import java.util.Date;

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
public class TFVisitor implements Serializable {

        //自增列
		@Primary
        private Integer visitorId;
        //访问名称
        private String visitorName;
        //访问缘由
        private String visitorReason;
        //被访问人
        private Integer peopleByVisitingId;   
        private String propertyNo;
        //检查人
        private String checkNo;
        //检查日期
        private Date checkDate;
        //检查状态
        private String checkState;
        //访客电话
        private String visotorTelephone;
        //访客身份证
        private String visitorCardNo;
        //开始日期
        private Date beginDate;
        //截止日期
        private Date endDate;    
        //被拜访人
        @NoColumn
        private TFEmployInfo employInfo;  
        //检查人
        @NoColumn
        private TFEmployInfo checkEmpInfo;   

}


package com.jca.databeans.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
public class TFEmployInfo implements Serializable {

        //人员ID
		@Primary
        private Integer employId;
        //人员NO
        private String employNo;
        //姓名
        private String employName;
        //机构名称
        private String organName;
        @NoColumn
        private Integer index;
        //房号
        private String roomNo;
        //部门NO
        private String departmentNo;
        @NoColumn
        private String eTempNo;
        @NoColumn
        private String departmentName;
        //所属物业
        private String propertyNo;
        @NoColumn
        private String propertyName;
        //区域NO
        private String areaNo;
        //密钥
        private String secretKey;
        //卡号
        private String cardNo;
        //身份证
        private String idCardNo;
        //性别（0：男；1：女）
        private Integer sex;
        @NoColumn
        private String sexCN;
        //相片
        private String photo;
        //相片状态（0：未上传；1：已上传）
        private String photoState;
        //联系方式
        private String telephone;
        //民族
        private String nation;
        //籍贯
        private String birthPlace;
        //起始日期
        private String beginDatetime;
        @NoColumn
        private Date tempBeginDatetime;
        @NoColumn
        private Date tempEndDatetime;
        //截止日期
        private String endDatetime;
        //是否防潜回（0：否；1：是）
        private String isSubmarineBack;
        //是否在场（0：出场；1：在场）
        private Integer isPresent=0;
        @NoColumn
        private Integer num;
        //
        private Integer isDown;
        //创建人
        private String createBy;
        //创建时间       
        private String createTime;
        //更新人
        private String updateBy;
        //更新时间
        private String updateTime;

}


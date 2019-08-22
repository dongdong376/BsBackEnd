package com.jca.databeans.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
public class TFArea implements Serializable {

        //区域ID
		@Primary
        private Integer areaId;
        //区域NO
        private String areaNo;
        @NoColumn
        private String tempAreaNo;
        @NoColumn
        private Integer index;
        //区域名称
        private String areaName;
        //父区域NO
        private String areaParentNo="0";
        //所在物业
        private String propertyNo;
        //城市
        private String city;
        //区域属性
        private String attributes;
        //详细地址
        private String address;
        //省份
        private String province;
        //区或镇
        private String district;
        //密钥
        private String secretKey;
        //
        private String isDown;
        //创建人
        private String createBy;
        //创建时间
        private String createTime;
        //更新人
        private String updateBy;
        //更新时间
        private String updateTime;
        @NoColumn
        private Integer outSum;
        @NoColumn
        private Integer inSum;
        @NoColumn
        private List<TFDevice> devices;
        @NoColumn
        private List<TFEventRecord> records;

}


package com.jca.databeans.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Field;

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
public class TFDevice implements Serializable {

        //设备ID
		@Primary		
        private Integer faceId;
        //设备NO(就是设备序列号)
        private String faceNo;
        @NoColumn
        private Integer index;
        //设备名称
        private String faceName;
        //区域NO
        private String areaNo;
        @NoColumn
        private String areaName;
        @NoColumn
        private String tempDeviceAreaNo;
        //电脑号（0~9）
        private String pcNo;
        //机器设备的SN
        private String faceSn;
        //密钥
        private String secretKey;
        //IP
        private String faceIp;
        //Mac
        private String faceMac;
        //子网掩码
        private String faceNetmark;
        //出入口（1:进, 2:出）
        private String faceInOut;
        //在线（0:在线，1:离线）
        private String isOnline;
        //状态（0:启用, 1:停用, 2:异常）
        private String state;
        //厂商
        private String faceFirm;
        //物业组
        private String propertyNo;
        //平台
        private String facePlatform;
        //系统
        private String faceSystem;
        //版本
        private String faceVersion;
        //备注
        private String remarks;
        //
        private Integer isDown;
        //创建人
        private String createBy;
        //创建时间
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private String createTime;
        //更新人
        private String updateBy;
        //更新时间
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private String updateTime;
        @NoColumn
        private TFArea area;        
        @NoColumn
        private TFProperty property;

}


package com.jca.databeans.vo;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddDeviceVo implements Serializable {
	        //设备NO(就是设备序列号)
	        private String faceNo;
	        //设备名称
	        private String faceName;
	        //区域NO
	        private String areaNo;
	        //电脑号（0~9）
	        private String pcNo;
	        //IP
	        private String faceIp;
	        //物业编号
	        //private String propertyNo;
	        //Mac
	        private String faceMax;
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
}

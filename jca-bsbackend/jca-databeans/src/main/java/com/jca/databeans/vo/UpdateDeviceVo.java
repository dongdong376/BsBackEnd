package com.jca.databeans.vo;

import com.jca.databeans.pojo.TFDevice;
import com.jca.datacommon.annotation.NoColumn;
import com.jca.datacommon.annotation.Primary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeviceVo {
	        //设备NO(就是设备序列号)
	        private String faceNo;
	        //设备名称
	        private String faceName;
	        //区域NO
	        private String areaNo;
	        //电脑号（0~9）
	        private String pcNo;
	        //物业编号
	        private String propertyNo;
	        //出入口（1:进, 2:出）
	        private String faceInOut;
	        //在线（0:在线，1:离线）
	        private String isOnline;
	        //状态（0:启用, 1:停用, 2:异常）
	        private String state;
	        //备注
	        private String remarks;
	        //
	        private Integer isDown;
}

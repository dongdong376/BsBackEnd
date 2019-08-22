package com.jca.databeans.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.jca.databeans.pojo.TFMenu;
import com.jca.databeans.pojo.TFMenu.TFMenuBuilder;
import com.jca.datacommon.annotation.Primary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatorVo implements Serializable {
	//物业ID
	        //物业NO
	        private String propertyNo;
	        //物业名称
	        private String propertyName;
	        //密钥
	        private String secretKey;
	        //联系人
	        private String contacts;
	        //联系方式
	        private String telephone;
	        //地址
	        private String address;
	        //
	        private Integer isDown;
	        //物业组No
	        private String propertyGroupNo;
}

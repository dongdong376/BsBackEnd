package com.jca.databeans.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jca.datacommon.annotation.Primary;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOperatorVo implements Serializable {
	
	private String operatorName;
	private String operatorNo;
	private String password;
	//是否启用
	private String isDown;
	//账号所属角色No
	private String roleNo;
	//权限所属的菜单编号
	private Integer[] menuId;
	//物业组No
	private String propertyGroupNo;
}

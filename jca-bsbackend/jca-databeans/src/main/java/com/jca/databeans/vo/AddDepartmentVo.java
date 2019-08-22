package com.jca.databeans.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="添加部门视图对象")
@NoArgsConstructor
@AllArgsConstructor
public class AddDepartmentVo implements Serializable {

        //部门名称
		@ApiModelProperty(value="部门名称")
        private String departmentName; 
        //房号
		@ApiModelProperty(value="房号")
        private String roomNo;
        //主负责人
		@ApiModelProperty(value="主负责人")
        private String principal;
        //联系电话
		@ApiModelProperty(value="联系电话")
        private String telephone;
        private String parentNo;
}


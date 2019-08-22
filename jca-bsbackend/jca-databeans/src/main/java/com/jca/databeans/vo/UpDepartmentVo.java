package com.jca.databeans.vo;

import java.io.Serializable;

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
public class UpDepartmentVo implements Serializable {
		
		private Integer departmentId;
        //部门名称
        private String departmentName; 
        //房号
        private String roomNo;
        //主负责人
        private String principal;
        //联系电话
        private String telephone;
}


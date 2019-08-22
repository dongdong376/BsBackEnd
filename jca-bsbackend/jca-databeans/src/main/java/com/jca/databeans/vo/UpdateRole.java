package com.jca.databeans.vo;

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
public class UpdateRole implements Serializable {
		
		//角色主键id
		@Primary
		private Integer roleId;
        //名称
        private String roleName;
        //描述
        private String remarks;
        //修改人
        private String updateBy;
        //修改时间
        private String updateTime;
        //菜单编号
        private Integer [] menuId;
}


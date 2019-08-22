package com.jca.databeans.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class TFRole implements Serializable {

        //角色ID
		@Primary
        private Integer roleId;
        //角色NO
        private String roleNo;
        //名称
        private String roleName;
        //描述
        private String remarks;
        //
        private Integer isDown;
        //创建人
        private Integer createBy;
        //创建时间
        private String createTime;
        //更新人
        private Integer updateBy;
        //更新时间
        private String updateTime;
		public TFRole(String roleNo) {
			super();
			this.roleNo = roleNo;
		}
		@NoColumn
		private Integer []menuId;
		//账户数量
		@NoColumn
		private Integer operatorSum;
		@NoColumn
		private List<TFOperator> operators;
        
}


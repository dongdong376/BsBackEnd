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
public class TFRoleMenuResource implements Serializable {

        //主键
		@Primary
        private Integer roleMenuId;
        //
        private String roleNo;
        //
        private Integer menuId;
        //资源类型
        private Integer type;
        //
        private Integer createBy;
        //
        private String createTime;

}


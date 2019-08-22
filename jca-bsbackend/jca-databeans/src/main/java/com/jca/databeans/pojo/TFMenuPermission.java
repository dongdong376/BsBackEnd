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
public class TFMenuPermission implements Serializable {

        //菜单权限ID
		@Primary
        private Integer menuPermissionId;
        //角色NO
        @NoColumn
        private String roleNo;
        //菜单ID
        private Integer menuId;
        //
        private Integer ifDown;
        //权限码
        private String code;
        //操作详细描述
        private String description;
        //是否禁用
        private Integer status;
        //权限组id
        private Integer permissionGroupId;
        //创建人
        private String createBy;
        //创建时间
        private LocalDateTime createTime;
        //更新人
        private String updateBy;
        //更新时间
        private LocalDateTime updateTime;

}


package com.jca.databeans.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.jca.datacommon.annotation.NoColumn;

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
public class TFMenuPermissionGroup implements Serializable {

        //
        private Integer permissionGroupId;
        //
        private String name;
        //
        private String description;
        //
        private String status;
        //
        private LocalDateTime createTime;
        //操作组
        private LocalDateTime updateTime;
        //权限组中的权限列表
        @NoColumn
        private List<TFMenuPermission> tfMenuPermissions;

}


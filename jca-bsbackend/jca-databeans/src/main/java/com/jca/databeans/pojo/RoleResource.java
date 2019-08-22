package com.jca.databeans.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 
 * @version 1.0
 * @date 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResource {

    private Integer id;

    private Integer roleId;

    private Integer resourceId;

    /**
     * 资源类型，1：菜单；2：权限
     */
    private Integer type;

    private LocalDateTime createTime;
}

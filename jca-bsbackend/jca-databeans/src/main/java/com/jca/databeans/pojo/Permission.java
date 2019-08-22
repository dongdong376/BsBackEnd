package com.jca.databeans.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限
 * @author Administrator
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {
    private Integer id;

    private String description;

    private String code;

    private String uriPattern;

    private Integer method;

    private Integer groupId;

    private Integer status;

    public LocalDateTime createTime;

    public LocalDateTime updateTime;
}

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
 * 菜单
 * @author Administrator
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {

    private Integer id;

    private Integer pid;

    private Integer weight;

    private String name;

    private String icon;

    private String path;

    private String code;

    private Integer status ;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @NoColumn
    private List<Menu> children;

}

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
public class TFMenu implements Serializable {

        //
        private Integer id;
        //
        private Integer pid;
        //
        private String code;
        //
        private String name;
        //
        private String icon;
        //路由路径
        private String path;
        
        private String pageUrl;
        //
        private Integer weight;
        //
        private Integer status;
        //
        private String createTime;
        //
        private String updateTime;
        @NoColumn
        private List<TFMenu> tfMenus;

}


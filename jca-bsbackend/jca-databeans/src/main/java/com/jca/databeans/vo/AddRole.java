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
public class AddRole implements Serializable {
        //名称
        private String roleName;
        //描述
        private String remarks;
        //菜单编号
        private Integer [] menuId;
}


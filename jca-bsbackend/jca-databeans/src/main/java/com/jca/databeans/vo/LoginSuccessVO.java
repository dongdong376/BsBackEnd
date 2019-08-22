package com.jca.databeans.vo;

import java.util.List;

import com.jca.databeans.pojo.TFMenu;
import com.jca.databeans.pojo.TFProperty;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginSuccessVO {
    
    private String menuToken;

    private List<String> permissions;

    private List<TFMenu> menus;
}

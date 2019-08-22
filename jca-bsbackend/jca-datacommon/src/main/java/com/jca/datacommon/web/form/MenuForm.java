package com.jca.datacommon.web.form;

import com.jca.datacommon.annotation.NotBlank;
import com.jca.datacommon.annotation.NotNull;
import com.jca.datacommon.annotation.Size;

import lombok.Data;

/**菜单表单
 * @author 
 * @version 1.0
 * @date 
 */
@Data
public class MenuForm {
    private Integer pid;

    @NotBlank
    @Size(min = 2, max = 12)
    private String name;

    private String path;

    private String icon;

    @NotNull
    private Integer weight;
}

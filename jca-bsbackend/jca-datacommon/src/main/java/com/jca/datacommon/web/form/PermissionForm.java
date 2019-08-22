package com.jca.datacommon.web.form;

import com.jca.datacommon.annotation.EnumValue;
import com.jca.datacommon.annotation.NotBlank;
import com.jca.datacommon.annotation.NotNull;
import com.jca.datacommon.enums.MethodEnum;
import com.jca.datacommon.enums.StatusEnum;

import lombok.Data;

/**
 * @author
 * @version 1.0
 * @date 
 */
@Data
public class PermissionForm {

    private Integer groupId;

    /**
     * 字符串非空检验
     */
    @NotBlank
    private String uriPattern;

    @NotBlank
    private String code;

    /**
     * method不能为空且只能是MethodEnum中对应的枚举值value
     */
    @EnumValue(MethodEnum.class)
    @NotNull
    private Integer method;

    @NotBlank
    private String description;

    @EnumValue(StatusEnum.class)
    private Integer status;
}

package com.jca.datacommon.web.form;

import com.jca.datacommon.annotation.NotBlank;

import lombok.Data;

/**
 * 
 * @author Administrator
 *
 */
@Data
public class SmallProcessLoginForm {

    @NotBlank
    private String telephone;

    @NotBlank
    private String password;

}

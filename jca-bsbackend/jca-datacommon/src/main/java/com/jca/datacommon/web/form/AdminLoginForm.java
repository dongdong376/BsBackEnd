package com.jca.datacommon.web.form;

import com.jca.datacommon.annotation.NotBlank;

import lombok.Data;

/**
 * 
 * @author Administrator
 *
 */
@Data
public class AdminLoginForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}

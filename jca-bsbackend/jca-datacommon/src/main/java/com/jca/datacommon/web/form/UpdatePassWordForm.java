package com.jca.datacommon.web.form;

import com.jca.datacommon.annotation.NotBlank;

import lombok.Data;

/**
 * 修改密码
 * @author Administrator
 *
 */
@Data
public class UpdatePassWordForm {
	@NotBlank
	private Integer operatorId;
	@NotBlank
	private String oldPassword;
	@NotBlank
	private String password;
}

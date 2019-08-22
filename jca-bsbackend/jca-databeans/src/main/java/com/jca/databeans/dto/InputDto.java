package com.jca.databeans.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InputDto {

	@ApiModelProperty(value="单一参数传入")
	private String paramString;
	@ApiModelProperty(value="多个参数传入")
	private String[] paramStrings;
}

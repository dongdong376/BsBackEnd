package com.jca.databeans.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddGroupVo {
	private String propertyGroupName;
	private String remarks;
	//与物业关联
	private String propertyIds;
}

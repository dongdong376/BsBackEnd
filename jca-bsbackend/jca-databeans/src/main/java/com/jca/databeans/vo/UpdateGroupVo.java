package com.jca.databeans.vo;

import java.util.List;

import com.jca.databeans.vo.PropertyGroupVo.PropertyGroupVoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGroupVo {
	private String propertyGroupName;
	private String remarks;
	private Integer propertyGroupId;
	private String propertyGroupNo;
	private String propertyIds;
}

package com.jca.databeans.pojo;

import java.time.LocalDateTime;

import com.jca.databeans.pojo.RoleResource.RoleResourceBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempEntity {
	private String faceInOut;
	private Integer yesterdayStrangenessFlux;
	private Integer todayResidentFlux;
	private Integer todayComeFlux; 
	private Integer yesterdayComeFlux; 
	private Integer yesterdayFlux; 
	private Integer todayStrangenessFlux; 
	private Integer todayFlux;
	private Integer yesterdayResidentFlux;
}

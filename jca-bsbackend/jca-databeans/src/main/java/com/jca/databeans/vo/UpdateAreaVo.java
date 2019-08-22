package com.jca.databeans.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.jca.datacommon.annotation.Primary;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 
 * @author:
 * @date: 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAreaVo implements Serializable {
        @Primary   
        @NotNull(message="必有字段")
		private Integer areaId;
        //区域名称
		@ApiModelProperty(value="区域名称")
		@NotBlank(message="区域名不能为空")
        private String areaName; 		
        //城市
		@NotBlank(message="所属城市不能为空")
        private String city;
        //区域属性
		@NotBlank(message="所属区的属性不能为空")
        private String attributes;
		//区或镇
		@NotBlank(message="不能为空")
		private String district;
        //详细地址
		//@NotBlank(message="所属区的属性不能为空")
        private String address;
        //省份
		@NotBlank(message="所属省份不能为空")
        private String province;             

}


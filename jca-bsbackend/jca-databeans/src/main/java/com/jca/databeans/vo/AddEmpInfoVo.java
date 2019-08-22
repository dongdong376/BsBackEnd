package com.jca.databeans.vo;

import java.io.Serializable;

import com.jca.databeans.vo.AddDeviceVo.AddDeviceVoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddEmpInfoVo implements Serializable{
	private String cardNo;
	private String idCardNo;
	private String nation;
	private String telephone;
	private String departmentNo;
	private String beginDatetime;
	private String endDatetime;
	private String employName;
	private Integer sex;
	private String birthPlace;
	private String roomNo;
	private Integer isSubmarineBack;
	private String photo;
}

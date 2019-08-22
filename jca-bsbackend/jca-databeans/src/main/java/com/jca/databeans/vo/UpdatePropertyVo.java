package com.jca.databeans.vo;

import com.jca.datacommon.annotation.Primary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePropertyVo {
	@Primary
	private Integer propertyId;
    //物业名称
    private String propertyName;
    //联系人
    private String contacts;
    //联系方式
    private String telephone;
    //地址
    private String address;
    //省
    private String province;
    //市
    private String city;
    //区或街
    private String district;
}

package com.jca.databeans.vo;

import java.time.LocalDateTime;

import com.jca.databeans.vo.OperatorVo.OperatorVoBuilder;
import com.jca.datacommon.annotation.Primary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPropertyVo {
	
    //物业名称
    private String propertyName;
    //联系人
    private String contacts;
    //联系方式
    private String telephone;
    //详细地址
    private String address;
    //省
    private String province;
    //市
    private String city;
    //区或街
    private String district;
}

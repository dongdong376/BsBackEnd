package com.jca.databeans.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.jca.datacommon.annotation.NoColumn;
import com.jca.datacommon.annotation.Primary;

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
public class TFProperty implements Serializable {

        //物业ID
		@Primary
        private Integer propertyId;
        //物业NO
        private String propertyNo;
        //物业名称
        private String propertyName;
        //密钥
        private String secretKey;
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
        //
        private Integer isDown;
        //物业组No
        private String propertyGroupNo;
        //创建人
        private Integer createBy;
        //创建时间
        private String createTime;
        //更新人
        private Integer updateBy;
        //更新时间
        private String updateTime;
        @NoColumn
        private Integer deviceCount;
        @NoColumn
        private List<TFDevice> devices;

}


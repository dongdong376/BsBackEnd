package com.jca.databeans.vo;

import java.io.Serializable;
import java.util.Date;

import com.jca.databeans.pojo.TFDevice;
import com.jca.databeans.pojo.TFEmployInfo;
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
public class AddDeviceauthorVo implements Serializable {

        //人脸机SN数组
        private String faceSns[];
        //人员NO数组
        private String employNos[];
               
}


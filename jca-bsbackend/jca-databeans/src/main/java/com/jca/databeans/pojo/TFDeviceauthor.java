package com.jca.databeans.pojo;

import java.io.Serializable;
import java.util.Date;

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
public class TFDeviceauthor implements Serializable {

        //授权ID
		@Primary
        private Long authorId;
        //人脸机SN
        private String faceSn;
        //人员NO
        private String employNo;
        //
        private String author;
        //
        private Integer isDownload;
        //0:添加(添加人员授权)；1:更新(人员工号，头像变动)；2：删除(删除人员)；4：已下载人员授权
        private Integer isDown;
        //卡号
        private String cardNo;
        @NoColumn
        private String tempCardNo;
        //关联人员
        @NoColumn
        private TFEmployInfo employInfo;
        //关联设备
        @NoColumn
        private TFDevice device;
        @NoColumn
        private Integer index;
        @NoColumn
        private String auEmployName;
        @NoColumn
        private String auDepartmentName;
        @NoColumn
        private String auFaceName;
        @NoColumn
        private String auAreaName;
        @NoColumn
        private Date auBeginDate;
        @NoColumn
        private Date auEndDate;     
        @NoColumn
        private String auIdCardNo;
}


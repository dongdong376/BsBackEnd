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
public class TFOwnerApply implements Serializable {

        @Primary
        private Integer ownerId;
        //申请人编号
        private Integer ownerVisitorId;
        //申请时间
        private Date ownerApplyDate;
        @NoColumn
        private Integer index;
        @NoColumn
        private String propertyName;
        private String ownerCheckPeople;
        //拒绝缘由
        private String ownerConfuseReason;
        //审核状态(0:未审核,1:已通过,2:已拒绝)
        private String ownerCheckState;
        //审核时间
        private Date ownerCheckDate;
        @NoColumn
        private TFVisitor vAO;
        @NoColumn
        private TFEmployInfo oCAE;
}


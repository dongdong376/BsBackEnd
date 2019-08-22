package com.jca.databeans.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
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
public class TFDepartment implements Serializable {

        //部门ID
		@Primary
        private Integer departmentId;
        //部门NO
        private String departmentNo;
        @NoColumn
        private String dTempNo;
        @NoColumn
        private Integer index;
        //部门名称
        private String departmentName;
        //父NO
        private String parentNo;
        //物业编号
        private String propertyNo;
        //房号
        private String roomNo;
        //联系电话
        private String telephone;
        //主负责人
        private String principal;
        //成员数量
        private Integer memberNum;
        //
        private Integer isDown;
        //创建人
        private String createBy;
        //创建时间
        private String createTime;
        //更新人
        private String updateBy;
        //更新时间
        private String updateTime;
        //子部门
        @NoColumn
        private List<TFDepartment> deps;
        //人员
        @NoColumn
        private List<TFEmployInfo> infos;

}


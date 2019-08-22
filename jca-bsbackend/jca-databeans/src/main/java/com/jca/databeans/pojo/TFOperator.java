package com.jca.databeans.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
public class TFOperator implements Serializable {

        //帐号ID
		@Primary
        private Integer operatorId;
        //帐号NO（就是登陆帐号）
        private String operatorNo;
        //姓名
        private String operatorName;
        //密码
        private String password;
        //角色NO
        private String roleNo;
        //物业组NO
        private String propertyGroupNo;
        //状态（0:启用, 1:停用）
        private String isDown;
        //创建人
        private Integer createBy;
        //创建时间
        private String createTime;
        //更新人
        private Integer updateBy;
        //更新时间
        private String updateTime;
        @NoColumn
        private String roleName;
        @NoColumn
        private String groupName;	
        @NoColumn
        private TFPropertyGroup group; 
        @NoColumn
        private TFRole role;
}


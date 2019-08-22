package com.jca.databeans.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class TFPropertyGroup implements Serializable {

        //物业组ID
		@Primary
        private Integer propertyGroupId;
        //物业组NO
        private String propertyGroupNo;
        //小组名称
        private String propertyGroupName;
        //描述
        private String remarks;
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
        //创建人名称
        @NoColumn
        private String createName;
        @NoColumn
        private String propertyName;
        @NoColumn
        private List<TFProperty> properties;
        
   
		public TFPropertyGroup(String propertyGroupNo) {
			super();
			this.propertyGroupNo = propertyGroupNo;
		}
       
}


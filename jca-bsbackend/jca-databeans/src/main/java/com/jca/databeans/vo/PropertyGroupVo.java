package com.jca.databeans.vo;

import java.util.List;

import com.jca.databeans.pojo.TFProperty;
import com.jca.databeans.pojo.TFPropertyGroup;
import com.jca.databeans.pojo.TFPropertyGroup.TFPropertyGroupBuilder;
import com.jca.datacommon.annotation.NoColumn;
import com.jca.datacommon.annotation.Primary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyGroupVo {
			//物业组ID
			@Primary
	        private Integer propertyGroupId;
			//
			private Integer index;
	        //物业组NO
	        private String propertyGroupNo;
	        //小组名称
	        private String propertyGroupName;
	        //描述
	        private String remarks;
	        //
	        private Integer isDown;
	        //创建时间
	        private String createTime;
	        //更新时间
	        private String updateTime;
	        private Integer propertyCount;
	        //创建人名称
	        @NoColumn
	        private String createName;
	        @NoColumn
	        private String propertyName;
	        @NoColumn
	        private List<Integer> propertyNo;
}

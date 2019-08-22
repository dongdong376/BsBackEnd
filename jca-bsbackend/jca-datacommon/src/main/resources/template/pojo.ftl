package com.jca.databeans.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.jca.datacommon.annotation.NoColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 
 * @author:
 * @date: 2018/6/27 下午2:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ${myClass.className} implements Serializable {

     <#list myClass.fieldList as field>
        //${field.fieldRemarks}
        private ${field.fieldType} ${field.fieldName};
    </#list>    

}


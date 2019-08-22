package com.jca.datacommon.web.form;

import com.jca.datacommon.annotation.NotNull;
import com.jca.datacommon.annotation.Size;

import lombok.Data;

/**
 * @author
 * @version 1.0
 * @date 
 */
@Data
public class PageForm {
    @NotNull
    private Integer pageNum;

    @NotNull
    @Size(min = 1, max = 20)
    private Integer pageSize;
}

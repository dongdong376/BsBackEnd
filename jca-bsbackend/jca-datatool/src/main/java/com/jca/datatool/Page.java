package com.jca.datatool;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Data;

/**
 * @author
 * @version 1.0
 * @date 
 */
@Data
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 5390817850198908640L;
    //总数
    private Long total;
    //数据
    private List<T> list;

    private Page(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public static <T> Page<T> with(Long total, List<T> data) {
        return new Page<T>(total, data);
    }

    public static <T> Page<T> empty() {
        return new Page<T>(0L, Collections.emptyList());//使用集合容器，返回空数据列表
    }
}

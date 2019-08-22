package com.jca.databeans.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * 数据实体
 * @author Administrator
 *
 */
@Data
public class Test implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5443610843911694313L;	
	private Integer infoId;
	private String infoName;
	public Test(Integer infoId, String infoName) {
		super();
		this.infoId = infoId;
		this.infoName = infoName;
	}
	public Test() {
		super();
	}
	public Test(String infoName) {
		super();
		this.infoName = infoName;
	}
	
}

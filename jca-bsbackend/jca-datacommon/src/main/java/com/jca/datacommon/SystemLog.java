package com.jca.datacommon;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 系统日志实体
 * @author Administrator
 *
 */
@Data
public class SystemLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1664181445384368042L;

	private String id;

	private String description;

	private String method;

	private Long logType;

	private String requestIp;

	private String exceptioncode;

	private String exceptionDetail;

	private String params;

	private String createBy;

	private LocalDateTime createDate;

}

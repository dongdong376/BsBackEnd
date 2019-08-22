package com.jca.datacommon.log.appender;

import java.time.LocalDateTime;

import com.alibaba.fastjson.JSON;
import com.jca.datacommon.tool.DateUtils;
import com.jca.datacommon.tool.LocalUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义日志输出
 * 
 * @author
 * @version 1.0
 * @date
 */
@Slf4j
public class CustomLogAppender extends AppenderBase<ILoggingEvent> {

	private static final String IP = LocalUtils.getLocalAddress();

	/**
	 * 日志格式编码
	 */
	private Encoder<ILoggingEvent> encoder;

	/**
	 * 项目名
	 */
	private String project;

	// static {
	// //自定义loggerAppender加入到logback的rootLogger中
	// LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
	// Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
	//
	// }

	@Override
	protected void append(ILoggingEvent e) {
		if (e.getLevel() == Level.ERROR) {
			doError(e);
		}
		log.info("==>" + e.getMessage() + ",threadName" + e.getThreadName());
		String msg = e.getMessage();
		String date = DateUtils.defaultFormat(LocalDateTime.now());
		log.info("==>" + date);
		LogVO logVO = new LogVO(project, IP, msg);
		log.info(JSON.toJSONString(logVO));
		System.out.println(JSON.toJSONString(logVO));
	}

	private void doError(ILoggingEvent e) {

	}

	public Encoder<ILoggingEvent> getEncoder() {
		return encoder;
	}

	public void setEncoder(Encoder<ILoggingEvent> encoder) {
		this.encoder = encoder;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
}

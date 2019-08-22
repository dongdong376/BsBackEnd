package com.jca.datacommon.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.jca.datacommon.tool.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 解决跨域问题 过滤器
 * 
 * @author Administrator
 *
 */
@Slf4j
public class SimpleCORSFilter implements Filter {
	// 字符编码
	protected String encoding = null;

	protected FilterConfig filterConfig = null;

	protected boolean ignore = true;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("初始化参数==>");
		this.filterConfig = filterConfig;
		// 获取初始化参数
		this.encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if (value == null) {
			this.ignore = true;
		} else if (value.equalsIgnoreCase("true")) {
			this.ignore = true;
		} else if (value.equalsIgnoreCase("yes")) {
			this.ignore = true;
		} else
			this.ignore = false;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		log.info("过滤操作==>");		
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest reqs = (HttpServletRequest) req;
		if (ignore || (req.getCharacterEncoding() == null)) {
			String encoding = selectEncoding(req);
			if (encoding != null)
				req.setCharacterEncoding(encoding);
		}		
		//包装请求
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(reqs) {
          /**
           * 当调用request.getHeader("token")时，则获取请求参数中token值并当做Header的值返回
           * @param name
           * @return
           */
          @Override
          public String getHeader(String name) {
              // 先从原本的Request中获取头，如果为空且名字为token，则从参数中查找并返回
              String superHeader = super.getHeader(name);
              if("usertoken".equals(name) && StringUtils.isEmpty(superHeader)){
					String token = reqs.getParameter("usertoken");
                  if (!StringUtils.isEmpty(token)) {
                      return token ;
                  }
              }
              return superHeader;
          }
      };
		//跨域
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "86400");
		response.setHeader("Connection", "keep-alive");
		response.setHeader("Access-Control-Allow-Headers","X-Requested-With,X-UI-Request,Lang,Content-Type,Accept,"
				+ "WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type,"
				+ " WG-OS-Version, WG-Device-Model, WG-CPU, WG-Sid, WG-App-Id, WG-Token");
		response.setContentType("application/josn;charset=UTF-8");
		chain.doFilter(requestWrapper, res);
	}

	@Override
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	protected String selectEncoding(ServletRequest request) {
		return (this.encoding);
	}
}
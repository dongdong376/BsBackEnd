package com.jca.datacommon.web;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * uri基本信息
 * @author Administrator
 *
 */
public class RequestUri {
    /**
     *  请求方法
     */
    private RequestMethod method;

    /**
     * 实际uri
     */
    private String uri;

    public RequestUri(RequestMethod method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    public RequestUri(String method, String uri) {
        this.method = RequestMethod.valueOf(method);
        this.uri = uri;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}

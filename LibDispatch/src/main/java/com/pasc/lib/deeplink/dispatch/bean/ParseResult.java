package com.pasc.lib.deeplink.dispatch.bean;

import java.util.Map;

/**
 * Created by chendaixi947 on 2019/4/17
 *
 * @since 1.0
 */
public class ParseResult {
    /** 协议完整路径 */
    private String url;
    /** scheme */
    private String scheme;
    /** host */
    private String host;
    /** 所有参数 */
    private Map<String, String> params;
    /** 端口号 */
    private int port;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

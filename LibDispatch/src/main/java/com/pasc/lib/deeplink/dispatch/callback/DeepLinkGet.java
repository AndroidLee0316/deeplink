package com.pasc.lib.deeplink.dispatch.callback;

import com.pasc.lib.deeplink.dispatch.bean.DeepLinkEntry;

import java.util.List;

/**
 * @author chendaixi947
 * @version 1.0
 * @date 2019/4/22
 */
public abstract class DeepLinkGet {
    /**
     * 注入DeepLink支持的协议
     *
     * @param deepLinkEntries 存放自定义协议的list
     */
    public abstract void injectProtocols(List<DeepLinkEntry> deepLinkEntries);

    /**
     * 注入白名单
     *
     * @param whiteUrls 允许打开app的scheme+域名，如：https://smt-stg.yun.city.pingan.com
     */
    public void injectWhiteList(List<String> whiteUrls) {
    }
}

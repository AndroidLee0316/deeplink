package com.pasc.lib.deeplink;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.pasc.lib.deeplink.dispatch.DeepLinkManager;
import com.pasc.lib.deeplink.dispatch.bean.ParseResult;
import com.pasc.lib.deeplink.dispatch.callback.CustomStrategy;
import com.pasc.lib.deeplink.dispatch.util.DeepLinkUri;
import com.pasc.lib.router.ServiceHandlerCallback;
import com.pasc.lib.router.ServiceProtocol;
import com.pasc.lib.statistics.StatisticsManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2019 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2019/4/18
 */
public abstract class PascDeepLinkStrategy implements CustomStrategy {
    @Override
    public void onResult(final Activity activity, ParseResult parseResult) {
        try {
            //统计
            if (parseResult.getParams().containsKey("statsPageName")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("服务名称", parseResult.getParams().get("statsPageName"));
                StatisticsManager.getInstance().onEvent("DeepLink", "跳转页面", map);
            }

            if (parseResult.getParams().containsKey("h5Link")) {
                //跳转到网页
                String h5Link = parseResult.getParams().get("h5Link");
                if (!TextUtils.isEmpty(h5Link)) {
                    List<String> whiteList = DeepLinkManager.getInStance().getWhiteList();
                    //判断当前uri是否在白名单以内
                    if (whiteList != null && !whiteList.isEmpty()) {
                        DeepLinkUri deepLinkUri = DeepLinkUri.parse(h5Link);
                        //当前设置了白名单
                        if (deepLinkUri != null && !whiteList.contains(deepLinkUri.scheme() + "://" + deepLinkUri.host())) {
                            //传过来的url不在白名单里面，过滤掉
                            jumpNotFound(activity);
                            return;
                        }
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append(h5Link);
                    if (!h5Link.contains("?")) {
                        //原url不自带参数
                        if (!parseResult.getParams().isEmpty() &&
                                !(parseResult.getParams().size() == 1 && parseResult.getParams().containsKey("h5Link"))) {
                            //当前有前置参数
                            sb.append("?");
                        }
                    }
                    if (!sb.toString().endsWith("?")) {
                        sb.append("&");
                    }
                    //将所有前置参数拼接到url后头，以防h5需要前置参数
                    for (Map.Entry<String, String> entry : parseResult.getParams().entrySet()) {
                        if (!entry.getKey().equals("h5Link")) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue());
                            sb.append("&");
                        }
                    }

                    String destH5Link;
                    if (sb.toString().endsWith("&")) {
                        destH5Link = sb.toString().substring(0, sb.toString().length() - 1);
                    } else {
                        destH5Link = sb.toString();
                    }

                    ServiceProtocol.instance().startService(activity, destH5Link, parseResult.getParams());
                }
            } else {
                //直接跳转目标Activity
                Uri uri = Uri.parse(parseResult.getUrl());
                String path = uri.getPath();
                String host = uri.getHost();
                String routerUrl = "router://" + host + path;
                ServiceProtocol.instance().startService(activity, routerUrl, parseResult.getParams(), new ServiceHandlerCallback() {
                    @Override
                    public void onSuccess(Activity activity, String s, Map<String, String> map) {
                        activity.finish();
                    }

                    @Override
                    public void onError(Activity activity, String s, Map<String, String> map, int i, String s1) {
                        jumpNotFound(activity);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            jumpNotFound(activity);
        }
    }

    private void jumpNotFound(Activity activity) {
        //没找到界面,启动主页
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
        if (intent != null) {
            activity.startActivity(intent);
        }
        activity.finish();
    }
}

package com.pasc.lib.deeplink;

import android.app.Application;

import com.pasc.lib.deeplink.dispatch.DeepLinkManager;
import com.pasc.lib.deeplink.dispatch.callback.CustomStrategy;
import com.pasc.lib.deeplink.dispatch.callback.DeepLinkGet;

/**
 * Copyright (C) 2019 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2019/4/18
 */
public class PascDeepLinkManager {
    private PascDeepLinkManager() {
    }

    public void init(Application application, DeepLinkGet deepLinkGet, CustomStrategy customStrategy) {
        if (deepLinkGet == null) {
            throw new IllegalArgumentException("参数deepLinkApi为null,不合法！！！！");
        }
        DeepLinkManager.getInStance().init(application, deepLinkGet, customStrategy);
    }
    public static PascDeepLinkManager getInStance() {
        return PascDeepLinkManager.SingleHolder.instance;
    }

    private static final class SingleHolder {
        private static PascDeepLinkManager instance = new PascDeepLinkManager();
    }
}

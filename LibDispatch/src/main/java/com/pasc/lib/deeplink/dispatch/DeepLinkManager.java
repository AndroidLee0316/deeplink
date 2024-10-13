package com.pasc.lib.deeplink.dispatch;

import android.app.Application;

import com.pasc.lib.deeplink.dispatch.bean.DeepLinkEntry;
import com.pasc.lib.deeplink.dispatch.callback.CustomStrategy;
import com.pasc.lib.deeplink.dispatch.callback.DeepLinkGet;

import java.util.ArrayList;
import java.util.List;

/**
 * DeepLinkManager
 * Created by chendaixi947 on 2019/4/16
 *
 * @since 1.0
 */
public class DeepLinkManager {

    private CustomStrategy mCustomStrategy;
    private List<String> mWhiteList = new ArrayList<>();

    private DeepLinkManager() {
    }

    public void init(Application application, DeepLinkGet deepLinkGet, CustomStrategy customStrategy) {
        if (deepLinkGet == null) {
            throw new IllegalArgumentException("参数deepLinkApi为null,不合法！！！！");
        }
        mCustomStrategy = customStrategy;
        List<DeepLinkEntry> deepLinkEntries = new ArrayList<>();
        deepLinkGet.injectProtocols(deepLinkEntries);
        deepLinkGet.injectWhiteList(mWhiteList);
        DeepLinkLoader.register(deepLinkEntries);
    }

    public List<String> getWhiteList() {
        return mWhiteList;
    }

    public CustomStrategy getCustomStrategy() {
        return mCustomStrategy;
    }


    public static DeepLinkManager getInStance() {
        return SingleHolder.instance;
    }

    private static final class SingleHolder {
        private static DeepLinkManager instance = new DeepLinkManager();
    }
}

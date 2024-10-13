package com.pasc.lib.deeplink.dispatch.activity;

import android.app.Activity;
import android.os.Bundle;

import com.pasc.lib.deeplink.dispatch.DeepLinkLoader;
import com.pasc.lib.deeplink.dispatch.DeepLinkManager;
import com.pasc.lib.deeplink.dispatch.delegate.DeepLinkDelegate;

import java.util.List;

/**
 * deepLink跳转中间层，用于拦截所有自定义的uri协议。
 * <p>
 * uri协议：初始化DeepLink组件时传入。具体可查看类{@link com.pasc.lib.deeplink.dispatch.DeepLinkManager}的{@link com.pasc.lib.deeplink.dispatch.DeepLinkManager#init(android.app.Application, com.pasc.lib.deeplink.dispatch.callback.DeepLinkGet, com.pasc.lib.deeplink.dispatch.callback.CustomStrategy)}
 * 方法，以及类{@link com.pasc.lib.deeplink.dispatch.callback.DeepLinkGet}的{@link com.pasc.lib.deeplink.dispatch.callback.DeepLinkGet#injectProtocols(List)}方法
 *
 * @author chendaixi947
 * @version 1.0
 */
public class DeepLinkActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(new DeepLinkLoader());
        deepLinkDelegate.setCustomStrategy(DeepLinkManager.getInStance().getCustomStrategy());
        deepLinkDelegate.dispatchFrom(this);
        finish();
    }
}

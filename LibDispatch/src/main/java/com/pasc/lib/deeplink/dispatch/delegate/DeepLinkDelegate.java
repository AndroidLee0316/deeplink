package com.pasc.lib.deeplink.dispatch.delegate;


import com.pasc.lib.deeplink.dispatch.DeepLinkLoader;

import java.util.Arrays;

/**
 * DeepLink代理类
 *
 * @author chendaixi947
 * @version 1.0
 */
public final class DeepLinkDelegate extends BaseDeepLinkDelegate {
    public DeepLinkDelegate(DeepLinkLoader deepLinkLoader) {
        super(Arrays.asList(
                deepLinkLoader));
    }
}

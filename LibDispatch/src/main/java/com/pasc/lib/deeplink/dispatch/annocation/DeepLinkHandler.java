
package com.pasc.lib.deeplink.dispatch.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface DeepLinkHandler {
    String ACTION = "com.pasc.lib.deeplink.DEEPLINK_ACTION";
    String EXTRA_SUCCESSFUL = "com.pasc.lib.deeplink.EXTRA_SUCCESSFUL";
    String EXTRA_URI = "com.pasc.lib.deeplink.EXTRA_URI";
    String EXTRA_URI_TEMPLATE = "com.pasc.lib.deeplink.EXTRA_URI_TEMPLATE";
    String EXTRA_ERROR_MESSAGE = "com.pasc.lib.deeplink.EXTRA_ERROR_MESSAGE";
}

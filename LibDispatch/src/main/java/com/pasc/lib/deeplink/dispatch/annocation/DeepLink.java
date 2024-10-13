
package com.pasc.lib.deeplink.dispatch.annocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface DeepLink {
    String IS_DEEP_LINK = "is_deep_link_flag";
    String URI = "deep_link_uri";
    String REFERRER_URI = "android.intent.extra.REFERRER";
}

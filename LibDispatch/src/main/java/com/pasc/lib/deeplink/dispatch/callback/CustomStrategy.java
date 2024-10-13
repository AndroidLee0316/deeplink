package com.pasc.lib.deeplink.dispatch.callback;

import android.app.Activity;

import com.pasc.lib.deeplink.dispatch.bean.ParseResult;

/**
 * DeepLink协议解析结果结果回调,该接口只有为自定义跳转类型时回调
 * Created by chendaixi947 on 2019/4/17
 *
 * @since 1.0
 */
public interface CustomStrategy {
    void onResult(Activity activity, ParseResult parseResult);
}

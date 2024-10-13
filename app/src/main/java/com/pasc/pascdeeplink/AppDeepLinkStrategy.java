package com.pasc.pascdeeplink;

import android.app.Activity;
import android.util.Log;

import com.pasc.lib.deeplink.dispatch.bean.ParseResult;
import com.pasc.lib.deeplink.dispatch.callback.CustomStrategy;

/**
 * Copyright (C) 2019 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2019/4/17
 */
public class AppDeepLinkStrategy implements CustomStrategy {
    private static final String TAG = AppDeepLinkStrategy.class.getSimpleName();

    @Override
    public void onResult(Activity activity, ParseResult parseResult) {
        //自定义跳转
        Log.d(TAG, "url = " + parseResult.getUrl() + ",\n" + "scheme = " + parseResult.getScheme() + ",\n"
                + ",\nhost = " + parseResult.getHost() + ",\nport = " + parseResult.getPort() + ", \nparams = " + parseResult.getParams());

    }
}

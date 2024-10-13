package com.pasc.pascdeeplink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.pasc.pascdeeplink.ui.MainActivity;
import com.pasc.pascdeeplink.ui.SecondActivity;

/**
 * Copyright (C) 2019 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2019/4/18
 */
public class JumpUtils {
    private static final String ACTION_DEEP_LINK_METHOD = "deep_link_method";
    private static final String ACTION_DEEP_LINK_COMPLEX = "deep_link_complex";
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Handles deep link with one param, doc does not contain "param"
     *
     * @return A intent to start {@link MainActivity}
     */
    public static Intent intentForDeepLinkMethod(Context context) {
        return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_METHOD);
    }


    public static Intent intentForParamDeepLinkMethod(Context context) {
        return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_COMPLEX);
    }

    /**
     * Handles deep link with params.
     *
     * @param context of the activity
     * @param bundle  expected to contain the key {@code qp}.
     * @return TaskStackBuilder set with first intent to start {@link MainActivity} and second intent
     * to start {@link SecondActivity}.
     */
    public static TaskStackBuilder intentForTaskStackBuilderMethods(Context context, Bundle bundle) {
        Log.d(TAG, "without query parameter :");
        if (bundle != null && bundle.containsKey("qp")) {
            Log.d(TAG, "found new parameter :with query parameter :" + bundle.getString("qp"));
        }
        Intent detailsIntent =
                new Intent(context, SecondActivity.class).setAction(ACTION_DEEP_LINK_COMPLEX);
        Intent parentIntent =
                new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_COMPLEX);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;

    }

    public static Intent intentForComplexMethod(Context context, Bundle bundle) {
        if (bundle != null && bundle.containsKey("qp")) {
            Log.d(TAG, "found new parameter :with query parameter :" + bundle.getString("qp"));
        }
        return new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_COMPLEX);
    }
}

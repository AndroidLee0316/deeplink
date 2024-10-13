package com.pasc.lib.deeplink.dispatch.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.pasc.lib.deeplink.dispatch.annocation.DeepLink;
import com.pasc.lib.deeplink.dispatch.annocation.DeepLinkHandler;
import com.pasc.lib.deeplink.dispatch.bean.DeepLinkEntry;
import com.pasc.lib.deeplink.dispatch.bean.DeepLinkResult;
import com.pasc.lib.deeplink.dispatch.bean.ParseResult;
import com.pasc.lib.deeplink.dispatch.callback.CustomStrategy;
import com.pasc.lib.deeplink.dispatch.loader.Parser;
import com.pasc.lib.deeplink.dispatch.util.DeepLinkUri;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"WeakerAccess", "unused"})
public class BaseDeepLinkDelegate {
    protected static final String TAG = "DeepLinkDelegate";
    /** 实现Parser接口的所有Loader集合 */
    protected final List<? extends Parser> mLoaders;
    private CustomStrategy mCustomStrategy;

    public BaseDeepLinkDelegate(List<? extends Parser> loaders) {
        this.mLoaders = loaders;
    }

    /**
     * 根据uri字符串查找对应的DeepLinkEntry
     *
     * @param uriString uri字符串
     */
    private DeepLinkEntry findEntry(String uriString) {
        for (Parser loader : mLoaders) {
            DeepLinkEntry entry = loader.parseUri(uriString);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }

    public void setCustomStrategy(CustomStrategy customStrategy) {
        this.mCustomStrategy = customStrategy;
    }

    /**
     * 协议的解析、分发
     *
     * @param activity activity
     */
    public DeepLinkResult dispatchFrom(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        }
        return dispatchFrom(activity, activity.getIntent());
    }


    /**
     * 协议的解析、分发
     *
     * @param activity     activity
     * @param sourceIntent 携带数据的intent
     */
    public DeepLinkResult dispatchFrom(Activity activity, Intent sourceIntent) {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        }
        if (sourceIntent == null) {
            throw new NullPointerException("sourceIntent == null");
        }
        //拿到sourceIntent带的Uri协议数据
        Uri uri = sourceIntent.getData();
        if (uri == null) {
            return createResultAndNotify(activity, false, null,
                    null, "No Uri in given activity's intent.");
        }
        String uriString = URLDecoder.decode(uri.toString());
        //拿到包含uriString的DeepLinkEntry对象
        DeepLinkEntry entry = findEntry(uriString);
        DeepLinkUri deepLinkUri = DeepLinkUri.parse(uriString);

        if (entry != null && deepLinkUri != null) {
            Map<String, String> tempParamsMap = entry.getParameters(uriString);
            if (uriString.contains("?") && !uriString.endsWith("?")) {
                //uri自带参数，格式如：isz://example.com?URL=encodedURL&nativePage=xxx&extension=1
                String paramsSub = uriString.substring(uriString.indexOf("?") + 1, uriString.length());
                String[] split = paramsSub.split("&");
                for (String s : split) {
                    int index = s.indexOf("=");
                    if (index == s.length() - 1) {
                        //只有key
                        tempParamsMap.put(s.substring(0, index), "");
                    } else {
                        tempParamsMap.put(s.substring(0, index), s.substring(index + 1, s.length()));
                    }
                }
            }

            if (entry.getType() == DeepLinkEntry.Type.CUSTOM) {
                //外部自定义跳转
                if (mCustomStrategy != null) {
                    ParseResult parseResult = new ParseResult();
                    parseResult.setHost(deepLinkUri.host());
                    parseResult.setParams(tempParamsMap);
                    parseResult.setPort(deepLinkUri.port());
                    parseResult.setScheme(deepLinkUri.scheme());
                    parseResult.setUrl(uriString);
                    mCustomStrategy.onResult(activity, parseResult);
                }
                notifyListener(activity, false, uri, uriString, null);
                return new DeepLinkResult(false, uriString, null);
            }
            Map<String, String> nextParameterMap = new HashMap<>(tempParamsMap);
            buildParameterMap(uri, deepLinkUri, nextParameterMap);
            Bundle parameters;
            if (sourceIntent.getExtras() != null) {
                parameters = new Bundle(sourceIntent.getExtras());
            } else {
                parameters = new Bundle();
            }
            for (Map.Entry<String, String> parameterEntry : nextParameterMap.entrySet()) {
                parameters.putString(parameterEntry.getKey(), parameterEntry.getValue());
            }
            try {
                Class<?> c = entry.getActivityClass();
                Intent newIntent;
                TaskStackBuilder taskStackBuilder = null;
                if (entry.getType() == DeepLinkEntry.Type.CLASS) {
                    newIntent = new Intent(activity, c);
                } else {
                    Method method;
                    try {
                        method = c.getMethod(entry.getMethod(), Context.class);
                        if (method.getReturnType().equals(TaskStackBuilder.class)) {
                            taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity);
                            if (taskStackBuilder.getIntentCount() == 0) {
                                return createResultAndNotify(activity, false, uri, entry.getUriTemplate(),
                                        "Could not deep link to method: "
                                                + entry.getMethod() + " intents length == 0");
                            }
                            newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.getIntentCount() - 1);
                        } else {
                            newIntent = (Intent) method.invoke(c, activity);
                        }
                    } catch (NoSuchMethodException exception) {
                        method = c.getMethod(entry.getMethod(), Context.class, Bundle.class);
                        if (method.getReturnType().equals(TaskStackBuilder.class)) {
                            taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity, parameters);
                            if (taskStackBuilder.getIntentCount() == 0) {
                                return createResultAndNotify(activity, false, uri, entry.getUriTemplate(),
                                        "Could not deep link to method: "
                                                + entry.getMethod() + " intents length == 0");
                            }
                            newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.getIntentCount() - 1);
                        } else {
                            newIntent = (Intent) method.invoke(c, activity, parameters);
                        }
                    }
                }

                if (newIntent == null) {
                    return createResultAndNotify(activity, false, uri, entry.getUriTemplate(), null);
                }

                if (newIntent.getAction() == null) {
                    newIntent.setAction(sourceIntent.getAction());
                }
                if (newIntent.getData() == null) {
                    newIntent.setData(sourceIntent.getData());
                }
                newIntent.putExtras(parameters);
                newIntent.putExtra(DeepLink.IS_DEEP_LINK, true);
                newIntent.putExtra(DeepLink.REFERRER_URI, uri);
                if (activity.getCallingActivity() != null) {
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                }
                if (taskStackBuilder != null) {
                    taskStackBuilder.startActivities();
                } else {
                    activity.startActivity(newIntent);
                }
                return createResultAndNotify(activity, true, uri, entry.getUriTemplate(), null);
            } catch (NoSuchMethodException exception) {
                return createResultAndNotify(activity, false, uri,
                        entry.getUriTemplate(), "Deep link to non-existent method: " + entry.getMethod());
            } catch (IllegalAccessException exception) {
                return createResultAndNotify(activity, false, uri,
                        entry.getUriTemplate(), "Could not deep link to method: " + entry.getMethod());
            } catch (InvocationTargetException exception) {
                return createResultAndNotify(activity, false, uri,
                        entry.getUriTemplate(), "Could not deep link to method: " + entry.getMethod());
            }
        } else {
            return createResultAndNotify(activity, false, uri, null,
                    "No registered entity to handle deep link: " + uri.toString());
        }
    }

    private void buildParameterMap(Uri uri, DeepLinkUri deepLinkUri, Map<String, String> parameterMap) {
        for (String queryParameter : deepLinkUri.queryParameterNames()) {
            for (String queryParameterValue : deepLinkUri.queryParameterValues(queryParameter)) {
                if (parameterMap.containsKey(queryParameter)) {
                    Log.w(TAG, "Duplicate parameter name in path and query param: " + queryParameter);
                }
                parameterMap.put(queryParameter, queryParameterValue);
            }
        }
        parameterMap.put(DeepLink.URI, uri.toString());
    }

    /**
     * 创建和向外通知DeepLink分发结果
     *
     * @param activity
     * @param error
     * @param successful
     * @param uri
     * @param uriString
     */
    private static DeepLinkResult createResultAndNotify(Activity activity,
                                                        final boolean successful, final Uri uri,
                                                        String uriString, final String error) {
        notifyListener(activity, !successful, uri, uriString, error);
        activity.finish();
        return new DeepLinkResult(successful, uri != null ? uri.toString() : null, error);
    }

    private static void notifyListener(Context context, boolean isError, Uri uri,
                                       String uriString, String errorMessage) {
        Intent intent = new Intent();
        intent.setAction(DeepLinkHandler.ACTION);
        intent.putExtra(DeepLinkHandler.EXTRA_URI, uri != null ? uri.toString() : "");
        intent.putExtra(DeepLinkHandler.EXTRA_URI_TEMPLATE, uriString != null ? uriString : "");
        intent.putExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, !isError);
        if (isError) {
            intent.putExtra(DeepLinkHandler.EXTRA_ERROR_MESSAGE, errorMessage);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public boolean supportsUri(String uriString) {
        return findEntry(uriString) != null;
    }
}

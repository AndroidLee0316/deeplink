package com.pasc.pascdeeplink;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.pasc.lib.deeplink.PascDeepLinkManager;
import com.pasc.lib.deeplink.PascDeepLinkStrategy;
import com.pasc.lib.deeplink.dispatch.annocation.DeepLinkHandler;
import com.pasc.lib.deeplink.dispatch.bean.DeepLinkEntry;
import com.pasc.lib.deeplink.dispatch.bean.ParseResult;
import com.pasc.lib.deeplink.dispatch.callback.DeepLinkGet;
import com.pasc.lib.router.BaseJumper;
import com.pasc.lib.router.RouterManager;
import com.pasc.lib.router.ServiceHandlerCallback;
import com.pasc.lib.router.ServiceProtocol;
import com.pasc.pascdeeplink.ui.router.LoginActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initDeepLink();
        initARouter();
    }

    private void initARouter() {
        RouterManager.initARouter(this, BuildConfig.DEBUG);
        RouterManager.instance().setApiGet(new com.pasc.lib.router.interceptor.ApiGet() {
            @Override
            public boolean isLogin() {
                return LoginActivity.isLogin;
            }

            @Override
            public boolean isCertification() {
                return false;
            }

            @Override
            public void beforeInterceptor(String path, Bundle bundle) {
            }

            @Override
            public void gotoLogin(String targetPath, Bundle targetBundle) {
                    Log.d("SampleApplication","gotoLogin");

                    BaseJumper.jumpARouter("/login/login/main");
                //Toast.makeText(SampleApplication.this, "gotoLogin", Toast.LENGTH_LONG).show();
            }

            @Override
            public void gotoCertification(String targetPath, Bundle targetBundle) {
                Log.d("SampleApplication","gotoCertification");
                //Toast.makeText(SampleApplication.this, "gotoCertification", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }

    private void initDeepLink() {
        IntentFilter intentFilter = new IntentFilter(DeepLinkHandler.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new DeepLinkReceiver(), intentFilter);

        PascDeepLinkManager.getInStance().init(this, new DeepLinkGet() {
            @Override
            public void injectProtocols(List<DeepLinkEntry> deepLinkEntries) {
                //添加协议
                List<DeepLinkEntry> REGISTRY = Collections.unmodifiableList(Arrays.asList(
                        new DeepLinkEntry("smt://com.pingan.smt/.*", DeepLinkEntry.Type.CUSTOM, null, null)

//                        new DeepLinkEntry("smt://com.deeplink.sample/app/home/main", DeepLinkEntry.Type.CLASS, MainActivity.class, null),
//                        new DeepLinkEntry("smt://com.deeplink.sample.method/TaskStackBuilder/app/home/main", DeepLinkEntry.Type.METHOD, JumpUtils.class, "intentForTaskStackBuilderMethods"),
//                        new DeepLinkEntry("smt://com.deeplink.custom/test/test/main", DeepLinkEntry.Type.CUSTOM, null, null)
                ));
                deepLinkEntries.addAll(REGISTRY);
            }

            @Override
            public void injectWhiteList(List<String> whiteUrls) {
                super.injectWhiteList(whiteUrls);
                whiteUrls.add("https://smt-stg.yun.city.pingan.com");
            }
        }, new PascDeepLinkStrategy() {
            @Override
            public void onResult(Activity activity, ParseResult parseResult) {
                super.onResult(activity, parseResult);
                String msg = "url = " + parseResult.getUrl() + ",\n" + "scheme = " + parseResult.getScheme() + ",\n"
                        + ",\nhost = " + parseResult.getHost() + ",\nport = " + parseResult.getPort() + ", \nparams = " + parseResult.getParams();
                // Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                Log.d("DeepLink", msg);
                //@TODO 在这里添加自定义跳转逻辑，包括需要认证、需要登陆、需要定位权限等前置条件判断

                ServiceProtocol.instance().startService(activity, parseResult.getUrl(), parseResult.getParams(), new ServiceHandlerCallback() {
                    @Override
                    public void onSuccess(Activity activity, String s, Map<String, String> map) {
                        Toast.makeText(activity,"onSuccess",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Activity activity, String s, Map<String, String> map, int i, String s1) {
                        Toast.makeText(activity,"onError",Toast.LENGTH_LONG).show();
                    }
                });

                if ("true".equals(parseResult.getParams().get("needLogin"))) {
                    //@TODO 登陆逻辑
                }

                if ("true".equals(parseResult.getParams().get("needCert"))) {
                    //@TODO 认证逻辑
                }
                if ("true".equals(parseResult.getParams().get("needLocPermission"))) {
                    //@TODO 定位权限逻辑
                }

                //@TODO 根据路由或者H5跳转到对应页面逻辑

            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}

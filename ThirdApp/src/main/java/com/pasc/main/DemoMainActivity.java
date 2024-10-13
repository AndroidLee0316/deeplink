package com.pasc.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author chendaixi947
 * @version 1.0
 * @date 2019/4/19
 */
public class DemoMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);
        getSupportActionBar().setTitle("H5测试网页");
        WebView wView = (WebView) findViewById(R.id.webView);
        WebSettings wSet = wView.getSettings();
        wSet.setJavaScriptEnabled(true);
        wView.loadUrl("file:///android_asset/demo_test.html");
    }
}

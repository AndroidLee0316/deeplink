package com.pasc.pascdeeplink.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pasc.lib.deeplink.dispatch.annocation.DeepLink;
import com.pasc.lib.router.ServiceProtocol;
import com.pasc.pascdeeplink.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String ACTION_DEEP_LINK_METHOD = "deep_link_method";
    private static final String ACTION_DEEP_LINK_COMPLEX = "deep_link_complex";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button testRouterJump = findViewById(R.id.testRouterJump);
        testRouterJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                ServiceProtocol.instance().startService(MainActivity.this, "router://com.pingan.smt/hybrid/app/main?needLogin=true&needIdentity=true&statsPageName=新闻详情&h5Link=https%3A%2F%2Fsmt-stg.yun.city.pingan.com%2Fbasic%2Fstg%2Fapp%2Ffeature%2Fnews%2F%3Fopenweb%3Dpaschybrid%26from%3Dsinglemessage%26isappinstalled%3D0%23%2Farticle%2Fffaa8a3223b84efd8ef807de4d265a63", params);
            }
        });

        getSupportActionBar().setTitle("我是首页");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        Intent intent = getIntent();
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            String toastMessage;
            Bundle parameters = intent.getExtras();
            Log.d(TAG, "Deeplink params: " + parameters);

            if (ACTION_DEEP_LINK_METHOD.equals(intent.getAction())) {
                toastMessage = "method with param1:" + parameters.getString("param1");
            } else if (ACTION_DEEP_LINK_COMPLEX.equals(intent.getAction())) {
                toastMessage = parameters.getString("arbitraryNumber");
            } else if (parameters.containsKey("arg")) {
                toastMessage = "class and found arg:" + parameters.getString("arg");
            } else {
                toastMessage = "class";
            }

            // You can pass a query parameter with the URI, and it's also in parameters, like
            // dld://classDeepLink?qp=123
            if (parameters.containsKey("qp")) {
                toastMessage += " with query parameter " + parameters.getString("qp");
            }
            Uri referrer = ActivityCompat.getReferrer(this);
            if (referrer != null) {
                toastMessage += " and referrer: " + referrer.toString();
            }
            showToast(toastMessage);
        }

    }

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

    private void showToast(String message) {
        Toast.makeText(this, "Deep Link: " + message, Toast.LENGTH_SHORT).show();
    }
}

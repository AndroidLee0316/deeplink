package com.pasc.pascdeeplink.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.pasc.lib.deeplink.dispatch.annocation.DeepLink;
import com.pasc.pascdeeplink.R;

// You can have multiple path parameters in the URI - both must be present
public class SecondActivity extends AppCompatActivity {

    private static final String TAG = SecondActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("我是SecondActivity");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));

        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = getIntent().getExtras();
            Log.d(TAG, "Deeplink params: " + parameters);

            String idString = parameters.getString("id");
            String name = parameters.getString("name");
            if (!TextUtils.isEmpty(idString)) {
                showToast("class id== " + idString + " and name==" + name);
            } else {
                showToast("no id in the deeplink");
            }
        } else {
            showToast("no deep link :( ");
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, "Deep Link: " + message, Toast.LENGTH_SHORT).show();
    }
}

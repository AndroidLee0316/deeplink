package com.pasc.pascdeeplink.ui.router;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.pascdeeplink.R;

/**
 * @author yangzijian
 * @date 2019/1/4
 * @des
 * @modify
 **/
@Route(path = "/app/main/home")
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.feedback_activity);
        String data = getIntent ().getStringExtra ("data");
        Bundle bundle = getIntent ().getExtras ();
        Log.e ("yzj", "" + data);

    }


}

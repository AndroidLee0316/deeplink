package com.pasc.pascdeeplink.ui.router;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pasc.lib.router.interceptor.LoginInterceptor;
import com.pasc.pascdeeplink.R;

/**
 * @author yangzijian
 * @date 2019/1/4
 * @des
 * @modify
 **/
@Route(path = "/login/login/main")
public class LoginActivity extends AppCompatActivity {
    public static boolean isLogin=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.login_activity);
        String name=getIntent ().getStringExtra ("name");
        String job=getIntent ().getStringExtra ("job");
        String obj=getIntent ().getStringExtra ("obj");

        Log.e ("yzj",""+name+" - "+job+" - "+obj);

    }

    public void viewClick(View view){
        isLogin=true;
        LoginInterceptor.notifyCallBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        LoginInterceptor.notifyCallBack (false);

    }
}

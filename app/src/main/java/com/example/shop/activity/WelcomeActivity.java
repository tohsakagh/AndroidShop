package com.example.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //两秒钟进入MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //启动MainActivity主页面，这段代码是在主线程执行
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                //关闭当前页面（结束WelcomeActivity）
                finish();
            }
        },100);
    }
}

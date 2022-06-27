package com.example.shop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.R;


public class RegistSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    TextView registSuccess;
    SpannableString text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_success);
        registSuccess = (TextView) findViewById(R.id.regist_success);
        text = new SpannableString("注册成功,转移到登录页面");
        //设置下划线
        text.setSpan(new UnderlineSpan(), 0,12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体前景色
        text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registSuccess.setText((CharSequence) text);
        registSuccess.setMovementMethod(LinkMovementMethod.getInstance());
        registSuccess.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.regist_success){
            intent = new Intent(RegistSuccessActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}

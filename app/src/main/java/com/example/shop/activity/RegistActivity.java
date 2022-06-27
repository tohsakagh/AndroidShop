package com.example.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.R;
import com.example.shop.utils.RegistUtils;


public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    private Button toLogin;
    private Button regist;
    private EditText registUsernameInput;
    private EditText registPasswsordInput;
    private TextView errorTip;
    private ImageView registToHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        toLogin = (Button) findViewById(R.id.toLogin_id);
        toLogin.setOnClickListener(this);
        regist = (Button) findViewById(R.id.btn_regist);
        regist.setOnClickListener(this);
        registUsernameInput = (EditText) findViewById(R.id.regist_username_input);
        registPasswsordInput = (EditText) findViewById(R.id.regist_password_input);
        errorTip = (TextView) findViewById(R.id.error_tip);
        registToHome = (ImageView) findViewById(R.id.regist_go_home);
        registToHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.btn_regist) {
            String username = registUsernameInput.getText().toString();
            String password = registPasswsordInput.getText().toString();
            boolean regist = new RegistUtils().isRegist(username, password, errorTip);
            if (regist) {
                intent = new Intent(RegistActivity.this, RegistSuccessActivity.class);
            }
        } else if (v.getId() == R.id.toLogin_id) {
            intent = new Intent(RegistActivity.this, LoginActivity.class);
        } else if (v.getId() == R.id.regist_go_home){
            intent = new Intent(RegistActivity.this, MainActivity.class);
        }

        if (intent != null){
            startActivity(intent);
        }
    }
}

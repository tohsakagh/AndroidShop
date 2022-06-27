package com.example.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.R;
import com.example.shop.utils.LoginUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signin;
    private TextView regist;
    private EditText loginUsernameInput;
    private EditText loginPasswordInput;
    private TextView errorMessage;
    private ImageView loginToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        signin = (Button) findViewById(R.id.signin);
        regist = (TextView) findViewById(R.id.regist);
        loginUsernameInput = (EditText) findViewById(R.id.login_username_input);
        loginPasswordInput = (EditText) findViewById(R.id.login_password_input);
        errorMessage = (TextView) findViewById(R.id.erroremessqge);
        loginToHome = (ImageView) findViewById(R.id.login_go_home);
        errorMessage.setVisibility(View.GONE);
        signin.setOnClickListener(this);
        regist.setOnClickListener(this);
        loginToHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.signin){
            String username = loginUsernameInput.getText().toString();
            String password = loginPasswordInput.getText().toString();
            boolean exist = LoginUtils.isExist(username, password);
            if (exist){
                errorMessage.setVisibility(View.GONE);
                intent = new Intent(LoginActivity.this, MainActivity.class);
            }else {
                loginPasswordInput.setText(null);
                errorMessage.setVisibility(View.VISIBLE);
            }
        }else if (v.getId() == R.id.regist){
            intent = new Intent(LoginActivity.this, RegistActivity.class);
        }else if (v.getId() == R.id.login_go_home){
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}

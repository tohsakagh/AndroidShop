package com.example.shop.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.R;
import com.example.shop.bean.Msg;
import com.example.shop.service.UserService;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameUpdate;
    private EditText passwordUpdate;
    private Button btnUsernameUpdate;
    private Button btnPasswordUpdate;
    private ImageView back;

    private UserService userUtils;
    private int id;

    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        userUtils = new UserService();
        id = userUtils.getIdByUsername(Msg.loginUsername);

        usernameUpdate = (EditText) findViewById(R.id.login_username_update);
        passwordUpdate = (EditText) findViewById(R.id.login_password_update);
        btnPasswordUpdate = (Button) findViewById(R.id.btn_password_update);
        btnUsernameUpdate = (Button) findViewById(R.id.btn_username_update);
        back = (ImageView) findViewById(R.id.back);

        btnUsernameUpdate.setOnClickListener(this);
        btnPasswordUpdate.setOnClickListener(this);
        back.setOnClickListener(this);
        usernameUpdate.setEnabled(false);
        passwordUpdate.setEnabled(false);
        usernameUpdate.setText(Msg.loginUsername);
        passwordUpdate.setText(Msg.loginPassword);

    }

    @Override
    public void onClick(View v) {
        String message = null;
        if (v.getId() == R.id.btn_username_update) {
            message = Msg.loginUsername;
            if ("??????".equals(btnUsernameUpdate.getText())) {
                usernameUpdate.setEnabled(true);
                btnUsernameUpdate.setText("??????");
            } else if ("??????".equals(btnUsernameUpdate.getText())) {
                alertClick(v, usernameUpdate, message);
                usernameUpdate.setEnabled(false);
                btnUsernameUpdate.setText("??????");
                Msg.loginUsername = usernameUpdate.getText().toString();
                View inflate = getLayoutInflater().inflate(R.layout.fragment_islogin_mine, null);
                usernameText = (TextView) inflate.findViewById(R.id.username);
                usernameText.setText(Msg.loginUsername);
            }
        } else if (v.getId() == R.id.btn_password_update) {
            message = Msg.loginPassword;
            if ("??????".equals(btnPasswordUpdate.getText())) {
                passwordUpdate.setEnabled(true);
                passwordUpdate.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                btnPasswordUpdate.setText("??????");
            } else if ("??????".equals(btnPasswordUpdate.getText())) {
                alertClick(v, passwordUpdate, message);
                passwordUpdate.setEnabled(false);
                passwordUpdate.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnPasswordUpdate.setText("??????");
                Msg.loginPassword = passwordUpdate.getText().toString();
            }
        }else if (v.getId() == R.id.back){
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            intent.putExtra("id", 4);
            startActivity(intent);
        }
    }

    public void alertClick(View v, EditText editText, String message) {
        //?????? ???????????????????????????????????????
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("??????????????????????????????");//??????????????????????????????
        builder.setCancelable(false);//???????????????
        //???????????????????????????
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userUtils.updateUsernameByid(id, usernameUpdate.getText().toString());
                userUtils.updatePasswordByid(id, passwordUpdate.getText().toString());
                dialog.cancel();
            }
        });
        //???????????????????????????
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.setText(message);
                dialog.cancel();
            }
        });

        builder.show();
    }
}
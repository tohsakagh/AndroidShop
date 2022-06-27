package com.example.shop.utils;


import android.widget.TextView;

import com.example.shop.bean.User;
import com.example.shop.dao.UserDao;


public class RegistUtils {

    private UserDao userDao;

    public RegistUtils() {
        userDao = new UserDao();
    }

    public boolean isRegist(String username, String password, TextView textView){
        String sql = "select * from user where username = ?";
        User user = userDao.querySingle(sql, User.class, username);
        if (user != null){
           textView.setText("用户名已注册");
            return false;
        }
        if (password.matches("^[0-9]*$")){
            textView.setText("密码过于简单");
            return false;
        }
        if (username.length() < 2 || username.length() > 20){
            textView.setText("用户名过短或过长");
            return false;
        }
        regist(username, password);
        return true;
    }

    public int regist(String username, String password){
        String sql = "insert into user values (null, ?, ?, null, null)";
        return userDao.update(sql, username, password);
    }
}

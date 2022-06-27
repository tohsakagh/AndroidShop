package com.example.shop.utils;

import com.example.shop.bean.Msg;
import com.example.shop.bean.User;
import com.example.shop.dao.UserDao;


public class LoginUtils {

    private static UserDao userDao;

    static {
        userDao = new UserDao();
    }

    public static boolean isExist(String username, String password){
        String sql = "select * from user where username = ? and password = ?";
        User user1 = userDao.querySingle(sql,User.class, username, password);
        if (user1 != null){
            Msg.isLogin = true;
            Msg.loginUsername = username;
            Msg.loginUserId = getIdByUsername(username);
            Msg.loginPassword = password;
            return true;
        }else {
            return false;
        }
    }

    public static int getIdByUsername(String username){
        String sql = "select id from user where username = ?";
        return (int) userDao.queryScalar(sql, username);
    }
}

package com.example.shop.service;

import com.example.shop.bean.Msg;
import com.example.shop.dao.UserDao;

public class UserService {

    private static UserDao userDao = new UserDao();

    public int getIdByUsername(String username){
        String sql = "select id from user where username = ?";
        return (int) userDao.queryScalar(sql, username);
    }

    public void updateUsernameByid(Integer id, String username){
        String sql = "update user set username = ? where id = ?";
        userDao.update(sql, username, id);
    }

    public void updatePasswordByid(Integer id, String password){
        String sql = "update user set password = ? where id = ?";
        userDao.update(sql, password, id);
    }

    public static String getPath(){
        String sql = "select path from user where username = ?";
        return (String) userDao.queryScalar(sql, Msg.loginUsername);
    }

    public static void updatePath(String path){
        String sql = "update user set path = ? where username = ?";
        userDao.update(sql, path, Msg.loginUsername);
    }

    public static boolean hasStore(String username){
        String sql = "select mystore from user where username = ?";
        String s = (String) userDao.queryScalar(sql, username);
        if (s != null){
            return true;
        }else {
            return false;
        }
    }

    public static void addStore(String storeName){
        String sql ="update user set mystore = ? where username = ?";
        userDao.update(sql, storeName, Msg.loginUsername);
    }

    public static String getStore(){
        String sql = "select mystore from user where username = ?";
        return (String) userDao.queryScalar(sql, Msg.loginUsername);
    }
}

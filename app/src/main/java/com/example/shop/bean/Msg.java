package com.example.shop.bean;

import java.util.HashMap;
import java.util.Map;

public class Msg {

    public static boolean isLogin = false;
    public static String loginUsername = null;
    public static Integer loginUserId = null;
    public static String loginPassword = null;
    public static Integer detailGoodsId = null;
    public static Map<Integer, Integer> map = new HashMap<>();

    public static void clear(){
        isLogin = false;
        loginUsername = null;
        loginPassword = null;
        loginUserId = null;
    }
}

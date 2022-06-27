package com.example.shop.utils;

import com.example.shop.bean.Cart;
import com.example.shop.dao.CartDao;

import java.util.List;

public class CartUtils {

    private static CartDao cartDao;

    static {
        cartDao = new CartDao();
    }

    public static Integer getCount(){
        String sql = "select count(*) from cart";
        Number number =  (Number) cartDao.queryScalar(sql);
        return number.intValue();
    }

    public static List<Cart> getCart(){
        String sql = "select * from cart";
        List<Cart> carts = cartDao.queryMultis(sql, Cart.class);
        return carts;
    }

}

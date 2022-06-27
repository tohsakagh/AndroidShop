package com.example.shop.service;

import com.example.shop.bean.Cart;
import com.example.shop.bean.Msg;
import com.example.shop.bean.Store;
import com.example.shop.dao.CartDao;

import java.util.List;

public class CartService {

    private static CartDao cartDao;

    static {
        cartDao = new CartDao();
    }

    public static void insert(Integer id, String name, String store){
        String sql = "insert into cart values (?, 1, ?, 0, ?)";
        cartDao.update(sql, id, name, store);
    }

    public static List<Cart> getAll(String name){
        String sql = "select * from cart where username = ?";
        return cartDao.queryMultis(sql, Cart.class, name);
    }

    public static void delete(Integer id, String username){
        String sql = "delete from  cart where id = ? and  username = ?";
        cartDao.update(sql, id, username);
    }

    public static List<Cart> getByStore(String name, String  store){
        String sql = "select * from cart where username = ? and store = ?";
        return cartDao.queryMultis(sql, Cart.class, name, store);
    }

    public static void updateStatus(Integer id, Boolean bool){
        String sql = "update cart set ischecked = ? where id = ? and username = ?";
        cartDao.update(sql, bool, id, Msg.loginUsername);
    }

    public static void updateCount(Integer count, Integer id){
        String sql = "update cart set count = ? where id = ? and username = ?";
        cartDao.update(sql, count, id, Msg.loginUsername);
    }

    public static Integer getCount(Integer id){
        String sql = "select count from cart where id = ? and username = ?";
        Number number = (Number) cartDao.queryScalar(sql, id, Msg.loginUsername);
        if (number != null) {
            return number.intValue();
        }else {
            return  0;
        }
    }

    public static void clearByStore(String store){
        String sql = "delete from cart where store = ? and username = ?";
        cartDao.update(sql, store, Msg.loginUsername);
    }

    public static Cart getCart(Integer id){
        String sql = "select * from cart where id = ? and username = ?";
        return cartDao.querySingle(sql, Cart.class, id, Msg.loginUsername);
    }
}

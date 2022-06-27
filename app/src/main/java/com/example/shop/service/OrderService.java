package com.example.shop.service;

import com.example.shop.bean.Msg;
import com.example.shop.bean.Order;
import com.example.shop.dao.OrderDao;

import java.util.List;

public class OrderService {

    private static OrderDao orderDao;

    static {
        orderDao = new OrderDao();
    }

    public static void insertToOrder(Integer id, Integer count){
        String sql = "insert into `order` values (?, ?, '未发货', ?)";
        orderDao.update(sql, id, Msg.loginUsername, count);
    }

    public static void deleteFromOrder(Integer id){
        String sql = "delete from `order` where id = ? and username = ?";
        orderDao.update(sql, id, Msg.loginUsername);
    }

    public static List<Order> getOrders(){
        String sql = "select * from `order` where username = ?";
        return orderDao.queryMultis(sql, Order.class, Msg.loginUsername);
    }
}

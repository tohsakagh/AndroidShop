package com.example.shop.service;

import com.example.shop.bean.Cart;
import com.example.shop.bean.Goods;
import com.example.shop.bean.Store;
import com.example.shop.dao.StoreDao;

import java.util.ArrayList;
import java.util.List;

public class StoreService {

    private static StoreDao storeDao;

    static {
        storeDao = new StoreDao();
    }

    public static Store getStore(String storeName){
        String sql = "select * from store where storename = ?";
        return storeDao.querySingle(sql, Store.class, storeName);
    }

    public static List<Store> getStores(){
        String sql = "select * from store";
        return storeDao.queryMultis(sql, Store.class);
    }

    public static List<Goods> getGoodsByStore(String store){
       return GoodsService.getGoodsByStore(store);
    }

    public static void updateStatus(Integer id, Boolean bool){
        String sql = "update store set ischecked = ? where id = ?";
        storeDao.update(sql, bool, id);
    }

    public static List<Cart> getGoodsByStoreAndIsCart(String name, String store){
        return CartService.getByStore(name, store);
    }

    public static List<Store> getStoresWithCart(String username){
        List<Store> stores = new ArrayList<>();
        List<Store> list = getStores();
        for (Store store : list){
            if (getGoodsByStoreAndIsCart(username, store.getStoreName()).size() != 0){
                stores.add(store);
            }
        }
        return stores;
    }

    public static  void clearIsCart(Store store){
        CartService.clearByStore(store.getStoreName());
    }

    public static void insert(String name, String url){
        String sql = "insert into store values (null, ?, 0, ?)";
        storeDao.update(sql, name, url);
    }
}

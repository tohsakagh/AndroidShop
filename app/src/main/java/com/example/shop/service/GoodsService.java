package com.example.shop.service;


import com.example.shop.bean.Goods;
import com.example.shop.bean.Store;
import com.example.shop.dao.GoodsDao;
import com.example.shop.dao.StoreDao;

import java.util.ArrayList;
import java.util.List;

public class GoodsService {

    private static GoodsDao goodsDao;

    static {
        goodsDao = new GoodsDao();
    }

    /**
     * 获取type总数
     * @return
     */
    public static int getTypes(){
        String sql = "SELECT COUNT(DISTINCT TYPE) FROM goods";
        Number count = (Number) goodsDao.queryScalar(sql);
        return count.intValue();
    }

    /**
     * 获取所有type
     * @return
     */
    public static List<String> getTypeNames(){
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT TYPE FROM goods";
        List<Object> types = goodsDao.queryTypes(sql, "type");
        for (Object type : types){
            list.add((String) type);
        }
        return list;
    }

    /**
     * 获取某个type的商品数量
     * @param type
     * @return
     */
    public static int getOneTypeCount(String type){
        String sql = null;
        Number count = 0;
        if (type != null) {
            sql = "SELECT COUNT(*) FROM goods where type = ? ";
            count = (Number) goodsDao.queryScalar(sql, type);
        }else {
            sql = "SELECT COUNT(*) FROM goods";
            count = (Number) goodsDao.queryScalar(sql);
        }
        return count.intValue();
    }


    public static List<Goods> getGoodsByType(String type){
        List<Goods> goods;
        String sql = null;
        if (type != null) {
            sql = "select * from goods where type  = ?";
            goods = goodsDao.queryMultis(sql, Goods.class, type);
        }else {
            sql = "select * from goods";
            goods = goodsDao.queryMultis(sql, Goods.class);
        }
        return goods;
    }

    public static List<Goods> getGoodsByStore(String store){
        List<Goods> goods;
        String sql = null;
        if (store != null) {
            sql = "select * from goods where store  = ?";
            goods = goodsDao.queryMultis(sql, Goods.class, store);
        }else {
            sql = "select * from goods";
            goods = goodsDao.queryMultis(sql, Goods.class);
        }
        return goods;
    }

    public static Goods getGoodsBuyId(Integer id){
        String sql = "select * from goods where id = ?";
        return goodsDao.querySingle(sql, Goods.class, id);
    }

    public static Store getStoreByGoods(Goods goods){
        String sql = "select * from store where storename = ?";
        StoreDao storeDao = new StoreDao();
        return storeDao.querySingle(sql, Store.class, goods.getStore());
    }

    public static void insert(String name, double price, String type, String url, String store, String description, String simpleName){
        String sql = "insert into goods values (null, ?, ?, ?, ?, ?, ?, ?)";
        goodsDao.update(sql, name, price, type, url, store, description, simpleName);
    }


    public static void delete(Integer id) {
        String sql = "delete from goods where id = ?";
        goodsDao.update(sql, id);
    }

    public static void updateNameAndPrice(Integer id, String simpleName, Double price) {
        String sql = "update goods set simpleName = ?, price = ? where id = ?";
        goodsDao.update(sql, simpleName, price, id);
    }

    /**
     * @param type
     * @param rule 0为升序 1 为降序
     * @return
     */
    public static List<Goods> getGoodsByTypeOrderByPrice(String type, Integer rule){
        List<Goods> goods;
        String sql = null;
        if (type != null) {
            if (rule == 0) {
                sql = "select * from goods where type  = ? order by price asc";
            }else {
                sql = "select * from goods where type  = ? order by price desc";
            }
            goods = goodsDao.queryMultis(sql, Goods.class, type);
        }else {
            if (rule == 0) {
                sql = "select * from goods order by price asc";
            }else {
                sql = "select * from goods order by price desc";
            }
            goods = goodsDao.queryMultis(sql, Goods.class);
        }
        return goods;
    }

    public static List<Goods> getGoodsByLike(String s){
        String sql = "select * from goods where goodsname like ? order by char_length(goodsname), goodsname";
        return goodsDao.queryMultis(sql, Goods.class, "%" + s + "%");
    }
}

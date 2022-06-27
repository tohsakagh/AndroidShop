package com.example.shop.service;

import com.example.shop.bean.Collection;
import com.example.shop.bean.Msg;
import com.example.shop.dao.CollectionDao;

import java.util.List;

public class CollectionService {

    private static CollectionDao collectionDao;

    static {
        collectionDao = new CollectionDao();
    }

    public static void insertToCollection(Integer id, String name){
        String sql = "insert into collection values (? , ?)";
        collectionDao.update(sql, id, name);
    }

    public static void deleteFromCollection(Integer id, String name){
        String sql = "delete from collection where id = ? and username = ?";
        collectionDao.update(sql, id, name);
    }

    public static boolean isInCollection(Integer id){
        String sql = "select * from collection where id = ? and username = ?";
        Collection collection = collectionDao.querySingle(sql, Collection.class, id, Msg.loginUsername);
        if (collection != null) return true;
        else  return  false;
    }

    public static List<Collection> getCollections(String name){
        String sql = "select * from collection where username = ?";
        return collectionDao.queryMultis(sql, Collection.class, name);
    }
}

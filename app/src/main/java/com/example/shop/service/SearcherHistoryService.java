package com.example.shop.service;

import com.example.shop.bean.Msg;
import com.example.shop.bean.SearcherHistory;
import com.example.shop.dao.SearcherHistoryDao;

import java.util.List;

public class SearcherHistoryService {

    private static SearcherHistoryDao searcherHistoryDao;

    static {
        searcherHistoryDao = new SearcherHistoryDao();
    }

    public static List<SearcherHistory> getAllHistories(){
        String sql = "SELECT * FROM history where username = ? ORDER BY id DESC LIMIT 8";
        return searcherHistoryDao.queryMultis(sql,SearcherHistory.class, Msg.loginUsername);
    }

    public static void deleteAll(){
        String sql = "delete  from history where username = ?";
        searcherHistoryDao.update(sql, Msg.loginUsername);
    }

    public static void deleteOne(String s){
        String sql = "delete  from history where searcherstring = ? and username = ?";
        searcherHistoryDao.update(sql, s, Msg.loginUsername);
    }

    public static void insert(String s) {
        String sql = null;
        sql = "select * from history where searcherstring = ? and username = ?";
        SearcherHistory searcherHistory = searcherHistoryDao.querySingle(sql, SearcherHistory.class, s, Msg.loginUsername);
        if (searcherHistory != null) {
            deleteOne(s);
        }
        sql = "insert into history values (?, ?, null)";
        searcherHistoryDao.update(sql, s, Msg.loginUsername);
    }
}

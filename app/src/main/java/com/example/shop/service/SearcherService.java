package com.example.shop.service;

import com.example.shop.bean.Goods;
import com.example.shop.bean.KeyWord;
import com.example.shop.dao.KeyWordDao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearcherService {

    private static KeyWordDao keyWordDao;

    static {
        keyWordDao = new KeyWordDao();
    }

    public static List<KeyWord> getKeyWordList() {
        String sql = "select * from keyword";
        return keyWordDao.queryMultis(sql, KeyWord.class);
    }

    public static List<String> getKeyWords(String input) {
        List<String> list = new ArrayList<>();
        String sql = "select word from keyword where  word like ? order by locate(?, word) asc";
        List<Object> words = keyWordDao.queryTypes(sql, "word", "%" + input + "%", input);
        for (Object o : words) {
            list.add((String) o);
        }
        return list;
    }

    public static List<Goods> getGoodsByKeyWord(String keyword) {
        List<Goods> list = new ArrayList<>();
        String sql = "select * from keyword where word = ?";
        KeyWord keyWord = keyWordDao.querySingle(sql, KeyWord.class, keyword);
        if (keyWord != null) {
            String idlist = keyWord.getIds();
            String[] ids = idlist.split(",");
            for (String id : ids) {
                list.add(GoodsService.getGoodsBuyId(Integer.parseInt(id)));
            }
        }

        return list;
    }

    /**
     *
     * @param keyword
     * @param rule 0升序1降序
     * @return
     */
    public static List<Goods> getGoodsByKeyWordOrderByPrice(String keyword, Integer rule) {
        List<Goods> list = new ArrayList<>();
        String sql;
        sql = "select * from keyword where word = ?";
        KeyWord keyWord = keyWordDao.querySingle(sql, KeyWord.class, keyword);
        if (keyWord != null) {
            String idlist = keyWord.getIds();
            String[] ids = idlist.split(",");
            for (String id : ids) {
                list.add(GoodsService.getGoodsBuyId(Integer.parseInt(id)));
            }
        }

        if (rule == 0){
            list.sort((o1, o2) -> (int) (o1.getPrice() - o2.getPrice()));
        }else {
            list.sort((o1, o2) -> (int) (o2.getPrice() - o1.getPrice()));
        }
        return list;
    }

    public static void insertKeyWord(String keyword, Integer id){
        String sql = "select * from keyword where word = ?";
        KeyWord keyWord = keyWordDao.querySingle(sql, KeyWord.class, keyword);
        if (keyWord == null){
            sql = "insert into keyword values (?, ?)";
            keyWordDao.update(sql, keyword, id);
        }else {
            String ids = keyWord.getIds();
            ids  = ids + "," + id;
            sql = "update keyword set ids = ? where word = ?";
            keyWordDao.update(sql, ids, keyword);
        }
    }


}

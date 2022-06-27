package com.example.shop.bean;

public class SearcherHistory {

    private String searcherString;
    private String userName;
    private Integer id;

    public String getSearcherString() {
        return searcherString;
    }

    public void setSearcherString(String searcherString) {
        this.searcherString = searcherString;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SearcherHistory{" +
                "searcherString='" + searcherString + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}

package com.example.shop.bean;

public class KeyWord {

    private String word;
    private String ids;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "KeyWord{" +
                "word='" + word + '\'' +
                ", ids='" + ids + '\'' +
                '}';
    }
}

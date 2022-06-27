package com.example.shop.bean;



import com.example.shop.service.StoreService;

import java.util.List;

public class Store {

    private Integer id;
    private String storeName;
    private Integer isChecked;
    private String url;
    private List<Goods> goods;

    public Store() {
    }

    public Store(Integer id, String storeName) {
        this.id = id;
        this.storeName = storeName;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
        if (isChecked == 0){
            StoreService.updateStatus(this.id, false);
        }else {
            StoreService.updateStatus(id, true);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Goods> getGoods() {
        return StoreService.getGoodsByStore(this.getStoreName());
    }

    public void setGoods(List<Goods> goods) {
        this.goods = StoreService.getGoodsByStore(this.getStoreName());
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", storeName='" + storeName + '\'' +
                ", isChecked=" + isChecked +
                ", url='" + url + '\'' +
                ", goods=" + goods +
                '}';
    }

    public List<Cart> getGoodsIsCart(String name){
        return StoreService.getGoodsByStoreAndIsCart(name ,this.storeName);
    }

    public void clearGoodsIsCart(){
        StoreService.clearIsCart(this);
    }
}

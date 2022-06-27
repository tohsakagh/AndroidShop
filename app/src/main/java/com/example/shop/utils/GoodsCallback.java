package com.example.shop.utils;

public interface GoodsCallback {

    /**
     * 选中店铺
     * @param shopId 店铺id
     * @param state 是否选中
     */
    void checkedStore(int shopId, Integer state);

    /**
     * 计算价格
     */
    void calculationPrice();
}

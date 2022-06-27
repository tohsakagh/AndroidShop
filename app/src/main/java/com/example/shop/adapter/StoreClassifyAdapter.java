package com.example.shop.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.shop.R;
import com.example.shop.bean.Store;

import java.util.List;

public class StoreClassifyAdapter extends BaseQuickAdapter<Store, BaseViewHolder> {

    public StoreClassifyAdapter(int layoutResId, @Nullable List<Store> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Store item) {
        helper.setText(R.id.item_store, item.getStoreName());
    }
}

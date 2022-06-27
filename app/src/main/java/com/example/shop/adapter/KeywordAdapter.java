package com.example.shop.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.shop.R;

import java.util.List;

public class KeywordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public KeywordAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_keyword, item);

        helper.addOnClickListener(R.id.item_keyword);
    }
}

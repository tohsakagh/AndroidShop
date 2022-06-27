package com.example.shop.adapter;

import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.shop.R;
import com.example.shop.bean.SearcherHistory;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

public class SearcherHistoryAdapter extends BaseQuickAdapter<SearcherHistory, BaseViewHolder> {


    public SearcherHistoryAdapter(int layoutResId, @Nullable List<SearcherHistory> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearcherHistory item) {
        helper.setText(R.id.text_history, item.getSearcherString());



    }
}

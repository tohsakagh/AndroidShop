package com.example.shop.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.shop.R;
import com.example.shop.bean.Goods;
import com.example.shop.utils.BitmapUtils;

import java.util.List;

public class GoodsTwoListAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    private Context context;

    public GoodsTwoListAdapter(int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        helper.setText(R.id.tv_goods_name, item.getSimpleName())
                .setText(R.id.tv_goods_price, "￥" + item.getPrice() + "")
                .setText(R.id.tv_goods_store, item.getStore())
                .setText(R.id.tv_goods_description, item.getDescription());

        ImageView goodImg = helper.getView(R.id.iv_goods);
        if ("https".equals(item.getUrl().substring(0, 5))) {
            Glide.with(mContext).load(item.getUrl()).into(goodImg);
        } else {
            goodImg.setImageBitmap(BitmapUtils.stringToBitmap(item.getUrl()));
        }
    }
}

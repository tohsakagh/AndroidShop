package com.example.shop.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.shop.R;
import com.example.shop.bean.Collection;
import com.example.shop.bean.Goods;
import com.example.shop.service.GoodsService;
import com.example.shop.utils.BitmapUtils;

import java.util.List;

public class CollectionAdapter extends BaseQuickAdapter<Collection, BaseViewHolder> {

    public CollectionAdapter(int layoutResId, @Nullable List<Collection> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Collection item) {
        Goods goods= GoodsService.getGoodsBuyId(item.getId());
        helper.setText(R.id.tv_goods_name, goods.getSimpleName())
              .setText(R.id.tv_goods_price, "￥" + goods.getPrice() + "")
                .setText(R.id.tv_goods_description, goods.getDescription());

        helper.addOnClickListener(R.id.tv_add_cart).addOnClickListener(R.id.btnDelete).addOnClickListener(R.id.collection_goods);
        ImageView goodImg = helper.getView(R.id.iv_goods);
        if ("https".equals(goods.getUrl().substring(0,5))) {
            Glide.with(mContext).load(goods.getUrl()).into(goodImg);
        }else {
            goodImg.setImageBitmap(BitmapUtils.stringToBitmap(goods.getUrl()));
        }
    }
}

package com.example.shop.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.shop.R;
import com.example.shop.bean.Goods;
import com.example.shop.utils.BitmapUtils;

import java.util.List;

public class MyStoreGoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    public MyStoreGoodsAdapter(int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        helper.setText(R.id.tv_goods_name, item.getSimpleName())
                .setText(R.id.tv_goods_price, "￥" + item.getPrice() + "")
                .setText(R.id.tv_goods_description, item.getDescription());

        helper.addOnClickListener(R.id.btnUpdate).addOnClickListener(R.id.btnDelete);
        ImageView goodImg = helper.getView(R.id.iv_goods);
        goodImg.setImageBitmap(BitmapUtils.stringToBitmap(item.getUrl()));
    }
}

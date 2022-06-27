package com.example.shop.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.shop.R;
import com.example.shop.bean.Cart;
import com.example.shop.bean.Goods;
import com.example.shop.service.CartService;
import com.example.shop.service.GoodsService;
import com.example.shop.utils.BitmapUtils;

import java.util.List;

public class CartGoodsAdapter extends BaseQuickAdapter<Cart, BaseViewHolder> {

    public CartGoodsAdapter(int layoutResId, @Nullable List<Cart> data) {
        super(layoutResId, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void convert(BaseViewHolder helper, Cart item) {
        Goods goods = GoodsService.getGoodsBuyId(item.getId());
        helper.setText(R.id.tv_goods_name, goods.getGoodsName())
                .setText(R.id.tv_goods_price, "￥" + goods.getPrice() + "")
                .setText(R.id.tv_goods_num, CartService.getCount(item.getId()) + "");


        ImageView checkedGoods = helper.getView(R.id.iv_checked_goods);
        //判断商品是否选中
        if (item.getIschecked() == 1) {
            checkedGoods.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
        } else {
            checkedGoods.setImageDrawable(mContext.getDrawable(R.drawable.ic_check));
        }
        //添加点击事件
        helper.addOnClickListener(R.id.iv_checked_goods)//选中商品
                .addOnClickListener(R.id.tv_increase_goods_num)//增加商品
                .addOnClickListener(R.id.tv_reduce_goods_num)//减少商品
                .addOnClickListener(R.id.tv_goods_name)
                .addOnClickListener(R.id.iv_goods)
                .addOnClickListener(R.id.btnCollction)
                .addOnClickListener(R.id.btnDelete);

        ImageView goodImg = helper.getView(R.id.iv_goods);
        if ("https".equals(goods.getUrl().substring(0,5))) {
            Glide.with(mContext).load(goods.getUrl()).into(goodImg);
        }else {
            goodImg.setImageBitmap(BitmapUtils.stringToBitmap(goods.getUrl()));
        }
    }
}



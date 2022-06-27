package com.example.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.shop.R;
import com.example.shop.activity.GoodsDetailActivity;
import com.example.shop.activity.MainActivity;
import com.example.shop.bean.Cart;
import com.example.shop.bean.Goods;
import com.example.shop.bean.Msg;
import com.example.shop.bean.Store;
import com.example.shop.fragment.CartFragment;
import com.example.shop.service.CartService;
import com.example.shop.service.CollectionService;
import com.example.shop.utils.GoodsCallback;

import java.util.List;

public class StoreAdapter extends BaseQuickAdapter<Store, BaseViewHolder> {

    private RecyclerView rvGood;
    private GoodsCallback goodsCallback;
    private List<Store> stores;
    private List<Cart> goodsList;
    private CartGoodsAdapter cartGoodsAdapter;

    private Context context;

    public StoreAdapter(int layoutResId, @Nullable List<Store> data, GoodsCallback goodsCallback, Context context) {
        super(layoutResId, data);
        this.goodsCallback = goodsCallback;
        this.context = context;
        stores = data;
    }

    /**
     * 控制店铺是否选中
     */
    private void controlStore(Store store) {
        int num = 0;
        for (Cart cart : store.getGoodsIsCart(Msg.loginUsername)) {
            if (cart.getIschecked() == 1) {
                ++num;
            }
        }
        if (num == store.getGoodsIsCart(Msg.loginUsername).size()) {
            //全选中  传递需要选中的店铺的id过去
            goodsCallback.checkedStore(store.getId(), 1);
        } else {
            goodsCallback.checkedStore(store.getId(), 0);
        }
    }

    /**
     * 控制商品是否选中
     */
    @SuppressLint("NotifyDataSetChanged")
    public void controlGoods(int id, Integer state) {
        //根据店铺id选中该店铺下所有商品
        for (Store store : stores) {
            //店铺id等于传递过来的店铺id  则选中该店铺下所有商品
            if (store.getId() == id) {
                for (Cart cart : store.getGoodsIsCart(Msg.loginUsername)) {
                    cart.setIschecked(state);
                    updateStatus(cart.getId(), state);
                    //刷新
                    notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 修改商品数量  增加或者减少
     * @param cart
     * @param state  true增加 false减少
     */
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void updateGoodsNum(Cart cart, CartGoodsAdapter cartGoodsAdapter, boolean state) {
        //其实商品应该还有一个库存值或者其他的限定值，我这里写一个假的库存值为10
        int inventory = 10;
        int count = cart.getCount();

        if(state){
            if (count >= inventory){
                Toast.makeText(mContext,"商品数量不可超过库存值~",Toast.LENGTH_SHORT).show();
                return;
            }
            count++;
        }else {
            if (count <= 1){
                Toast.makeText(mContext,"已是最低商品数量~",Toast.LENGTH_SHORT).show();
                return;
            }
            count--;
        }
        cart.setCount(count);//设置商品数量
        CartService.updateCount(count, cart.getId());
        //刷新适配器
        cartGoodsAdapter.notifyDataSetChanged();
        //计算商品价格
        goodsCallback.calculationPrice();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void convert(BaseViewHolder helper, Store item) {
        rvGood = helper.getView(R.id.rv_goods);
        helper.setText(R.id.tv_store_name, item.getStoreName());


        ImageView checkedStore = helper.getView(R.id.iv_checked_store);
        if (item.getIsChecked() == 1) {
            checkedStore.setImageDrawable(mContext.getDrawable(R.drawable.ic_checked));
        } else {
            checkedStore.setImageDrawable(mContext.getDrawable(R.drawable.ic_check));
        }
        //点击事件
        helper.addOnClickListener(R.id.iv_checked_store).addOnClickListener(R.id.tv_store_name);//选中店铺

        goodsList = item.getGoodsIsCart(Msg.loginUsername);
        cartGoodsAdapter = new CartGoodsAdapter(R.layout.item_cart_goods, goodsList);
        rvGood.setLayoutManager(new LinearLayoutManager(mContext));
        rvGood.setAdapter(cartGoodsAdapter);

        //商品item中的点击事件
        cartGoodsAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = null;
                Cart cart = item.getGoodsIsCart(Msg.loginUsername).get(position);
                switch (view.getId()) {
                    case R.id.iv_checked_goods://选中商品
                        //如果已选中则取消选中，未选中则选中
                        if (cart.getIschecked() == 0) {
                            cart.setIschecked(1);
                            updateStatus(cart.getId(), 1);
                        } else {
                            cart.setIschecked(0);
                            updateStatus(cart.getId(), 0);
                        }
                        //刷新适配器
                        cartGoodsAdapter.notifyDataSetChanged();
                        controlStore(item);
                        break;
                    case R.id.tv_increase_goods_num://增加商品数量
                        updateGoodsNum(cart, cartGoodsAdapter, true);
                        break;
                    case R.id.tv_reduce_goods_num://减少商品数量
                        updateGoodsNum(cart, cartGoodsAdapter, false);
                        break;
                    case R.id.tv_goods_name:
                    case R.id.iv_goods:
                        intent = new Intent(view.getContext(), GoodsDetailActivity.class);
                        break;
                    case R.id.btnCollction:
                        CollectionService.insertToCollection(cart.getId(), Msg.loginUsername);
                        delete(cart);
                        break;
                    case R.id.btnDelete:
                       delete(cart);
                        break;
                    default:
                        break;
                }
                if (intent != null){
                    intent.putExtra("id", cart.getId());
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    private void delete(Cart cart){
        CartService.delete(cart.getId(), Msg.loginUsername);
        goodsList.remove(cart);
        cartGoodsAdapter.notifyDataSetChanged();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("id", 3);
        context.startActivity(intent);
    }

    private void updateStatus(Integer id, Integer status){
        if (status == 0){
            CartService.updateStatus(id, false);
        }else {
            CartService.updateStatus(id, true);
        }
    }
}


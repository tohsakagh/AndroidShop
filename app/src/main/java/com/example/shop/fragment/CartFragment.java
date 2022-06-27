package com.example.shop.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.activity.MainActivity;
import com.example.shop.activity.OrderActivity;
import com.example.shop.activity.StoreActivity;
import com.example.shop.adapter.StoreAdapter;
import com.example.shop.bean.Cart;
import com.example.shop.bean.Goods;
import com.example.shop.bean.Msg;
import com.example.shop.bean.Store;
import com.example.shop.service.CartService;
import com.example.shop.service.CollectionService;
import com.example.shop.service.GoodsService;
import com.example.shop.service.OrderService;
import com.example.shop.service.StoreService;
import com.example.shop.utils.GoodsCallback;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends BasePageTitleFragment implements GoodsCallback, View.OnClickListener {

    private RecyclerView rvStore;
    private List<Store> mList = new ArrayList<>();
    private StoreAdapter storeAdapter;

    private ImageView tvBack;
    private TextView tvEdit;//编辑
    private ImageView ivCheckedAll;//全选
    private TextView tvTotal;//合计价格
    private TextView tvSettlement;//结算
    private LinearLayout layEdit;//编辑底部布局
    private TextView tvCollectGoods;//收藏商品
    private TextView tvDeleteGoods;//删除商品

    private boolean isEdit = false;//是否编辑
    private boolean isAllChecked = false;//是否全选
    private boolean isHaveGoods = false;//购物车是否有商品

    private List<Integer> storeIdList = new ArrayList<>();//店铺列表
    private double totalPrice = 0.00;//商品总价
    private int totalCount = 0;//商品总数量

    private AlertDialog dialog;//弹窗

    private boolean isGetData = false;

    @SuppressLint("NotifyDataSetChanged")
    @androidx.annotation.Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;

            storeAdapter.notifyDataSetChanged();
            //   这里可以做网络请求或者需要的数据刷新操作
        } else {

            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }


    @Override
    protected View initView() {
        setTitleIcon("购物车", true ,false, false);//标题栏的显示
        View cartFragment = View.inflate(getContext(), R.layout.fragment_cart, null);

        mList.addAll(StoreService.getStoresWithCart(Msg.loginUsername));
        rvStore = cartFragment.findViewById(R.id.rv_store);

        if (mList.size() != 0) {
            isHaveGoods = true;
        }
        RelativeLayout relativeLayout = cartFragment.findViewById(R.id.empty);
        relativeLayout.setOnClickListener(this);
        if (!isHaveGoods) {
            relativeLayout.setVisibility(View.VISIBLE);
            rvStore.setVisibility(View.GONE);
            TextView textView = cartFragment.findViewById(R.id.goto_home);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("id", 1);
                    startActivity(intent);
                }
            });
        } else {
            relativeLayout.setVisibility(View.GONE);
            rvStore.setVisibility(View.VISIBLE);
        }

        cartFragment.findViewById(R.id.lay_bottom).setOnClickListener(this);

        storeAdapter = new StoreAdapter(R.layout.item_store, mList, this, getContext());
        rvStore.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStore.setAdapter(storeAdapter);


        //店铺点击
        storeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Store store = mList.get(position);
                storeAdapter.notifyDataSetChanged();
                if (view.getId() == R.id.iv_checked_store) {
                    if (store.getIsChecked() == 0) {
                        store.setIsChecked(1);
                        storeAdapter.controlGoods(store.getId(), 1);
                        if (!storeIdList.contains(store.getId())) {
                            storeIdList.add(store.getId());
                        }
                    } else {
                        store.setIsChecked(0);
                        storeAdapter.controlGoods(store.getId(), 0);
                        if (storeIdList.contains(store.getId())) {
                            storeIdList.remove((Integer) store.getId());
                        }
                    }
                } else if (view.getId() == R.id.tv_store_name) {
                    Intent intent = new Intent(getContext(), StoreActivity.class);
                    intent.putExtra("name", store.getStoreName());
                    getActivity().startActivity(intent);
                }
                controllerAllChecked();
            }
        });

        tvBack = cartFragment.findViewById(R.id.tv_back);
        tvEdit = cartFragment.findViewById(R.id.tv_edit);
        ivCheckedAll = cartFragment.findViewById(R.id.iv_checked_all);
        tvTotal = cartFragment.findViewById(R.id.tv_total);
        tvSettlement = cartFragment.findViewById(R.id.tv_settlement);
        layEdit = cartFragment.findViewById(R.id.lay_edit);
        tvCollectGoods = cartFragment.findViewById(R.id.tv_collect_goods);
        tvDeleteGoods = cartFragment.findViewById(R.id.tv_delete_goods);

        tvBack.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        ivCheckedAll.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
        tvCollectGoods.setOnClickListener(this);
        tvDeleteGoods.setOnClickListener(this);

        return cartFragment;
    }

    @Override
    protected void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    @Override
    public void checkedStore(int id, Integer state) {
        for (Store store : mList) {
            //遍历
            if (id == store.getId()) {
                store.setIsChecked(state);
                storeAdapter.notifyDataSetChanged();
                if (!storeIdList.contains(id) && state == 1) {
                    storeIdList.add(id);
                } else {
                    if (storeIdList.contains(id)) {
                        storeIdList.remove((Integer) id);
                    }
                }
            }
        }
        controllerAllChecked();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void calculationPrice() {
        totalPrice = 0.00;
        totalCount = 0;
        for (int i = 0; i < mList.size(); i++) {
            Store store = mList.get(i);
            for (int j = 0; j < store.getGoodsIsCart(Msg.loginUsername).size(); j++) {
                Cart cart = store.getGoodsIsCart(Msg.loginUsername).get(j);
                if (cart.getIschecked() == 1) {
                    totalCount += cart.getCount();
                    Goods goods = GoodsService.getGoodsBuyId(cart.getId());
                    totalPrice += goods.getPrice() * cart.getCount();
                }
            }
        }
        tvTotal.setText("￥" + totalPrice);
        tvSettlement.setText("结算(" + totalCount + ")");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void controllerAllChecked() {
        if (storeIdList.size() == mList.size() && mList.size() != 0) {
            ivCheckedAll.setImageDrawable(getContext().getDrawable(R.drawable.ic_checked));
            isAllChecked = true;
        } else {
            ivCheckedAll.setImageDrawable(getContext().getDrawable(R.drawable.ic_check));
            isAllChecked = false;
        }
        calculationPrice();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_back:
                getActivity().finish();
                break;
            case R.id.tv_edit://编辑
                if (!isHaveGoods) {
                    showMsg("当前购物车空空如也，区逛一逛商城选择你想要的商品把");
                } else {
                    if (isEdit) {
                        tvEdit.setText("编辑");
                        layEdit.setVisibility(View.GONE);
                        isEdit = false;
                    } else {
                        tvEdit.setText("完成");
                        layEdit.setVisibility(View.VISIBLE);
                        isEdit = true;
                    }
                }
                break;
            case R.id.iv_checked_all://全选
                if (!isHaveGoods) {
                    showMsg("当前购物车空空如也，区逛一逛商城选择你想要的商品把");
                } else {
                    if (isAllChecked) {
                        //取消全选
                        isSelectAllStore(0);
                        ivCheckedAll.setImageDrawable(getContext().getDrawable(R.drawable.ic_check));
                    } else {
                        //全选
                        isSelectAllStore(1);
                        ivCheckedAll.setImageDrawable(getContext().getDrawable(R.drawable.ic_checked));
                    }
                }
                break;
            case R.id.tv_settlement://结算
                if (!isHaveGoods) {
                    showMsg("当前购物车空空如也，去逛一逛商城选择你想要的商品把");
                }
                if (totalCount == 0) {
                    showMsg("请选择要结算的商品");
                    return;
                }
                //弹窗
                dialog = new AlertDialog.Builder(getContext())
                        .setMessage("总计:" + totalCount + "种商品，" + totalPrice + "元， 你确定要结算吗？")
                        .setPositiveButton("确定", (dialog, which) -> submitOrder())
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .create();
                dialog.show();
                break;
            case R.id.tv_delete_goods://删除
                if (totalCount == 0) {
                    showMsg("请选择要删除的商品");
                    return;
                }
                //弹窗
                dialog = new AlertDialog.Builder(getContext())
                        .setMessage("确定要删除所选商品吗?")
                        .setPositiveButton("确定", (dialog, which) -> deleteGoods())
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .create();
                dialog.show();
                break;
            case R.id.tv_collect_goods://收藏
                collection();
                showMsg("收藏成功");
                break;
            default:
                break;
        }
    }

    /**
     * 删除商品
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NotifyDataSetChanged")
    private void deleteGoods() {
        //店铺列表
        for (Store store : mList) {
            if (store.getIsChecked() == 1) {
                System.out.println(store.getGoodsIsCart(Msg.loginUsername));
                store.clearGoodsIsCart();
                store.setIsChecked(0);
            } else {
                List<Cart> goodsList = store.getGoodsIsCart(Msg.loginUsername);
                for (Cart cart : goodsList) {
                    if (cart.getIschecked() == 1) {
                        CartService.delete(cart.getId(), Msg.loginUsername);
                    }
                }
            }
        }
        mList.clear();
        mList.addAll(StoreService.getStoresWithCart(Msg.loginUsername));
        storeIdList.clear();
        controllerAllChecked();
        //改变界面UI
        tvEdit.setText("编辑");
        layEdit.setVisibility(View.GONE);
        isEdit = false;
        //刷新数据
        if (mList.size() == 0) {
            isHaveGoods = false;
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("id", 3);
            startActivity(intent);
        }
        storeAdapter.notifyDataSetChanged();
    }

    private void collection(){
        //店铺列表
        for (Store store : mList) {
            if (store.getIsChecked() == 1) {
                for (Cart cart: store.getGoodsIsCart(Msg.loginUsername)){
                    CollectionService.insertToCollection(cart.getId(), Msg.loginUsername);
                    CartService.delete(cart.getId(), Msg.loginUsername);
                }
                store.clearGoodsIsCart();
                store.setIsChecked(0);
            } else {
                List<Cart> goodsList = store.getGoodsIsCart(Msg.loginUsername);
                for (Cart cart : goodsList) {
                    if (cart.getIschecked() == 1) {
                        CollectionService.insertToCollection(cart.getId(), Msg.loginUsername);
                        CartService.delete(cart.getId(), Msg.loginUsername);
                    }
                }
            }
        }
        mList.clear();
        mList.addAll(StoreService.getStoresWithCart(Msg.loginUsername));
        storeIdList.clear();
        controllerAllChecked();
        //改变界面UI
        tvEdit.setText("编辑");
        layEdit.setVisibility(View.GONE);
        isEdit = false;
        //刷新数据
        if (mList.size() == 0) {
            isHaveGoods = false;
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("id", 3);
            startActivity(intent);
        }
        storeAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NotifyDataSetChanged")
    private void submitOrder(){
        //店铺列表
        for (Store store : mList) {
            if (store.getIsChecked() == 1) {
                for (Cart cart : store.getGoodsIsCart(Msg.loginUsername)){
                    OrderService.insertToOrder(cart.getId(), cart.getCount());
                }
                store.clearGoodsIsCart();
                store.setIsChecked(0);
            } else {
                List<Cart> goodsList = store.getGoodsIsCart(Msg.loginUsername);
                for (Cart cart : goodsList) {
                    if (cart.getIschecked() == 1) {
                        CartService.delete(cart.getId(), Msg.loginUsername);
                        OrderService.insertToOrder(cart.getId(), cart.getCount());
                    }
                }
            }
        }
        mList.clear();
        mList.addAll(StoreService.getStoresWithCart(Msg.loginUsername));
        storeIdList.clear();
        controllerAllChecked();
        //改变界面UI
        tvEdit.setText("编辑");
        layEdit.setVisibility(View.GONE);
        isEdit = false;
        //刷新数据
        if (mList.size() == 0) {
            isHaveGoods = false;
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("id", 3);
            startActivity(intent);
        }
        storeAdapter.notifyDataSetChanged();
        startActivity(new Intent(getContext(), OrderActivity.class));
    }

    private void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 是否全选
     *
     * @param state 状态
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void isSelectAllStore(Integer state) {
        //修改背景
        ivCheckedAll.setImageDrawable(getContext().getDrawable(state == 1 ? R.drawable.ic_checked : R.drawable.ic_check));

        for (Store store : mList) {
            //商品是否选中
            storeAdapter.controlGoods(store.getId(), state);
            //店铺是否选中
            checkedStore(store.getId(), state);
        }
        isAllChecked = state != 0;
    }

}


package com.example.shop.fragment;

import android.content.Intent;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.activity.StoreActivity;
import com.example.shop.activity.TypeGoodsActivity;
import com.example.shop.adapter.GoodsTypeAdapter;
import com.example.shop.adapter.StoreClassifyAdapter;
import com.example.shop.bean.Store;
import com.example.shop.service.GoodsService;
import com.example.shop.service.StoreService;

public class ClassifyFragment extends BasePageTitleFragment{

    private TextView tvChooseType;
    private TextView tvChooseStore;
    private ImageView ivChooseType;
    private ImageView ivChooseStore;
    @Override
    protected View initView() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setTitleIcon("商品分类",true, true, false);//标题栏的显示
        View classifyFragment = View.inflate(getContext(), R.layout.fragment_classify, null);

        tvChooseType = classifyFragment.findViewById(R.id.tv_choose_type);
        tvChooseStore = classifyFragment.findViewById(R.id.tv_choose_store);
        ivChooseType = classifyFragment.findViewById(R.id.iv_choose_type);
        ivChooseStore = classifyFragment.findViewById(R.id.iv_choose_store);

        RecyclerView recyclerView= classifyFragment.findViewById(R.id.rv_type);
        GoodsTypeAdapter goodsTypeAdapter = new GoodsTypeAdapter(R.layout.item_type, GoodsService.getTypeNames());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(goodsTypeAdapter);
        goodsTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String type = GoodsService.getTypeNames().get(position);//这句是关键，通过postion下标拿到你list集合中的数据，list集合中有你的数据，.出来去接受就行了。
                Intent intent =   new Intent(getContext(), TypeGoodsActivity.class);;
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView1 = classifyFragment.findViewById(R.id.rv_store);
        StoreClassifyAdapter storeClassifyAdapter = new StoreClassifyAdapter(R.layout.item_class_store, StoreService.getStores());
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView1.setAdapter(storeClassifyAdapter);
        storeClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Store store = StoreService.getStores().get(position);
                Intent intent = new Intent(getContext(), StoreActivity.class);
                intent.putExtra("name", store.getStoreName());
                startActivity(intent);
            }
        });

        tvChooseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView1.setVisibility(View.GONE);
                ivChooseType.setVisibility(View.VISIBLE);
                ivChooseStore.setVisibility(View.GONE);
            }
        });

        tvChooseStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.VISIBLE);
                ivChooseType.setVisibility(View.GONE);
                ivChooseStore.setVisibility(View.VISIBLE);
            }
        });
        return classifyFragment ;
    }

    @Override
    protected void initData() {

    }

}

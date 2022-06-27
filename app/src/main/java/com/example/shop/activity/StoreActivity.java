package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.adapter.GoodsAdapter;
import com.example.shop.bean.Goods;
import com.example.shop.bean.Store;
import com.example.shop.service.GoodsService;
import com.example.shop.service.StoreService;
import com.example.shop.utils.BitmapUtils;

import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private RecyclerView rvGoods;
    private GoodsAdapter goodsAdapter;
    private List<Goods> goodsList;

    private ImageView storePhoto;
    private TextView storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        String name = getIntent().getStringExtra("name");
        goodsList = GoodsService.getGoodsByStore(name);

        storePhoto = findViewById(R.id.tv_store_photo);
        storeName = findViewById(R.id.tv_store_name);
        storeName.setText(name);

        Store store = StoreService.getStore(name);

        if ("https".equals(store.getUrl().substring(0,5))) {
            Glide.with(this).load(store.getUrl()).into(storePhoto);
        }else {
            storePhoto.setImageBitmap(BitmapUtils.stringToBitmap(store.getUrl()));
        }

        goodsAdapter = new GoodsAdapter(R.layout.item_goods, goodsList);
        rvGoods = findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new LinearLayoutManager(this));
        rvGoods.setAdapter(goodsAdapter);

        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StoreActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", goodsList.get(position).getId());
                startActivity(intent);
            }
        });
    }
}

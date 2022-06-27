package com.example.shop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.adapter.CollectionAdapter;
import com.example.shop.bean.Cart;
import com.example.shop.bean.Collection;
import com.example.shop.bean.Goods;
import com.example.shop.bean.Msg;
import com.example.shop.service.CartService;
import com.example.shop.service.CollectionService;
import com.example.shop.service.GoodsService;

import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private RecyclerView rvCollection;
    private List<Collection> collections;
    private CollectionAdapter collectionAdapter;

    private TextView title;
    private ImageView back;

    private boolean isHaveCollection = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        collections = CollectionService.getCollections(Msg.loginUsername);
        rvCollection = findViewById(R.id.rv_collection);

        if (collections.size() != 0){
            isHaveCollection = true;
        }

        RelativeLayout relativeLayout = findViewById(R.id.empty);
        if (!isHaveCollection){
            relativeLayout.setVisibility(View.VISIBLE);
            rvCollection.setVisibility(View.GONE);
        }else {
            relativeLayout.setVisibility(View.GONE);
            rvCollection.setVisibility(View.VISIBLE);
        }

        collectionAdapter = new CollectionAdapter(R.layout.item_collection, collections);
        rvCollection.setLayoutManager(new LinearLayoutManager(this));
        rvCollection.setAdapter(collectionAdapter);

        title = findViewById(R.id.title);
        back = findViewById(R.id.back);

        title.setText("我的收藏(" + collections.size() + ")");
        back.setOnClickListener(v -> CollectionActivity.this.finish());

        collectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Collection collection = collections.get(position);

                if (view.getId() == R.id.tv_add_cart){
                    Cart cart = CartService.getCart(collection.getId());
                    Goods goods = GoodsService.getGoodsBuyId(collection.getId());
                    if (cart != null){
                        Toast.makeText(CollectionActivity.this, "已在购物车中", Toast.LENGTH_SHORT).show();
                    }else {
                        CartService.insert(collection.getId(), Msg.loginUsername, goods.getStore());
                        GoodsService.getStoreByGoods(goods).setIsChecked(0);
                        Toast.makeText(CollectionActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    }
                }else if (view.getId() == R.id.btnDelete){
                    CollectionService.deleteFromCollection(collection.getId(), collection.getUsername());
                    collections.remove(collection);
                    collectionAdapter.notifyDataSetChanged();
                }else if (view.getId() == R.id.collection_goods){
                    Intent intent = new Intent(CollectionActivity.this, GoodsDetailActivity.class);
                    intent.putExtra("id", collection.getId());
                    startActivity(intent);
                }
            }
        });
    }


}
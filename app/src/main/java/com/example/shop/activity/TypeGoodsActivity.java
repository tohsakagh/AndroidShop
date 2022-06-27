package com.example.shop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.adapter.GoodsAdapter;
import com.example.shop.adapter.GoodsTwoListAdapter;
import com.example.shop.bean.Goods;
import com.example.shop.service.GoodsService;

import java.util.ArrayList;
import java.util.List;

public class TypeGoodsActivity extends AppCompatActivity implements View.OnClickListener {

    private String type;

    private ImageView back;
    private TextView title;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private List<Goods> mList = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    private GoodsTwoListAdapter goodsTwoListAdapter;

    private ImageView list;
    private ImageView sort;

    private boolean isAsc = false;
    private boolean isTwoList = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_goods);

        type = getIntent().getStringExtra("type");

        back =  findViewById(R.id.back);
        title = findViewById(R.id.title);
        list = findViewById(R.id.choose_list);
        sort = findViewById(R.id.sort);

        back.setOnClickListener(this);
        title.setText(type + "分类");

        recyclerView = findViewById(R.id.rv_classify);
        mList.addAll(GoodsService.getGoodsByType(type));
        goodsAdapter = new GoodsAdapter(R.layout.item_goods, mList);
        goodsAdapter.setContext(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(goodsAdapter);

        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(TypeGoodsActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                startActivity(intent);
            }
        });

        recyclerView1 = findViewById(R.id.rv_twolist);
        goodsTwoListAdapter = new GoodsTwoListAdapter(R.layout.item_goods_twolist, mList);
        goodsTwoListAdapter.setContext(this);
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView1.setAdapter(goodsTwoListAdapter);
        goodsTwoListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(TypeGoodsActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                startActivity(intent);
            }
        });

        sort.setOnClickListener(v -> listBySort());
        list.setOnClickListener(v -> changeList());

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.back){
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("id", 2);
        }
        startActivity(intent);
    }

    private void sortByPrice(Integer rule){
        List<Goods> goodsList = GoodsService.getGoodsByTypeOrderByPrice(type, rule);
        mList.clear();
        mList.addAll(goodsList);
        goodsAdapter.notifyDataSetChanged();
        goodsTwoListAdapter.notifyDataSetChanged();
    }

    private void listBySort(){
        if (!isAsc){
            sort.setImageDrawable(this.getDrawable(R.drawable.ascending));
            sortByPrice(0);
            isAsc = true;
        }else {
            sort.setImageDrawable(this.getDrawable(R.drawable.descending));
            sortByPrice(1);
            isAsc = false;
        }
    }

    private void changeList(){
        if (!isTwoList){
            list.setImageDrawable(this.getDrawable(R.drawable.twolist));
            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            isTwoList = true;
        }else {
            list.setImageDrawable(this.getDrawable(R.drawable.onelist));
            recyclerView1.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            isTwoList = false;
        }
    }
}
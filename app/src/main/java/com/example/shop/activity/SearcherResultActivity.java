package com.example.shop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.adapter.GoodsAdapter;
import com.example.shop.adapter.GoodsTwoListAdapter;
import com.example.shop.bean.Goods;
import com.example.shop.service.GoodsService;
import com.example.shop.service.SearcherService;

import java.util.ArrayList;
import java.util.List;

public class SearcherResultActivity extends AppCompatActivity {

    private EditText textSearcher;
    private ImageView back;
    private ImageView delete;
    private TextView toSearcher;
    private TextView text1;
    private TextView toPrevious;
    private RelativeLayout empty;

    private RecyclerView rvSearcherResult;
    private RecyclerView recyclerView1;
    private List<Goods> goodsList = new ArrayList<>();

    private GoodsAdapter goodsAdapter;
    private GoodsTwoListAdapter goodsTwoListAdapter;

    private ImageView list;
    private ImageView sort;

    private boolean isAsc = false;
    private boolean isTwoList = false;

    private Intent intent;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searcher_result);
        intent = null;

        textSearcher = findViewById(R.id.text_searcher);
        back = findViewById(R.id.iv_back);
        delete = findViewById(R.id.delete);
        toSearcher = findViewById(R.id.to_searcher);
        text1 = findViewById(R.id.text1);
        toPrevious = findViewById(R.id.to_previous);
        empty = findViewById(R.id.empty);
        list = findViewById(R.id.choose_list);
        sort = findViewById(R.id.sort);

        rvSearcherResult = findViewById(R.id.rv_searcher_result);

        boolean isHaveGoods = false;

        String text = getIntent().getStringExtra("text");
        textSearcher.setText(text);

        List<Goods> goodsList1 = SearcherService.getGoodsByKeyWord(textSearcher.getText().toString());
        List<Goods> goodsList2 = GoodsService.getGoodsByLike(textSearcher.getText().toString());

        if (goodsList1.size() != 0){
            goodsList.addAll(goodsList1);
        }else {
            goodsList.addAll(goodsList2);
        }

        if (goodsList.size() != 0){
            isHaveGoods = true;
        }

        if (!isHaveGoods){
            text1.setText("抱歉，没有找到与“ " + textSearcher.getText().toString() + " “相关的商品");
            rvSearcherResult.setVisibility(View.GONE);
            recyclerView1.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }else {
            rvSearcherResult.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }

        goodsAdapter = new GoodsAdapter(R.layout.item_goods, goodsList);
        rvSearcherResult.setLayoutManager(new LinearLayoutManager(this));
        rvSearcherResult.setAdapter(goodsAdapter);
        textSearcher.setFocusable(false);
        textSearcher.setEnabled(true);

        textSearcher.setOnClickListener(v -> {
            intent = new Intent(SearcherResultActivity.this, SearcherActivity.class);
            intent.putExtra("text", textSearcher.getText().toString());
            startActivity(intent);
        });

        back.setOnClickListener(v -> {
         finish();
        });

        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                intent = new Intent(SearcherResultActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", goodsList.get(position).getId());
                startActivity(intent);
            }
        });

        recyclerView1 = findViewById(R.id.rv_twolist);
        goodsTwoListAdapter = new GoodsTwoListAdapter(R.layout.item_goods_twolist, goodsList);
        goodsTwoListAdapter.setContext(this);
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView1.setAdapter(goodsTwoListAdapter);
        goodsTwoListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                intent = new Intent(SearcherResultActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", goodsList.get(position).getId());
                startActivity(intent);
            }
        });

        sort.setOnClickListener(v -> listBySort());
        list.setOnClickListener(v -> changeList());
    }

    private void sortByPrice(Integer rule){
        List<Goods> list = SearcherService.getGoodsByKeyWordOrderByPrice(textSearcher.getText().toString(), rule);

        goodsList.clear();
        goodsList.addAll(list);
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
            rvSearcherResult.setVisibility(View.GONE);
            isTwoList = true;
        }else {
            list.setImageDrawable(this.getDrawable(R.drawable.onelist));
            recyclerView1.setVisibility(View.GONE);
            rvSearcherResult.setVisibility(View.VISIBLE);
            isTwoList = false;
        }
    }
}

package com.example.shop.fragment;

import android.content.Intent;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.activity.GoodsDetailActivity;
import com.example.shop.adapter.GoodsAdapter;
import com.example.shop.adapter.GoodsTwoListAdapter;
import com.example.shop.bean.Goods;
import com.example.shop.service.GoodsService;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BasePageTitleFragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private List<Goods> mList = new ArrayList<>();
    private GoodsAdapter goodsAdapter;
    private GoodsTwoListAdapter goodsTwoListAdapter;

    private ImageView list;
    private ImageView sort;

    private boolean isAsc = false;
    private boolean isTwoList = false;


    @Override
    protected View initView() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setTitleIcon(null, false, false, true);//标题栏的显示
        View homeFragment = View.inflate(getContext(), R.layout.fragment_home, null);

        list = homeFragment.findViewById(R.id.choose_list);
        sort = homeFragment.findViewById(R.id.sort);

        recyclerView = homeFragment.findViewById(R.id.rv_classify);
        mList.addAll(GoodsService.getGoodsByType(null));
        goodsAdapter = new GoodsAdapter(R.layout.item_goods, mList);
        goodsAdapter.setContext(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(goodsAdapter);
        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                startActivity(intent);
            }
        });

        recyclerView1 = homeFragment.findViewById(R.id.rv_twolist);
        goodsTwoListAdapter = new GoodsTwoListAdapter(R.layout.item_goods_twolist, mList);
        goodsTwoListAdapter.setContext(getContext());
        recyclerView1.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView1.setAdapter(goodsTwoListAdapter);
        goodsTwoListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                startActivity(intent);
            }
        });

        sort.setOnClickListener(v -> listBySort());
        list.setOnClickListener(v -> changeList());

        return homeFragment;
    }

    @Override
    protected void initData() {

    }

    private void sortByPrice(Integer rule){
        List<Goods> goodsList = GoodsService.getGoodsByTypeOrderByPrice(null, rule);
        mList.clear();
        mList.addAll(goodsList);
        goodsAdapter.notifyDataSetChanged();
        goodsTwoListAdapter.notifyDataSetChanged();
    }

    private void listBySort(){
       if (!isAsc){
            sort.setImageDrawable(getContext().getDrawable(R.drawable.ascending));
            sortByPrice(0);
            isAsc = true;
       }else {
           sort.setImageDrawable(getContext().getDrawable(R.drawable.descending));
            sortByPrice(1);
            isAsc = false;
       }
    }

    private void changeList(){
        if (!isTwoList){
            list.setImageDrawable(getContext().getDrawable(R.drawable.twolist));
            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            isTwoList = true;
        }else {
            list.setImageDrawable(getContext().getDrawable(R.drawable.onelist));
            recyclerView1.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            isTwoList = false;
        }
    }

}

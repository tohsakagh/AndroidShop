package com.example.shop.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop.R;
import com.example.shop.bean.Cart;
import com.example.shop.bean.Goods;
import com.example.shop.bean.Msg;
import com.example.shop.bean.Store;
import com.example.shop.service.CartService;
import com.example.shop.service.CollectionService;
import com.example.shop.service.GoodsService;
import com.example.shop.service.OrderService;
import com.example.shop.service.StoreService;
import com.example.shop.utils.BitmapUtils;

import java.util.List;

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView btnToStore;
    private ImageView collection;
    private ImageView toCart;
    private TextView addToCart;
    private TextView buy;

    private TextView name;
    private TextView price;
    private TextView description;
    private LinearLayout toStore;
    private ImageView storePhoto;
    private TextView storeName;

    private Integer id;
    private Goods goods;


    private RecyclerView rvGoods;
    private List<Goods> goodsList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_deatil);

        id = getIntent().getIntExtra("id", 0);
        goods = GoodsService.getGoodsBuyId(id);

        Store store = StoreService.getStore(goods.getStore());

        back = findViewById(R.id.back_icons);
        btnToStore = findViewById(R.id.button_store);
        collection = findViewById(R.id.button_collection);
        toCart = findViewById(R.id.button_cart);
        addToCart = findViewById(R.id.button_add_cart);
        buy = findViewById(R.id.button_buy);
        toStore = findViewById(R.id.to_store);
        storePhoto = findViewById(R.id.tv_store_photo);
        storeName = findViewById(R.id.tv_store_name);
        rvGoods = findViewById(R.id.rv_goods);

        back.setOnClickListener(this);
        btnToStore.setOnClickListener(this);
        collection.setOnClickListener(this);
        toCart.setOnClickListener(this);
        addToCart.setOnClickListener(this);
        buy.setOnClickListener(this);
        toStore.setOnClickListener(this);


        name = findViewById(R.id.text_goods_name);
        price = findViewById(R.id.text_goods_price);
        description = findViewById(R.id.text_goods_description);
        name.setText(goods.getGoodsName());
        price.setText("￥" + goods.getPrice() + "");
        description.setText(goods.getDescription());
        storeName.setText(goods.getStore().toString());

        ImageView goodImg = findViewById(R.id.goods_photo);
        if ("https".equals(goods.getUrl().substring(0,5))) {
            Glide.with(this).load(goods.getUrl()).into(goodImg);
        }else {
            goodImg.setImageBitmap(BitmapUtils.stringToBitmap(goods.getUrl()));
        }

        if ("https".equals(store.getUrl().substring(0,5))) {
            Glide.with(this).load(store.getUrl()).into(storePhoto);
        }else {
            storePhoto.setImageBitmap(BitmapUtils.stringToBitmap(store.getUrl()));
        }


        goodsList = GoodsService.getGoodsByStore(goods.getStore());

        MyAdapter myAdapter = new MyAdapter();
        rvGoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvGoods.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        rvGoods.setAdapter(myAdapter);


        if (CollectionService.isInCollection(goods.getId())){
            collection.setImageDrawable(GoodsDetailActivity.this.getDrawable(R.drawable.collection));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.back_icons) {
            GoodsDetailActivity.this.finish();
        } else if (v.getId() == R.id.button_store || v.getId() == R.id.to_store) {
            intent = new Intent(GoodsDetailActivity.this, StoreActivity.class);
            intent.putExtra("name", goods.getStore());
            startActivity(intent);
        } else if (v.getId() == R.id.button_collection) {
            if (!Msg.isLogin){
                intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);

            }else {
                if (!CollectionService.isInCollection(id)) {
                    CollectionService.insertToCollection(id, Msg.loginUsername);
                    showMsg("收藏成功");
                    collection.setImageDrawable(GoodsDetailActivity.this.getDrawable(R.drawable.collection));
                } else {
                    CollectionService.deleteFromCollection(id, Msg.loginUsername);
                    showMsg("取消收藏");
                    collection.setImageDrawable(GoodsDetailActivity.this.getDrawable(R.drawable.nocollection));
                }
            }
        } else if (v.getId() == R.id.button_cart) {
            intent = new Intent(GoodsDetailActivity.this, MainActivity.class);
            intent.putExtra("id", 3);
        } else if (v.getId() == R.id.button_add_cart) {
            if (!Msg.isLogin){
                intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
            }else {
                if (CartService.getCart(id) == null) {
                    CartService.insert(id,Msg.loginUsername, goods.getStore());
                    GoodsService.getStoreByGoods(goods).setIsChecked(0);
                    showMsg("添加成功");
                }
            }
        } else if (v.getId() == R.id.button_buy) {
            if (!Msg.isLogin){
                intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
            }else {
                alertClick(v, name, price);
            }
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    public void alertClick(View v, TextView name, TextView price) {
        View view = View.inflate(GoodsDetailActivity.this, R.layout.item_goods, null);
        //创建 一个提示对话框的构造者对象
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("你确定要花费" + price.getText().toString() + "元购买" + name.getText().toString() + "?");//设置弹出对话框的内容
        builder.setCancelable(false);//能否被取消
        //正面的按钮（肯定）
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OrderService.insertToOrder(id, 1);
                startActivity(new Intent(GoodsDetailActivity.this, OrderActivity.class));
                dialog.cancel();
            }
        });
        //反面的按钮（否定）
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(GoodsDetailActivity.this, R.layout.item_goods_list, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            Goods goods = goodsList.get(position);
            holder.description.setText(goods.getDescription());
            if ("https".equals(goods.getUrl().substring(0,5))) {
                Glide.with(GoodsDetailActivity.this).load(goods.getUrl()).into(holder.photo);
            }else {
                holder.photo.setImageBitmap(BitmapUtils.stringToBitmap(goods.getUrl()));
            }
        }

        @Override
        public int getItemCount() {
            return goodsList.size();
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView description;
        ImageView photo;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.tv_goods_description);
            photo = itemView.findViewById(R.id.iv_goods);
        }
    }
}
package com.example.shop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.example.shop.R;
import com.example.shop.adapter.MyStoreGoodsAdapter;
import com.example.shop.bean.Goods;
import com.example.shop.bean.Msg;
import com.example.shop.bean.Store;
import com.example.shop.service.GoodsService;
import com.example.shop.service.StoreService;
import com.example.shop.service.UserService;
;
import java.util.List;

public class MyStoreActivity extends AppCompatActivity {

    private ImageView back;
    private TextView mystoreName;
    private ImageView addGoods;
    private LinearLayout lvAddStore;
    private TextView addStore;
    private RecyclerView storeGoods;


    private String storeName = null;

    private MyStoreGoodsAdapter myStoreGoodsAdapter;
    private List<Goods> goodsList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);

        back = findViewById(R.id.back);
        mystoreName = findViewById(R.id.tv_mystore_name);
        addGoods = findViewById(R.id.add_goods);
        lvAddStore = findViewById(R.id.lv_add_store);
        addStore = findViewById(R.id.add_store);
        storeGoods = findViewById(R.id.rv_store_goods);

        back.setOnClickListener(v -> finish());
        addGoods.setOnClickListener(v ->  startActivity(new Intent(this, PublishGoodsActivity.class)));
        addStore.setOnClickListener(v ->  startActivity(new Intent(MyStoreActivity.this, CreateStoreActivity.class)));
        if (!UserService.hasStore(Msg.loginUsername)){
            lvAddStore.setVisibility(View.VISIBLE);
        }else {
            storeName = UserService.getStore();
            mystoreName.setText("我的店铺("  + storeName+ ")");
            Store store = StoreService.getStore(storeName);

            goodsList = GoodsService.getGoodsByStore(storeName);
            myStoreGoodsAdapter = new MyStoreGoodsAdapter(R.layout.item_mystore_goods, goodsList);
            storeGoods.setLayoutManager(new LinearLayoutManager(this));
            storeGoods.setAdapter(myStoreGoodsAdapter);

            myStoreGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    Goods goods = goodsList.get(position);
                    if (view.getId() == R.id.item){
                        Intent intent = new Intent(MyStoreActivity.this, GoodsDetailActivity.class);
                        intent.putExtra("id", goods.getId());
                        startActivity(intent);
                    }else if (view.getId() == R.id.btnUpdate){
                        showDialog(goods);
                    }else if (view.getId() == R.id.btnDelete){
                        GoodsService.delete(goods.getId());

                        myStoreGoodsAdapter.notifyDataSetChanged();
                        goodsList.remove(goods);
                    }
                }
            });

        }

    }

    public void showDialog(Goods goods){
        MyDialog myDialog = new MyDialog(this, goods);
        myDialog.show();
    }


    class MyDialog extends Dialog{

        Activity context;
        Goods goods;

         EditText nameUpdate;
         EditText priceUpdate;
         TextView cancel;
         TextView sure;

         MyDialog(@NonNull Activity context, Goods goods) {
            super(context);
            this.context = context;
            this.goods = goods;
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            this.setContentView(R.layout.update_goods);

            nameUpdate = findViewById(R.id.goods_simple_name_input);
            priceUpdate = findViewById(R.id.goods_price_input);

            nameUpdate.setText(goods.getSimpleName());
            priceUpdate.setText(goods.getPrice() + "");
            Window window = this.getWindow();
            WindowManager windowManager = context.getWindowManager();
            Display defaultDisplay = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams p = window.getAttributes();
            p.width = (int) (defaultDisplay.getWidth() * 0.95);
            window.setAttributes(p);

            sure = findViewById(R.id.sure);
            cancel = findViewById(R.id.cancel);

            this.setCancelable(true);

            sure.setOnClickListener(v -> sure());
            cancel.setOnClickListener(v -> cancel());

        }

        public void sure(){
            GoodsService.updateNameAndPrice(goods.getId(), nameUpdate.getText().toString(), Double.parseDouble(priceUpdate.getText().toString()));
            Toast.makeText(context, "修改成功=", Toast.LENGTH_SHORT).show();
            myStoreGoodsAdapter.notifyDataSetChanged();
            cancel();
        }
    }

}

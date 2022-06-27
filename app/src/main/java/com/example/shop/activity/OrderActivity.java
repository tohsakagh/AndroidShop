package com.example.shop.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.adapter.OrderAdapter;
import com.example.shop.bean.Order;
import com.example.shop.service.OrderService;

import java.util.List;

public class OrderActivity extends AppCompatActivity{

    private RecyclerView rvOrder;
    private List<Order> orders;
    private OrderAdapter orderAdapter;
    private Dialog dialog;

    private ImageView back;

    private boolean isHaveOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orders = OrderService.getOrders();
        System.out.println(orders);
        rvOrder = findViewById(R.id.rv_order);

        if (orders.size() != 0){
            isHaveOrder = true;
        }

        RelativeLayout relativeLayout = findViewById(R.id.empty);
        if (!isHaveOrder){
            relativeLayout.setVisibility(View.VISIBLE);
            rvOrder.setVisibility(View.GONE);
        }else {
            relativeLayout.setVisibility(View.GONE);
            rvOrder.setVisibility(View.VISIBLE);
        }

        orderAdapter = new OrderAdapter(R.layout.item_order, orders);
        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        rvOrder.setAdapter(orderAdapter);

        back = findViewById(R.id.tv_back);

        back.setOnClickListener(v -> OrderActivity.this.finish());



        orderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Order order = orders.get(position);

                if (view.getId() == R.id.tv_goods_cancel){
                    dialog = new AlertDialog.Builder(OrderActivity.this)
                            .setMessage("你确定要取消当前订单吗?")
                            .setPositiveButton("确定", (dialog, which) -> cancelOrder(order))
                            .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                            .create();
                    dialog.show();
                } else if (view.getId() == R.id.order_goods){
                    Intent intent = new Intent(OrderActivity.this, GoodsDetailActivity.class);
                    intent.putExtra("id", order.getId());
                    startActivity(intent);
                }
            }
        });

    }

    private void cancelOrder(Order order){
        OrderService.deleteFromOrder(order.getId());
        orders.remove(order);
        orderAdapter.notifyDataSetChanged();
    }

}
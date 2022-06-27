package com.example.shop.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.shop.R;
import com.example.shop.activity.CollectionActivity;
import com.example.shop.activity.LoginActivity;
import com.example.shop.activity.OrderActivity;
import com.example.shop.activity.RegistActivity;

public class NologinMineFragment extends BasePageTitleFragment implements View.OnClickListener{

    private Button login;
    private Button regist;

    private RelativeLayout toCollection;
    private RelativeLayout toOrder;
    private RelativeLayout toPublish;
    private RelativeLayout toMyStore;
    private RelativeLayout setting;
    private RelativeLayout toLogoff;

    @Override
    protected View initView() {
        setTitleIcon("个人中心",true, true, false);//标题栏的显示
        View nologinMineFragment = View.inflate(getContext(), R.layout.fragment_nologin_mine, null);
        nologinMineFragment.findViewById(R.id.nologin_head).setOnClickListener(this);
        login =   nologinMineFragment.findViewById(R.id.login);
        regist =  nologinMineFragment.findViewById(R.id.regist);
        toCollection = nologinMineFragment.findViewById(R.id.re_collection);
        toOrder = nologinMineFragment.findViewById(R.id.re_order);
        toPublish = nologinMineFragment.findViewById(R.id.re_publish);
        toMyStore = nologinMineFragment.findViewById(R.id.re_store);
        setting = nologinMineFragment.findViewById(R.id.re_setting);
        toLogoff = nologinMineFragment.findViewById(R.id.re_logoff);

        login.setOnClickListener(this);
        regist.setOnClickListener(this);
        toCollection.setOnClickListener(this);
        toOrder.setOnClickListener(this);
        toPublish.setOnClickListener(this);
        toMyStore.setOnClickListener(this);
        setting.setOnClickListener(this);
        toLogoff.setOnClickListener(this);

        return nologinMineFragment ;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        if (v.getId() == R.id.login){
            intent = new Intent(getActivity(), LoginActivity.class);
        }else if (v.getId() == R.id.regist){
            intent = new Intent(getActivity(), RegistActivity.class);
        }else {
            intent = new Intent(getActivity(), LoginActivity.class);
        }
        startActivity(intent);
    }
}

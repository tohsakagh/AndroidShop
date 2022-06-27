package com.example.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shop.R;
import com.example.shop.activity.SearcherActivity;


public abstract class BasePageTitleFragment extends Fragment {

    private View mFragmentView;//父控件(由父控件找到子控件)
    private RelativeLayout mTop;
    private ImageView mIvLogoPage;
    private TextView mTvTitlePage;
    protected FrameLayout mFlTitleContentPage;
    private LinearLayout mTvSearcher;
    private TextView textSearcher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.base_top_title_page, container, false);   //通用布局(图片 充值)
        mTop = mFragmentView.findViewById(R.id.top);
        mTvSearcher = mFragmentView.findViewById(R.id.searcher);
        textSearcher = mFragmentView.findViewById(R.id.text_searcher);
        mIvLogoPage = (ImageView) mFragmentView.findViewById(R.id.iv_logo_page);
        mTvTitlePage = (TextView) mFragmentView.findViewById(R.id.tv_title_page);
        mFlTitleContentPage = (FrameLayout) mFragmentView.findViewById(R.id.fl_title_content_page);
        View view = initView();
        mFlTitleContentPage.addView(view);

        textSearcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), SearcherActivity.class));
            }
        });
        return mFragmentView;
    }

    public void setTitleIcon(String msg, boolean show, boolean show1, boolean show2){    //设置标题和图标
        mTvTitlePage.setText(msg);  //设置标题
        mTvTitlePage.setVisibility(show ? View.VISIBLE : View.GONE);//true就是显示  false就是不显示
        mTop.setVisibility(show1 ? View.VISIBLE : View.GONE);
        mTvSearcher.setVisibility(show2? View.VISIBLE : View.GONE);
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract View initView();
    protected abstract void initData();

}

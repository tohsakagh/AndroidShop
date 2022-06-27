package com.example.shop.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shop.R;
import com.example.shop.bean.Msg;
import com.example.shop.fragment.CartFragment;
import com.example.shop.fragment.ClassifyFragment;
import com.example.shop.fragment.HomeFragment;
import com.example.shop.fragment.IsloginMineFragment;
import com.example.shop.fragment.NologinMineFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //标题栏隐藏在values/styles.xml里<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar"> 将其隐藏掉了。
    //初始化fragment
    private HomeFragment mHomeFragment;
    private ClassifyFragment mClassifyFragment;
    private CartFragment mCartFragment;
    //    private MineFragment mMineFragment;
    private NologinMineFragment mNologinMineFragment;
    private IsloginMineFragment mIsloginMineFragment;

    //片段类容
    private FrameLayout mFlFragmentContent;
    //底部4个按钮
    private RelativeLayout mRlFirstLayout;
    private RelativeLayout mRlSecondLayout;
    private RelativeLayout mRlThirdLayout;
    private RelativeLayout mRlFourLayout;

    private ImageView mIvFirstHome;
    private TextView mTvFirstHome;
    private ImageView mIvSecondMatch;
    private TextView mTvSecondMatch;
    private ImageView mIvThirdRecommend;
    private TextView mTvThirdRecommend;
    private ImageView mIvFourMine;
    private TextView mTvFourMine;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        id = getIntent().getIntExtra("id", 0);
        initView();     //初始化视图
    }

    private void initView(){
        mFlFragmentContent = (FrameLayout) findViewById(R.id.fl_fragment_content);
        mRlFirstLayout = (RelativeLayout) findViewById(R.id.rl_first_layout);
        mIvFirstHome = (ImageView) findViewById(R.id.iv_first_home);
        mTvFirstHome = (TextView) findViewById(R.id.tv_first_home);
        mRlSecondLayout = (RelativeLayout) findViewById(R.id.rl_second_layout);
        mIvSecondMatch = (ImageView) findViewById(R.id.iv_second_match);
        mTvSecondMatch = (TextView) findViewById(R.id.tv_second_match);
        mRlThirdLayout = (RelativeLayout) findViewById(R.id.rl_third_layout);
        mIvThirdRecommend = (ImageView) findViewById(R.id.iv_third_recommend);
        mTvThirdRecommend = (TextView) findViewById(R.id.tv_third_recommend);
        mRlFourLayout = (RelativeLayout) findViewById(R.id.rl_four_layout);
        mIvFourMine = (ImageView) findViewById(R.id.iv_four_mine);
        mTvFourMine = (TextView) findViewById(R.id.tv_four_mine);
        //给四个按钮设置监听器
        mRlFirstLayout.setOnClickListener(this);
        mRlSecondLayout.setOnClickListener(this);
        mRlThirdLayout.setOnClickListener(this);
        mRlFourLayout.setOnClickListener(this);
        //默认第一个首页被选中高亮显示
        selected(id);
    }

    public void selected(int id){
        if (id == 1){
            mRlFirstLayout.setSelected(true);
            mTransaction = mFragmentManager.beginTransaction();
            mTransaction.replace(R.id.fl_fragment_content, new HomeFragment());
        }else if (id == 2){
            mRlSecondLayout.setSelected(true);
            mTransaction = mFragmentManager.beginTransaction();
            mTransaction.replace(R.id.fl_fragment_content, new ClassifyFragment());
        }else if (id == 3){
            mRlThirdLayout.setSelected(true);
            mTransaction = mFragmentManager.beginTransaction();
            mTransaction.replace(R.id.fl_fragment_content, new CartFragment());
        }else if (id == 4){
            mRlFourLayout.setSelected(true);
            mTransaction = mFragmentManager.beginTransaction();
            mTransaction.replace(R.id.fl_fragment_content, new IsloginMineFragment());
        }else {
            mRlFirstLayout.setSelected(true); mTransaction = mFragmentManager.beginTransaction();
            mTransaction.replace(R.id.fl_fragment_content, new HomeFragment());
        }
        mTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        mTransaction = mFragmentManager.beginTransaction(); //开启事务
        hideAllFragment(mTransaction);
        switch (v.getId()){
            //首页
            case R.id.rl_first_layout:
                seleted();
                mRlFirstLayout.setSelected(true);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mTransaction.add(R.id.fl_fragment_content,mHomeFragment);    //通过事务将内容添加到内容页
                }else{
                    mTransaction.show(mHomeFragment);
                }
                break;
            //赛程
            case R.id.rl_second_layout:
                seleted();
                mRlSecondLayout.setSelected(true);
                if (mClassifyFragment == null) {
                    mClassifyFragment = new ClassifyFragment();
                    mTransaction.add(R.id.fl_fragment_content,mClassifyFragment);    //通过事务将内容添加到内容页
                }else{
                    mTransaction.show(mClassifyFragment);
                }
                break;
            //推荐
            case R.id.rl_third_layout:
                seleted();
                mRlThirdLayout.setSelected(true);
                if (mCartFragment == null) {
                    mCartFragment = new CartFragment();
                    mTransaction.add(R.id.fl_fragment_content,mCartFragment);    //通过事务将内容添加到内容页
                }else{
                    mTransaction.show(mCartFragment);
                }
                break;
            //个人中心
            case R.id.rl_four_layout:
                seleted();
                if (!Msg.isLogin) {
                    mRlFourLayout.setSelected(true);
                    if (mNologinMineFragment == null) {
                        mNologinMineFragment = new NologinMineFragment();
                        mTransaction.add(R.id.fl_fragment_content, mNologinMineFragment);    //通过事务将内容添加到内容页
                    } else {
                        mTransaction.show(mNologinMineFragment);
                    }
                }else {
                    mRlFourLayout.setSelected(true);
                    if (mIsloginMineFragment == null) {
                        mIsloginMineFragment = new IsloginMineFragment();
                        mTransaction.add(R.id.fl_fragment_content, mIsloginMineFragment);    //通过事务将内容添加到内容页
                    } else {
                        mTransaction.show(mIsloginMineFragment);
                    }
                }
                break;
        }
        mTransaction.commit();

    }

    //设置所有按钮都是默认都不选中
    private void seleted() {
        mRlFirstLayout.setSelected(false);
        mRlSecondLayout.setSelected(false);
        mRlThirdLayout.setSelected(false);
        mRlFourLayout.setSelected(false);
    }

    //删除所有Fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mClassifyFragment != null) {
            transaction.hide(mClassifyFragment);
        }
        if (mCartFragment != null) {
            transaction.hide(mCartFragment);
        }
        if (mNologinMineFragment != null) {
            transaction.hide(mNologinMineFragment);
        }
        if (mIsloginMineFragment != null) {
            transaction.hide(mIsloginMineFragment);
        }
    }
}


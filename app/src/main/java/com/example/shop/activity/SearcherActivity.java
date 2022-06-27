package com.example.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.shop.R;
import com.example.shop.adapter.KeywordAdapter;
import com.example.shop.adapter.SearcherHistoryAdapter;
import com.example.shop.bean.SearcherHistory;
import com.example.shop.service.SearcherHistoryService;
import com.example.shop.service.SearcherService;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;


import java.util.List;

public class SearcherActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textSearcher;
    private ImageView back;
    private TextView textHistory;
    private ImageView clear;
    private ImageView delete;
    private TextView toSearcher;

    private RecyclerView rvSearcherHistory;
    private RecyclerView rvSearcherList;


    private List<SearcherHistory> allHistories;

    private SearcherHistoryAdapter searcherHistoryAdapter;
    private KeywordAdapter keywordAdapter;


    private AlertDialog dialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searcher);
        //唤起键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        allHistories = SearcherHistoryService.getAllHistories();
        intent = null;

        textSearcher = findViewById(R.id.text_searcher);
        back = findViewById(R.id.iv_back);
        textHistory = findViewById(R.id.text_history);
        clear = findViewById(R.id.clear);
        delete = findViewById(R.id.delete);
        toSearcher = findViewById(R.id.to_searcher);


        rvSearcherList = findViewById(R.id.rv_searcher_list);
        rvSearcherHistory = findViewById(R.id.rv_searcher_history);

        searcherHistoryAdapter = new SearcherHistoryAdapter(R.layout.item_searcher_history, allHistories);

        textSearcher.requestFocus();
        textSearcher.setOnClickListener(this);
        back.setOnClickListener(this);
        clear.setOnClickListener(this);
        delete.setOnClickListener(this);
        toSearcher.setOnClickListener(this);

        String text = getIntent().getStringExtra("text");

        if (text != null){
            rvSearcherList.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            rvSearcherHistory.setVisibility(View.GONE);
            clear.setVisibility(View.GONE);
            textHistory.setVisibility(View.GONE);
            textSearcher.setText(text);
            showSearcherList();
        }

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        rvSearcherHistory.setLayoutManager(flexboxLayoutManager);
        rvSearcherHistory.setAdapter(searcherHistoryAdapter);


        searcherHistoryAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                TextView textView = view.findViewById(R.id.text_history);
                dialog = new AlertDialog.Builder(SearcherActivity.this)
                        .setMessage("你确定要删除这条记录吗?")
                        .setPositiveButton("确定", (dialog, which) -> deleteSearcherHistory(textView, position))
                        .setNegativeButton("取消", (dialog, which) -> dialog.cancel()).create();
                dialog.show();

                return true;
            }
        });
        searcherHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TextView textView = view.findViewById(R.id.text_history);
                SearcherHistoryService.insert(textView.getText().toString());
                intent = new Intent(SearcherActivity.this, SearcherResultActivity.class);
                intent.putExtra("text", textView.getText().toString());
                startActivity(intent);
            }
        });

        textSearcher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    delete.setVisibility(View.GONE);
                    rvSearcherHistory.setVisibility(View.VISIBLE);
                    textHistory.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.VISIBLE);
                    showSearcherList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_searcher) {
            if (textSearcher.getText().toString().length() != 0) {
                showSearcherList();
            }
        } else if (v.getId() == R.id.iv_back || v.getId() == R.id.to_previous) {
            this.finish();
        } else if (v.getId() == R.id.delete) {
            textSearcher.setText("");
            rvSearcherList.setVisibility(View.GONE);
            rvSearcherHistory.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.to_searcher) {
            SearcherHistoryService.insert(textSearcher.getText().toString());
            intent = new Intent(SearcherActivity.this, SearcherResultActivity.class);
            intent.putExtra("text", textSearcher.getText().toString());
            startActivity(intent);
        }else if (v.getId() == R.id.clear){
            allHistories.clear();
            SearcherHistoryService.deleteAll();
            searcherHistoryAdapter.notifyDataSetChanged();
        }
    }

    private void showSearcherList() {
        rvSearcherList.setVisibility(View.VISIBLE);
        rvSearcherHistory.setVisibility(View.GONE);
        textHistory.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
        String input = textSearcher.getText().toString().trim();
        List<String> keyWords = SearcherService.getKeyWords(input);

        keywordAdapter = new KeywordAdapter(R.layout.item_keyword, keyWords);
        rvSearcherList.setLayoutManager(new LinearLayoutManager(SearcherActivity.this));
        rvSearcherList.setAdapter(keywordAdapter);

        keywordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String s = SearcherService.getKeyWords(textSearcher.getText().toString()).get(position);
                SearcherHistoryService.insert(s);
                textSearcher.setText(s);
                rvSearcherList.setVisibility(View.GONE);
                intent = new Intent(SearcherActivity.this, SearcherResultActivity.class);
                intent.putExtra("text", textSearcher.getText().toString());
                startActivity(intent);
            }
        });
    }


    public void deleteSearcherHistory(TextView textView, int position){
        SearcherHistoryService.deleteOne(textView.getText().toString());
        allHistories.remove(position);
        searcherHistoryAdapter.notifyDataSetChanged();
    }
}
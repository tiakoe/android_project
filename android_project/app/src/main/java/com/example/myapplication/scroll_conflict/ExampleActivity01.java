package com.example.myapplication.scroll_conflict;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.Toast.makeText;

public class ExampleActivity01 extends AppCompatActivity {

    @BindView(R.id.id_scroll_view_01)
    ScrollView mScrollView;
    @BindView(R.id.id_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.id_linearLayout)
    LinearLayout mLinearLayout;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_scroll_conflict_01);
        ButterKnife.bind(this);

        initParam();
        initListener();


    }

    private void initParam() {
        mContext = this.getApplicationContext();
    }

    @SuppressLint("ShowToast")
    @OnClick(R.id.id_item_01)
    public void onClickItem() {
        makeText(mContext, "you have click item", Toast.LENGTH_SHORT);
    }

    void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("mSwipeRefreshLayout", "mSwipeRefreshLayout--you are refreshing..");
            }
        });

        mSwipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.d("mSwipeRefreshLayout", "mSwipeRefreshLayout--正在滚动");
            }
        });

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mScrollView.getScrollY() == 0) {
                    Log.d("mScrollView", "mScrollView--正在滚动");
                }
            }
        });


    }
}

package com.a.fun_module;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class LoadRecyclerView extends RecyclerView{

    private RecyclerViewScrollListener rvScrollListener;

    public LoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // 初始化方法
    private void init() {
        rvScrollListener = new RecyclerViewScrollListener();
        this.addOnScrollListener(rvScrollListener);
    }

    // 设置是否加载更多
    public void setLoading(boolean bool) {
        rvScrollListener.setLoadingMore(bool);
    }

    // 加载更多
    public void setOnLoadListener(RecyclerViewScrollListener.OnLoadListener onLoadListener) {
        if (rvScrollListener != null)
            rvScrollListener.setOnLoadListener(onLoadListener);
    }


}

package com.example.myapplication.scroll_conflict;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExampleActivity02 extends AppCompatActivity {


    @BindView(R.id.id_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.id_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_scroll_conflict_02);

        ButterKnife.bind(this);

        initListener();

    }

    /*
    *    recyclerView 正在滚动
         swipeRefreshLayout 正在滚动
         swipeRefreshLayout 正在滚动
         swipeRefreshLayout 正在滚动
         swipeRefreshLayout 正在滚动
         swipeRefreshLayout 正在滚动
         swipeRefreshLayout 正在滚动
         recyclerView 正在滚动
  * */
    void initListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mSwipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.d("SRL_RV", "swipeRefreshLayout 正在滚动");
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false));
        mRecyclerView.setAdapter(new SubRvAdapter(this, CreateData.getStringList02()));


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("SRL_RV", "recyclerView 正在滚动");
            }
        });
    }
}

class SubRvAdapter extends RecyclerView.Adapter<SubRvAdapter.MyViewHolder> {

    private List<String> list;
    private final LayoutInflater inflater;

    public SubRvAdapter(Context context, List<String> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.example_scroll_conflict_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(list.get(position));
        holder.tv.setTextSize(20);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv;

        private MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.id_text_view_001);
        }
    }
}






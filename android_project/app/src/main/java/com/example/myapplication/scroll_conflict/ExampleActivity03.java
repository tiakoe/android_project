package com.example.myapplication.scroll_conflict;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExampleActivity03 extends AppCompatActivity {


    @BindView(R.id.id_recycler_view_03)
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_scroll_conflict_03);
        ButterKnife.bind(this);

        initListener();
    }

    private void initListener() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false));
        mRecyclerView.setAdapter(new SubRvAdapter(this, CreateData.getStringList02()));
    }
}


class MyScrollView extends ScrollView {


    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d("SV_RV", "onInterceptTouchEvent: 外层在滑动+");
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d("SV_RV", "onInterceptTouchEvent: 外层在滑动++");
        }
        return super.onTouchEvent(ev);
    }

}


class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d("SV_RV", "dispatchTouchEvent: 内层在滑动++");
        }
        return super.dispatchTouchEvent(ev);
    }
}

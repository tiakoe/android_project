package com.example.myapplication.scroll_conflict;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExampleActivity04 extends AppCompatActivity {
    @BindView(R.id.id_view_pager_040)
    MyViewPager01 mViewPager00;
    @BindView(R.id.id_view_pager_041)
    MyViewPager02 mViewPager01;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_scroll_conflict_04);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        mViewPager00.setAdapter(new SubVpAdapter(getSupportFragmentManager(),
                CreateData.getFragmentList1()));
        mViewPager01.setAdapter(new SubVpAdapter(getSupportFragmentManager(),
                CreateData.getFragmentList2()));
    }
}


class MyViewPager01 extends ViewPager {

    public MyViewPager01(Context context) {
        super(context);
    }

    public MyViewPager01(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

class MyViewPager02 extends ViewPager {

    public MyViewPager02(Context context) {
        super(context);
    }

    public MyViewPager02(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}


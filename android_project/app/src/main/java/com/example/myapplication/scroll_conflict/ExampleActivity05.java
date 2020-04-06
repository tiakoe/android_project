package com.example.myapplication.scroll_conflict;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExampleActivity05 extends AppCompatActivity {
    @BindView(R.id.id_view_pager_050)
    ViewPager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_scroll_conflict_05);
        ButterKnife.bind(this);


        mViewPager.setAdapter(new SubVpAdapter(getSupportFragmentManager(),
                CreateData.getFragmentList4()));

    }

}

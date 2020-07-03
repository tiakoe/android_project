package com.a.myapplication.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;

import com.a.myapplication.R;
import com.a.myapplication.adapter.GridAdapter;
import com.a.myapplication.banner.ImageHolderCreator;
import com.a.myapplication.base.BaseActivity;
import com.a.myapplication.detail.DetailActivity;
import com.a.myapplication.model.ItemData;
import com.a.myapplication.model.ItemData2;
import com.a.myapplication.model.ItemData3;
import com.a.myapplication.model.Result;
import com.to.aboomy.banner.Banner;
import com.to.aboomy.banner.IndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;


public class HomeActivity extends BaseActivity<HomePresenter> implements HomeView {

    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.utilList)
    RecyclerView utilList;

    @BindView(R.id.historyButton)
    Button historyButton;

    @BindView(R.id.recycleList)
    RecyclerView recycleList;

    private boolean loadMore = false;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        presenter.getBanner();
        presenter.getUtil();
        presenter.getGradeList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setBannerData(Result<ItemData> list) {

        ArrayList<String> arrayList = new ArrayList<>();
        //使用内置Indicator
        IndicatorView indicator = new IndicatorView(this)
                .setIndicatorColor(Color.DKGRAY)
                .setIndicatorSelectorColor(Color.WHITE);
        ArrayList<ItemData> data = list.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                arrayList.add(data.get(i).getImg());
                System.out.println(list.getData().get(i));
            }
        }
        assert list.getData() != null;
        System.out.println(list.getData().toString());
        Log.d("dd2", list.getData().toString());
        banner.setIndicator(indicator)
                .setHolderCreator(new ImageHolderCreator())
                .setPages(arrayList);
    }

    @Override
    public void showBannerError(String errorMessage) {

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setUtilListData(Result<ItemData3> list) {

        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        if (loadMore) {
            utilList.removeAllViews();
            urls = new ArrayList<>();
            strings = new ArrayList<>();
        }

        ArrayList<ItemData3> temp = list.getData();
        for (int i = 0; i < temp.size(); i++) {
            if (!loadMore) {
                if (i > 2) {
                    break;
                }
            }
            urls.add(temp.get(i).getMenu_img());
            strings.add(temp.get(i).getName());
        }

        if (!loadMore) {
            urls.add("更多");
            strings.add("更多");
        }

        utilList.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL,
                false));
        utilList.setBackgroundColor(Color.parseColor("#ffffff"));
        GridAdapter gridAdapter = new GridAdapter(this, urls, strings);

        gridAdapter.setOnItemClickListener((view, position) -> {
            if (!loadMore && position == 3) {
                loadMore = true;
                presenter.getUtil();
            } else {
                Intent it = new Intent(this, DetailActivity.class);
                it.putExtra("url", temp.get(position).getUrl());
                startActivityForResult(it, 1);
            }
        });

        utilList.setAdapter(gridAdapter);
        utilList.addItemDecoration(new ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

            }
        });
        utilList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showUtilListError(String errorMessage) {

    }

    @SuppressLint("ShowToast")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setRecycleListData(Result<ItemData2> list) {
        Log.d("dd3", list.toString());
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        ArrayList<ItemData2> temp = list.getData();

        for (int i = 0; i < temp.size(); i++) {
            urls.add(temp.get(i).getImage());
            strings.add(temp.get(i).getName());
        }
        recycleList.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL,
                false));

        GridAdapter gridAdapter = new GridAdapter(this, urls, strings);
        gridAdapter.setOnItemClickListener((view, position) -> {
            Toast.makeText(this, temp.get(position).toString(), Toast.LENGTH_SHORT).show();
        });
        recycleList.setAdapter(gridAdapter);
        recycleList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showRecycleListError(String errorMessage) {

    }


}

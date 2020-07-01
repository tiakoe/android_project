package com.a.myapplication.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a.myapplication.R;
import com.a.myapplication.adapter.GridAdapter;
import com.a.myapplication.banner.ImageHolderCreator;
import com.a.myapplication.base.BaseActivity;
import com.a.myapplication.model.ItemData;
import com.a.myapplication.model.ItemData2;
import com.a.myapplication.model.ItemData3;
import com.a.myapplication.model.Result;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.to.aboomy.banner.Banner;
import com.to.aboomy.banner.IndicatorView;

import java.io.InputStream;
import java.util.ArrayList;

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

    private ArrayList<Result<ItemData>> mData1 = new ArrayList<>();
    private int mPosition;

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
            //            使用forEach()会产生RxJavaPlugins异常!!!!!
            //            list.getData().forEach(itemData -> {
            //                arrayList.add(itemData.getImg());
            //                System.out.println(itemData.getImg());
            //            });
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
    @SuppressLint("StaticFieldleak")
    private Bitmap loadImage(Context context,String imgUrl){
        final Bitmap[] rBitmap = new Bitmap[1];
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context)
                            .asBitmap()
                            .load(imgUrl)
                            .submit().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                rBitmap[0] =bitmap;
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
            }
        }.execute();
        return rBitmap[0];
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setUtilListData(Result<ItemData3> list) {

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        if (loadMore) {
            utilList.removeAllViews();
            bitmaps = new ArrayList<>();
            strings = new ArrayList<>();
        }

        ArrayList<ItemData3> temp = list.getData();
        //        使用会报错
        //        Collections.sort(temp, Comparator.comparing(ItemData3::getSort));
        for (int i = 0; i < temp.size(); i++) {
            if (!loadMore) {
                if (i > 2) {
                    break;
                }
            }
            View view = this.getLayoutInflater().inflate(R.layout.item, null);
            //            ImageView imageView = view.findViewById(R.id.itemImage);
            //            Glide.with(this).load(temp.get(i).getMenu_img())
            //                    .placeholder(R.drawable.ic_baseline_refresh_24)
            //                    .error(R.drawable.ic_baseline_error_outline_24)
            //                    .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
            //                    .into(imageView);
            //            -------
//            try {
                ArrayList<Bitmap> finalBitmaps = new ArrayList<>();
                bitmaps.add(loadImage(this,temp.get(i).getMenu_img()));

//                Glide.with(this)
//                        .asBitmap()
//                        .load(temp.get(i).getMenu_img())
//                        .placeholder(R.drawable.ic_baseline_refresh_24).error(R
//                        .drawable.ic_baseline_error_outline_24)
//                        .error(R.drawable.ic_baseline_error_outline_24)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource,
//                                                        @Nullable Transition<?
//                                                                super Bitmap> transition) {
//                                finalBitmaps.add(resource);
//                            }
//                        });
//                if (finalBitmaps == null || finalBitmaps.size() == 0) {
//                    bitmaps.add(BitmapFactory.decodeResource(this.getResources(),
//                            R.drawable.ic_baseline_error_outline_24));
//                } else {
//                    bitmaps.addAll(finalBitmaps);
//                }
//            } catch (Exception e) {
//                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
//                        R.drawable.ic_baseline_error_outline_24);
//                bitmaps.add(bitmap);
//                e.printStackTrace();
//            }

            strings.add(temp.get(i).getName());
            //            -------

            //            TextView textView = view.findViewById(R.id.itemText);
            //            textView.setText(temp.get(i).getName());

            //            Intent it = new Intent(this, DetailActivity.class);
            //            it.putExtra("url", temp.get(i).getUrl());
            //            view.setOnClickListener(v -> {
            //                startActivityForResult(it, 1);
            //            });
            //
            //            utilList.addView(view);
        }

        View view = this.getLayoutInflater().inflate(R.layout.item, null);
        ImageView imageView = view.findViewById(R.id.itemImage);
        //        错误用法！
        //        Glide.with(this)
        //                .load(R.drawable.ic_baseline_widgets_24)
        //                .into(imageView);

        //        添加更多图标
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_baseline_error_outline_24);
//                imageView.setImageBitmap(bitmap);
        if (!loadMore) {
            bitmaps.add(bitmap);
            strings.add("更多");
        }

        //        --------------

        utilList.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL,
                false));
        utilList.setBackgroundColor(Color.parseColor("#ffffff"));
        utilList.setAdapter(new GridAdapter(bitmaps, strings));
        utilList.setItemAnimator(new DefaultItemAnimator());

        //-----------------
        //        TextView textView = view.findViewById(R.id.itemText);
        //        textView.setText("更多");
        //
        //        view.setOnClickListener(v -> {
        //            loadMore = true;
        //            presenter.getUtil();
        //        });
        //        //        view.setBackgroundColor(0x999999);
        //        if (!loadMore) {
        //            utilList.addView(view);
        //        }

    }

    @Override
    public void showUtilListError(String errorMessage) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setRecycleListData(Result<ItemData2> list) {
        Log.d("dd3", list.toString());
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        ArrayList<ItemData2> temp = list.getData();
        //        使用会报错
        //        Collections.sort(temp, Comparator.comparing(ItemData2::getSort));

        for (int i = 0; i < temp.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.ic_baseline_error_outline_24);
            bitmaps.add(bitmap);
            //            年级接口访问403，拒绝访问！！！
            //                        try {
            //                            Glide.with(this)
            //                                    .asBitmap()
            //                                    .load(temp.get(i).getImage())
            //                                    .placeholder(R.drawable.ic_baseline_refresh_24)
            //                                    .error(R
            //                                    .drawable.ic_baseline_error_outline_24)
            //                                    .into(new SimpleTarget<Bitmap>() {
            //                                        @Override
            //                                        public void onResourceReady(@NonNull Bitmap
            //                                        resource,
            //                                                                    @Nullable
            //                                                                    Transition<?
            //                                                                                                                                        super Bitmap> transition) {
            //                                            bitmaps.add(resource);
            //                                        }
            //                                    });
            //                        } catch (Exception e) {
            //                            Bitmap bitmap= BitmapFactory.decodeResource(this
            //                            .getResources(), R
            //                            .drawable.ic_baseline_error_outline_24);
            //                            bitmaps.add(bitmap);
            //                            e.printStackTrace();
            //                        }
            strings.add(temp.get(i).getName());
        }
        recycleList.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL,
                false));
        recycleList.setAdapter(new GridAdapter(bitmaps, strings));
        recycleList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showRecycleListError(String errorMessage) {

    }


}

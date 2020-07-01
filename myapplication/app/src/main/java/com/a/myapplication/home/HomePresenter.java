package com.a.myapplication.home;

import android.util.Log;

import com.a.myapplication.base.BaseObserver;
import com.a.myapplication.base.BasePresenter;
import com.a.myapplication.model.ItemData;
import com.a.myapplication.model.ItemData2;
import com.a.myapplication.model.ItemData3;
import com.a.myapplication.model.Result;

public class HomePresenter extends BasePresenter<HomeView> {

    HomePresenter(HomeView baseView) {
        super(baseView);
    }

    public void getBanner() {
        addDisposable(apiServer1.getDataApi1(),
                new BaseObserver<Result<ItemData>>(baseView, true) {
            @Override
            public void onSuccess(Result<ItemData> bean) {
                baseView.setBannerData(bean);
            }

            @Override
            public void onError(String msg) {
                baseView.showBannerError(msg);
            }
        });
    }

    public void getGradeList() {
        addDisposable(apiServer2.getDataApi2("zheshidianduxiaochengxu"),
                new BaseObserver<Result<ItemData2>>(baseView, true) {
                    @Override
                    public void onSuccess(Result<ItemData2> bean) {
                        baseView.setRecycleListData(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        baseView.showRecycleListError(msg);
                    }
                });
    }


    public void getUtil() {
        addDisposable(apiServer1.getDataApi3(),
                new BaseObserver<Result<ItemData3>>(baseView, true) {
                    @Override
                    public void onSuccess(Result<ItemData3> bean) {
                        baseView.setUtilListData(bean);
                    }

                    @Override
                    public void onError(String msg) {
                        baseView.showUtilListError(msg);
                    }
                });
    }

}




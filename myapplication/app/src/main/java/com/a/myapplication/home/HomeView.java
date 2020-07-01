package com.a.myapplication.home;


import com.a.myapplication.base.BaseView;
import com.a.myapplication.model.ItemData;
import com.a.myapplication.model.ItemData2;
import com.a.myapplication.model.ItemData3;
import com.a.myapplication.model.Result;

public interface HomeView extends BaseView {
    void setBannerData(Result<ItemData> list);
    void showBannerError(String errorMessage);

    void setUtilListData(Result<ItemData3> list);
    void showUtilListError(String errorMessage);

    void setRecycleListData(Result<ItemData2> list);
    void showRecycleListError(String errorMessage);

}

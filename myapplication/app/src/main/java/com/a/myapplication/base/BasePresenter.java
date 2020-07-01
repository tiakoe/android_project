package com.a.myapplication.base;


import com.a.myapplication.API;
import com.a.myapplication.RetrofitService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BasePresenter<V extends BaseView> {

    private CompositeDisposable compositeDisposable;
    public V baseView;

    protected API apiServer1 = RetrofitService.getInstance().getApiService1();
    protected API apiServer2 = RetrofitService.getInstance().getApiService2();

    public BasePresenter(V baseView) {
        this.baseView = baseView;
    }

    public void detachView() {
        baseView = null;
        removeDisposable();
    }

    public V getBaseView() {
        return baseView;
    }

    public void addDisposable(Observable<?> observable, BaseObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable
                .add(observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer));
    }

    private void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

}

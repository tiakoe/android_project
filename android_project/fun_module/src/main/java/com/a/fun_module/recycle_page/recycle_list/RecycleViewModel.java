package com.a.fun_module.recycle_page.recycle_list;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecycleViewModel extends ViewModel {

    private MutableLiveData<ItemObject> liveData = new MutableLiveData<>();

    public RecycleViewModel(ItemObject itemObject) {
        liveData.setValue(itemObject);
    }

    public MutableLiveData<ItemObject> getLiveData() {
        return liveData;
    }

    public void setLiveData(MutableLiveData<ItemObject> liveData) {
        this.liveData = liveData;
    }


}

package com.a.myapplication.banner;

import android.content.Context;
import android.view.View;

public interface HolderCreator {
    View createView(Context context, final int index, Object o);
}

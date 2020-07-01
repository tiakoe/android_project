package com.a.myapplication.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ImageHolderCreator implements HolderCreator, com.to.aboomy.banner.HolderCreator {
    @Override
    public View createView(final Context context, final int index, Object o) {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(iv).load(o).into(iv);
        iv.setOnClickListener(v -> Toast.makeText(context, index + "", Toast.LENGTH_LONG).show());
        return iv;
    }
}

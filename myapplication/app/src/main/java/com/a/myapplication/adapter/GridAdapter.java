package com.a.myapplication.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.myapplication.R;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.Holder> {

    private List<Bitmap> bitmaps;
    private List<String> strings;

    public GridAdapter(List<Bitmap> bitmaps, List<String> strings) {
        this.bitmaps = bitmaps;
        this.strings = strings;
        Log.d("dd", String.valueOf(bitmaps.size()));
        Log.d("dd2", String.valueOf(strings.size()));
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (bitmaps.size() != 0) {
            holder.imageView.setImageBitmap(bitmaps.get(position));
        }
        if (strings.size() != 0) {
            holder.textView.setText(strings.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public Holder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.itemImage);
            textView =  itemView.findViewById(R.id.itemText);
        }
    }
}

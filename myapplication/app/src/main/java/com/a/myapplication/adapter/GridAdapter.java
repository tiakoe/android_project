package com.a.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.a.myapplication.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import static com.a.myapplication.R.drawable.ic_baseline_widgets_24;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.Holder> {

    private Context context;
    private List<String> strings;
    private List<String> urls;

    public GridAdapter(Context context, List<String> urls, List<String> strings) {
        this.context = context;
        this.strings = strings;
        this.urls = urls;
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
        if (urls.get(position).equals("更多")) {
            holder.imageView.setBackgroundResource(ic_baseline_widgets_24);

        } else {
            Glide.with(context)
                    .load(urls.get(position))
                    .placeholder(R.drawable.ic_baseline_refresh_24)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable
                                Transition<
                                        ? super Drawable> transition) {
                            holder.imageView.setImageDrawable(resource);
                        }
                    });
        }

        if (strings.size() != 0) {
            holder.textView.setText(strings.get(position));
        }

        holder.itemView.setOnClickListener((v -> {
            mOnItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
        }));

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            textView = itemView.findViewById(R.id.itemText);
        }
    }
}

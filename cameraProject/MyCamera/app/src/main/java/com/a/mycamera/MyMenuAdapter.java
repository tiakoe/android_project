package com.a.mycamera;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a.mycamera.view.HorizontalRecycleView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 选项适配器
 */
public class MyMenuAdapter extends RecyclerView.Adapter<MyMenuAdapter.ItemViewHolder>
        implements HorizontalRecycleView.IHorizontalRecycleView {
    private Context context;
    private View view;
    private List<String> arrayLists;
    private HorizontalRecycleView recyclerView;

    public MyMenuAdapter(Context context, List<String> arrayLists, HorizontalRecycleView recyclerView){
        this.context = context;
        this.arrayLists = arrayLists;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.tvName.setText(arrayLists.get(position));
    }

    @Override
    public int getItemCount() {
        return  arrayLists.size();
    }

    @Override
    public View getItemView() {
        return view;
    }

    @Override
    public void onViewSelected(boolean isSelected, int pos, RecyclerView.ViewHolder holder, int itemWidth) {
        if(isSelected) {
            ((ItemViewHolder) holder).tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            ((ItemViewHolder) holder).tvName.setTextColor(Color.WHITE);
            ((ItemViewHolder) holder).tvName.setAlpha(1.0f);
        }else{
            ((ItemViewHolder) holder).tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            ((ItemViewHolder) holder).tvName.setTextColor(Color.rgb(0xfe,0xfe,0xfe));
            ((ItemViewHolder) holder).tvName.setAlpha(0.5f);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        ItemViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvName.setTag(this);
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemViewHolder itemViewHolder = (ItemViewHolder)v.getTag();
                    int position = recyclerView.getChildAdapterPosition(itemViewHolder.itemView);
                    position--;
                    recyclerView.moveToPosition(position);
                }
            });
        }
    }
}

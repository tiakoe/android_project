package com.a.http_module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zy
 * date 2019/7/9 0009.
 * desc:
 */

public class ListAdapter extends BaseAdapter{

    private List<Msg> mList;
    private Context mContext;
    private SimpleDateFormat mSimpleDateFormat;

    @SuppressLint("SimpleDateFormat")
    public ListAdapter(Context context, List<Msg> list){
        this.mContext = context;
        this.mList = list;
        mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.aidl_item_list,parent,false);
            holder.mTextView = (TextView) convertView;
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Msg msg = mList.get(position);
        holder.mTextView.setText(mSimpleDateFormat.format(new Date(msg.getTime())) +": " + msg.getMsg());
        return convertView;
    }

    static class ViewHolder{
        TextView mTextView;
    }

}


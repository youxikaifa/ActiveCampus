package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thy.activecampus.R;
import com.thy.activecampus.model.SelectType;


import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class TypeAdapter extends BaseAdapter {

    Context context;
    List<SelectType> list;
    LayoutInflater inflater;

    public TypeAdapter(Context context, List<SelectType> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SelectType getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder =null;
        if (view==null){
            holder = new Holder();
            view = inflater.inflate(R.layout.item_select_type,null);
            holder.image = (ImageView) view.findViewById(R.id.iv_head);
            holder.decrip = (TextView) view.findViewById(R.id.decrip);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.image.setImageResource(list.get(position).getId());
        holder.decrip.setText(list.get(position).getTitle());
        return view;
    }

    class Holder{
        ImageView image;
        TextView decrip;
    }
}

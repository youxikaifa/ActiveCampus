package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.thy.activecampus.model.LabelM;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class MyPubAdapter extends BaseAdapter {

    public Context context;
    public List<LabelM> list;
    public LayoutInflater inflater;

    public MyPubAdapter(List<LabelM> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LabelM getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return null;
    }
}

package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thy.activecampus.R;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class TestAdapter extends BaseAdapter {

    Context context;
    List<String> labels;
    LayoutInflater inflater;

    public TestAdapter(Context context, List<String> labels) {
        this.context = context;
        this.labels = labels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public String getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_test,null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(labels.get(position));
        return tv;
    }
}

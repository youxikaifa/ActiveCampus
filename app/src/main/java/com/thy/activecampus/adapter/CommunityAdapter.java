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

public class CommunityAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater inflater;
    private List<String> list;

    public CommunityAdapter(Context mContext, List<String> list) {
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;

        if (convertView==null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_community,null);
            holder.type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.type.setText(list.get(position));


        return convertView;
    }


    static class Holder{
        TextView type;
    }
}

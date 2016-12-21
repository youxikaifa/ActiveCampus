package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thy.activecampus.R;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.RecentScanM;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class RecentScanAdapter extends BaseAdapter {

    Context context;
    List<LabelM> rList;
    LayoutInflater inflater;

    public RecentScanAdapter(Context context, List<LabelM> rList) {
        this.context = context;
        this.rList = rList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rList.size();
    }

    @Override
    public LabelM getItem(int i) {

        return rList.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (rList.size()>0){
            Holder holder = null;
            if (view == null) {
                holder = new Holder();
                view = inflater.inflate(R.layout.item_recent_scan, null);
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.rlDel = (RelativeLayout) view.findViewById(R.id.rlDel);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            holder.title.setText(rList.get(i).getTitle());
            holder.rlDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rList.remove(i);
                    notifyDataSetChanged();
                }
            });

            return view;
        }else{
            TextView tvNoResult = new TextView(context);
            tvNoResult.setText("未搜索到相关结果");
            return tvNoResult;
        }

    }


    class Holder {
        TextView title;
        RelativeLayout rlDel;
    }
}

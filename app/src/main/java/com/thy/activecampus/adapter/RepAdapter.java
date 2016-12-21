package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thy.activecampus.R;
import com.thy.activecampus.model.AutoComment;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class RepAdapter extends BaseAdapter {

    Context context;
    List<AutoComment> reps;
    LayoutInflater inflater;

    public RepAdapter(Context context, List<AutoComment> reps) {
        this.context = context;
        this.reps = reps;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reps.size();
    }

    @Override
    public AutoComment getItem(int position) {
        return reps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (reps.get(position).getType()==1){
            view = inflater.inflate(R.layout.item_auto_rep,null);

            TextView name = (TextView) view.findViewById(R.id.name);
            TextView content = (TextView) view.findViewById(R.id.content);
            TextView time = (TextView) view.findViewById(R.id.time);
            LinearLayout ll_item = (LinearLayout) view.findViewById(R.id.ll_item);

            name.setText(reps.get(position).getFrom_user().getName());
            content.setText(reps.get(position).getContent());
            time.setText(logicTime(reps.get(position).getCreated()));

        }else if (reps.get(position).getType()==2){
            view = inflater.inflate(R.layout.item_auto_rep_two,null);

            TextView fromName = (TextView) view.findViewById(R.id.fromName);
            TextView targetName = (TextView) view.findViewById(R.id.targetName);
            TextView content = (TextView) view.findViewById(R.id.content);
            TextView time = (TextView) view.findViewById(R.id.time);
            LinearLayout ll_item = (LinearLayout) view.findViewById(R.id.ll_item);

            fromName.setText(reps.get(position).getFrom_user().getName());
            targetName.setText(reps.get(position).getTarget_user().getName());
            content.setText(reps.get(position).getContent());
            time.setText(logicTime(reps.get(position).getCreated()));
        }


        return view;
    }

    /**
     * 动态发表时间
     * @param time 发表的时间
     * @return
     */
    public String logicTime(String time){
        long past = Long.parseLong(time);
        long now = System.currentTimeMillis();
        long pastTime = (now-past)/1000;
        StringBuffer sb = new StringBuffer();
        if (pastTime==0){
            return sb.append(1 + "秒前").toString() ;
        }
        if (pastTime > 0 && pastTime < 60) { // 1分钟内
            return sb.append(pastTime + "秒前").toString();
        } else if (pastTime > 60 && pastTime < 3600) {
            return sb.append(pastTime / 60+"分钟前").toString();
        } else if (pastTime >= 3600 && pastTime < 3600 * 24) {
            return sb.append(pastTime / 3600 +"小时前").toString();
        }else if (pastTime >= 3600 * 24 && pastTime < 3600 * 48) {
            return sb.append("昨天").toString();
        }else if (pastTime >= 3600 * 48 && pastTime < 3600 * 72) {
            return sb.append("前天").toString();
        }else if (pastTime >= 3600 * 72) {
            return sb.append("3天前").toString();
        }else{
            return "";
        }

    }


}

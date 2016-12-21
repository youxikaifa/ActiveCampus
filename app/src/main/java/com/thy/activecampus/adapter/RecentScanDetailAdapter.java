package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.RecentScanDM;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class RecentScanDetailAdapter extends BaseAdapter {

    Context context;
    List<LabelM> list;
    LayoutInflater inflater;

    public RecentScanDetailAdapter(Context context, List<LabelM> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LabelM getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null){
            holder = new Holder();
            view = inflater.inflate(R.layout.item_recent_scan_detail,null);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.content = (TextView) view.findViewById(R.id.content);
            holder.head = (SimpleDraweeView) view.findViewById(R.id.user_head);
            holder.time = (TextView) view.findViewById(R.id.time);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        if (list.get(i).getHead().substring(0,4).equals("http")){
            holder.head.setImageURI(list.get(i).getHead());
        }else{
            holder.head.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+list.get(i).getHead());
        }

        holder.title.setText(list.get(i).getTitle());
        holder.content.setText(list.get(i).getContent());
        holder.time.setText(logicTime(list.get(i).getPubTime()));
        return view;
    }

    class Holder{
        SimpleDraweeView head;
        TextView title,content,time;
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

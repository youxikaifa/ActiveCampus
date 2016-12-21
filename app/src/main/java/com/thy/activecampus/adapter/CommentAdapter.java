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
import com.thy.activecampus.model.Comment;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class CommentAdapter extends BaseAdapter {
    Context context;
    List<Comment.CommentsBean> list;
    LayoutInflater inflater;

    public CommentAdapter(Context context,List<Comment.CommentsBean> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Comment.CommentsBean getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_comment,null);
            holder.head = (SimpleDraweeView) convertView.findViewById(R.id.head);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.pubTime = (TextView) convertView.findViewById(R.id.pubTime);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        if (list.get(position).getHead().substring(0,4).equals("http")){
            holder.head.setImageURI(list.get(position).getHead());
        }else{
            holder.head.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+list.get(position).getHead());
        }

        holder.name.setText(list.get(position).getName());
        holder.pubTime.setText(logicTime(list.get(position).getPubTime()));
        holder.content.setText(list.get(position).getContent());
        return convertView;
    }

    class  Holder{
        SimpleDraweeView head;
        TextView name,pubTime,content;
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

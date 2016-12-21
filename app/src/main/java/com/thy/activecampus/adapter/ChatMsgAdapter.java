package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.Room;

import java.util.List;


/**
 * Created by Jin on 7/29.
 */

public class ChatMsgAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<Room> list;

    public ChatMsgAdapter(Context context, List<Room> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Room getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_chat_msg,null);
            holder.userIconleft = (SimpleDraweeView) convertView.findViewById(R.id.userIconleft);
            holder.nameleft = (TextView) convertView.findViewById(R.id.nameLeft);
            holder.msgleft = (TextView) convertView.findViewById(R.id.msgLeft);
            holder.timeleft = (TextView) convertView.findViewById(R.id.timeLeft);
            holder.userIconRight = (SimpleDraweeView) convertView.findViewById(R.id.userIconRight);
            holder.nameRight = (TextView) convertView.findViewById(R.id.nameRight);
            holder.msgright = (TextView) convertView.findViewById(R.id.msgRight);
            holder.timeRight = (TextView) convertView.findViewById(R.id.timeRight);
            holder.rlLeft = (RelativeLayout) convertView.findViewById(R.id.rl_recieve);
            holder.rlRight = (RelativeLayout) convertView.findViewById(R.id.rl_send);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        if(list.get(position).getBean().getType()==0){
            if (list.get(position).getBean().getThumb().substring(0,4).equals("http")){
                holder.userIconleft.setImageURI(list.get(position).getBean().getThumb());
            }else{
                holder.userIconleft.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+list.get(position).getBean().getThumb());
            }
            holder.rlLeft.setVisibility(View.VISIBLE);
            holder.rlRight.setVisibility(View.GONE);
            holder.nameleft.setText(list.get(position).getBean().getName());
            holder.msgleft.setText(list.get(position).getBean().getMsg());
            holder.timeleft.setText(logicTime(list.get(position).getBean().getCreated()));
        }else{
            if (list.get(position).getThumb().substring(0,4).equals("http")){
                holder.userIconRight.setImageURI(list.get(position).getBean().getThumb());
            }else{
                holder.userIconRight.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+list.get(position).getBean().getThumb());
            }
            holder.rlRight.setVisibility(View.VISIBLE);
            holder.rlLeft.setVisibility(View.GONE);

            holder.nameRight.setText(list.get(position).getBean().getName());
            holder.msgright.setText(list.get(position).getBean().getMsg());
            holder.timeRight.setText(logicTime(list.get(position).getBean().getCreated()));
        }

        return convertView;
    }

    class Holder{
        SimpleDraweeView userIconleft,userIconRight;
        TextView nameleft,nameRight,msgleft,msgright,timeleft,timeRight;
        RelativeLayout rlLeft,rlRight;
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

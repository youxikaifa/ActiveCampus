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
import com.thy.activecampus.model.Room;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class MsgAdapter extends BaseAdapter {

    Context context;
    List<Room> list;
    LayoutInflater inflater;

    public MsgAdapter(Context context, List<Room> list) {
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
            convertView = inflater.inflate(R.layout.item_follow,null);
            holder.head = (SimpleDraweeView) convertView.findViewById(R.id.head);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.name = (TextView) convertView.findViewById(R.id.recentMsg);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.recentMsg = (TextView) convertView.findViewById(R.id.recentMsg);
            holder.unReadNum = (TextView) convertView.findViewById(R.id.message_num);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        if (list.get(position).getUnReadNum()!=0){
            holder.unReadNum.setVisibility(View.VISIBLE);
            if (list.get(position).getUnReadNum()>99){
                holder.unReadNum.setText("+"+list.get(position).getUnReadNum());
            }else{
                holder.unReadNum.setText(list.get(position).getUnReadNum()+"");
            }
        }else{
            holder.unReadNum.setVisibility(View.GONE);
        }
        if (list.get(position).getThumb().substring(0,4).equals("http")){
            holder.head.setImageURI(list.get(position).getThumb());
        }else{
            holder.head.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+list.get(position).getThumb());
        }

        holder.title.setText(list.get(position).getTitle());
        if (list.get(position).getBean()!=null){
            holder.name.setText(list.get(position).getBean().getName());
            holder.time.setText(logicTime(list.get(position).getBean().getCreated()));
            holder.recentMsg.setText(list.get(position).getBean().getMsg());
        }
        return convertView;
    }

    class Holder{
        SimpleDraweeView head;
        TextView title,name,time,unReadNum,recentMsg;
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

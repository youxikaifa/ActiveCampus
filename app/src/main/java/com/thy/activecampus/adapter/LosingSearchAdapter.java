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
import com.thy.activecampus.model.Losing;
import com.thy.activecampus.ui.activity.LosingA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class LosingSearchAdapter extends BaseAdapter{

    Context context;
    List<Losing> list;
    LayoutInflater inflater;
    SimpleDateFormat sdf;

    public LosingSearchAdapter(Context context, List<Losing> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Losing getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder = null;
        if (view==null){
            holder = new Holder();
            view = inflater.inflate(R.layout.item_losing,null);
            holder.img = (SimpleDraweeView) view.findViewById(R.id.view_pic);
            holder.head = (SimpleDraweeView) view.findViewById(R.id.view_head);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.phone = (TextView) view.findViewById(R.id.phone);
            holder.addr = (TextView) view.findViewById(R.id.addr);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.type = (TextView) view.findViewById(R.id.type);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        if (list.get(position).getThumbs()!=null){
            holder.img.setImageURI(MyConstants.BASE_URL+list.get(position).getThumbs().get(0));
        }
        holder.head.setImageURI(MyConstants.BASE_URL+list.get(position).getHead());
        holder.title.setText(list.get(position).getTitle());
        holder.name.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getContact());
        holder.addr.setText(list.get(position).getAddr());
        holder.time.setText(time2Date(list.get(position).getTime()));
        if (list.get(position).getType()==0){
            holder.type.setText("失物");
            holder.type.setBackgroundResource(R.drawable.xml_losing_background);
        }else{
            holder.type.setText("招领");
            holder.type.setBackgroundResource(R.drawable.xml_get_background);
        }
        return view;
    }


    class Holder{
        SimpleDraweeView img,head;
        TextView title,name,addr,phone,time,type;
    }

    /**
     * 动态发表时间
     * @param time 发表的时间
     * @return
     */
    public String time2Date(String time){

        return sdf.format(new Date(Long.parseLong(time)));
    }
}


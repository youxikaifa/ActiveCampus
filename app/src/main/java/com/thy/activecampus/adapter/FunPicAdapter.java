package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.model.FunPic;

import java.util.List;


/**
 * Created by Jin on 7/29.
 */

public class FunPicAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<FunPic.ResultBean.DataBean>funpics ;

    public FunPicAdapter(Context context, List<FunPic.ResultBean.DataBean> funpics) {
        this.context = context;
        this.funpics = funpics;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return funpics.size();
    }

    @Override
    public FunPic.ResultBean.DataBean getItem(int position) {
        return funpics.get(position);
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
            view = inflater.inflate(R.layout.item_funpic,null);
            holder.content = (TextView) view.findViewById(R.id.tv_content);
            holder.time = (TextView) view.findViewById(R.id.tv_time);
            holder.viewPic = (SimpleDraweeView) view.findViewById(R.id.view_pic);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(funpics.get(position).getUrl())
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();

        holder.content.setText(funpics.get(position).getContent());
        holder.time.setText(funpics.get(position).getUpdatetime());
        holder.viewPic.setController(draweeController);
        return view;
    }

    class Holder{
        TextView content,time;
        SimpleDraweeView viewPic;
    }
}

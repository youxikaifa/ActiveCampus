package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.model.News;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class NewsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<News.ResultBean.DataBean> news ;

    public NewsAdapter(Context context, List<News.ResultBean.DataBean> news) {
        this.context = context;
        this.news = news;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public News.ResultBean.DataBean getItem(int position) {
        return news.get(position);
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
            view = inflater.inflate(R.layout.item_news,null);
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.time = (TextView) view.findViewById(R.id.tv_time);
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            holder.type = (TextView) view.findViewById(R.id.tv_type);
            holder.viewPic = (SimpleDraweeView) view.findViewById(R.id.view_pic);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.title.setText(news.get(position).getTitle());
        holder.time.setText(news.get(position).getDate());
        holder.viewPic.setImageURI(news.get(position).getThumbnail_pic_s());
        holder.name.setText("来自· "+news.get(position).getAuthor_name());
        holder.type.setText(news.get(position).getRealtype());
        return view;
    }

    class Holder{
        TextView title,time,name,type;
        SimpleDraweeView viewPic;
    }
}

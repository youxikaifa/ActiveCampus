package com.thy.activecampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thy.activecampus.R;
import com.thy.activecampus.model.Joke;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class JokeAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<Joke.ResultBean.DataBean> jokes;

    public JokeAdapter(Context context, List<Joke.ResultBean.DataBean> jokes) {
        this.context = context;
        this.jokes = jokes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return jokes.size();
    }

    @Override
    public Joke.ResultBean.DataBean getItem(int position) {
        return jokes.get(position);
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
            view = inflater.inflate(R.layout.item_joke,null);
            holder.content = (TextView) view.findViewById(R.id.tv_content);
            holder.time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.content.setText(jokes.get(position).getContent());
        holder.time.setText(jokes.get(position).getUpdatetime());

        return view;
    }

    class Holder{
        TextView content,time;
    }
}

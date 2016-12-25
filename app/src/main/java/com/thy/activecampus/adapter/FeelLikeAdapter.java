package com.thy.activecampus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class FeelLikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<String> list;
    LayoutInflater inflater;

    public FeelLikeAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_feel_like,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.viewHead.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ItemViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView  viewHead;

        public ItemViewHolder(View view){
            super(view);
            viewHead = (SimpleDraweeView) view.findViewById(R.id.view_head);
        }
    }
}

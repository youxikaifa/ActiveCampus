package com.thy.activecampus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.model.Image;
import com.thy.activecampus.util.ImgLayoutUtil;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class PicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;
    LayoutInflater inflater;
    List<Image> images;

    public PicsAdapter(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_select_pic, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (position==images.size()){
            itemViewHolder.ivSelect.setImageResource(R.drawable.ic_add_pic);
            itemViewHolder.ivCancle.setVisibility(View.GONE);
            itemViewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCallBack.onAddClick(position);
                }
            });
        }else{
            Bitmap bitmap = ImgLayoutUtil.getImageThumbnail(images.get(position).getPath(), ImgLayoutUtil.getWidth(context)+600, ImgLayoutUtil.getHeight(context));
            itemViewHolder.ivSelect.setImageBitmap(bitmap);
            itemViewHolder.ivCancle.setVisibility(View.VISIBLE);
            itemViewHolder.ivCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCallBack.onCancleClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return images.size() + 1;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSelect,ivCancle;

        public ItemViewHolder(View view) {
            super(view);
            ivSelect = (ImageView) view.findViewById(R.id.ivSelect);
            ivCancle = (ImageView) view.findViewById(R.id.iv_cancle);
        }
    }

    public interface AddCallBack{
        void onAddClick(int position);
        void onCancleClick(int position);
    }
    public AddCallBack addCallBack;

    public void setACallback(AddCallBack callback){
        this.addCallBack = callback;
    }


}

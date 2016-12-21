package com.thy.activecampus.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.thy.activecampus.R;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.ui.activity.AutoDyneDetailA;
import com.thy.activecampus.ui.activity.AutoDyneDetailA_;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class AutoDyneAdapter extends BaseAdapter implements View.OnClickListener {

    Context context;
    LayoutInflater inflater;
    List<AutoDyne> list;

    public AutoDyneAdapter(Context context, List<AutoDyne> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_like:
                cCallBack.likeClick();
                break;
            case R.id.rl_share:
                cCallBack.shareClick();
                break;
            default:break;
        }
    }


    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     *
     * @author Ivan Xu
     *         2014-11-26
     */
    public interface ChildCallBack {
        void shareClick();
        void likeClick();
    }

    public ChildCallBack cCallBack;
    public void setcCallBack(ChildCallBack cCallBack){
        this.cCallBack = cCallBack;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AutoDyne getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder = null;

        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.item_autodyne, null);
            holder.viewPic = (SimpleDraweeView) view.findViewById(R.id.view_pic);
            holder.viewHead = (SimpleDraweeView) view.findViewById(R.id.view_head);
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            holder.time = (TextView) view.findViewById(R.id.tv_time);
            holder.type = (TextView) view.findViewById(R.id.tv_type);
            holder.likeNum = (TextView) view.findViewById(R.id.likeNum);
            holder.scanNum = (TextView) view.findViewById(R.id.scanNum);
            holder.picNum = (TextView) view.findViewById(R.id.picNum);
            holder.share = (RelativeLayout) view.findViewById(R.id.rl_share);
            holder.comment = (RelativeLayout) view.findViewById(R.id.rl_comment);
            holder.like = (RelativeLayout) view.findViewById(R.id.rl_like);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        ControllerListener controllerListener = getListener(holder.viewPic);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(Uri.parse(list.get(position).getPics().get(0)))
                .build();
        holder.viewPic.setController(controller);
        holder.viewHead.setImageURI(list.get(position).getPics().get(0));
        holder.name.setText(list.get(position).getName());
        holder.time.setText(list.get(position).getTime());
        holder.type.setText(list.get(position).getType());
        holder.likeNum.setText(list.get(position).getLikes() + "");
        holder.scanNum.setText(list.get(position).getComments() + "");
        holder.picNum.setText(list.get(position).getPics().size() + "");

        holder.like.setOnClickListener(this);
        holder.share.setOnClickListener(this);
        return view;
    }

    final class Holder {
        SimpleDraweeView viewPic, viewHead;
        TextView name, time, type, likeNum, scanNum, picNum;
        RelativeLayout share, comment, like;
    }



    public ControllerListener getListener(final SimpleDraweeView view){
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = 1080;
                layoutParams.height = (int) ((float) (1080 * height) / (float) width);
                view.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                Log.d("TAG", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        return  controllerListener;
    }



}

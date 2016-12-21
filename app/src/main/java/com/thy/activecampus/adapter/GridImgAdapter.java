package com.thy.activecampus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class GridImgAdapter extends BaseAdapter {

    Context context;
    List<String> list;
    LayoutInflater inflater;

    public GridImgAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        if (list.size() > 6) {
            return 6;
        }
        return list.size();
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.item_grid_last_notice, null);
            holder.draweeView = (SimpleDraweeView) view.findViewById(R.id.lastImg);
            holder.notice = (TextView) view.findViewById(R.id.notice);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setLowResImageRequest(ImageRequest.fromUri("http://pic44.nipic.com/20140717/2531170_194615292000_2.jpg"))
//                .setImageRequest(ImageRequest.fromUri("http://192.168.1.111:3000"+list.get(i)))
//                .setOldController(holder.draweeView.getController())
//                .build();
//        holder.draweeView.setController(controller);
        holder.draweeView.setImageURI(MyConstants.BASE_URL + list.get(i));
        holder.draweeView.setAspectRatio(1.0f); //设置宽高比
        if (i == 5 && list.size()>6) {
            holder.notice.setVisibility(View.VISIBLE);
            holder.notice.setText("+" + (list.size() - 6));
        }
        return view;
    }

    class Holder {
        SimpleDraweeView draweeView;
        TextView notice;
    }
}

package com.thy.activecampus.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class MyPagerAdapter extends PagerAdapter {

    public List<String> list;
    public Context context;
    public LayoutInflater inflater;

    public MyPagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.item_slide_pager, null);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.view_img);
        TextView tvReduce = (TextView) view.findViewById(R.id.tv_reduce);
        if (list.get(position).substring(0,4).equals("http")){
            simpleDraweeView.setImageURI(list.get(position));
        }else{
            simpleDraweeView.setImageURI(MyConstants.BASE_URL+list.get(position));
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

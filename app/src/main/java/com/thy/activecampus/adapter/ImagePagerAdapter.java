package com.thy.activecampus.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.common.MyConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class ImagePagerAdapter extends PagerAdapter {

    List<String> list;
    Context context;
    List<SimpleDraweeView> views = new ArrayList<>();

    public ImagePagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        initView();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(views.get(position));
    }

    @Override
    public SimpleDraweeView instantiateItem(ViewGroup container, int position) {

        container.addView(views.get(position));
        return views.get(position);
    }

    private void initView(){
        for (int i = 0; i < list.size(); i++) {
            String url = list.get(i);
            SimpleDraweeView curView = new SimpleDraweeView(context);
            curView.setImageURI(MyConstants.BASE_URL+"/images/upload/" + url.substring(url.lastIndexOf("/") + 1));
//            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            curView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            views.add(curView);
        }
    }
}

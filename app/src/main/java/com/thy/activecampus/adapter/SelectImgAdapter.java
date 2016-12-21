package com.thy.activecampus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.thy.activecampus.R;
import com.thy.activecampus.model.Image;
import com.thy.activecampus.util.ImgLayoutUtil;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class SelectImgAdapter extends BaseAdapter {
    private Context context;
    private List<Image> imgPath;

    public SelectImgAdapter(Context context, List<Image> imgPath) {
        this.context = context;
        this.imgPath = imgPath;
    }


    @Override
    public int getCount() {
        return imgPath.size();
    }

    @Override
    public Image getItem(int position) {
        return imgPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView iv_image;
        if (convertView == null) {//创建ImageView
            iv_image = new ImageView(context);
            iv_image.setLayoutParams(new AbsListView.LayoutParams(ImgLayoutUtil.getWidth(context) / 3 - 5, ImgLayoutUtil.getWidth(context) / 3 - 5) );
            iv_image.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            convertView = iv_image;
        }else{
            iv_image = (ImageView) convertView;
        }
        if (imgPath.get(position).getId()==0){
            iv_image.setImageResource(R.drawable.ic_add_img);
        }else {
            Bitmap bitmap = ImgLayoutUtil.getImageThumbnail(getItem(position).getPath(), ImgLayoutUtil.getWidth(context) / 3 - 5, ImgLayoutUtil.getWidth(context) / 3 - 5);
            iv_image.setImageBitmap(bitmap);
        }
        return convertView;
    }
}

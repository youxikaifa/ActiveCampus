package com.thy.activecampus.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class BaseF extends Fragment {
    public static final List<String> newsList = new ArrayList<>();
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null){
            boolean isSuuportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSuuportHidden){
                ft.hide(this);
            }else{
                ft.show(this);
            }
            ft.commit();
        }

        newsList.add("http://ww2.sinaimg.cn/large/794398dcjw1ejyc0lqgd2j20rs0e6wga.jpg");
        newsList.add("http://d.hiphotos.baidu.com/zhidao/pic/item/e7cd7b899e510fb38cc9205dd833c895d0430c23.jpg");
        newsList.add("http://image.tianjimedia.com/uploadImages/2012/228/46/IZ124LVX8814.jpg");
        newsList.add("http://imgsrc.baidu.com/forum/pic/item/e7cd7b899e510fb3f2c2ab27d933c895d0430ca3.jpg");
        newsList.add("http://img.zcool.cn/community/01049d5542d5020000019ae9d1f0f0.jpg");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN,isHidden());
    }
}

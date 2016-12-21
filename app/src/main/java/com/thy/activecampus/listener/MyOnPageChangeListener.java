package com.thy.activecampus.listener;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jin on 7/29.
 */

public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    ImageView ind;
    TextView t1;
    TextView t2;

    public MyOnPageChangeListener(ImageView ind, TextView t1, TextView t2) {
        this.ind = ind;
        this.t1 = t1;
        this.t2 = t2;
    }


    @Override
    public void onPageSelected(int index) {
        Animation animation = null;
        switch (index){
            case 0:
                animation = new TranslateAnimation(540, 0, 0, 0);
                t2.setTextColor(Color.parseColor("#55ffffff"));
                t1.setTextColor(Color.WHITE);
                break;
            case 1:
                animation = new TranslateAnimation(270, 540, 0, 0);
                t2.setTextColor(Color.WHITE);
                t1.setTextColor(Color.parseColor("#55ffffff"));
                break;
            default: break;
        }
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        ind.startAnimation(animation);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
}

package com.thy.activecampus.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.FixedSpeedScroller;
import com.thy.activecampus.adapter.MyPagerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jin on 7/29.
 */

public class SlideView extends FrameLayout {

    Context context;
    ViewPager viewPager;
    MyPagerAdapter adapter;
    List<String> list;
    int curPosition = 1;
    int mWidth,mHeight;

    private ScheduledExecutorService scheduledExecutorService;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(curPosition,true);

        }
    };

    public SlideView(Context context) {
        this(context,null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }else if(widthMode == MeasureSpec.AT_MOST){
            throw new IllegalArgumentException("width must be EXACTLY,you should set like android:width=\"200dp\"");
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }else if(widthMeasureSpec == MeasureSpec.AT_MOST){

            throw new IllegalArgumentException("height must be EXACTLY,you should set like android:height=\"200dp\"");
        }

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setData(List<String> imgs){

        initData(imgs);
        initView();
        startPlay();
    }





    protected void initView() {

        LayoutInflater.from(context).inflate(R.layout.layout_slideview, this,true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        controlViewPagerSpeed();


        adapter = new MyPagerAdapter(context, list);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == 0) {
                    if (curPosition == list.size() - 1) {
                        viewPager.setCurrentItem(1, false);
                    }
                    if (curPosition == 0) {
                        viewPager.setCurrentItem(list.size() - 2, false);
                    }
                }


            }
        });

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    public void initData(List<String> images) {
        list = new ArrayList<>();
        if (images.size()>0){
            list.add(images.get(images.size()-1));
            list.addAll(images);
            list.add(images.get(0));
        }else{
            list.add("http://img.redocn.com/sheji/20150504/qingchunsibianhuixiaoyuanhuodongzhanban_4248976.jpg");
            list.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1479890341&di=dafc7e73776401a6719c2fdd06621bf3&src=http://www.ahjhzx.cn/upload/bimg/2011100637800509.jpg");
            list.add("http://pic.58pic.com/58pic/11/01/50/24N58PICTDi.jpg");
            list.add("http://img.redocn.com/sheji/20150714/xiaoyuanyundonghuihuodonghaibaosheji_4652548.jpg");
            list.add("http://www.jitu5.com/uploads/allimg/101015/449-101015121H00.jpg");
            list.add("http://img.redocn.com/sheji/20150504/qingchunsibianhuixiaoyuanhuodongzhanban_4248976.jpg");
            list.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1479890341&di=dafc7e73776401a6719c2fdd06621bf3&src=http://www.ahjhzx.cn/upload/bimg/2011100637800509.jpg");
        }

    }

    private void controlViewPagerSpeed() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),
                    new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(1500);
        } catch (Exception e) {
            Log.e("ERR", "", e);
        }
    }


    public void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideTask(), 2, 8, TimeUnit.SECONDS);
    }

    class SlideTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager){
                curPosition++;
                handler.obtainMessage().sendToTarget();
            }
        }
    }
}

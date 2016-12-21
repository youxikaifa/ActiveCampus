package com.thy.activecampus.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.ImagePagerAdapter;
import com.thy.activecampus.common.MyConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class PagerDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private List<String> uris;
    private ViewPager imgVp;
    private ImageView save;
    private RelativeLayout back;
    private TextView curPage;
    private TextView totalPage;
    private RelativeLayout rlDetail;
    private List<View> views = new ArrayList<>();;
    private ImagePagerAdapter adapter;
    private int position;

    public PagerDialog(Context context, List<String> uris,int position) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.uris = uris;
        this.position = position;
    }

    public PagerDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected PagerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_show_picture, null);
        this.setContentView(view);



//        init();
        findView(view);
        initViews();

        adapter = new ImagePagerAdapter(mContext,uris);
        imgVp.setAdapter(adapter);
        imgVp.setCurrentItem(position);
        imgVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPage.setText(position+1+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void findView(View view) {
        imgVp = (ViewPager) view.findViewById(R.id.imgVp);
        back = (RelativeLayout) view.findViewById(R.id.rlBack);
        save = (ImageView) view.findViewById(R.id.iv_save);
        rlDetail = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        curPage = (TextView) view.findViewById(R.id.tv_cur_page);
        totalPage = (TextView) view.findViewById(R.id.tv_total_page);
    }

    private void initViews() {
        curPage.setText(position+1+"");
        totalPage.setText(uris.size()+"");

        Animation backAnimotion = new TranslateAnimation(0, 0, -50, 0);
        backAnimotion.setDuration(1000);
        back.startAnimation(backAnimotion);

        Animation bottomAnimation = new TranslateAnimation(0, 0, 50, 0);
        bottomAnimation.setDuration(1000);
        rlDetail.startAnimation(bottomAnimation);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBack:
                this.dismiss();
                break;
            case R.id.iv_save:
                Toast.makeText(mContext, "保存", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


}

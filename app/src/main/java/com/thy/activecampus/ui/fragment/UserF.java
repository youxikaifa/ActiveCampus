package com.thy.activecampus.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.base.BaseF;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.User;
import com.thy.activecampus.ui.activity.LoginA;
import com.thy.activecampus.ui.activity.LoginA_;
import com.thy.activecampus.ui.activity.RecentScanA_;
import com.thy.activecampus.ui.activity.ReviseResA_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */
@EFragment(R.layout.fragment_main_my)
public class UserF extends BaseF{


    @ViewById(R.id.view_head)
    SimpleDraweeView head;
    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.tv_reduce)
    TextView tvReduce;
    @ViewById(R.id.following)
    TextView followingNum; //我关注的人数
    @ViewById(R.id.fans)
    TextView fansNum; //关注我的人数
    @ViewById(R.id.tv_recent_scan)
    TextView recentScan;
    @ViewById(R.id.tv_collect)
    TextView tvCollect;
    @ViewById(R.id.rl_revise_res)
    RelativeLayout rlRevise;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;


    public ACache cache;
    public User user;
    public List<LabelM> labelMList = new ArrayList<>();

    @AfterViews
    public void initViews(){

        loadLocalData();

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cache = ACache.get(getActivity(), MyConstants.USER_INFO);
                User u = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
                onReload(u);
            }
        });


    }

    @Background
    public void loadLocalData() {
        cache = ACache.get(getActivity(), MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (user.getThumb()!=null&&user.getThumb().substring(0,4).equals("http")){
                    head.setImageURI(user.getThumb());
                }else{
                    head.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+user.getThumb());
                }
                tvName.setText(user.getName());
                followingNum.setText(user.getFollowing().size()+"");
                fansNum.setText(user.getFans().size()+"");
                tvReduce.setText(user.getReduce().toString());
                tvCollect.setText(user.getCollections().size()+"");
                recentScan.setText(labelMList.size()+"");

            }
        });

    }

    @Click(R.id.ll_follow)
    void follow(){

    }

    @Click(R.id.ll_fans)
    void fans(){

    }

    @Click(R.id.rl_collection)
    void collect(){
        Toast.makeText(getActivity(), "我的收藏", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.rl_recent)
    void recent(){
        startActivity(new Intent(getActivity(), RecentScanA_.class));
    }

    @Click(R.id.rl_feedback)
    void feedback(){

    }

    @Click(R.id.rl_share)
    void share(){

    }

    @Click(R.id.rl_set)
    void set(){

    }

    @Click(R.id.rl_revise_res)
    void revise(){
        startActivity(new Intent(getActivity(), ReviseResA_.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.btn_exit)
    void exit(){
        cache.clear();
        startActivity(new Intent(getActivity(), LoginA_.class));
        getActivity().finish();
    }


    @Subscribe(sticky = true)
    @Background
    public void onReload(final User u) {
        cache = ACache.get(getActivity(), MyConstants.USER_INFO);
        JSONArray json = cache.getAsJSONArray(MyConstants.LABEL_RECENT_SCAN);
        if (json != null) {
            labelMList = new Gson().fromJson(json.toString(), new TypeToken<ArrayList<LabelM>>() {}.getType());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (u.getThumb().length()>0&&u.getThumb().substring(0,4).equals("http")){
                    head.setImageURI(u.getThumb());
                }else{
                    head.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+u.getThumb());
                }
                tvName.setText(u.getName());
                tvReduce.setText(u.getReduce().toString());

                followingNum.setText(u.getFollowing().size()+"");
                fansNum.setText(u.getFans().size()+"");
                tvCollect.setText(u.getCollections().size()+"");
                recentScan.setText(labelMList.size()+"");

                mSwipeLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}

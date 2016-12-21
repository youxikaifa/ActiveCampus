package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.thy.activecampus.R;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.Room;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.widget.SlideView;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_label_detail)
public class LabelDetailA extends BaseA {

    private LabelReqImpl request = LabelReqImpl.getInstance();

    @ViewById(R.id.likeNum)
    TextView likeNum;

    @ViewById(R.id.followNum)
    TextView followNum;
    @ViewById(R.id.tv_join)
    TextView join;
    @ViewById(R.id.view_user)
    SimpleDraweeView viewUser;
    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.tv_pubtime)
    TextView tvTime;
    @ViewById(R.id.tv_content)
    TextView tvContent;
    @ViewById(R.id.lv_comment)
    ListView lvComment;
    @ViewById(R.id.tv_notice)
    TextView tvNotice;
    @ViewById(R.id.tv_join)
    TextView tvJoin;
    @ViewById(R.id.iv_follow)
    ImageView follow;
    @ViewById(R.id.iv_like)
    ImageView like;
    @ViewById(R.id.slideview)
    SlideView slideView;

    LabelM labelM;
    ACache cache;
    User user;
    boolean isLike;
    boolean isFollow;
    int curLike = 0;
    int curFollow = 0;

    List<Room> localRooms = new ArrayList<>();


    @AfterViews
    public void initViews() {
        cache = ACache.get(this, MyConstants.USER_INFO);

        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
        slideView.setData(labelM.getPicUrls());
        init();

    }

    private void init() {
        curFollow = labelM.getFollowers().size();
        curLike = labelM.getLikes().size();
        if (labelM.getHead().substring(0,4).equals("http")){
            viewUser.setImageURI(labelM.getHead());
        }else{
            viewUser.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+labelM.getHead());
        }

        if (labelM.getFollowers()!=null){
            if (labelM.getFollowers().contains(user.get_id())) {
                follow.setImageResource(R.drawable.follow_active);
                followNum.setTextColor(Color.parseColor("#11cd6e"));
                isFollow = true;
            } else {
                follow.setImageResource(R.drawable.follow);
                followNum.setTextColor(Color.parseColor("#a9b7b7"));
                isFollow = false;
            }
            followNum.setText(labelM.getFollowers().size() + "");
        }

        if (labelM.getLikes()!=null){
            if (labelM.getLikes().contains(user.get_id())) {
                like.setImageResource(R.drawable.like_active);
                likeNum.setTextColor(Color.parseColor("#11cd6e"));
                isLike = true;
            } else {
                like.setImageResource(R.drawable.like);
                likeNum.setTextColor(Color.parseColor("#a9b7b7"));
                isLike = false;
            }
            likeNum.setText(labelM.getLikes().size() + "");
        }





        tvName.setText(labelM.getName());
        tvTime.setText(logicTime(labelM.getPubTime()));
        tvContent.setText(labelM.getContent());

        if (labelM.getUserId().equals(user.get_id())){
            tvNotice.setVisibility(View.GONE);
        }else{
            if (user.getFollowing()!=null && user.getFollowing().contains(labelM.getUserId())){
                tvNotice.setText("已关注");
            }else {
                tvNotice.setText("+关注");
            }
        }

    }

    @Click(R.id.back)
    void back() {
        this.finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEventMainThread(LabelM model) {
        this.labelM = model;

    }


    @Click(R.id.rl_follow)
    @Background
    void collect() {

        if (user.getCollections().contains(labelM.get_id())) {
            request.unCollect(MyConstants.BASE_URL,user.get_id(),labelM.get_id(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.v(">>>>>>>>>>>>>", e.getLocalizedMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            follow.setImageResource(R.drawable.follow);
                            followNum.setTextColor(Color.parseColor("#a9b7b7"));
                            followNum.setText(labelM.getFollowers().size()-1+"");
                            labelM.getFollowers().remove(user.get_id());

                            user.getCollections().remove(labelM.get_id());
                            cache.put(MyConstants.USER_MESSAGE,user);
                        }
                    });

                }
            });
        } else {
            request.collect(MyConstants.BASE_URL, user.get_id(),labelM.get_id(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.v(">>>>>>>>>>>>>", e.getLocalizedMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String json = response.body().string();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            follow.setImageResource(R.drawable.follow_active);
                            followNum.setTextColor(Color.parseColor("#11cd6e"));
                            followNum.setText(labelM.getFollowers().size()+1+"");

                            labelM.getFollowers().add(user.get_id());

                            user.getCollections().add(labelM.get_id());
                            cache.put(MyConstants.USER_MESSAGE,user);
                        }
                    });


                }
            });
        }

    }

    @Click(R.id.rl_like)
    @Background
    void like() {
        final String user_id = user.get_id();
        String label_id = labelM.get_id();

        if (labelM.getLikes().contains(user.get_id())) {
            request.unLike(MyConstants.BASE_URL , user_id, label_id, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.v(">>>>>>>>>>>>>", e.getLocalizedMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            like.setImageResource(R.drawable.like);
                            likeNum.setTextColor(Color.parseColor("#a9b7b7"));
                            likeNum.setText(labelM.getLikes().size()-1+"");
                            labelM.getLikes().remove(user_id);
                        }
                    });

                }
            });
        } else {
            request.like(MyConstants.BASE_URL , user_id, label_id, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String json = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            like.setImageResource(R.drawable.like_active);
                            likeNum.setTextColor(Color.parseColor("#11cd6e"));
                            likeNum.setText(labelM.getLikes().size()+1+"");
                            labelM.getLikes().add(user_id);
                        }
                    });


                }
            });
        }
    }

    @Click(R.id.rl_comment)
    void comment() {
        Intent intent = new Intent(this, CommentA_.class);
        intent.putExtra("LabelID", labelM.get_id());
        intent.putExtra("Title", labelM.getTitle());
        startActivity(intent);
    }

    @Click(R.id.tv_notice)
    @Background
    void notice() {
        String my_id = user.get_id();
        final String other_id = labelM.getUserId();
        if (user.getFollowing()!=null&&user.getFollowing().contains(other_id)){
            request.unnotice(MyConstants.BASE_URL, my_id, other_id, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvNotice.setText("+关注");
                        }
                    });
                    String json = response.body().string();
                    user = new Gson().fromJson(json,User.class);
                    cache.put(MyConstants.USER_MESSAGE,user);
                }
            });
        }else{
            request.notice(MyConstants.BASE_URL, my_id, other_id, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvNotice.setText("已关注");
                        }
                    });
                    String json = response.body().string();
                    user = new Gson().fromJson(json,User.class);
                    cache.put(MyConstants.USER_MESSAGE,user);
                }
            });
        }
    }

    @Click(R.id.tv_join)
    void join() {
        Intent intent = new Intent(this, ChatRoomA_.class);
        intent.putExtra("label_id",labelM.get_id());
        intent.putExtra("label_thumb",labelM.getHead());
        intent.putExtra("label_title",labelM.getTitle());
        intent.putExtra("message_id","");
        startActivity(intent);
        request.joinRoom(MyConstants.BASE_URL_ANOTHER_PORT, user.get_id(),labelM.get_id(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String back = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        user = new Gson().fromJson(back,User.class);
                        cache.put(MyConstants.USER_MESSAGE,user);
                    }
                });
            }
        });
    }


    @Click(R.id.view_user)
    void user() {
        Intent intent = new Intent(new Intent(this, UserHomePageA_.class));
        intent.putExtra("user_id",labelM.getUserId());
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}

package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.RecentAdapter;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.OnItemClickListener;
import com.thy.activecampus.model.Image;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.net.okReq.LabelReq;
import com.thy.activecampus.view.MyListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_user_homepage)
public class UserHomePageA extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    public static final LabelReqImpl request = LabelReqImpl.getInstance();
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout swipeLayout;
    @ViewById(R.id.scroll_view)
    ScrollView scrollView;
    @ViewById(R.id.lv_user_pub)
    RecyclerView lvMyActive;
    @ViewById(R.id.followNum)
    TextView followNum;
    @ViewById(R.id.fenNum)
    TextView fenNum;
    @ViewById(R.id.iv_add_friend)
    ImageView ivAddFriend;
    @ViewById(R.id.tv_friend)
    TextView tvFriend;

    public ACache cache;
    public User user;
    public User otherUser;

    public RecentAdapter adapter;

    public List<LabelM> labels;
    public List<LabelM> tempLabel;
    public LinearLayoutManager llManager = new LinearLayoutManager(this);
    public String userId = null;
    public int lastVisibleItem = 0;

    @AfterViews
    public void initView(){

        initLocalData();

        scrollView.smoothScrollTo(0,0);
        swipeLayout.setOnRefreshListener(this);
        adapter = new RecentAdapter(this,labels);
        lvMyActive.setLayoutManager(llManager);
        lvMyActive.setAdapter(adapter);
        this.adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().postSticky(labels.get(position));
                startActivity(new Intent(UserHomePageA.this, LabelDetailA_.class));
            }
        });
        lvMyActive.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == adapter.getItemCount()) {

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = llManager.findLastVisibleItemPosition();
            }
        });
        loadData();
    }

    private void initLocalData() {
        labels = new ArrayList<>();
        cache = ACache.get(this, MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
        userId = getIntent().getStringExtra("user_id");
        getUserInfo();
    }

    @Background
    public void getUserInfo(){
        request.getUserInfo(MyConstants.BASE_URL_ANOTHER_PORT, userId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                otherUser = new Gson().fromJson(json,User.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (otherUser.getFriend()!=null){
                            if (otherUser.getFriend().contains(user.get_id())){
                                ivAddFriend.setImageResource(R.drawable.ic_add_friend);
                            }
                        }
                        if (otherUser.getFollowed()!=null){
                            followNum.setText(otherUser.getFollowed()+"");
                        }
                        if (otherUser.getFans()!=null){
                            fenNum.setText(otherUser.getFans()+"");
                        }
                    }
                });
            }
        });
    }


    @Background
    public void loadData(){
        request.getMyPub(MyConstants.BASE_URL, user.get_id(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                tempLabel = new Gson().fromJson(json,new TypeToken<ArrayList<LabelM>>() {}.getType());
                labels.clear();
                labels.addAll(tempLabel);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeLayout.setRefreshing(false);
                    }
                });
            }
        });
    }


    @Click(R.id.rl_add_friend)
    void friend(){
        if (!user.getFriend().contains(userId)){
            request.addFriend(MyConstants.BASE_URL_ANOTHER_PORT, userId, otherUser.get_id(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String back = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserHomePageA.this, back, Toast.LENGTH_SHORT).show();
                            ivAddFriend.setImageResource(R.drawable.ic_add_friend);
                            tvFriend.setText("已是好友");
                        }
                    });

                }
            });
        }else{
            request.addFriend(MyConstants.BASE_URL_ANOTHER_PORT, userId, otherUser.get_id(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String back = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserHomePageA.this, back, Toast.LENGTH_SHORT).show();
                            ivAddFriend.setImageResource(R.drawable.ic_add_friend_black);
                        }
                    });

                }
            });
        }

    }

    @Click(R.id.rl_add_follow)
    void follow(){
        Toast.makeText(this, "关注", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.rl_add_fensi)
    void fensi(){
        Toast.makeText(this, "粉丝", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRefresh() {
        loadData();
    }
}

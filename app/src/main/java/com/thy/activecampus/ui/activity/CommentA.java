package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.CommentAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.Comment;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_comment)
public class CommentA extends BaseA {

    public LabelReqImpl request = LabelReqImpl.getInstance();
    @ViewById(R.id.srl_comment)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewById(R.id.lv_comment)
    ListView lvComment;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.et_send_msg)
    EditText etContent;

    List<Comment.CommentsBean> list;
    CommentAdapter adapter;
    String title;
    String labelId;
    ACache aCache;
    User user;

    @AfterViews
    public void initViews() {
        list = new ArrayList<>();
        aCache = ACache.get(this, MyConstants.USER_INFO);
        user = (User) aCache.getAsObject(MyConstants.USER_MESSAGE);
        initData();
        initA();
        fetchData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
    }

    public void initA() {
        tvTitle.setText(title);
        adapter = new CommentAdapter(this, list);
        lvComment.setAdapter(adapter);
    }

    public void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        labelId = intent.getStringExtra("LabelID");
    }

    @Background
    public void fetchData() {
        request.getComment(MyConstants.BASE_URL, labelId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json.length()!=0) {
                    Comment temp = new Gson().fromJson(json, Comment.class);

                    list.addAll(temp.getComments());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    @Click(R.id.iv_send_comment)
    @Background
    void send() {
        String content = etContent.getText().toString();
        if (content.length() != 0) {
//            lvComment.setSelection(temp.get(0).getComments().size());
            Comment.CommentsBean bean = new Comment.CommentsBean();
            bean.setUserId(user.get_id());
            bean.setHead(user.getThumb());
            bean.setName(user.getName());
            bean.setSex(user.getSex());
            bean.setContent(content);
            bean.setPubTime(System.currentTimeMillis()+"");
            list.add(bean);
            request.sendComment(MyConstants.BASE_URL, bean, labelId, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String json = response.body().string();
                    if (json != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                etContent.setText(null);
                                adapter.notifyDataSetChanged();
                                lvComment.setSelection(list.size());
                                response.close();
                            }
                        });
                    }
                }
            });
        }
    }


    @Click(R.id.iv_back)
    void back(){
        this.finish();
    }
}

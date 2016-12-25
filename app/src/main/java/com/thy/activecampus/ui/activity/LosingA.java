package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.LosingAdapter;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.EndLessOnScrollListener;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.model.Losing;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.view.DividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_losing)
public class LosingA extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final LabelReqImpl request = LabelReqImpl.getInstance();

    @ViewById(R.id.lv_losing)
    RecyclerView lvLosing;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewById(R.id.fab)
    FloatingActionButton fab;

    LosingAdapter adapter;

    List<Losing> list;
    List<Losing> tempList ;



    @AfterViews
    public void initViews(){
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF99CC")));
        list = new ArrayList<>();
        lvLosing.setLayoutManager(new LinearLayoutManager(this));
        lvLosing.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mSwipeLayout.setOnRefreshListener(this);
        adapter = new LosingAdapter(this,list);
        lvLosing.setAdapter(adapter);
        lvLosing.addOnScrollListener(new EndLessOnScrollListener(lvLosing) {
            @Override
            public void onLoadMore(int currentPage) {
                onReload(currentPage);
            }
        });
        adapter.setLCallBack(new LosingAdapter.LostCallBack() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(LosingA.this,LostDetailA_.class);
                intent.putExtra("lost",list.get(position));
                startActivity(intent);
            }
        });
        onReload(0);
    }

    @Background
    public void onReload(int curPage) {
        String url = MyConstants.BASE_URL+"/v1/getlost?pageSize="+curPage;
        if (curPage == 0) {
            list.clear();
        }

        request.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String back = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempList = new Gson().fromJson(back, new TypeToken<ArrayList<Losing>>() {
                        }.getType());
                        if (tempList.size()==0){
                            adapter.changeLoadStatus(1);
                        }else{
                            list.addAll(tempList);
                            adapter.notifyDataSetChanged();
                        }
                        mSwipeLayout.setRefreshing(false);
                    }
                });

            }
        });

    }

    @Click(R.id.rl_back)
    void back(){
        this.finish();
    }

    @Click(R.id.rl_search)
    void search(){
        startActivity(new Intent(LosingA.this,LostSearchA_.class));
    }

    @Override
    public void onRefresh() {
        onReload(0);
    }


    @Click(R.id.fab)
    void fab(){
        startActivity(new Intent(this,SendLostA_.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        onReload(0);
    }
}

package com.thy.activecampus.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.RecentAdapter;
import com.thy.activecampus.base.BaseF;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.EndLessOnScrollListener;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.activity.LabelDetailA_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
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
@EFragment(R.layout.fragment_recent)
public class RecentFra extends BaseF implements SwipeRefreshLayout.OnRefreshListener {

    private LabelReqImpl request = LabelReqImpl.getInstance();

    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public RecentAdapter adapter;
    private List<LabelM> list;
    private List<LabelM> newList = new ArrayList<>(); //存储每次从网上获取的数据
    float newY = 0;
    float oldY = 0;

    public ACache cache;

    @AfterViews
    public void initView() {
        list = new ArrayList<>();
        cache = ACache.get(getActivity());
        init();
        mSwipeLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED); //设置颜色
        mSwipeLayout.setOnRefreshListener(this); //设置下拉刷新的监听
        mSwipeLayout.setDistanceToTriggerSync(300);  // 设置手指在屏幕下拉多少距离会触发下拉刷新

        adapter = new RecentAdapter(getActivity(), list);

        adapter.setLabelCallBack(new RecentAdapter.LabelCallBack() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), LabelDetailA_.class);
                intent.putExtra("label", list.get(position));
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener(mRecyclerView) {
            @Override
            public void onLoadMore(int currentPage) {
                onReload(currentPage);
            }
        });


        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    newY = event.getY();
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    oldY = event.getY();
                }

                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }


    private void init() {
        loadNativeData();
    }

    private void loadNativeData() {
        String jsonArray = cache.getAsString("data");
        if (jsonArray != null) {
            list = new Gson().fromJson(jsonArray, new TypeToken<ArrayList<LabelM>>() {
            }.getType());
        }

    }

    @Background
    public void onReload(int curPage) {

        if (curPage == 0) {
            list.clear();
        }

        request.getLabels(MyConstants.BASE_URL, curPage, 0, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String back = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newList = new Gson().fromJson(back, new TypeToken<ArrayList<LabelM>>() {
                        }.getType());
                        if (newList.size() == 0) {
                            adapter.changeLoadStatus(1);
                        } else {
                            list.addAll(newList);
                            adapter.notifyDataSetChanged();

                        }
                        mSwipeLayout.setRefreshing(false);

                    }
                });
                cache.put("data", back);

            }
        });

    }


    @Override

    public void onRefresh() {
        onReload(0);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

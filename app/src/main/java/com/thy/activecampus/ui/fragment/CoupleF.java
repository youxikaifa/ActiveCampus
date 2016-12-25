package com.thy.activecampus.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.AutoCommonAdapter;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.EndLessOnScrollListener;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.activity.AutoDyneDetailA_;
import com.thy.activecampus.view.DividerItemDecoration;

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
@EFragment(R.layout.fragment_common)
public class CoupleF extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public final static LabelReqImpl request = LabelReqImpl.getInstance();
    public final static int COUPLE = 2;
    @ViewById(R.id.lvCommon)
    RecyclerView lvCouple;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;


    AutoCommonAdapter adapter;
    List<AutoDyne> list;
    List<AutoDyne> tempList;
    ACache cache;
    String userId;

//    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

    EndLessOnScrollListener listener;
    @AfterViews
    public void initViews() {
        list = new ArrayList<>();

        loadCache();

        mSwipeLayout.setOnRefreshListener(this);
        lvCouple.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvCouple.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));


        listener = new EndLessOnScrollListener(lvCouple) {
            @Override
            public void onLoadMore(int currentPage) {
                onReload(currentPage);
            }
        };


        lvCouple.addOnScrollListener(listener);
        adapter = new AutoCommonAdapter(getActivity(), list, userId);
        adapter.setcCallBack(new AutoCommonAdapter.ChildCallBack() {

            @Override
            public void onShareClick(int position) {
                Toast.makeText(getActivity(), "分享", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLikeClick(final int position) {
                String auto_id = list.get(position).get_id();
                request.clickAutoLike(MyConstants.BASE_URL, auto_id, userId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String back = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AutoDyne autoDyne = new Gson().fromJson(back, AutoDyne.class);
                                list.remove(position);
                                list.add(position, autoDyne);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), AutoDyneDetailA_.class);
                intent.putExtra("autodyne", list.get(position));
                startActivity(intent);

                String auto_id = list.get(position).get_id();
                request.joinScans(MyConstants.BASE_URL, auto_id, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            }
        });
        lvCouple.setAdapter(adapter);
        mSwipeLayout.setRefreshing(true);
        onReload(0);
    }

    private void loadCache() {
        cache = ACache.get(getActivity(), MyConstants.USER_INFO);
        User user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
        userId = user.get_id();
    }

    @Background
    public void onReload(int curPage) {

        if (curPage == 0) {
            list.clear();
        }

        request.getAutoDyne(MyConstants.BASE_URL, curPage, COUPLE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String back = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempList = new Gson().fromJson(back, new TypeToken<ArrayList<AutoDyne>>() {
                        }.getType());
                        if (tempList.size() == 0) {
                            adapter.changeLoadStatus(1);
                        } else {
                            list.addAll(tempList);
                            adapter.notifyDataSetChanged();

                        }
                        mSwipeLayout.setRefreshing(false);

                    }
                });

            }
        });

    }

    @Override
    public void onRefresh() {
        System.out.println("------------->?刷新了couple");
        listener.resetPage();
        onReload(0);
    }



}


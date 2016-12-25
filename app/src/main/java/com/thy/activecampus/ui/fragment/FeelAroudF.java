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
import com.thy.activecampus.adapter.FeelCommonAdapter;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.EndLessOnScrollListener;
import com.thy.activecampus.model.FeelModel;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.activity.FeelDetailA_;
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
 * 同校
 * Created by Jin on 7/29.
 */

@EFragment(R.layout.fragment_common)
public class FeelAroudF extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public final static LabelReqImpl request = LabelReqImpl.getInstance();
    public final static String SCHOOL = "北京大学";
    @ViewById(R.id.lvCommon)
    RecyclerView lvHot;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;


    FeelCommonAdapter adapter;
    List<FeelModel> list;
    List<FeelModel> tempList;
    ACache cache;
    User user;

    EndLessOnScrollListener listener;


    @AfterViews
    public void initViews() {

        list = new ArrayList<>();
        loadCache();


        lvHot.setLayoutManager(new LinearLayoutManager(getActivity()));

        listener = new EndLessOnScrollListener(lvHot) {
            @Override
            public void onLoadMore(int currentPage) {
                onReload(currentPage);
            }
        };

        lvHot.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        lvHot.addOnScrollListener(listener);

        adapter = new FeelCommonAdapter(getActivity(), list, user);
        adapter.setFeelCallBack(new FeelCommonAdapter.FeelCallBack() {
            @Override
            public void onLikeClick(final int position) {
                Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();

                request.clickFeelLike(MyConstants.BASE_URL, list.get(position).get_id(), user.getThumb(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String back = response.body().string();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FeelModel model = new Gson().fromJson(back,FeelModel.class);
                                list.remove(position);
                                list.add(position,model);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }

            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(getActivity(), FeelDetailA_.class);
                intent.putExtra("feel",list.get(position));
                startActivity(intent);

            }
        });
        lvHot.setAdapter(adapter);
        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setOnRefreshListener(this);
        onReload(0);
    }

    private void loadCache() {
        cache = ACache.get(getActivity(), MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
    }

    @Background
    public void onReload(int curPage) {

        if (curPage == 0) {
            list.clear();
        }

        request.getFeels(MyConstants.BASE_URL, curPage, SCHOOL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String back = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempList = new Gson().fromJson(back, new TypeToken<ArrayList<FeelModel>>() {
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
        listener.resetPage();
        onReload(0);
    }


    @Override
    public void onResume() {
        super.onResume();
        onReload(0);
    }
}

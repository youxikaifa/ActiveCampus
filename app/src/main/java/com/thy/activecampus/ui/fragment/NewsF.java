package com.thy.activecampus.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.NewsAdapter;
import com.thy.activecampus.model.News;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.activity.NewsWebA_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EFragment(R.layout.fragment_common)
public class NewsF extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    public final static LabelReqImpl request = LabelReqImpl.getInstance();
    @ViewById(R.id.lvDuanZi)
    ListView lvNews;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    NewsAdapter adapter;
    List<News.ResultBean.DataBean> news;
    List<News.ResultBean.DataBean> tempNews;
    View footerView;

    PopupWindow window;

    @AfterViews
    public void initViews() {
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer_view, null);
        news = new ArrayList<>();
        mSwipeLayout.setOnRefreshListener(this);
        lvNews.setOnScrollListener(this);
        adapter = new NewsAdapter(getContext(), news);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(this);
        mSwipeLayout.setRefreshing(true);
        loadData();
    }


    public void loadData() {
        String url = "http://v.juhe.cn/toutiao/index?type=top&key=9f02633b02990d69cf7504a90549e8a7";
        request.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String back = response.body().string();

                JsonReader reader = new JsonReader(new StringReader(back));
                reader.setLenient(true);

                final News entity = new Gson().fromJson(reader, News.class);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempNews = entity.getResult().getData();
                        news.addAll(tempNews);
                        adapter.notifyDataSetChanged();
                        lvNews.removeFooterView(footerView);
                        mSwipeLayout.setRefreshing(false);
                    }
                });
            }
        });

    }


    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE || scrollState==SCROLL_STATE_FLING){
            footerView.setVisibility(View.VISIBLE);
            loadData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


        Intent intent = new Intent(getActivity(), NewsWebA_.class);
        intent.putExtra("url",news.get(position).getUrl());
        startActivity(intent);
    }


}

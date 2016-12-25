package com.thy.activecampus.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.JokeAdapter;
import com.thy.activecampus.model.Joke;
import com.thy.activecampus.net.impl.LabelReqImpl;

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
@EFragment(R.layout.fragment_common_funny)
public class JokeF extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    public final static LabelReqImpl request = LabelReqImpl.getInstance();
    @ViewById(R.id.lvDuanZi)
    ListView lvJoke;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    JokeAdapter adapter;
    List<Joke.ResultBean.DataBean> jokes;
    List<Joke.ResultBean.DataBean> tempJokes;
    int curPage = 1;
    View footerView;


    @AfterViews
    public void initViews() {
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer_view1, null);
        jokes = new ArrayList<>();
        mSwipeLayout.setOnRefreshListener(this);
        lvJoke.setOnScrollListener(this);
        adapter = new JokeAdapter(getContext(), jokes);
        lvJoke.setAdapter(adapter);
        mSwipeLayout.setRefreshing(true);
        loadData();
    }


    public void loadData() {

        request.getJokes(curPage, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String back = response.body().string();

                JsonReader reader = new JsonReader(new StringReader(back));
                reader.setLenient(true);

                final Joke entity = new Gson().fromJson(reader, Joke.class);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempJokes = entity.getResult().getData();
                        jokes.addAll(tempJokes);
                        adapter.notifyDataSetChanged();
                        lvJoke.removeFooterView(footerView);
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
            curPage++;
            loadData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}

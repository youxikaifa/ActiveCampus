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
import com.thy.activecampus.adapter.FunPicAdapter;
import com.thy.activecampus.model.FunPic;
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
public class FunPicF extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    public final static LabelReqImpl request = LabelReqImpl.getInstance();
    @ViewById(R.id.lvDuanZi)
    ListView lvFunPic;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    FunPicAdapter adapter;
    List<FunPic.ResultBean.DataBean> funpics;
    List<FunPic.ResultBean.DataBean> tempFunpics;
    int curPage = 1;
    View footerView;

    @AfterViews
    public void initViews() {
        lvFunPic.setDividerHeight(50);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer_view1, null);
        funpics = new ArrayList<>();
        mSwipeLayout.setOnRefreshListener(this);
        lvFunPic.setOnScrollListener(this);
        adapter = new FunPicAdapter(getContext(), funpics);
        lvFunPic.setAdapter(adapter);
        mSwipeLayout.setRefreshing(true);
        loadData();
    }


    public void loadData() {

        request.getFunPics(curPage, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String back = response.body().string();

                JsonReader reader = new JsonReader(new StringReader(back));
                reader.setLenient(true);

                final FunPic entity = new Gson().fromJson(reader, FunPic.class);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempFunpics = entity.getResult().getData();
                        funpics.addAll(tempFunpics);
                        adapter.notifyDataSetChanged();
                        lvFunPic.removeFooterView(footerView);
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

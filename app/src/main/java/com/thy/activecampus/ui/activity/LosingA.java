package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.LosingAdapter;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.Losing;
import com.thy.activecampus.net.impl.LabelReqImpl;

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
public class LosingA extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public static final LabelReqImpl request = LabelReqImpl.getInstance();

    @ViewById(R.id.lv_losing)
    ListView lvLosing;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;


    View footerView ;
    LosingAdapter adapter;

    List<Losing> list;
    List<Losing> tempList ;
    int curPage = 0;


    @AfterViews
    public void initViews(){
        list = new ArrayList<>();
        footerView = LayoutInflater.from(this).inflate(R.layout.item_footer_view,null);
        lvLosing.addFooterView(footerView);
        mSwipeLayout.setOnRefreshListener(this);
        adapter = new LosingAdapter(this,list);
        lvLosing.setAdapter(adapter);
        lvLosing.setOnScrollListener(this);
        lvLosing.setOnItemClickListener(this);
        fecthData();
    }

    @Background
    public void fecthData(){
        String url = MyConstants.BASE_URL+"/v1/getlost?pageSize="+curPage;
        request.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String back = response.body().string();
                tempList = new Gson().fromJson(back,new TypeToken<ArrayList<Losing>>() {}.getType());
                if(tempList.size()>0){
                    curPage++;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.addAll(tempList);
                        adapter.notifyDataSetChanged();
                        mSwipeLayout.setRefreshing(false);
                        footerView.setVisibility(View.GONE);
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
        fecthData();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE || scrollState==SCROLL_STATE_FLING){
            footerView.setVisibility(View.VISIBLE);
            fecthData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem+visibleItemCount==totalItemCount){

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,LostDetailA_.class);
        intent.putExtra("lost",list.get(position));
        startActivity(intent);
    }

    @Click(R.id.fab)
    void fab(){
        startActivity(new Intent(this,SendLostA_.class));
    }
}

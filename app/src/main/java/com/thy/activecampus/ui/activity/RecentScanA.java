package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.RecentScanDetailAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.RecentScanDM;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_recent_scan)
public class RecentScanA extends BaseA implements AdapterView.OnItemClickListener {

    @ViewById(R.id.lv_history_detail)
    ListView lvHistoryDetail;

    List<LabelM> list;
    List<LabelM> templist;
    RecentScanDetailAdapter adapter;
    ACache cache;


    @AfterViews
    public void initViews(){
        init();
        adapter = new RecentScanDetailAdapter(this,list);
        lvHistoryDetail.setAdapter(adapter);
        lvHistoryDetail.setOnItemClickListener(this);
    }


    private void init(){
        list = new ArrayList<>();
        templist = new ArrayList<>();
        cache = ACache.get(this,MyConstants.USER_INFO);
        JSONArray json = cache.getAsJSONArray(MyConstants.LABEL_RECENT_SCAN);
        if (json!=null){
            templist = new Gson().fromJson(json.toString(),new TypeToken<ArrayList<LabelM>>() {}.getType());
            list.addAll(templist);
        }

    }

    @Click(R.id.back)
    void back(){
        this.finish();
    }

    @Click(R.id.clear)
    void clear(){
        cache.remove(MyConstants.LABEL_RECENT_SCAN);
        list.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        EventBus.getDefault().postSticky(list.get(i));
        startActivity(new Intent(this,LabelDetailA_.class));
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

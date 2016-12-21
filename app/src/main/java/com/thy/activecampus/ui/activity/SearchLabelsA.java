package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.HeadImagePagerAdapter;
import com.thy.activecampus.adapter.RecentScanAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.MyOnPageChangeListener;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.RecentScanM;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_search_label)
public class SearchLabelsA extends BaseA implements View.OnClickListener {

    public LabelReqImpl request= LabelReqImpl.getInstance();
    ListView lvActivity;
    ListView lvPerson;

    @ViewById(R.id.et_search)
    EditText etSearch;
    @ViewById(R.id.vPager)
    ViewPager mPager;//页卡内容
    @ViewById(R.id.text1)
    TextView t1;
    @ViewById(R.id.text2)
    TextView t2;
    @ViewById(R.id.cursor)
    ImageView ind;  //指示器
    private List<View> list; // Tab页面列表
    private List<LabelM> rList; //记录标题列表
    public List<LabelM> templist = new ArrayList<>();
    private LayoutInflater inflater;

    HeadImagePagerAdapter adapter;
    RecentScanAdapter rAdapter;

    View view1;
    View view2;
    RelativeLayout rlRecentA;
    RelativeLayout rlRecentP;

    public ACache cache;



    @AfterViews
    public void initViews(){
        cache = ACache.get(this,MyConstants.USER_INFO);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
                }
                return false;

            }
        });

        setPagerData();
    }

    public void search(){
        Toast.makeText(this, "正在搜索", Toast.LENGTH_SHORT).show();
        String key= etSearch.getText().toString();
        request.search(MyConstants.BASE_URL, key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String back = response.body().string();
                templist = new Gson().fromJson(back,new TypeToken<ArrayList<LabelM>>() {}.getType() );
                rList.clear();
                rList.addAll(templist);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void setPagerData(){
        inflater = LayoutInflater.from(this);
        rList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            RecentScanM model = new RecentScanM();
//            model.title = "流浪4级一套连招能打600多血,再来一套你就挂了,小法是一个非常无脑的英雄,哈哈";
//            rList.add(model);
//        }

        view1 = inflater.inflate(R.layout.search_tab_one,null);
        view2 = inflater.inflate(R.layout.search_tab_two,null);
        lvActivity = (ListView) view1.findViewById(R.id.lv_history);
        lvPerson = (ListView) view2.findViewById(R.id.lv_person);

        rlRecentA = (RelativeLayout) view1.findViewById(R.id.recentA);
        rlRecentP = (RelativeLayout) view2.findViewById(R.id.recentP);


        rAdapter = new RecentScanAdapter(this,rList);
        lvActivity.setAdapter(rAdapter);
        lvPerson.setAdapter(rAdapter);

        lvActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveRecentScan(position);
                EventBus.getDefault().postSticky(rList.get(position));
                startActivity(new Intent(SearchLabelsA.this, LabelDetailA_.class));
            }
        });

        rlRecentA.setOnClickListener(this);
        rlRecentP.setOnClickListener(this);
        list = new ArrayList<>();
        list.add(view1);
        list.add(view2);
        adapter = new HeadImagePagerAdapter(list);
        mPager.setAdapter(adapter);
        mPager.addOnPageChangeListener(new MyOnPageChangeListener(ind,t1,t2));

    }


    @Background
    public void saveRecentScan(int position) {
        JSONArray array = cache.getAsJSONArray(MyConstants.LABEL_NATIVE_CACHE);
        if (array != null) {
            templist = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<LabelM>>() {}.getType());
            if (!templist.contains(list.get(position))) {
                if (templist.size() > 50) {
                    templist.remove(0);
                }
                templist.add(rList.get(position));
                String json = new Gson().toJson(templist);
                cache.put(MyConstants.LABEL_RECENT_SCAN, json);
            }
        }

    }



    @Click(R.id.back)
    void back(){
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.recentA:
                startActivity(new Intent(this,RecentScanA_.class));
                break;
            case R.id.recentP:
                startActivity(new Intent(this,RecentScanA_.class));
                break;
            default:break;
        }
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

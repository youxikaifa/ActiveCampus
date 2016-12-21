package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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

@EActivity(R.layout.activity_lost_search)
public class LostSearchA extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final LabelReqImpl request = LabelReqImpl.getInstance();

    @ViewById(R.id.lv_lost)
    ListView lvLost;
    @ViewById(R.id.et_search)
    EditText etSearch;
    @ViewById(R.id.tv_notice)
    TextView tvNotice;
    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;

    LosingAdapter adapter;

    List<Losing> list = new ArrayList<>();
    List<Losing> tempList ;

    @AfterViews
    public void initViews(){
        adapter = new LosingAdapter(this,list);
        lvLost.setAdapter(adapter);
        lvLost.setOnItemClickListener(this);

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    if (etSearch.getText()!=null){
                        progressBar.setVisibility(View.VISIBLE);
                        search();
                    }
                }
                return false;

            }
        });

    }

    @Background
    public void search(){

        String key = etSearch.getText().toString();
        request.searchLost(MyConstants.BASE_URL, key, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String back = response.body().string();
                tempList = new Gson().fromJson(back,new TypeToken<ArrayList<Losing>>() {}.getType());
                list.clear();
                list.addAll(tempList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (list.size()==0){
                            tvNotice.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    @Click(R.id.back)
    void back(){
        this.finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(LostSearchA.this,LostDetailA_.class);
        intent.putExtra("lost",list.get(position));
        startActivity(intent);
    }
}

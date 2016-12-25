package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.TypeAdapter;
import com.thy.activecampus.model.SelectType;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

@EActivity(R.layout.activity_select_type)
public class SelectTypeA extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @ViewById(R.id.lvType)
    ListView lvType;

    List<SelectType> list;

    TypeAdapter adapter;


    @AfterViews
    public void initViews(){

        initData();

        adapter = new TypeAdapter(this,list);
        lvType.setAdapter(adapter);
        lvType.setOnItemClickListener(this);
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new SelectType(R.drawable.ic_game,"游戏"));
        list.add(new SelectType(R.drawable.ic_run,"运动"));
        list.add(new SelectType(R.drawable.ic_togetor,"聚会"));
        list.add(new SelectType(R.drawable.ic_tv,"电视剧"));
        list.add(new SelectType(R.drawable.ic_movie,"电影"));
        list.add(new SelectType(R.drawable.ic_ktv,"KTV"));
        list.add(new SelectType(R.drawable.ic_paly,"吃喝玩乐"));
        list.add(new SelectType(R.drawable.ic_compitition,"比赛"));
        list.add(new SelectType(R.drawable.ic_video,"摄影旅行"));
        list.add(new SelectType(R.drawable.ic_expression,"表情包"));
        list.add(new SelectType(R.drawable.ic_basketball,"篮球"));
        list.add(new SelectType(R.drawable.ic_football,"足球"));
        list.add(new SelectType(R.drawable.ic_ask,"问答"));
        list.add(new SelectType(R.drawable.ic_write,"书法习字"));
        list.add(new SelectType(R.drawable.ic_other,"其他"));
    }

    @Click(R.id.iv_back)
    void back(){
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,AddLabelA_.class);
        intent.putExtra("title",list.get(position).getTitle());
        startActivity(intent);
        this.finish();
    }
}

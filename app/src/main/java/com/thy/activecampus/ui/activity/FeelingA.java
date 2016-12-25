package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.FragmentAdapter;
import com.thy.activecampus.ui.fragment.BoyF_;
import com.thy.activecampus.ui.fragment.CoupleF_;
import com.thy.activecampus.ui.fragment.FeelAroudF;
import com.thy.activecampus.ui.fragment.FeelAroudF_;
import com.thy.activecampus.ui.fragment.FeelHotF;
import com.thy.activecampus.ui.fragment.FeelHotF_;
import com.thy.activecampus.ui.fragment.GirlF_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_feeling)
public class FeelingA extends AppCompatActivity {

    @ViewById(R.id.tabs)
    TabLayout tabs;
    @ViewById(R.id.viewpager)
    ViewPager viewPager;
    @ViewById(R.id.fab)
    FloatingActionButton fab;


    @AfterViews
    public void initViews() {
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF99CC")));
        initViewPager();
    }



    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("热门");
        titles.add("同校");
        for (int i = 0; i < titles.size(); i++) {
            tabs.addTab(tabs.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new FeelHotF_());
        fragments.add(new FeelAroudF_());

        FragmentAdapter mFragmentAdapteradapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        viewPager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来。
        tabs.setupWithViewPager(viewPager);
        //给TabLayout设置适配器
        tabs.setTabsFromPagerAdapter(mFragmentAdapteradapter);
    }

    @Click(R.id.fab)
    void fab(){
        Intent intent = new Intent(this,PubAutoDyneA_.class);
        intent.putExtra("type","feel");
        startActivity(intent);
    }
}

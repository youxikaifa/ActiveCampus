package com.thy.activecampus.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.FragmentAdapter;
import com.thy.activecampus.ui.fragment.FunPicF_;
import com.thy.activecampus.ui.fragment.JokeF_;
import com.thy.activecampus.ui.fragment.NewsF_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

@EActivity(R.layout.activity_joke)
public class JokeA extends AppCompatActivity {

    @ViewById(R.id.tabs)
    TabLayout tabs;
    @ViewById(R.id.viewpager)
    ViewPager viewPager;



    @AfterViews
    public void initViews() {
        initViewPager();
    }



    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("笑话");
        titles.add("趣图");
        titles.add("头条");
        for (int i = 0; i < titles.size(); i++) {
            tabs.addTab(tabs.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new JokeF_());
        fragments.add(new FunPicF_());
        fragments.add(new NewsF_());

        FragmentAdapter mFragmentAdapteradapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        viewPager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来。
        tabs.setupWithViewPager(viewPager);
        //给TabLayout设置适配器
        tabs.setTabsFromPagerAdapter(mFragmentAdapteradapter);
    }




    @Click(R.id.rl_back)
    void back() {
        this.finish();
    }

}

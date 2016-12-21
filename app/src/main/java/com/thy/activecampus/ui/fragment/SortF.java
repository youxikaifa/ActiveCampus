package com.thy.activecampus.ui.fragment;

import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.TestAdapter;
import com.thy.activecampus.base.BaseF;
import com.thy.activecampus.ui.activity.AutoDyneA_;
import com.thy.activecampus.ui.activity.JokeA;
import com.thy.activecampus.ui.activity.JokeA_;
import com.thy.activecampus.ui.activity.LosingA;
import com.thy.activecampus.ui.activity.LosingA_;
import com.thy.activecampus.widget.SlideView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */
@EFragment(R.layout.fragment_main_sort)
public class SortF extends BaseF {

    @ViewById(R.id.slideview)
    SlideView slideView;

    List<String> list ;
    @AfterViews
    public void initView() {


        init();

        slideView.setData(list);

    }

    private void init() {
        list = new ArrayList<>();
        list.add("http://img.redocn.com/sheji/20150504/qingchunsibianhuixiaoyuanhuodongzhanban_4248976.jpg");
        list.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1479890341&di=dafc7e73776401a6719c2fdd06621bf3&src=http://www.ahjhzx.cn/upload/bimg/2011100637800509.jpg");
        list.add("http://pic.58pic.com/58pic/11/01/50/24N58PICTDi.jpg");
        list.add("http://img.redocn.com/sheji/20150714/xiaoyuanyundonghuihuodonghaibaosheji_4652548.jpg");
        list.add("http://www.jitu5.com/uploads/allimg/101015/449-101015121H00.jpg");
        list.add("http://img.redocn.com/sheji/20150504/qingchunsibianhuixiaoyuanhuodongzhanban_4248976.jpg");
        list.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1479890341&di=dafc7e73776401a6719c2fdd06621bf3&src=http://www.ahjhzx.cn/upload/bimg/2011100637800509.jpg");

    }


    @Click(R.id.shiwu)
    void shiwu() {
        startActivity(new Intent(getActivity(), LosingA_.class));
    }

    @Click(R.id.zhaopin)
    void zhaopin() {

    }

    @Click(R.id.kaoyan)
    void kaoyan() {

    }

    @Click(R.id.duanzi)
    void duanzi() {
        startActivity(new Intent(getActivity(), JokeA_.class));
    }

    @Click(R.id.zatan)
    void zatan() {

    }

    @Click(R.id.dianping)
    void dianping() {

    }

    @Click(R.id.zipai)
    void zipai() {
        startActivity(new Intent(getActivity(), AutoDyneA_.class));
    }


}

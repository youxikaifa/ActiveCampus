package com.thy.activecampus.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thy.activecampus.R;
import com.thy.activecampus.adapter.SelectImgFAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.model.FeelModel;
import com.thy.activecampus.model.Image;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.fragment.SelectImgF;
import com.thy.activecampus.ui.fragment.SelectImgF_;
import com.thy.activecampus.ui.fragment.SelectTypeF;
import com.thy.activecampus.ui.fragment.SelectTypeF_;

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
@EActivity(R.layout.activity_pub_autodyne)
public class PubAutoDyneA extends BaseA {

    public final static LabelReqImpl request = LabelReqImpl.getInstance();
    private static final String FEELING = "feel";
    private static final String AUTODYNE = "autodyne";

    @ViewById(R.id.et_title)
    EditText etTitle;
    @ViewById(R.id.tvNotice)
    TextView tvNotice;
    @ViewById(R.id.rlTitle)
    RelativeLayout rlTitle;
    @ViewById(R.id.tvSelectNum)
    TextView select_Num;
    @ViewById(R.id.rl_notice)
    RelativeLayout rlNotice;
    @ViewById(R.id.viewpager)
    ViewPager viewPager;
    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;
    @ViewById(R.id.tv_title)
    TextView title;

    public  ArrayList<Image> images = new ArrayList<>();

    private  ArrayList<String> imgPaths = new ArrayList<>();
    int type = 0;
    public ACache cache;
    public User user;

    SelectImgF selectImgF;
    SelectTypeF selectTypeF;
    List<Fragment> fragments;
    SelectImgFAdapter adapter;

    String _type; //要发送的是那种动态 心情还是自拍

    @AfterViews
    public void initViews(){
        getArgs();
        loadCache();
        fragments = new ArrayList<>();
        selectImgF = new SelectImgF_();
        selectTypeF = new SelectTypeF_();
        fragments.add(selectImgF);
        fragments.add(selectTypeF);
        adapter = new SelectImgFAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }

    private void getArgs() {
        Intent intent = getIntent();
        _type = intent.getStringExtra("type");
        if (_type.equals(FEELING)){
            title.setText("发心情");
        }else{
            title.setText("发自拍");
        }
    }

    @Background
    public void loadCache() {
        cache = ACache.get(this, MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
    }


    @Click(R.id.tv_pub)
    void pub(){
        if (isCommit()){
            progressBar.setVisibility(View.VISIBLE);
            if(_type.equals(AUTODYNE)){
                AutoDyne autodyne = new AutoDyne();
                autodyne.setUserId(user.get_id());
                autodyne.setName(user.getName());
                autodyne.setPics(imgPaths);
                autodyne.setHead(user.getThumb());
                autodyne.setTitle(etTitle.getText().toString());
                autodyne.setType(type);
                autodyne.setMotto(user.getMotto());
                autodyne.setSchool(user.getSchool());
                request.pubAutoDyne(MyConstants.BASE_URL, autodyne, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        imgPaths.clear();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        imgPaths.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                PubAutoDyneA.this.finish();

                            }
                        });
                    }
                });
            }else{
                FeelModel model = new FeelModel();
                model.setUserId(user.get_id());
                model.setName(user.getName());
                model.setPics(imgPaths);
                model.setHead(user.getThumb());
                model.setContent(etTitle.getText().toString());
                model.setType(type);
                model.setSex(user.getSex());
                model.setMotto(user.getMotto());
                model.setSchool(user.getSchool());

                request.pubFeeling(MyConstants.BASE_URL, model, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        imgPaths.clear();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        imgPaths.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                PubAutoDyneA.this.finish();

                            }
                        });
                    }
                });
            }

        }
    }

    public boolean isCommit() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            if (_type.equals(FEELING)){
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            }

            return false;
        } else if (images.size()==0) {
            Toast.makeText(this, "请选择相片", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Click(R.id.iv_select_pics)
    void select(){
//        hideSoftInput();
        viewPager.setCurrentItem(0);
        if(getWindow().getAttributes().softInputMode== WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)

        {

            hideSoftInput();

        }
    }

    @Click(R.id.rlTitle)
    void clickTitle(){
        etTitle.setFocusable(true);
        showSoftInut();
    }

    @Click(R.id.iv_person_sort)
    void sort(){
        viewPager.setCurrentItem(1);
    }

    private void showSoftInut() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //这里给它设置了弹出的时间，
        imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //这里给它设置了弹出的时间，
        imm.toggleSoftInput(1000, InputMethodManager.RESULT_HIDDEN);
    }



    public void onReloadImgs(List<Image> imgs,List<String> paths){
        images.clear();
        images.addAll(imgs);
        imgPaths.clear();
        imgPaths.addAll(paths);

        if (imgs.size()==0){
            select_Num.setVisibility(View.GONE);
        }else{
            select_Num.setVisibility(View.VISIBLE);
            select_Num.setText(imgs.size()+"");
        }
    }

    public void onReloadType(int _type){
        type = _type;
    }


}

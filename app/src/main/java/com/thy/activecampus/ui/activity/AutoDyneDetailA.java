package com.thy.activecampus.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.ACommentAdapter;
import com.thy.activecampus.adapter.ImagePagerAdapter;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.AutoComment;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.view.ListViewForScrollView;
import com.thy.activecampus.view.MyListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_autodyne_detail)
public class AutoDyneDetailA extends AppCompatActivity implements AdapterView.OnItemClickListener {


    public static final LabelReqImpl request = LabelReqImpl.getInstance();
    @ViewById(R.id.layout_content)
    RelativeLayout layoutContent;
    @ViewById(R.id.lv_comment)
    MyListView lvComment;
    @ViewById(R.id.mScrollView)
    ScrollView mScrollView;
    @ViewById(R.id.view_head)
    SimpleDraweeView head;
    @ViewById(R.id.tv_name)
    TextView name;
    @ViewById(R.id.tv_time)
    TextView time;
    @ViewById(R.id.tv_title)
    TextView title;
    @ViewById(R.id.vpPics)
    ViewPager vpPics;
    @ViewById(R.id.fab)
    FloatingActionButton fab;

    List<AutoComment> comments;
    List<AutoComment> allComments;
    List<AutoComment> allReps;
    Map<Integer,List<AutoComment>> map = new HashMap<>();
    AutoDyne autoDyne;
    ACommentAdapter adapter;
    ImagePagerAdapter pagerAdapter;

    PopupWindow commentWindow;
    PopupWindow repWindow;

    ACache cache;
    User user;




    @AfterViews
    public void initViews(){
        loadCache();
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF99CC")));
        comments = new ArrayList<>();
        allReps = new ArrayList<>();
        mScrollView.smoothScrollTo(0,0);
        getArgments();

        loadNativeData();

        adapter = new ACommentAdapter(this,comments,map);
        adapter.setRCallback(new ACommentAdapter.RepCallBack() {
            @Override
            public void rCallBack(AutoComment comment) {
                View repView = LayoutInflater.from(AutoDyneDetailA.this).inflate(R.layout.window_auto_rep,null);
                showPopWindow3(repView, comment);
            }
        });
        lvComment.setAdapter(adapter);

        pagerAdapter = new ImagePagerAdapter(this,autoDyne.getPics());
        vpPics.setAdapter(pagerAdapter);
        lvComment.setOnItemClickListener(this);

        loadData();

    }

    public void loadCache() {
        cache = ACache.get(this, MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
    }

    private void loadNativeData() {
        head.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+autoDyne.getHead());
        name.setText(autoDyne.getName());
        title.setText(autoDyne.getTitle());
        time.setText(logicTime(autoDyne.getTime()));
    }

    public void getArgments(){
        Intent intent = getIntent();
        autoDyne = (AutoDyne) intent.getSerializableExtra("autodyne");
    }

    @Background
    public void loadData(){
        request.getAutoComments(MyConstants.BASE_URL, autoDyne.get_id(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String back = response.body().string();
                refreshData(back);
            }
        });
    }

    public void refreshData(String back){
        comments.clear();
        allReps.clear();
        map.clear();
        allComments = new Gson().fromJson(back,new TypeToken<ArrayList<AutoComment>>(){}.getType());
        for (int i = 0; i < allComments.size(); i++) {
            if (allComments.get(i).getType()==0){
                comments.add(allComments.get(i));
            }else{
                allReps.add(allComments.get(i));
            }
        }


        for (int i = 0; i < comments.size(); i++) {
            if (map.get(i)==null){
                map.put(i,new ArrayList<AutoComment>());
            }
            for (int j = 0; j < allReps.size(); j++) {
                if (comments.get(i).get_id().equals(allReps.get(j).getRepid())){
                    map.get(i).add(allReps.get(j));
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }



    @Click(R.id.back)
    void back(){
        this.finish();
    }


    @Click(R.id.fab)
    void fab(){
        View view = LayoutInflater.from(this).inflate(R.layout.window_commit_comment,null);

        final EditText etContent = (EditText) view.findViewById(R.id.et_content);
        final TextView tvSend = (TextView) view.findViewById(R.id.tv_send);
        RelativeLayout space = (RelativeLayout) view.findViewById(R.id.space);
        ImageView img = (ImageView) view.findViewById(R.id.select_photo);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AutoDyneDetailA.this, "tupian", Toast.LENGTH_SHORT).show();
                hideSoft();
            }
        });
        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentWindow.dismiss();
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AutoComment comment = new AutoComment();
                AutoComment.FromUserBean bean = new AutoComment.FromUserBean();
                comment.setAutoid(autoDyne.get_id());
                comment.setType(0);
                comment.setContent(etContent.getText().toString());
                bean.setThumb(user.getThumb());
                bean.set_id(user.get_id());
                bean.setName(user.getName());
                comment.setFrom_user(bean);
                request.commitAutoComment(MyConstants.BASE_URL, comment, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String back = response.body().string();
                        refreshData(back);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                commentWindow.dismiss();
                            }
                        });
                    }
                });

            }
        });
        commentWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,false);
        commentWindow.setFocusable(true);
        commentWindow.setBackgroundDrawable(new BitmapDrawable());
        commentWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        commentWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        commentWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

        etContent.requestFocus();
        etContent.setFocusable(true);

        showSoftInut();

    }

    private void showSoftInut() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //这里给它设置了弹出的时间，
        imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private void hideSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


    /**
     * 动态发表时间
     * @param time 发表的时间
     * @return
     */
    public String logicTime(String time){
        long past = Long.parseLong(time);
        long now = System.currentTimeMillis();
        long pastTime = (now-past)/1000;
        StringBuffer sb = new StringBuffer();
        if (pastTime==0){
            return sb.append(1 + "秒前").toString() ;
        }
        if (pastTime > 0 && pastTime < 60) { // 1分钟内
            return sb.append(pastTime + "秒前").toString();
        } else if (pastTime > 60 && pastTime < 3600) {
            return sb.append(pastTime / 60+"分钟前").toString();
        } else if (pastTime >= 3600 && pastTime < 3600 * 24) {
            return sb.append(pastTime / 3600 +"小时前").toString();
        }else if (pastTime >= 3600 * 24 && pastTime < 3600 * 48) {
            return sb.append("昨天").toString();
        }else if (pastTime >= 3600 * 48 && pastTime < 3600 * 72) {
            return sb.append("前天").toString();
        }else if (pastTime >= 3600 * 72) {
            return sb.append("3天前").toString();
        }else{
            return "";
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        View repView = LayoutInflater.from(this).inflate(R.layout.window_auto_rep,null);
        showPopWindow2(repView, position);
    }

    public void showPopWindow2(View repView, final int position) {

        String name = comments.get(position).getFrom_user().getName();

        final EditText etContent = (EditText) repView.findViewById(R.id.et_content);
        TextView tvSend = (TextView) repView.findViewById(R.id.tv_send);
        RelativeLayout space = (RelativeLayout) repView.findViewById(R.id.space);
        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repWindow.dismiss();
            }
        });
        etContent.setHint("回复: "+name);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoComment autoComment = new AutoComment();
                AutoComment.FromUserBean bean = new AutoComment.FromUserBean();
                autoComment.setAutoid(autoDyne.get_id());
                autoComment.setType(1);
                autoComment.setContent(etContent.getText().toString());
                autoComment.setRepid(comments.get(position).get_id());
                bean.setName(user.getName());
                bean.set_id(user.get_id());
                bean.setThumb(user.getThumb());
                autoComment.setFrom_user(bean);
                request.commitAutoComment(MyConstants.BASE_URL, autoComment, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String back = response.body().string();
                        refreshData(back);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                repWindow.dismiss();
                            }
                        });
                    }
                });
            }
        });
        repWindow = new PopupWindow(repView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,false);
        repWindow.setFocusable(true);
        repWindow.setBackgroundDrawable(new BitmapDrawable());
        repWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        repWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        repWindow.showAtLocation(repView, Gravity.BOTTOM,0,0);

        etContent.requestFocus();
        etContent.setFocusable(true);

        showSoftInut();
    }

    public void showPopWindow3(View repView, final AutoComment comment){
        final EditText etContent = (EditText) repView.findViewById(R.id.et_content);
        TextView tvSend = (TextView) repView.findViewById(R.id.tv_send);
        RelativeLayout space = (RelativeLayout) repView.findViewById(R.id.space);
        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repWindow.dismiss();
            }
        });
        etContent.setHint(user.getName()+" 回复: "+comment.getFrom_user().getName());

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoComment autoComment = new AutoComment();
                AutoComment.FromUserBean fromBean = new AutoComment.FromUserBean();
                AutoComment.TargetUserBean targetBean = new AutoComment.TargetUserBean();
                autoComment.setAutoid(autoDyne.get_id());
                autoComment.setType(2);
                autoComment.setContent(etContent.getText().toString());
                autoComment.setRepid(comment.getRepid());
                fromBean.setName(user.getName());
                fromBean.set_id(user.get_id());
                fromBean.setThumb(user.getThumb());
                targetBean.set_id(comment.getFrom_user().get_id());
                targetBean.setName(comment.getFrom_user().getName());
                targetBean.setThumb(comment.getFrom_user().getThumb());
                autoComment.setFrom_user(fromBean);
                autoComment.setTarget_user(targetBean);

                request.commitAutoComment(MyConstants.BASE_URL, autoComment, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String back = response.body().string();
                        refreshData(back);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                repWindow.dismiss();
                            }
                        });
                    }
                });

            }
        });
        repWindow = new PopupWindow(repView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,false);
        repWindow.setFocusable(true);
        repWindow.setBackgroundDrawable(new BitmapDrawable());
        repWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        repWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        repWindow.showAtLocation(repView, Gravity.BOTTOM,0,0);

        etContent.requestFocus();
        etContent.setFocusable(true);

        showSoftInut();
    }
}

package com.thy.activecampus.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.ACommentAdapter;
import com.thy.activecampus.adapter.ImagePagerAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.AutoComment;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.Room;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.view.MyListView;
import com.thy.activecampus.widget.PagerDialog;
import com.thy.activecampus.widget.SlideView;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
@EActivity(R.layout.activity_label_detail)
public class LabelDetailA extends BaseA implements AdapterView.OnItemClickListener {

    private LabelReqImpl request = LabelReqImpl.getInstance();

    @ViewById(R.id.fl_content)
    FrameLayout flContent;
    @ViewById(R.id.view_head)
    SimpleDraweeView head;
    @ViewById(R.id.tv_name)
    TextView tvName;
    @ViewById(R.id.tv_time)
    TextView tvTime;
    @ViewById(R.id.iv_sex)
    ImageView ivSex;
    @ViewById(R.id.tv_motto)
    TextView tvMotto;
    @ViewById(R.id.tv_content)
    TextView tvContent;
    @ViewById(R.id.tv_school)
    TextView school;
    @ViewById(R.id.tv_comments)
    TextView tvComments;
    @ViewById(R.id.fab)
    FloatingActionButton fab;
    @ViewById(R.id.view_pic)
    SimpleDraweeView viewPic;
    @ViewById(R.id.picNum)
    TextView picNum;
    @ViewById(R.id.lv_comment)
    MyListView lvComment;

    LabelM labelM;
    ACache cache;
    User user;

    List<AutoComment> comments = new ArrayList<>();
    List<AutoComment> allComments;
    List<AutoComment> allReps = new ArrayList<>();
    Map<Integer,List<AutoComment>> map = new HashMap<>();

    PopupWindow commentWindow;
    PopupWindow repWindow;

    ACommentAdapter adapter;


    @AfterViews
    public void initViews() {
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF99CC")));
        getAgrs();
        cache = ACache.get(this, MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);

        adapter = new ACommentAdapter(this,comments,map);
        adapter.setRCallback(new ACommentAdapter.RepCallBack() {
            @Override
            public void rCallBack(AutoComment comment) {
                View repView = LayoutInflater.from(LabelDetailA.this).inflate(R.layout.window_auto_rep,null);
                showPopWindow3(repView, comment);
            }
        });
        lvComment.setAdapter(adapter);
        lvComment.setOnItemClickListener(this);
        init();

    }

    private void getAgrs() {
        Intent intent = getIntent();
        labelM = (LabelM) intent.getSerializableExtra("label");
    }

    @Click(R.id.iv_chat)
    void chat(){
        Intent intent = new Intent(this, ChatRoomA_.class);
        intent.putExtra("label_id",labelM.get_id());
        intent.putExtra("label_thumb",labelM.getHead());
        intent.putExtra("label_title",labelM.getTitle());
        intent.putExtra("message_id","");
        startActivity(intent);
        request.joinRoom(MyConstants.BASE_URL_ANOTHER_PORT, user.get_id(),labelM.get_id(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String back = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        user = new Gson().fromJson(back,User.class);
                        cache.put(MyConstants.USER_MESSAGE,user);
                    }
                });
            }
        });
    }

    private void init() {

        head.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+labelM.getHead());
        tvName.setText(labelM.getName());
        tvTime.setText(logicTime(labelM.getPubTime()));
        tvContent.setText(labelM.getContent());


        if (labelM.getSchool()!=null){
            school.setText(labelM.getSchool());
        }

        if (labelM.getMotto()!=null){
            tvMotto.setText(labelM.getMotto());
        }


        if (labelM.getPicUrls().size()==0){
            flContent.setVisibility(View.GONE);
        }else{
            ControllerListener controllerListener = getListener(viewPic);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(Uri.parse(MyConstants.BASE_URL+labelM.getPicUrls().get(0)))
                    .build();
            viewPic.setController(controller);
            flContent.setVisibility(View.VISIBLE);

            tvComments.setText(labelM.getPicUrls().size()+" 回复");
            picNum.setText(labelM.getPicUrls().size()+"");
        }

        if (labelM.getSex()==0){
            ivSex.setImageResource(R.drawable.ic_male);
        }else{
            ivSex.setImageResource(R.drawable.ic_famale);
        }

    }

    @Click(R.id.iv_back)
    void back() {
        this.finish();
    }


    @Click(R.id.view_head)
    void user() {
        Intent intent = new Intent(new Intent(this, UserHomePageA_.class));
        intent.putExtra("user_id",labelM.getUserId());
        startActivity(intent);
    }

    @Click(R.id.fl_content)
    void pic(){
        PagerDialog dialog = new PagerDialog(this, labelM.getPicUrls(), 0);
        dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
        dialog.show();
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
                comment.setAutoid(labelM.get_id());
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

    public ControllerListener getListener(final SimpleDraweeView view){
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = 1080;
                layoutParams.height = (int) ((float) (1080 * height) / (float) width);
                view.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                Log.d("TAG", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        return  controllerListener;
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
                autoComment.setAutoid(labelM.get_id());
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

    private void showSoftInut() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //这里给它设置了弹出的时间，
        imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
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
                tvComments.setText(allComments.size()+" 回复");
                adapter.notifyDataSetChanged();
            }
        });
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
                autoComment.setAutoid(labelM.get_id());
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View repView = LayoutInflater.from(this).inflate(R.layout.window_auto_rep,null);
        showPopWindow2(repView, position);
    }




}

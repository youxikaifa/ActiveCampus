package com.thy.activecampus.ui.activity;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.thy.activecampus.R;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.base.SelectPicureBaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_revise_res)
public class ReviseResA extends SelectPicureBaseA {

    @ViewById(R.id.ivhead)
    SimpleDraweeView ivHead; //touxiang
    @ViewById(R.id.nick)
    TextView tvNick;
    @ViewById(R.id.reduce)
    TextView tvReduce;
    @ViewById(R.id.school)
    TextView tvSchool;
    @ViewById(R.id.skill)
    TextView tvSkill;
    @ViewById(R.id.habit)
    TextView tvHabit;
    @ViewById(R.id.phone)
    TextView tvPhone;
    @ViewById(R.id.qq)
    TextView tvQQ;
    @ViewById(R.id.weixin)
    TextView tvWeixin;
    @ViewById(R.id.motto)
    TextView tvMotto;

    LabelReqImpl request = LabelReqImpl.getInstance();
    EditText etInput;

    ACache cache;
    User user;

    String path = null;
    String name = null,school = null,reduce = null,
            skill = null,habit = null,phone = null,
            qq = null,weixin = null,weibo = null,motto = null;


    @AfterViews
    public void initViews() {
        init();
    }

    public void init(){
        cache = ACache.get(this,MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
        if (user.getThumb().substring(0,4).equals("http")){
            ivHead.setImageURI(user.getThumb());
        }else{
            ivHead.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+user.getThumb());
        }
        if (user.getMotto()!=null){
            tvMotto.setText(user.getMotto());
        }
        tvNick.setText(user.getName());
        tvSchool.setText(user.getSchool());
        tvReduce.setText(user.getReduce());
        tvSkill.setText(user.getSkill());
        tvHabit.setText(user.getHabit());
        tvPhone.setText(user.getContact().getPhone());
        tvQQ.setText(user.getContact().getQq());
        tvWeixin.setText(user.getContact().getWeixin());
    }

    public void fixHeadDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.selectpicture);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        selectPictrue();
                        break;
                    case 1:
                        takePhoto();
                        break;
                    case 2:
//                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.setCancelable(true);
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void fixMottoDialog(){
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("书写你的右铭")
                .setIcon(R.drawable.ic_motto)
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvMotto.setText(str);
                            motto = str;
                        }
                    }
                })
                .show();
    }

    public void fixNameDialog() {
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("请输入昵称")
                .setIcon(R.drawable.head)
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvNick.setText(str);
                            name = str;
                        }
                    }
                })
                .show();
    }

    public void fixSchoolDialog(){
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("请输入学校名称")
                .setIcon(R.drawable.ic_school)
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvSchool.setText(str);
                            school = str;
                        }
                    }
                })
                .show();
    }

    public void fixReduceDialog(){
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("请输入个人简介")
                .setIcon(R.drawable.reduce)
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvReduce.setText(str);
                            reduce = str;
                        }
                }
                })
                .show();
    }

    public void fixSkillDialog(){
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("你拥有什么技能呢？")
                .setIcon(R.drawable.skill)
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvSkill.setText(str);
                            skill = str;
                        }
                    }
                })
                .show();
    }

    public void fixHabitDialog(){
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("你有什么\"好习惯\"呢")
                .setIcon(R.drawable.habit)
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvHabit.setText(str);
                            habit = str;
                        }
                    }
                })
                .show();
    }

    public void fixPhoneDialog(){
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("你的电话是多少呀?")
                .setIcon(R.drawable.phone)
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvPhone.setText(str);
                            phone = str;
                        }
                    }
                })
                .show();
    }

    public void fixQqDialog(){
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("QQ告诉我吧!")
                .setIcon(R.drawable.qq)
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvQQ.setText(str);
                            qq = str;
                        }
                    }
                })
                .show();
    }

    public void fixWeixinDialog(){
        etInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("微信也告诉我吧!")
                .setIcon(R.drawable.weixin )
                .setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str =  etInput.getText().toString();
                        if (str.length()>0){
                            tvWeixin.setText(str);
                            weixin = str;
                        }
                    }
                })
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    path = data.getData().getPath();
                    break;
                case CAMERA_REQUEST_CODE:
                    if (isSdcardExisting()) {
                        resizeImage(getImageUri());
                    } else {
                        Toast.makeText(this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }
                    break;

                case RESIZE_REQUEST_CODE:
                    if (data != null) {

                        showResizeImage(data);
                        ivHead.setImageURI(uri);
                        if (uri!=null){
                            path = uri.toString();
                        }
                    } else {
                        Toast.makeText(this, "data为空", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Click(R.id.rlBack)
    void back() {
        this.finish();
    }

    @Click(R.id.tv_save)
    void save() {
        User u = new User();
        User.ContactBean bean = user.getContact();

        u.set_id(user.get_id());
        u.setName(name);
        u.setThumb(path);
        u.setReduce(reduce);
        u.setSchool(school);
        u.setSkill(skill);
        u.setHabit(habit);
        u.setMotto(motto);
        bean.setPhone(phone);
        bean.setQq(qq);
        bean.setWeixin(weixin);
        bean.setWeibo(weibo);
        u.setContact(bean);

        request.fixUserMsg(MyConstants.BASE_URL_ANOTHER_PORT, user.getThumb(), u, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                User u = new Gson().fromJson(json,User.class);
                EventBus.getDefault().postSticky(u);
                cache.put(MyConstants.USER_MESSAGE,u);
                response.close();
                ReviseResA.this.finish();
            }
        });

    }

    @Click(R.id.rl_head)
    void head() {
        fixHeadDialog();
    }

    @Click(R.id.rl_nick)
    void nick() {
        fixNameDialog();
    }

    @Click(R.id.rl_school)
    void schol() {
        fixSchoolDialog();
    }

    @Click(R.id.rl_reduce)
    void reduce() {
        fixReduceDialog();
    }

    @Click(R.id.rl_good)
    void good() {
        fixSkillDialog();
    }

    @Click(R.id.rl_habit)
    void habit() {
        fixHabitDialog();
    }

    @Click(R.id.rl_phone)
    void phone() {
        fixPhoneDialog();
    }

    @Click(R.id.rl_qq)
    void qq() {
        fixQqDialog();
    }

    @Click(R.id.rl_weixin)
    void weixin() {
        fixWeixinDialog();
    }

    @Click(R.id.rl_motto)
    void motto(){
        fixMottoDialog();
    }
}

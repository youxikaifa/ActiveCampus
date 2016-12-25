package com.thy.activecampus.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.thy.activecampus.R;
import com.thy.activecampus.base.SelectPicureBaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_checkres)
public class CheckResA extends SelectPicureBaseA {


    @ViewById(R.id.tv_male)
    TextView tvMale;
    @ViewById(R.id.tv_famale)
    TextView tvFamale;
    @ViewById(R.id.et_name)
    EditText etName;
    @ViewById(R.id.et_school)
    EditText etSchool;
    @ViewById(R.id.et_motto)
    EditText etMotto;
    @ViewById(R.id.iv_setHead)
    SimpleDraweeView image;
    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;

    private int whiteColor = Color.parseColor("#ffffff");
    private int grayColor = Color.parseColor("#aaaaaa");
    public int sex = 0;//0默认为男，

    ACache cache;
    String path = null;
    User user;
    LabelReqImpl request;

    @AfterViews
    public void initView() {
        cache = ACache.get(this, MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
    }


    @Click(R.id.iv_back)
    void back() {
        this.finish();
    }

    @Click(R.id.tv_male)
    void male() {
        if (sex == 1) {
            tvMale.setTextColor(whiteColor);
            tvMale.setBackgroundResource(R.drawable.xml_circle_solid_blue);
            tvFamale.setTextColor(grayColor);
            tvFamale.setBackground(null);
            sex = 0;
            user.setSex(sex);
        }
    }

    @Click(R.id.tv_famale)
    void famale() {
        if (sex == 0) {
            tvFamale.setTextColor(whiteColor);
            tvFamale.setBackgroundResource(R.drawable.xml_circle_solid_blue);
            tvMale.setTextColor(grayColor);
            tvMale.setBackground(null);
            sex = 1;
            user.setSex(sex);
        }
    }

    @Click(R.id.et_name)
    void etName() {
        etName.setFocusable(true);
        etName.setFocusableInTouchMode(true);
        etName.requestFocus();
    }

    @Click(R.id.et_school)
    void school() {
        etSchool.setFocusable(true);
        etSchool.setFocusableInTouchMode(true);
        etSchool.requestFocus();
    }

    @Click(R.id.et_motto)
    void motto() {
        etMotto.setFocusable(true);
        etMotto.setFocusableInTouchMode(true);
        etMotto.requestFocus();
    }


    @Click(R.id.startApp)
    void start() {
        String url = null;
        if (uri != null) {
            url = uri.toString().substring(7);
        }

        if (isCommit()){
            progressBar.setVisibility(View.VISIBLE);
            uploadHead(url);
        }
    }

    public boolean isCommit() {
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etSchool.getText().toString().trim())) {
            Toast.makeText(this, "请输入学校名称", Toast.LENGTH_SHORT).show();
            return false;
        } else if(TextUtils.isEmpty(etMotto.getText().toString().trim())){
            Toast.makeText(this, "请输入座右铭", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            return true;
        }
    }


    @Click(R.id.iv_setHead)
    void head() {
        showDialog();
    }

    public void showDialog() {
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
                        image.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "data为空", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(">>>>>>>>>>>", "onCreate");
    }

    public void selectPictrue() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
    }

    public void takePhoto() {
        if (isSdcardExisting()) {
            Intent cameraIntent = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
//            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "请插入sd卡", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void uploadHead(String imgPath) {
        User u = new User();
        String name = etName.getText().toString();
        String school = etSchool.getText().toString();
        String motto =etMotto.getText().toString();

        u.setName(name);
        u.setSchool(school);
        u.setMotto(motto);
        u.setSex(sex);
        u.set_id(user.get_id());
        request = LabelReqImpl.getInstance();

        request.uploadHead(MyConstants.BASE_URL_ANOTHER_PORT, u, imgPath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String json = response.body().string();
                User u = new Gson().fromJson(json, User.class);

                user.setThumb(u.getThumb());
                user.setName(u.getName());
                user.setSex(u.getSex());
                user.setSchool(u.getSchool());
                user.setMotto(u.getMotto());

                cache.put(MyConstants.USER_MESSAGE, user);

                response.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(CheckResA.this, MainActivity_.class));
                        Toast.makeText(CheckResA.this, "个人信息设置成功", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }
}

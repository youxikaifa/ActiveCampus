package com.thy.activecampus.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.base.SelectPicureBaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.User;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.thy.activecampus.base.SelectPicureBaseA.uri;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_user)
public class UserA extends SelectPicureBaseA {

    public static final String[] items = {"相册", "拍照", "取消"};

    @ViewById(R.id.user_head)
    SimpleDraweeView userHead;
    public User user;

    public ACache aCache;

    @AfterViews
    public void initViews() {
        initLocalData();
    }

    @Click(R.id.friend)
    void friend() {

    }

    @Click(R.id.follow)
    void follow() {

    }

    @Click(R.id.history)
    void history() {

    }

    @Click(R.id.back)
    void back() {
        this.finish();
        showActivityAnimation();
    }

    @Click(R.id.user_head)
    void userHead() {
        //上传头像
        Toast.makeText(this, "添加头像", Toast.LENGTH_SHORT).show();
        showDialog();
    }




    @Click(R.id.btn_exit)
    void exit() {
//        mTencent.logout(this);
        aCache.clear();
        startActivity(new Intent(this,LoginA_.class));
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
    }




    private void initLocalData() {
        aCache = ACache.get(this, MyConstants.USER_INFO);
        user = (User)aCache.getAsObject(MyConstants.USER_MESSAGE);
        userHead.setImageURI(user.getThumb());
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }
    public void showActivityAnimation(){
        overridePendingTransition(R.anim.a_hide_from_right,R.anim.b_show_from_right);
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
                        userHead.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "data为空", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}


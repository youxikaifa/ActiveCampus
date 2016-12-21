package com.thy.activecampus.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jin on 7/29.
 */

public class SendMsgListener implements TextWatcher {

    ImageView ivAdd ; //增加按钮
    TextView btnSend ; //发送按钮
    boolean isSend = false;

    public SendMsgListener(TextView btnSend,ImageView ivAdd) {
        this.btnSend = btnSend;
        this.ivAdd = ivAdd;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ScaleAnimation animation = null;
        if (s.toString().length()==0 && isSend){

            ivAdd.setVisibility(View.VISIBLE);
            animation = new ScaleAnimation(1,0,1,0,1,0.5f,1,0.5f);
            animation.setDuration(500);
            btnSend.startAnimation(animation);
            btnSend.setVisibility(View.GONE);
            isSend= false;

        }
        if (s.toString().length()>0 && (!isSend)){

            ivAdd.setVisibility(View.GONE);
            animation = new ScaleAnimation(0,1,0,1,1,0.5f,1,0.5f);
            animation.setDuration(500);
            btnSend.startAnimation(animation);
            btnSend.setVisibility(View.VISIBLE);
            isSend = true;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

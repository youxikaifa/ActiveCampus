package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;

/**
 * Created by Jin on 7/29.
 */

public class WelComeActivity extends BaseA {

    public ACache cache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().hide();
        cache = ACache.get(this, MyConstants.USER_INFO);


        String flag = cache.getAsString(MyConstants.IS_LOGIN);
        if (flag == null || flag.equals("NO")) {
            startActivity(new Intent(this, LoginA_.class));
        } else {
            startActivity(new Intent(this, MainActivity_.class));
            cache.put(MyConstants.IS_LOGIN, "YES");
        }
        this.finish();

    }


}

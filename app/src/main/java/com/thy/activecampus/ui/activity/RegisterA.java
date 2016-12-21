package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thy.activecampus.R;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
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
@EActivity(R.layout.activity_register)
public class RegisterA extends AppCompatActivity {

    public LabelReqImpl request = LabelReqImpl.getInstance();

    @ViewById(R.id.et_phonenum)
    EditText etPhoneNum;
    @ViewById(R.id.et_pwd)
    EditText etPwd;

    public ACache cache;

    @AfterViews
    public void initView() {
        cache = ACache.get(this, MyConstants.USER_INFO);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Click(R.id.tv_login)
    void login() {
        startActivity(new Intent(this, LoginA_.class));
    }

    @Click(R.id.recieveCode)
    void clickCode() {

    }

    @Click(R.id.tv_register)
    @Background
    void register() {
        User user = new User();
        String account = etPhoneNum.getText().toString();
        String password = etPwd.getText().toString();
        user.setAccount(account);
        user.setPwd(password);
        request.register(MyConstants.BASE_URL_ANOTHER_PORT, user, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String json = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (json.equals("1")) {
                            Toast.makeText(RegisterA.this, "此账号已注册", Toast.LENGTH_SHORT).show();
                        } else {
                            User u = new Gson().fromJson(json, User.class);
                            cache.put(MyConstants.USER_MESSAGE, u);
                            cache.put(MyConstants.IS_LOGIN, "YES");
                            startActivity(new Intent(RegisterA.this, CheckResA_.class));
                        }
                    }
                });

            }
        });
    }


}

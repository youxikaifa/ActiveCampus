package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.thy.activecampus.R;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.util.QQUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_login)
public class LoginA extends BaseA {

    public LabelReqImpl request = LabelReqImpl.getInstance();
    @ViewById(R.id.rootView)
    RelativeLayout rootView;
    @ViewById(R.id.contentView)
    RelativeLayout content;
    @ViewById(R.id.et_account)
    EditText etAccount;
    @ViewById(R.id.et_pwd)
    public EditText etPwd;
    public ACache cache;
    public Tencent mTencent;
    public Type listType;
    public PopupWindow window;
    public User user;


    @AfterViews
    public void initView() {
        init();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void init() {
        user = new User();
        cache = ACache.get(this, MyConstants.USER_INFO);
        listType = new TypeToken<ArrayList<User>>() {
        }.getType();
        mTencent = Tencent.createInstance("1105758160", this.getApplicationContext());

//        String expire_in = cache.getAsString("expire_in");
//        if (expire_in!=null){
//            String curTime = System.currentTimeMillis()+"";
//            if (expire_in.compareTo(curTime)>0){
//                startActivity(new Intent(this,MainActivity_.class));
//            }
//        }


//        Blurry.with(this)
//                .radius(25)
//                .sampling(1)
//                .async()
//                .animate(500)

//                .onto(rootView);
    }

    @Click(R.id.tv_login)
    @Background
    void login() {
        showWindow();

        User user = new User();
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        user.setAccount(account);
        user.setPwd(pwd);

        request.login(MyConstants.BASE_URL_ANOTHER_PORT, user, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginA.this, "login  failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String jsonArray = null;
                        try {
                            jsonArray = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (jsonArray.equals("0")) {
                            Toast.makeText(LoginA.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                            window.dismiss();
                        } else if (jsonArray.equals("1")) {
                            Toast.makeText(LoginA.this, "密码错误", Toast.LENGTH_SHORT).show();
                            window.dismiss();
                        } else {
                            Toast.makeText(LoginA.this, "登录成功", Toast.LENGTH_SHORT).show();
                            User u = new Gson().fromJson(jsonArray, User.class);
                            cache.put(MyConstants.USER_MESSAGE, u); //保存的是一串string
                            cache.put(MyConstants.IS_LOGIN, "YES");

                            response.close();

                            window.dismiss();
                            startActivity(new Intent(LoginA.this, MainActivity_.class));
                            LoginA.this.finish();

                        }
                    }
                });
            }
        });

    }

    @Background
    public void showWindow() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_for_login, null);
        window = new PopupWindow(v, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);

        window.setTouchable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                window.showAtLocation(content, Gravity.CENTER, 0, 0);
            }
        });
    }

    @Click(R.id.tv_register)
    void register() {
        startActivity(new Intent(this, RegisterA_.class));
    }

    @Click(R.id.iv_qq)
    void qq() {
        mTencent.login(this, SCOPE, listener);
    }

    @Click(R.id.iv_weibo)
    void weibo() {

    }

    @Click(R.id.iv_weixin)
    void weixin() {

    }

    IUiListener listener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject json) {

            initOpenidAndToken(json);


        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, listener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                QQUtil.showResultDialog(LoginA.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                QQUtil.showResultDialog(LoginA.this, "返回为空", "登录失败");
                return;
            }

            showWindow();

            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            QQUtil.toastMessage(LoginA.this, "onError: " + e.errorDetail);
            QQUtil.dismissDialog();
        }

        @Override
        public void onCancel() {
            QQUtil.toastMessage(LoginA.this, "登录已取消");
            QQUtil.dismissDialog();
        }
    }


    public void initOpenidAndToken(JSONObject jsonObject) {


        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }

            user.setToken(token);
            user.setOpenid(openId);
//            cache.put("access_token", token,ACache.TIME_DAY);
//            cache.put("expire_in", System.currentTimeMillis()+expires,ACache.TIME_DAY);
//            cache.put("openid", openId,ACache.TIME_DAY);

            final String url = "https://graph.qq.com/user/get_user_info?access_token=" + token + "&oauth_consumer_key="
                    + MyConstants.APP_ID + "&openid=" + openId;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    request.getUserInfo(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            try {
                                JSONObject object = new JSONObject(json);
                                String nickname = object.getString("nickname");
                                String figureurl = object.getString("figureurl_qq_2");
                                String gender = object.getString("gender");
                                if (gender.equals("女")){
                                    user.setSex(1);
                                }else{
                                    user.setSex(0);
                                }
                                user.setThumb(figureurl);
                                user.setName(nickname);

                                request.loginForThree(MyConstants.BASE_URL_ANOTHER_PORT, user, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String json = response.body().string();
                                        User u = new Gson().fromJson(json,User.class);
                                        cache.put(MyConstants.USER_MESSAGE,u);
                                        cache.put(MyConstants.IS_LOGIN,"YES");
                                        startActivity(new Intent(LoginA.this, MainActivity_.class));
                                        LoginA.this.finish();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {

        }
    }


}

package com.thy.activecampus.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tencent.tauth.Tencent;
import com.thy.activecampus.R;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.activity.MainActivity;
import com.thy.activecampus.ui.activity.MainActivity_;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class BaseA extends AppCompatActivity {

//    public LabelReqImpl request = LabelReqImpl.getInstance();

    public static final String SCOPE = "all";  //访问用户资料
    public static HashMap<String,WeakReference<Activity>> mContext = new HashMap<>();
    public static final String BASE_URL = "http://121.42.206.117:3000/v1/";
    public static List<Activity> activities = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }

    public static synchronized void setActivityContext(Activity context){
        WeakReference<Activity> reference = new WeakReference<Activity>(context);
        mContext.put(context.getClass().getSimpleName(),reference);
    }

    public String getTime(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String a = sdf.format(time);
        return sdf.format(time);
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




    public void initTencentInstance(){
//
//        if (mTencent.isSessionValid()){
//            startActivity(new Intent(this,MainActivity_.class));
//        }
    }



}

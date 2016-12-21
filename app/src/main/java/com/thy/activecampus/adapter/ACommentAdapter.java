package com.thy.activecampus.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.AutoComment;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.view.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */

public class ACommentAdapter extends BaseAdapter{

    public static final LabelReqImpl request = LabelReqImpl.getInstance();
    Context context;
    List<AutoComment> comments;
    LayoutInflater inflater;
    RepAdapter adapter;
    Map<Integer,List<AutoComment>> map;


    public ACommentAdapter(Context context, List<AutoComment> comments, Map<Integer,List<AutoComment>> map) {
        this.context = context;
        this.comments = comments;
        this.inflater = LayoutInflater.from(context);
        this.map = map;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public AutoComment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int p, View view, ViewGroup parent) {
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.item_auto_comment, null);
            holder.viewHead = (SimpleDraweeView) view.findViewById(R.id.view_head);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.content = (TextView) view.findViewById(R.id.content);
            holder.lvRep = (MyListView) view.findViewById(R.id.lvRep);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.viewHead.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+comments.get(p).getFrom_user().getThumb());
        holder.name.setText(comments.get(p).getFrom_user().getName());
        holder.time.setText(logicTime(comments.get(p).getCreated()));
        holder.content.setText(comments.get(p).getContent());
        adapter = new RepAdapter(context,map.get(p));
        holder.lvRep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                callback.rCallBack(map.get(p).get(position));
            }
        });
        holder.lvRep.setAdapter(adapter);

        return view;
    }


    class Holder {
        SimpleDraweeView viewHead;
        TextView name, time, content;
        MyListView lvRep;
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

    public  RepCallBack callback;

    public interface RepCallBack{
        void rCallBack(AutoComment comment) ;
    }

    public void setRCallback(RepCallBack callback){
        this.callback = callback;
    }

}

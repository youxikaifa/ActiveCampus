package com.thy.activecampus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.GridOnClickListener;
import com.thy.activecampus.listener.OnItemClickListener;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.view.NoScrollGridView;
import com.thy.activecampus.widget.DynamicTagFlowLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Jin on 7/29.
 */

public class RecentAdapter extends RecyclerView.Adapter {

    public static final LabelReqImpl request = LabelReqImpl.getInstance();
    private LayoutInflater inflater;
    private Context context;
    private List<LabelM> list;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOTER = 0;
    private GridImgAdapter adapter;

    private OnItemClickListener onItemClickListener = null;


    public RecentAdapter(Context context, List<LabelM> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    public void refreshItem(List<LabelM> newDatas) {
        list.removeAll(list);
        list.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addLoadItem(List<LabelM> newDatas) {
        list.addAll(newDatas);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {

        if ((position == getItemCount() - 1) && getItemCount() > 1) {
            return TYPE_FOOTER;
        }else{
            return TYPE_NORMAL;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_NORMAL) {

            View v =  inflater.inflate(R.layout.item_recent_news_normal, null);

            return new NormalHolder(v, onItemClickListener);
        } else if (viewType == TYPE_FOOTER) {
            View view =  inflater.inflate(R.layout.item_loadmore, null);

            return new FooterHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NormalHolder) {

            NormalHolder normalHolder = (NormalHolder) holder;


            if (list.size() > 0) {
                String url = list.get(position).getHead();
                if (list.get(position).getSex() == 0) {
                    normalHolder.sex.setImageResource(R.drawable.ic_male);
                } else {
                    normalHolder.sex.setImageResource(R.drawable.ic_famale);
                }

                if (list.get(position).getType() == 0) {
//            holder.type.setImageResource(null);
                }
                if (url.substring(0,4).equals("http")){
                    normalHolder.head.setImageURI(url);
                }else{
                    normalHolder.head.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+list.get(position).getHead());
                }

                normalHolder.name.setText(list.get(position).getName());
                normalHolder.title.setText(list.get(position).getTitle());
                normalHolder.content.setText(list.get(position).getContent());
                normalHolder.pubTime.setText(logicTime(list.get(position).getPubTime()));
                if (list.get(position).getPicUrls().size() == 0) {
                    normalHolder.gvImgs.setVisibility(View.GONE);
                } else {
                    normalHolder.gvImgs.setVisibility(View.VISIBLE);
                    adapter = new GridImgAdapter(context, list.get(position).getPicUrls());
                    normalHolder.gvImgs.setAdapter(adapter);
                    normalHolder.gvImgs.setOnItemClickListener(new GridOnClickListener(context, list.get(position).getPicUrls()));
                }

                if (list.get(position).getTags().size()!=0){
                    normalHolder.tags.setVisibility(View.VISIBLE);
                    normalHolder.tags.setTags(list.get(position).getTags());
                }else{
                    normalHolder.tags.setVisibility(View.GONE);
                }


            }


        } else if (holder instanceof FooterHolder) {
            //都是静态数据,不需要写
            if (getItemCount()>3){
                ((FooterHolder) holder).bar.setVisibility(View.VISIBLE);
            }else{
                ((FooterHolder) holder).bar.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return 0;
        } else {
            return list.size() + 1;
        }
    }


    class NormalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener mOnItemClickListener = null;
        SimpleDraweeView head;
        ImageView sex, type;
        DynamicTagFlowLayout tags;
        TextView name, title, content, pubTime;
        NoScrollGridView gvImgs; //对应picUrls

        public NormalHolder(View view, OnItemClickListener listener) {
            super(view);
            mOnItemClickListener = listener;
            view.setOnClickListener(this);
            head = (SimpleDraweeView) view.findViewById(R.id.head);
            sex = (ImageView) view.findViewById(R.id.iv_sex);
            type = (ImageView) view.findViewById(R.id.iv_type);
            name = (TextView) view.findViewById(R.id.name);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            pubTime = (TextView) view.findViewById(R.id.pubTime);
            gvImgs = (NoScrollGridView) view.findViewById(R.id.gridImgs);
            tags = (DynamicTagFlowLayout) view.findViewById(R.id.tags);

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        ProgressBar bar;
//        TextView tvLoad;

        public FooterHolder(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.bar);
//            tvLoad = (TextView) itemView.findViewById(R.id.tv_load);
        }
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
}

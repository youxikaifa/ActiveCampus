package com.thy.activecampus.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.model.Losing;
import com.thy.activecampus.ui.activity.LosingA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class LosingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    private static final int TYPE_HEAD = 2;  //顶部 headView

    private static final int ON_LOADING = 0;  //正在加载
    private static final int NO_MORE_DATA = 1;  //没有更多数据
    private int LOAD_MORE_STATUS = 0;

    public Context context;
    public List<Losing> list;
    public LayoutInflater inflater ;
    SimpleDateFormat sdf;
    int realPosition;

    public LosingAdapter(Context context, List<Losing> list) {
        this.context = context;
        this.list = list;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_losing, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.item_footer_view1,parent,false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;

        } else if (viewType == TYPE_HEAD) {
            View view = inflater.inflate(R.layout.item_feeling_head, parent, false);
            HeadViewHolder headViewHolder = new HeadViewHolder(view);
            return headViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FootViewHolder) {
            FootViewHolder footerHolder = (FootViewHolder) holder;
            switch (LOAD_MORE_STATUS) {
                case ON_LOADING:
                    footerHolder.tv_footer.setText("正在加载...");
                    break;
                case NO_MORE_DATA:
                    footerHolder.tv_footer.setText("没有更多数据");
                    break;
                default:
                    break;
            }
        } else if (holder instanceof HeadViewHolder) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
        } else if (holder instanceof ItemViewHolder) {

            if (list.size() != 0) {
                realPosition = position -1;
                if (list.get(realPosition).getThumbs()!=null){
                    ((ItemViewHolder) holder).img.setImageURI(MyConstants.BASE_URL+list.get(realPosition).getThumbs().get(0));
                }
                ((ItemViewHolder) holder).head.setImageURI(MyConstants.BASE_URL+list.get(realPosition).getHead());
                ((ItemViewHolder) holder).title.setText(list.get(realPosition).getTitle());
                ((ItemViewHolder) holder).name.setText(list.get(realPosition).getName());
                ((ItemViewHolder) holder).phone.setText(list.get(realPosition).getContact());
                ((ItemViewHolder) holder).addr.setText(list.get(realPosition).getAddr());
                ((ItemViewHolder) holder).time.setText(time2Date(list.get(realPosition).getTime()));
                if (list.get(realPosition).getType()==0){
                    ((ItemViewHolder) holder).type.setText("失物");
                    ((ItemViewHolder) holder).type.setBackgroundResource(R.drawable.xml_losing_background);
                }else{
                    ((ItemViewHolder) holder).type.setText("招领");
                    ((ItemViewHolder) holder).type.setBackgroundResource(R.drawable.xml_get_background);
                }
                ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.onItemClick(position-1);
                    }
                });

            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+2;
    }
    public void changeLoadStatus(int status){
        LOAD_MORE_STATUS = status;
        notifyDataSetChanged();
    }


    /**
     * 动态发表时间
     * @param time 发表的时间
     * @return
     */
    public String time2Date(String time){

        return sdf.format(new Date(Long.parseLong(time)));
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img,head;
        TextView title,name,addr,phone,time,type;
        RelativeLayout itemView;
        public ItemViewHolder(View view) {
            super(view);
            img = (SimpleDraweeView) view.findViewById(R.id.view_pic);
            head = (SimpleDraweeView) view.findViewById(R.id.view_head);
            title = (TextView) view.findViewById(R.id.title);
            name = (TextView) view.findViewById(R.id.name);
            phone = (TextView) view.findViewById(R.id.phone);
            addr = (TextView) view.findViewById(R.id.addr);
            time = (TextView) view.findViewById(R.id.time);
            type = (TextView) view.findViewById(R.id.type);
            itemView = (RelativeLayout) view.findViewById(R.id.itemView);
        }
    }

    /**
     * 底部FootView布局
     */
    public class FootViewHolder extends RecyclerView.ViewHolder {
        TextView tv_footer;

        public FootViewHolder(View view) {
            super(view);
            tv_footer = (TextView) view.findViewById(R.id.tv_footer__view);
        }
    }


    /**
     * 头部FootView布局
     */
    public class HeadViewHolder extends RecyclerView.ViewHolder {
        public HeadViewHolder(View view) {
            super(view);

        }
    }

    public LostCallBack callBack;
    public interface LostCallBack{
        void onItemClick(int position);
    }
    public void setLCallBack(LostCallBack callBack){
        this.callBack = callBack;
    }
}

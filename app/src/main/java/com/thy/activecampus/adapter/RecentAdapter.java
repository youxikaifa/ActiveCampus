package com.thy.activecampus.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
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
    private static final int TYPE_HEADER = 2;

    private static final int ON_LOADING = 0;  //正在加载
    private static final int NO_MORE_DATA = 1;  //没有更多数据
    private int LOAD_MORE_STATUS = 0;
    int realPosition; //由于头造成的位置变化


    public RecentAdapter(Context context, List<LabelM> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = inflater.inflate(R.layout.item_recent_news_normal, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.item_footer_view1,parent,false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;

        } else if (viewType == TYPE_HEADER) {
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
            headViewHolder.space.setVisibility(View.VISIBLE);

        } else if (holder instanceof ItemViewHolder) {

            if (list.size() != 0) {
                realPosition = position -1;


                ((ItemViewHolder) holder).viewHead.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT + list.get(realPosition).getHead());
                ((ItemViewHolder) holder).name.setText(list.get(realPosition).getName());
                ((ItemViewHolder) holder).time.setText(logicTime(list.get(realPosition).getPubTime()));
                ((ItemViewHolder) holder).content.setText(list.get(realPosition).getContent());
                ((ItemViewHolder) holder).pics.setText(list.get(realPosition).getPicUrls().size() + "");
                ((ItemViewHolder) holder).motto.setText(list.get(realPosition).getMotto());

                if (list.get(realPosition).getSex()==0){
                    ((ItemViewHolder) holder).ivSex.setImageResource(R.drawable.ic_famale);
                }else{
                    ((ItemViewHolder) holder).ivSex.setImageResource(R.drawable.ic_male);
                }

                if (list.get(realPosition).getPicUrls().size()==0){
                    ((ItemViewHolder) holder).flContent.setVisibility(View.GONE);
                }else{
                    ((ItemViewHolder) holder).flContent.setVisibility(View.VISIBLE);
                    ControllerListener controllerListener = getListener(((ItemViewHolder) holder).viewPic);
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setControllerListener(controllerListener)
                            .setUri(Uri.parse(MyConstants.BASE_URL + "/images/upload/" + list.get(realPosition).getPicUrls().get(0).substring(17)))
                            .build();
                    ((ItemViewHolder) holder).viewPic.setController(controller);
                }
                if (list.get(realPosition).getTags().size()!=0){
                    ((ItemViewHolder) holder).tags.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).tags.setTags(list.get(realPosition).getTags());
                }else{
                    ((ItemViewHolder) holder).tags.setVisibility(View.GONE);
                }

                ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        labelCallBack.onItemClick(position-1);
                    }
                });


                ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        labelCallBack.onItemClick(position-1);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView viewPic, viewHead;
        TextView name, time, type, content, pics, school, motto;
        RelativeLayout itemView;
        DynamicTagFlowLayout tags;
        ImageView ivSex;
        FrameLayout flContent;

        public ItemViewHolder(View view) {
            super(view);
            viewPic = (SimpleDraweeView) view.findViewById(R.id.view_pic);
            viewHead = (SimpleDraweeView) view.findViewById(R.id.head);
            name = (TextView) view.findViewById(R.id.tv_name);
            time = (TextView) view.findViewById(R.id.tv_time);
            type = (TextView) view.findViewById(R.id.tv_type);
            school = (TextView) view.findViewById(R.id.tv_school);
            motto = (TextView) view.findViewById(R.id.tv_motto);
            content = (TextView) view.findViewById(R.id.tv_content);
            ivSex = (ImageView) view.findViewById(R.id.iv_sex);
            pics = (TextView) view.findViewById(R.id.picNum);
            itemView = (RelativeLayout) view.findViewById(R.id.itemView);
            tags = (DynamicTagFlowLayout) view.findViewById(R.id.tags);
            flContent = (FrameLayout) view.findViewById(R.id.fl_content);
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
        RelativeLayout space;
        public HeadViewHolder(View view) {
            super(view);
            space = (RelativeLayout) view.findViewById(R.id.space);
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

    public ControllerListener getListener(final SimpleDraweeView view) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = 1080;
                layoutParams.height = (int) ((float) (1080 * height) / (float) width);
                view.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                Log.d("TAG", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        return controllerListener;
    }

    public LabelCallBack labelCallBack;

    public interface LabelCallBack{
        void onItemClick(int position);
    }

    public void setLabelCallBack(LabelCallBack callBack){
        this.labelCallBack = callBack;
    }

    public void changeLoadStatus(int status) {
        LOAD_MORE_STATUS = status;
        notifyDataSetChanged();
    }
}

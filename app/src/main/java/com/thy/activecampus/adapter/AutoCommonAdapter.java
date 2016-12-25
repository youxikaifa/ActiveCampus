package com.thy.activecampus.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.thy.activecampus.model.AutoDyne;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class AutoCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM =0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private static final int ON_LOADING =0;  //正在加载
    private static final int NO_MORE_DATA = 1;  //没有更多数据
    private int LOAD_MORE_STATUS = 0;

    public Context context;
    public List<AutoDyne> list;
    public LayoutInflater inflater ;
    SimpleDateFormat sdf;
    String userId;

    public AutoCommonAdapter(Context context, List<AutoDyne> list, String userId) {
        this.context = context;
        this.list = list;
        this.userId = userId;
        this.inflater = LayoutInflater.from(context);
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==TYPE_ITEM){
            View view = inflater.inflate(R.layout.item_autodyne,parent,false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }else if (viewType == TYPE_FOOTER){
            View view = inflater.inflate(R.layout.item_footer_view1,parent,false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FootViewHolder){
            FootViewHolder footerHolder = (FootViewHolder) holder;
            switch(LOAD_MORE_STATUS){
                case ON_LOADING:
                    footerHolder.tv_footer.setText("正在加载...");
                    break;
                case NO_MORE_DATA:
                    footerHolder.tv_footer.setText("没有更多数据");
                    break;
                default:break;
            }
        }else if (holder instanceof ItemViewHolder){
            ControllerListener controllerListener = getListener(((ItemViewHolder)holder).viewPic);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(Uri.parse(MyConstants.BASE_URL+"/images/upload/"+list.get(position).getPics().get(0).substring(17)))
                    .build();
            ((ItemViewHolder)holder).viewPic.setController(controller);
            ((ItemViewHolder)holder).viewHead.setImageURI(MyConstants.BASE_URL_ANOTHER_PORT+list.get(position).getHead());
            ((ItemViewHolder)holder).name.setText(list.get(position).getName());
            ((ItemViewHolder)holder).time.setText(time2Date(list.get(position).getTime()));
            ((ItemViewHolder)holder).title.setText(list.get(position).getTitle());
            ((ItemViewHolder)holder).likeNum.setText(list.get(position).getLikes().size() + "");
            ((ItemViewHolder)holder).scanNum.setText(list.get(position).getScans() + "");
            ((ItemViewHolder)holder).picNum.setText(list.get(position).getPics().size() + "");

            if (list.get(position).getType()==0){
                ((ItemViewHolder)holder).type.setText("帅哥");
            }else if (list.get(position).getType()==1){
                ((ItemViewHolder)holder).type.setText("美女");
            }else if(list.get(position).getType()==2){
                ((ItemViewHolder)holder).type.setText("情侣");
            }

            if (list.get(position).getLikes().contains(userId)){
                ((ItemViewHolder)holder).ivLike.setImageResource(R.drawable.ic_like_red);
                ((ItemViewHolder)holder).likeNum.setTextColor(context.getResources().getColor(R.color.like));
            }else{
                ((ItemViewHolder)holder).ivLike.setImageResource(R.drawable.ic_like_gray);
                ((ItemViewHolder)holder).likeNum.setTextColor(context.getResources().getColor(R.color.unlike));
            }

            ((ItemViewHolder)holder).like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cCallback.onLikeClick(position);
                }
            });
            ((ItemViewHolder)holder).share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cCallback.onShareClick(position);
                }
            });
            ((ItemViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cCallback.onItemClick(position);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }



    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public  class ItemViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView viewPic, viewHead;
        TextView name, time, type,title, likeNum, scanNum, picNum;
        RelativeLayout share, scan, like,itemView;
        ImageView ivLike;
        public ItemViewHolder(View view){
            super(view);
            viewPic = (SimpleDraweeView) view.findViewById(R.id.view_pic);
            viewHead = (SimpleDraweeView) view.findViewById(R.id.view_head);
            name = (TextView) view.findViewById(R.id.tv_name);
            time = (TextView) view.findViewById(R.id.tv_time);
            type = (TextView) view.findViewById(R.id.tv_type);
            title = (TextView) view.findViewById(R.id.tv_title);
            ivLike = (ImageView) view.findViewById(R.id.iv_like);
            likeNum = (TextView) view.findViewById(R.id.likeNum);
            scanNum = (TextView) view.findViewById(R.id.scanNum);
            picNum = (TextView) view.findViewById(R.id.picNum);
            share = (RelativeLayout) view.findViewById(R.id.rl_share);
            scan = (RelativeLayout) view.findViewById(R.id.rl_scan);
            like = (RelativeLayout) view.findViewById(R.id.rl_like);
            itemView = (RelativeLayout) view.findViewById(R.id.rl_item);
        }
    }
    /**
     * 底部FootView布局
     */
    public class FootViewHolder extends  RecyclerView.ViewHolder{
        TextView tv_footer;
        public FootViewHolder(View view) {
            super(view);
            tv_footer=(TextView)view.findViewById(R.id.tv_footer__view);
        }
    }


    public ControllerListener getListener(final SimpleDraweeView view){
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

        return  controllerListener;
    }

    public ChildCallBack cCallback;

    /**
     * 定义点赞和分享的点击接口
     */
    public interface ChildCallBack {
        void onShareClick(int position);
        void onLikeClick(int position);
        void onItemClick(int position);
    }

    public void setcCallBack(ChildCallBack cCallBack){
        this.cCallback = cCallBack;
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
}

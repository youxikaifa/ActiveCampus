package com.thy.activecampus.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.thy.activecampus.R;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.Losing;
import com.thy.activecampus.widget.SlideView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jin on 7/29.
 */

@EActivity(R.layout.activity_lost_detail)
public class LostDetailA extends AppCompatActivity {

    @ViewById(R.id.view_nopic)
    SimpleDraweeView viewNopic;
    @ViewById(R.id.mSlideView)
    SlideView mSlideView;

    @ViewById(R.id.view_head)
    SimpleDraweeView viewHead;
    @ViewById(R.id.tv_name)
    TextView name;
    @ViewById(R.id.tv_phone)
    TextView phone;
    @ViewById(R.id.tv_time)
    TextView time;
    @ViewById(R.id.tv_addr)
    TextView addr;
    @ViewById(R.id.tv_remark)
    TextView remark;
    @ViewById(R.id.tv_school)
    TextView school;
    @ViewById(R.id.tv_type)
    TextView type;

    public Losing losing;

    @AfterViews
    public void initViews(){
        preInit();

        if (losing.getThumbs()==null){
            viewNopic.setVisibility(View.VISIBLE);
            mSlideView.setVisibility(View.GONE);
        }else {
            viewNopic.setVisibility(View.GONE);
            mSlideView.setVisibility(View.VISIBLE);
            mSlideView.setData(losing.getThumbs());
        }


    }


    public void preInit(){
        Intent intent = getIntent();
        losing = (Losing) intent.getSerializableExtra("lost");
        name.setText(losing.getName());
        phone.setText(losing.getContact());
        time.setText(time2Date(losing.getTime()));
        addr.setText(losing.getAddr());
        remark.setText(losing.getRemark());
        school.setText(losing.getSchool());
        if (losing.getType()==0){
            type.setText("失物");
        }else{
            type.setText("招领");
        }
        if (losing.getHead().substring(0,4).equals("http")){
            viewHead.setImageURI(losing.getHead());
        }else{
            viewHead.setImageURI(MyConstants.BASE_URL+losing.getHead());
        }

    }

    public String time2Date(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.parseLong(time)));
    }

    @Click(R.id.rl_back)
    void back(){
        this.finish();
    }

    @Click(R.id.rl_phone)
    void phone(){
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + losing.getContact())));
    }

}

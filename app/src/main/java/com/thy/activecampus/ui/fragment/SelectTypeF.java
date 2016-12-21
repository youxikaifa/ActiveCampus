package com.thy.activecampus.ui.fragment;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.thy.activecampus.R;
import com.thy.activecampus.ui.activity.PubAutoDyneA;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Jin on 7/29.
 */
@EFragment(R.layout.fragment_select_type)
public class SelectTypeF extends Fragment {


    @ViewById(R.id.iv_boy)
    ImageView ivBoy;
    @ViewById(R.id.iv_girl)
    ImageView ivGirl;
    @ViewById(R.id.iv_couple)
    ImageView ivCouple;

    PubAutoDyneA activity;

    @AfterViews
    public void initViews(){
        activity = (PubAutoDyneA) getActivity();

    }

    @Click(R.id.iv_boy)
    void boy(){
        ivBoy.setBackgroundResource(R.drawable.xml_type_select);
        ivGirl.setBackgroundResource(R.drawable.xml_type_unselect);
        ivCouple.setBackgroundResource(R.drawable.xml_type_unselect);
        activity.onReloadType(0);
    }

    @Click(R.id.iv_girl)
    void girl(){
        ivBoy.setBackgroundResource(R.drawable.xml_type_unselect);
        ivGirl.setBackgroundResource(R.drawable.xml_type_select);
        ivCouple.setBackgroundResource(R.drawable.xml_type_unselect);
        activity.onReloadType(1);
    }

    @Click(R.id.iv_couple)
    void couple(){
        ivBoy.setBackgroundResource(R.drawable.xml_type_unselect);
        ivGirl.setBackgroundResource(R.drawable.xml_type_unselect);
        ivCouple.setBackgroundResource(R.drawable.xml_type_select);
        activity.onReloadType(2);
    }
}

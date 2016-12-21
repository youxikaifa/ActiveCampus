package com.thy.activecampus.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.thy.activecampus.R;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.ui.fragment.HomeF;
import com.thy.activecampus.ui.fragment.HomeF_;
import com.thy.activecampus.ui.fragment.MesageF;
import com.thy.activecampus.ui.fragment.MesageF_;
import com.thy.activecampus.ui.fragment.SortF;
import com.thy.activecampus.ui.fragment.SortF_;
import com.thy.activecampus.ui.fragment.UserF;
import com.thy.activecampus.ui.fragment.UserF_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseA {

    @ViewById(R.id.bottom_navigation_bar)
    BottomNavigationBar tabBar;
    @ViewById(R.id.content)
    FrameLayout flContent;


    private FragmentManager fm;
    private FragmentTransaction ft;

    private SortF sortF = null;
    private HomeF homeF = null;
    private MesageF mesageF = null;
    private UserF userF = null;

    private int oldPosition = 0;
    private Fragment[] fragments;

    public ACache cache;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @AfterViews
    public void initViews() {


        tabBar.addItem(new BottomNavigationItem(R.drawable.ic_message, ""))
                .addItem(new BottomNavigationItem(R.drawable.ic_home, ""))
                .addItem(new BottomNavigationItem(R.drawable.ic_sort, ""))
                .addItem(new BottomNavigationItem(R.drawable.ic_my, ""))
                .setActiveColor(R.color.colorPrimary)
                .setInActiveColor(R.color.white)
//                .setMode(BottomNavigationBar.MODE_FIXED)

//                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setFirstSelectedPosition(0)

                .initialise();
//        tabBar.setAutoHideEnabled(true);


        tabBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                ft = fm.beginTransaction();
                switch (position) {
                    case 0:
                        if (mesageF == null) {
                            mesageF = new MesageF_();
                            fragments[0] = mesageF;
                            ft.add(R.id.content, mesageF);
                        }
                        hideFragment(ft, 0);
                        ft.show(mesageF);

                        break;
                    case 1:
                        if (homeF == null) {
                            homeF = new HomeF_();
                            fragments[1] = homeF;
                            ft.add(R.id.content, homeF);
                        }
                        hideFragment(ft, 1);
                        ft.show(homeF);

                        break;

                    case 2:
                        if (sortF == null) {
                            sortF = new SortF_();
                            fragments[2] = sortF;
                            ft.add(R.id.content, sortF);
                        }
                        hideFragment(ft, 2);

                        ft.show(sortF);
                        break;
                    case 3:
                        if (userF == null) {
                            userF = new UserF_();
                            fragments[3] = userF;
                            ft.add(R.id.content, userF);
                        }
                        hideFragment(ft, 3);

                        ft.show(userF);
                        break;

                    default:
                        Toast.makeText(MainActivity.this, "no select", Toast.LENGTH_SHORT).show();
                        break;
                }
                ft.commit();

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }


    private void hideFragment(FragmentTransaction ft, int newPosition) {
        if (fragments[oldPosition] != null && fragments[oldPosition].isVisible()) {
            ft.hide(fragments[oldPosition]);
        }
        oldPosition = newPosition;
    }


//    @Click(R.id.user_head)
//    void userHead() {
//        startActivity(new Intent(this, UserA_.class));
//        overridePendingTransition(R.anim.a_show_from_left, R.anim.b_hide_from_left);
//    }



    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            fragments = new Fragment[4];
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            mesageF = new MesageF_.FragmentBuilder_().build();
            fragments[0] = mesageF;
            ft.add(R.id.content, mesageF);
            ft.commit();
        }
    }
}

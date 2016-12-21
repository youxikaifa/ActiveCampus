package com.thy.activecampus.ui.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.RecentAdapter;
import com.thy.activecampus.base.BaseF;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.OnItemClickListener;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.activity.AddLabelA_;
import com.thy.activecampus.ui.activity.LabelDetailA_;
import com.thy.activecampus.ui.activity.SearchLabelsA_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EFragment(R.layout.fragment_main_home)
public class HomeF extends BaseF implements SwipeRefreshLayout.OnRefreshListener {

    private LabelReqImpl request = LabelReqImpl.getInstance();
    private Timer timer = new Timer();

    @ViewById(R.id.notice)
    TextView notice;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @ViewById(R.id.fab)
    FloatingActionButton fab;
    Animation btnShow = null;
    Animation btnHide = null;

    public RecentAdapter adapter;
    private List<LabelM> list;
    private List<LabelM> newList = new ArrayList<>(); //存储每次从网上获取的数据
    public LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
    public int lastVisibleItem = 0;

    public List<LabelM> nativeDatas = new ArrayList<>();

    public ACache cache;


    @AfterViews
    public void initView() {
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00bcd4")));
        list = new ArrayList<>();
        cache = ACache.get(getActivity(), MyConstants.USER_INFO);

        mSwipeLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED); //设置颜色
        mSwipeLayout.setOnRefreshListener(this); //设置下拉刷新的监听
        mSwipeLayout.setDistanceToTriggerSync(300);  // 设置手指在屏幕下拉多少距离会触发下拉刷新

        adapter = new RecentAdapter(getActivity(), list);

        this.adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (list.size() > 0) {
                    saveRecentScan(position);
                    EventBus.getDefault().postSticky(list.get(position));
                    startActivity(new Intent(getActivity(), LabelDetailA_.class));
                }

            }
        });
        mRecyclerView.setLayoutManager(llManager);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == adapter.getItemCount()) {
                    loadMore();
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 15 && fab.isShown()) {
                    fab.setVisibility(View.GONE);
                    if (btnShow == null) {
                        btnShow = AnimationUtils.loadAnimation(getActivity(), R.anim.float_btn_show);
                    }
                    fab.startAnimation(btnShow);

                } else if (dy < -15 && !fab.isShown()) {
                    fab.setVisibility(View.VISIBLE);
                    if (btnHide == null) {
                        btnHide = AnimationUtils.loadAnimation(getActivity(), R.anim.float_btn_hide);
                    }
                    fab.startAnimation(btnHide);

                }
                lastVisibleItem = llManager.findLastVisibleItemPosition();

            }
        });


        mRecyclerView.setAdapter(adapter);

        loadNativeData();
        if (list.size()==0){
            notice.setVisibility(View.GONE);
        }else{
            notice.setVisibility(View.VISIBLE);
        }
    }


    @Background
    public void saveRecentScan(int position) {
        JSONArray array = cache.getAsJSONArray(MyConstants.LABEL_NATIVE_CACHE);
        if (array != null) {
            nativeDatas = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<LabelM>>() {}.getType());
            if (!nativeDatas.contains(list.get(position))) {
                if (nativeDatas.size() > 50) {
                    nativeDatas.remove(0);
                }
                nativeDatas.add(list.get(position));
                String json = new Gson().toJson(nativeDatas);
                cache.put(MyConstants.LABEL_RECENT_SCAN, json);
            }
        }

    }


    @Background
    public void loadNativeData() {
        JSONArray array = cache.getAsJSONArray(MyConstants.LABEL_NATIVE_CACHE);
        if (array != null) {
            newList = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<LabelM>>() {
            }.getType());
            list.addAll(newList);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }


    }

    @Background
    public void fetchData() {
        request.get(MyConstants.BASE_URL_FOR_FIND, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            failRequest();
                        }
                    });
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String jsonArray = null;
                try {
                    jsonArray = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list = new Gson().fromJson(jsonArray, new TypeToken<ArrayList<LabelM>>() {
                }.getType()); //

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.refreshItem(list);
                        mSwipeLayout.setRefreshing(false);
                        response.close();
                    }
                });

                cache.put(MyConstants.LABEL_NATIVE_CACHE, jsonArray);

            }
        });
    }

    @Background
    public void loadMore() {
        if (list.size() != 0) {
            int count = list.size() - 1;
            String url = MyConstants.BASE_URL_FOR_LOAD_MORE + list.get(count).getPubTime();
            request.get(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                failRequest();
                            }
                        });
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String jsonArray = null;
                    try {
                        jsonArray = response.body().string();
                        if (jsonArray!=null){
                            newList = new Gson().fromJson(jsonArray, new TypeToken<ArrayList<LabelM>>() {}.getType());
                            list.addAll(newList);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addLoadItem(newList);
                            response.close();
                        }
                    });

                }
            });
        }
    }


    @UiThread
    public void failRequest() {
        mSwipeLayout.setRefreshing(false);
        Toast.makeText(getActivity(), "当前网络状况不佳,请稍后重试", Toast.LENGTH_SHORT).show();
    }

    @Override

    public void onRefresh() {
        fetchData();
    }

//    @UiThread
//    public void refreshtimeout(){
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mSwipeLayout.setRefreshing(false);
//                        Toast.makeText(mContext, "当前网络状况不佳,请稍后重试", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        },5000);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }


    @Click(R.id.rl_search)
    void search() {
        startActivity(new Intent(getActivity(), SearchLabelsA_.class));
    }

    @Click(R.id.fab)
    void fab() {
        startActivity(new Intent(getActivity(), AddLabelA_.class));
    }

}

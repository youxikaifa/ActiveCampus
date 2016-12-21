package com.thy.activecampus.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.RecentAdapter;
import com.thy.activecampus.base.BaseF;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.listener.OnItemClickListener;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.activity.LabelDetailA_;
import com.thy.activecampus.ui.activity.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

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
@EFragment(R.layout.fragment_recent)
public class RecentFra extends BaseF implements SwipeRefreshLayout.OnRefreshListener {


    private static final String BASE_URL = "http://121.42.206.117:3000/v1/find";
    private static final String LOAD_MORE_BASE_URL = "http://121.42.206.117:3000/v1/loadmore?publishTime=";
    private LabelReqImpl request = LabelReqImpl.getInstance();
    private Timer timer = new Timer();

    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public RecentAdapter adapter;
    private List<LabelM> list;
    private List<LabelM> newList = new ArrayList<>(); //存储每次从网上获取的数据
    private int countLabels = 0;
    private Context mContext = getContext();
    public LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
    public int lastVisibleItem = 0;
    float newY = 0;
    float oldY = 0;

    public ACache cache;

    @AfterViews
    public void initView() {
        list = new ArrayList<>();
        cache = ACache.get(getActivity());
        init();
        mSwipeLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED); //设置颜色
        mSwipeLayout.setOnRefreshListener(this); //设置下拉刷新的监听
        mSwipeLayout.setDistanceToTriggerSync(300);  // 设置手指在屏幕下拉多少距离会触发下拉刷新

        adapter = new RecentAdapter(getActivity(), list);

        this.adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (list.size() > 0) {
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
                lastVisibleItem = llManager.findLastVisibleItemPosition();
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    newY = event.getY();
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    oldY = event.getY();
                }

                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }


    private void init() {
        countLabels();
        loadNativeData();
    }

    private void loadNativeData() {
        String jsonArray = cache.getAsString("data");
        if (jsonArray != null) {
            list = new Gson().fromJson(jsonArray, new TypeToken<ArrayList<LabelM>>() {
            }.getType());
        }

    }

    @Background
    public void fetchData() {
        cache.clear();
        request.get(BASE_URL, new Callback() {
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
                Type listType = new TypeToken<ArrayList<LabelM>>() {
                }.getType();
                String jsonArray = null;
                try {
                    jsonArray = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list = new Gson().fromJson(jsonArray, listType); //


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countLabels();
                        adapter.refreshItem(list);
                        mSwipeLayout.setRefreshing(false);
                        response.close();
                    }
                });

                cache.put("data", jsonArray);

            }
        });
    }

    @Background
    public void loadMore() {
        if (list.size() != 0) {
            int count = list.size() - 1;
            String url = LOAD_MORE_BASE_URL + list.get(count).getPubTime();
            request.get(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("LOAD_MORE", "FAILED");
                                failRequest();
                            }
                        });
                    } catch (Exception a) {
                        a.printStackTrace();
                    }
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Log.v("LOAD_MORE", "SUCCESS");
                    Type listType = new TypeToken<ArrayList<LabelM>>() {
                    }.getType();
                    String jsonArray = null;
                    try {
                        jsonArray = response.body().string();
                        newList = new Gson().fromJson(jsonArray, listType);
                        list.addAll(newList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            adapter.getAllNum(countLabels);
                            adapter.addLoadItem(newList);
                            response.close();
                        }
                    });

                }
            });
        }
    }

    public void countLabels() {
        String url = "http://121.42.206.117:3000/v1/getall";
        request.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failRequest();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonArray = null;
                try {
                    jsonArray = response.body().string();
                    countLabels = Integer.parseInt(jsonArray);
//                    if (adapter != null) {
//                        adapter.getAllNum(countLabels);
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
}

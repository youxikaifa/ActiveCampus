package com.thy.activecampus.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.App;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.MsgAdapter;
import com.thy.activecampus.base.BaseF;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.model.Room;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.ui.activity.ChatRoomA_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Jin on 7/29.
 */
@EFragment(R.layout.fragment_main_message)
public class MesageF extends BaseF {

    public final static LabelReqImpl request = LabelReqImpl.getInstance();
//    public static final int MAX_SIZE = Integer.MAX_VALUE;

    @ViewById(R.id.roomInfo)
    TextView roomInfo;
    @ViewById(R.id.lvMsg)
    ListView lvMsg;
    @ViewById(R.id.mSwipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    MsgAdapter adapter;

    List<Room> rooms;
    List<Room> tempRooms = new ArrayList<>();
    public ACache cache;
    public User user;
    public Socket mSocket;
    public IO.Options options;

    public int[] unRead ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < rooms.size(); i++) {
            mSocket.emit(MyConstants.EVENT_LEAVE, rooms.get(i).getLabel_id(), user.get_id(), user.getName());
        }
        mSocket.off(MyConstants.EVENT_CONNECT, onConnect);
        mSocket.off(MyConstants.EVENT_USER_LEAVE, onUserLeave);
        mSocket.off(MyConstants.EVENT_COME, onUserCome);
        mSocket.off(MyConstants.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(MyConstants.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(MyConstants.EVENT_MSG, onNewMessage);
    }

    @AfterViews
    public void initViews() {
        initData();
        initSocket();


//        fetchData();

    }

    private void initSocket() {
        mSocket = ((App) getActivity().getApplication()).getSocket();
        options = new IO.Options();
        options.reconnection = true;
        try {
            // http://localhost:3000/room/room_1
            mSocket = IO.socket(MyConstants.LOCALHOST, options);
            mSocket.on(MyConstants.EVENT_CONNECT, onConnect);
            mSocket.on(MyConstants.EVENT_USER_LEAVE, onUserLeave);
            mSocket.on(MyConstants.EVENT_COME, onUserCome);
            mSocket.on(MyConstants.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(MyConstants.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on(MyConstants.EVENT_MSG, onNewMessage);

            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

//    @Background
//    public void fetchData() {
//        request.getJoinRoom(MyConstants.BASE_URL_ANOTHER_PORT, user.getId(), new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = response.body().string();
//                tempRooms = new Gson().fromJson(json, new TypeToken<ArrayList<Room>>() {
//                }.getType());
//                rooms.clear();
//                rooms.addAll(tempRooms);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });
//    }


    public void initData() {
        rooms = new ArrayList<>();
        cache = ACache.get(getActivity(), MyConstants.USER_INFO);
        user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
        JSONArray jsonArray = cache.getAsJSONArray(MyConstants.ROOM_INTO);
        if (jsonArray != null) {
            tempRooms = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<Room>>() {
            }.getType());
            rooms.addAll(tempRooms);
            roomInfo.setVisibility(View.GONE);
            unRead = new int[rooms.size()];
        } else {
            roomInfo.setVisibility(View.VISIBLE);
        }
        adapter = new MsgAdapter(getActivity(), rooms);
        lvMsg.setAdapter(adapter);
        lvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                unRead[position]=0;
                rooms.get(position).setUnReadNum(0);
                Intent intent = new Intent(getActivity(), ChatRoomA_.class);
                intent.putExtra("label_id", rooms.get(position).getLabel_id());
                intent.putExtra("label_thumb",rooms.get(position).getThumb());
                intent.putExtra("label_title",rooms.get(position).getTitle());
                if (rooms.get(position).getBean()!=null){
                    intent.putExtra("message_id", rooms.get(position).getBean().getMessage_id());
                }
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onReload();
            }
        });

    }

    @Background
    public void onReload() {
        JSONArray jsonArray = cache.getAsJSONArray(MyConstants.ROOM_INTO);
        if (jsonArray != null) {

            tempRooms = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<Room>>() {}.getType());
            rooms.clear();
            rooms.addAll(tempRooms);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    roomInfo.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            unRead = new int[rooms.size()];
        }else{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    roomInfo.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }

    }


    public Emitter.Listener onUserLeave = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), args[0].toString(), Toast.LENGTH_SHORT).show();
                    //收到服务器的信息,有人退出了房间
                }
            });
        }
    };

    public Emitter.Listener onUserCome = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(getActivity(), args[0].toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            for (int i = 0; i < rooms.size(); i++) {
                mSocket.emit(MyConstants.EVENT_JOIN, rooms.get(i).getLabel_id(), user.get_id(), user.getName()); //参数是 房间id，user对象
            }
        }
    };


    public Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "连接错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    public Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
//            Toast.makeText(getActivity(), "new Message", Toast.LENGTH_SHORT).show();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "增加了一条信息", Toast.LENGTH_SHORT).show();
                    String user_id, name, msg, thumb, created,label_id,label_thumb,label_title;
                    user_id = (String) args[0];
                    name = (String) args[1];
                    msg = (String) args[2];
                    thumb = (String) args[3];
                    created = (String) args[4];
                    label_id = (String) args[5];
                    label_thumb = (String) args[6];
                    label_title = (String) args[7];

                    for (int i = 0; i < rooms.size(); i++) {
                        if (rooms.get(i).getLabel_id().equals(label_id)){
                            unRead[i] += 1;
                            rooms.get(i).setUnReadNum(unRead[i]);
                            rooms.get(i).getBean().setCreated(created);
                            rooms.get(i).getBean().setName(name);
                            rooms.get(i).getBean().setThumb(thumb);
                            rooms.get(i).getBean().setMsg(msg);
                            rooms.get(i).getBean().setMessage_id("");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    cache.put(MyConstants.ROOM_INTO,new Gson().toJson(rooms));
                                }
                            });
                        }
                    }
                }
            });
        }
    };


}

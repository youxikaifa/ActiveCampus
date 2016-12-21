package com.thy.activecampus.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thy.activecampus.App;
import com.thy.activecampus.R;
import com.thy.activecampus.adapter.ChatMsgAdapter;
import com.thy.activecampus.base.BaseA;
import com.thy.activecampus.common.ACache;
import com.thy.activecampus.common.MyConstants;
import com.thy.activecampus.listener.KeyboardChangeListener;
import com.thy.activecampus.listener.SendMsgListener;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.Room;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.impl.LabelReqImpl;
import com.thy.activecampus.view.ChatMsgListView;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jin on 7/29.
 */
@EActivity(R.layout.activity_chat_room)
public class ChatRoomA extends BaseA implements Toolbar.OnMenuItemClickListener {

    public LabelReqImpl request = LabelReqImpl.getInstance();
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.lv_chat_msg)
    ChatMsgListView lvChatMsg;
    @ViewById(R.id.btn_send)
    TextView btnSend;
    @ViewById(R.id.iv_add)
    ImageView ivAdd;
    @ViewById(R.id.et_input)
    public EditText etInput;

    public ACache cache;
    public List<Room> tempList = new ArrayList<>();
    public List<Room> chatMsgList;
    public ChatMsgAdapter adapter;

    public Socket mSocket;
    public IO.Options options;
    boolean isConnected = false;
    String name, user_id, msg, thumb;
    String label_id = null, message_id = null,label_thumb=null,label_title=null;

    List<Room> rooms;

    List<Room> localRooms;

    @AfterViews
    public void initViews() {
        mSocket = ((App) getApplication()).getSocket();
        init();
    }


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    public void getLocalData() {
        cache = ACache.get(this, MyConstants.USER_INFO);
        User user = (User) cache.getAsObject(MyConstants.USER_MESSAGE);
//        rooms = (List<Room>) cache.getAsJSONArray(MyConstants.MESSAGE_INFO);
        name = user.getName();
        user_id = user.get_id();
        thumb = user.getThumb();
    }

    private void init() {
        getLocalData();
        chatMsgList = new ArrayList<>();

        toolbar.inflateMenu(R.menu.base_toolbar_menu);
        toolbar.setOnMenuItemClickListener(this);

        lvChatMsg.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        view.requestFocus();
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        adapter = new ChatMsgAdapter(ChatRoomA.this, chatMsgList);
        lvChatMsg.setAdapter(adapter);
        fetchData();

        etInput.addTextChangedListener(new SendMsgListener(btnSend, ivAdd));
        listenerSoftInput(); //全局监听软键盘

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

//    @Subscribe(sticky = true)
//    public void onEventMainThread(LabelM model) {
//        roomid = model.getPubTime();
//    }

    @Background
    public void fetchData() {

        request.getRoomMsg(MyConstants.URL_FOR_CHATROOM, user_id, label_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Toast.makeText(ChatRoomA.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Toast.makeText(ChatRoomA.this, "获取信息成功", Toast.LENGTH_SHORT).show();
                String json = response.body().string();

                if (json!=null) {
                    tempList = new Gson().fromJson(json, new TypeToken<ArrayList<Room>>() {}.getType());
                    chatMsgList.addAll(tempList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            lvChatMsg.setSelection(chatMsgList.size());
                        }
                    });
                    if (tempList.size()>0){
                        takeCache(tempList.get(tempList.size() - 1));
                    }else{
                        Room room = new Room();
                        room.setTitle(label_title);
                        room.setThumb(label_thumb);
                        room.setLabel_id(label_id);
                        room.setUnReadNum(0);
                        takeCache(room);
                    }
                }
            }
        });
    }

    private void takeCache(Room room) {
        boolean isContain = false;
        int position = 0;
        JSONArray jsonArray = cache.getAsJSONArray(MyConstants.ROOM_INTO);
        if (jsonArray != null) {
            localRooms = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<Room>>() {
            }.getType());

            for (int i = 0; i < localRooms.size(); i++) {
                if (localRooms.get(i).getLabel_id().equals(room.getLabel_id())) {
                    isContain = true;
                    position = i;
                }
            }

            if (!isContain) {
                localRooms.add(room);
            } else {
                localRooms.remove(position);
                localRooms.add(position, room);
            }
        } else {
            localRooms = new ArrayList<>();
            localRooms.add(room);
        }

        String json = new Gson().toJson(localRooms);
        cache.put(MyConstants.ROOM_INTO, json);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(this, "Click 0", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "Click 1", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    @Click(R.id.rlBack)
    void back() {
        this.finish();
    }


    public Emitter.Listener onUserLeave = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatRoomA.this, args[0].toString(), Toast.LENGTH_SHORT).show();
//                    mSocket.emit("discoonect", "110", userid, "Jin" + num);
                    //收到服务器的信息,有人退出了房间
                }
            });
        }
    };

    public Emitter.Listener onUserCome = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatRoomA.this, args[0].toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if (!isConnected) {
                mSocket.emit(MyConstants.EVENT_JOIN, label_id, user_id, name); //参数是 房间id，user对象
                isConnected = true;
            }
        }
    };


    public Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatRoomA.this, "连接错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    public Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatRoomA.this, "增加了一条信息", Toast.LENGTH_SHORT).show();
                    String user_id, name, msg, thumb, created, label_id,label_thumb,label_title;
                    user_id = (String) args[0];
                    name = (String) args[1];
                    msg = (String) args[2];
                    thumb = (String) args[3];
                    created = (String) args[4];
                    label_id = (String) args[5];
                    label_thumb = (String) args[6];
                    label_title = (String) args[7];
                    addMessage(user_id, name, msg, thumb, created, label_id,label_thumb,label_title);
                }
            });
        }
    };

    public void addMessage(String _user_id, String name, String msg, String thumb, String created, String label_id,String label_thumb,String label_title) {
        int type = 0;
        if (user_id.equals(_user_id)) {
            type = 1;
        }
        Room room = new Room();
        Room.MessageBean bean = new Room().new MessageBean();
        bean.setType(type);
        bean.setName(name);
        bean.setMsg(msg);
        bean.setThumb(thumb);
        bean.setCreated(created);
        room.setLabel_id(label_id);
        room.setThumb(label_thumb);
        room.setTitle(label_title);
        room.setBean(bean);
        chatMsgList.add(room);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                lvChatMsg.setSelection(chatMsgList.size());
            }
        });

    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Click(R.id.btn_send)
    @Background
    void send() {
        msg = etInput.getText().toString();

        if (mSocket.connected()) {
            mSocket.emit(MyConstants.EVENT_MESSAGE, label_id, user_id, name, msg, thumb,label_thumb,label_title,message_id);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    adapter.notifyDataSetChanged();
                    etInput.setText("");

                }

            });
//            lvChatMsg.setDataChangedListener(listener);
        }

    }

    ChatMsgListView.DataChangeListener listener = new ChatMsgListView.DataChangeListener() {
        @Override
        public void onSucceed() {
            lvChatMsg.setSelection(chatMsgList.size());
        }
    };

    private void listenerSoftInput() {
        new KeyboardChangeListener(this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                lvChatMsg.setSelection(chatMsgList.size());
//                lvChatMsg.setDataChangedListener(listener);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//        aCache = ACache.get(getApplicationContext());

        Intent intent = getIntent();
        label_id = intent.getStringExtra("label_id");
        label_thumb = intent.getStringExtra("label_thumb");
        label_title = intent.getStringExtra("label_title");
        message_id = intent.getStringExtra("message_id");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chatMsgList.clear();
        EventBus.getDefault().unregister(this);
//        mSocket.emit(MyConstants.EVENT_LEAVE, label_id, user_id, name);
//
//        mSocket.off(MyConstants.EVENT_CONNECT, onConnect);
//        mSocket.off(MyConstants.EVENT_USER_LEAVE, onUserLeave);
//        mSocket.off(MyConstants.EVENT_COME, onUserCome);
//        mSocket.off(MyConstants.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.off(MyConstants.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.off(MyConstants.EVENT_MSG, onNewMessage);
    }

}

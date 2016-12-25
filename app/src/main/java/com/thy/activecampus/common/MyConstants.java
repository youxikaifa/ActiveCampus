package com.thy.activecampus.common;

/**
 * Created by Jin on 7/29.
 */

public class MyConstants {
    public static final int REQUEST_CODE_CAPTURE = 2000;

    public static final int FETCH_STARTED = 2001;
    public static final int FETCH_COMPLETED = 2002;
    public static final int ERROR = 2003;

    public static final int MAX_LIMIT = 99;

    public static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 23;
    public static final int PERMISSION_REQUEST_CAMERA = 24;

    public static final String PREF_WRITE_EXTERNAL_STORAGE_REQUESTED = "writeExternalRequested";
    public static final String PREF_CAMERA_REQUESTED = "cameraRequested";
    public static final String APP_ID = "1105758160";

    public static final String IP = "http://121.42.206.117";
    public static final String BASE_URL_ANOTHER_PORT = IP+":2016";
    public static final String LOCALHOST = IP+":2016";
    public static final String URL_FOR_CHATROOM = BASE_URL_ANOTHER_PORT+"/chatroom";

    //聊天
    public static final String EVENT_CONNECT= "connect";
    public static final String EVENT_MESSAGE = "message";
    public static final String EVENT_USER_LEAVE = "userLeave";
    public static final String EVENT_COME = "userCome";
    public static final String EVENT_CONNECT_ERROR = "connect_error";
    public static final String EVENT_CONNECT_TIMEOUT = "connect_timeout";
    public static final String EVENT_JOIN = "join";
    public static final String EVENT_MSG = "msg";
    public static final String EVENT_LEAVE = "leave";

    //本地存储
    public static final String USER_MESSAGE = "user";
    public static final String USER_INFO = "userInfo";
    public static final String LABEL_RECENT_SCAN = "recentscan"; //最近浏览
    public static final String IS_LOGIN= "login";
    public static final String LABEL_NATIVE_CACHE = "nativecache"; //缓存无网状态下显示的一些label
    public static final String ROOM_INTO = "roomInfo"; //存储加入的房间信息
    public static final String MESSAGE_INFO = "messageInfo";//存储每个房间的聊天记录




    //网络请求
    //121.42.206.117
    public static final String BASE_URL = IP+":3000";
    public static final String BASE_URL_FOR_FIND = BASE_URL+"/v1/find";
    public static final String BASE_URL_FOR_LOAD_MORE = BASE_URL+"/v1/loadmore?publishTime=";



}

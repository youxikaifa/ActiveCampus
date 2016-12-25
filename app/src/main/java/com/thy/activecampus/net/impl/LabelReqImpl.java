package com.thy.activecampus.net.impl;


import com.google.gson.Gson;
import com.thy.activecampus.model.AutoComment;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.model.Comment;
import com.thy.activecampus.model.FeelModel;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.Losing;
import com.thy.activecampus.model.User;
import com.thy.activecampus.net.okReq.LabelReq;
import com.thy.activecampus.util.CompressImgUtil;

import java.io.File;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Jin on 7/29.
 */

public class LabelReqImpl implements LabelReq {

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    private static class ChartRequestHolder {
        public static LabelReqImpl instance = new LabelReqImpl();
    }

    public static LabelReqImpl getInstance() {
        return ChartRequestHolder.instance;
    }

    private OkHttpClient client;

    @Override
    public void get(String url, Callback callback) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getJokes(int page, Callback callback) {
        client = new OkHttpClient();
        String url = "http://japi.juhe.cn/joke/content/text.from?key=895196f900ddc118613223c4684c5aa5&page="+page+"&pagesize=10";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getFunPics(int page, Callback callback) {
        client = new OkHttpClient();
        String url = "http://japi.juhe.cn/joke/img/text.from?key=895196f900ddc118613223c4684c5aa5&page="+page+"&pagesize=10";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void post(String url, LabelM model, Callback callback) {

        String realUrl = url + "/v1/upload";
        CompressImgUtil util = new CompressImgUtil();

        client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (int i = 0; i < model.getPicUrls().size(); i++) {
            String smallFilePath = util.getSmallFilePath(model.getPicUrls().get(i));

            File f = new File(smallFilePath);
//            File f = new File(model.getUris().get(i));
            if (f != null) {
                builder.addFormDataPart("images", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }
//        //添加其它信息
        builder.addFormDataPart("userId", model.getUserId());
        builder.addFormDataPart("name", model.getName());
        builder.addFormDataPart("sex", String.valueOf(model.getSex()));
        builder.addFormDataPart("content", model.getContent());
        builder.addFormDataPart("head", model.getHead());
        builder.addFormDataPart("school", model.getSchool());
        builder.addFormDataPart("motto", model.getMotto());
        builder.addFormDataPart("tags", String.valueOf(model.getTags()));
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(realUrl)//地址
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void uploadHead(String url, User user, String imgPath, Callback callback) {
        String realUrl = url + "/uploadhead";
        CompressImgUtil util = new CompressImgUtil();
        client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (imgPath != null) {
            String smallFilePath = util.getSmallFilePath(imgPath);

            File f = new File(smallFilePath);
            if (f != null) {
                builder.addFormDataPart("images", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }

        builder.addFormDataPart("sex", String.valueOf(user.getSex()));
        builder.addFormDataPart("userId", user.get_id());
        builder.addFormDataPart("name", user.getName());
        builder.addFormDataPart("school", user.getSchool());
        builder.addFormDataPart("motto", user.getMotto());
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(realUrl)//地址
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void uploadMsg(String url, User user, Callback callback) {
        String realUrl = url + "/uploadMsg";
        client = new OkHttpClient();
        String json = new Gson().toJson(user);
        RequestBody body = RequestBody.create(JSON, json);

        //构建请求
        Request request = new Request.Builder()
                .url(realUrl)//地址
                .post(body)//添加请求体
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void update(String url, LabelM model, Callback callback) {
        client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(model);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getUserInfo(String url, Callback callback) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void login(String url, User user, Callback callback) {
        String realUrl = url + "/login";
        client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(realUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void loginForThree(String url, User user, Callback callback) {
        String realUrl = url + "/loginForThree";
        client = new OkHttpClient();
        String json = new Gson().toJson(user);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(realUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void register(String url, User user, Callback callback) {
        String realUrl = url + "/register";
        client = new OkHttpClient();
        String json = new Gson().toJson(user);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(realUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getRoomMsg(String url, String user_id, String label_id, Callback callback) {
        client = new OkHttpClient();
        String realUrl = url + "/?user_id=" + user_id + "&label_id=" + label_id;
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void collect(String url,String user_id,String label_id, Callback callback) {
        String realUrl = url+ "/v1/collect?user_id="+user_id+"&label_id="+label_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }


    @Override
    public void like(String url, String user_id,String label_id, Callback callback) {
        String realUrl = url+ "/v1/like?user_id="+user_id+"&label_id="+label_id;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void unLike(String url, String user_id,String label_id, Callback callback) {
        String realUrl = url+ "/v1/unLike?user_id="+user_id+"&label_id="+label_id;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void sendComment(String url, Comment.CommentsBean bean, String labelId, Callback callback) {
        client = new OkHttpClient();
        String realUrl = url + "/v1/sendComment?labelId=" + labelId;
        Gson gson = new Gson();
        String json = gson.toJson(bean);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(realUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getComment(String url, String labelId, Callback callback) {
        client = new OkHttpClient();
        String realUrl = url + "/v1/getComment?labelId=" + labelId;
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void notice(String url, String my_id, String other_id, Callback callback) {
        client = new OkHttpClient();
        String realUrl = url + "/v1/notice?my_id=" + my_id + "&other_id=" + other_id;
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void unnotice(String url, String my_id, String other_id, Callback callback) {
        client = new OkHttpClient();
        String realUrl = url + "/v1/unnotice?my_id=" + my_id + "&other_id=" + other_id;
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void fixUserMsg(String url, String oldThumbPath, User user, Callback callback) {
        client = new OkHttpClient();
        String realUrl = url + "/fixUserMsg";
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (user.getThumb()!=null){
            File f = new File(user.getThumb().substring(7));
            if (f != null) {
                builder.addFormDataPart("image", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }

        if (oldThumbPath==null){
            builder.addFormDataPart("oldThumbPath","");
        }else{
            builder.addFormDataPart("oldThumbPath",oldThumbPath);
        }

        builder.addFormDataPart("id", user.get_id());
        if (user.getName()!=null){
            builder.addFormDataPart("name", user.getName());
        }else{
            builder.addFormDataPart("name", "");
        }
        if (user.getReduce()!=null){
            builder.addFormDataPart("reduce", user.getReduce());
        }else{
            builder.addFormDataPart("reduce", "");
        }

        if (user.getMotto()!=null){
            builder.addFormDataPart("motto", user.getMotto());
        }else{
            builder.addFormDataPart("motto", "");
        }

        if (user.getSchool()!=null){
            builder.addFormDataPart("school", user.getSchool());
        }else{
            builder.addFormDataPart("school", "");
        }
        if (user.getSkill()!=null){
            builder.addFormDataPart("skill", user.getSkill());
        }else{
            builder.addFormDataPart("skill", "");
        }
        if (user.getHabit()!=null){
            builder.addFormDataPart("habit", user.getHabit());
        }else{
            builder.addFormDataPart("habit", "");
        }
        if (user.getContact().getPhone()!=null){
            builder.addFormDataPart("phone", user.getContact().getPhone());
        }else{
            builder.addFormDataPart("phone", "");
        }
        if (user.getContact().getQq()!=null){
            builder.addFormDataPart("qq", user.getContact().getQq());
        }else{
            builder.addFormDataPart("qq", "");
        }
        if (user.getContact().getWeixin()!=null){
            builder.addFormDataPart("weixin", user.getContact().getWeixin());
        }else{
            builder.addFormDataPart("weixin", "");
        }
        if (user.getContact().getWeibo()!=null){
            builder.addFormDataPart("weibo", user.getContact().getWeibo());
        }else{
            builder.addFormDataPart("weibo", "");
        }
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(realUrl)//
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(callback);

    }

    @Override
    public void getJoinRoom(String url, String user_id, Callback callback) {
        String realUrl = url+"/getJoinRoom?user_id="+user_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void joinRoom(String url, String user_id, String label_id, Callback callback) {
        String realUrl = url + "/joinRoom?user_id=" + user_id+"&label_id="+label_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }



    @Override
    public void unCollect(String url, String user_id, String label_id,Callback callback) {
        String realUrl = url+ "/v1/unCollect?user_id="+user_id+"&label_id="+label_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getRoomInfo(String url, String label_id, String message_id, Callback callback) {
        String realUrl = url+"/getRoomInfo?label_id="+label_id+"&message_id="+message_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getMyPub(String url, String user_id, Callback callback) {
        String realUrl = url+"/v1/getMyPub?user_id="+user_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);

    }

    @Override
    public void getUserInfo(String url, String user_id, Callback callback) {
        String realUrl = url+"/getUserInfo?user_id="+user_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void addFriend(String url, String my_id, String other_id, Callback callback) {
        String realUrl = url+"/addFriend?my_id="+my_id+"&other_id="+other_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void unAddFriend(String url, String my_id, String other_id, Callback callback) {
        String realUrl = url+"/addFriend?my_id="+my_id+"&other_id="+other_id;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void search(String url, String key, Callback callback) {
        String realUrl = url+"/v1/search?key="+key;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void searchLost(String url, String key, Callback callback) {
        String realUrl = url+"/v1/searchlost?key="+key;
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void findlost(String url, Losing losing, Callback callback) {
        String realUrl = url + "/v1/findlost";
        CompressImgUtil util = new CompressImgUtil();

        client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (int i = 0; i < losing.getThumbs().size(); i++) {
            String smallFilePath = util.getSmallFilePath(losing.getThumbs().get(i));

            File f = new File(smallFilePath);
//            File f = new File(model.getUris().get(i));
            if (f != null) {
                builder.addFormDataPart("images", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }
//        //添加其它信息
        builder.addFormDataPart("title", losing.getTitle());
        builder.addFormDataPart("type",losing.getType()+"");
        builder.addFormDataPart("name", losing.getName());
        builder.addFormDataPart("contact",losing.getContact());
        builder.addFormDataPart("addr",losing.getAddr());
        builder.addFormDataPart("remark",losing.getRemark());
        builder.addFormDataPart("head",losing.getHead());
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(realUrl)//地址
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getAutoDyne(String url, int curPage, int type, Callback callback) {
        String realUrl = url+"/v1/getAutoDyne?page="+curPage+"&type="+type;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getFeels(String url, int curPage, String school, Callback callback) {
        String realUrl = url+"/v1/getFeels?page="+curPage+"&school="+school;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getLabels(String url, int curPage, int type, Callback callback) {
        String realUrl = url+"/v1/getLabels?page="+curPage+"&type="+type;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void getAutoComments(String url, String auto_id, Callback callback) {
        String realUrl = url+"/v1/getAutoComments?auto_id="+auto_id;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void commitAutoComment(String url, AutoComment comment, Callback callback) {
        String realUrl = url + "/v1/commitAutoComment";
        client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(comment));
        Request request = new Request.Builder()
                .url(realUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void clickAutoLike(String url, String auto_id,String user_id, Callback callback) {
        String realUrl = url+"/v1/clickAutoLike?auto_id="+auto_id+"&user_id="+user_id;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void clickFeelLike(String url, String feel_id, String thumb, Callback callback) {
        String realUrl = url+"/v1/clickFeelLike?feel_id="+feel_id+"&thumb="+thumb;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void joinScans(String url, String auto_id, Callback callback) {
        String realUrl = url+"/v1/clickAutoLike?auto_id="+auto_id;
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void pubAutoDyne(String url, AutoDyne autoDyne, Callback callback) {
        String realUrl = url + "/v1/pubAutoDyne";
        CompressImgUtil util = new CompressImgUtil();

        client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (int i = 0; i < autoDyne.getPics().size(); i++) {
            String smallFilePath = util.getSmallFilePath(autoDyne.getPics().get(i));

            File f = new File(smallFilePath);
//            File f = new File(model.getUris().get(i));
            if (f != null) {
                builder.addFormDataPart("images", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }
//        //添加其它信息
        builder.addFormDataPart("title", autoDyne.getTitle());
        builder.addFormDataPart("type",autoDyne.getType()+"");
        builder.addFormDataPart("name", autoDyne.getName());
        builder.addFormDataPart("userId",autoDyne.getUserId());
        builder.addFormDataPart("head",autoDyne.getHead());
        builder.addFormDataPart("motto",autoDyne.getMotto());
        builder.addFormDataPart("school",autoDyne.getSchool());
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(realUrl)//地址
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void pubFeeling(String url, FeelModel model, Callback callback) {
        String realUrl = url + "/v1/pubFeeling";
        CompressImgUtil util = new CompressImgUtil();

        client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (int i = 0; i < model.getPics().size(); i++) {
            String smallFilePath = util.getSmallFilePath(model.getPics().get(i));

            File f = new File(smallFilePath);
            if (f != null) {
                builder.addFormDataPart("images", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }
//        //添加其它信息
        builder.addFormDataPart("content", model.getContent());
        builder.addFormDataPart("type",model.getType()+"");
        builder.addFormDataPart("name", model.getName());
        builder.addFormDataPart("userId",model.getUserId());
        builder.addFormDataPart("head",model.getHead());
        builder.addFormDataPart("sex",model.getSex()+"");
        builder.addFormDataPart("motto",model.getMotto());
        builder.addFormDataPart("school",model.getSchool());
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(realUrl)//地址
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(callback);
    }


}

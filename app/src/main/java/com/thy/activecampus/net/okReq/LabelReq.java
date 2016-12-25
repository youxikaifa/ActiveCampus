package com.thy.activecampus.net.okReq;

import com.thy.activecampus.model.AutoComment;
import com.thy.activecampus.model.AutoDyne;
import com.thy.activecampus.model.Comment;
import com.thy.activecampus.model.FeelModel;
import com.thy.activecampus.model.LabelM;
import com.thy.activecampus.model.Losing;
import com.thy.activecampus.model.User;

import okhttp3.Callback;

/**
 * Created by Jin on 7/29.
 */

public interface LabelReq {
    void get(String url, Callback callback);
    void getJokes(int page,Callback callback);
    void getFunPics(int page,Callback callback);

    void post(String url, LabelM model, Callback callback);
    void uploadHead(String url,User user, String imgPath,Callback callback);

    void uploadMsg(String url,User user,Callback callback);

    void update(String url, LabelM model, Callback callback);

    void getUserInfo(String url, Callback callback);

    void login(String url, User user, Callback callback);

    void loginForThree(String url,User user,Callback callback);

    void register(String url, User user, Callback callback);

    void getRoomMsg(String url,String user_id,String label_id,Callback callback);

    void collect(String url,String user_id,String label_id,Callback callback);
    void unCollect(String url,String user_id,String label_id,Callback callback);
    void like(String url,String user_id,String label_id,Callback callback);
    void unLike(String url,String user_id,String label_id,Callback callback);
    void sendComment(String url, Comment.CommentsBean bean,String labelId,Callback callback);
    void getComment(String url,String labelId,Callback callback);

    /**
     * 加关注
     * @param url
     * @param my_id 主动加的用户id
     * @param other_id 被动加的用户id
     * @param callback
     */
    void notice(String url,String my_id,String other_id,Callback callback);
    void unnotice(String url,String id,String userId,Callback callback);

    void fixUserMsg(String url,String oldThumbPath,User user,Callback callback);
    void getJoinRoom(String url,String user_id,Callback callback);
    void joinRoom(String url,String user_id,String label_id,Callback callback);

    void getRoomInfo(String url,String label_id,String message_id,Callback callback);


    void getMyPub(String url,String user_id,Callback callback);

    void getUserInfo(String url,String user_id,Callback callback);

    void addFriend(String url,String my_id,String other_id,Callback callback);
    void unAddFriend(String url,String my_id,String other_id,Callback callback);
    void search(String url,String key,Callback callback);
    void searchLost(String url,String key,Callback callback);
    void findlost(String url, Losing losing,Callback callback);

    void getAutoDyne(String url,int curPage,int type,Callback callback);
    void getFeels(String url,int curPage,String school,Callback callback);
    void getLabels(String url,int curPage,int type ,Callback callback);

    void getAutoComments(String url,String auto_id,Callback callback);

    void commitAutoComment(String url, AutoComment comment, Callback callback);

    void clickAutoLike(String url,String auto_id,String user_id,Callback callback);
    void clickFeelLike(String url,String feel_id,String thumb,Callback callback);

    void joinScans(String url,String auto_id,Callback callback);

    void pubAutoDyne(String url, AutoDyne autoDyne,Callback callback);
    void pubFeeling(String url, FeelModel model,Callback callback);
}

package com.thy.activecampus.model;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Jin on 7/29.
 */

public class AutoDyne implements Serializable{


    /**
     * _id : 5859a29726994f1de479b9b7
     * userId : 5856229c4fcc9a2df0175b12
     * head : https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1481873251&di=a2b0ac9445d51eb936c5b583f974f934&src=http://img4.duitang.com/uploads/item/201406/28/20140628141104_PXLRN.thumb.700_0.jpeg
     * pics : ["https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1481873251&di=a2b0ac9445d51eb936c5b583f974f934&src=http://img4.duitang.com/uploads/item/201406/28/20140628141104_PXLRN.thumb.700_0.jpeg","/images/thumbnail/j166ZKRP2jR-ir0gIc6e1fXe.jpg"]
     * name : 小叮当
     * time : 1482269335785
     * type : 2
     * title : 七月与安生
     * likes : []
     * comments : 0
     * scans : 234
     */

    private String _id;
    private String userId;
    private String head;//
    private String name;//
    private String time;//
    private int type;//
    private String title;//
    private int comments;//
    private int scans; //
    private List<String> pics;//
    private List<?> likes; //

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getScans() {
        return scans;
    }

    public void setScans(int scans) {
        this.scans = scans;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<?> getLikes() {
        return likes;
    }

    public void setLikes(List<?> likes) {
        this.likes = likes;
    }
}

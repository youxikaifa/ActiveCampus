package com.thy.activecampus.model;

import java.io.Serializable;
import java.util.List;



/**
 * Created by Jin on 7/29.
 */

public class FeelModel implements Serializable {

    /**
     * _id : 585bb8633b46cddd886b9fbf
     * head : http
     * sex : 0
     * name : 爵士避雷
     * motto : 永远相信最美好的就在明天
     * content : 七月与安生
     * pics : ["http","http"]
     * school : 江西财经大学
     * time : 1482405987891
     * likes : ["http","http"]
     * comments : 0
     * type : 0
     */

    private String _id;
    private String head;
    private int sex;
    private String name;
    private String motto;
    private String content;
    private String school;
    private String time;
    private String userId;
    private int comments;
    private int type;
    private List<String> pics;
    private List<String> likes;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String get_id() {
        return _id;
    }

    public String getHead() {
        return head;
    }

    public int getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }

    public String getMotto() {
        return motto;
    }

    public String getContent() {
        return content;
    }

    public String getSchool() {
        return school;
    }

    public String getTime() {
        return time;
    }

    public int getComments() {
        return comments;
    }

    public int getType() {
        return type;
    }

    public List<String> getPics() {
        return pics;
    }

    public List<String> getLikes() {
        return likes;
    }
}

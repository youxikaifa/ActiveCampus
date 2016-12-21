package com.thy.activecampus.model;

import java.io.Serializable;
import java.util.List;



/**
 * Created by Jin on 7/29.
 */

public class Losing implements Serializable {


    /**
     * _id : 584d3e44d7bd2208580c6b0e
     * thumbs : ["http://","http://"]
     * head : http://
     * title : 校园卡
     * name : 小菜
     * contact : 18702605053
     * addr : 食堂
     * time : 1481457220914
     * type : 0
     * remark :
     */

    private String _id;
    private String head;
    private String title;
    private String name;
    private String contact;
    private String addr;
    private String time;
    private int type;
    private String school;
    private String remark;
    private List<String> thumbs;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setThumbs(List<String> thumbs) {
        this.thumbs = thumbs;
    }

    public String get_id() {
        return _id;
    }

    public String getHead() {
        return head;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getAddr() {
        return addr;
    }

    public String getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public List<String> getThumbs() {
        return thumbs;
    }
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}

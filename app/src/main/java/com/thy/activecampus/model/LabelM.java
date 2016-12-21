package com.thy.activecampus.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class LabelM implements Serializable{


    /**
     * _id : 582981320f878f3e48d490a7
     * id : dfafwerqrfgfwfsd
     * userId : user_fdafsfasfasdffasd
     * head : https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1472471227&di=c0a14b2b4ba380f91fea040f2191fb6e&src=http://images.17173.com/2012/news/2012/07/02/gxy0702dp05s.jpg
     * sex : 0
     * name : 飘逸的风
     * title : 组团打个撸
     * content : 今晚10点,不见不散
     * pubTime : 23:10
     * tags : [1,3]
     * type : 0
     * comments : [{"id":"eqwerqerqer","head":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2564104581,2258051991&fm=80&w=179&h=119&img.JPEG","name":"Jin","sex":0,"commentContent":"不错,晚上搞起来","pubTime":"12:31"},{"id":"fgsdfgretgsdfg","head":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2564104581,2258051991&fm=80&w=179&h=119&img.JPEG","name":"Yio","sex":0,"commentContent":"不错","pubTime":"12:33"},{"id":"fsdafweqrfasdfas","head":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2564104581,2258051991&fm=80&w=179&h=119&img.JPEG","name":"Lin","sex":1,"commentContent":"不错,晚上搞起来","pubTime":"12:35"}]
     * follow : ["dsafsdaf","fdasfasdf"]
     * like : ["fadsfqwrfdfadsf","fdasfaffadsfgag"]
     * picUrls : ["https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2564104581,2258051991&fm=80&w=179&h=119&img.JPEG","https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2564104581,2258051991&fm=80&w=179&h=119&img.JPEG"]
     */

    private String _id;
    private String userId;
    private String head;
    private int sex;
    private String name;
    private String title;
    private String content;
    private String pubTime;
    private int type;
    private List<String> tags;
    private List<CommentsBean> comments;
    private List<String> followers;
    private List<String> likes;
    private List<String> picUrls;

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }

    public static class CommentsBean implements Serializable{
        /**
         * id : eqwerqerqer
         * head : https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2564104581,2258051991&fm=80&w=179&h=119&img.JPEG
         * name : Jin
         * sex : 0
         * commentContent : 不错,晚上搞起来
         * pubTime : 12:31
         */

        private String id;
        private String head;
        private String name;
        private int sex;
        private String commentContent;
        private String pubTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }
    }

    
}


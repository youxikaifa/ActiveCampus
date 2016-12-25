package com.thy.activecampus.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class User implements Serializable {




    /**
     * _id : a3ec9e97d4fce3c1ffa72b6bed171108
     * name : 无极剑圣
     * pwd : cjcjcncncjvk
     * token : f19954dbb533d6562bd4ea816553349d
     * expires : 1479713128944
     * openid : e70be92ab0767110c6bf810e16e04e1d
     * created : 1479713128944
     * thumb : /images/head/94xLFYXezqalNAMi9B1Ks758.jpg
     * followed : ["sdadsasda"]
     * friend : [""]
     * sex : 0
     * fans : [""]
     * reduce :
     * following : [""]
     * collections : [""]
     * school :
     * skill :
     * habit :
     * contact : {"phone":"","qq":"","weixin":"","weibo":""}
     */

    private String _id;
    private String name;
    private String account;
    private String pwd;
    private String token;
    private String expires;
    private String openid;
    private String created;
    private String thumb;
    private int sex;
    private String reduce;
    private String school;
    private String skill;
    private String habit;
    private String motto;
    private ContactBean contact;
    private List<String> followed;
    private List<String> friend;
    private List<String> fans;
    private List<String> following;
    private List<String> collections;

    public User(){

    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getReduce() {
        return reduce;
    }

    public void setReduce(String reduce) {
        this.reduce = reduce;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public List<String> getFollowed() {
        return followed;
    }

    public void setFollowed(List<String> followed) {
        this.followed = followed;
    }

    public List<String> getFriend() {
        return friend;
    }

    public void setFriend(List<String> friend) {
        this.friend = friend;
    }

    public List<String> getFans() {
        return fans;
    }

    public void setFans(List<String> fans) {
        this.fans = fans;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public List<String> getCollections() {
        return collections;
    }

    public void setCollections(List<String> collections) {
        this.collections = collections;
    }

    public  class ContactBean implements Serializable {
        /**
         * phone :
         * qq :
         * weixin :
         * weibo :
         */

        public String phone;
        public String qq;
        public String weixin;
        public String weibo;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getWeibo() {
            return weibo;
        }

        public void setWeibo(String weibo) {
            this.weibo = weibo;
        }
    }
}

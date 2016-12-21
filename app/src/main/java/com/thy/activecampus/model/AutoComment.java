package com.thy.activecampus.model;


import java.io.Serializable;

/**
 * Created by Jin on 7/29.
 */

public class AutoComment implements Serializable{

    /**
     * _id : 58564e4424705a24d42301e8
     * from_user : {"_id":"dsfasd","thumb":"dsafa"}
     * target_user : {"_id":"dsfasd","thumb":"dsafa"}
     * repid : fasffsa
     * autoid : fasfasf
     * type : 0
     * content : hah
     * created : 1482051140780
     */

    private String _id;
    private FromUserBean from_user;
    private TargetUserBean target_user;
    private String repid;
    private String autoid;
    private int type;
    private String content;
    private String created;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setFrom_user(FromUserBean from_user) {
        this.from_user = from_user;
    }

    public void setTarget_user(TargetUserBean target_user) {
        this.target_user = target_user;
    }

    public void setRepid(String repid) {
        this.repid = repid;
    }

    public void setAutoid(String autoid) {
        this.autoid = autoid;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String get_id() {
        return _id;
    }

    public FromUserBean getFrom_user() {
        return from_user;
    }

    public TargetUserBean getTarget_user() {
        return target_user;
    }

    public String getRepid() {
        return repid;
    }

    public String getAutoid() {
        return autoid;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }

    public static class FromUserBean implements Serializable{
        /**
         * _id : dsfasd
         * thumb : dsafa
         */

        private String _id;
        private String thumb;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public void set_id(String _id) {
            this._id = _id;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String get_id() {
            return _id;
        }

        public String getThumb() {
            return thumb;
        }
    }

    public static class TargetUserBean implements Serializable{
        /**
         * _id : dsfasd
         * thumb : dsafa
         */

        private String _id;
        private String thumb;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public void set_id(String _id) {
            this._id = _id;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String get_id() {
            return _id;
        }

        public String getThumb() {
            return thumb;
        }
    }
}

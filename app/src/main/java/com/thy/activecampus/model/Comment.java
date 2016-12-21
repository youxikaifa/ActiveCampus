package com.thy.activecampus.model;

import java.util.List;

/**
 * Created by Jin on 7/29.
 */

public class Comment {

    /**
     * _id : 582c23dbef1e8f363444a569
     * labelId : ewqrwerqwerfdasf
     * comments : [{"userId":"fasdwewrfa","head":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1814145627,3378814656&fm=80&w=179&h=119&img.PNG","name":"推车的老汉","content":"推车如泥水行舟，不进则退","pubTime":"1479287771709","sex":0}]
     */

    private String _id;
    private String labelId;
    private List<CommentsBean> comment;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comment = comments;
    }

    public String get_id() {
        return _id;
    }

    public String getLabelId() {
        return labelId;
    }

    public List<CommentsBean> getComments() {
        return comment;
    }

    public static class CommentsBean {
        /**
         * userId : fasdwewrfa
         * head : https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1814145627,3378814656&fm=80&w=179&h=119&img.PNG
         * name : 推车的老汉
         * content : 推车如泥水行舟，不进则退
         * pubTime : 1479287771709
         * sex : 0
         */

        private String userId;
        private String head;
        private String name;
        private String content;
        private String pubTime;
        private int sex;

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getUserId() {
            return userId;
        }

        public String getHead() {
            return head;
        }

        public String getName() {
            return name;
        }

        public String getContent() {
            return content;
        }

        public String getPubTime() {
            return pubTime;
        }

        public int getSex() {
            return sex;
        }
    }
}

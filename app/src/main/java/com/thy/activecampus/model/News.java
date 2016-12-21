package com.thy.activecampus.model;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Jin on 7/29.
 */

public class News implements Serializable {


    private String reason;
    private ResultBean result;
    private int error_code;

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public int getError_code() {
        return error_code;
    }

    public static class ResultBean {


        private String stat;
        private List<DataBean> data;

        public void setStat(String stat) {
            this.stat = stat;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public String getStat() {
            return stat;
        }

        public List<DataBean> getData() {
            return data;
        }

        public static class DataBean {
            /**
             * title : 东方八卦：杨幂祝唐嫣罗晋“三年抱俩”，柳岩坐挖掘机秀性感身材，2016环球夫人总决赛群芳争艳
             * date : 2016-12-13 11:56
             * author_name : 东方头条
             * thumbnail_pic_s : http://03.imgmini.eastday.com/mobile/20161213/20161213115612_3d49c50c111724285821f43351279f3f_1_mwpm_03200403.png
             * thumbnail_pic_s02 : http://03.imgmini.eastday.com/mobile/20161213/20161213115612_3d49c50c111724285821f43351279f3f_1_mwpl_05500201.png
             * thumbnail_pic_s03 : http://03.imgmini.eastday.com/mobile/20161213/20161213115612_3d49c50c111724285821f43351279f3f_1_mwpl_05500201.png
             * url : http://mini.eastday.com/mobile/161213115612545.html?qid=juheshuju
             * uniquekey : 161213115612545
             * type : 头条
             * realtype : 娱乐
             */

            private String title;
            private String date;
            private String author_name;
            private String thumbnail_pic_s;
            private String thumbnail_pic_s02;
            private String thumbnail_pic_s03;
            private String url;
            private String uniquekey;
            private String type;
            private String realtype;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
                this.thumbnail_pic_s02 = thumbnail_pic_s02;
            }

            public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
                this.thumbnail_pic_s03 = thumbnail_pic_s03;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setRealtype(String realtype) {
                this.realtype = realtype;
            }

            public String getTitle() {
                return title;
            }

            public String getDate() {
                return date;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public String getThumbnail_pic_s02() {
                return thumbnail_pic_s02;
            }

            public String getThumbnail_pic_s03() {
                return thumbnail_pic_s03;
            }

            public String getUrl() {
                return url;
            }

            public String getUniquekey() {
                return uniquekey;
            }

            public String getType() {
                return type;
            }

            public String getRealtype() {
                return realtype;
            }
        }
    }
}

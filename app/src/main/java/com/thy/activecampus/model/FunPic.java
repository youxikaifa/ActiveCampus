package com.thy.activecampus.model;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Jin on 7/29.
 */

public class FunPic implements Serializable {
    /**
     * error_code : 0
     * reason : Success
     * result : {"data":[{"content":"看看我们公司的御前带刀程序员！","hashId":"C30F8D9B9A39B0F4A205986EAB41ABDD","unixtime":1481616310,"updatetime":"2016-12-13 16:05:10","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/C30F8D9B9A39B0F4A205986EAB41ABDD.gif"},{"content":"蛇精病","hashId":"E016A6D672CC3DB4BB6F979A8C8D7016","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/E016A6D672CC3DB4BB6F979A8C8D7016.gif"},{"content":"广电局的想法还真多","hashId":"D12A9C35FCC56B1E85F4E03DC6D63C77","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/D12A9C35FCC56B1E85F4E03DC6D63C77.jpg"},{"content":"这张图的解说是：給大象按摩前列腺，我怎么就不大信呢","hashId":"5F2865E74574EB6F12488E5B2F621D99","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/5F2865E74574EB6F12488E5B2F621D99.jpg"},{"content":"北京人在纽约，说啥都带儿化音","hashId":"F03D9B24F561102B59D3537BAEF3DA25","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/F03D9B24F561102B59D3537BAEF3DA25.jpg"},{"content":"美剧和韩剧不同的接吻方式，哈哈太形象了","hashId":"F792F4052478458A6515140A8E238C89","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/F792F4052478458A6515140A8E238C89.gif"},{"content":"吃得苦中苦，方为人上人啊","hashId":"4515A57434845A3A51660D6F11451FB0","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/4515A57434845A3A51660D6F11451FB0.jpg"},{"content":"我还不如他们玩得好","hashId":"54B75CF2C028D256EFC7C8B29C3DA8DD","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/54B75CF2C028D256EFC7C8B29C3DA8DD.gif"},{"content":"明目张胆","hashId":"B2115D247D5789F6FAE483A9D1FA3DB0","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/B2115D247D5789F6FAE483A9D1FA3DB0.jpg"},{"content":"怎样测试一个贱友是不是GAY","hashId":"E3388004CFB47D9DF44FB766A9EFE188","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/E3388004CFB47D9DF44FB766A9EFE188.gif"}]}
     */

    private int error_code;
    private String reason;
    private ResultBean result;

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public String getReason() {
        return reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public static class ResultBean {
        /**
         * data : [{"content":"看看我们公司的御前带刀程序员！","hashId":"C30F8D9B9A39B0F4A205986EAB41ABDD","unixtime":1481616310,"updatetime":"2016-12-13 16:05:10","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/C30F8D9B9A39B0F4A205986EAB41ABDD.gif"},{"content":"蛇精病","hashId":"E016A6D672CC3DB4BB6F979A8C8D7016","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/E016A6D672CC3DB4BB6F979A8C8D7016.gif"},{"content":"广电局的想法还真多","hashId":"D12A9C35FCC56B1E85F4E03DC6D63C77","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/D12A9C35FCC56B1E85F4E03DC6D63C77.jpg"},{"content":"这张图的解说是：給大象按摩前列腺，我怎么就不大信呢","hashId":"5F2865E74574EB6F12488E5B2F621D99","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/5F2865E74574EB6F12488E5B2F621D99.jpg"},{"content":"北京人在纽约，说啥都带儿化音","hashId":"F03D9B24F561102B59D3537BAEF3DA25","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/F03D9B24F561102B59D3537BAEF3DA25.jpg"},{"content":"美剧和韩剧不同的接吻方式，哈哈太形象了","hashId":"F792F4052478458A6515140A8E238C89","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/F792F4052478458A6515140A8E238C89.gif"},{"content":"吃得苦中苦，方为人上人啊","hashId":"4515A57434845A3A51660D6F11451FB0","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/4515A57434845A3A51660D6F11451FB0.jpg"},{"content":"我还不如他们玩得好","hashId":"54B75CF2C028D256EFC7C8B29C3DA8DD","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/54B75CF2C028D256EFC7C8B29C3DA8DD.gif"},{"content":"明目张胆","hashId":"B2115D247D5789F6FAE483A9D1FA3DB0","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/B2115D247D5789F6FAE483A9D1FA3DB0.jpg"},{"content":"怎样测试一个贱友是不是GAY","hashId":"E3388004CFB47D9DF44FB766A9EFE188","unixtime":1481609109,"updatetime":"2016-12-13 14:05:09","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/E3388004CFB47D9DF44FB766A9EFE188.gif"}]
         */

        private List<DataBean> data;

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public List<DataBean> getData() {
            return data;
        }

        public static class DataBean {
            /**
             * content : 看看我们公司的御前带刀程序员！
             * hashId : C30F8D9B9A39B0F4A205986EAB41ABDD
             * unixtime : 1481616310
             * updatetime : 2016-12-13 16:05:10
             * url : http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201612/13/C30F8D9B9A39B0F4A205986EAB41ABDD.gif
             */

            private String content;
            private String hashId;
            private int unixtime;
            private String updatetime;
            private String url;

            public void setContent(String content) {
                this.content = content;
            }

            public void setHashId(String hashId) {
                this.hashId = hashId;
            }

            public void setUnixtime(int unixtime) {
                this.unixtime = unixtime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getContent() {
                return content;
            }

            public String getHashId() {
                return hashId;
            }

            public int getUnixtime() {
                return unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public String getUrl() {
                return url;
            }
        }
    }
}

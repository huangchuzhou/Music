package com.zdxh.music.bean;

/**
 * Created by huangchuzhou on 2016/4/16.
 */
public class ImageBean {


    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    /**
     * singerPic : http://pic.ttpod.cn/b3b6a9a85118ea3affa0dd13bf5d9f7e.jpg
     */
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private String singerName;

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String singerPic;

        public String getSingerPic() {
            return singerPic;
        }

        public void setSingerPic(String singerPic) {
            this.singerPic = singerPic;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "singerPic='" + singerPic + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "data=" + data +
                '}';
    }
}

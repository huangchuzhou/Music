package com.zdxh.music.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/16.
 */
public class EntityBean implements Serializable{

    /**
     * vip : 0
     * song_id : 28850467
     * song_name : 匆匆那年
     * singer_id : 1765796
     * singer_name : 王菲
     * artist_flag : 1
     * album_id : 1181876
     * album_name : 匆匆那年
     * pick_count : 744165
     * audition_list : [{"duration":"04:01","suffix":"m4a","bitRate":32,"typeDescription":"流畅品质","url":"http://om32.alicdn.com/177/2177/1715068189/1773679434_15965850_l.m4a?auth_key=322b1f28d4a0ce07ded87ca8ddd9149c-1460851200-0-null","size":"0.95 MB","type":0},{"duration":"04:01","suffix":"mp3","bitRate":128,"typeDescription":"标准品质","url":"http://m5.file.xiami.com/177/2177/1715068189/1773679434_15965850_l.mp3?auth_key=3a084aa6be44be039c227b42c75c3996-1460851200-0-null","size":"3.68 MB","type":0},{"duration":"04:01","suffix":"mp3","bitRate":320,"typeDescription":"超高品质","url":"http://m6.file.xiami.com/177/2177/1715068189/1773679434_15965850_h.mp3?auth_key=2122953c036ffe5955d5f581d06d4c72-1460851200-0-null","size":"9.20 MB","type":0}]
     * url_list : [{"duration":"04:01","suffix":"m4a","bitRate":32,"typeDescription":"流畅品质","url":"http://om32.alicdn.com/177/2177/1715068189/1773679434_15965850_l.m4a?auth_key=322b1f28d4a0ce07ded87ca8ddd9149c-1460851200-0-null","size":"0.95 MB","type":0},{"duration":"04:01","suffix":"mp3","bitRate":128,"typeDescription":"标准品质","url":"http://m5.file.xiami.com/177/2177/1715068189/1773679434_15965850_l.mp3?auth_key=3a084aa6be44be039c227b42c75c3996-1460851200-0-null","size":"3.68 MB","type":0},{"duration":"04:01","suffix":"mp3","bitRate":320,"typeDescription":"超高品质","url":"http://m6.file.xiami.com/177/2177/1715068189/1773679434_15965850_h.mp3?auth_key=2122953c036ffe5955d5f581d06d4c72-1460851200-0-null","size":"9.20 MB","type":0}]
     * ll_list : [{"duration":"04:00","suffix":"flac","bitRate":804,"typeDescription":"无损品质","url":"http://om7.alicdn.com/177/2177/1715068189/1773679434_46975818_h.flac?auth_key=dbc017ee94369493859b0e1cb27c0933-1460851200-0-null","size":"23.10 MB","type":0}]
     * mv_list : [{"id":0,"duration":"04:08","suffix":"mp4","bitrate":500,"type_description":"标清","url":"http://otmv.alicdn.com/new/mv_1_5/95/0a/95206c90a7724a14afd63c801664120a.mp4?k=d9840cec54132901&t=1461218461","size":"16.98 MB","type":0,"durationMilliSecond":248550,"pic_url":"http://3p.pic.ttdtweb.com/3p.ttpod.com/video/mv_pic/mv_pic_5/160_90/2334/85048/595338.jpg","videoId":595338,"typeDescription":"标清","picUrl":"http://3p.pic.ttdtweb.com/3p.ttpod.com/video/mv_pic/mv_pic_5/160_90/2334/85048/595338.jpg","lsize":17803352},{"id":0,"duration":"04:08","suffix":"mp4","bitrate":750,"type_description":"720P","url":"http://otmv.alicdn.com/new/mv_1_5/2f/23/2f15f15b953f7e0bc8de74f16fcaa123.mp4?k=69e604ef5d5345f7&t=1461218461","size":"35.39 MB","type":1,"durationMilliSecond":248550,"pic_url":"http://3p.pic.ttdtweb.com/3p.ttpod.com/video/mv_pic/mv_pic_5/160_90/2334/85048/595338.jpg","videoId":595338,"typeDescription":"720P","picUrl":"http://3p.pic.ttdtweb.com/3p.ttpod.com/video/mv_pic/mv_pic_5/160_90/2334/85048/595338.jpg","lsize":37104563}]
     */

    private String song;

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private int song_id;
        private String song_name;
        private int singer_id;
        private String singer_name;
        private int album_id;
        private String album_name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DataBean dataBean = (DataBean) o;

            if (song_name != null ? !song_name.equals(dataBean.song_name) : dataBean.song_name != null)
                return false;
            return singer_name != null ? singer_name.equals(dataBean.singer_name) : dataBean.singer_name == null;

        }

        @Override
        public int hashCode() {
            int result = song_name != null ? song_name.hashCode() : 0;
            result = 31 * result + (singer_name != null ? singer_name.hashCode() : 0);
            return result;
        }

        /**
         * duration : 04:01
         * suffix : m4a
         * bitRate : 32
         * typeDescription : 流畅品质
         * url : http://om32.alicdn.com/177/2177/1715068189/1773679434_15965850_l.m4a?auth_key=322b1f28d4a0ce07ded87ca8ddd9149c-1460851200-0-null
         * size : 0.95 MB
         * type : 0
         */

        private List<AuditionListBean> audition_list;

        public int getSong_id() {
            return song_id;
        }

        public void setSong_id(int song_id) {
            this.song_id = song_id;
        }

        public String getSong_name() {
            return song_name;
        }

        public void setSong_name(String song_name) {
            this.song_name = song_name;
        }

        public int getSinger_id() {
            return singer_id;
        }

        public void setSinger_id(int singer_id) {
            this.singer_id = singer_id;
        }

        public String getSinger_name() {
            return singer_name;
        }

        public void setSinger_name(String singer_name) {
            this.singer_name = singer_name;
        }

        public int getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(int album_id) {
            this.album_id = album_id;
        }

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public List<AuditionListBean> getAudition_list() {
            return audition_list;
        }

        public void setAudition_list(List<AuditionListBean> audition_list) {
            this.audition_list = audition_list;
        }

        public static class AuditionListBean implements Serializable{
            private String duration;
            private String typeDescription;
            private String url;
            private String size;

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getTypeDescription() {
                return typeDescription;
            }

            public void setTypeDescription(String typeDescription) {
                this.typeDescription = typeDescription;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            @Override
            public String toString() {
                return "AuditionListBean{" +
                        "duration='" + duration + '\'' +
                        ", typeDescription='" + typeDescription + '\'' +
                        ", url='" + url + '\'' +
                        ", size='" + size + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "song_id=" + song_id +
                    ", song_name='" + song_name + '\'' +
                    ", singer_id=" + singer_id +
                    ", singer_name='" + singer_name + '\'' +
                    ", album_id=" + album_id +
                    ", album_name='" + album_name + '\'' +
                    ", audition_list=" + audition_list +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EntityBean{" +
                "data=" + data +
                '}';
    }
}

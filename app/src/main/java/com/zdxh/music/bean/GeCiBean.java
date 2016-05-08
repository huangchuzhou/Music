package com.zdxh.music.bean;

import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/15.
 * 请求url http://geci.me/api/lyric/ SongName/ Artist
 * 根据歌词名和歌手名获取歌词
 */
public class GeCiBean {


    /**
     * count : 15
     * code : 0
     * result : [{"aid":2848529,"lrc":"http://s.geci.me/lrc/344/34435/3443588.lrc","song":"海阔天空","artist_id":2,"sid":3443588},{"aid":2346662,"lrc":"http://s.geci.me/lrc/274/27442/2744281.lrc","song":"海阔天空","artist_id":2396,"sid":2744281},{"aid":1889264,"lrc":"http://s.geci.me/lrc/210/21070/2107014.lrc","song":"海阔天空","artist_id":8715,"sid":2107014},{"aid":2075717,"lrc":"http://s.geci.me/lrc/236/23651/2365157.lrc","song":"海阔天空","artist_id":8715,"sid":2365157},{"aid":1563419,"lrc":"http://s.geci.me/lrc/166/16685/1668536.lrc","song":"海阔天空","artist_id":9208,"sid":1668536},{"aid":1567586,"lrc":"http://s.geci.me/lrc/167/16739/1673997.lrc","song":"海阔天空","artist_id":9208,"sid":1673997},{"aid":1571906,"lrc":"http://s.geci.me/lrc/167/16796/1679605.lrc","song":"海阔天空","artist_id":9208,"sid":1679605},{"aid":1573814,"lrc":"http://s.geci.me/lrc/168/16819/1681961.lrc","song":"海阔天空","artist_id":9208,"sid":1681961},{"aid":1656038,"lrc":"http://s.geci.me/lrc/179/17907/1790768.lrc","song":"海阔天空","artist_id":9208,"sid":1790768},{"aid":1718741,"lrc":"http://s.geci.me/lrc/187/18757/1875769.lrc","song":"海阔天空","artist_id":9208,"sid":1875769},{"aid":2003267,"lrc":"http://s.geci.me/lrc/226/22642/2264296.lrc","song":"海阔天空","artist_id":9208,"sid":2264296},{"aid":2020610,"lrc":"http://s.geci.me/lrc/228/22889/2288967.lrc","song":"海阔天空","artist_id":9208,"sid":2288967},{"aid":2051678,"lrc":"http://s.geci.me/lrc/233/23323/2332322.lrc","song":"海阔天空","artist_id":9208,"sid":2332322},{"aid":2412704,"lrc":"http://s.geci.me/lrc/283/28376/2837689.lrc","song":"海阔天空","artist_id":9208,"sid":2837689},{"aid":2607041,"lrc":"http://s.geci.me/lrc/311/31116/3111659.lrc","song":"海阔天空","artist_id":9208,"sid":3111659}]
     */

    private int count;
    /**
     * aid : 2848529
     * lrc : http://s.geci.me/lrc/344/34435/3443588.lrc
     * song : 海阔天空
     * artist_id : 2
     * sid : 3443588
     */

    private List<ResultBean> result;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String lrc;
        private String song;

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public String getSong() {
            return song;
        }

        public void setSong(String song) {
            this.song = song;
        }
    }


    @Override
    public String toString() {
        return "GeCiBean{" +
                "count=" + count +
                ", result=" + result +
                '}';
    }
}

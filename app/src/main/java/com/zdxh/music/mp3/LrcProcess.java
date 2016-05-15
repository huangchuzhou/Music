package com.zdxh.music.mp3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangchuzhou on 2016/5/8.
 * 处理歌词的类
 */
public class LrcProcess {
    //创建两个列表，一个存时间，一个存歌词，加入和读取列表时两个需要同步操作，
    //存放时间点数据
    private ArrayList<Long> timeMills = new ArrayList<>();
    //存放时间点所对应的歌词
    private ArrayList<String> messages = new ArrayList<>();
    //暂存时间点数据，因为有的一行有多个时间，需要先提取所有时间，最后提取歌词
    private ArrayList<Long> timeTemp = new ArrayList<>();
    //存放标题，词曲作者，专辑，歌词编辑人
    private ArrayList<String> mp3Titles = new ArrayList<>();
    //创建列表
    private ArrayList<ArrayList> queues = new ArrayList<>();

    private ArrayList<String> lrcList;	//List集合存放歌词内容对象
    private ArrayList<Long> timeList;	//List集合存放歌词时间对象
    public ArrayList<ArrayList> process(InputStream inputStream){

        try {
            //创建BufferdeReader对象
            InputStreamReader inputReader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            String temp = null;
            //行数
            int line = 0;
            //本行中时间点个数
            int count =0;
            //创建一个正则表达式对象用来匹配“[00:00.00]/[00:00:00]/[00:00]” “//("\\[([^\\]]+)\\]")”
            Pattern p = Pattern.compile("\\[\\s*[0-9]{1,2}\\s*:\\s*[0-5][0-9]\\s*[\\.:]?\\s*[0-9]?[0-9]?\\s*\\]");
            String result = null;
            String msg = null;
            boolean b = true;
            //一行一行读取
            while ((temp = bufferedReader.readLine()) != null){
                line++;
                if (line <= 4){
                    mp3Titles.add(temp.substring(4, temp.length() - 1));

                }
                //计算前清零
                count = 0;
                //清除暂存列表
                timeTemp.clear();
                //对一行进行匹配
                Matcher m = p.matcher(temp);
                while(m.find()){
                    count ++;
                    //获取匹配到的字段

                    String timeStr = m.group();
                    //根据匹配到的字段计算出时间
                    Long timeMill = time2Long(timeStr.substring(1,timeStr.length() - 1));
                    //加入列表
                    timeTemp.add(timeMill);
                }
                //如果存在时间点数据
                if(count > 0){
                    //按从小到大顺序插入时间点数据
                    for(int j = 0; j < timeTemp.size(); j++ ){
                        //如果列表为空，直接添加
                        if(timeMills.size() == 0){
                            //时间点数据添加到时间列表
                            timeMills.add(timeTemp.get(j));
                            //截取歌词字符串
                            //第一行多一个"]"需特殊处理
                            if(line == 1){
                                msg = temp.substring(10 * count + 1);
                            }else{
                                msg = temp.substring(10 * count);
                            }
                            result = "" + msg;
                            //添加到歌词列表
                            messages.add(result);
                        }
                        //如果时间大于列表中最后一个时间，直接添加到结尾
                        else if(timeTemp.get(j) > timeMills.get(timeMills.size() - 1)){
                            timeMills.add(timeTemp.get(j));
                            if (line == 1){
                                msg = temp.substring(10 * count + 1);
                            }else{
                                msg = temp.substring(10 * count);
                            }
                            result = "" + msg;
                            messages.add(result);
                        }
                        //否则按大小顺序插入
                        else{
                            for(int index = 0; index < timeMills.size(); index++ ){
                                if(timeTemp.get(j) <= timeMills.get(index)){
                                    timeMills.add(index, timeTemp.get(j));
                                    if(line == 1){
                                        msg = temp.substring(10 * count + 1);
                                    }else{
                                        msg = temp.substring(10 * count);
                                    }
                                    result = "" + msg;
                                    messages.add(index,result);
                                    break;
                                }
                            }
                        }
                    }
                }

            }
            queues.add(timeMills);
            queues.add(messages);
            queues.add(mp3Titles);

            lrcList = queues.get(1);
            timeList = queues.get(0);
            mp3Titles = queues.get(2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return queues;
    }
    /**
     * 将分钟，秒全部转换成毫秒
     * @param timeStr
     * @return
     */
    public Long time2Long(String timeStr){
        String s[] = timeStr.split(":");
        int min = Integer.parseInt(s[0]);

        int sec = 0;
        int mill = 0;
        //LRC文件支持三种不同的时间格式
        //mm:ss.ms
        //mm:ss:ms
        //mm:ss
        //如果格式为mm:ss:ms
        if (s.length > 2){
            sec = Integer.parseInt(s[1]);
            mill = Integer.parseInt(s[2]);

        }else{
            String ss[] = s[1].split("\\.");
            //如果格式为mm:ss.ms
            if(ss.length > 1){
                sec = Integer.parseInt(ss[0]);
                mill = Integer.parseInt(ss[1]);
            }
            //如果格式为mm:ss
            else{
                sec = Integer.parseInt(ss[0]);
                mill = 0;
            }
        }
        return min * 60*1000+sec*1000+mill*10L;
    }
    public ArrayList<String> getLrcList() {
        return lrcList;
    }
    public ArrayList<String> getMp3Titles() {
        return mp3Titles;
    }
    public ArrayList<Long> getTimeList() {
        return timeList;
    }


}





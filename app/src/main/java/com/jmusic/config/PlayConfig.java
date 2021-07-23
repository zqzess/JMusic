package com.jmusic.config;

import android.media.MediaPlayer;

import com.jmusic.bean.MusicInfo;
/**
 * @author: zqzess
 * 
 * @time: 2021/7/19 15:50
 *
 * @github: https://github.com/zqzess
**/
public class PlayConfig {
    public static long playingId;   //播放歌曲id
    public static boolean isPlaying=true;    //暂停
    public static int currentPosition=0;   //暂停时播放位置
    public static MediaPlayer mediaPlayer;
    public static boolean isSingle = true ; //单曲循环
    public static boolean isSingleList = false;   //列表循环
    public static boolean isRandomList = false;   //列表随机
    public static MusicInfo playNow;    //当前正在播放的音乐
    public static int playFlag=0;   /**0为无播放内容，1为有播放内容,3为启动app进入主页没有播放或主页控制暂停**/

    public static String timeAllCount()
    {
        int timems = mediaPlayer.getDuration();
        int times = timems/1000;
        int min = times/60;
        int s = times%60;
        String timeall = String.format("%02d:%02d",min,s);
        return timeall;
    }
    public static String timeEndCount()
    {
        int timems = mediaPlayer.getDuration(); //总时长
        int timeat = mediaPlayer.getCurrentPosition(); //已播放时长
        int lefttime = timems - timeat; //剩余时长
        int times = lefttime/1000;
        int min = times/60;
        int s = times%60;
        String timeend = String.format("- %02d:%02d",min,s);
        return timeend;
    }
    public static String timeChange(int timechange)
    {
        int timems = mediaPlayer.getDuration(); //总时长
        int lefttime = timems - timechange; //剩余时长
        int times = lefttime/1000;
        int min = times/60;
        int s = times%60;
        String timeend = String.format("- %02d:%02d",min,s);
        return timeend;
    }
}

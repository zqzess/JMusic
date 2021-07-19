package com.lib_common.bean;

public class NetString {
    private static String hostName="http://bd-api.kuwo.cn";

    //音乐资源
    //http://bd-api.kuwo.cn/api/service/resource/musicResource/6651913?uid=-1&token= HTTP/1.1
    public static String musicResource=hostName+"/api/service/resource/musicResource";

    //音乐信息
    //http://bd-api.kuwo.cn/api/service/music/6651913?uid=-1&token= HTTP/1.1
    public static String musicInfo=hostName+"/api/service/music";

    //推荐
    //http://bd-api.kuwo.cn/api/service/resource/recommend?uid=-1&token= HTTP/1.1
    //    /api/service/resource/recommend?uid=908709&token=18c04ab3d548b525a151ec78b39afb4d
    //http://bd-api.kuwo.cn/api/service/resource/recommendList?uid=-1&token= HTTP/1.1
    public static String musicRecommend=hostName+"/api/service/resource/recommend?uid=-1&token= HTTP/1.1";

    //歌词资源
    //http://bd-api.kuwo.cn/api/service/music/lyric/64847875?uid=-1&token= HTTP/1.1
//    public static String musicLyric=hostName+"/api/service/music/lyric";
    public static String musicLyric;

    //开屏
    //http://bd-api.kuwo.cn/api/service/openscreen/index?uid=-1&token= HTTP/1.1
    public static String openScreen=hostName+"/api/service/openscreen/index";

    //电台资源
    //http://bd-api.kuwo.cn/api/service/radio/scene_radio?uid=-1&token= HTTP/1.1
    public static String radio=hostName+"/api/service/radio/scene_radio";

    //欧美热歌榜
    //http://bd-api.kuwo.cn/api/service/finds/index?uid=-1&categoryId=97&token= HTTP/1.1
    //http://bd-api.kuwo.cn/api/service/category/97/musics?pn=0&rn=30&uid=-1&token= HTTP/1.1

    //华语热歌榜
    //http://bd-api.kuwo.cn/api/service/finds/index?uid=-1&categoryId=88&token= HTTP/1.1
    //http://bd-api.kuwo.cn/api/service/category/88/musics?pn=0&rn=30&uid=-1&token= HTTP/1.1

    //榜单
    //http://bd-api.kuwo.cn/api/service/category/list?uid=-1&token= HTTP/1.1

    //歌曲地址
    //http://bd-api.kuwo.cn/api/service/music/audioUrl/52510796 HTTP/1.1
//    public static String audioUrl=hostName+"/api/service/music/audioUrl";
    public static String audioUrl;

    public static String getMusicLyric() {
        return musicLyric;
    }

    public static void setMusicLyric(String musicLyric) {
        NetString.musicLyric = hostName+"/api/service/music/lyric"+musicLyric+"?uid=-1&token= HTTP/1.1";
    }

    public static String getAudioUrl() {
        return audioUrl;
    }

    public static void setAudioUrl(String audioUrl) {
        NetString.audioUrl = hostName+"/api/service/music/audioUrl"+audioUrl;
    }
}

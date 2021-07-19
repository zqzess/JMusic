package com.jmusic.util;


import java.util.HashMap;
import java.util.Map;

public class HeadersUtil {
    public static Map<String, String> MUSICINFO=new HashMap<String,String>();
    public static Map<String, String> MUSICURL=new HashMap<String,String>();

    public static void headersInit()
    {
        /**
         * 获取音乐信息
         *
         **/
        MUSICINFO.clear();
        MUSICINFO.put("user-agent","Dart/2.10 (dart:io)");
//        MUSICINFO.put("appuid","123849114427");
        MUSICINFO.put("appuid","123849114429"); //fake appuid
        MUSICINFO.put("plat","ar");
//        MUSICINFO.put("plat","ip");   //fake plat
        //MUSICINFO.put("devid","da1947619a05c9ea");
        MUSICINFO.put("devid","da1987619a07n9ya");//fake devid
        MUSICINFO.put("ver","1.1.9");
        //MUSICINFO.put("brand","Meizu 15");
        //MUSICINFO.put("channel","meizu");
        MUSICINFO.put("brand","huawei mate40");//fake brand
        MUSICINFO.put("channel","huawei");//fake channel
        MUSICINFO.put("content-length","0");
        MUSICINFO.put("api-ver","application/json");
//        MUSICINFO.put("net","wifi");
        MUSICINFO.put("net","mobile");
        MUSICINFO.put("host","bd-api.kuwo.cn");

        /**
         * 获取音乐链接
         *
         *
         **/

        MUSICURL.clear();
        MUSICURL.put("host","bd-api.kuwo.cn");
        MUSICURL.put("channel","appstore");//fake channel
        MUSICURL.put("plat","ip");
        MUSICURL.put("Accept-Language","zh-cn");
//        MUSICURL.put("Accept-Encoding","gzip, deflate");
        MUSICURL.put("devid","65D7A354-5t7A-4737-914E-9C8BA6JKFE4A");//fake devid
        MUSICURL.put("ver","1.2.3");
        MUSICURL.put("Cache-Control","no-cache");
        MUSICURL.put("user-agent","bodian/29 CFNetwork/1240.0.4 Darwin/20.5.0");
        MUSICURL.put("Connnection","keep-alive");
//        MUSICURL.put("user-agent","Dalvik/2.1.0 (Linux; U; Android 7.1.1; 15 Build/NGI77B)");
        MUSICURL.put("Accept","*/*");



    }

}

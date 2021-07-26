package com.lib_dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/7/26 9:56
 * desc   :
 * version: 1.0
 * project: JMusic
 */
@Entity
public class PlayListInfo {
    @Id(autoincrement = true)
    Long id;//数据库编号

    long musicId;   //歌曲id
    String songName;    //曲名
    String artist;    //歌手
    long ArtistId;    //歌手id
    String album;   //专辑
    String albumPic;    //专辑图片
    String musicLink;   //歌曲链接
    long albumId;   //专辑id
    int isMv;   //是否mv，0不是，1是
    String form;    //歌单
    @Generated(hash = 1018667764)
    public PlayListInfo(Long id, long musicId, String songName, String artist,
            long ArtistId, String album, String albumPic, String musicLink,
            long albumId, int isMv, String form) {
        this.id = id;
        this.musicId = musicId;
        this.songName = songName;
        this.artist = artist;
        this.ArtistId = ArtistId;
        this.album = album;
        this.albumPic = albumPic;
        this.musicLink = musicLink;
        this.albumId = albumId;
        this.isMv = isMv;
        this.form = form;
    }
    @Generated(hash = 759288294)
    public PlayListInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getMusicId() {
        return this.musicId;
    }
    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }
    public String getSongName() {
        return this.songName;
    }
    public void setSongName(String songName) {
        this.songName = songName;
    }
    public String getArtist() {
        return this.artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public long getArtistId() {
        return this.ArtistId;
    }
    public void setArtistId(long ArtistId) {
        this.ArtistId = ArtistId;
    }
    public String getAlbum() {
        return this.album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getAlbumPic() {
        return this.albumPic;
    }
    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }
    public String getMusicLink() {
        return this.musicLink;
    }
    public void setMusicLink(String musicLink) {
        this.musicLink = musicLink;
    }
    public long getAlbumId() {
        return this.albumId;
    }
    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
    public int getIsMv() {
        return this.isMv;
    }
    public void setIsMv(int isMv) {
        this.isMv = isMv;
    }
    public String getForm() {
        return this.form;
    }
    public void setForm(String form) {
        this.form = form;
    }
}


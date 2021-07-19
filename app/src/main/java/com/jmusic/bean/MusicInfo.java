package com.jmusic.bean;

public class MusicInfo {
    long id; //音乐id
    String name;
    long albumId;
    String album;
    String albumPic;
    String albumPic120;
    String artist;
    long artistId;
    String artistPic;
    int duration;
    String releaseDate;
    int isMv;

    public MusicInfo(long id, String name, long albumId, String album, String albumPic, String albumPic120, String artist, long artistId, String artistPic, int duration, String releaseDate, int isMv) {
        this.id = id;
        this.name = name;
        this.albumId = albumId;
        this.album = album;
        this.albumPic = albumPic;
        this.albumPic120 = albumPic120;
        this.artist = artist;
        this.artistId = artistId;
        this.artistPic = artistPic;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.isMv = isMv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public String getAlbumPic120() {
        return albumPic120;
    }

    public void setAlbumPic120(String albumPic120) {
        this.albumPic120 = albumPic120;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getArtistPic() {
        return artistPic;
    }

    public void setArtistPic(String artistPic) {
        this.artistPic = artistPic;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getIsMv() {
        return isMv;
    }

    public void setIsMv(int isMv) {
        this.isMv = isMv;
    }
}

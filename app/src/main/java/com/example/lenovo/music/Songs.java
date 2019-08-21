package com.example.lenovo.music;

public class Songs {
    private  long id; //歌曲的id
    private String title;//歌名
    private String artist;//歌手
    private long duration;//歌曲长度
    private String url;//歌曲的地址


    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }


}

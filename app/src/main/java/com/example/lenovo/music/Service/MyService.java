package com.example.lenovo.music.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.lenovo.music.LocalMusic.LocalMusicList;
import com.example.lenovo.music.MainActivity;
import com.example.lenovo.music.Songs;

import java.io.Serializable;
import java.util.ArrayList;

public class MyService extends Service {
    private MusicBinder mBinder=new MusicBinder();
    private MediaPlayer mediaPlayer=null;
    public class MusicBinder extends Binder implements Serializable {
        private ArrayList<Songs> musicList;
        private int index;
       // private int currentPosition=index;
        private boolean orderPlay=true;
        private boolean cyclePlay=false;
        private boolean randomPlay=false;
        private Songs songsToMain;
        private boolean isLikeMusicList;//用于判断是否是播放在我喜欢的ListView中的
        private LocalMusicList.LocalMusicFragmentLister lister;
        private boolean isRecentMusicList=false;
        private MainActivity.MainActivityLister mainActivityLister;
        private boolean isMusicContentPage=false;
        public void startPlay(String url){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            try {
                mediaPlayer.setDataSource(url);
                Log.e("URL:",url);
                mediaPlayer.prepare();
            }catch (Exception e){}
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if(cyclePlay){
                        if(musicList!=null){
                            startPlay(musicList.get(index).getUrl());
                            lister.changeButtomInfo(musicList.get(index));
                        }else {
                            mediaPlayer.reset();
                            Toast.makeText(MyService.this,"已播放完当前列表，请重新选择需要播放的音乐！！！",Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        if(musicList!=null){
                            nextSong();
                            Log.e("MySrvice:","已自动下一首");
                            lister.changeButtomInfo(musicList.get(index));
                        }else {
                            mediaPlayer.reset();
                            Toast.makeText(MyService.this,"已播放完当前列表，请重新选择需要播放的音乐！！！",Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return true;
                }
            });
        }
        //下一曲
        public void nextSong(){
            if(musicList!=null){
                mediaPlayer.reset();
                if(cyclePlay||orderPlay){
                    index=index+1;
                    if(index>musicList.size()-1){
                        index=0;
                    }
                    songsToMain=musicList.get(index);
                    startPlay( musicList.get(index).getUrl());
                }else if(randomPlay){
                    index=(int)(Math.random()*musicList.size());
                    if(index>musicList.size()-1){
                        index=0;
                    }else {
                        songsToMain=musicList.get(index);
                        startPlay( musicList.get(index).getUrl());
                    }
                }
                if(isMusicContentPage){
                    mainActivityLister.changeImage(musicList.get(index));
                }
            }else {
                Toast.makeText(MyService.this,"请选择需要播放的歌曲！！！",Toast.LENGTH_SHORT).show();
            }


        }
        //上一曲
        public void lastSong(){
            if(musicList!=null){
                mediaPlayer.reset();
                if(cyclePlay||orderPlay){
                    index=index-1;
                    if(index<0){
                        index=musicList.size()-1;
                    }
                    songsToMain=musicList.get(index);
                    startPlay(musicList.get(index).getUrl() );
                }else if(randomPlay){
                    index=(int)(Math.random()*musicList.size());
                    if(index>musicList.size()-1){
                        index=0;
                    }else {
                        songsToMain=musicList.get(index);
                        startPlay( musicList.get(index).getUrl());
                    }
                }
                if(isMusicContentPage){
                    mainActivityLister.changeImage(musicList.get(index));
                }
            }else {
                Toast.makeText(MyService.this,"请选择需要播放的歌曲！！！",Toast.LENGTH_SHORT).show();
            }

        }


        //暂停音乐
        public void pauseMusic(){
            mediaPlayer.pause();
        }
        //暂停后再播放音乐
        public void startMusic(){
            mediaPlayer.start();
        }
        //活动条活动的时候调用的方法
        public void seekToPosition(int position){
            mediaPlayer.seekTo(position);
            mediaPlayer.start();
            Log.e("MyService:" ,"滑动到这里");
        }

        public boolean isPlaying(){return mediaPlayer.isPlaying();}
        public void  setMusicList(ArrayList<Songs> musicList){this.musicList=musicList;}
        public ArrayList<Songs> getMusicList(){return musicList;}
        public void setIndex(int index) { this.index = index; }
        //播放模式
        public void setOrderPlay(boolean orderPlay){this.orderPlay=orderPlay;}

        public void setCyclePlay(boolean cyclePlay) { this.cyclePlay = cyclePlay; }
        public void setRandomPlay(boolean randomPlay){this.randomPlay=randomPlay;}
        public Songs getSongsToMain(){return songsToMain;}
        //获取当前的播放的位置
        public int getCurrentPosition(){
            return mediaPlayer.getCurrentPosition();
        }
        public void setLister(LocalMusicList.LocalMusicFragmentLister lister){ this.lister=lister;}
        public void setIsLikeMusicList(boolean isLikeMusicList){this.isLikeMusicList=isLikeMusicList;}
        public boolean getIsLikeMusicList(){return  isLikeMusicList;}
        public void setMainActivityLister(MainActivity.MainActivityLister mainActivityLister){
            this.mainActivityLister=mainActivityLister;
        }
        public void setIsMusicContentPage(boolean isMusicContentPage){
            this.isMusicContentPage=isMusicContentPage;
        }
        public void setIsRecentMusicList(boolean isRecentMusicList){
            this.isRecentMusicList=isRecentMusicList;
        }
        public boolean getIsRecentMusicList(){return isRecentMusicList;}
        public boolean getIsMusicContentPage(){return  isMusicContentPage;}


    }

    public MyService() {
        if (mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}
